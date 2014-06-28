package la.service.wf.action;

import com.mingdao.api.entity.Req;
import com.mingdao.api.la.RequestLA;
import la.service.common.entity.Article;
import la.service.common.entity.NewsRspMessage;
import la.service.common.entity.RspMessage;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "req", namespace = "/wf")
public class WfReqAction extends BaseAction {

    @ReqGet
    private String laToken;

    @ReqGet
    private String eventKey;

    @ReqGet
    private String userId;

    private Map<String, String> requestMap;

    @Override
    public String execute() throws Exception {
        String content = null;
        System.out.println("-------WfReqAction userId="+userId);
        System.out.println("-------WfReqAction laToken="+laToken);
        System.out.println("-------WfReqAction eventKey="+eventKey);
        if (StringUtils.isNotBlank(eventKey) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(laToken)) {
            requestMap = WxParamsUtils.get(userId);
            System.out.print("-------requestMap  from map ="+requestMap);
            if (requestMap != null && !requestMap.isEmpty()) {
                if (eventKey.equals("APPLY_EXECUTION")) {//申请-执行中
                    System.out.println("-------进入到执行中if语句中 =");
                    List<Req> reqList = RequestLA.reqIngList(laToken);
                    System.out.println("-------进入到执行中if语句中获取集合 ="+reqList);
                    if (reqList != null && !reqList.isEmpty()) {
                        NewsRspMessage newsMessage = MessageUtils.buildRspMessage(requestMap, NewsRspMessage.class);
                        List<Article> articles = _requestList(reqList);
                        newsMessage.setArticleCount(articles.size());
                        newsMessage.setArticles(articles);
                        content = MessageUtils.messageToXml(newsMessage);
                    }else{
                        TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                        textRspMessage.setContent("未找到相关记录!");
                        content = MessageUtils.messageToXml(textRspMessage);
                    }
                } else if (eventKey.equals("APPLY_COMPLETED")) {//申请-已完成

                } else if (eventKey.equals("APPLY_NEW")) {//申请-新建

                }
            }
        }
        /*TextRspMessage message = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
        String respContent = "";
        if (eventKey.equals("APPLY_EXECUTION")) {//申请-执行中
            respContent = "申请-执行中菜单";
        } else if (eventKey.equals("APPLY_COMPLETED")) {//申请-已完成
            respContent = "申请-已完成菜单";
        } else if (eventKey.equals("APPLY_NEW")) {//申请-新建
            respContent = "申请-新建菜单";
        } else if (eventKey.equals("APPROVE_EXECUTION")) {//审批-进行中
            respContent = "审批-进行中菜单";
        } else if (eventKey.equals("APPROVE_COMPLETED")) {//审批-已完成
            respContent = "审批-已完成菜单";
        } else if (eventKey.equals("EXECUTION_WAITING")) {//执行-等待
            respContent = "执行等待";
        } else if (eventKey.equals("EXECUTION_COMPLETED")) {//执行-完毕
            respContent = "执行完毕";
        }*/
        writeJsonByAction(content);
        return null;
    }

    private List<Article> _requestList(List<Req> reqList) {
        List<Article> articles = new ArrayList<Article>();
        if (reqList != null && !reqList.isEmpty()) {
            if (reqList.size() > 1) {
                Article article = new Article();//图文混排
                article.setTitle("查看更多");
                article.setDescription("查看更多");
                article.setPicUrl("http://y2.ifengimg.com/b4c1e3c5e4848389/2014/0627/rdn_53acb1b0d5924.jpg");
                article.setUrl("http://www.baidu.com");
                articles.add(article);
            }
            for (Req req : reqList) {
                Article article = new Article();//图文混排
                article.setTitle(req.getApplyName());
                article.setDescription(req.getReqNo());
                article.setPicUrl("http://y2.ifengimg.com/b4c1e3c5e4848389/2014/0627/rdn_53acb1b0d5924.jpg");
                article.setUrl("http://www.baidu.com");
                articles.add(article);
            }
        }

        return articles;
    }
}
