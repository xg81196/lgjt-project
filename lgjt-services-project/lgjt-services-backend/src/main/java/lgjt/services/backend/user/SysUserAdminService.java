package lgjt.services.backend.user;

import java.util.*;
import java.util.stream.Collectors;

import com.ttsx.platform.tool.util.EncryptUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.backend.user.KsGroupAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.services.backend.org.KsGroupAdminService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.role.SysMenu;
import lgjt.domain.backend.role.SysRole;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.vo.SysUserAdminVo;
import lgjt.domain.backend.user.vo.SysUserAdminVo2;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.org.SysUnionService;
import lgjt.services.backend.role.SysRoleService;


/**
 * 管理端用户管理
 * @author wuguangwei
 */
@Log4j
@IocBean
public class SysUserAdminService extends BaseService {

	public static final String GET_USER_MENU = "sys.admin.getUserMenu";

	public static final String GET_TOP_MENU = "sys.admin.getTopMenu";

	public static final String GET_USER_ADMIN = "sys.admin.queryPage";

	public static final String GET_USER_ADMIN_ORG = "sys.admin.queryAdminPageByOrgId";

	@Inject
	SysUnionService sysUnionService;

	@Inject
	SysUserDataService sysUserDataService;

	@Inject
	SysOrganizationService sysOrganizationService;

	@Inject
	SysRoleService sysRoleService;

	@Inject
	KsGroupAdminService ksGroupAdminService;


