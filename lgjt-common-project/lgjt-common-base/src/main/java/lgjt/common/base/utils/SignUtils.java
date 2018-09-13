package lgjt.common.base.utils;

import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author wuguangwei
 * @date 2018/4/25
 * @Description: 签名工具类
 */
public class SignUtils {

	/**
	 *
	 * @param paramValues
	 *            参数列表
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String, String> paramValues, String secret) {
		return sign(paramValues, null, secret);
	}

	/**
	 * 对paramValues进行签名，其中noSignParamNames这些参数不参与签名
	 * 
	 * @param paramValues
	 * @param noSignParamNames
	 * @param secret
	 * @return
	 */
	public static String sign(Map<String, String> paramValues,
			List<String> noSignParamNames, String secret) {
		try {
			StringBuilder buffer = new StringBuilder();
			List<String> paramNames = new ArrayList<String>(paramValues.size());
			paramNames.addAll(paramValues.keySet());
			if (noSignParamNames != null && noSignParamNames.size() > 0) {
				for (String ignoreParamName : noSignParamNames) {
					paramNames.remove(ignoreParamName);
				}
			}
			Collections.sort(paramNames);
			//buffer.append(secret);
			for (String paramName : paramNames) {
				buffer.append(paramName).append("=").append(paramValues.get(paramName)).append("|");
			}
			buffer.append("key=").append(secret);
			return  DigestUtils.md5Hex(buffer.toString());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public static String md5(Map<String, String> data, String key) {
		String content = getSignContent(data, true);
		if (StringUtils.isBlank(content)) {
			return null;
		}
		StringBuilder buf = new StringBuilder(content);
		buf.append("|").append("key=" + key);

		//String param = buf.toString().replace("\"","");
		System.out.println(buf.toString());
		String sign = MD5Util.md5(buf.toString());
		System.out.println(sign);
		return sign;
	}



	/**
	 * @param data
	 * @param withSignType sign_type字段，rue,计算签名； false 不计算签名
	 * @return
	 */
	public static String getSignContent(Map<String, String> data, boolean withSignType) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		TreeMap<String, String> map = new TreeMap<String, String>(data);

		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String k = entry.getKey();
			if (StringUtils.isBlank(k)) {
				continue;
			}
			if ("class".equalsIgnoreCase(k) || "key".equalsIgnoreCase(k)
					|| "param_sign".equalsIgnoreCase(k) || "info".equalsIgnoreCase(k)
					|| "orderItem".equalsIgnoreCase(k) || "payTime".equalsIgnoreCase(k)
					|| "url".equalsIgnoreCase(k)) {
				continue;
			}
			if (!withSignType && "sign_type".equalsIgnoreCase(k)) {
				continue;
			}
			String v = entry.getValue();
			// 字段为空，不参与签名
			if (StringUtils.isBlank(v)) {
				continue;
			}
			buf.append(k);
			buf.append("=");
			buf.append(v);
			buf.append("|");
		}

		buf = buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	public static boolean checkMd5Sign(String content, String sign, String key) {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		StringBuilder buf = new StringBuilder(content);
		buf.append("&key=" + key);

		return MD5Util.md5(buf.toString()).equalsIgnoreCase(sign);
	}



	public static void main(String[] args) {
		Map<String, String> paramValues = Maps.newHashMap();
		String secret = "123456";
		paramValues.put("api_version","1.0");
		paramValues.put("app_id","A02003");
		paramValues.put("auth_token","9cb92ce6-6278-4a97-b24a-9cff63533036");
		paramValues.put("param1","张三");
		paramValues.put("param2","test");

		//System.out.println(sign(paramValues,null,secret));
		String ss ="api_version=1.0|app_id=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|auth_token=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|info=[{\"id\":\"1\",\"money\":100,\"name\":\"按摩课程\",\"num\":10,\"price\":10,\"remark\":\"一个视频\"}]|money=100|orderId=20180724155203282|realname=wgw|source=1|state=0|time=20180724155203|timestamp=20180724150006|url=http://222.175.74.2:5003/front/pay/getPayInfo|userId=111|key=93387873322265723214994862580830";

		String s1 = "api_version=1.0|app_id=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|auth_token=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|info=[{\"id\":\"1\",\"money\":100,\"name\":\"按摩课程\",\"num\":10,\"price\":10,\"remark\":\"一个视频\"}]|money=100|orderId=20180726102659096|realname=wgw|source=1|state=0|time=20180726102659|timestamp=20180725180605|url=http://222.175.74.2:5003/front/pay/getPayInfo|userId=111|key=78212824868279493832221876355223";
		//String sign = SignUtils.md5(data, "123");
		System.out.println(MD5Util.md5(s1));


	}

}
