package la.service.wf.action;

import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import la.service.web.support.ActionSupport;
import org.guiceside.web.annotation.Action;

/**
 * Created by wangjia on 14-6-28.
 */
@Action(name = "task", namespace = "/wf")
public class WfTaskAction extends ActionSupport {
    @Override
    public String execute() throws Exception {
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
        }
        message.setContent(respContent);
        respMessage = MessageUtils.messageToXml(message);*/
        return null;
    }
}
