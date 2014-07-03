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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by wangjia on 14-6-28.
 */
@Action(name = "manage", namespace = "/wf")
public class WfManageAction extends ActionSupport {
    @ReqGet
    @ReqSet
    private String laToken;

    @ReqGet
    private String eventKey;

    @ReqGet
    private String userId;

    private Map<String, String> requestMap;

    @ReqGet
    @ReqSet
    private Long reqId;

    @ReqGet
    @ReqSet
    private Long id;


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
                String title = "";
                if (eventKey.equals("MANAGE_ING")) {//我的经办-未执行
                    page = RequestLA.manageIngList(laToken, 5);
                    title = "未执行";
                } else if (eventKey.equals("MANAGE_HISTORY")) {//我的经办-已完成
                    page = RequestLA.manageHistoryList(laToken, 5);
                    title = "已完成";
                }
                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    String str = buildMessageContent(page, title, eventKey, "MANAGE", laToken);
                    textRspMessage.setContent(str);
                } else {
                    textRspMessage.setContent(defaultMsg);
                }
                content = MessageUtils.messageToXml(textRspMessage);
            }
        }
        writeXmlByAction(content);
        return null;
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/manage/ingList.ftl", type = Dispatcher.FreeMarker)
    })
    public String ing() throws Exception {
        return "success";
    }

    @PageFlow(result = {
            @Result(name = "success", path = "/view/wf/manage/historyList.ftl", type = Dispatcher.FreeMarker)
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
            Page<Req> reqPage = RequestLA.manageIngList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getManageId());
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
            Page<Req> reqPage = RequestLA.manageHistoryList(laToken, 15);
            if (reqPage != null) {
                List<Req> reqList = reqPage.getResultList();
                JSONArray jsonArray = new JSONArray();
                if (reqList != null && !reqList.isEmpty()) {
                    for (Req req : reqList) {
                        JSONObject reqObj = new JSONObject();
                        reqObj.put("id", req.getManageId());
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
            @Result(name = "success", path = "/view/wf/manage/view.ftl", type = Dispatcher.FreeMarker)
    })
    public String view() throws Exception {
        if (StringUtils.isNotBlank(laToken) && id != null&& reqId != null) {
            reqViewEntity = RequestLA.view(laToken, reqId);
            reqViewEntity.setManageId(id);
        }
        return "success";
    }

}
