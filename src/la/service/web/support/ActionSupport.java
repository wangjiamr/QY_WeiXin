package la.service.web.support;


import com.mingdao.api.entity.Page;
import com.mingdao.api.entity.Req;
import la.service.common.UserInfo;
import la.service.common.UserSession;
import ognl.NoSuchPropertyException;
import ognl.OgnlException;
import org.guiceside.commons.lang.BeanUtils;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.persistence.entity.Tracker;
import org.guiceside.web.action.BaseAction;

import java.util.Date;
import java.util.List;


public abstract class ActionSupport extends BaseAction {
    protected String buildMessageContent(Page<Req> page, String title, String type, String reqType, String laToken) throws Exception {
        if (page != null) {
            String detailURL = null;
            String moreUrl = null;
            if (type.contains("REQ")) {
                detailURL = "http://qywx.mingdao.com/wf/req/view?laToken=" + laToken + "&id=";
            } else if (type.contains("TASK")) {
                detailURL = "http://qywx.mingdao.com/wf/task/view?laToken=" + laToken + "&id=";
            } else if (type.contains("MANAGE")) {
                detailURL = "http://qywx.mingdao.com/wf/manage/view?laToken=" + laToken + "&id=";
            }
            if (type.equals("REQ_ING")) {
                moreUrl = "http://qywx.mingdao.com/wf/req/ing?laToken=" + laToken;
            } else if (type.equals("REQ_HISTORY")) {
                moreUrl = "http://qywx.mingdao.com/wf/req/history?laToken=" + laToken;
            } else if (type.equals("REQ_CONFIRM")) {
                moreUrl = "http://qywx.mingdao.com/wf/req/confirm?laToken=" + laToken;
            } else if (type.equals("TASK_ING")) {
                moreUrl = "http://qywx.mingdao.com/wf/task/ing?laToken=" + laToken;
            } else if (type.equals("TASK_HISTORY")) {
                moreUrl = "http://qywx.mingdao.com/wf/task/history?laToken=" + laToken;
            } else if (type.equals("MANAGE_ING")) {
                moreUrl = "http://qywx.mingdao.com/wf/manage/ing?laToken=" + laToken;
            } else if (type.equals("MANAGE_HISTORY")) {
                moreUrl = "http://qywx.mingdao.com/wf/manage/history?laToken=" + laToken;
            }
            List<Req> reqList = page.getResultList();
            if (reqList != null && !reqList.isEmpty()) {
                StringBuilder buffer = new StringBuilder("您当前有"+page.getTotalRecord()+"条" + title + "\n\n");
                for (int x = 0; x < reqList.size(); x++) {
                    Req req = reqList.get(x);
                    String dateStr = reqType.equals("REQ") ? req.getSendDate() : req.getReceiveDate();
                    String userName = StringUtils.isBlank(req.getUserName()) ? "" : req.getUserName();
                    String applyName = StringUtils.isBlank(req.getApplyName()) ? "" : req.getApplyName();
                    if (StringUtils.isNotBlank(dateStr)) {
                        Date sendDate = DateFormatUtil.parse(dateStr, DateFormatUtil.YEAR_MONTH_DAY_PATTERN);
                        dateStr = DateFormatUtil.format(sendDate, "yyyy/MM/dd");
                        buffer.append(dateStr);
                        buffer.append(padding(11, " "));
                        if (reqType.equals("REQ")) {
                            buffer.append("<a href=\"" + detailURL + req.getId() + "\">查看</a>");
                        }else if (reqType.equals("TASK")){
                            buffer.append("<a href=\"" + detailURL + req.getTaskId() +"&reqId="+req.getId()+ "\">查看</a>");
                        }else if (reqType.equals("MANAGE")){
                            buffer.append("<a href=\"" + detailURL + req.getManageId() +"&reqId="+req.getId()+ "\">查看</a>");
                        }
                        buffer.append("\n");
                    }
                    if (reqType.equals("REQ")) {
                        buffer.append(" ");
                        buffer.append(applyName + "\n");
                    } else {
                        buffer.append("  " + userName).append("  ");
                        buffer.append(applyName + "\n");
                    }
                    if (x + 1 != reqList.size()) {
                        buffer.append(padding(36, "."));
                        buffer.append("\n");
                    }
                }
                if (page.isHasNextPage()) {
                    buffer.append("\n<a href=\"" + moreUrl + "\">查看更多</a>");
                }
                return buffer.toString();
            }
        }
        return "没有找到数据!";
    }

    private String padding(int length, String paddingStr) {
        if (length < -1) return "";
        if (StringUtils.isBlank(paddingStr)) {
            paddingStr = " ";
        }
        StringBuilder blank = new StringBuilder();
        for (int x = 0; x < length; x++) {
            blank.append(paddingStr);
        }
        return blank.toString();
    }

    protected String get(Object entity, String property) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmpty(result);
    }

    protected String getDate(Object entity, String property, String f) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        return StringUtils.defaultIfEmptyByDate((Date) result, f);
    }

    protected <T> T get(Object entity, String property, Class<T> type) {
        Object result = null;
        if (entity != null) {
            try {
                result = BeanUtils.getValue(entity, property);
            } catch (OgnlException e) {
                result = null;
            }
        }
        result = StringUtils.defaultIfEmpty(result);
        result = BeanUtils.convertValue(result, type);
        return (T) result;
    }

}