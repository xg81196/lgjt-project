package lgjt.web.backend.module.admin.user;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.Authority;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.domain.backend.utils.UserUtil;

/**
 * @author wuguangwei
 * @date 2018/4/17
 * @Description: 用户数据权限
 */

@At("/admin/sysUserData")
@IocBean
@Log4j
public class SysUserDataModule {

    @Inject
    private SysUserDataService sysUserDataService;

    @Inject
    private SysOrganizationService sysOrganizationService;

    /**
     * 用户权限数据权限
     * @param obj
     * @return
     */
    @At("/authTree")
    @Authority("USER_STUDENT")
    public Object authTree(@Param("..") SysOrganization obj) {

        if (StringUtils.isBlank( obj.getId() )) {
            obj.setSuperId("-1");
           return  Results.parse(Constants.STATE_SUCCESS, "",sysOrganizationService.queryOrgList(obj));
        } else {
            return Results.parse(Constants.STATE_SUCCESS, "",sysUserDataService.querySubOrgList(obj.getId()));
        }

    }



    /**
     * 获取用户权限
     * @param obj
     * @return
     */
    @At("/get")
    @Authority("USER_STUDENT")
    public Object get(@Param("..") SysUserData obj) {

        return Results.parse(Constants.STATE_SUCCESS, "", sysUserDataService.getOrgsByUserId(UserUtil.getAdminUser().getId()));
    }


    /**
     * 保存用户权限
     * @param obj
     * @return
     */
    @At("/save")
    @Authority("USER_STUDENT")
    public Object save(@Param("..") SysUserData obj) {

        sysUserDataService.save(obj, UserUtil.getAdminUser(),1);

        return Results.parse(Constants.STATE_SUCCESS, "保存成功");
    }


    /**
     * 查询组织机构用户
     * @param sysUserAdmin
     * @return
     */
    @At("/queryDataScope")
    @Authority("USER_STUDENT")
    public Object queryDataScope(@Param("..")SysUserAdmin sysUserAdmin) {

        return Results.parse(Constants.STATE_SUCCESS,"",sysUserDataService.queryDataScope(UserUtil.getAdminUser()));

    }

}
