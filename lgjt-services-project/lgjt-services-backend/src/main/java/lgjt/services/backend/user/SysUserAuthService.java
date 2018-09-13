package lgjt.services.backend.user;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.utils.HttpClientUtil;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserAuth;
import lgjt.domain.backend.user.vo.SysUserVo;
import lgjt.services.backend.BaseService2;
import lgjt.services.backend.org.SysOrganizationService;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * @author wuguangwei
 * @date 2018/4/28
 * @Description:
 */
@Log4j
@IocBean
public class SysUserAuthService extends BaseService2 {

    @Inject
    SysOrganizationService sysOrganizationService;

    @Inject
    SysUserDataService sysUserDataService;



    public PageResult<SysUserAuth> queryPage (SysUserAuth sysUserAuth) {

            SimpleCriteria cri = Cnd.cri();

            if(StringTool.isNotNull(sysUserAuth.getStatus())) {
                cri.where().andEquals("status", sysUserAuth.getStatus());
            }
            if(StringTool.isNotNull(sysUserAuth.getUserName())) {
                cri.where().andEquals("user_id", sysUserAuth.getUserName());
            }


            return super.queryPage(SysUserAuth.class, sysUserAuth, cri);
        }



    public PageResult<SysUserVo> queryPageAuthUser(SysUserAuth obj, SysUserAdmin sysUserAdmin, String orgId) {

        String sqlStr = "select u.id,u.user_name,u.real_name,u.`status`,u.sex,u.phone_number,u.birth_date,u.email,u.id_no,u.id_type" +
                ",u.number_no,u.job,u.job_no,u.title,u.work_year,u.user_type,(select name from sys_dict d where u.nation=d.code and d.parent_id='minzu') nation_name,u.nation,u.work_status,(select name from sys_dict d where u.household_register=d.code and d.parent_id='hukou') household_register_name, u.household_register,(select name from sys_dict d where u.education=d.code and d.parent_id='xueli') education_name,u.education" +
                ",(select name from sys_dict d where u.technical_level=d.code and d.parent_id='techlevel') technical_level_name,u.technical_level" +
                ",(select name from sys_dict d where u.membership_change_type=d.code and d.parent_id='membershiptype') membership_change_type_name,u.membership_change_type,u.membership_change_date" +
                ",(select name from sys_dict d where u.membership_change_reason=d.code and d.parent_id='membershipreason') membership_change_reason_name,u.membership_change_reason,u.remark,\n" +
                "u.org_id,u.com_id,u.union_id, "+
				/*"(select o.`name` from sys_organization o where u.org_id=o.id)  org_name,\n" +*/
				"(select o.`name` from sys_union o where u.union_id=o.id)  union_name,\n" +
                "(select o.`name` from sys_organization o where u.com_id=o.id)  com_name \n" +
                "  from sys_user_auth u where  u.status !=1 ";
             /*   "  from sys_user_auth u where  u.status in (0,2) and ((u.org_id in (select object_id from sys_user_data where admin_user_id=@userId))\n" +
				"  or ( u.com_id in (select object_id from sys_user_data where admin_user_id=@userId))\n" +
				"or ( u.union_id in (select object_id from sys_user_data where admin_user_id=@userId)))";
*/

        String condition = this.dataScopeFilter(sysUserAdmin);

        if (StringUtils.isNotBlank(condition)) {
            sqlStr+=" "+condition+" ";
        }
        String[] orgIdss = null;
        if(StringTool.isNotNull(orgId)) {
            SysOrganization sysOrganization = sysOrganizationService.get(orgId);
            if (!Objects.isNull(sysOrganization)){

                if ( sysOrganization.getType() == 0 ) {
                    sqlStr+=" and (u.com_id in (@orgId))";
                    List<SysOrganization> sysOrganizations = sysUserDataService.querySubOrgList(orgId);
                    if (CollectionUtils.isNotEmpty( sysOrganizations )) {
                        String orgIds = sysOrganizations.stream().map(SysOrganization :: getId).collect(joining(","));
                        orgIdss = orgIds.split(",");
                    }
                }
            }

        }


        if(StringTool.isNotNull(obj.getUserName())) {
            sqlStr+=" and (u.user_name=@userName)";
            //cri.where().andEquals("user_name", obj.getUserName());
        }

        if(StringTool.isNotNull(obj.getRealName())) {
            sqlStr+=" and (u.real_name=@userRealName)";
            //cri.where().andEquals("user_name", obj.getUserName());
        }

        if(StringTool.isNotNull(obj.getPhoneNumber())) {
            sqlStr+=" and (u.phone_number=@phoneNumber)";
        }

        if(StringTool.isNotNull(obj.getStatus())) {
            sqlStr+=" and (u.status=@status)";
        }


        Pager page = getPager(obj);
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(dao.getEntity(SysUserVo.class));
        sql.params().set("userId", sysUserAdmin.getId());
        if(StringTool.isNotNull(obj.getUserName())) {
            sql.params().set("userName",  obj.getUserName());
        }
        if(StringTool.isNotNull(obj.getRealName())) {
            sql.params().set("userRealName",  obj.getRealName());
        }

        if(StringTool.isNotNull(obj.getPhoneNumber())) {
            sql.params().set("phoneNumber",  obj.getPhoneNumber());
        }

        if(StringTool.isNotNull(obj.getStatus())) {
            sql.params().set("status",  obj.getStatus());
        }

        if(StringTool.isNotNull(orgId)) {
            SysOrganization sysOrganization = sysOrganizationService.get(orgId);
            if (!Objects.isNull(sysOrganization)) {
                if (sysOrganization.getType() == 0) {
                    sql.params().set("orgId", orgIdss);
                }

            }
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
        if(StringTool.isNotNull(obj.getRealName())) {
            sql.params().set("userRealName",  obj.getRealName());
        }

        if(StringTool.isNotNull(obj.getPhoneNumber())) {
            sql.params().set("phoneNumber",  obj.getPhoneNumber());
        }

        if(StringTool.isNotNull(obj.getStatus())) {
            sql.params().set("status",  obj.getStatus());
        }
        if(StringTool.isNotNull(orgId)) {
            SysOrganization sysOrganization = sysOrganizationService.get(orgId);
            if (!Objects.isNull(sysOrganization)) {
                if (sysOrganization.getType() == 0) {
                    sql.params().set("orgId", orgIdss);
                }

            }
        }
        sql.setCallback(Sqls.callback.integer());
        dao.execute(sql);


       /* for (SysUserVo sysUserVo : list) {
            String unionName = sysOrganizationService.getOrgString(sysUserVo.getUnionId());
            if (StringUtils.isNotBlank(unionName)) {
				if (unionName.lastIndexOf("/") > 0) {
					sysUserVo.setUnionName(unionName.substring(1,unionName.lastIndexOf("/")));
				}else {
					sysUserVo.setUnionName(unionName.substring(1));
				}

                sysUserVo.setUnionName(unionName.substring(1));

            }*/

		/*	String comName = sysOrganizationService.getOrgString(sysUserVo.getComId());
			if (StringUtils.isNotBlank(comName)) {
				if (comName.lastIndexOf("/") > 0) {
					sysUserVo.setComName(comName.substring(1,comName.lastIndexOf("/")));
				}else {
					sysUserVo.setComName(comName.substring(1));
				}
				sysUserVo.setComName(comName.substring(1));
			}


        }*/
        return new PageResult<>(list, sql.getInt());
    }


    /**
     * 审核通过并同步用户信息到用户表
     * @param obj
     * @return
     */
    public int updateAuditStatusAndSyncUser(SysUserAuth obj,String userName) {

        obj.setUpdTime(new Date());
        obj.setUpdUser(userName);
        int count = super.updateIgnoreNull(obj);
        if ( count > 0 && obj.getStatus()!=2) {
            SysUserAuth sysUserAuth = super.fetch(SysUserAuth.class,obj.getId());

            SysUser sysUser = new SysUser();

            //先同步到用户表
            sysUser.setOrgId(sysUserAuth.getOrgId());
            sysUser.setComId(sysUserAuth.getComId());
            sysUser.setUnionId(sysUserAuth.getUnionId());
            sysUser.setEducation(sysUserAuth.getEducation());
            sysUser.setUserName(sysUserAuth.getUserName());
            sysUser.setRealName(sysUserAuth.getRealName());
            sysUser.setHeadPortrait(sysUserAuth.getHeadPortrait());
            sysUser.setIdType(sysUserAuth.getIdType());
            sysUser.setIdNo(sysUserAuth.getIdNo());
            sysUser.setPhoneNumber(sysUserAuth.getPhoneNumber());
            sysUser.setTechnicalLevel(sysUserAuth.getTechnicalLevel());
            sysUser.setTitle(sysUserAuth.getTitle());
            sysUser.setUserType(1);
            sysUser.setNation(sysUserAuth.getNation());
            sysUser.setNumberNo(sysUserAuth.getNumberNo());
            sysUser.setJobNo(sysUserAuth.getJobNo());
            sysUser.setWorkStatus(sysUserAuth.getWorkStatus());
            sysUser.setHouseholdRegister(sysUserAuth.getHouseholdRegister());
            sysUser.setMembershipChangeDate(sysUserAuth.getMembershipChangeDate());
            sysUser.setMembershipChangeReason(sysUserAuth.getMembershipChangeReason());
            sysUser.setMembershipChangeType(sysUserAuth.getMembershipChangeType());
            sysUser.setBirthDate(sysUserAuth.getBirthDate());
            sysUser.setUpdTime(new Date());
            sysUser.setUpdUser(userName);
            sysUser.setSex(sysUserAuth.getSex());
            sysUser.setId(sysUserAuth.getUserId());
            super.updateIgnoreNull(sysUser);

        }
        return 1;
    }



    public SysUserVo checkId(String value) {

        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("u.id",value);
        List<SysUserVo> sysUserAuths =  super.query("sys.users.queryUserById",SysUserVo.class,cri);
        if ( CollectionUtils.isNotEmpty(sysUserAuths)) {
            return sysUserAuths.get(0);
        }
        return null;
   }


}
