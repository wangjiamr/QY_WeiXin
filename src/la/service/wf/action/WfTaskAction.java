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
                String msg = "未找到相关记录!";
                if (eventKey.equals("TASK_ING")) {//我的审批-未执行
                    page = RequestLA.taskIngList(laToken, 5);
                   // msg = "我的审批-未审批:未找到相关记录!";
                } else if (eventKey.equals("TASK_HISTORY")) {//我的审批-已执行
                    page = RequestLA.taskHistoryList(laToken, 5);
                   // msg = "我的审批-已审批:未找到相关记录!";
                }
                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    textRspMessage.setContent(buildList(page, msg));
                } else {
                    textRspMessage.setContent(msg);
                }
                content = MessageUtils.messageToXml(textRspMessage);
            }
        }
        writeXmlByAction(content);
        return null;
    }

    private String buildList(Page<Req> page) {
        return buildList(page, null);
    }

    private String buildList(Page<Req> page, String defaultStr) {
        if (page != null) {
            List<Req> reqList = page.getResultList();
            if (reqList != null && !reqList.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("您当前有").append("10").append("未处理\n\n");
                String blank = padding(12);
                for (int x = 0; x < reqList.size(); x++) {
                    Req req = reqList.get(x);
                    String dateStr = "";
                    if (StringUtils.isNotBlank(req.getReceiveDate())) {
                        Date sendDate = DateFormatUtil.parse(req.getReceiveDate(), DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        dateStr = DateFormatUtil.format(sendDate, "yyyy/MM/dd");
                    }
                    buffer.append(req.getUserName()+"发起的");
                    buffer.append(req.getApplyName() + "\n");
                    buffer.append(dateStr);
                    buffer.append(blank);
                    buffer.append("<a href=\"www.baidu.com?x=" + x + "\">查看</a>\n");
                    if (x + 1 != reqList.size()) {
                        buffer.append(splitLine(blank + dateStr));
                    }
                    buffer.append("\n");

                }
                if (page.isHasNextPage()) {
                    buffer.append("<a href=\"www.baidu.com?more\">查看更多</a>");
                }
                return buffer.toString();
            }
        }
        if (StringUtils.isNotBlank(defaultStr)) {
            return defaultStr;
        }
        return "没有找到数据!";
    }

    private String padding(int length) {
        if (length < -1) return "";
        StringBuilder blank = new StringBuilder();
        for (int x = 0; x < length; x++) {
            blank.append(" ");
        }
        return blank.toString();
    }

    private String splitLine(String str) {
        if (StringUtils.isNotBlank(str)) {
            StringBuilder sb = new StringBuilder();
            for (char b : str.toCharArray()) {
                sb.append("-");
            }
            return sb.toString();
        }
        return "";
    }
}
