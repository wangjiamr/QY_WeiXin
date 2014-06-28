package la.service.common.action;

import com.mingdao.api.entity.Req;
import com.mingdao.api.la.RequestLA;
import com.mingdao.api.utils.SignatureUtil;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import la.service.common.entity.*;
import la.service.util.MessageUtils;
import la.service.util.WxParamsUtils;
import la.service.util.WxTokenUtils;
import la.service.web.support.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.annotation.*;

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
public class AuthorizeAction extends ActionSupport {


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

    @ReqSet
    private String laToken;

    @ReqSet
    private String eventKey;

    @ReqSet
    private String userId;


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

    @PageFlow(result = {
            @Result(name = "manage", path = "/wf/manage?laToken=${laToken}&eventKey=${eventKey}", type = Dispatcher.Redirect),
            @Result(name = "task", path = "/wf/task?laToken=${laToken}&eventKey=${eventKey}", type = Dispatcher.Redirect),
            @Result(name = "req", path = "/wf/req?laToken=${laToken}&eventKey=${eventKey}", type = Dispatcher.Redirect)
    })
    public String approve() throws Exception {

        if (getHttpServletRequest().getMethod().toUpperCase().equals("GET")) {// 验证信息是否来之微信服务器
            String  signature= getHttpServletRequest().getParameter("signature");
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
            System.out.print("-------------------POST Method--------------------");
            String respMessage = null;
            try {
                Map<String, String> requestMap = MessageUtils.parseXml(getHttpServletRequest());
                System.out.println(requestMap);
                if (requestMap != null & !requestMap.isEmpty()) {
                    userId = requestMap.get("FromUserName");
                    System.out.println(userId);
                    if (StringUtils.isNotEmpty(userId)) {
                        WxParamsUtils.put(userId, requestMap);
                        laToken = WxTokenUtils.get(userId);
                        System.out.println("token-----value from map:"+laToken);
                        if (StringUtils.isBlank(laToken)) {
                            String timestamp = System.currentTimeMillis() + "";
                            String nonce = (Math.random() * (999999 - 100000) + 1215) + "";
                            String content = "mingdao";
                            String  signature= SignatureUtil.getSignature(timestamp, nonce, content, CORP_ID, CORP_SECRET);
                            System.out.println("signature:"+signature);
                            laToken = RequestLA.getToken(signature, userId, timestamp, nonce, content, CORP_ID, CORP_SECRET);
                        }
                        System.out.println("token-------value from webService"+laToken);
                        if (StringUtils.isNotBlank(laToken)) {
                            System.out.println("token"+laToken);
                            WxTokenUtils.put(userId, laToken);
                            String msgType = requestMap.get("MsgType");
                            System.out.println(msgType);
                            if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
                            } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_EVENT)) { // 事件推送
                                String eventType = requestMap.get("Event"); // 事件类型
                                if (eventType.equals(MessageUtils.EVENT_TYPE_SUBSCRIBE)) {// 订阅
                                    TextRspMessage message = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                                    message.setContent("谢谢您的关注！");
                                    respMessage = MessageUtils.messageToXml(message);
                                    writeJsonByAction(respMessage);
                                } else if (eventType.equals(MessageUtils.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
                                    eventKey = requestMap.get("EventKey");
                                    System.out.println("aaaaaa"+eventKey);
                                    System.out.println(eventKey.startsWith("APPLY_"));
                                    if (StringUtils.isNotBlank(eventKey)) {
                                        if (eventKey.startsWith("APPLY_")) {
                                            System.out.println("req"+eventKey);

                                            requestMap = WxParamsUtils.get(userId);
                                            System.out.print("-------requestMap  from map ="+requestMap);
                                            if (requestMap != null && !requestMap.isEmpty()) {
                                                String content=null;
                                                if (eventKey.equals("APPLY_EXECUTION")) {//申请-执行中
                                                    System.out.println("-------进入到执行中if语句中 =");
                                                    List<Req> reqList = RequestLA.reqIngList(laToken);
                                                    System.out.println("-------进入到执行中if语句中获取集合 ="+reqList);
                                                    if (reqList != null && !reqList.isEmpty()) {
                                                        NewsRspMessage newsMessage = MessageUtils.buildRspMessage(requestMap, NewsRspMessage.class);
                                                        List<Article> articles = _requestList(reqList);
                                                        newsMessage.setArticleCount(articles.size());
                                                        newsMessage.setArticles(articles);
                                                        content = MessageUtils.messageToXml(newsMessage);
                                                    }else{
                                                        TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                                                        textRspMessage.setContent("未找到相关记录!");
                                                        content = MessageUtils.messageToXml(textRspMessage);
                                                    }
                                                } else if (eventKey.equals("APPLY_COMPLETED")) {//申请-已完成

                                                } else if (eventKey.equals("APPLY_NEW")) {//申请-新建

                                                }
                                                writeJsonByAction(content);
                                            }
                                            return null;
                                        } else if (eventKey.startsWith("APPROVE_")) {
                                            System.out.println("task"+eventKey);
                                            return "task";
                                        } else if (eventKey.startsWith("EXECUTION_")) {
                                            System.out.println("manage"+eventKey);
                                            return "manage";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void signature() throws Exception {
        System.out.println("Invoke method-----12345------>" + getHttpServletRequest().getMethod());


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
        if (StringUtils.isNotEmpty(result)) {
            WeChatUserInfo userInfo = (WeChatUserInfo) JSONObject.toBean(JSONObject.fromObject(result), WeChatUserInfo.class);
            if (userInfo != null) {
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
        String wangjiaUerId = "68680cb7-9e50-4789-8baa-22656c3fad4a";
        String token = new AuthorizeAction().getToken();
        String postMenu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token + "&agentid=3";
        String getMenus = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?access_token=" + token + "&agentid=3";
        //System.out.print(urlStr);
        //System.out.print(sendRequest(postMenu,_menus().toString(),true));
        //System.out.print(sendRequest(getMenus,null,false));
        System.out.print(token);
        //System.out.print(new AuthorizeAction().getToken());
        ///  System.out.print(new AuthorizeAction().getUserInfo(wangjiaUerId));
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


    private List<Article> _requestList(List<Req> reqList) {
        List<Article> articles = new ArrayList<Article>();
        if (reqList != null && !reqList.isEmpty()) {
            if (reqList.size() > 1) {
                Article article = new Article();//图文混排
                article.setTitle("查看更多");
                article.setDescription("查看更多");
                article.setPicUrl("http://y2.ifengimg.com/b4c1e3c5e4848389/2014/0627/rdn_53acb1b0d5924.jpg");
                article.setUrl("http://www.baidu.com");
                articles.add(article);
            }
            for (Req req : reqList) {
                Article article = new Article();//图文混排
                article.setTitle(req.getApplyName());
                article.setDescription(req.getReqNo());
                article.setPicUrl("http://y2.ifengimg.com/b4c1e3c5e4848389/2014/0627/rdn_53acb1b0d5924.jpg");
                article.setUrl("http://www.baidu.com");
                articles.add(article);
            }
        }

        return articles;
    }
}
