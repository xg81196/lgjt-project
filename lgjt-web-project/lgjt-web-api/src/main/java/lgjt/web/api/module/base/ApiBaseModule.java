package lgjt.web.api.module.base;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.IocUtils;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.domain.api.systask.secretkey.SysSecretKey;
import lgjt.services.api.secretkey.SysSecretKeyService;
import lgjt.web.api.config.ApiConfig;


import java.util.ArrayList;
import java.util.List;

/**
 * Module 基础类
 * @author daijiaqi
 * @date 2018/5/912:02
 */

public class ApiBaseModule {

    private SysSecretKey sysSecretKey;
    /**
     * 基础参数校验
     * @param appId
     * @param timestamp
     * @param apiVersion
     * @param authToken
     * @param paramSign
     * @return
     */
    public ReturnCode parametersCheck(String appId,String timestamp, String apiVersion,String authToken,String paramSign,List<String> otherParams){
        // 判断共有参数
        ReturnCode returnCode = ParameterVerificationUtils.checkStandardParameters(appId, timestamp, apiVersion,
                authToken, paramSign);
        if (!returnCode.codeEquals(ReturnCode.CODE_100000)) { return returnCode; }
        // 判断 appId是否存在
        SysSecretKey sysSecretKey = IocUtils.getBean(SysSecretKeyService.class).get(appId);
        if (sysSecretKey == null) { return ReturnCode.CODE_101003; }
       this.sysSecretKey=sysSecretKey;
        // 正常的处理逻辑 key = REDIS_PREFIX+appId+day;
        String communicationKey = ParameterVerificationUtils.getCommunicationKey(ApiConfig.REDIS_PREFIX_COMMUNICATIONKEY, appId, timestamp);
        if (communicationKey.length() == 0) {return ReturnCode.CODE_101010; }
        List<String> params =new ArrayList<String>();
        params.add("app_id=" + appId);
        params.add("timestamp=" + timestamp);
        params.add("auth_token=" + authToken);
        params.add("api_version=" + apiVersion);

        if(otherParams!=null && otherParams.size()>0){
            params.addAll(otherParams);
        }
        // 判断签名是否正确
        String paramSignLocal = ParameterVerificationUtils
                .md5(ParameterVerificationUtils.parametersSort(params) + "|key=" + communicationKey);
        if (!paramSignLocal.equalsIgnoreCase(paramSign)) {return ReturnCode.CODE_101009 ; }
        return ReturnCode.CODE_100000;
    }

    /**
     * 根据用户token 获取登录用户
     * @param authToken 用户token
     */
    public UserLoginInfo getUserLoginInfo(String authToken){
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfoAndRefresh(ApiConfig.REDIS_PREFIX_LOGIN+ authToken);
        return  userLoginInfo;
    }

    /**
     * 获取当前秘钥的组织ID
     * @return
     */
    public String getSysSecretKeyOrgId(){
        return (this.sysSecretKey==null?null:this.sysSecretKey.getOrgId());
    }
}
