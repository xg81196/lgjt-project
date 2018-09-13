package lgjt.web.backend.module.admin.login;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.BaseModule;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.util.StringUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.IocUtils;
import lgjt.common.base.utils.StaticUtils;
import lgjt.common.base.utils.TokenUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.role.SysMenu;
import lgjt.domain.backend.role.SysRole;
import lgjt.domain.backend.role.vo.SysRoleMenuVo;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.org.SysUnionComService;
import lgjt.services.backend.org.SysUnionService;
import lgjt.services.backend.role.SysMenuService;
import lgjt.services.backend.role.SysRoleService;
import lgjt.services.backend.user.SysUserAdminService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.domain.backend.utils.UserUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * @author wuguangwei
 * @depre 管理端登录相关
 */
@At("/admin/user")
@IocBean
@Log4j
public class AdminLoginModule extends BaseModule {

	
	@Inject
	SysUserAdminService sysUserAdminService;

	@Inject
	SysUserDataService sysUserDataService;

	@Inject
	SysMenuService sysMenuService;

	@Inject
	SysOrganizationService sysOrganizationService;

	@Inject
	SysUnionService sysUnionService;

	@Inject
	SysRoleService sysRoleService;


	/**
	 * 后台管理登录接口
	 * @param obj
	 * @param code
	 * @return
	 */

	@Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
	@At("/login")
	@POST
	public Object login(@Param("..") SysUserAdmin obj,@Param("code")String code) {

		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		String codeCompare = (String)new TokenUtils().getAttribute("CODE:"+token);
		if(StringTool.isNotEmpty(code)&&StringTool.isNotEmpty(codeCompare)){
			if(!code.equalsIgnoreCase(codeCompare)){
				return Results.parse(Constants.STATE_FAIL,"验证码错误！");
			}

			SimpleCriteria cri=Cnd.cri();
			cri.where().andEquals("user_name", obj.getUserName());
			List<SysUserAdmin>  users = sysUserAdminService.query(SysUserAdmin.class,cri);

			SysUserAdmin sysUserAdmin = null;

			if( CollectionUtils.isNotEmpty(users) ){
				sysUserAdmin = users.get(0);
			}else {
				return Results.parse(Constants.STATE_FAIL,"用户名或密码错误！");
			}

			if(sysUserAdmin.getUserName() != null){
				if (sysUserAdmin.getStatus() != null) {
					if(sysUserAdmin.getStatus() == 1){
						return Results.parse(Constants.STATE_FAIL,"您的当前账户被禁用，请联系管理员！");
					}
				}

               //前端已经对密码进行加密
				String password = DigestUtils.md5Hex(obj.getPassword()+sysUserAdmin.getSalt());

                if(!password.equals(sysUserAdmin.getPassword())){
                    return Results.parse(Constants.STATE_FAIL,"用户名或密码错误！");
                }
				/*List<SysUserData> sysUserDataList = sysUserDataService.getOrgsByUserId(sysUserAdmin.getId());

                 SysUserData sysUserData = null;
                 if (CollectionUtils.isNotEmpty(sysUserDataList))  {
					 sysUserData  = sysUserDataList.get(0);
				 }

				SysOrganization sysOrganization = null;
				SysUnion sysUnion = null;
				if ( sysUserData != null ) {
                     if (UserDataType.UNION_TYPE == sysUserData.getObjectType())  {
						 sysUnion = sysUnionService.get(sysUserData.getObjectId());
					 }else {
						 sysOrganization =  sysOrganizationService.get(sysUserData.getObjectId());
					 }
				}
*/
				List<SysRoleMenuVo> sysRoleMenuVoList = sysMenuService.queryUserRoleMenuByUserId(sysUserAdmin.getId());
                //用户对象存入缓存
				UserUtil.setAdminUser(token,sysRoleMenuVoList,sysUserAdmin,null,null);

				/*if (sysOrganization != null || sysUserData != null)  {
					if (sysOrganization != null ) {
						UserUtil.setAdminUser(token,sysRoleMenuVoList,sysUserAdmin,sysOrganization.getId(),sysOrganization.getName());
					}else{
						UserUtil.setAdminUser(token,sysRoleMenuVoList,sysUserAdmin,sysUnion.getId(),sysUnion.getName());
					}
				}*/


                //用户菜单信息存入缓存
				applyAuthority(sysUserAdmin.getUserName(),token);
				//用户数据权限
				applyDataAuthority(sysUserAdmin.getId(),token);

				/**
				 * 角色
				 */
				applyRole ();

				/*
                2018-07-20 赵天意
                BUG 11289
                这里要判断该登录用户是至少有一个或以上的菜单权限
                如果都没有，返回错误信息
                 */

				if(UserUtil.getMenu().size() <= 1) {
					return Results.parse(Constants.STATE_FAIL,"没有菜单权限，请联系管理员！");
				}


				return Results.parse(Constants.STATE_SUCCESS, null, sysUserAdmin);
			} else {
				return Results.parse(Constants.STATE_FAIL,"用户名或密码错误！");	
			}
		}else {
			return Results.parse(Constants.STATE_FAIL,"请输入验证码！");
		}
	}


