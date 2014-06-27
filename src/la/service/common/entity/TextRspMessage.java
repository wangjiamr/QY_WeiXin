package la.service.common.entity;

/**
 * Created by wangjia on 14-6-26.
 */
public class TextRspMessage extends RspMessage {
    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
