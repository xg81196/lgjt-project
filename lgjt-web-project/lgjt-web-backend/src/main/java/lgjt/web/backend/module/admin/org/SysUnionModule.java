package lgjt.web.backend.module.admin.org;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.CodeGenerateUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.services.backend.org.ImportOrgExcelService;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.org.SysUnionService;
import lgjt.services.backend.role.SysRoleService;
import lgjt.services.backend.user.SysUserAdminService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.web.backend.init.TreeCacheImpl;
import lgjt.web.backend.utils.ExportUtils;
import lgjt.domain.backend.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@At("/admin/union")
@IocBean
@Log4j
public class SysUnionModule {

    
	@Inject("sysOrganizationService")
	SysOrganizationService service;

	@Inject("sysUserDataService")
	SysUserDataService sysUserDataService;

	@Inject
	SysUserAdminService sysUserAdminService;

	@Inject
	SysRoleService sysRoleService;

	@Inject
	SysUnionService sysUnionService;



	@At("/get")
	@Authority("USER_COMPAPP")
	public Object get(@Param("id") String id) {
		SysUnion obj = sysUnionService.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/delete")
	@Authority("USER_COMPAPP")
	public Object delete(@Param("ids") String ids) {
		if(sysUnionService.delete(ids)>0) {
			TreeCacheImpl.getInstance().refresh();
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL,"工会或企业下有子企业或人员");
		}
	}
	
	@At("/save")
	@Authority("USER_COMPAPP")
	public Object insert(@Param("..") SysUnion obj) {
		SysUnion o = sysUnionService.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}


	@At("/insertUpdate")
	@Authority("USER_COMPAPP")
	public Object insertUpdate(@Param("..") SysUnion obj) {

		boolean result = false;
		if(StringTool.isNull(obj.getId())) {
			String id = UUIDUtil.getUUID();
			obj.setId(id);
			obj.setSuperId(obj.getOrgId());
			SysUnion sysUnion = sysUnionService.checkName(obj.getName());
			if ( !Objects.isNull(sysUnion)) {
				return Results.parse(Constants.STATE_FAIL,"工会名称重复");
			}
			sysUnionService.insertUnion(obj);

			/**
			 * 自动添加管理员权限
			 */
			/*if (SysUserAdmin.ADMIN.equals(UserUtil.getAdminUser().getUserName())) {*/
				initUserData(id, UserUtil.getAdminUser());
			/*} else {*/

				/**
				 * 添加对应的管理员
				 */
				SysUserAdmin sysAdmin = initSysUserAdmin(obj, id);
				/**
				 * 添加对应的菜单权限
				 */
				initUserRole(obj, sysAdmin);
				/**
				 * 添加数据权限
				 */
				initUserData(id, sysAdmin);
			/*}*/

			result = true;
		}else {
			int upd = sysUnionService.updateIgnoreNull(obj);
			if(upd>0) {
				result = true;
			}
		}
		TreeCacheImpl.getInstance().refresh();

		if(result) {
			return Results.parse(Constants.STATE_SUCCESS,null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	private void initUserData(String id, SysUserAdmin sysAdmin) {
		SysUserData sysUserData = new SysUserData();
		sysUserData.setId(UUIDUtil.getUUID());
		sysUserData.setAdminUserId(sysAdmin.getId());
		sysUserData.setObjectId(id);
		sysUserData.setObjectType(UserDataType.UNION_TYPE);
		//默认向下兼容
		sysUserData.setCompatibility(0);
		sysUserData.setCrtIp(ClientInfo.getIp());
		sysUserData.setCrtUser(UserUtil.getAdminUser().getUserName());
		sysUserData.setCrtTime(new Date());
		sysUserDataService.insert(sysUserData);
	}

	private void initUserRole(SysUnion obj, SysUserAdmin sysAdmin) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setAdminUserId(sysAdmin.getId());
		sysUserRole.setRoleId(sysRoleService.checkRole_name("工会管理员").getId());
		sysUserRole.setUserName(sysAdmin.getUserName());
		sysUserRole.setCrtUser(sysAdmin.getUserName());
		sysUserRole.setCrtTime(new Date());
		sysUserRole.setCrtIp(ClientInfo.getIp());
		sysRoleService.insert(sysUserRole);
	}

	private SysUserAdmin initSysUserAdmin(SysUnion obj, String id) {
		SysUserAdmin sysAdmin = new SysUserAdmin();
		String scope = "";
		if (StringUtils.isNotBlank(obj.getCity())) {
			scope = obj.getCity();
		}

		if ( StringUtils.isBlank(scope) )  {
			scope = obj.getCounty();
		}

		if ( StringUtils.isBlank(scope) )  {
			scope = obj.getProvince();
		}

		sysAdmin.setUserName( scope+ CodeGenerateUtils.generateCode());

		String name = sysAdmin.getUserName().hashCode()+"";
		if (name.length()>6){
			sysAdmin.setExtend2(StringUtils.leftPad(name.substring(name.length()-6,name.length()),6,"0"));
		}else {
			sysAdmin.setExtend2(StringUtils.leftPad(name,6,"0"));
		}

		sysAdmin.setRealName( obj.getName());
		sysAdmin.setStatus(0);
		sysAdmin.setSex(1);
		sysAdmin.setPhoneNumber(obj.getUnionLeaderPhone());
		sysAdmin.setOrgId(id);
		sysAdmin.setSalt(RandomStringUtils.randomAlphanumeric(20));
		sysAdmin.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(sysAdmin.getExtend2())+sysAdmin.getSalt()));
		sysAdmin.setCrtUser(UserUtil.getAdminUser().getUserName());
		sysAdmin.setCrtTime(new Date());
		sysAdmin.setCrtIp(ClientInfo.getIp());
		sysAdmin.setExtend3(sysAdmin.getPassword());
		sysAdmin.setExtend4(sysRoleService.checkRole_name("工会管理员").getId());

		sysUserAdminService.insert(sysAdmin);
		return sysAdmin;
	}


