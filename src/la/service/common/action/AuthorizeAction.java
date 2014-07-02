package la.service.common.action;

import com.mingdao.api.entity.LAToken;
import com.mingdao.api.entity.Req;
import com.mingdao.api.la.RequestLA;
import com.mingdao.api.utils.SignatureUtil;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import la.service.common.entity.*;
import la.service.util.MessageUtils;
import la.service.util.TimeStampLaTokenUtils;
import la.service.util.WxParamsUtils;
import la.service.util.WxTokenUtils;
import la.service.web.support.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.guiceside.commons.TimeUtils;
import org.guiceside.commons.TokenUtils;
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


    private static final String AGENT_ID = "4";


    private static final String wxToken = "mingdaoWX";

    private static final String USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get";

    private static final String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";

    private static final String CORP_ID = "wx853769e334ead822";

    private static final String CORP_SECRET = "17ab4955fe78a806a1a778c7adbd93d8";

    private static final String PUSH_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

    @Override
    public String execute() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        jsonObject.put("authorize", "-1");
        writeJsonByAction(jsonObject.toString());
        return null;
    }


    @PageFlow(result = {
            @Result(name = "manage", path = "/wf/manage?laToken=${laToken}&eventKey=${eventKey}&userId=${userId}", type = Dispatcher.Redirect),
            @Result(name = "task", path = "/wf/task?laToken=${laToken}&eventKey=${eventKey}&userId=${userId}", type = Dispatcher.Redirect),
            @Result(name = "req", path = "/wf/req?laToken=${laToken}&eventKey=${eventKey}&userId=${userId}", type = Dispatcher.Redirect)
    })
    public String approve() throws Exception {
        if (getHttpServletRequest().getMethod().toUpperCase().equals("GET")) {// 验证信息是否来之微信服务器
            String signature = getHttpServletRequest().getParameter("signature");
            String timestamp = getHttpServletRequest().getParameter("timestamp");
            String nonce = getHttpServletRequest().getParameter("nonce");
            String echostr = getHttpServletRequest().getParameter("echostr");
            if (StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)) {
                List<String> params = new ArrayList<String>();
                params.add(wxToken);
                params.add(timestamp);
                params.add(nonce);
                Collections.sort(params);
                String content = params.get(0) + params.get(1) + params.get(2);
                String outStr = null;
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    byte[] digest = md.digest(content.getBytes());
                    outStr = bytes2Hex(digest);
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }
                if (outStr.equals(signature)) {
                    writeTextByAction(echostr);
                }
            }
        } else {
            String respMessage = null;
            Map<String, String> requestMap = MessageUtils.parseXml(getHttpServletRequest());
            if (requestMap != null & !requestMap.isEmpty()) {
                userId = requestMap.get("FromUserName");
                if (StringUtils.isNotBlank(userId)) {
                    WxParamsUtils.put(userId, requestMap);
                    laToken = WxTokenUtils.get(userId);
                    if (StringUtils.isBlank(laToken)) {
                        getLAToken();
                    } else {
                        Long currentTimeMillis = System.currentTimeMillis();
                        Long tokenTimeMillis = TimeStampLaTokenUtils.get(laToken);
                        Double diffHours = TimeUtils.getHoursTimeDiff(tokenTimeMillis, currentTimeMillis);
                        if (diffHours == null) {
                            diffHours = 0.0d;
                        }
                        if (diffHours.intValue() > 2) {
                            getLAToken();
                        } else {
                            boolean validate_flag = RequestLA.validateToken(laToken);
                            if (!validate_flag) {
                                getLAToken();
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(laToken)) {
                        eventKey = requestMap.get("EventKey");
                        String msgType = requestMap.get("MsgType");
                        TextRspMessage textRspMessage = MessageUtils.buildRspMessage(requestMap, TextRspMessage.class);
                        if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) { // 文本消息
                            textRspMessage.setContent("请点击菜单体验明道快捷审批!");
                        } else if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_EVENT)) { // 事件推送
                            String eventType = requestMap.get("Event"); // 事件类型
                            if (eventType.equals(MessageUtils.EVENT_TYPE_SUBSCRIBE)) {// 订阅
                                textRspMessage.setContent("谢谢您的关注");
                            } else if (eventType.equals(MessageUtils.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
                                if (StringUtils.isNotBlank(eventKey)) {

                                    if (eventKey.contains("REQ")) {
                                        return "req";
                                    } else if (eventKey.contains("TASK")) {
                                        return "task";
                                    } else if (eventKey.contains("MANAGE")) {
                                        return "manage";
                                    }
                                }
                            }
                        }
                        respMessage = MessageUtils.messageToXml(textRspMessage);
                        writeXmlByAction(respMessage);
                    }
                }
            }
        }
        return null;
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


    private static String getToken() {
        String result = sendRequest(TOKEN_URL + "?corpid=" + CORP_ID + "&corpsecret=" + CORP_SECRET, null, false);
        if (StringUtils.isNotEmpty(result)) {
            return JSONObject.fromObject(result).get("access_token").toString();
        }
        return null;
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


    private void getLAToken() throws Exception {
        LAToken laTokenObj = null;
        String timestamp = System.currentTimeMillis() + "";
        String nonce = (Math.random() * (999999 - 100000) + 1215) + "";
        String content = "mingdao";
        String signature = SignatureUtil.getSignature(timestamp, nonce, content, CORP_ID, CORP_SECRET);
        laTokenObj = RequestLA.getToken(signature, userId, timestamp, nonce, content, CORP_ID, CORP_SECRET);
        if (laTokenObj != null) {
            laToken = laTokenObj.getLaToken();
            if (StringUtils.isNotBlank(laToken)) {
                WxTokenUtils.put(userId, laToken);
                TimeStampLaTokenUtils.put(laToken, laTokenObj.getTimeStamp());
            }
        }
    }

    private static String pushMessage(String content, String userId) {
        PushMessage message = new PushMessage(content);
        message.setTouser(userId.replace(",", "|"));
        message.setMsgtype("text");
        message.setAgentid(AGENT_ID);
        message.setSafe("1");
        message.setToparty(null);
        return sendRequest(PUSH_MESSAGE_URL + getToken(), JSONObject.fromObject(message).toString(), true);
    }
}
