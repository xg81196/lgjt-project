package lgjt.domain.backend.utils;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.IStringCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.role.SysMenu;
import lgjt.domain.backend.role.vo.SysRoleMenuVo;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;

import java.util.*;
import java.util.stream.Collectors;


@Log4j
public class UserUtil {

	public static final String USER_INFO = "USER_INFO";
	public static final String MENUS = "MENUS";
	
	public static final String AUTHS = "AUTHS";

	public static final String ROLES = "ROLES";



	/**
	 * 获取当前登录用户信息(前台)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SysUser getUser()  {
		String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		SysUser user = (SysUser) cache.get(loginPrefix+token);
		return user;

	}
	
	/**
	 * 前台用户登录信息存在redis中
	 * 
	 * @param user
	 */
	public static void setUser(String token,SysUser user) {

		String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");
		IObjectCache cache = CacheFactory.getObjectCache();
		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);
		cache.set(loginPrefix+token, user);
		cache.setExpireTime(loginPrefix+token,time);
		cache.set(user.getUserName(), token);
		cache.setExpireTime(user.getUserName(),time);
		
	}


	/**
	 * 获取当前登录用户信息(管理端台)
	 *
	 * @return
	 * @throws Exception
	 */
	public static SysUserAdmin getAdminUser()  {
		String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		SysUserAdmin user = (SysUserAdmin) cache.get(loginPrefix+token);
		return user;
	}


	/**
	 * 管理端台用户登录信息存在redis中
	 *
	 * @param user
	 */
	public static void setAdminUser(String token,List<SysRoleMenuVo> menus,SysUserAdmin user,String orgId,String orgName) {

		String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");

		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);

		IObjectCache cache = CacheFactory.getObjectCache();
		cache.set(loginPrefix+token,user);
		cache.setExpireTime(loginPrefix+token,time);
		//将数据保存至redis
		HashMap<String ,String> userMap =new HashMap<String,String>(1);
		userMap.put("userId",user.getId());
		userMap.put("orgId",user.getOrgId());
		userMap.put("orgName",orgName);
		userMap.put("extend5", user.getExtend5());

		/**
		 * 角色菜单编码
		 */
		if (CollectionUtils.isNotEmpty(menus))  {

			Map<String, List<SysRoleMenuVo>> menuCodeMap = menus.stream().filter(r-> StringUtils.isNotBlank(r.getRoleId())).collect(Collectors.groupingBy(SysRoleMenuVo::getRoleId));
			/**
			 * 拼接如下格式 1:;WEBCAST_VIEW;QUERY_LINK;|2:;WEBCAST_UPLOAD;
			 */
			StringBuilder sb = new StringBuilder();
			for(Map.Entry<String, List<SysRoleMenuVo>> rm: menuCodeMap.entrySet()) {
				sb.append(";");
				for (SysRoleMenuVo m : rm.getValue()) {
					sb.append(m.getMenuCode()).append(";");
				}
			}
			userMap.put("roles", sb.toString().trim());

		}

		ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
		loginInfoCache.setExpireTime(time);
		loginInfoCache.login(loginPrefix+token,user.getUserName(), user.getRealName(), ClientInfo.getIp(), userMap);

	}

	
	/**
	 * 后台用户注销登录
	 */
	public static void invalidate() throws Exception {
		String loginPrefix = PropertyUtil.getProperty("redis-prefix-login");
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		if (cache.get(loginPrefix+token) != null) {
			cache.del(loginPrefix+token);
			cache.del("MENUS"+token);
		}
	}
	

	/**
	 * 存储登陆用户的权限资源
	 * */
	public static void setMenu(List<SysMenu> menus, String token) {

		IObjectCache cache = CacheFactory.getObjectCache();
		cache.set("MENUS"+token, menus);
		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);
		cache.setExpireTime("MENUS"+token, time);
	}

	@SuppressWarnings("unchecked")
	public static List<SysMenu> getMenu() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		String ss=MENUS+token;
		List<SysMenu> list= cache.get(ss) != null ? (List<SysMenu>) cache
				.get(ss) : null;
				return list;
	}
	
	/**
	 * 存储登陆用户的权限资源
	 * */
	public static void setAuth(List<SysMenu> menus,String token) {

		Set<String> set = new HashSet<String>();
		for(SysMenu m:menus) {
			set.add(m.getResCode());
		}
		IObjectCache cache = CacheFactory.getObjectCache();
		cache.set(AUTHS+token, set);
		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);
		cache.setExpireTime(AUTHS+token, time);
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getAuth() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		return cache.get(AUTHS+token) != null ? (Set<String>) cache
				.get(AUTHS+token) : null;
