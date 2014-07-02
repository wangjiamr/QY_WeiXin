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
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;

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
                    String str = buildMessageContent(page, title, eventKey,true,laToken);
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
}