	private void applyRole (){
		List<SysRole> sysRoleList = sysRoleService.queryUserRole();

		StringJoiner sj = new StringJoiner(",");
		for ( SysRole sysRole : sysRoleList) {
			sj.add(sysRole.getRoleName());
		}
		UserUtil.setRoles(sj.toString());
	}
	
	private void applyAuthority(String userName,String token) {
		// 获取登陆用户的权限和权限所对应的资源
		List<SysMenu> resources = sysUserAdminService.getUserMenu(userName);

		// 查询所有父节点
		List<SysMenu> topResources = sysUserAdminService.getTopMenu();

		topResources.add(0, this.bindFixedRes());

		List<SysMenu> renderResource = new ArrayList<SysMenu>();
		renderResource.add(this.bindFixedRes());

		for (SysMenu topResource : topResources) {
			String topResouceId = topResource.getId();
			for (SysMenu resource : resources) {
				if (resource.getParentId().equals(topResouceId)) {
					topResource.getList().add(resource);
				}
			}
			// 过滤没有子节点的父节点
			if (topResource.getList().size() > 0) {
				renderResource.add(topResource);
			}
		}

		UserUtil.setMenu(renderResource,token);
		UserUtil.setAuth(resources,token);
	}


	private void applyDataAuthority(String userId,String token) {

		//数据权限范围ID列表
		Set<String> unionIdList = new HashSet<>();

		Set<String> comIdList = new HashSet<>();
		//Map<String,String> idsMap = new HashMap<>();


		//用户对应的数据权限ID列表
		SysUserDataService sysUserDataService = IocUtils.getBean(SysUserDataService.class);
		SysUnionComService sysUnionComService = IocUtils.getBean(SysUnionComService.class);
		SysUnionService sysUnionService = IocUtils.getBean(SysUnionService.class);


		List<SysUserData> sysUserDataList = sysUserDataService.getOrgsByUserId(userId);

		if(sysUserDataList !=null && sysUserDataList.size() > 0){

			for (SysUserData sysUserData : sysUserDataList) {
				/**
				 * 向下兼容
				 */
				if ( sysUserData.getCompatibility() == 0 ) {
					/**
					 * 工会类型
					 */
					if (UserDataType.UNION_TYPE == sysUserData.getObjectType()) {
						/**
						 * 工会下的企业
						 */
						List<SysUnionCom> sysUnionComList = sysUnionComService.getUnionComByUnionId(sysUserData.getObjectId());
						if (CollectionUtils.isNotEmpty(sysUnionComList))  {
							List<String> comIds = sysUnionComList.stream().map(SysUnionCom :: getComId).collect(Collectors.toList());
							comIdList.addAll(comIds);
						}
						/**
						 * 下属工会
						 */
						List<SysUnion> sysUnionList = sysUnionService.querySubUnionList(sysUserData.getObjectId());
						if (CollectionUtils.isNotEmpty(sysUnionList))  {
							List<String> unionIds = sysUnionList.stream().map(SysUnion :: getId).collect(Collectors.toList());
							/**
							 * 下属工会对应的企业
							 */
							for (String unid : unionIds) {
								List<SysUnionCom> unionComList = sysUnionComService.getUnionComByUnionId(unid);
								if (CollectionUtils.isNotEmpty(unionComList))  {
									List<String> comIds = unionComList.stream().map(SysUnionCom :: getComId).collect(Collectors.toList());
									comIdList.addAll(comIds);
								}
							}
							unionIdList.addAll(unionIds);
						}
					}  else {
						//TODO:企业的下属企业未作处理?
						comIdList.add(sysUserData.getObjectId());
					}

				} else {
					unionIdList.add(sysUserData.getObjectId());
				}
			}

		}

		//权限存入缓存
		UserUtil.setUserData(StringUtils.join(unionIdList, ",")
				,StringUtils.join(comIdList, ","),token);
	}
	
	
	private SysMenu bindFixedRes() {
		SysMenu resource = new SysMenu();
		resource.setId("0");
		resource.setName("首页");
		resource.setImg("fa fa-home");
		resource.setView("admin.main.quickmenu");
		resource.setOption("admin.option.test.quickmenu");
		resource.setList(new ArrayList<>());
		return resource;
	}

