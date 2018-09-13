package lgjt.services.backend.user;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.tool.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;

import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.role.SysRole;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.domain.backend.user.vo.SysUserDataVo;
import lgjt.domain.backend.utils.UserUtil;


/**
 * 数据权限
 * @author wuguangwei
 */
@Log4j
@IocBean
public class SysUserDataService extends BaseService {

	private static final String QUERY_DATA_AUTH = "sys.user.queryDataAuth";

	@Inject
	SysUserService sysUserService;



	public PageResult<SysUserData> queryPage(SysUserData obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getAdminUserId())) {
			cri.where().andEquals("admin_user_id", obj.getAdminUserId());
		}
		if(StringTool.isNotNull(obj.getObjectId())) {
			cri.where().andEquals("object_id", obj.getObjectId());
		}
		if(StringTool.isNotNull(obj.getObjectType())) {
			cri.where().andEquals("object_type", obj.getObjectType());
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

		return super.queryPage(SysUserData.class, obj, cri);
	}

	public List<SysUserData> query(SysUserData obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getAdminUserId())) {
			cri.where().andEquals("admin_user_id", obj.getAdminUserId());
		}
		if(StringTool.isNotNull(obj.getObjectId())) {
			cri.where().andEquals("object_id", obj.getObjectId());
		}
		if(StringTool.isNotNull(obj.getObjectType())) {
			cri.where().andEquals("object_type", obj.getObjectType());
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
		return super.query(SysUserData.class, cri);
	}

   	public SysUserData get(String id) {
		return super.fetch(SysUserData.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysUserData.class, cri);
		}
		return 0;
	}

	public SysUserData checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUserData.class,cri);
	}


	/**
	 * 根据父节点查询
	 * @author wuguangwei
	 * @param parentId
	 * @return
	 */
	public List<SysOrganization> queryCompanyIdList(String parentId) {
		SimpleCriteria cri=Cnd.cri();
		cri.where().andEquals("super_id", parentId);

		return  super.query(SysOrganization.class, cri);

	}


	/**
	 * 查询包括本机构及下属机构
	 * @author wuguangwei
	 * @param companyId
	 * @return
	 */
	public List<SysOrganization> querySubOrgList(String companyId) {

		//公司及下面公司ID列表
		List<SysOrganization> companyIdList = new ArrayList<>();
		SysOrganization company = super.fetch(SysOrganization.class, companyId);

		 //获取下面公司ID
		if (!Objects.isNull( company )) {
			List<SysOrganization> subIdList = queryCompanyIdList(companyId);
			getCompanyTreeList(subIdList, companyIdList);
			companyIdList.add(company);
		}

		return companyIdList;

	}



	/**
	 * 递归
	 * @author wuguangwei
	 */
	private void getCompanyTreeList(List<SysOrganization> subIdList, List<SysOrganization> companyIdList){

		for(SysOrganization company : subIdList){
			List<SysOrganization> list = queryCompanyIdList(company.getId());
			if(list != null && list.size() > 0){
				getCompanyTreeList(list, companyIdList);
			}

			companyIdList.add(company);
		}
	}


	public SysOrganization getFixedTopRes() {
		SysOrganization resource = new SysOrganization();
		resource.setId("-1");
		resource.setName("机构树");
		return resource;
	}


	public PageResult<SysUser> queryDataScope(SysUserAdmin sysUserAdmin) {

		//查询管理员权限
		SimpleCriteria cri= Cnd.cri();
		cri.where().andEquals("admin_user_id", sysUserAdmin.getId());
		return sysUserService.queryPage(QUERY_DATA_AUTH,SysUser.class,new SysUser(), cri);


	}




	/**
	 * 保存用户与组织机构
	 * @param userData
	 * @param user
	 */
	public void save(SysUserData userData, SysUserAdmin user,int type) {

		//先删除用户与机构关系
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("admin_user_id",user.getId());
		super.delete(SysUserData.class,cri);

		if(StringUtils.isBlank(userData.getOrgIdList())){
			return ;
		}

		//保存用户与机构关系
		Set<String> unionIdList = new HashSet<>();

		Set<String> comIdList = new HashSet<>();

		List<SysUserData> list = new ArrayList<>();
		//System.out.println(userData.getOrgIdList());
		List<SysUserDataVo> userDataVoList = JSON.parseArray(userData.getOrgIdList(), SysUserDataVo.class);
		for(SysUserDataVo userDataVo : userDataVoList){
			SysUserData sysUserData = new SysUserData();
			sysUserData.setId(UUIDUtil.getUUID());
			sysUserData.setAdminUserId(user.getId());
			sysUserData.setObjectId(userDataVo.getId());
			sysUserData.setObjectType(userData.getObjectType());
			sysUserData.setCompatibility(userDataVo.getCompatibility());
			sysUserData.setCrtTime(new Date());
			sysUserData.setCrtUser(user.getUserName());
			list.add(sysUserData);
			if (UserDataType.UNION_TYPE == userData.getObjectType()) {
				unionIdList.add(userDataVo.getId());
			}else{
				comIdList.add(userDataVo.getId());
			}

			String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
			//权限存入缓存
			UserUtil.setUserData(StringUtils.join(unionIdList, ",")
					,StringUtils.join(comIdList, ","),token);

		}
		this.getDao().fastInsert(list);

		if ( 1 != type ) {
			SysUserRole sysRole = new SysUserRole();
			sysRole.setRoleId(user.getRoleId());
			sysRole.setAdminUserId(user.getId());
			sysRole.setCrtTime(new Date());
			sysRole.setCrtUser(UserUtil.getAdminUser().getUserName());
			sysRole.setCrtIp(ClientInfo.getIp());
			this.insert(sysRole);
		}

	}

	/**
	 * 单个更新
	 * @param userData
	 * @param user
	 * @param type
	 * @return
	 */
	public int singleUpdate(SysUserData userData, SysUserAdmin user,int type) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("admin_user_id",user.getId());
		List<SysUserData> suds = super.query(SysUserData.class, cri);
		userData.setId(suds.get(0).getId());
		userData.setObjectType(type);
		return super.updateIgnoreNull(userData);
	}

	/**
	 * 根据用户查询数据权限
	 * @param userId
	 * @return
	 */
	public List<SysUserData> getOrgsByUserId(String userId) {

		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("admin_user_id",userId);
		return this.query(SysUserData.class,cri);
	}


	/**
	 * 根据用户和类型查询数据权限
	 * @param userId
	 * @return
	 */
	public List<SysUserData> getOrgsByUserIdAndType(String userId,int type) {

		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("admin_user_id",userId);
		cri.where().andEquals("object_type",type);
		return this.query(SysUserData.class,cri);
	}

}