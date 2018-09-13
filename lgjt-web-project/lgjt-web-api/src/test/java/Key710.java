

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * @author daijiaqi
 * @date 2018/7/913:41
 */
public class Key710 {
    /**
     * 分隔数
     */
    public static final int SEPARATION_NUMBER=6;

    public static final SimpleDateFormat YYYYMMDDHHMMSSSSS =new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final int[] base64OxArr = {
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9,
            0xA, 0xB, 0xC, 0xD, 0xE, 0xF
    };

    public static void main(String[] args) throws Exception {
//        String uuid=UUID.randomUUID().toString();
//        String timeStamp=YYYYMMDDHHMMSSSSS.format(new Date());
//        Random r =new Random();
//        String str = timeStamp+uuid+r.nextInt(999999);//"201807092222220000";//UUID+18
//        String[] strArr = new String[]{str};
//        for (int i = 0; i < strArr.length; i++) {
//            str = strArr[i];
//            String encode = base64Encoder(str);
//            System.out.println("原始：" + str);
//            System.out.println("加密后：" + encode);
//            System.out.println("解密后：" + base64Decode(encode));
//            System.out.println("===========" + str);
//        }
        String uuid=UUID.randomUUID().toString();
        System.out.println("原始UUID："+uuid);
        String encode = base64Encoder(uuid);
        System.out.println("加密后："+encode);

        System.out.println("解密后："+ base64Decode(encode));

        System.out.println("解密后-UUID"+getDecodeUUID(encode));

        System.out.println("解密后-时间戳"+getDecodeTimestamp(encode));
    }

    /**
     * 封装参数
     * YYYYMMDDHHMMSSSSS+参数+6位随机数
     * @param str 参数
     * @return 封装后的结果
     */
    private static String packageParameter(String str){
        Random r =new Random();
        return YYYYMMDDHHMMSSSSS.format(new Date())+str+r.nextInt(99999999);
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
    public static String base64Encoder(String str)throws Exception {
        try {
            str=packageParameter(str);
            int cover =getCover(str);
            int[] oxArr=getOxArr(cover);
            int keyArrIndex=0;
            byte[] bs = str.getBytes("UTF-8");
            int oxInt=0;
            for (int i = 0; i < bs.length; i++) {
                if(i%cover==0){
                    oxInt=oxArr[keyArrIndex++];
                    if(keyArrIndex>=oxArr.length){
                        keyArrIndex=0;
                    }
                }
                bs[i] = (byte) (bs[i] ^ oxInt);
            }
            String result = (cover+getOxString(oxArr)+new String(bs,"utf-8").toString());
            System.out.println("result ="+result);
          return  TtsxBase64.getUrlEncoder().encodeToString(result.getBytes("utf-8"));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取解密后的内容
     * @param str 加密字符串
     * @return
     * @throws Exception
     */
    public static String getDecodeUUID(String str)throws Exception{
        return base64Decode(str).substring(17,18+35);
    }

    /**
     * 获取解密后的内容
     * @param str 加密字符串
     * @return
     * @throws Exception
     */
    public static String getDecodeTimestamp(String str)throws Exception{
        return base64Decode(str).substring(0,17);
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
    public static String base64Decode(String str)throws Exception {
        try {
            String strBase64Decoder = new String(TtsxBase64.getUrlDecoder().decode(str.getBytes("utf-8")),"utf-8");
            System.out.println("1==="+strBase64Decoder);
            int keyCount=Integer.parseInt(strBase64Decoder.substring(0,1));
            System.out.println("keyCount==="+keyCount);
            String keys= strBase64Decoder.substring(1,keyCount+1);
            System.out.println("keys==="+keys);
            int[] keyArr = get0xIntegerBy0xString(keys);
            int keyArrIndex=0;
            String strBase64DecoderNoKey=strBase64Decoder.substring(keyCount+1).trim();
            byte[] strBase64DecoderNoKeyArr=strBase64DecoderNoKey.getBytes("utf-8");
            int oxInt=0;
            for (int i = 0; i < strBase64DecoderNoKeyArr.length; i++) {
                if(i%keyCount==0){
                    oxInt=keyArr[keyArrIndex++];
                    if(keyArrIndex>=keyArr.length){
                        keyArrIndex=0;
                    }
                }
                strBase64DecoderNoKeyArr[i] = (byte) (strBase64DecoderNoKeyArr[i] ^ oxInt);
            }
            return new String(strBase64DecoderNoKeyArr, "UTF-8");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取补位数
     * @param str
     * @return
     */
    public static int getCover(String str){
        int b=str.length()%SEPARATION_NUMBER;
        int cover=SEPARATION_NUMBER-b;//需要补位数
        if(cover<=3){//补6
            cover=cover+SEPARATION_NUMBER;
        }
        return cover-1;
    }

    /**
     * 获取16进制数组
     * @param keyCount
     * @return
     */
    private static  int[]  getOxArr(int keyCount){
        int[] result =new int[keyCount];
        Random r= new Random();
        for (int i = 0; i <keyCount ; i++) {
            result[i]= base64OxArr[r.nextInt(base64OxArr.length)];
        }
        return result;
    }

    /**
     * 获取16进制字符串
     * @param result 16进制数组
     * @return
     */
    private static  String  getOxString(  int[] result){
        StringBuffer sb =new StringBuffer();
        for (int i:result) {
            sb.append(Integer.toHexString(i));
        }
        return sb.toString();
    }

    /**
     * 根据16进制字符串返回16进制整数
     * @param keys 16进制字符串
     * @return 16进制整数数组
     */
    private static int[]  get0xIntegerBy0xString(String keys){
        int[] keyArr=new int[keys.length()];
        for(int i=0;i<keys.length();i++){
            keyArr[i]= Character.digit(keys.charAt(i),16);
        }
        return keyArr;
    }
}
