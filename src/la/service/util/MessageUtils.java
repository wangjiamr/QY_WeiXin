package la.service.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import la.service.common.entity.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjia on 14-6-26.
 */
public class MessageUtils {

    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";

    /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型：subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "click";

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<String, String>();

        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();

        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        inputStream.close();
        return map;
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textRspMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(TextRspMessage textRspMessage) {
        xstream.alias("xml", textRspMessage.getClass());
        return xstream.toXML(textRspMessage);
    }

    /**
     * 音乐消息对象转换成xml
     *
     * @param musicRspMessage 音乐消息对象
     * @return xml
     */
    public static String musicMessageToXml(MusicRspMessage musicRspMessage) {
        xstream.alias("xml", musicRspMessage.getClass());
        return xstream.toXML(musicRspMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsRspMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(NewsRspMessage newsRspMessage) {
        xstream.alias("xml", newsRspMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsRspMessage);
    }

    public static <T> String messageToXml(T message) {
        if (message.getClass().equals(TextRspMessage.class)) {
            return textMessageToXml((TextRspMessage) message);
        } else if (message.getClass().equals((NewsRspMessage.class))) {
            return newsMessageToXml((NewsRspMessage) message);
        } else if (message.getClass().equals(MusicRspMessage.class)) {
            return musicMessageToXml((MusicRspMessage) message);
        }
        return null;
    }

    /**
     * 扩展xstream，使其支持CDATA块
     *
     * @date 2013-05-19
     */
    private static XStream xstream = new XStream(new XppDriver() {
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @SuppressWarnings("unchecked")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    public static <T> T buildRspMessage(Map<String, String> requestMap, Class<? extends RspMessage> clazz) throws Exception {
        T obj = null;
        if (requestMap != null && clazz != null&& !requestMap.isEmpty()) {
            String fromUserName = requestMap.get("FromUserName");// 发送方帐号（open_id）
            String toUserName = requestMap.get("ToUserName"); // 公众帐号
            RspMessage message = clazz.newInstance();
            message.setToUserName(fromUserName);
            message.setFromUserName(toUserName);
            message.setCreateTime(new Date().getTime());
            message.setMsgType(messageType(clazz));
            message.setFuncFlag(0);
            obj = (T) message;
        }
        return obj;
    }

    private static String messageType(Class<? extends RspMessage> clazz) {
        if (clazz == null || clazz.equals(TextRspMessage.class)) {
            return REQ_MESSAGE_TYPE_TEXT;
        } else if (clazz.equals(NewsRspMessage.class)) {
            return RESP_MESSAGE_TYPE_NEWS;
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("FromUserName", "mingdao");
        requestMap.put("ToUserName", "wangjia");
        NewsRspMessage message = buildRspMessage(requestMap, NewsRspMessage.class);
        System.out.print(message);
    }
}
