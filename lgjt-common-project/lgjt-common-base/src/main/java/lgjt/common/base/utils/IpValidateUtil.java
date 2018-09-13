package lgjt.common.base.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


public class IpValidateUtil {
	
	/** 正则表达式验证字符串，验证是否为：IP地址 */
	final static String regIpAddress = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

	/** 正则表达式验证字符串，验证是否为：IP地址~IP地址 */
	final static String regIpAddressScope = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})" + "~(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
	
	/**
	 * 判断字符串是否为空或空字符串
	 * 
	 * @param s
	 *            要判断的字符串
	 * @return 为空返回true，否则false
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 判断字符串是否不为空或空字符串
	 * 
	 * @param s
	 *            要判断的字符串
	 * @return 为空返回false，否则true
	 */
	public static boolean isNotNullOrEmpty(String s) {
		return s != null && s.trim().length() > 0;
	}
	
	/**
	 * 判断一个字符串是否为ip地址
	 * 
	 * @param p
	 *            ip字符串
	 * @return
	 */
	public static boolean isIPAddress(String p) {
		if (p == null || p.trim().length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile(regIpAddress, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(p).matches();
	}

	/**
	 * 判断一个字符串是否为ip地址区间
	 * 
	 * @param p
	 *            ip字符串
	 * @return
	 */
	public static boolean isIPAddressScope(String p) {
		if (p == null || p.trim().length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile(regIpAddressScope, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(p).matches();
	}

	/**
	 * 判断一个字符串是否为ip地址区间
	 * 
	 * @param p
	 *            ip字符串
	 * @return
	 */
	public static boolean isIPAddressOrScope(String p) {
		if (p == null || p.trim().length() == 0) {
			return false;
		}
		boolean match = true;
		String[] ipAddressScopeArr = p.split(";");
		Pattern patternIpAddressScope = Pattern.compile(regIpAddressScope, Pattern.CASE_INSENSITIVE);
		Pattern patternIpAddress = Pattern.compile(regIpAddress, Pattern.CASE_INSENSITIVE);
		for (int i = 0; i < ipAddressScopeArr.length; i++) {
			String ipAddressScope = ipAddressScopeArr[i];
			if (isNullOrEmpty(ipAddressScope)) {
				continue;
			}
			if (!patternIpAddressScope.matcher(ipAddressScope).matches() && !patternIpAddress.matcher(ipAddressScope).matches()) {
				match = false;
				break;
			}
		}
		return match;
	}

	/**
	 * 验证IP地址是否在限制区域内
	 * 
	 * @param scope
	 *            IP限制区域，支持格式：IP地址、IP地址~IP地址、IP地址~IP地址;IP地址
	 * @param ipAddress
	 *            要验证的IP地址，格式：IP地址
	 * @return
	 * @throws Exception
	 *             传入IP格式不正确
	 */
	public static boolean isIPAddressInScope(String scope, String ipAddress) throws Exception {
		// 是否通过验证
		boolean isValid = false;
		// 传入的ipAddress格式是否正确
		if (!isIPAddress(ipAddress)) {
			throw new Exception("传入的需要验证的IP地址格式不正确！");
		}
		// 获取限制区域IP地址
		String[] limitScopeAndIpAddressArr = scope.split(";");
		for (int i = 0; i < limitScopeAndIpAddressArr.length; i++) {
			String limitScopeAndIpAddress = limitScopeAndIpAddressArr[i];
			// IP限制类型为：IP地址-IP地址
			isValid = true;
			if (isIPAddressScope(limitScopeAndIpAddress)) {
				String[] limitIpScopeArr = limitScopeAndIpAddress.split("~");
				String[] limitIpBegin = limitIpScopeArr[0].split("\\.");
				String[] limitIpEnd = limitIpScopeArr[1].split("\\.");
				String[] userIp = ipAddress.split("\\.");
				for (int j = 0; j < 4; j++) {
					// 转换为int判断范围
					int u_ip = Integer.parseInt(userIp[j]);
					int b_ip = Integer.parseInt(limitIpBegin[j]);
					int e_ip = Integer.parseInt(limitIpEnd[j]);
					if (u_ip < b_ip || u_ip > e_ip) {
						isValid = false;
						break;
					}
				}
			}
			// IP限制类型为：IP地址
			else if (isIPAddress(limitScopeAndIpAddress)) {
				String[] limitIp = limitScopeAndIpAddress.split("\\.");
				String[] userIp = ipAddress.split("\\.");
				for (int j = 0; j < 4; j++) {
					// 转换为int判断范围
					int u_ip = Integer.parseInt(userIp[j]);
					int l_ip = Integer.parseInt(limitIp[j]);
					if (u_ip != l_ip) {
						isValid = false;
						break;
					}
				}
			}
			// 无法处理
			else {
				throw new Exception("传入的IP限制区域格式不正确！");
			}
			if (isValid) {
				break;
			}
		}
		return isValid;
	}
	
	  /** 
	   * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	   * 
	   * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	   * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	   * 
	   * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	   * 192.168.1.100 
	   * 
	   * 用户真实IP为： 192.168.1.110 
	   * 
	   * @param request 
	   * @return 
	   */ 
	  public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    if(ip!=null){
	    	int idx = ip.indexOf(",");
	    	if(idx>0){
	    		ip = ip.substring(0, idx).trim();
	    	}
	    }
	    return ip; 
	  } 

}
