package lgjt.web.app.wxutils;

import java.math.BigDecimal;

public class CommonUtil {
	
	public static final String TOKEN = "token";
	public static final String KEY = "muoooo";
	
	public static final Integer THIRD_LOGIN_WX = 1;
	
	public static String getWXKey() {
		return PropertyUtil.getProperty("wx_key");
	}
	
	public static String getROOT() {
		String root =  PropertyUtil.getProperty("approot");
		if(!root.endsWith("/")) {
			root = root + "/";
		}
		return root;
	}
	
	public static String getAdminROOT() {
		String root =  PropertyUtil.getProperty("adminroot");
		if(!root.endsWith("/")) {
			root = root + "/";
		}
		return root;
	}
	
	public static double getDouble(Double d,int len) {
		if(null == d) {
			return 0;
		}
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(len,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
