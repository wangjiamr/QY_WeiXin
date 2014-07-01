package la.service.common;


import la.service.util.AccessLaTokenUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.guiceside.commons.collection.RequestData;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.web.action.ActionContext;
import org.guiceside.web.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-11-6
 * @since JDK1.5
 */
public class ActionInterceptor implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object result = null;
        Object[] args = invocation.getArguments();

        ActionContext actionContext = (ActionContext) args[0];
        ActionMapping actionMapping = (ActionMapping) actionContext
                .getActionContext().get(ActionContext.ACTIONMAPPING);
        System.out.println("------------------running in "+actionMapping.getNamespace());
        System.out.println("------------------running in "+actionMapping.getName());
        System.out.println("------------------running in "+actionMapping.getMethodName());
//        RequestData requestData = (RequestData) actionContext.getActionContext().get(ActionContext.REQUESTDATA);
//        HttpServletRequest httpServletRequest = (HttpServletRequest) actionContext.getActionContext().get(ActionContext.HTTPSERVLETREQUEST);
//        if (actionMapping.getNamespace().equals("/common")) {
//            if (actionMapping.getName().equals("authorize")) {
//                if (actionMapping.getMethodName().equals("execute") || actionMapping.getMethodName().equals("companyList")) {
//                    result = invocation.proceed();
//                    return result;
//                }
//            }
//        }
//
//        if (requestData != null) {
//            String laToken = requestData.getString("laToken");
//            if (StringUtils.isNotBlank(laToken)) {
//                UserInfo userInfo = AccessLaTokenUtils.get(laToken);
//                if (userInfo != null) {
//                    httpServletRequest.removeAttribute(AccessLaTokenUtils.laTokenKey);
//                    httpServletRequest.setAttribute(AccessLaTokenUtils.laTokenKey, userInfo);
//                    result = invocation.proceed();
//                }
//            }
//        }
        result = invocation.proceed();
        return result;
    }
}