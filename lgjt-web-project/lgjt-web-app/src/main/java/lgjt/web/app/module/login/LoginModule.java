package lgjt.web.app.module.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.Authority;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.domain.app.org.SysOrganization;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.config.SysConfigService;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.services.app.user.SysUserAuthService;
import lgjt.services.app.user.SysUserService;
import lgjt.vo.app.user.SysUserVo;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;
import lgjt.web.app.utils.LoginUtil;
import lgjt.web.app.utils.SysUserUtil;

/**
 * show 登录接口.
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-9 下午4:50:45
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-9 下午4:50:45
 * @UpdateRemark: 说明本次修改内容
 */
@At("/user")
@IocBean
@Log4j
public class LoginModule extends AppBaseModule {


	@Inject("sysUserService")
	SysUserService sysUserService;
	@Inject("sysSecretKeyService")
	SysSecretKeyService sysSecretKeyService;
	@Inject("sysOrganizationService")
	SysOrganizationService sysOrganizationService;
	@Inject("sysUserAuthService")
	SysUserAuthService sysUserAuthService;
	@Inject("sysConfigService")
	SysConfigService sysConfigService;


	/**
	 * show 通过用户名和密码登录.
	 * @author daijiaqi
     * @date 2018/5/1114:39
	 * @param userName 用户名
	 * @param password 密码
	 * @return 登录成功/失败信息+用户信息
	 */
	@POST
	@GET
	@Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
	@At("/login")
	public Object login(@Param("userName") String userName, @Param("password") String password) {
		try {

			// 判断用户是否存在
			lgjt.domain.app.user.SysUserVo sysuser = sysUserService.getByUserName(userName);
//			SysUser sysUserBySql = sysUserAuthBySqlService.getByUserName(userName);
			if (sysuser == null) {
				return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
			}
			//判断用户状态
			if(sysuser.getStatus()==SysUser.STATUS_DISABLE) {
				return ResultsImpl.parse(ReturnCode.CODE_103017.getCode(), ReturnCode.CODE_103017.getValue());
			}
			//获取加密密码
			String userPasswordMd5 = StringUtil.trim(DigestUtils.md5Hex(password=ParameterVerificationUtils.base64Decode(password) + sysuser.getSalt()));

			// 判断密码
			if (!userPasswordMd5.equals(sysuser.getPassword())) {
				return ResultsImpl.parse(ReturnCode.CODE_103001.getCode(), ReturnCode.CODE_103001.getValue());
			}

			SysOrganization sysOrganization = sysOrganizationService.getById(sysuser.getComId());
			ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
			loginInfoCache.setExpireTime(Integer.parseInt(AppConfig.REDIS_EXPIRE_TIME));
			String token = UUIDUtil.getUUID();
			HashMap<String ,String> userMap =new HashMap<String,String>();
			userMap.put("userId",sysuser.getId());
			userMap.put("headPortrait", sysuser.getHeadPortrait());
			if (!Objects.isNull(sysOrganization)) {
				userMap.put("orgId",sysOrganization.getId());
				userMap.put("orgName",sysOrganization.getName());
			}

			loginInfoCache.login(AppConfig.REDIS_PREFIX_LOGIN + token,userName, sysuser.getRealName(), ClientInfo.getIp(), userMap);

			
			//调用backend 接分接口
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", token);
			sysuser.setPassword(null);
			sysuser.setSalt(null);

			//赋值用户 相关属性
			SysUserVo sysUserVo = new SysUserVo();
			BeanUtils.copyProperties(sysUserVo,sysuser);
			SysUserUtil.setUserPropertys(sysUserVo,"0",sysOrganizationService);
			data.put("userInfo",sysUserVo);
			return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(),data);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
		}
	}


	/**
	 * 退出登录+解绑
	 * @param openId openId
	 * @return
	 */
	@POST
	@GET
	@At("/loginOut")
	public Object loginOut(@Param("openId") String openId) {
		//获取当前用户
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if(userLoginInfo==null){
			return	ResultsImpl.parse(ReturnCode.CODE_2.getCode(), ReturnCode.CODE_2.getValue());
		}
		String userName= userLoginInfo.getUserName();
		if(LoginUtil.loginOut()){
			//解除绑定
			openId=StringUtil.trim(openId);
			if(openId.length()>0){
//				SysUser sysUser = 	sysUserService.getSysUserByOpenId(openId);
				lgjt.domain.app.user.SysUserVo sysUser = sysUserService.getByUserName(userName);
				if(sysUser!=null){
					String openIds = StringUtil.trim(sysUser.getOpenIds());
					if(openIds.indexOf(openId)>=0){
						SysUser sysUserUpdate=new SysUser();
						sysUserUpdate.setId(sysUser.getId());
						sysUserUpdate.setOpenIds(openIds.replaceAll(openId,"").replaceAll(",+",","));
						sysUserService.updateIgnoreNull(sysUserUpdate);
					}else{
						return ResultsImpl.parse(ReturnCode.CODE_103032.getCode(), ReturnCode.CODE_103032.getValue());
					}
				}
			}
			//退出成功
			return	ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
		}else{
			//退出失敗
			return	ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());
		}

	}
}
