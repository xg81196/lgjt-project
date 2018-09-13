package lgjt.web.backend.module.admin.user;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import lombok.extern.log4j.Log4j;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.Authority;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.SignUtils;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.domain.backend.user.vo.SysUserAdminVo2;
import lgjt.services.backend.role.SysRoleService;
import lgjt.services.backend.user.SysUserAdminService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.domain.backend.utils.UserUtil;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;


@At("/admin/userAdmin")
@IocBean
@Log4j
public class SysUserAdminModule {


    @Inject("sysUserAdminService")
    SysUserAdminService service;

    @Inject
    SysUserDataService sysUserDataService;
    @Inject
    SysRoleService sysRoleService;


    @At("/get")
    @Authority("USER_PLAYFORM")
    public Object get(@Param("id") String id) {
        SysUserAdminVo2 obj = service.get2(id);
        if (null != obj) {
            if (!obj.getPassword().equals(obj.getExtend3())) {
                obj.setPassword("");
                return Results.parse(Constants.STATE_SUCCESS, null, obj);
            }
            obj.setPassword(obj.getExtend2());
            return Results.parse(Constants.STATE_SUCCESS, null, obj);
        } else {
            return Results.parse(Constants.STATE_FAIL, "数据不存在");
        }
    }

    @At("/delete")
    @Authority("USER_PLAYFORM")
    public Object delete(@Param("ids") String ids) {
        service.delete(ids);
        //移除数据权限
        UserUtil.delUserData();

        return Results.parse(Constants.STATE_SUCCESS, "删除成功");
    }

