package lgjt.web.backend.filter;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;
import lgjt.common.base.Authority;
import lgjt.common.base.Authoritys;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.utils.UserUtil;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

/**
 * 用户拦截器，判断是否登录，判断是否有权限去对应的模块及操作
 * 
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-16 下午2:45:58
 * 
 * @UpdateUser: wuguangwei
 * @UpdateDate: 2018-4-17
 * @UpdateRemark: 加入权限过滤
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

	/**
	 * 数据被篡改
	 */
	private static final UTF8JsonView ANTI = new UTF8JsonView(
			JsonFormat.nice());

	/**
	 * 需要排除的url
	 */
	private String[] excludedURLArray = {"/checkcode/getCheckCode","/admin/user/login"
			,"/admin/user/islogin","/admin/dict/query","/admin/user/getToken","/file/view","/admin/sysUser/downloadFile"
			,"/admin/organization/downloadFile","/video/get","/admin/union/orgAuthTree","/tree/treeChiren","/tree/getTreeAndChildren","/admin/tree/getTreeAndSuper"
			,"/tree/getTreeLastChild","/admin/user/resetPassword","/admin/courseInfo/getCourseDetail"};

	static {
		UNLOGIN.setData(Results.parse(Constants.STATE_UNLOGIN,"未登陆"));
		UNAUTH.setData(Results.parse(Constants.STATE_UNAUTH,"没有权限"));
		ANTI.setData(Results.parse(Constants.STATE_FAIL,"用户数据被篡改"));
	}

	
	public View match(ActionContext context) {

		String path = context.getPath();


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

		//签名
		/*SysUserAdmin userAdmin = UserUtil.getAdminUser();
		//查询数据有没有被篡改
		SysUserAdminService sysUserAdminService = IocUtils.getBean(SysUserAdminService.class);
		userAdmin = sysUserAdminService.get(userAdmin.getId());
		String addSign =  SignUtils.sign(userAdmin.getAddAntiTamperString(userAdmin), PropertyUtil.getProperty("secret"));
		String updSign  = SignUtils.sign(userAdmin.getUpdAntiString(userAdmin), PropertyUtil.getProperty("secret"));*/

		//TODO:
		/*if(!addSign.equals(userAdmin.getAntiTamper()) && !updSign.equals(userAdmin.getAntiTamper())){

			//签名有误
			return  ANTI;
		}*/

		Authority[] authorityArray = getAuthoritys(context);

		UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfo(loginPrefix+token);

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
	 *
	 * @param authorityArray 权限注解
	 * @return true 不需要登录  false 需要登录
	 * @author daijiaqi
	 * @date 2018年5月4日
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
	 *
	 * @param context Action执行的上下文
	 * @return 权限注解
	 * @author daijiaqi
	 * @date 2018年5月4日
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
