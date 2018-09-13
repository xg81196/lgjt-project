package lgjt.web.api.module.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IStringCache;
import com.ttsx.util.cache.StringCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.common.base.utils.RedisKeys;
import lgjt.domain.api.systask.secretkey.SysSecretKey;
import lgjt.services.api.secretkey.SysSecretKeyService;
import lgjt.web.api.config.ApiConfig;
import lgjt.web.api.module.base.ApiBaseModule;

/**
   * 通讯秘钥接口类
   *@author daijiaqi
   *@date 2018/5/6 23:54
   */
@At("/api")
@IocBean
@Log4j
public class CommunicationModule extends ApiBaseModule {

	
	@Inject
    private SysSecretKeyService sysSecretKeyService;
	
	/**
	 * 获取通讯秘钥
	 * 
	 * @param appId
	 *            对接系统ID （必填）
	 * @param timestamp
	 *            时间戳，格式为yyyyMMddHHmmss。例如：20180101 142513 （必填）
	 * @param apiVersion
	 *            API接口版本，当前固定为1.0 （必填）
	 * @param authToken
	 *            对接系统ID （必填）
	 * @param paramSign
	 *            参数签名 （必填）
	 * @return 通讯秘钥
	 */
	@POST
	@GET
	@At("/getCommunicationKey")
	public Object getCommunicationKey(@Param("app_id") String appId, @Param("timestamp") String timestamp,
			@Param("api_version") String apiVersion, @Param("auth_token") String authToken,
			@Param("param_sign") String paramSign) {

		System.out.println("----appId="+appId+"----timestamp="+timestamp+"-----apiVersion="+apiVersion+"-------authToken="+authToken+"-----paramSign "+paramSign);
		//判断共有参数
		ReturnCode returnCode = ParameterVerificationUtils.checkStandardParameters(appId, timestamp, apiVersion, authToken, paramSign);
		if(!returnCode.getCode().equals(ReturnCode.CODE_100000.getCode())) {
			return ResultsImpl.parse(returnCode.getCode(),returnCode.getValue()); 
		}
		//判断 appId是否存在
		SysSecretKey sysSecretKey = sysSecretKeyService.get(appId);
		if(sysSecretKey==null) {
			return ResultsImpl.parse(ReturnCode.CODE_101003.getCode(),ReturnCode.CODE_101003.getValue()); 
		}
		//判断签名是否正确
		String paramSignLocal = ParameterVerificationUtils.md5(ParameterVerificationUtils.parametersSort("app_id=" + appId,"timestamp=" + timestamp,"auth_token=" + authToken,"api_version=" + apiVersion)+"|key=" + sysSecretKey.getSecretKey());
		if (!paramSignLocal.equalsIgnoreCase(paramSign)) {
			return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
		}
		
		// 正常的处理逻辑 key = redis-prefix-communicationKey+appId+day;
		String communicationKey ="";
		try {
			communicationKey = ParameterVerificationUtils.getCommunicationKey(ApiConfig.REDIS_PREFIX_COMMUNICATIONKEY, appId, timestamp);
			if (communicationKey.length() == 0) {
				throw new Exception("");
			}
		}catch(Exception e){
			return ResultsImpl.parse(ReturnCode.CODE_101010.getCode(), ReturnCode.CODE_101010.getValue());
		}
		IStringCache cache = CacheFactory.getStringCache();
		cache.set(RedisKeys.TIMESTAMP,timestamp);
		String  dateHour = ParameterVerificationUtils.getDateHourFromTimeStamp(timestamp);
		Map<String, String> result = new HashMap<String, String>();
		result.put("key", communicationKey);
		result.put("startTime", dateHour+ "0000");
		result.put("expireTime",dateHour+ "5959");
		return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
	}

	/**
	 * 视频资源平台回调接口
	 *
	 * @param appId 对接系统ID
	 * @param timestamp 时间戳
	 * @param apiVersion 版本默认1.0
	 * @param authToken 对接系统ID
	 * @param paramSign 签名
	 * @param rulesId  视频资源ID
	 * @return
	 */
	 @POST
	 @GET
	 @At("/getPlayRules")
	 public Object getPlayRules(@Param("app_id") String appId, @Param("timestamp") String timestamp,
								   @Param("api_version") String apiVersion, @Param("auth_token") String authToken,
								   @Param("param_sign") String paramSign, @Param("rules_id") String rulesId) {

		 try{
			 List<String> params =new ArrayList<String>();
             params.add("rules_id=" + rulesId);


			 ReturnCode code =  super.parametersCheck(appId,timestamp,apiVersion,authToken,paramSign,params);
			 if(!code.codeEquals(ReturnCode.CODE_100000)){
				 return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
			 }

             String rules = StringUtil.trim(StringCache.getInstance().get("resources:"+rulesId));
			 if(rules.length()==0){//业务不合法
                 return ResultsImpl.parse(ReturnCode.CODE_102001.getCode(), ReturnCode.CODE_102001.getValue());
             }

             String[] rulesArr = rules.split("_");//0 videoid ,1  秒
			 if(rulesArr.length<2){
                 return ResultsImpl.parse(ReturnCode.CODE_102003.getCode(), ReturnCode.CODE_102003.getValue());
             }

			 Map<String, String> result = new HashMap<String, String>();
             result.put("videoId",rulesArr[0]);
			 result.put("playTimes",rulesArr[1]);
			 return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
		 }catch(Exception e){
			 log.error("noticeUserLogout("+appId+","+timestamp+","+apiVersion+","+authToken+","+paramSign+")",e);
		 }
		 return ResultsImpl.parse(ReturnCode.CODE_103023.getCode(), ReturnCode.CODE_103023.getValue());

	 }
}