	public PageResult<SysUserAdminVo> queryPage(SysUserAdmin obj,String userId) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andLike("ua.user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("ua.real_name", obj.getRealName());
		}
		if(StringTool.isNotNull(obj.getHeadPortrait())) {
			cri.where().andEquals("ua.head_portrait", obj.getHeadPortrait());
		}

		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("ua.status", obj.getStatus());
		}

		if(StringTool.isNotNull(obj.getOrgId())) {
			cri.where().andEquals("ua.org_id", obj.getOrgId());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("ua.id_no", obj.getIdNo());
		}

		cri.where().andEquals("d.admin_user_id", userId);


		return super.queryPage(GET_USER_ADMIN,SysUserAdminVo.class, obj, cri);
	}


	public PageResult<SysUserAdminVo> queryUserRolePage(SysUserAdmin obj,String username) {

		SimpleCriteria cri = Cnd.cri();



		if (StringUtils.isNotBlank(username)&& !SysUserAdmin.ADMIN.equals(username)
				&& !UserUtil.getRoleNames().contains(SysUserAdmin.PLATFORM_ADMIN)) {


			cri.where().andEquals("ua.crt_user",username);
			cri.where().orEquals("ua.user_name",username);
			if (StringUtils.isNotBlank(obj.getUserName()))
			  cri.where().andLike("ua.user_name",obj.getUserName());


		} else {
			if (StringUtils.isNotBlank( obj.getUserName() )){
				cri.where().andLike("ua.user_name",obj.getUserName());
			}
		}


		if (StringUtils.isNotBlank( obj.getRealName() )){
			cri.where().andLike("ua.real_name",obj.getRealName());
		}

		if (StringUtils.isNotBlank( obj.getPhoneNumber() )){
			cri.where().andEquals("ua.phone_number",obj.getPhoneNumber());
		}

		cri.desc("ua.crt_time");

		return  super.queryPage(GET_USER_ADMIN,SysUserAdminVo.class,obj,cri);



	}


	public PageResult<SysUserAdminVo> queryAdminPageByOrgId(String orgId,Integer type) {
		SimpleCriteria cri = Cnd.cri();

		/**
		 * 工会管理员
		 */
		if (type != null && type != 1) {
			List<SysUnion> sysUnionList = sysUnionService.querySubUnionList(orgId);
			String orgIds="";
			StringJoiner sj = new StringJoiner(",");
			if (CollectionUtils.isNotEmpty(sysUnionList)) {
				List<String> unionIds = sysUnionList.stream().map(SysUnion::getId).collect(Collectors.toList());
				//orgIds= StringUtils.join(unionIds,",");
				for (String id : unionIds) {
					sj.add(id);
				}
			}

			cri.where().andIn("d.object_id ",sj.toString().split(","));
			cri.where().andNotEquals("ua.user_name",SysUserAdmin.ADMIN);
			/*Map<String,Object> params = new HashMap(1);
			params.put("orgId",sj.toString());*/

			return  super.queryPage(GET_USER_ADMIN_ORG,SysUserAdminVo.class,new SysUserAdmin(),cri);


		} else {
			Map<String,Object> params = new HashMap();
			params.put("orgId",orgId);
			cri.where().andEquals("d.object_id ",orgId);
			cri.where().andNotEquals("ua.user_name",SysUserAdmin.ADMIN);

			return  super.queryPage(GET_USER_ADMIN_ORG,SysUserAdminVo.class,new SysUserAdmin(),cri,params);
		}



	}

	public List<SysUserAdmin> query(SysUserAdmin obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("real_name", obj.getRealName());
		}
		if(StringTool.isNotNull(obj.getHeadPortrait())) {
			cri.where().andEquals("head_portrait", obj.getHeadPortrait());
		}
		if(StringTool.isNotNull(obj.getPassword())) {
			cri.where().andEquals("password", obj.getPassword());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getSex())) {
			cri.where().andEquals("sex", obj.getSex());
		}
		if(StringTool.isNotNull(obj.getPhoneNumber())) {
			cri.where().andEquals("phone_number", obj.getPhoneNumber());
		}
		if(StringTool.isNotNull(obj.getEmail())) {
			cri.where().andEquals("email", obj.getEmail());
		}
		if(StringTool.isNotNull(obj.getOrgId())) {
			cri.where().andEquals("org_id", obj.getOrgId());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("id_no", obj.getIdNo());
		}
		if(StringTool.isNotNull(obj.getAntiTamper())) {
			cri.where().andEquals("anti_tamper", obj.getAntiTamper());
		}
		if(StringTool.isNotNull(obj.getRemark())) {
			cri.where().andEquals("remark", obj.getRemark());
		}

		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		return super.query(SysUserAdmin.class, cri);
	}

   	public SysUserAdmin get(String id) {
		return super.fetch(SysUserAdmin.class, id);
	}


	public SysUserAdminVo2 get2(String id) {
		return super.fetch(SysUserAdminVo2.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysUserAdmin.class, cri);
		}
		return 0;
	}

	public SysUserAdmin checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUserAdmin.class,cri);
	}
	public SysUserAdmin checkUser_name(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_name",value);
		return super.fetch(SysUserAdmin.class,cri);
	}

	public SysUserAdmin checkUser_phone(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("phone_number",value);
		return super.fetch(SysUserAdmin.class,cri);
	}


	/**
	 * 获取用户资源
	 * @param userName
	 * @return
	 */
	public List<SysMenu> getUserMenu(String userName) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("username", userName);
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("sort").asc("id");
		return super.query(GET_USER_MENU, SysMenu.class, cri, param);
	}


	public List<SysMenu> getTopMenu() {
		Criteria cri = Cnd.cri();
		cri.where().andEquals("parent_id", -1);
		cri.getOrderBy().asc("sort").asc("id");
		return super.query(GET_TOP_MENU, SysMenu.class, cri);
	}

	/*
	 * 修改密码
	 */
	public void updatePassword(SysUserAdmin user) {

		//盐值加密 20位
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		//防篡改 密码+用户名
		user.setAntiTamper(DigestUtils.md5Hex(user.getNewpwd()+user.getUserName()));
		user.setPassword(DigestUtils.md5Hex(user.getNewpwd()+user.getSalt()));
		super.updateIgnoreNull(user);

		/*Chain chain = Chain.makeSpecial("password", DigestUtils.md5Hex(user.getNewpwd()+user.getSalt()))
				.addSpecial("salt", user.getSalt()).addSpecial("anti_tamper", user.getAntiTamper());
		update(SysUserAdmin.class, chain, Cnd.where("user_name", "=", user.getUserName()));*/
	}

	public Integer  getGroupAdminForSQLServer() {

		List<KsGroupAdmin> ksGroupAdmins = ksGroupAdminService.getKsGroupAdminList();

		String roleId = sysRoleService.checkRole_name("班组长").getId();
		int i = 0;
		for(KsGroupAdmin ksGroupAdmin: ksGroupAdmins) {
			SysUserAdmin sysUserAdmin = new SysUserAdmin();
			sysUserAdmin.setValueForKsGroupAdmin(ksGroupAdmin);
			sysUserAdmin.setExtend4(roleId);
			super.insert(sysUserAdmin);
            //初始化角色
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setAdminUserId(sysUserAdmin.getId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserName(sysUserAdmin.getUserName());
            sysUserRole.setCrtUser(sysUserAdmin.getUserName());
            sysUserRole.setCrtTime(new Date());
            sysUserRole.setCrtIp(ClientInfo.getIp());
            sysRoleService.insert(sysUserRole);

            //初始化用户资源
            SysUserData userData = new SysUserData();
            userData.setId(UUIDUtil.getUUID());
            userData.setAdminUserId(sysUserAdmin.getId());
            userData.setObjectId(sysUserAdmin.getOrgId());
            userData.setObjectType(0);
            sysUserDataService.insert(userData);
			i++;
		}

		return i;
	}


}