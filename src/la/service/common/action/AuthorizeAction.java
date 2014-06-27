package la.service.common.action;

import la.service.common.entity.*;
import la.service.util.MessageUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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


    private static final String wxToken = "mingdaoWX";

    private static final String USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get";

    private static final String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    private static final String CORP_ID = "wx853769e334ead822";

    private static final String CORP_SECRET = "17ab4955fe78a806a1a778c7adbd93d8";


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
        System.out.println("Invoke method-----12345------>" + getHttpServletRequest().getMethod());
        if (getHttpServletRequest().getMethod().toUpperCase().equals("GET")) {// 验证信息是否来之微信服务器
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
                md = MessageDigest.getInstance("SHA-1");
                byte[] digest = md.digest(content.getBytes());
                outStr = bytes2Hex(digest);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            if (org.guiceside.commons.lang.StringUtils.isNotEmpty(signature)) {
                if (outStr.equals(signature) && StringUtils.isNotEmpty(signature)) {
                    writeJsonByAction(echostr);
                }
            }
        } else {
            System.out.println("--------->POST Method");
            String respMessage = null;
            try {
                String respContent = "请求处理异常，请稍候尝试！";
                Map<String, String> requestMap = MessageUtils.parseXml(getHttpServletRequest());
                String fromUserName = requestMap.get("FromUserName");// 发送方帐号（open_id）
                String toUserName = requestMap.get("ToUserName"); // 公众帐号
                String msgType = requestMap.get("MsgType"); // 消息类型

                NewsRspMessage textMessage = new NewsRspMessage();
                if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
                    textMessage = new NewsRspMessage();
                    textMessage.setToUserName(fromUserName);
                    textMessage.setFromUserName(toUserName);
                    textMessage.setCreateTime(new Date().getTime());
                    textMessage.setMsgType(MessageUtils.RESP_MESSAGE_TYPE_NEWS);
                    textMessage.setFuncFlag(0);
                    String content = requestMap.get("Content");
                    List<Article> list = new ArrayList<Article>();
                    for (int x = 0; x < Integer.parseInt(content); x++) {
                        list.add(_news(false));
                    }
                    textMessage.setArticleCount(list.size());
                    textMessage.setArticles(list);
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
                        //
                    } else if (eventType.equals(MessageUtils.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
                        String key = requestMap.get("EventKey");
                        if (StringUtils.isNotEmpty(key)) {
                            if (key.equals("APPLY_EXECUTION")) {//申请-执行中
                                respContent = "申请-执行中菜单";
                            } else if (key.equals("APPLY_COMPLETED")) {//申请-已完成
                                respContent = "申请-已完成菜单";
                            } else if (key.equals("APPLY_NEW")) {//申请-新建
                                respContent = "申请-新建菜单";
                            } else if (key.equals("APPROVE_EXECUTION")) {//审批-进行中
                                respContent = "审批-进行中菜单";
                            } else if (key.equals("APPROVE_COMPLETED")) {//审批-已完成
                                respContent = "审批-已完成菜单";
                            } else if (key.equals("EXECUTION_WAITING")) {//执行-等待
                                respContent = "执行等待";
                            } else if (key.equals("EXECUTION_COMPLETED")) {//执行-完毕
                                respContent = "执行完毕";
                            }
                        }
                    }
                }
                respMessage = MessageUtils.newsMessageToXml(textMessage);
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

    private Article _news(boolean showImage) {
        Article article = new Article();//图文混排
        article.setTitle("明道快捷审批");
        article.setDescription("描述描述描述描述");
        if (showImage) {
            article.setPicUrl("http://y2.ifengimg.com/b4c1e3c5e4848389/2014/0627/rdn_53acb1b0d5924.jpg");
        } else {
            article.setPicUrl("");
        }
        article.setUrl("http://www.baidu.com");
        return article;
    }

    private String getUserInfo(String userId) throws Exception {
        String result = sendRequest(USER_INFO_URL + "?access_token=" + getToken() + "&userid=" + userId, null, false);
        if(StringUtils.isNotEmpty(result)){
            WeChatUserInfo userInfo=(WeChatUserInfo)JSONObject.toBean(JSONObject.fromObject(result),WeChatUserInfo.class);
            if(userInfo!=null){
                return userInfo.toString();
            }
        }
        return null;
    }

    private String getToken() {
        String result = sendRequest(TOKEN_URL + "?corpid=" + CORP_ID + "&corpsecret=" + CORP_SECRET, null, false);
        if (StringUtils.isNotEmpty(result)) {
            return JSONObject.fromObject(result).get("access_token").toString();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String wangjiaUerId="68680cb7-9e50-4789-8baa-22656c3fad4a";
        //String urlStr = url" https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + ACCESS_TOKEN;
        //System.out.print(new AuthorizeAction().getToken());
        System.out.print(new AuthorizeAction().getUserInfo(wangjiaUerId));
    }

    private static String sendRequest(String urlString, String params, boolean post) {
        String postType = "GET";
        if (post) {
            postType = "POST";
        }
        URL url = null;
        HttpURLConnection connection = null;
        StringBuilder result = new StringBuilder();
        try {

            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(50000);//设置读取超时
            connection.setRequestMethod(postType);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            if (StringUtils.isNotEmpty(params)) {
                osw.write(params);
            }
            osw.flush();
            osw.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                result.append(lines);
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
        }
        return result.toString();
    }

    private static JSONObject _menus() {
        JSONObject root = new JSONObject();
        JSONArray menu = new JSONArray();
        JSONObject apply = new JSONObject();
        apply.put("name", "申请");
        JSONObject approve = new JSONObject();
        approve.put("name", "审批");
        JSONObject execute = new JSONObject();
        execute.put("name", "执行");
        JSONObject apply_execution = new JSONObject();
        JSONObject apply_complete = new JSONObject();
        JSONObject apply_new = new JSONObject();
        JSONObject approve_execution = new JSONObject();
        JSONObject approve_complete = new JSONObject();
        JSONObject execute_waiting = new JSONObject();
        JSONObject execute_complete = new JSONObject();
        JSONArray apply_sub_buttons = new JSONArray();
        JSONArray approve_sub_buttons = new JSONArray();
        JSONArray execute_sub_buttons = new JSONArray();

        apply_execution.put("name", "进行中");
        apply_execution.put("type", "click");
        apply_execution.put("key", "APPLY_EXECUTION");

        apply_complete.put("name", "已完成");
        apply_complete.put("type", "click");
        apply_complete.put("key", "APPLY_COMPLETED");

        apply_new.put("name", "新建");
        apply_new.put("type", "click");
        apply_new.put("key", "APPLY_NEW");


        approve_execution.put("name", "进行中");
        approve_execution.put("type", "click");
        approve_execution.put("key", "APPROVE_EXECUTION");

        approve_complete.put("name", "已完成");
        approve_complete.put("type", "click");
        approve_complete.put("key", "APPROVE_COMPLETED");

        execute_waiting.put("name", "等待执行");
        execute_waiting.put("type", "click");
        execute_waiting.put("key", "EXECUTION_WAITING");

        execute_complete.put("name", "执行完毕");
        execute_complete.put("type", "click");
        execute_complete.put("key", "EXECUTION_COMPLETED");

        apply_sub_buttons.add(apply_new);
        apply_sub_buttons.add(apply_execution);
        apply_sub_buttons.add(apply_complete);
        approve_sub_buttons.add(approve_execution);
        approve_sub_buttons.add(approve_complete);
        execute_sub_buttons.add(execute_waiting);
        execute_sub_buttons.add(execute_complete);

        apply.put("sub_button", apply_sub_buttons);
        approve.put("sub_button", approve_sub_buttons);
        execute.put("sub_button", execute_sub_buttons);
        menu.add(apply);
        menu.add(approve);
        menu.add(execute);
        root.put("button", menu);
        return root;
    }
}
