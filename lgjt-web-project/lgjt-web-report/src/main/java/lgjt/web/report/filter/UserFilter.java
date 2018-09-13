package lgjt.web.report.filter;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.Authority;
import lgjt.common.base.Authoritys;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.LoginUtil;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

/**
 * show 用户拦截器，判断是否登录，判断是否有权限去对应的模块及操作.
 * @author daijiaqi
 * @Description: TODO(用一句话描述该类作用)
 * @CreateDate: 2016-12-16 下午2:45:58
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-16 下午2:45:58
 * @UpdateRemark: 说明本次修改内容
 */
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
     * show 如果路径包含 /admin/ 则必须要登录，且有权限.
     * <p>show 反之 如果带ConstantsCommon.AUTHORITY_NO_LOGIN 的注解，则不需要登录<br>
     * show 其余的都需要登录
     * @author daijiaqi  
     * @date 2018年5月4日
     * @param context Action执行的上下文
     * @return 按照注释中的返回
     */
	public View match(ActionContext context) {
		String path = context.getPath();
		Authority[] authorityArray = getAuthoritys(context);
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();

		//调试
//        if (userLoginInfo == null) {
//            String token = Mvcs.getReq().getHeader(LetterConfig.HEAD_TOKEN_NAME);
//            if (token == null) {
//                token = "10000";
//            }
//            ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
//            loginInfoCache.setExpireTime(Integer.parseInt(LetterConfig.REDIS_EXPIRE_TIME) * 60);
//
//            userLoginInfo = loginInfoCache.login(LetterConfig.REDIS_PREFIX_LOGIN + token, "daijiaqi", "戴佳旗", ClientInfo.getIp(), null);
//        }

		if (authorityArray.length == 0) {//
			return userLoginInfo == null ? UNLOGIN : null;
		} else {//有注解
			if (isNoLogin(authorityArray)) {//判断登录
				return null;
			} else {
				if (userLoginInfo == null) {
					return UNLOGIN;
				}
				String userAuths = StringUtil.trim(userLoginInfo.getInfos().get("roles"));
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
	 * @author daijiaqi  
     * @date 2018年5月4日
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
	 * @author daijiaqi  
     * @date 2018年5月4日
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