    @At("/save")
    @Authority("USER_PLAYFORM")
    public Object insert(@Param("..") SysUserData userData, @Param("..") SysUserAdmin obj, @Param("type") int type) {

        if (service.checkUser_name(obj.getUserName()) != null) {
            return Results.parse(Constants.STATE_FAIL, "用户名重复");
        }

        String id = UUIDUtil.getUUID();
        obj.setId(id);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        obj.setSalt(salt);
        //前端已经对密码进行加密
        obj.setPassword(DigestUtils.md5Hex(obj.getPassword() + obj.getSalt()));
        obj.setCrtUser(UserUtil.getAdminUser().getUserName());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setCrtTime(new Date());
        //防篡改。防篡改
        String sign = SignUtils.sign(obj.getAddAntiTamperString(obj), PropertyUtil.getProperty("secret"));
        obj.setAntiTamper(sign);
        obj.setCrtUser(UserUtil.getAdminUser().getUserName());
        obj.setCrtTime(new Date());
        obj.setExtend1(userData.getOrgIdList());
        obj.setExtend4(sysRoleService.checkRole_name("平台管理员").getId());
        obj.setExtend2(obj.getPassword());
        obj.setExtend3(obj.getPassword());
        SysUserAdmin o = service.insert(obj);
        if (o != null) {
            //保存数据权限
            sysUserDataService.save(userData, o, type);
            return Results.parse(Constants.STATE_SUCCESS, null, o);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    @At("/singleSave")
    @Authority("USER_PLAYFORM")
    public Object singleSave(@Param("..") SysUserData userData, @Param("..") SysUserAdmin obj, @Param("type") int type) {

        if (service.checkUser_name(obj.getUserName()) != null) {
            return Results.parse(Constants.STATE_FAIL, "用户名重复");
        }

        String id = UUIDUtil.getUUID();
        obj.setId(id);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        obj.setSalt(salt);
        //前端已经对密码进行加密
        obj.setPassword(DigestUtils.md5Hex(obj.getPassword() + obj.getSalt()));
        obj.setCrtUser(UserUtil.getAdminUser().getUserName());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setCrtTime(new Date());
        //防篡改。防篡改
        String sign = SignUtils.sign(obj.getAddAntiTamperString(obj), PropertyUtil.getProperty("secret"));
        obj.setAntiTamper(sign);
        obj.setCrtUser(UserUtil.getAdminUser().getUserName());
        obj.setCrtTime(new Date());
        obj.setExtend1(userData.getOrgIdList());
        obj.setExtend4(sysRoleService.checkRole_name("平台管理员").getId());
        obj.setExtend2(obj.getPassword());
        obj.setExtend3(obj.getPassword());
        SysUserAdmin o = service.insert(obj);
        initUserRole(obj, obj.getRoleId());
        if (o != null) {
            userData.setId(UUIDUtil.getUUID());
            userData.setAdminUserId(o.getId());
            userData.setObjectId(o.getOrgId());
            userData.setObjectType(type);
            sysUserDataService.insert(userData);
            return Results.parse(Constants.STATE_SUCCESS, null, o);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }

    }

    private void initUserRole(SysUserAdmin sysAdmin, String roleId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setAdminUserId(sysAdmin.getId());
        sysUserRole.setRoleId(roleId);
        sysUserRole.setUserName(sysAdmin.getUserName());
        sysUserRole.setCrtUser(sysAdmin.getUserName());
        sysUserRole.setCrtTime(new Date());
        sysUserRole.setCrtIp(ClientInfo.getIp());
        sysRoleService.insert(sysUserRole);
    }


    @At("/update")
    @Authority("USER_PLAYFORM")
    public Object update(@Param("..") SysUserData userData, @Param("..") SysUserAdmin obj, @Param("type") int type) {

        SysUserAdmin userAdmin = service.get(obj.getId());
        if (StringUtils.isNotBlank(obj.getUserName())) {
            userAdmin.setUserName(obj.getUserName());
        }

        if (StringUtils.isNotBlank(obj.getEmail())) {
            userAdmin.setEmail(obj.getEmail());
        }

        if (StringUtils.isNotBlank(obj.getIdNo())) {
            userAdmin.setIdNo(obj.getIdNo());
        }

        if (StringUtils.isNotBlank(obj.getRealName())) {
            userAdmin.setRealName(obj.getRealName());
        }

        if (obj.getStatus() != null) {
            userAdmin.setStatus(obj.getStatus());
        }
        userAdmin.setPhoneNumber(obj.getPhoneNumber());
        if (StringUtils.isNotBlank(obj.getRemark())) {
            userAdmin.setRemark(obj.getRemark());
        }
        userAdmin.setUpdUser(UserUtil.getAdminUser().getUserName());
        userAdmin.setUpdTime(new Date());
        userAdmin.setUpdIp(ClientInfo.getIp());
        //防篡改。防篡改
        String sign = SignUtils.sign(obj.getUpdAntiString(userAdmin), PropertyUtil.getProperty("secret"));
        obj.setAntiTamper(sign);
        userAdmin.setExtend1(userData.getOrgIdList());
        obj.setExtend4(obj.getRoleId());
        int upd = service.update(userAdmin);
        if (upd > 0) {
            //保存数据权限
            sysUserDataService.save(userData, obj, type);
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    @At("/singleUpdate")
    @Authority("USER_PLAYFORM")
    public Object singleUpdate(@Param("..") SysUserData userData, @Param("..") SysUserAdmin obj, @Param("type") int type) {

        SysUserAdmin userAdmin = service.get(obj.getId());
        if (StringUtils.isNotBlank(obj.getUserName())) {
            userAdmin.setUserName(obj.getUserName());
        }

        if (StringUtils.isNotBlank(obj.getEmail())) {
            userAdmin.setEmail(obj.getEmail());
        }

        if (StringUtils.isNotBlank(obj.getIdNo())) {
            userAdmin.setIdNo(obj.getIdNo());
        }

        if (StringUtils.isNotBlank(obj.getRealName())) {
            userAdmin.setRealName(obj.getRealName());
        }

        if (obj.getStatus() != null) {
            userAdmin.setStatus(obj.getStatus());
        }
        userAdmin.setPhoneNumber(obj.getPhoneNumber());
        if (StringUtils.isNotBlank(obj.getRemark())) {
            userAdmin.setRemark(obj.getRemark());
        }
        userAdmin.setUpdUser(UserUtil.getAdminUser().getUserName());
        userAdmin.setUpdTime(new Date());
        userAdmin.setUpdIp(ClientInfo.getIp());
        //防篡改。防篡改
        String sign = SignUtils.sign(obj.getUpdAntiString(userAdmin), PropertyUtil.getProperty("secret"));
        obj.setAntiTamper(sign);
        userAdmin.setExtend1(userData.getOrgIdList());
        obj.setExtend4(obj.getRoleId());
        int upd = service.update(userAdmin);
        if (upd > 0) {
            //保存数据权限
            sysUserDataService.singleUpdate(userData, obj, type);
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    @At("/queryPage")
    @Authority("USER_PLAYFORM")
    //@Ok("json:{quoteName:true, ignoreNull:false}")
    public Object queryPage(@Param("..") SysUserAdmin obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryUserRolePage(obj, UserUtil.getAdminUser().getUserName()));
    }

    @At("/queryAdminPageByOrgId")
    @Authority("USER_PLAYFORM")
    //@Ok("json:{quoteName:true, ignoreNull:false}")
    public Object queryAdminPageByOrgId(@Param("orgId") String orgId, @Param("type") Integer type) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryAdminPageByOrgId(orgId, type));
    }

    @At("/queryUserRolePage")
    @Authority("USER_PLAYFORM")
    public Object queryUserRolePage(@Param("..") SysUserAdmin obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryUserRolePage(obj, UserUtil.getAdminUser().getUserName()));
    }

    @At("/query")
    @Authority("USER_PLAYFORM")
    public Object query(@Param("..") SysUserAdmin obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.query(obj));
    }

    @At("/checkId")
    @Authority("USER_PLAYFORM")
    public Object checkId(String value) {
        return service.checkId(value);
    }

    @At("/checkUser_name")
    @Authority("USER_PLAYFORM")
    public Object checkUser_name(String value) {
        return service.checkUser_name(value);
    }

    @At("/getGroupAdminForSQLServer")
    @POST
    public Object getGroupAdminForSQLServer(@Param("authKey") String authKey) {
        if(authKey.equals("111111")) {
            int count = service.getGroupAdminForSQLServer();
            return Results.parse(Constants.STATE_SUCCESS, "成功导入班组长"+count+"个");
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }
}