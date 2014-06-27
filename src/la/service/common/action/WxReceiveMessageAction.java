package la.service.common.action;

import la.service.common.entity.TextMessage;
import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjia on 14-6-26.
 */
@Action(name = "message", namespace = "/common")
public class WxReceiveMessageAction extends BaseAction {
    @Override
    public String execute() throws Exception {
        return null;
    }

    public void textHandler() throws Exception {
        String respMessage = null;
        try {
            String respContent = "请求处理异常，请稍候尝试！";
            Map<String, String> requestMap = MessageUtils.parseXml(getHttpServletRequest());
            String fromUserName = requestMap.get("FromUserName");// 发送方帐号（open_id）
            String toUserName = requestMap.get("ToUserName"); // 公众帐号
            String msgType = requestMap.get("MsgType"); // 消息类型
            TextRspMessage textMessage = new TextRspMessage(); // 回复文本消息
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);
            if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
                respContent = "您发送的是文本消息！";
            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_IMAGE)) {// 图片消息
                respContent = "您发送的是图片消息！";
            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_LOCATION)) { // 地理位置消息
                respContent = "您发送的是地理位置消息！";
            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_LINK)) { // 链接消息
                respContent = "您发送的是链接消息！";
            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_VOICE)) {// 音频消息
                respContent = "您发送的是音频消息！";
            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_EVENT)) { // 事件推送
                String eventType = requestMap.get("Event"); // 事件类型
                if (eventType.equals(MessageUtils.EVENT_TYPE_SUBSCRIBE)) {// 订阅
                    respContent = "谢谢您的关注！";
                } else if (eventType.equals(MessageUtils.EVENT_TYPE_UNSUBSCRIBE)) { // 取消订阅
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                } else if (eventType.equals(MessageUtils.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
                    // TODO 自定义菜单权没有开放，暂不处理该类消息
                }
            }

            textMessage.setContent(respContent);
            respMessage = MessageUtils.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeJsonByAction(respMessage);
    }

    public void imageHandler() throws Exception {

    }

    public void voiceHandler() throws Exception {

    }

    public void videoHandler() throws Exception {

    }

    public void newsHandler() throws Exception {

    }

    public void linkHandler() throws Exception {

    }

    public void locationHandler() throws Exception {

    }

    public static boolean isQqFace(String content) {
        boolean result = false;
        String qqFaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|/::'\\(|/::-\\||/::@|" +
                "/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|" +
                "/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:" +
                "handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|" +
                "/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|" +
                "/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|" +
                "/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|" +
                "/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
        Pattern p = Pattern.compile(qqFaceRegex);
        Matcher m = p.matcher(content);
        if (m.matches()) {
            result = true;
        }
        return result;
    }


}
