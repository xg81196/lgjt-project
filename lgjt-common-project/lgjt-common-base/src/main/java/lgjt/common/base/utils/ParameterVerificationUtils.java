package lgjt.common.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ttsx.util.cache.IStringCache;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;


/**
 * 参数校验类
 * 
 * @author daijiaqi
 *
 */
@Log4j
public class ParameterVerificationUtils {

	/**
	 * 根据参数返回 待签名字符串
	 * 如果位空 或者 空字符串则忽略
	 * @param parameters
	 *            参数
	 * @return 待签名字符串
	 */
	public static String parametersSort(List<String> parameters) {
		StringBuffer sb = new StringBuffer();
		if (parameters != null) {
			Collections.sort(parameters);
		}
//		for (String parameter : parameters) {
//			if(StringUtils.trim(parameter).length()==0 || !parameter.matches("[^=]+=.+")) {
//				continue;
//			}
//			if (sb.length() > 0) {
//				sb.append("|");
//			}
//			sb.append(parameter);
//		}
		for (String parameter : parameters) {
			if(parameter!=null) {
				int index = parameter.indexOf("=");
				if(index>0 && index<(parameter.length()-1)) {
					if (sb.length() > 0) {
						sb.append("|");
					}
					sb.append(parameter);
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * <p>Title:  根据参数返回 待签名字符串</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月24日  
	 * @param parameters 不定参数
	 * @return 待签名字符串
	 */
	public static String parametersSort(String... parameters) {
		List<String> ps = new ArrayList<String>();
		for (String p : parameters) {
			if(p!=null) {
				ps.add(p);
			}
		}
		return parametersSort(ps);
	}

	/**
	 * 标准MD5加密
	 * 
	 * @param inStr
	 * @return
	 * @throws Exception
	 */
	public static String md5(String inStr) {
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
	
	/**
	 * 判断请求时间的合法性
	 * @param reqTimeStamp	发出请求的服务器时间
	 * @return false 误差超出10分钟或者异常，true 误差在10分钟以内
	 */
	public static boolean checkTimeStamp(String reqTimeStamp) throws Exception{
			boolean result =false;
			long reqTimeStampLong = SimpleDateFormatUtils.getDate(reqTimeStamp,SimpleDateFormatUtils.PATTERN_TYPE_3).getTime();
			long currentTimeStampLong = new Date().getTime();
			long minTime = (currentTimeStampLong-ConstantsCommon.TIMESTAMP_MAXIMUM_ERROR_MILLISECOND);
			long maxTime = (currentTimeStampLong+ConstantsCommon.TIMESTAMP_MAXIMUM_ERROR_MILLISECOND);
			if(reqTimeStampLong>=minTime && reqTimeStampLong<=maxTime) {
				result=true;
			}

		return result ;
	}
	
	/**
	 * 
	 * <p>Title: 校验标准参数</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月24日  
	 * @param appId 系统ID
	 * @param timestamp 远程系统时间 yyyyMMddHHmmss
	 * @param apiVersion 版本号 默认1.0
	 * @param authToken 系统id
	 * @param paramSign 签名
	 * @return	0:代码 ，1:信息
	 */
	public static ReturnCode  checkStandardParameters(String appId,  String timestamp,String apiVersion,String authToken,String paramSign){
		if (StringUtils.isEmpty(appId)) {
			return ReturnCode.CODE_101003;
		}
		if (StringUtils.isEmpty(apiVersion)) {
			return ReturnCode.CODE_101004;
		}
		try {
			if (StringUtils.isEmpty(timestamp) || !ParameterVerificationUtils.checkTimeStamp(timestamp)) {
				throw new Exception("");
			}
		}catch(Exception e){
			return ReturnCode.CODE_101005;
		}

		if (StringUtils.isEmpty(authToken)) {
			return ReturnCode.CODE_101006;
		}
		if (StringUtils.isEmpty(paramSign)) {
			return ReturnCode.CODE_101008;
		}
		return ReturnCode.CODE_100000;
	}
	
	/**
	 * 获取年月日
	 * <p>Title: getDateFromTimeStamp</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月24日  
	 * @param timestamp
	 * @return
	 */
	public static String getDateFromTimeStamp(String timestamp) {
		String date = "";
		try {
			date= timestamp.substring(0, 8);
		}catch(Exception e) {}
		return date;
	}
	/**
	 * 获取小时
	 * <p>Title: getDateFromTimeStamp</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月24日  
	 * @param timestamp
	 * @return
	 */
	public static String getHourFromTimeStamp(String timestamp) {
		String hour = "";
		try {
			hour= timestamp.substring(8, 10);
		}catch(Exception e) {}
		return hour;
	}
	/**
	 * 获取年月日小时  2018010112
	 * <p>Title: getDateFromTimeStamp</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月24日  
	 * @param timestamp
	 * @return
	 */
	public static String getDateHourFromTimeStamp(String timestamp) {
		String dateHour = "";
		try {
			dateHour= timestamp.substring(0, 10);
		}catch(Exception e) {}
		return dateHour;
	}
	
	
	
	/**
	 * base64加密
	
	 * <p>Title: base64Encoder</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月4日  
	 * @param parameter
	 * @return
	 */
	public static String base64Encoder(String parameter) {
		try {
			return Base64.encodeBase64String(parameter.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {}
		return null;
	}
	
	/**
	 * base64解密
	
	 * <p>Title: base64Decode</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月4日  
	 * @param parameter
	 * @return
	 */
	public static String base64Decode(String parameter) {
		try {
			byte[] bs =  Base64.decodeBase64(parameter);
			return new String(bs,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	/**
	 * 获取通讯秘钥
	
	 * <p>Title: getCommunicationKey</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月4日  
	 * @param communicationKeyPrefix 密钥前缀
	 * @param appId 唯一ID
	 * @param timestamp 时间戳
	 * @return
	 */
	public static String getCommunicationKey(String communicationKeyPrefix,String appId,String timestamp) {
		String redisKey = communicationKeyPrefix + appId + ParameterVerificationUtils.getDateFromTimeStamp(timestamp);
		IObjectCache<Map> objectCache = CacheFactory.getObjectCache();
		String	result = StringUtil.trim((String) objectCache.get(redisKey)
					.get(ParameterVerificationUtils.getHourFromTimeStamp(timestamp)));
		return result;
	}

	/**
	 * 存储短信验证码
	 * @param prefix
	 * @param phoneNumber
	 * @param smsType
	 * @param value
	 * @param expireTime
	 * @return
	 */
	public static ReturnCode setSmsCodeToRedis(String prefix,String phoneNumber,String smsType,String value,int expireTime){
		String key =prefix+"_"+phoneNumber+"_"+smsType;
		IStringCache stringCache = CacheFactory.getStringCache();
		if(stringCache.exist(key)){
			return ReturnCode.CODE_103013;
		}else{
			stringCache.set(key,value);
			stringCache.setExpireTime(key,expireTime);
		}
		return ReturnCode.CODE_100000;
	}


	/**
	 * 校验验证码是否存在
	 * @param prefix
	 * @param phoneNumber
	 * @param smsType
	 * @return
	 */
	public static ReturnCode checkSmsCodeFromRedis(String prefix,String phoneNumber,String smsType){
		String key =prefix+"_"+phoneNumber+"_"+smsType;
		IStringCache stringCache = CacheFactory.getStringCache();
		if(stringCache.exist(key)){
			return ReturnCode.CODE_103013;
		}
		return ReturnCode.CODE_100000;
	}



	/**
	 * 获取短信验证码
	 * @param prefix
	 * @param phoneNumber
	 * @param smsType
	 * @return
	 */
	public static String getSmsCodeToRedis(String prefix,String phoneNumber,String smsType){
		String key =prefix+"_"+phoneNumber+"_"+smsType;
		IStringCache stringCache = CacheFactory.getStringCache();
		return stringCache.get(key);
	}

}