	@At("/queryPage")
	@Authority("USER_COMPAPP")
	public Object queryPage(@Param("..") SysUnion obj,@Param("orgId") String orgId) {

		return Results.parse(Constants.STATE_SUCCESS,null,sysUnionService.queryPage(obj,UserUtil.getAdminUser().getUserName(),orgId));
	}


	@At("/query")
	@Authority("USER_COMPAPP")
	public Object query(@Param("..") SysUnion obj) {
		obj.setStatus(0);
		return Results.parse(Constants.STATE_SUCCESS,null,sysUnionService.query(obj));
	}



	/**
	 * 查询所有机构
	 * @param obj
	 * @return
	 */
	@At("/orgTree")
	@Authority("USER_COMPAPP")
	public Object orgTree(@Param("..") SysOrganization obj) {

		return  Results.parse(Constants.STATE_SUCCESS, "",service.queryOrganizationTree(obj.getType(),UserUtil.getAdminUnionAndComUserData(),UserUtil.getAdminUser().getUserName()));

	}

	/**
	 * 查询权限机构
	 * @param obj
	 * @return
	 */
	@At("/orgAuthTree")
	@Authority("USER_COMPAPP")
	public Object orgAuthTree(@Param("..") SysOrganization obj) {

		//SysOrganization fixedTopRes = sysUserDataService.getFixedTopRes();

		List<SysUnion> organizationList = new ArrayList<>();
		SysUnion sysUnion = new SysUnion();
		sysUnion.setId("10000");
		sysUnion.setSuperId("-1");
		sysUnion.setName("山东省总工会");
		//sysUnion.setType(UserDataType.UNION_TYPE);
		SysUnion sysUnion1 = new SysUnion();
		sysUnion1.setId("10001");
		sysUnion1.setSuperId("10000");
		sysUnion1.setName("济南市总工会");
		//sysUnion1.setType(UserDataType.UNION_TYPE);
		sysUnion.getUnionList().add(sysUnion1);
		SysUnion sysUnion2 = new SysUnion();
		sysUnion2.setId("10002");
		sysUnion2.setSuperId("10000");
		sysUnion2.setName("枣庄市总工会");
		///sysUnion2.setType(UserDataType.UNION_TYPE);
		sysUnion.getUnionList().add(sysUnion2);
		organizationList.add(sysUnion);
		SysUnion resource = new SysUnion();
		resource.setId("-1");
		resource.setName("中华全国总工会");
		//List<SysOrganization> organizationList = sysUserDataService.querySubOrgList(UserUtil.getAdminUser().getOrgId());
		resource.setUnionList(organizationList);
		List<SysUnion> result = new ArrayList<>();
		result.add(resource);

		return  Results.parse(Constants.STATE_SUCCESS, "",result);

	}



}