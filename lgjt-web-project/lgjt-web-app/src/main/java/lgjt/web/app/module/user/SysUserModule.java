package lgjt.web.app.module.user;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.impl.client.HttpClients;
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
import lgjt.common.base.utils.*;
import lgjt.domain.app.systask.secretkey.SysSecretKey;
import lgjt.domain.app.systask.sysUserContrast.SysUserContrast;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.services.app.sysUserContrast.SysUserContrastService;
import lgjt.services.app.user.SysUserAuthService;
import lgjt.services.app.user.SysUserService;
import lgjt.services.mongodb.company.MongodbService;
import lgjt.vo.app.user.SysUserVo;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;
import lgjt.web.app.utils.HttpUtil;
import lgjt.web.app.utils.LoginUtil;
import lgjt.web.app.utils.SensitiveInfoUtils;
import lgjt.web.app.utils.SysUserUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * show 用户管理.
 *
 * @author daijiaqi
 * @date 2018年4月25日
 */
@At("/sysUser")
@IocBean
@Log4j
public class SysUserModule extends AppBaseModule {
    @Inject("sysUserAuthService")
    SysUserAuthService sysUserAuthService;

    @Inject("sysUserService")
    SysUserService sysUserService;

    @Inject("sysSecretKeyService")
    SysSecretKeyService sysSecretKeyService;

    @Inject("sysUserContrastService")
    SysUserContrastService sysUserContrastService;

    @Inject("sysOrganizationService")
    SysOrganizationService sysOrganizationService;


    /**
     * show 密码修改.
     *
     * @param oldPassword     旧密码   Md5加密
     * @param newPassword     新密码  Md5加密
     * @param confirmPassword 确认密码  Md5加密
     * @return 修改密码成功/失败信息
     * @author daijiaqi
     * @date 2018年4月25日
     */

