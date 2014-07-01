package la.service.common.entity;

/**
 * Created by wangjia on 14-7-1.
 */
public class PushMessage {

    private String touser;

    private String toparty;

    private String msgtype;

    private String agentid;

    private MessageContent text;

    private String safe;

    public PushMessage(String content) {
        MessageContent message = new MessageContent();
        message.setContent(content);
        text = message;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public MessageContent getText() {
        return text;
    }

    public void setText(MessageContent text) {
        this.text = text;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public class MessageContent {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
