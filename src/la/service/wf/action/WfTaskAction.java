package la.service.wf.action;

import com.mingdao.api.entity.Page;
import com.mingdao.api.entity.Req;
import com.mingdao.api.la.RequestLA;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
import la.service.web.support.ActionSupport;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjia on 14-6-28.
 */
@Action(name = "task", namespace = "/wf")
public class WfTaskAction extends ActionSupport {

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
        if (StringUtils.isNotBlank(eventKey) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(laToken)) {
            requestMap = WxParamsUtils.get(userId);
            if (requestMap != null && !requestMap.isEmpty()) {
                Page<Req> page = null;
                String title = "";
                String msg = "未找到相关记录!";
                if (eventKey.equals("TASK_ING")) {//我的审批-未执行
                    page = RequestLA.taskIngList(laToken, 5);
                    title = "未执行";
                } else if (eventKey.equals("TASK_HISTORY")) {//我的审批-已执行
                    page = RequestLA.taskHistoryList(laToken, 5);
                    title = "已执行";
                }
                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    String str = buildMessageContent(page, title, eventKey,false,laToken);
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

}
