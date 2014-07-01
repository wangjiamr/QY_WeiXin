package la.service.wf.action;

import com.mingdao.api.entity.Page;
import com.mingdao.api.entity.Req;
import com.mingdao.api.la.RequestLA;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
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
        if (StringUtils.isNotBlank(eventKey) && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(laToken)) {
            requestMap = WxParamsUtils.get(userId);
            if (requestMap != null && !requestMap.isEmpty()) {
                Page<Req> page = null;
                String defaultMsg = "未找到相关记录!";
                if (eventKey.equals("REQ_ING")) {//申请-进行中
                    page = RequestLA.reqIngList(laToken, 5);
                    defaultMsg = "我的申请-进行中:未找到相关记录!";
                } else if (eventKey.equals("REQ_HISTORY")) {//申请-已完成
                    page = RequestLA.reqHistoryList(laToken, 5);
                    defaultMsg = "我的申请-已完成:未找到相关记录!";
                } else if (eventKey.equals("REQ_CONFIRM")) {//申请-待确认
                    page = RequestLA.reqConfirmList(laToken, 5);
                    defaultMsg = "我的申请-待确认:未找到相关记录!";
                } else if (eventKey.equals("SEND_REQ")) {//申请-新建
                    defaultMsg = "<a href=\"http://itunes.apple.com/cn/app/ming-dao/id468630782?mt=8\">请先下载明道App!</a>";
                }

                TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                if (page != null) {
                    String str = buildList(page,defaultMsg);
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
                    if (StringUtils.isNotBlank(req.getSendDate())) {
                        Date sendDate = DateFormatUtil.parse(req.getSendDate(), DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        dateStr = DateFormatUtil.format(sendDate, "yyyy/MM/dd");
                    }
                    buffer.append("我发起的");
                    buffer.append(req.getApplyName()+"\n");
                    buffer.append(dateStr);
                    buffer.append(blank);
                    buffer.append(" <a href=\"http://www.baidu.com?x="+x+"\">查看</a>\n");
                    if (x + 1 != reqList.size()) {
                        buffer.append(splitLine(dateStr));
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
