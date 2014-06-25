package la.service.util;

import com.qiniu.api.config.Config;
import com.qiniu.api.rs.PutPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: zhenjiaWang
 * Date: 14-2-5
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class QNToken {

    private static final String HMAC_SHA1 = "HmacSHA1";

    public static String getUpToken(String scope, Integer afterHour,
                                    String returnUrl, String returnBody) {
        Long current = System.currentTimeMillis();
        String s = current.toString();
        s = s.substring(0, 10);
        Long ns = Long.valueOf(s);
        ns += (afterHour * 60 * 60);
        Config.ACCESS_KEY = "He6K4qzOV8pDNuyToCXh7NTy7bbfJ12XIjUhToG_";
        Config.SECRET_KEY = "gQybrcc5rnoCCWsNGwpTVN0t6n1FsuMcBXvSviXd";
        PutPolicy putPolicy = new PutPolicy(scope);
        putPolicy.returnUrl = returnUrl;
        putPolicy.returnBody = returnBody;
        putPolicy.deadline = ns;
        putPolicy.fsizeLimit = 1024 * 1024 * 10;
        com.qiniu.api.auth.digest.Mac mac = new com.qiniu.api.auth.digest.Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        String upToken = null;
        try {
            upToken = putPolicy.token(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upToken;
    }


}
