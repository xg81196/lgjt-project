package lgjt.web.app.config;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.util.StringUtil;

/**
 * show 配置文件中的内容统一出口.
 * <p>Title: AppConfig</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月4日
 */
public class AppConfig {

	/**
	 * 登录用户的token 失效时间
	 */
	public static final String REDIS_EXPIRE_TIME=StringUtil.trim(PropertyUtil.getProperty("redis-expire-time"));
	/**
	 * 登录用户的token在redis中的前缀
	 */
	public static final String REDIS_PREFIX_LOGIN=StringUtil.trim(PropertyUtil.getProperty("redis-prefix-login"));
	
	/**
	 * 临时密钥在redis里的前缀
	 */
	public static final String REDIS_PREFIX_COMMUNICATIONKEY=StringUtil.trim(PropertyUtil.getProperty("redis-prefix-communicationKey"));

	/**
	 * 短信服务：用户密码重置 模板ID
	 */
	public static final String SMS_TEMPID_PASSWORD_RESET=StringUtil.trim(PropertyUtil.getProperty("sms_tempid_password_reset"));

	/**
	 * 短信服务：用户注册 模板ID
	 */
	public static final String SMS_TEMPID_USER_SIGIN=StringUtil.trim(PropertyUtil.getProperty("sms_tempid_user_sigin"));

	/**
	 * 短信服务超时时间
	 */
	public static final String REDIS_EXPIRE_TIME_SMS=StringUtil.trim(PropertyUtil.getProperty("redis-expire-time-sms"));

	/**
	 * 短信服务redis前缀
	 */
	public static final String REDIS_PREFIX_SMS=StringUtil.trim(PropertyUtil.getProperty("redis-prefix-sms"));

//	/**
//	 * COURSE url地址
//	 */
//	public static final String COURSE_URL=StringUtil.trim(PropertyUtil.getProperty("course_url"));


//	/**
//	 * user url地址
//	 */
//	public static final String USER_URL=StringUtil.trim(PropertyUtil.getProperty("user_url"));
	/**
	 * token在header中的名称
	 */
	public static final String HEAD_TOKEN_NAME = StringUtil.trim(PropertyUtil.getProperty("head_token_name"));

//	/**
//	 * 登录用户的锁定失效时间
//	 */
//	public static final String REDIS_EXPIRE_TIME_LOCK=StringUtil.trim(PropertyUtil.getProperty("redis-expire-time-lock"));
	/**
	 * 登录用户的锁定状态在redis中的前缀
	 */
	public static final String REDIS_PREFIX_LOGINLOCK=StringUtil.trim(PropertyUtil.getProperty("redis-prefix-login-lock"));
	

}