	/**
	 * 判断是否登录
	 * @return
	 */
	@Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
	@At("/islogin")
	public Object islogin() {
		String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
		SysUserAdmin user = UserUtil.getAdminUser();
		if ( user != null ) {
			if(UserUtil.getMenu()==null){
				applyAuthority(user.getUserName(),token);
			}
			return Results.parse(Constants.STATE_SUCCESS, null);
		}
		return Results.parse(Constants.STATE_FAIL);
	}

	@At("/checkName")
	@Authority("USER_PLAYFORM")
	public Object checkName(@Param("..") SysUserAdmin obj) {
		if (sysUserAdminService.query(obj).size() > 0) {
			return Results.parse(Constants.STATE_FAIL);
		}
		return Results.parse(Constants.STATE_SUCCESS, null);

	}

	/**
	 * 退出登录
	 * @return
	 */
	@At("/logout")
	@Authority("USER")
	public Object logout() {
		try {
			UserUtil.invalidate();
			return Results.parse(Constants.STATE_SUCCESS);
		} catch (Exception e) {
			log.error("user logout error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/getToken")
	public Object getToken(){
		String tokenOld= StringUtil.trim(Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME));
		ILoginInfoCache cache = CacheFactory.getLoginInfoCache();
		if(tokenOld.length()>0 && cache.isLoginAndRefresh(tokenOld)){//如果老token还有效,返回老token
			Mvcs.getResp().setHeader(StaticUtils.TOKEN_NAME, tokenOld);
			return Results.parse(Constants.STATE_SUCCESS,null,tokenOld);
		}
		String token=cache.getNewToken();
		return Results.parse(Constants.STATE_SUCCESS,null,token);
	}

	/**
	 * 查询当前登录用户信息
	 *
	 * @return
	 */
	@At("/getuser")
/*	@Authority("USER_PLAYFORM")*/
	public Object getAdminUser() {
		try {
			return Results.parse(Constants.STATE_SUCCESS, null,
					UserUtil.getAdminUser());
		} catch (Exception e) {
			log.error("get manage user error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	/**
	 * 查询权限
	 * @return
	 */
	@At("/queryMenumapping")
	/*@Authority("USER_PLAYFORM")*/
	public Object queryMenumapping() {
		try {
			return Results.parse(Constants.STATE_SUCCESS, null,
					UserUtil.getMenu());
		} catch (Exception e) {
			log.error("query menu mapping error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	@At("/updatePassword")
	@Authority("USER_PLAYFORM")
	public Object updatePassword(@Param("..") SysUserAdmin user) {
		try {
			String oldpwd = user.getOldpwd();// 原密码
			String newpwd = user.getNewpwd();// 新密码
			String confpwd = user.getConfpwd();// 密码确认

			/*if (StringTool.isEmpty(oldpwd)) {
				return Results.parse(Constants.STATE_FAIL, "原密码不能为空");
			}*/
			if (StringTool.isEmpty(newpwd)) {
				return Results.parse(Constants.STATE_FAIL, "新密码不能为空");
			}
			/*if (!newpwd.equals(confpwd)) {
				return Results.parse(Constants.STATE_FAIL, "两次输入的密码不一致");
			}*/
			SysUserAdmin obj = new SysUserAdmin();
			obj.setUserName(user.getUserName());
			List<SysUserAdmin> list = sysUserAdminService.query(obj);
			if (CollectionUtils.isNotEmpty(list)) {
				SysUserAdmin sysUserAdmin = list.get(0);
				/*String oldPass = DigestUtils.md5Hex(user.getOldpwd()+sysUserAdmin.getSalt());
				if (! oldPass.equals(sysUserAdmin.getPassword()))
				   return Results.parse(Constants.STATE_FAIL, "原密码输入错误");*/
				sysUserAdmin.setNewpwd(newpwd);
				sysUserAdmin.setOldpwd(oldpwd);
				sysUserAdmin.setConfpwd(confpwd);
				sysUserAdminService.updatePassword(sysUserAdmin);
			}

			return Results.parse(Constants.STATE_SUCCESS, "密码修改成功");
		} catch (Exception e) {
			log.error("edit manage user password error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}


	/**
	 * 重置密码
	 * @param user
	 * @return
	 */
	@At("/resetPassword")
	public Object resetPassword(@Param("..") SysUserAdmin user,@Param("code")String code) {
		try {
			String username = user.getUserName();// 原密码
			String newpwd = user.getNewpwd();// 新密码
			String confpwd = user.getConfpwd();// 密码确认
			String phoneNumber = user.getPhoneNumber();

			String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
			String codeCompare = (String)new TokenUtils().getAttribute("CODE:"+token);
			if(StringTool.isNotEmpty(code)&&StringTool.isNotEmpty(codeCompare)) {
				if (!code.equalsIgnoreCase(codeCompare)) {
					return Results.parse(Constants.STATE_FAIL, "手机号不能为空！");
				}
			}

				if (StringTool.isEmpty(phoneNumber)) {
					return Results.parse(Constants.STATE_FAIL, "用户名不能为空!");
				}

				SysUserAdmin sysUser = sysUserAdminService.checkUser_phone(phoneNumber);

				if (Objects.isNull(sysUser)) {
					return Results.parse(Constants.STATE_FAIL, "手机号不存在!");
				}

			if (StringTool.isEmpty(username)) {
				return Results.parse(Constants.STATE_FAIL, "用户名不能为空");
			}

			SysUserAdmin sysUserAdmin = sysUserAdminService.checkUser_name(username);

			if (Objects.isNull(sysUserAdmin)) {
				return Results.parse(Constants.STATE_FAIL, "用户名不存在!");
			}

			if (StringTool.isEmpty(newpwd)) {
				return Results.parse(Constants.STATE_FAIL, "新密码不能为空!");
			}
			if (!newpwd.equals(confpwd)) {
				return Results.parse(Constants.STATE_FAIL, "两次输入的密码不一致!");
			}


			SysUserAdmin obj = new SysUserAdmin();
			obj.setUserName(user.getUserName());
			List<SysUserAdmin> list = sysUserAdminService.query(obj);
			if (CollectionUtils.isNotEmpty(list)) {
				SysUserAdmin users = list.get(0);
				users.setNewpwd(newpwd);
				users.setConfpwd(confpwd);
				sysUserAdminService.updatePassword(users);
			}

			return Results.parse(Constants.STATE_SUCCESS, "密码修改成功");
		} catch (Exception e) {
			log.error("edit manage user password error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("96e79218965eb72c92a549dd5a330112"+"111111"));

	}
}
