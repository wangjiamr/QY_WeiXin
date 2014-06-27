package la.service.common.entity;

/**
 * Created by wangjia on 14-6-26.
 */
public class ImageMessage extends Message{
    /**图片地址*/
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
