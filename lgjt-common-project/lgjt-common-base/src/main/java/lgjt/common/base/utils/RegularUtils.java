package lgjt.common.base.utils;
/**
 * 正则表达式工具类
 * <p>Title: RegularUtils</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月4日
 */
public class RegularUtils {
	/**
	 * 手机号正则表达式
	 */
	private static final String REGULAR_PHONENUMBER="^1[3|4|5|7|8][0-9]\\d{4,8}$";
	
		/**
	 * 验证手机号
	
	 * <p>Title: matchesPhoneNumber</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月4日  
	 * @param phoneNumber
	 * @return
	 */
	public static boolean matchesPhoneNumber(String phoneNumber) {
		return phoneNumber==null?false:phoneNumber.matches(REGULAR_PHONENUMBER);
	}

}
