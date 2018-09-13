package lgjt.web.app.wxutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private static Properties props;

	static {
		props = init();
	}

	public static Properties init() {
		try {
			InputStream in = PropertyUtil.class
					.getResourceAsStream("/conf.properties");
			Properties pros = new Properties();
			pros.load(in);
			return pros;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	public static String get(String key, String de) {
		return props.getProperty(key, de);
	}

	public static boolean getBoolean(String key) {
		String value = getProperty(key);
		if (null != value && value.trim().equals("true")) {
			return true;
		}
		return false;

	}
	
	public static int getInt(String key,int def) {
		String value = getProperty(key);
		int result = def;
		try{
			result = Integer.valueOf(value);
		}catch(Exception e) {
		}
		return result;
	}

}
