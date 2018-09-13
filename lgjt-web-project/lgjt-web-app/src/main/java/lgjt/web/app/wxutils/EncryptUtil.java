package lgjt.web.app.wxutils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtil {

	
	public static String md5(String origPW) {
		return DigestUtils.md5Hex(origPW);
	}
	
	
	public static void main(String[] args) {

		System.out.println(base64Encode("#/letterIndex"));
	}
	public static String base64Encode(String orig) {
		try {
			return  Base64.encodeBase64String(orig.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
	
	public static String base64Decode(String passwd) {
		try {
			byte[] bs =  Base64.decodeBase64(passwd);
			return new String(bs,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
	
	
	
	public static boolean verify(String token,Map<String,String> params,String key) {
		String text = createLinkString(params) + key;
		if(md5(text).equals(token)) {
			return true;
		}
		return false;
	}
	
	 /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    
}
