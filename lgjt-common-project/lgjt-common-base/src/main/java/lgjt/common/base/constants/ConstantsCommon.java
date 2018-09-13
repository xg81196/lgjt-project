package lgjt.common.base.constants;

/**
 * 系统常量-一般情况下不会更改
 * @author daijiaqi
 */
public class ConstantsCommon {
	/**
	 * 远程服务器时间与当前服务器时间最大误差时间 单位（分）
	 */
	public static final int TIMESTAMP_MAXIMUM_ERROR_MINUTE = 10;
	
	/**
	 * 远程服务器时间与当前服务器时间最大误差时间 单位（毫秒）
	 */
	public static final long TIMESTAMP_MAXIMUM_ERROR_MILLISECOND = TIMESTAMP_MAXIMUM_ERROR_MINUTE*60*1000;

	/**
	 * 权限注解：如果方法名中包含了这个注解则不需要登录，对ADMIN无效
	 * 其它的如果不包含此注解默认都需要登录
	 */
	public static final String AUTHORITY_NO_LOGIN="NO_LOGIN";



	/**
	 * TOKEN：token在header中的名称
	 */
	public static String HEAD_TOKEN_NAME = "ttsx_auth_token";

	/**
	 * TOKEN：token在redis中的前缀
	 */
	public static String TOKEN_REDIS_PREFIX="login-";


	/**
	 * UserLoginInfo：对象中用户权限的KEY
	 */
	public static String USERLOGININFO_AUTHS_KEY="AUTHS";


			
}
