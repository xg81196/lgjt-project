package lgjt.web.api.config;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.util.StringUtil;

/**
 * 配置文件中的内容统一出口
 * <p>Title: AppConfig</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月4日
 */
public class ApiConfig {

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
}
