package lgjt.common.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wuguangwei
 * @date 2018/7/19
 * @Description:
 */
public class MD5Util {


    public static final String LOGIN_USER_TICKET_SALT = "E_bao_Yang_salt";

    // MD5
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 32位加密
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "System doesn't support MD5 algorithm.");
        }

       /* try {
            msgDigest.update(text.getBytes("utf-8"));

        } catch (UnsupportedEncodingException e) {

            throw new IllegalStateException(
                    "System doesn't support your  EncodingException.");

        }*/

        byte[] bytes;
       try {
            bytes = msgDigest.digest(text.getBytes("utf-8"));
       } catch ( Exception e) {
           throw new IllegalStateException(
                   "System doesn't support your  EncodingException.");

       }



        String md5Str = encodeHex(bytes);

        return md5Str;
    }

    public static String generateLoginTicket(String userName, String md5Passwd, String timestamp) {
        return md5(userName + md5Passwd + timestamp + LOGIN_USER_TICKET_SALT);
    }

    private static String encodeHex(byte[] bytes) {

        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    public static void main(String[] args) {

        String s = "api_version=1.0|app_id=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|auth_token=b98401b8-6395-4a9e-a11c-8fbf9c4580a9|orderInfo={info=[{id=1, money=100, name=按摩课程, num=10, price=10, remark=一个视频}], money=100, orderId=20180724143557097, realname=wgw, source=1, state=0, time=1532414157907, url=http://222.175.74.2:5003/front/pay/getPayInfo, userId=111}|timestamp=20180724140444|key=66909056468321858701489663156969";
        System.out.println(md5(s));
    }
}