    @At("/editUserPassword")
    @POST
    @GET
    public Object editUserPassword(@Param("oldPassword") String oldPassword,
                                   @Param("newPassword") String newPassword, @Param("confirmPassword") String confirmPassword) {

        try {

            UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
            if (userLoginInfo == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
            }
            lgjt.domain.app.user.SysUserVo sysUser = sysUserService.getByUserName(userLoginInfo.getUserName());
            if (!sysUser.getPassword().equals(DigestUtils
                    .md5Hex(ParameterVerificationUtils.base64Decode(StringUtil.trim(oldPassword)) + sysUser.getSalt()))) {
                return ResultsImpl.parse(ReturnCode.CODE_103001.getCode(), ReturnCode.CODE_103001.getValue());
            }

            //判断密码
            if (StringUtil.trim(newPassword).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103010.getCode(), ReturnCode.CODE_103010.getValue());
            }
            //判断确认密码
            if (StringUtil.trim(confirmPassword).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103015.getCode(), ReturnCode.CODE_103015.getValue());
            }
            //密码与确认密码
            if (!newPassword.equals(confirmPassword)) {
                return ResultsImpl.parse(ReturnCode.CODE_103016.getCode(), ReturnCode.CODE_103016.getValue());
            }
            sysUser.setPassword(DigestUtils
                    .md5Hex(ParameterVerificationUtils.base64Decode(newPassword) + sysUser.getSalt()));
            sysUser.setUpdIp(ClientInfo.getIp());
            sysUser.setUpdUser(sysUser.getUpdUser());
            sysUser.setUpdTime(new Date());
            int updateCount = sysUserService.update(sysUser);
            if (updateCount > 0) {
                return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
            }
        } catch (Exception e) {
            log.error("oldPassword=" + oldPassword + ",newPassword=" + newPassword + ",confirmPassword=" + confirmPassword + ")", e);
        }
        return ResultsImpl.parse(ReturnCode.CODE_103023.getCode(), ReturnCode.CODE_103023.getValue());
    }


    /**
     * show 修改用户信息.
     *
     * @param sysUser 用户信息表，对应lgjt.domain.app.user中的实体类SysUser
     * @return 修改成功/失败信息
     * @author daijiaqi
     * @date 2018年4月25日
     */
    @At("/editUserInfo")
    @POST
    @GET
    public Object editUserInfo(@Param("..") SysUser sysUser) {
        try {
            UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
            if (userLoginInfo == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
            }
            //ID比对
            lgjt.domain.app.user.SysUserVo sysUserFromDB = sysUserService.getByUserName(userLoginInfo.getUserName());
            if (sysUser.getId() == null || sysUserFromDB == null || !(sysUser.getId().toString().equals(sysUserFromDB.getId()))) {
                return ResultsImpl.parse(ReturnCode.CODE_103022.getCode(), ReturnCode.CODE_103022.getValue());
            }

            // 不允許修改
            sysUser.setUserName(null);
            sysUser.setStatus(null);
            sysUser.setUserType(null);
            sysUser.setPassword(null);
            sysUser.setPhoneNumber(null);
            sysUser.setSalt(null);
            sysUser.setCrtUser(null);
            sysUser.setCrtIp(null);
            sysUser.setCrtTime(null);
            sysUser.setUpdUser(sysUser.getUserName());
            sysUser.setUpdIp(ClientInfo.getIp());
            sysUser.setUpdTime(new Date());
            int updateCount = sysUserService.updateIgnoreNull(sysUser);
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
        } catch (Exception e) {
            String logUserInfo = "";
            try {
                logUserInfo = JSONObject.toJSONString(sysUser);
            } catch (Exception e1) {
            }
            log.error("userInfo=" + logUserInfo + ")", e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
    }

    /**
     * show 通过当前用户，获取用户信息.
     *
     * @return 用户信息Object
     * @author daijiaqi
     * @date 2018年4月25日
     */
    @At("/getUserInfo")
    @POST
    @GET
    public Object getUserInfo() {
        try {
            UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
            if (userLoginInfo == null) {
                return ResultsImpl.parse(ReturnCode.CODE_2.getCode(), ReturnCode.CODE_2.getValue());
            }
            //		String userName = StringUtil.trim(userLoginInfo.getUserName());
            //获取用户对象
            lgjt.domain.app.user.SysUserVo sysUser = sysUserService.getByUserName(userLoginInfo.getUserName());
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), sysUser);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
        return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
    }


    /**
     * show 密码重置.
     *
     * @param phoneNumber     电话号
     * @param password        密码
     * @param confirmPassword 确认密码
     * @param smsCode         短信验证码
     * @return 重置成功/失败信息
     * @author daijiaqi
     * @date 2018年4月25日
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/passwordReset")
    @POST
    @GET
    public Object passwordReset(@Param("phoneNumber") String phoneNumber, @Param("password") String password,
                                @Param("confirmPassword") String confirmPassword, @Param("smsCode") String smsCode) {
        try {

            //判断手机号
            if (!RegularUtils.matchesPhoneNumber(phoneNumber)) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }
            SysUser sysUser = sysUserService.getByPhoneNumber(phoneNumber);
            //判断用户是否存在
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }
            //判断用户状态
            if (sysUser.getStatus() == SysUser.STATUS_DISABLE) {
                return ResultsImpl.parse(ReturnCode.CODE_103017.getCode(), ReturnCode.CODE_103017.getValue());
            }
            //判断密码
            if (StringUtil.trim(password).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103010.getCode(), ReturnCode.CODE_103010.getValue());
            }
            //判断确认密码
            if (StringUtil.trim(confirmPassword).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103015.getCode(), ReturnCode.CODE_103015.getValue());
            }
            //密码与确认密码
            if (!password.equals(confirmPassword)) {
                return ResultsImpl.parse(ReturnCode.CODE_103016.getCode(), ReturnCode.CODE_103016.getValue());
            }

            //验证验证码
            String redisSmsCode = ParameterVerificationUtils.getSmsCodeToRedis(AppConfig.REDIS_PREFIX_SMS, phoneNumber, SmsType.SMS_TYPE_PASSWORD_RESET.getCode());
            if (redisSmsCode == null || !redisSmsCode.equals(smsCode)) {
                return ResultsImpl.parse(ReturnCode.CODE_103014.getCode(), ReturnCode.CODE_103014.getValue());
            }

            //更新必要字段
            SysUser updateUser = new SysUser();
            updateUser.setId(sysUser.getId());
            updateUser.setPassword(DigestUtils
                    .md5Hex(ParameterVerificationUtils.base64Decode(password) + sysUser.getSalt()));
            updateUser.setUpdIp(ClientInfo.getIp());
            updateUser.setUpdUser(sysUser.getUpdUser());
            updateUser.setUpdTime(new Date());
            int updateCount = sysUserService.updateIgnoreNull(updateUser);
            if (updateCount == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103018.getCode(), ReturnCode.CODE_103018.getValue());
            }
        } catch (Exception e) {
            log.error(e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
        return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
    }

    /**
     * show 用户注册.
     *
     * @param sysUser         用户信息表，对应lgjt.domain.app.user中的实体类SysUser
     * @param registerType    注册类型
     * @param confirmPassword 确认密码
     * @param smsCode         短信验证码
     * @return 用户注册成功/失败信息
     * @author daijiaqi
     * @date 2018年4月25日
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/signIn")
    @POST
    @GET
    public Object signIn(@Param("..") SysUser sysUser,
                         @Param("register_type") Integer registerType, @Param("confirmPassword") String confirmPassword, @Param("smsCode") String smsCode) {
        try {

            //验证手机号合法性
            if (StringUtil.trim(sysUser.getPhoneNumber()).length() == 0
                    || !RegularUtils.matchesPhoneNumber(sysUser.getPhoneNumber())) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }
            //验证验证码
            String redisSmsCode = ParameterVerificationUtils.getSmsCodeToRedis(AppConfig.REDIS_PREFIX_SMS, sysUser.getPhoneNumber(), SmsType.SMS_TYPE_USER_SIGIN.getCode());
            if (redisSmsCode == null || !redisSmsCode.equals(smsCode)) {
                return ResultsImpl.parse(ReturnCode.CODE_103014.getCode(), ReturnCode.CODE_103014.getValue());
            }
            // 启用
            if (sysUser.getStatus() == null) {
                sysUser.setStatus(SysUser.STATUS_ENABLED);
            }
            // 性别
            if (sysUser.getSex() == null) {
                sysUser.setSex(SysUser.SEX_FEMALE);
            }
            // 一般用户
//			if (sysUser.getUserType() == null) {
            sysUser.setUserType(SysUser.USERTYPE_ORDINARYUSER);
//			}
            //判断密码
            if (StringUtil.trim(sysUser.getPassword()).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103010.getCode(), ReturnCode.CODE_103010.getValue());
            }
            //判断确认密码
            if (StringUtil.trim(confirmPassword).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103015.getCode(), ReturnCode.CODE_103015.getValue());
            }
            //密码与确认密码
            if (!sysUser.getPassword().equals(confirmPassword)) {
                return ResultsImpl.parse(ReturnCode.CODE_103016.getCode(), ReturnCode.CODE_103016.getValue());
            }
            if (sysUser.getRealName() == null) {
                sysUser.setRealName(sysUser.getUserName());
            }
            String uid = UUIDUtil.getUUID();
            //和学习系统保持一直
            sysUser.setId(uid);
            //给个默认企业
            sysUser.setComId("xgry");
            sysUser.setSalt(RandomStringUtils.randomAlphanumeric(20));
            sysUser.setPassword(DigestUtils
                    .md5Hex(ParameterVerificationUtils.base64Decode(sysUser.getPassword()) + sysUser.getSalt()));
            sysUser.setCrtUser(sysUser.getUserName());
            sysUser.setCrtIp(ClientInfo.getIp());
            sysUser.setCrtTime(new Date());

            if (StringUtil.trim(sysUser.getUserName()).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103009.getCode(), ReturnCode.CODE_103009.getValue());
            }

            if (sysUserService.getByUserNameOrPhoneNumber(sysUser.getRealName(), sysUser.getPhoneNumber()) > 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103008.getCode(), ReturnCode.CODE_103008.getValue());
            }

            //调用 学习系统用户插入接口 开始
            //http://127.0.0.1:20020/admin/user/insert
            // userName|userRealName|accountType|sex|password|companyId|orgId
            // (其中参数sex默认传0(0：女，1：男) accountType传入3、password可以固定为111111 、companyId传入ngqy、orgId传入nvorg )
//			log.info("http .../user/insert begin....");
//			if(true){
//				String url =AppConfig.USER_URL+"/admin/user/insert";
//				HashMap<String,String> params =new HashMap<>();
//				params.put("id",uid);
//				params.put("userName",sysUser.getUserName());
//				params.put("userRealName","游客");
//				params.put("accountType","3");
//				params.put("sex","0");
//				params.put("password","111111");
//				params.put("companyId","ngqy");
//				params.put("orgId","nvorg");
//				HashMap<String,String> headers=new HashMap<>();
////				headers.put("ttsx_auth_token","");
//				JSONObject obj =null;
//				String compName="";
//
////				try{
////					obj = HttpUtil.post( HttpClients.createDefault(), url, params, headers);
//////					JSONObject postObj = HttpUtil.post(HttpClients.createDefault(), url,params,headers);
//////					log.info("postObj="+(postObj==null?null:postObj.toJSONString()));
////				}catch(Exception e){
////					log.error("http:("+sysUser.getUserName()+",游客,"+3+","+0+",111111,ngqy,nvorg"+"::"+(obj==null?obj:obj.toJSONString())+")",e);
////					throw new Exception("注册报错:"+e.getMessage());
////				}
//			}
//			log.info("http .../user/insert end....");
            //调用 学习系统用户插入接口 结束
            SysUser sysUserResult = sysUserService.insert(sysUser);
            //防篡改


            sysUserResult.setPassword(null);
            sysUserResult.setSalt(null);
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(),
                    sysUserResult);
        } catch (Exception e) {
            log.error("user_name=" + sysUser.getUserName()
                    + ",user_passwd=" + sysUser.getPassword() + ",user_phone=" + sysUser.getPhoneNumber()
                    + ",register_type=" + registerType + ",confirmPassword=" + confirmPassword + ",smsCode=" + smsCode + ")", e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
    }

    /**
     * show 用户绑定.
     *
     * @param appId         对接系统ID
     * @param timestamp     时间戳，格式为yyyyMMddHHmmss。例如：20180101142513
     * @param apiVersion    API接口版本，当前固定为1.0
     * @param authToken     用户在对接系统中的唯一ID
     * @param paramSign     参数签名
     * @param companyUserId 企业用户ID。
     * @param sysUserId     系统用户ID。标识系统用户ID
     * @param idType        证件类型
     * @param idNo          证件号
     * @return 用户绑定成功/失败信息
     * @author daijiaqi
     * @date 2018年4月27日
     */
    @At("/userBinding")
    @POST
    @GET
    public Object userBinding(@Param("app_id") String appId, @Param("timestamp") String timestamp,
                              @Param("api_version") String apiVersion, @Param("auth_token") String authToken,
                              @Param("param_sign") String paramSign, @Param("company_user_id") String companyUserId,
                              @Param("sys_user_id") String sysUserId, @Param("id_type") String idType,
                              @Param("user_card_id") String idNo) {
        try {

            List<String> parameters = new ArrayList<String>();
            parameters.add("company_user_id=" + companyUserId);
            parameters.add("sys_user_id=" + sysUserId);
            parameters.add("id_type=" + idType);
            parameters.add("user_card_id=" + idNo);

            ReturnCode code = super.parametersCheck(appId, timestamp, apiVersion, authToken, paramSign, parameters);
            if (!code.codeEquals(ReturnCode.CODE_100000)) {
                return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
            }
            //判断登录
            UserLoginInfo userLoginInfo = super.getUserLoginInfo(AppConfig.REDIS_PREFIX_LOGIN + authToken);
            if (userLoginInfo == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
            }

            // 用户认证
            SysUser sysUser = sysUserService.get(sysUserId);
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }
            DBCollection dbCollection = MongodbService.getInstance("1").getDBCollectionByName(appId);
            DBObject dbObjectQuery = new BasicDBObject("id", companyUserId);
            DBObject dbObject = dbCollection.findOne(dbObjectQuery);
            if (dbObject == null || dbObject.get("userName") == null || dbObject.get("id") == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103004.getCode(), ReturnCode.CODE_103004.getValue());
            }
            // 关联
            SysUserContrast sysUserContrast = new SysUserContrast();
            sysUserContrast.setAppId(appId);
            sysUserContrast.setCompanyUserId(companyUserId);
            sysUserContrast.setUserName(sysUser.getUserName());
            sysUserContrast.setSysUserId(sysUserId);

            sysUserContrastService.insert(sysUserContrast);
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
        } catch (Exception e) {
            log.error("userBinding(" + appId + "," + timestamp + "," + apiVersion + "," + authToken + "," + paramSign
                    + "," + companyUserId + "," + sysUserId + "," + idType + "," + idNo + ")");
            return ResultsImpl.parse(ReturnCode.CODE_103005.getCode(), ReturnCode.CODE_103005.getValue());
        }
    }

    /**
     * show 通过手机号查询找回用户名.
     *
     * @param phoneNumber 手机号码
     * @param smsCode     短信验证码
     * @return 成功/失败信息+用户名信息
     * @author daijiaqi
     * @date 2018年4月25日
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/getUserNameByPhoneNumber")
    @POST
    @GET
    public Object getUserNameByPhoneNumber(@Param("phoneNumber") String phoneNumber, @Param("smsCode") String smsCode) {

        try {
            //判断手机号非空
            if (StringUtil.trim(phoneNumber).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }
            //判断验证码非空
            if (StringUtil.trim(smsCode).length() == 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103027.getCode(), ReturnCode.CODE_103027.getValue());
            }

            //验证手机号的合法性
            if (StringUtil.trim(phoneNumber).length() == 0 || !RegularUtils.matchesPhoneNumber(phoneNumber)) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }

            //获取该手机号的用户对象
            SysUser sysUser = sysUserService.getByPhoneNumber(phoneNumber);

            //判断数据库是否存在该手机号
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }

            //判断验证码
            String redisCode = ParameterVerificationUtils.getSmsCodeToRedis(AppConfig.REDIS_PREFIX_SMS, phoneNumber, SmsType.SMS_TYPE_PASSWORD_RESET.getCode());
            if (redisCode == null || !redisCode.equals(smsCode)) {
                return ResultsImpl.parse(ReturnCode.CODE_103014.getCode(), ReturnCode.CODE_103014.getValue());
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userName", sysUser.getUserName());
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), map);

        } catch (Exception e) {
            log.error(e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
    }

    /**
     * show 通过手机号查找身份证号.
     *
     * @param phoneNumber 用户手机号
     * @return 身份证号+type（0：原始手机号  1：加密手机号）当前为0
     * @author wangyu
     * @date 2018年4月25日
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/getIdNoByPhoneNumber")
    @POST
    @GET
    public Object getIdNoByPhoneNumber(@Param("phoneNumber") String phoneNumber) {

        try {
            //验证手机号的合法性
            if (StringUtil.trim(phoneNumber).length() == 0 || !RegularUtils.matchesPhoneNumber(phoneNumber)) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            }

            //获取该手机号的用户对象
            SysUser sysUser = sysUserService.getIdNoByPhoneNumber(phoneNumber);

            //判断数据库是否存在该手机号
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }
            HashMap<String, String> map = new HashMap<String, String>();
            //判断idNo是否为空
            if (sysUser.getIdNo() == null) {
                map.put("idNo", "");
                map.put("type", "0");
                return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), map);
            } else {
//				String idNo = sysUser.getIdNo().replaceAll("(\\d{4})\\d{10}(\\d{4})","$1****$2");//?
                String idNo = SensitiveInfoUtils.idCardNum(sysUser.getIdNo());
                map.put("idNo", idNo);
                map.put("type", "0");
                return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), map);
            }

        } catch (Exception e) {
            log.error("SysUserModule:getIdNoByPhoneNumber(phoneNumber=" + phoneNumber, e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
    }


    /**
     * show 通过身份证号查找手机号.
     *
     * @param idNo 用户身份证号
     * @return 返回密文手机号、加密手机号、type（0：原始手机号  1：加密手机号）当前为1
     * @author wangyu
     * @date 2018年4月25日
     */
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    @At("/getPhoneNumberByIdNo")
    @POST
    @GET
    public Object getPhoneNumberByIdNo(@Param("idNo") String idNo) {

        try {

            //验证身份证号的合法性
            if (StringUtil.trim(idNo).length() == 0 || !IdNumberUtils.validateIdCard(idNo)) {
                return ResultsImpl.parse(ReturnCode.CODE_103030.getCode(), ReturnCode.CODE_103030.getValue());
            }

            //获取该身份证号的用户对象
            SysUser sysUser = sysUserService.getPhoneNumberByIdNo(idNo);

            //判断数据库是否存在该身份证号
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }

            //判断手机号是否为空
            if (sysUser.getPhoneNumber() == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103011.getCode(), ReturnCode.CODE_103011.getValue());
            } else {
//				String phoneNumber = sysUser.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                String phoneNumber = SensitiveInfoUtils.mobilePhone(sysUser.getPhoneNumber());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phoneNumber1", phoneNumber.trim());
                map.put("phoneNumber2", EncryptionUtils.getEncryption(sysUser.getPhoneNumber().trim()));
                map.put("type", "1");
                return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), map);
            }

        } catch (Exception e) {
            log.error("SysUserModule:getPhoneNumberByIdNo(idNo=" + idNo, e);
            return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
        }
    }
}