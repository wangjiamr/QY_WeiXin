package la.service.common.action;

import la.service.common.entity.TextRspMessage;
import la.service.util.MessageUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 12-7-12
 * Time: 下午9:49
 * To change this template use File | Settings | File Templates.
 */
@Action(name = "authorize", namespace = "/common")
public class AuthorizeAction extends BaseAction {


    @ReqGet
    @ReqSet
    private String code;

    @ReqGet
    @ReqSet
    private String type;


    @ReqGet
    @ReqSet
    private String cId;

    @ReqGet
    @ReqSet
    private String uId;

    @ReqGet
    @ReqSet
    private Long id;

    @ReqGet
    private String state;

    @ReqSet
    private String url;

    @ReqSet
    private String authorizeUrl;

    @ReqGet
    private String account;

    @ReqGet
    private String password;

    @ReqGet
    private String companyId;

    @ReqGet
    private String laToken;


    private String wxToken = "mingdaoWX";

    @Override
    public String execute() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        jsonObject.put("authorize", "-1");

        System.out.println(jsonObject.toString());
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public String test() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Company", "mingdao");
        jsonObject.put("Address", "Shanghai China");
        writeJsonByAction(jsonObject.toString());
        return null;
    }

    public void signature() throws Exception {

        System.out.println( getHttpServletRequest().getMethod());


        RequestData requestData = getRequestData();
        if (requestData != null) {
            Set<String> set = requestData.keySet();
            if (set != null && !set.isEmpty()) {
                for (String key : set) {
                    System.out.println("--------------------->params:" + key + " value=" + requestData.get(key));
                }
            }
        }
        if( getHttpServletRequest().getMethod().toUpperCase().indexOf("GET")!=-1){
            String signature = getHttpServletRequest().getParameter("signature");
            String timestamp = getHttpServletRequest().getParameter("timestamp");
            String nonce = getHttpServletRequest().getParameter("nonce");
            String echostr = getHttpServletRequest().getParameter("echostr");
            List<String> params = new ArrayList<String>();
            params.add(wxToken);
            params.add(timestamp);
            params.add(nonce);
            Collections.sort(params);

            String content = params.get(0) + params.get(1) + params.get(2);
            MessageDigest md = null;
            String outStr = null;
            try {
                md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
                byte[] digest = md.digest(content.getBytes());       //返回的是byet[]，要转化为String存储比较方便
                outStr = bytes2Hex(digest);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            if(org.guiceside.commons.lang.StringUtils.isNotEmpty(signature)){
                if (outStr.equals(signature) && StringUtils.isNotEmpty(signature)) {
                    writeJsonByAction(echostr);
                }
            }
        }else{
            System.out.println("aaaaaaa");
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


    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
