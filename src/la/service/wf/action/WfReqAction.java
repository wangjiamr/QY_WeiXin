package la.service.wf.action;

import com.mingdao.api.entity.Page;
import com.mingdao.api.entity.Req;
import com.mingdao.api.entity.ReqViewEntity;
import com.mingdao.api.la.RequestLA;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
import la.service.web.support.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "req", namespace = "/wf")
public class WfReqAction extends ActionSupport {

    @ReqGet
    @ReqSet
    private String laToken;

    @ReqGet
    private String eventKey;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    private String userId;

    private Map<String, String> requestMap;

    @ReqSet
    private ReqViewEntity reqViewEntity;

    @Override
    public String execute() throws Exception {
        String content = null;
        if (StringUtils.isNotBlank(eventKey) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(laToken)) {
            requestMap = WxParamsUtils.get(userId);
            if (requestMap != null && !requestMap.isEmpty()) {
                Page<Req> page = null;
                String defaultMsg = "未找到相关记录!";
                String title = null;
                if (eventKey.equals("REQ_ING")) {//申请-进行中
                    page = RequestLA.reqIngList(laToken, 5);
                    title = "进行中";
                } else if (eventKey.equals("REQ_HISTORY")) {//申请-已完成
                    page = RequestLA.reqHistoryList(laToken, 5);
                    title = "已完成";
                } else if (eventKey.equals("REQ_CONFIRM")) {//申请-待确认
                    page = RequestLA.reqConfirmList(laToken, 5);
                    title = "待确认";
                } else if (eventKey.equals("SEND_REQ")) {//申请-新建
                    defaultMsg = "<a href=\"http://itunes.apple.com/cn/app/ming-dao/id468630782?mt=8\">请先下载明道App!</a>";
                }

                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    String str = buildMessageContent(page, title, eventKey, "REQ", laToken);
                    textRspMessage.setContent(str);
                } else {
                    textRspMessage.setContent(defaultMsg);
                }
                content = MessageUtils.messageToXml(textRspMessage);
            }
        }
        writeJsonByAction(content);
        return null;
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/req/ingList.ftl", type = Dispatcher.FreeMarker)
    })
    public String ing() throws Exception {
        return "success";
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/req/historyList.ftl", type = Dispatcher.FreeMarker)
    })
    public String history() throws Exception {
        return "success";
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/req/confirmList.ftl", type = Dispatcher.FreeMarker)
    })
    public String confirm() throws Exception {
        return "success";
    }

    public String ingList() throws Exception {
        List<String> iconList = new ArrayList<String>();
        iconList.add("ico-06.png");
        iconList.add("ico-07.png");
        iconList.add("ico-08.png");
        iconList.add("ico-09.png");

        Random random = new Random();//指定种子数100

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        if (StringUtils.isNotBlank(laToken)) {
            Page<Req> reqPage = RequestLA.reqIngList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getId());
                        reqObj.put("tip", req.getTip());
                        reqObj.put("result", req.getResult());
                        reqObj.put("reqNo", req.getReqNo());
                        reqObj.put("userName", req.getUserName());
                        reqObj.put("applyName", req.getApplyName());
                        reqObj.put("sendDate", req.getSendDate());
                        reqObj.put("icon", iconList.get(random.nextInt(4)));
                        jsonArray.add(reqObj);
                    }
                }
                JSONObject pageJson = new JSONObject();
                pageJson.put("currentPage", reqPage.getCurrentPage());
                pageJson.put("hasNextPage", reqPage.isHasNextPage());
                pageJson.put("nextIndex", reqPage.getNextIndex());
                pageJson.put("totalRecord", reqPage.getTotalRecord());
                jsonObject.put("reqList", jsonArray);
                jsonObject.put("result", "0");
                jsonObject.put("page", pageJson);
            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public String historyList() throws Exception {
        List<String> iconList = new ArrayList<String>();
        iconList.add("ico-06.png");
        iconList.add("ico-07.png");
        iconList.add("ico-08.png");
        iconList.add("ico-09.png");

        Random random = new Random();//指定种子数100
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        if (StringUtils.isNotBlank(laToken)) {
            Page<Req> reqPage = RequestLA.reqHistoryList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getId());
                        reqObj.put("tip", req.getTip());
                        reqObj.put("result", req.getResult());
                        reqObj.put("reqNo", req.getReqNo());
                        reqObj.put("userName", req.getUserName());
                        reqObj.put("applyName", req.getApplyName());
                        reqObj.put("sendDate", req.getSendDate());
                        reqObj.put("icon", iconList.get(random.nextInt(4)));
                        jsonArray.add(reqObj);
                    }
                }
                JSONObject pageJson = new JSONObject();
                pageJson.put("currentPage", reqPage.getCurrentPage());
                pageJson.put("hasNextPage", reqPage.isHasNextPage());
                pageJson.put("nextIndex", reqPage.getNextIndex());
                pageJson.put("totalRecord", reqPage.getTotalRecord());
                jsonObject.put("reqList", jsonArray);
                jsonObject.put("result", "0");
                jsonObject.put("page", pageJson);
            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public String confirmList() throws Exception {
        List<String> iconList = new ArrayList<String>();
        iconList.add("ico-06.png");
        iconList.add("ico-07.png");
        iconList.add("ico-08.png");
        iconList.add("ico-09.png");

        Random random = new Random();//指定种子数100
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        if (StringUtils.isNotBlank(laToken)) {
            Page<Req> reqPage = RequestLA.reqConfirmList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getId());
                        reqObj.put("tip", req.getTip());
                        reqObj.put("result", req.getResult());
                        reqObj.put("reqNo", req.getReqNo());
                        reqObj.put("userName", req.getUserName());
                        reqObj.put("applyName", req.getApplyName());
                        reqObj.put("sendDate", req.getSendDate());
                        reqObj.put("icon", iconList.get(random.nextInt(4)));
                        jsonArray.add(reqObj);
                    }
                }
                JSONObject pageJson = new JSONObject();
                pageJson.put("currentPage", reqPage.getCurrentPage());
                pageJson.put("hasNextPage", reqPage.isHasNextPage());
                pageJson.put("nextIndex", reqPage.getNextIndex());
                pageJson.put("totalRecord", reqPage.getTotalRecord());
                jsonObject.put("reqList", jsonArray);
                jsonObject.put("result", "0");
                jsonObject.put("page", pageJson);
            }
        }
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/req/view.ftl", type = Dispatcher.FreeMarker)
    })
    public String view() throws Exception {
        if (StringUtils.isNotBlank(laToken) && id != null) {
            reqViewEntity = RequestLA.view(laToken, id);
        }
        return "success";
    }
}
