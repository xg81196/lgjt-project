package lgjt.services.backend.user;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.backend.user.vo.KsAdmin;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.constants.UserDataType;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.vo.SysUserVo;
import lgjt.domain.backend.user.vo.SysUserVo2;
import lgjt.services.backend.BaseService2;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.org.SysUnionComService;
import lgjt.services.backend.org.SysUnionService;

import java.util.*;
import java.util.stream.Collectors;



@Log4j
@IocBean
public class SysUserService extends BaseService2 {

	public static final String QUERY_USER = "sys.users.queryPageUsers";

	@Inject
	SysUserDataService sysUserDataService;

	@Inject
	SysOrganizationService sysOrganizationService;

	@Inject
	SysUnionService sysUnionService;

	@Inject
	SysUnionComService sysUnionComService;

	@Inject
    KsAdminService ksAdminService;


	public PageResult<SysUser> queryPage(SysUser obj) {
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
		if(StringTool.isNotNull(obj.getBirthDate())) {
			cri.where().andEquals("birth_date", obj.getBirthDate());
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
		if(StringTool.isNotNull(obj.getIdType())) {
			cri.where().andEquals("id_type", obj.getIdType());
		}
		if(StringTool.isNotNull(obj.getComId())) {
			cri.where().andEquals("com_id", obj.getComId());
		}
		if(StringTool.isNotNull(obj.getUnionId())) {
			cri.where().andEquals("union_id", obj.getUnionId());
		}
		if(StringTool.isNotNull(obj.getNumberNo())) {
			cri.where().andEquals("number_no", obj.getNumberNo());
		}
		if(StringTool.isNotNull(obj.getJob())) {
			cri.where().andEquals("job", obj.getJob());
		}
		if(StringTool.isNotNull(obj.getJobNo())) {
			cri.where().andEquals("job_no", obj.getJobNo());
		}
		if(StringTool.isNotNull(obj.getTitle())) {
			cri.where().andEquals("title", obj.getTitle());
		}
		if(StringTool.isNotNull(obj.getWorkYear())) {
			cri.where().andEquals("work_year", obj.getWorkYear());
		}
		if(StringTool.isNotNull(obj.getWechat())) {
			cri.where().andEquals("wechat", obj.getWechat());
		}
		if(StringTool.isNotNull(obj.getAlipay())) {
			cri.where().andEquals("alipay", obj.getAlipay());
		}
		if(StringTool.isNotNull(obj.getUserType())) {
			cri.where().andEquals("user_type", obj.getUserType());
		}
		if(StringTool.isNotNull(obj.getAntiTamper())) {
			cri.where().andEquals("anti_tamper", obj.getAntiTamper());
		}
		if(StringTool.isNotNull(obj.getNation())) {
			cri.where().andEquals("nation", obj.getNation());
		}
		if(StringTool.isNotNull(obj.getWorkStatus())) {
			cri.where().andEquals("work_status", obj.getWorkStatus());
		}
		if(StringTool.isNotNull(obj.getHouseholdRegister())) {
			cri.where().andEquals("household_register", obj.getHouseholdRegister());
		}
		if(StringTool.isNotNull(obj.getEducation())) {
			cri.where().andEquals("education", obj.getEducation());
		}
		if(StringTool.isNotNull(obj.getTechnicalLevel())) {
			cri.where().andEquals("technical_level", obj.getTechnicalLevel());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeType())) {
			cri.where().andEquals("membership_change_type", obj.getMembershipChangeType());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeDate())) {
			cri.where().andEquals("membership_change_date", obj.getMembershipChangeDate());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeReason())) {
			cri.where().andEquals("membership_change_reason", obj.getMembershipChangeReason());
		}
		if(StringTool.isNotNull(obj.getRemark())) {
			cri.where().andEquals("remark", obj.getRemark());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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

		return super.queryPage(SysUser.class, obj, cri);
	}

	public List<SysUser> query(SysUser obj) {
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
		if(StringTool.isNotNull(obj.getBirthDate())) {
			cri.where().andEquals("birth_date", obj.getBirthDate());
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
		if(StringTool.isNotNull(obj.getIdType())) {
			cri.where().andEquals("id_type", obj.getIdType());
		}
		if(StringTool.isNotNull(obj.getComId())) {
			cri.where().andEquals("com_id", obj.getComId());
		}
		if(StringTool.isNotNull(obj.getUnionId())) {
			cri.where().andEquals("union_id", obj.getUnionId());
		}
		if(StringTool.isNotNull(obj.getNumberNo())) {
			cri.where().andEquals("number_no", obj.getNumberNo());
		}
		if(StringTool.isNotNull(obj.getJob())) {
			cri.where().andEquals("job", obj.getJob());
		}
		if(StringTool.isNotNull(obj.getJobNo())) {
			cri.where().andEquals("job_no", obj.getJobNo());
		}
		if(StringTool.isNotNull(obj.getTitle())) {
			cri.where().andEquals("title", obj.getTitle());
		}
		if(StringTool.isNotNull(obj.getWorkYear())) {
			cri.where().andEquals("work_year", obj.getWorkYear());
		}
		if(StringTool.isNotNull(obj.getWechat())) {
			cri.where().andEquals("wechat", obj.getWechat());
		}
		if(StringTool.isNotNull(obj.getAlipay())) {
			cri.where().andEquals("alipay", obj.getAlipay());
		}
		if(StringTool.isNotNull(obj.getUserType())) {
			cri.where().andEquals("user_type", obj.getUserType());
		}
		if(StringTool.isNotNull(obj.getAntiTamper())) {
			cri.where().andEquals("anti_tamper", obj.getAntiTamper());
		}
		if(StringTool.isNotNull(obj.getNation())) {
			cri.where().andEquals("nation", obj.getNation());
		}
		if(StringTool.isNotNull(obj.getWorkStatus())) {
			cri.where().andEquals("work_status", obj.getWorkStatus());
		}
		if(StringTool.isNotNull(obj.getHouseholdRegister())) {
			cri.where().andEquals("household_register", obj.getHouseholdRegister());
		}
		if(StringTool.isNotNull(obj.getEducation())) {
			cri.where().andEquals("education", obj.getEducation());
		}
		if(StringTool.isNotNull(obj.getTechnicalLevel())) {
			cri.where().andEquals("technical_level", obj.getTechnicalLevel());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeType())) {
			cri.where().andEquals("membership_change_type", obj.getMembershipChangeType());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeDate())) {
			cri.where().andEquals("membership_change_date", obj.getMembershipChangeDate());
		}
		if(StringTool.isNotNull(obj.getMembershipChangeReason())) {
			cri.where().andEquals("membership_change_reason", obj.getMembershipChangeReason());
		}
		if(StringTool.isNotNull(obj.getRemark())) {
			cri.where().andEquals("remark", obj.getRemark());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		return super.query(SysUser.class, cri);
	}

   	public SysUser get(String id) {
		return super.fetch(SysUser.class, id);
	}

	public SysUserVo2 get2(String id) {
		return super.fetch(SysUserVo2.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysUser.class, cri);
		}
		return 0;
	}

	public SysUser checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUser.class,cri);
	}
	public SysUser checkUser_name(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_name",value);
		return super.fetch(SysUser.class,cri);
	}

	public SysUser checkPhoneNumber(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("phone_number",value);
        return super.fetch(SysUser.class,cri);
    }

    public SysUser checkPersonId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id_no",value);
		return super.fetch(SysUser.class,cri);
	}


	public PageResult<SysUserVo> queryPageUsersNoTree(SysUser obj,SysUserAdmin sysUserAdmin) {
		SimpleCriteria cri = Cnd.cri();


		String sqlStr = "select u.id,u.user_name,u.real_name,u.`status`,u.sex,u.phone_number,DATE_FORMAT(u.birth_date,'%Y-%m-%d') birth_date,u.email,u.id_no,u.id_type" +
				",u.number_no,u.job,u.job_no,u.title,u.work_year,u.user_type,(select name from sys_dict d where u.nation=d.code and d.parent_id='minzu') nation_name,u.nation,u.work_status,(select name from sys_dict d where u.household_register=d.code and d.parent_id='hukou') household_register_name, u.household_register,(select name from sys_dict d where u.education=d.code and d.parent_id='xueli') education_name,u.education" +
				",(select name from sys_dict d where u.technical_level=d.code and d.parent_id='techlevel') technical_level_name,u.technical_level" +
				",(select name from sys_dict d where u.membership_change_type=d.code and d.parent_id='membershiptype') membership_change_type_name,u.membership_change_type,u.membership_change_date" +
				",(select name from sys_dict d where u.membership_change_reason=d.code and d.parent_id='membershipreason') membership_change_reason_name,u.membership_change_reason,u.remark,\n" +
				"u.org_id,u.com_id,u.union_id, "+
				/*"(select o.`name` from sys_organization o where u.org_id=o.id )  org_name,\n" +*/
				"(select o.`name` from sys_union o where u.union_id=o.id)  union_name,\n" +
				"(select o.`name` from sys_organization o where u.com_id=o.id)  com_name \n" +
				"  from sys_user u ";
		/*		"  from sys_user u where ((u.com_id in (select object_id from sys_user_data where admin_user_id=@userId))\n"+
				"  or ( u.org_id in (select object_id from sys_user_data where admin_user_id=@userId))\n"+
				"or ( u.union_id in (select object_id from sys_user_data where admin_user_id=@userId)))";*/

		String condition = this.dataScopeFilter(sysUserAdmin);

		if (StringUtils.isNotBlank(condition)) {
			sqlStr+="where "+condition+" ";
		}

		String[] orgIds = null;

		/*if(StringTool.isNotNull(obj.getComId())) {
			sqlStr+=" and (u.org_id=@orgId)";
		}*/


		if(StringTool.isNotNull(obj.getUserName())) {
			sqlStr+=" and (u.user_name=@userName)";
			//cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getUserType())) {
			sqlStr+=" and (u.user_type="+ obj.getUserType() +")";
			//cri.where().andEquals("user_name", obj.getUserName());
		}

		Pager page = getPager(obj);
		Sql sql = Sqls.create(sqlStr);
		sql.setCallback(Sqls.callback.entities());
		sql.setEntity(dao.getEntity(SysUserVo.class));
		sql.params().set("userId", sysUserAdmin.getId());
		if(StringTool.isNotNull(obj.getUserName())) {
			sql.params().set("userName",  obj.getUserName());
		}
		sql.setPager(page);
		dao.execute(sql);
		List<SysUserVo> list = sql.getList(SysUserVo.class);

		sql = Sqls.create("select count(1) from (" + sqlStr + ") as total");
		//sql.setPager(page);
		sql.params().set("userId", sysUserAdmin.getId());
		if(StringTool.isNotNull(obj.getUserName())) {
			sql.params().set("userName",  obj.getUserName());
		}
		sql.setCallback(Sqls.callback.integer());
		dao.execute(sql);

		for (SysUserVo sysUserVo : list) {
		/*	String unionName = sysOrganizationService.getOrgString(sysUserVo.getUnionId());
			if (StringUtils.isNotBlank(unionName)) {
				if (unionName.lastIndexOf("/") > 0) {
					sysUserVo.setUnionName(unionName.substring(1,unionName.lastIndexOf("/")));
				}else {
					sysUserVo.setUnionName(unionName.substring(1));
				}

				sysUserVo.setUnionName(unionName.substring(1));

			}*/
			/*String orgId  = sysUserVo.getOrgId();
			if (StringUtils.isBlank(sysUserVo.getOrgId())){
				orgId = sysUserVo.getComId();
			}
			if (StringUtils.isBlank(orgId)) {
				orgId = sysUserVo.getUnionId();
			}*/

		/*	String comName = sysOrganizationService.getOrgString(sysUserVo.getComId());
			if (StringUtils.isNotBlank(comName)) {
				if (comName.lastIndexOf("/") > 0) {
					sysUserVo.setComName(comName.substring(1,comName.lastIndexOf("/")));
				}else {
					sysUserVo.setComName(comName.substring(1));
				}
				sysUserVo.setComName(comName.substring(1));
			}*/
		}
		return new PageResult<>(list, sql.getInt());

		//PageResult<SysUserVo> pageResult =  super.queryPage(QUERY_USER,SysUserVo.class, obj, cri);

		//return pageResult;
	}




	public PageResult<SysUserVo> queryPageUsers(SysUser obj,String orgId,int type ) {
		SimpleCriteria cri = Cnd.cri();

		/**
		 * 2018-07-19 赵天意
		 * BUG11245 根据当前登录用户进行数据筛选
		 */

		//是否可以查询
		Boolean hasAuth = true;
/*		SysUserAdmin loginUser = UserUtil.getAdminUser();
		//判断是否为平台管理员

		//判断当前登录用户是否和点击的节点一样
		cri.where().andEquals("object_id", orgId);
		cri.where().andEquals("admin_user_id", loginUser.getId());

		if(super.query(SysUserData.class, cri).size() > 0) {
			*//*
			如果查出数据，证明点击节点是当前登录用户管辖范围
			select * from sys_user_data where object_id = '?' and admin_user_id = '?'
			 *//*

			hasAuth = true;
		} else {
			*//*
			如果没有查出任何数据，查询是否是当前登录用户管辖的子集
			 *//*
			cri = Cnd.cri();
			cri.where().andEquals("object_id", loginUser.getOrgId());
			List<SysUserData> suds = super.query(SysUserData.class, cri);

			//查询出选中的节点是企业还是公会
			if(suds.size() > 0) {
				SysUserData sud = suds.get(0);
				if(sud.getObjectType() == 100000) {
					//公会
					hasAuth = sysUnionComService.recursionUnion(loginUser.getOrgId(), orgId);
				} else {
					//企业
					hasAuth = sysUnionComService.recursionOrg(loginUser.getOrgId(), orgId);
				}
			} else {
				//这种情况数据admin 和 全国总共为的特殊情况
				//还需要针对平台管理进行判断
				String role = UserUtil.getRoleNames();
				if(role.equals("平台管理员")) {
					hasAuth = true;
				}
			}

		}*/


		if(hasAuth) {
			String sqlStr = "select u.id,u.user_name,u.real_name,u.`status`,u.sex,u.phone_number,DATE_FORMAT(u.birth_date,'%Y-%m-%d') birth_date,u.email,u.id_no,u.id_type" +
					",u.number_no,u.job,u.job_no,u.title,u.work_year,u.user_type,(select name from sys_dict d where u.nation=d.code and d.parent_id='minzu') nation_name,u.nation,u.work_status,(select name from sys_dict d where u.household_register=d.code and d.parent_id='hukou') household_register_name, u.household_register,(select name from sys_dict d where u.education=d.code and d.parent_id='xueli') education_name,u.education" +
					",(select name from sys_dict d where u.technical_level=d.code and d.parent_id='techlevel') technical_level_name,u.technical_level" +
					",(select name from sys_dict d where u.membership_change_type=d.code and d.parent_id='membershiptype') membership_change_type_name,u.membership_change_type,u.membership_change_date" +
					",(select name from sys_dict d where u.membership_change_reason=d.code and d.parent_id='membershipreason') membership_change_reason_name,u.membership_change_reason,u.remark,extend3,extend4,\n" +
					"u.org_id,u.com_id,u.union_id, "+
					/*"(select o.`name` from sys_organization o where u.org_id=o.id )  org_name,\n" +*/
					"(select o.`name` from sys_union o where u.union_id=o.id)  union_name,\n" +
					"(select o.`name` from sys_organization o where u.com_id=o.id)  com_name \n";
			String where= "";

			/**
			 * 工会查子节点 TODO?性能问题，先这么搞
			 */

			if (UserDataType.UNION_TYPE == type) {

				Set<String> comIdList = new HashSet<>();

				Set<String> unionIdList = new HashSet<>();

				List<SysUnion> sysUnionList = sysUnionService.querySubUnionList(orgId);
				if (CollectionUtils.isNotEmpty(sysUnionList))  {
					List<String> unionIds = sysUnionList.stream().map(SysUnion :: getId).collect(Collectors.toList());
					for (String unid : unionIds) {

						/**
						 * 工会下企业
						 */
						List<SysUnionCom> unionComList = sysUnionComService.getUnionComByUnionId(unid);
						if (CollectionUtils.isNotEmpty(unionComList))  {
							List<String> comIds = unionComList.stream().map(SysUnionCom :: getComId).collect(Collectors.toList());
							comIdList.addAll(comIds);
						}
					}

					unionIdList.addAll(unionIds);
				}

			/*StringBuilder sb = new  StringBuilder();
			for (int i = 0; i < strs.length; i++) {
				sb.append("'"+strs[i]+"'");
				if(i!=strs.length-1){
					sb.append(",");
				}
			}*/

				StringJoiner sj = new StringJoiner(",");
				for ( String s : comIdList) {
					sj.add("'"+s+"'");
				}

				StringJoiner sj1 = new StringJoiner(",");
				for ( String s : unionIdList) {
					sj1.add("'"+s+"'");
				}
                sqlStr+="  from sys_user u ";

				if(CollectionUtils.isNotEmpty(unionIdList)) {
                    where += "((u.union_id in ("+sj1.toString()+"))\n";
                }

				if (CollectionUtils.isNotEmpty(comIdList))  {
				    if(CollectionUtils.isNotEmpty(unionIdList)) {
                        where +="  or u.com_id in ("+sj.toString()+"))\n";
                    } else {
                        where +=" u.com_id in ("+sj.toString()+"))\n";
                    }

				}else {
					where +=")";
				}
			} else {

				/**
				 * 2018-07-19 赵天意
				 * 企业也要子集
				 *
				 */
				Set<String> comIdList = new HashSet<>();
				List<SysOrganization> sysOrganizationsList = sysOrganizationService.querOrgAndChild(orgId);

				StringJoiner sj = new StringJoiner(",");
				for(SysOrganization so: sysOrganizationsList) {
					sj.add("'"+so.getId()+"'");
				}
                sqlStr+="  from sys_user u ";

				if(sj.length() > 0) {
                    where += "((u.com_id in ("+sj.toString()+")))\n";
                }
			}

			if(StringTool.isNotNull(obj.getUserName())) {
                where+=" and (u.user_name like '%"+obj.getUserName()+"%')";
				//cri.where().andEquals("user_name", obj.getUserName());
			}

			if(StringTool.isNotNull(obj.getRealName())) {
                where+=" and (u.real_name like '%"+obj.getRealName()+"%')";
				//cri.where().andEquals("user_name", obj.getUserName());
			}

			if(StringTool.isNotNull(obj.getPhoneNumber())) {
                where+=" and (u.phone_number=@phoneNumber)";
			}

			if(StringTool.isNotNull(obj.getStatus())) {
                where+=" and (u.status=@status)";
			}

			Pager page = getPager(obj);

			//判断
            if(where.length() > 0) {
                if(where.substring(1, 3).equals("and")) {
                    where = where.substring(4);
                }
                sqlStr += "where " + where;
            }
            System.out.println(sqlStr);

			Sql sql = Sqls.create(sqlStr+" order by u.crt_time desc");
			sql.setCallback(Sqls.callback.entities());
			sql.setEntity(dao.getEntity(SysUserVo.class));
			sql.params().set("orgId", orgId);
		/*if(StringTool.isNotNull(obj.getUserName())) {
			sql.params().set("userName",  obj.getUserName());
		}

		if(StringTool.isNotNull(obj.getRealName())) {
			sql.params().set("userRealName",  obj.getRealName());
		}*/

			if(StringTool.isNotNull(obj.getPhoneNumber())) {
				sql.params().set("phoneNumber",  obj.getPhoneNumber());
			}

			if(StringTool.isNotNull(obj.getStatus())) {
				sql.params().set("status",  obj.getStatus());
			}

			sql.setPager(page);
			dao.execute(sql);
			List<SysUserVo> list = sql.getList(SysUserVo.class);

			sql = Sqls.create("select count(1) from (" + sqlStr + ") as total");
			//sql.setPager(page);
			sql.params().set("orgId", orgId);
		/*if(StringTool.isNotNull(obj.getUserName())) {
			sql.params().set("userName",  obj.getUserName());
		}

		if(StringTool.isNotNull(obj.getRealName())) {
			sql.params().set("userRealName",  obj.getRealName());
		}*/

			if(StringTool.isNotNull(obj.getPhoneNumber())) {
				sql.params().set("phoneNumber",  obj.getPhoneNumber());
			}

			if(StringTool.isNotNull(obj.getStatus())) {
				sql.params().set("status",  obj.getStatus());
			}
			sql.setCallback(Sqls.callback.integer());
			dao.execute(sql);

			for (SysUserVo sysUserVo : list) {
				sysUserVo.setSuperName(sysUserVo.getExtend3());
				sysUserVo.setType(sysUserVo.getExtend4());
				String unionName = sysOrganizationService.getOrgString(sysUserVo.getUnionId());
				if (StringUtils.isNotBlank(unionName)) {
				/*if (unionName.lastIndexOf("/") > 0) {
					sysUserVo.setUnionName(unionName.substring(1,unionName.lastIndexOf("/")));
				}else {
					sysUserVo.setUnionName(unionName.substring(1));
				}*/

					sysUserVo.setUnionName(unionName.substring(1));

				}

		/*	String comName = sysOrganizationService.getOrgString(sysUserVo.getComId());
			if (StringUtils.isNotBlank(comName)) {
				if (comName.lastIndexOf("/") > 0) {
					sysUserVo.setComName(comName.substring(1,comName.lastIndexOf("/")));
				}else {
					sysUserVo.setComName(comName.substring(1));
				}
				sysUserVo.setComName(comName.substring(1));
			}*/
			}
			return new PageResult<>(list, sql.getInt());
		} else {
			return new PageResult<>(new ArrayList<>(), 0);
		}

		//PageResult<SysUserVo> pageResult =  super.queryPage(QUERY_USER,SysUserVo.class, obj, cri);

		//return pageResult;
	}

    /**
     * 删除整个用户表，从线上SQLServer里拉数据
     * @return
     */
	public Integer getSysUserBySQLServer() {
        SimpleCriteria cri = Cnd.cri();
        super.delete(SysUser.class, cri);


        List<KsAdmin> ksAdmins = ksAdminService.getKsAdminFromSQL();
        List<SysUser> sysUsers = new ArrayList<>();

        int count = 0;

        for(KsAdmin ksAdmin: ksAdmins) {
            SysUser sysUser = new SysUser();
            sysUser.getValueForKsAdmin(ksAdmin);
            sysUsers.add(sysUser);
            //插入
            super.insert(sysUser);
            count++;

        }
        return count;
	}

	public Integer createTestUser() {
        int count = 300;
        for (int i = 1; i <= count; i++) {
            SysUser user = new SysUser();
            user.setId("111111"+i);
            user.setUserName("test"+i);
            user.setRealName("压力测试用户"+i);
            user.setPassword("da320414622d88ddaf3b34a1e0f22837");
            user.setStatus(0);
            user.setSex(1);
            user.setOrgId("680");
            user.setComId("680");
            user.setWorkYear(new Date());
            user.setUserType(1);
            user.setSalt("KEX1QPiYu1DeTuoUSY3KecVOXEzwk8h7");
            user.setCrtTime(new Date());
            user.setCrtUser("admin");
            user.setPhoneNumber("110");
            user.setGroupId("680");
            user.setCrtIp("127.0.0.1");
            super.insert(user);
        }
        return count;
	}

}