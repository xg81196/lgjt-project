package lgjt.web.api.filter;


import org.nutz.json.JsonFormat;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.UTF8JsonView;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;

import lgjt.common.base.Authority;
import lgjt.common.base.Authoritys;

/**
 * 用户拦截器，判断是否登录，判断是否有权限去对应的模块及操作
 * 
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-16 下午2:45:58
 * 
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
		UNLOGIN.setData(Results.parse(Constants.STATE_UNLOGIN,"未登陆"));
		UNAUTH.setData(Results.parse(Constants.STATE_UNAUTH,"没有权限"));
	}

	
	public View match(ActionContext context) {
		String path = context.getPath();	
		Authority[] authorityArray=getAuthoritys(context);
		if(authorityArray==null || authorityArray.length==0){
			return null;
		}
		ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
		String  token = context.getRequest().getParameter("ttsx_auth_token");
		UserLoginInfo  userLoginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix")+token);
		if(null == userLoginInfo){
			return UNLOGIN;
		} else  {// 若为后端请求
			String userAuths =	userLoginInfo.getInfos().get("roles");
			for(int i=0;i<authorityArray.length;i++){
				String code = StringUtil.trim(authorityArray[i].value());
				if(code.length()==0){
					continue;
				}
				if (null != userAuths) {
					if (userAuths.indexOf(";"+code+";")>=0) {
						return null;
					}
				}
			}
			return UNAUTH;
		}
	}
	
	/**
	 * 获取注解信息
	 * @param context
	 * @return
	 */
	private Authority[]  getAuthoritys(ActionContext context){
		Authoritys auths = context.getMethod().getAnnotation(Authoritys.class);
		if(auths!=null){
			return auths.value();
		}
		Authority auth = context.getMethod().getAnnotation(Authority.class);
		if(auth!=null){
			return new Authority[]{auth};
		}
		return null;
	}
}
