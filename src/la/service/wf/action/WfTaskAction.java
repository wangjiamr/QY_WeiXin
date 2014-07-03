package la.service.wf.action;

import com.mingdao.api.entity.Page;
import com.mingdao.api.entity.Req;
import com.mingdao.api.entity.ReqViewEntity;
import com.mingdao.api.la.RequestLA;
import la.service.common.UserInfo;
import la.service.common.UserSession;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
import la.service.web.support.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.FileIdUtils;
import org.guiceside.commons.TokenUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.*;

import java.util.*;

/**
 * Created by wangjia on 14-6-28.
 */
@Action(name = "task", namespace = "/wf")
public class WfTaskAction extends ActionSupport {

    @ReqGet
    @ReqSet
    private String laToken;

    @ReqGet
    private String eventKey;

    @ReqGet
    private String reason;

    @ReqGet
    private String userId;

    private Map<String, String> requestMap;


    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    @ReqSet
    private Long reqId;

    @ReqGet
    @ReqSet
    private Integer approveIdea;




    @ReqSet
    private ReqViewEntity reqViewEntity;


    @Override
    public String execute() throws Exception {
        String content = null;
        if (StringUtils.isNotBlank(eventKey) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(laToken)) {
            requestMap = WxParamsUtils.get(userId);
            if (requestMap != null && !requestMap.isEmpty()) {
                Page<Req> page = null;
                String title = "";
                String msg = "未找到相关记录!";
                if (eventKey.equals("TASK_ING")) {//我的审批-未执行
                    page = RequestLA.taskIngList(laToken, 5);
                    title = "未审批";
                } else if (eventKey.equals("TASK_HISTORY")) {//我的审批-已执行
                    page = RequestLA.taskHistoryList(laToken, 5);
                    title = "已审批";
                }
                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    String str = buildMessageContent(page, title, eventKey,"TASK",laToken);
                    textRspMessage.setContent(str);
                } else {
                    textRspMessage.setContent(msg);
                }
                content = MessageUtils.messageToXml(textRspMessage);
            }
        }
        writeXmlByAction(content);
        return null;
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/task/ingList.ftl", type = Dispatcher.FreeMarker)
    })
    public String ing() throws Exception {
        return "success";
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/task/historyList.ftl", type = Dispatcher.FreeMarker)
    })
    public String history() throws Exception {
        return "success";
    }

    public String ingList() throws Exception {
        List<String> iconList = new ArrayList<String>();
        iconList.add("ico-06.png");
        iconList.add("ico-07.png");
        iconList.add("ico-08.png");
        iconList.add("ico-09.png");

        Random random = new Random();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        if (StringUtils.isNotBlank(laToken)) {
            Page<Req> reqPage = RequestLA.taskIngList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getTaskId());
                        reqObj.put("reqId", req.getId());
                        reqObj.put("tip", req.getTip());
                        reqObj.put("result", req.getResult());
                        reqObj.put("reqNo", req.getReqNo());
                        reqObj.put("userName", req.getUserName());
                        reqObj.put("applyName", req.getApplyName());
                        reqObj.put("receiveDate", req.getReceiveDate());
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
            Page<Req> reqPage = RequestLA.taskHistoryList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getTaskId());
                        reqObj.put("reqId", req.getId());
                        reqObj.put("tip", req.getTip());
                        reqObj.put("result", req.getResult());
                        reqObj.put("reqNo", req.getReqNo());
                        reqObj.put("userName", req.getUserName());
                        reqObj.put("applyName", req.getApplyName());
                        reqObj.put("receiveDate", req.getReceiveDate());
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
            @Result(name = "success", path = "/view/wf/task/view.ftl", type = Dispatcher.FreeMarker)
    })
    public String view() throws Exception {
        if (StringUtils.isNotBlank(laToken) && id != null&& reqId != null) {
            reqViewEntity = RequestLA.view(laToken, reqId);
            reqViewEntity.setTaskId(id);
        }
        return "success";
    }


    @PageFlow(result = {@Result(name = "success", path = "/view/wf/task/reason.ftl", type = Dispatcher.FreeMarker)})
    public String reason() throws Exception {
        if (StringUtils.isNotBlank(laToken)&&id != null&&approveIdea!=null) {

        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @PageFlow(result = {@Result(name = "success", path = "/wf/task/ing?laToken=${laToken}", type = Dispatcher.Redirect)})
    public String approve() throws Exception {
        if (StringUtils.isNotBlank(laToken)&&id != null&&approveIdea!=null) {
            boolean approveFlag=RequestLA.taskApprove(laToken, id,approveIdea,reason);
            System.out.println(approveFlag);
        }
        return "success";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
