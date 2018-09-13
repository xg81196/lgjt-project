import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import lgjt.common.base.utils.ParameterVerificationUtils;

public class TestAll {

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<String> parameters =new ArrayList<String>();
		parameters.add("api_version=1.0");
		parameters.add("app_id=A02003");
		parameters.add("auth_token=9cb92ce6-6278-4a97-b24a-9cff63533036");
		parameters.add("param1=张三");
		parameters.add("param2=test@abc.com");
		parameters.add("timestamp=20180101142513");
		String result =	ParameterVerificationUtils.parametersSort(parameters);
		result=result+"|key=6fa33c418c26bf24012";
		
//		String resultMd5=MD5(result);
//		System.out.println(resultMd5);
		
//		System.out.println("=="+Md5Crypt.apr1Crypt("123456".getBytes("UTF-8")));
		String resultMd5=toMD5("123456");
		System.out.println(resultMd5);
	}

    public static String getMd5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
 
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("md5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
 
    /**
     * 标准MD5加密
     * @param inStr
     * @return
     */
    public static String toMD5(String inStr) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes("utf-8"));
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString().toUpperCase();
    }
 
}
