package lgjt.web.rush.filter;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;
import lgjt.common.base.Authority;
import lgjt.common.base.Authoritys;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.rush.utils.LoginUtil;


public class UserFilter implements ActionFilter {


    /**
     * 未登录的返回
     */
    private static final UTF8JsonView UNLOGIN = new UTF8JsonView(
            JsonFormat.nice());
    /**
     * 无权限的返回
     */
    private static final UTF8JsonView UNAUTH = new UTF8JsonView(
            JsonFormat.nice());

    static {
        UNLOGIN.setData(Results.parse(Constants.STATE_UNLOGIN, ReturnCode.CODE_103021.getValue()));
        UNAUTH.setData(Results.parse(Constants.STATE_UNAUTH, ReturnCode.CODE_103029.getValue()));
    }
    /**
     * 需要排除的url
     */
    private String[] excludedURLArray = {"/bannerInfo/queryPage","/bannerInfo/get","/front/courseInfo/view","/front/courseCategory/view",
            "/front/frontRushGaste/querySequence","/front/frontRushGaste/startRushGate","/admin/ques/downloadFile","/front/pay/getOrderPayInfo","/front/pay/orderPayResult"};


    /**
     * show 如果路径包含 /admin/ 则必须要登录，且有权限.
     * <p>show 反之 如果带ConstantsCommon.AUTHORITY_NO_LOGIN 的注解，则不需要登录<br>
     * show 其余的都需要登录
     * @param context Action执行的上下文
     * @return 按照注释中的返回
     */
    public View match(ActionContext context) {
        String path = context.getPath();
        Authority[] authorityArray = getAuthoritys(context);
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        // 判断是否在过滤url之外
        for (String page : excludedURLArray) {
            if (path.equals(page)) {
                return null;
            }
        }

        String token= Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        if(StringTool.isEmpty(token)){
            return UNLOGIN;
        }

        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();

        String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");

        if(!loginInfoCache.isLogin(loginPrefix+token)){
            return UNLOGIN;
        }


        if (authorityArray.length == 0) {//沒有注解都需要登陸
            return userLoginInfo == null ? UNLOGIN : null;
        } else {//有注解
            if (isNoLogin(authorityArray)) {//判断登录
                return null;
            } else {
                if (userLoginInfo == null) {
                    return UNLOGIN;
                }
                String userAuths = StringUtil.trim(userLoginInfo.getInfos().get("roles"));
                //System.out.println("------------------userAuths");
               // String userAuths = StringUtil.trim(LoginUtil.getAuthString());
                for (int i = 0; i < authorityArray.length; i++) {
                    String code = StringUtil.trim(authorityArray[i].value());
                    if (code.length() > 0) {
                        if (userAuths.indexOf(";" + code + ";") >= 0) {
                            return null;
                        }
                    }
                }
                return UNAUTH;
            }
        }
    }

    /**
     * show 根据注解判断方法是否需要登录.
     * @param authorityArray 权限注解
     * @return true 不需要登录  false 需要登录
     */
    private boolean isNoLogin(Authority[] authorityArray) {
        for (Authority authority : authorityArray) {
            String code = StringUtil.trim(authority.value());
            if (code.equalsIgnoreCase(ConstantsCommon.AUTHORITY_NO_LOGIN)) {
                return true;
            }
        }
        return false;
    }

    /**
     * show 获取注解信息.
     * @param context Action执行的上下文
     * @return 权限注解
     */
    private Authority[] getAuthoritys(ActionContext context) {
        Authoritys auths = context.getMethod().getAnnotation(Authoritys.class);
        if (auths != null) {
            return auths.value();
        }
        Authority auth = context.getMethod().getAnnotation(Authority.class);
        if (auth != null) {
            return new Authority[]{auth};
        }
        return new Authority[]{};
    }
}
