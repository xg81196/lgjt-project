package lgjt.web.help.module.admin;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.domain.help.ERPWorkToDo;
import lgjt.domain.help.LgHelpInfo;
import lgjt.domain.help.vo.HelpInfoView;
import lgjt.services.help.LgHelpInfoService;
import lgjt.services.help.LgHelpInfoServiceMysql;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;


@At("/admin/helpInfo")
@IocBean
@Log4j
public class AdminHelpInfoModule {


    @Inject
    LgHelpInfoService service;


    @At("/queryHelpInfo")
    public Object query(@Param("..") LgHelpInfo helpInfo){
        return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageInfo(helpInfo));
    }


}
