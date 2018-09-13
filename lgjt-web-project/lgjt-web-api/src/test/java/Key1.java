


import java.io.UnsupportedEncodingException;

/**
 * @author daijiaqi
 * @date 2018/7/913:41
 */
public class Key1 {
     public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println(UUID.randomUUID());
        String str = "5b3f148902531d1310448037";
        String[] strArr = new String[]{"d67259-6c5c08-44429-feb", "5b3f148902531d1310448037", "cad03882193b42eba6ba", "123018932eb34d7f8827", "dd930bfcd4654216920b", "d534f3c7db804053b53e"};
        for (int i = 0; i < strArr.length; i++) {
            str = strArr[i];
            String encode = base64Encoder(str);
            System.out.println("原始：" + str);
            System.out.println("加密后：" + encode);
            System.out.println("解密后：" + base64Decode(encode));
            System.out.println("===========" + str);
        }


    }

    /**
     * base64加密
     *
     * <p>Title: base64Encoder</p>
     * <p>Description: </p>
     *
     * @param str
     * @return
     * @author daijiaqi
     * @date 2018年5月4日
     */
    public static String base64Encoder(String str) {
        try {
            byte[] bs = str.getBytes("UTF-8");
            for (int i = 0; i < bs.length; i++)
                bs[i] = (byte) (bs[i] ^ 10);
            return java.util.Base64.getUrlEncoder().encodeToString(bs);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * base64解密
     *
     * <p>Title: base64Decode</p>
     * <p>Description: </p>
     *
     * @param str
     * @return
     * @author daijiaqi
     * @date 2018年5月4日
     */
    public static String base64Decode(String str) {
        try {
            byte[] bs = java.util.Base64.getUrlDecoder().decode(str);
            for (int i = 0; i < bs.length; i++)
                bs[i] = (byte) (bs[i] ^ 10);
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
