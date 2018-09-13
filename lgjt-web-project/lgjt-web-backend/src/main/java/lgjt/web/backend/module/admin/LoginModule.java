package lgjt.web.backend.module.admin;

import java.util.HashMap;
import java.util.Map;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.BaseModule;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.Authority;


/**
 * 登录后台的接口
 * 
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-9 下午4:50:45
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-9 下午4:50:45
 * @UpdateRemark: 说明本次修改内容
 */
@At("/admin")
@IocBean
@Log4j
public class LoginModule extends BaseModule {
	
	 @Authority("")
	 @At("/login")
	 public Object login(@Param("userName") String userName , @Param("password") String password , @Param("verificationCode") String verificationCode){
		 	String loginPrefix = PropertyUtil.getProperty("redis-prefix");
			ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
			
			
			if(loginInfoCache.isLogin(loginPrefix)){
				return 	Results.parse(Constants.STATE_FAIL,"用户已经登录");
			}
			Map<String,String> auths=new  HashMap<String,String>();
			String rolesAuths = PropertyUtil.getProperty("ROLES-AUTHS");
			if(rolesAuths!=null){
				String [] rolesAuthsArray =  rolesAuths.split("\\|");
				for(int i=0;i<rolesAuthsArray.length;i++){
					String[] roleAuthArry= rolesAuthsArray[i].split(":");
					auths.put(roleAuthArry[0].trim(),roleAuthArry[1].trim());		
				}
			}
			String realName="测试";
			String ip="127.0.0.1";
			HashMap<String, String> infos=new HashMap<String, String>();
//			infos.put("roles", auths.get(roleid));
			loginInfoCache.setExpireTime(30*60);
			UserLoginInfo uli = loginInfoCache.login( userName, realName, ip,infos);
			uli.getInfos().get("roles");
			
//			Map<String ,String > result =new HashMap<String,String>();
//			result.put("key", "sadasd");
//			result.put("date", "阿萨德阿萨德阿萨德");
			return 	Results.parse(Constants.STATE_SUCCESS,"成功",uli.getToken());
	 }
}
