package lgjt.web.backend.module.admin.role;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import lgjt.common.base.Authority;
import lgjt.domain.backend.role.SysMenu;
import lgjt.services.backend.role.SysMenuService;
import lgjt.domain.backend.utils.UserUtil;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/17
 * @Description: 菜单
 */

@At("/admin/menu")
@IocBean
@Log4j
public class SysMenuModule {

    @Inject("sysMenuService")
    SysMenuService service;

    @At("/queryMenu")
    @Authority("SYS_ROLE")
    public Object queryMenu() {
        List<SysMenu> list = service.queryMenuTree(UserUtil.getAdminUser());
        return Results.parse(Constants.STATE_SUCCESS, null, list);
    }
}
