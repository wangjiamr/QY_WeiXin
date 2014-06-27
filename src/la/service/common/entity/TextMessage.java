package la.service.common.entity;

/**
 * 文本消息
 * Created by wangjia on 14-6-26.
 */
public class TextMessage extends Message {
    /**消息内容*/
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }
}
