package lgjt.services.mongodb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * <p>Title: FilesysUtil</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class FilesysUtil {
    private final static int KEY=0x10;
    private final static String separator =";;;";
    public final static  SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取下载KEY
     * @param activeTime 有效时间，单位秒
     *                   如果小于等于0，则是永久有效
     * @param id 文件标识
     * @return 下载KEY
     */
    public static String getDownloadKey(long activeTime,String id){
        String tt="-1";
        id = StringTools.trim(id);

        if(activeTime>0){
           long t= new Date().getTime()+(activeTime*1000);
            tt=SDF.format(new Date(t));
        }
        StringBuffer sb =new StringBuffer();
        sb.append(new Date().getTime());
        sb.append(separator);
        sb.append(tt);
        sb.append(separator);
        sb.append(id);
        return getEncryption(sb.toString());
    }

    /**
     * 加解密
     * @param para
     * @return
     */
    public static String getEncryption(String para){
        char[] charArray = para.toCharArray();
        for(int i =0;i<charArray.length;i++){
            charArray[i]=(char)(charArray[i]^KEY);
        }
        return String.valueOf(charArray);
    }

    /**
     * 检查密钥合法性
     * @param key 密钥
     * @return true 合法，false 不合法
     */
    public static boolean checkKey(String key){
      String keyValue =getEncryption(key);
      String[] values=keyValue.split(separator);
      if(values.length!=3){
        return false;
      }
      return true;
    }

    /**
     *  检查KEY的合法性，及时间有效性
     * @param key 密钥
     * @return true 合法，false 不合法
     */
    public static boolean checkDownloadKey(String key){
        boolean result =false;
        if(checkKey(key)){
            String[] values = FilesysUtil.getKeyValue(key);
            String time = values[1];
            if(time.equals("-1")){
                result=true;
            }else{
                try {
                    long t = FilesysUtil.SDF.parse(time).getTime();
                    long nowTime = new Date().getTime();
                    if(t>=nowTime){
                        result=true;
                    }
                } catch (ParseException e) {}
            }
        }
        return result;
    }

    public static String[] getKeyValue(String key){
        String keyValue =getEncryption(key);
        String[] values=keyValue.split(separator);
       return values;
    }
}
