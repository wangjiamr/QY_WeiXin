package la.service.common.action;

import net.sf.json.JSONObject;
import org.guiceside.web.action.BaseAction;
import org.guiceside.web.annotation.Action;
import org.guiceside.web.annotation.ReqGet;
import org.guiceside.web.annotation.ReqSet;

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

    @Override
    public String execute() throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("result","-1");
        jsonObject.put("authorize","-1");

        System.out.println(jsonObject.toString());
        writeJsonByAction(jsonObject.toString());
        return null;
    }


}
