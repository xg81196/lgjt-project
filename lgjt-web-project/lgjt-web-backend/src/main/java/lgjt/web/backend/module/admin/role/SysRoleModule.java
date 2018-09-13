package lgjt.web.backend.module.admin.role;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.Authority;
import lgjt.domain.backend.role.SysRole;
import lgjt.services.backend.role.SysRoleService;
import lgjt.domain.backend.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author wuguangwei
 * @date 2018/4/17
 * @Description: 角色管理
 */

@At("/admin/role")
@IocBean
@Log4j
public class SysRoleModule {

    @Inject
    SysRoleService service;

    /**
     * 删除角色
     * @param id
     * @return
     */
    @At("/delete")
    @Authority("SYS_ROLE")
    public Object delete(@Param("ids") String ids) {
        try {
           /* SysRole role = service.fetch(SysRole.class, Cnd.where("super_id", "=", id));
            if(role != null){
                return Results.parse(Constants.STATE_FAIL, "角色下含有下级角色，请先删除下级角色");
            }*/
            String result = service.delete(ids);
            if (StringTool.isNull(result)) {
                return Results.parse(Constants.STATE_SUCCESS, null);
            } else {
                return Results.parse(Constants.STATE_FAIL, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL, "删除失败！");
        }
    }


    /**
     * 添加角色
     * @param obj
     * @return
     */
    @At("/insertUpdate")
    @Authority("SYS_ROLE")
    public Object insertUpdate(@Param("..") SysRole obj) {
        boolean result = false;
        try {
            if(StringTool.isNull(obj.getId())) {
                obj.setSuperId("-1");
                SysRole sr=service.insert(obj,UserUtil.getAdminUser().getUserName());
                if(sr!=null){
                    result = true;
                }else{
                    result = false;
                }
            }else {
                obj.setSuperId("-1");
                int upd = service.update(obj);
                if(upd>0) {
                    result = true;
                }
            }

            if(result) {
                return Results.parse(Constants.STATE_SUCCESS,null,obj);
            }else {
                return Results.parse(Constants.STATE_FAIL,"角色名不能重复");
            }
        } catch (Exception e) {
            log.error(e,e);
            return Results.parse(Constants.STATE_FAIL,"操作失败！");
        }

    }


    /**
     * 更新角色信息
     * @param obj
     * @return
     */
    @At("/update")
    @Authority("SYS_ROLE")
    public Object update(@Param("..") SysRole obj) {
        obj.setSuperId("-1");
        int upd = service.updateIgnoreNull(obj);
        if (upd > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    @At("/query")
    @Authority("SYS_ROLE")
    public Object query(@Param("..") SysRole obj) {
        return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
    }


    /**
     * 为角色分配菜单列表
     * @param roleId
     * @param resIds
     * @return
     */
    @At("/insertRoleMenu")
    @Authority("SYS_ROLE")
    public Object insertRoleMenu(@Param("roleId") String roleId,
                                 @Param("resIds") String resIds) {
        try {
            String str=service.insertRoleRes(roleId, resIds,UserUtil.getAdminUser().getUserName());
            if(StringTool.isEmpty(str)){
                return Results.parse(Constants.STATE_SUCCESS);
            }else{
                return Results.parse(Constants.STATE_FAIL,str,null);
            }
        } catch (Exception e) {
            log.error("save role res error", e);
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    /**
     * 为角色分配人员
     * @param roleId
     * @param userIds
     * @return
     */
    @At("/insertRoleUser")
    @Authority("SYS_ROLE")
    public Object insertRoleUser(@Param("roleId") String roleId, @Param("userIds") String userIds) {
        if(StringTool.isNull(roleId)||StringTool.isNull(userIds)){
            return Results.parse(Constants.STATE_FAIL,"传参错误，添加失败！");
        }
        try {
            if (!"-1".equals(roleId)) {
                String msg = service.insertRoleUser(roleId, userIds,UserUtil.getAdminUser());
                if(StringTool.isNull(msg)){
                    return Results.parse(Constants.STATE_SUCCESS,"添加权限成功！");
                }else{
                    return Results.parse(Constants.STATE_FAIL,msg);
                }
            }else{
                return Results.parse(Constants.STATE_FAIL,"添加权限失败！权限不得为根！");
            }
        } catch (Exception e) {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    /**
     * 查询角色下的用户
     * @param role
     * @return
     */
    @At("/queryRoleUser")
    @Authority("SYS_ROLE")
    public Object queryRoleUser(@Param("..") SysRole role, @Param("userName") String userName) {

        return Results.parse(Constants.STATE_SUCCESS, null,
                service.queryRoleUser(role.getId(),userName));
    }

    @At("/queryRoleUserByName")
    @Authority("SYS_ROLE")
    public Object queryRoleUserByName(@Param("userName") String userName) {
        return Results.parse(Constants.STATE_SUCCESS, null,
                service.queryRoleUserByName(userName));
    }

    /**
     * 删除角色下的用户
     * @param ids
     * @param roleId
     * @return
     */
    @At("/deleteRoleUser")
    @Authority("SYS_ROLE")
    public Object deleteRoleUser(@Param("ids") String ids,
                                 @Param("roleId") String roleId) {
        try {
            String str=service.deleteRoleUser(ids, roleId, UserUtil.getAdminUser());
            if(StringTool.isEmpty(str)){
                return Results.parse(Constants.STATE_SUCCESS, null);

            }else{

                return Results.parse(Constants.STATE_FAIL, str,null);
            }

        } catch (Exception e) {
            log.error(e,e);
            return Results.parse(Constants.STATE_FAIL, "操作失败");
        }
    }

    /**
     * 分页查询角色
     * @param obj
     * @return
     */
    @At("/queryPage")
    @Authority("SYS_ROLE")
    public Object queryPage(@Param("..") SysRole obj) {
        return Results.parse(Constants.STATE_SUCCESS, null,
                service.queryPage(obj));
    }

    /**
     * 权限分配查询角色树BH3468410 BH4468410
     * @return
     */
    @At("/queryRoleTree")
    @Authority("SYS_ROLE")
    public Object queryRoleTree(@Param("state") String state,@Param("name")String name) {
        return Results.parse(Constants.STATE_SUCCESS, null,
                service.queryRoleTree(state,name,UserUtil.getAdminUser()));
    }

    /**
     * 查询角色树
     * @param obj
     * @return
     */
    @At("/listTree")
    @Authority("SYS_ROLE")
    public Object listTree(@Param("..") SysRole obj) {
        return Results.parse(Constants.STATE_SUCCESS,null,service.listTree(obj,UserUtil.getAdminUser()));
    }

}
