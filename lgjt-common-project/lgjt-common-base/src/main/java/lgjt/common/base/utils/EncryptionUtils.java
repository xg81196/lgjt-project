package lgjt.common.base.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 加密解密工具类
 * @author wangyu
 * @date 2018-05-29
 */
public class EncryptionUtils {

	/**
	 * 获取加密手机号--异或加密
	 * @param phoneNumber 原始手机号
	 * @return
	 */
	public static String getEncryption(String phoneNumber){
		if(phoneNumber !=null){
			int key = 0x10;//16进制数--16
			char[] charArray = phoneNumber.toCharArray();
			for(int i=0; i<charArray.length;i++){
				charArray[i] = (char)(charArray[i]^key);
			}
			return String.valueOf(charArray);
		}		
		return null;
		
	}
}
