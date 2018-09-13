package lgjt.web.app.module.admin;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.services.app.admin.AdminLgGroupService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

@At("/admin/lgGroup")
@IocBean
@Log4j
public class AdminLgGroupModule {
    @Inject
    AdminLgGroupService service;

    @At("/getLgGroupBySQLServer")
    public Object getLgGroupBySQLServer(@Param("authKey") String authKey) {
        if(authKey.equals("111111")) {
            Integer result = service.getLgGroupBySQLServer();
            return Results.parse(Constants.STATE_SUCCESS, "共导入"+result+"条班组数据");
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }
}
