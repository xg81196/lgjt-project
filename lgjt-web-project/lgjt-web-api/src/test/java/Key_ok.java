import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;


/**
 * @author daijiaqi
 * @date 2018/7/913:41
 */
public class Key_ok {

    public static final int[] base64OxArr = {
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9,
            0xA, 0xB, 0xC, 0xD, 0xE, 0xF
    };

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = UUID.randomUUID().toString()+"201807092222220000";//UUID+18

        String[] strArr = new String[]{str};
        for (int i = 0; i < strArr.length; i++) {
            str = strArr[i];
            String encode = base64Encoder(str);
            System.out.println("原始：" + str);
            System.out.println("加密后：" + encode);
            System.out.println("解密后：" + base64Decode(encode));
            System.out.println("===========" + str);
        }
    }

    public static int getRandmNum(){
        Random r= new Random();
        int result = r.nextInt(base64OxArr.length);
        return base64OxArr[result];
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

            int oxInt=0;
            StringBuffer sb =new StringBuffer();
            for (int i = 0; i < bs.length; i++) {
                if(i%8==0){
                    oxInt=getRandmNum();
                    if(getRandmNum()%5==0){//混淆
                        sb.append("-");
                    }
                    sb.append(Integer.toHexString(oxInt).toUpperCase());
                }
                bs[i] = (byte) (bs[i] ^ oxInt);
            }
            System.out.println(bs.length);
            return Base64.getUrlEncoder().encodeToString(bs)+"_"+sb.toString();
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
            String sourceStr=str.substring(0,str.lastIndexOf("_"));

            String key=str.substring(str.lastIndexOf("_")+1).replaceAll("-","");

            byte[] bs = Base64.getUrlDecoder().decode(sourceStr);
            int oxInt=0;
            int indexKey=0;
            for (int i = 0; i < bs.length; i++) {
                if(i%8==0){
                    oxInt=   Character.digit(key.charAt(indexKey++), 16);
                }
                bs[i] = (byte) (bs[i] ^ oxInt);
            }
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }


}
