package lgjt.web.api.module.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.tool.util.UUIDUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;

import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.domain.api.systask.secretkey.SysSecretKey;
import lgjt.domain.api.systask.sysUserContrast.SysUserContrast;
import lgjt.domain.api.user.SysUser;
import lgjt.services.api.secretkey.SysSecretKeyService;
import lgjt.services.api.sysUserContrast.SysUserContrastService;
import lgjt.services.api.user.SysUserService;
import lgjt.web.api.config.ApiConfig;
import lgjt.web.api.module.base.ApiBaseModule;

/**
   * 通知接口类
   *@author daijiaqi
   *@date 2018/5/6 23:55
   */
@At("/notice")
@IocBean
@Log4j
public class NoticeModule extends ApiBaseModule {

	@Inject
	private SysSecretKeyService sysSecretKeyService;

	@Inject
	private SysUserContrastService sysUserContrastService;

	@Inject
	private SysUserService sysUserService;

	/**
	 * 
	 *通知用户登录
	 * @author daijiaqi
	 * @date 2018年4月26日
	 * @param appId
	 *            对接系统ID
	 * @param timestamp
	 *            时间戳，格式为yyyyMMddHHmmss。例如：20180101142513
	 * @param apiVersion
	 *            API接口版本，当前固定为1.0
	 * @param authToken
	 *            用户在对接系统中的唯一ID
	 * @param paramSign
	 *            参数签名
	 * @param userInfo
	 *            用户信息。这是一个JSON串，JSON串的格式参见“附录：用户信息
	 * @return
	 */
	@POST
	@GET
	@At("/noticeUserLogin")
	public Object noticeUserLogin(@Param("app_id") String appId, @Param("timestamp") String timestamp,
			@Param("api_version") String apiVersion, @Param("auth_token") String authToken,
			@Param("param_sign") String paramSign, @Param("user_info") String userInfo) {
		// 判断共有参数
		List<String> params =new ArrayList<String>();
		params.add("userInfo="+userInfo);
		ReturnCode code =  super.parametersCheck(appId,timestamp,apiVersion,authToken,paramSign,params);
		if(!code.codeEquals(ReturnCode.CODE_100000)){
			return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
		}
		
		// 判断关联表
		SysUserContrast sysUserContrastQuery = new SysUserContrast();
		sysUserContrastQuery.setCompanyUserId(authToken);// 用户企业ID
		sysUserContrastQuery.setCompanyCode(super.getSysSecretKeyOrgId());// 企业ID
		sysUserContrastQuery.setAppId(appId);// Appid 对应 唯一性
		SysUserContrast sysUserContrast = sysUserContrastService.get(sysUserContrastQuery);
		if (sysUserContrast == null) {
			return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
		}
		SysUser sysUser = sysUserService.getByUserName(sysUserContrast.getUserName());
		if (sysUser == null) {
			return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
		}
		// 登录
		ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
		loginInfoCache.setExpireTime(Integer.parseInt(ApiConfig.REDIS_EXPIRE_TIME));
		String token = UUIDUtil.getUUID();
		loginInfoCache.login(ApiConfig.REDIS_PREFIX_LOGIN+ token, sysUser.getUserName(),
				sysUser.getRealName(), ClientInfo.getIp(), null);
		Map<String, String> result = new HashMap<String, String>();
		result.put("token", token);
		result.put("userName", sysUser.getUserName());
		result.put("isFirst", 0 + "");// 是不是首次登录
		result.put("userType", sysUser.getUserType() + "");

		return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
	}

	/**
	 * 通知用户登出
	 * @author daijiaqi
	 * @date 2018年4月26日
	 * @param appId
	 *            对接系统ID
	 * @param timestamp
	 *            时间戳，格式为yyyyMMddHHmmss。例如：20180101142513
	 * @param apiVersion
	 *            API接口版本，当前固定为1.0
	 * @param authToken
	 *            用户登录时返回的Token
	 * @param paramSign
	 *            参数签名
	 * @return
	 */
	@POST
	@GET
	@At("/noticeUserLogout")
	public Object noticeUserLogout(@Param("app_id") String appId, @Param("timestamp") String timestamp,
			@Param("api_version") String apiVersion, @Param("auth_token") String authToken,
			@Param("param_sign") String paramSign) {
		// 判断共有参数
		try{
		List<String> params =new ArrayList<String>();
		ReturnCode code =  super.parametersCheck(appId,timestamp,apiVersion,authToken,paramSign,params);
		if(!code.codeEquals(ReturnCode.CODE_100000)){
			return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
		}

		UserLoginInfo userLoginInfo = super.getUserLoginInfo(authToken);
		if (userLoginInfo == null) {
			return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
		}
		ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
		loginInfoCache.logout(ApiConfig.REDIS_PREFIX_LOGIN+ authToken);
		Map<String, String> result = new HashMap<String, String>();
		return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
		}catch(Exception e){
				log.error("noticeUserLogout("+appId+","+timestamp+","+apiVersion+","+authToken+","+paramSign+")",e);
		}
		return ResultsImpl.parse(ReturnCode.CODE_103023.getCode(), ReturnCode.CODE_103023.getValue());
	}
}