package lgjt.web.app.module.sms;

import com.ttsx.util.cache.domain.UserLoginInfo;
import com.ttsx.util.cache.util.StringUtil;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import lgjt.common.base.Authority;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.constants.SmsType;
import lgjt.common.base.utils.EncryptionUtils;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.common.base.utils.RandomNumberUtils;
import lgjt.common.base.utils.RegularUtils;
import lgjt.common.sms.Sms;
import lgjt.domain.app.systask.secretkey.SysSecretKey;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;

import java.util.ArrayList;
import java.util.List;

/**
 * show 短信服务类.
 * @author daijiaqi
 * @date 2018/5/6 23:49
 */

@At("/app/sms")
@IocBean
@Log4j
public class SmsModule extends AppBaseModule {

    @Inject("sysSecretKeyService")
    private SysSecretKeyService sysSecretKeyService;


    
    /**
     * show 发送验证码.
     * @author daijiaqi
     * @date 2018/5/6 23:49
     * @param phoneNumber 手机号
     * @param smsType 短信类型  0：密码重置   1：注册   2：修改手机号  3：登录
     * @param type  登录方式  0：通过手机号登录   1：通过身份证号登录
     * @return 验证码发送成功/失败信息
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/sendSmsCode")
    @POST
    @GET
    public Object sendSmsCode(@Param("phoneNumber") String phoneNumber,@Param("smsType") String smsType,@Param("type") String type)
    {
        try {
        	
        	//判断登录方式--type为1时，将获取到的手机号解密
			if("1".equals(type)){
				phoneNumber = EncryptionUtils.getEncryption(phoneNumber);
			}
			
            if(!SmsType.SMS_TYPE_USER_SIGIN.getCode().equals(StringUtil.trim(smsType))&&
                    !SmsType.SMS_TYPE_PASSWORD_RESET.getCode().equals(StringUtil.trim(smsType))&&
                    !SmsType.SMS_TYPE_USER_UPDATE_PHONENUMBER.getCode().equals(StringUtil.trim(smsType))&&
                    !SmsType.SMS_TYPE_USER_LOGIN.getCode().equals(StringUtil.trim(smsType))){
                return ResultsImpl.parse(ReturnCode.CODE_103027.getCode(), ReturnCode.CODE_103027.getValue());
            }


            //判断手机号
            if(!RegularUtils.matchesPhoneNumber(phoneNumber)){
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }

            //发送随机6位验证码
            String smdCode =RandomNumberUtils.getRandomNumberBy(6)+"";

            //校验验证码是否存在 防止重复发送多次
            ReturnCode exitCode =ParameterVerificationUtils.checkSmsCodeFromRedis(AppConfig.REDIS_PREFIX_SMS, phoneNumber, smsType);
            if(!exitCode.codeEquals(ReturnCode.CODE_100000)){
                return ResultsImpl.parse(exitCode.getCode(), exitCode.getValue());
            }
            //设置验证码
            try{
                //发送验证码
                if(!Sms.getInstance().send(phoneNumber,AppConfig.SMS_TEMPID_USER_SIGIN,new String[]{smdCode})){
                    throw new Exception("send exception");
                }
                //设置验证码到缓存
                int expireTime=300;
                try {
                    expireTime = Integer.parseInt(AppConfig.REDIS_EXPIRE_TIME_SMS);
                }catch(Exception e){}
                ReturnCode smsReturnCode= ParameterVerificationUtils.setSmsCodeToRedis(AppConfig.REDIS_PREFIX_SMS, phoneNumber, smsType,smdCode,expireTime);
                if (!smsReturnCode.getCode().equals(ReturnCode.CODE_100000.getCode())) {
                    return ResultsImpl.parse(smsReturnCode.getCode(), smsReturnCode.getValue());
                }
            }catch(Exception e){
                log.error("smsSend:"+e.getMessage(),e);
                return ResultsImpl.parse(ReturnCode.CODE_103012.getCode(), ReturnCode.CODE_103012.getValue());
            }

            }
            catch(Exception e){
                log.error("sendSmsCode("+phoneNumber+","+smsType+")",e);
                return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
            }
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
    }
    
    
}
