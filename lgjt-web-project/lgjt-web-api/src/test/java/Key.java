

import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import lgjt.common.base.utils.SimpleDateFormatUtils;

import java.io.UnsupportedEncodingException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * 加密串的长度 000+补位长度+时间+字符串长度+随机数
 * @author daijiaqi
 * @date 2018/7/9 13:41
 */
public class Key {
    /**
     * 分隔数
     */
    private static final int SEPARATION_NUMBER=6;
    private static final String DECIMALFORMAT_PATTERN="000";
    private static final   DecimalFormat DECIMALFORMAT = new DecimalFormat(DECIMALFORMAT_PATTERN);
    private static final String  SDF_PATTERN ="yyyyMMddHHmmssSSS";
    private static final SimpleDateFormat SDF =new SimpleDateFormat(SDF_PATTERN);
    private static final int[] base64OxArr = {
            0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9,
            0xA, 0xB, 0xC, 0xD, 0xE, 0xF
    };

    /**
     *
     * 从解密后的字符串中获取UUID
     * @param str 解密后的字符串
     * @return UUID(只是代号，并不是严格的UUID)
     * @throws Exception
     */
    public static String getDecodeUUIDFromDecoderString(String str)throws Exception{
        return str.substring(SDF_PATTERN.length());
    }

    /**
     *  从解密后的字符串中获取timestamp
     * @param str 解密后的字符串
     * @return 时间戳 yyyyMMddHHmmssSSS
     * @throws Exception
     */
    public static String getDecodeTimestampFromDecoderString(String str)throws Exception{
        return str.substring(0,SDF_PATTERN.length());
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
            String length=DECIMALFORMAT.format(str.length());
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
            String result = length+(cover+getOxString(oxArr)+new String(bs,"utf-8").toString());
            System.out.println("result ="+result);
          return  TtsxBase64.getUrlEncoder().encodeToString(result.getBytes("utf-8"));
        } catch (Exception e) {
            throw e;
        }
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
            int length=Integer.parseInt(strBase64Decoder.substring(0,DECIMALFORMAT_PATTERN.length()));
            int keyCount=Integer.parseInt(strBase64Decoder.substring(0+DECIMALFORMAT_PATTERN.length(),1+DECIMALFORMAT_PATTERN.length()));
            System.out.println("keyCount==="+keyCount);
            String keys= strBase64Decoder.substring(1+DECIMALFORMAT_PATTERN.length(),keyCount+1+DECIMALFORMAT_PATTERN.length());
            System.out.println("keys==="+keys);
            int[] keyArr = get0xIntegerBy0xString(keys);
            int keyArrIndex=0;
            String strBase64DecoderNoKey=strBase64Decoder.substring(keyCount+1+DECIMALFORMAT_PATTERN.length(),keyCount+1+DECIMALFORMAT_PATTERN.length()+SDF_PATTERN.length()+length).trim();
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
    private static int getCover(String str){
        int b=str.length()%SEPARATION_NUMBER;
        int cover=SEPARATION_NUMBER-1-DECIMALFORMAT_PATTERN.length()-b;//需要补位数
        if(cover<=3){//补6
            cover=cover+SEPARATION_NUMBER;
        }
        return cover;
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

    /**
     * 封装参数
     * YYYYMMDDHHMMSSSSS+参数+8位以内的随机数字
     * @param str 参数
     * @return 封装后的结果
     */
    private static String packageParameter(String str){
        return SDF.format(new Date())+str+(new Random()).nextInt(99999999);
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(   decimalFormat.getPositivePrefix());

//        String uuid=UUID.randomUUID().toString();
//        DecimalFormat decimalFormat = new DecimalFormat("000");
//        String length=decimalFormat.format(1);
//        System.out.println(length);
        String uuid=UUID.randomUUID().toString();
        System.out.println("原始UUID："+uuid);
        String encode = base64Encoder(uuid);
        System.out.println("加密后："+encode);

        System.out.println("解密后："+ base64Decode(encode));

        System.out.println("解密后-UUID"+getDecodeUUIDFromDecoderString(encode));

        System.out.println("解密后-时间戳"+getDecodeTimestampFromDecoderString(encode));
    }
}