//				HttpSession session = Mvcs.getReq().getSession();
//				return session.getAttribute(AUTHS) != null ? (Set<String>) session
//						.getAttribute(AUTHS) : null;
	}


	/**
	 * 存储登陆用户的权限资源
	 * */
	public static void setUserData(String unionIds,String comIds, String token) {

		IStringCache cache = CacheFactory.getStringCache();
		cache.set(RedisKeys.USERDATAS_UNION+token, unionIds);
		cache.set(RedisKeys.USERDATAS_COMID+token, comIds);
		cache.set(RedisKeys.USERDATAS_UNION_COMID+token, unionIds+","+comIds);
		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);
		cache.setExpireTime(RedisKeys.USERDATAS_UNION+token, time);
		cache.setExpireTime(RedisKeys.USERDATAS_COMID+token, time);
		cache.setExpireTime(RedisKeys.USERDATAS_UNION_COMID+token, time);
	}

	/**
	 * 管理人员所拥有的工会数据权限
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getAdminUnionUserData() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IStringCache cache = CacheFactory.getStringCache();
		String ss=RedisKeys.USERDATAS_UNION+token;
		String userDatas= cache.get(ss) != null ? (String) cache
				.get(ss) : null;
		return userDatas;
	}

	/**
	 * 管理人员所拥有的企业数据权限
	 * @return
	 */
	public static String getAdminComUserData() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IStringCache cache = CacheFactory.getStringCache();
		String ss=RedisKeys.USERDATAS_COMID+token;
		String userDatas= cache.get(ss) != null ? (String) cache
				.get(ss) : null;
		return userDatas;
	}

	/**
	 * 管理人员所拥有的企业数据权限
	 * @return
	 */
	public static String getAdminUnionAndComUserData() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IStringCache cache = CacheFactory.getStringCache();
		String unionComIds = RedisKeys.USERDATAS_UNION_COMID+token;
		String userDatas= cache.get(unionComIds) != null ? (String) cache
				.get(unionComIds) : null;
		return userDatas;
	}

	@SuppressWarnings("unchecked")
	public static void delUserData() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IStringCache cache = CacheFactory.getStringCache();
		String comIds = RedisKeys.USERDATAS_COMID+token;
		String unionIds = RedisKeys.USERDATAS_UNION+token;
		String unionComIds = RedisKeys.USERDATAS_UNION_COMID+token;
		cache.del(comIds);
		cache.del(unionIds);
		cache.del(unionComIds);

	}


	public static void  setRoles( String roleNames) {
		IObjectCache cache = CacheFactory.getObjectCache();
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		cache.set(ROLES+token, roleNames);
		String tokenTime=PropertyUtil.getProperty("redis-expire-time");
		Integer time=Integer.valueOf(tokenTime);
		cache.setExpireTime(ROLES+token, time);
	}


	/**
	 * 所有角色名称
	 * @return
	 */
	public static String  getRoleNames() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		IObjectCache cache = CacheFactory.getObjectCache();
		String ss=ROLES+token;
		return  cache.get(ss) != null ? (String) cache
				.get(ss) : null;
	}

	
}
