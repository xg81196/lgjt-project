package lgjt.web.moments.module.admin;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.moments.challenge.LgChallenge;
import lgjt.domain.moments.challenge.LgChallengePk;
import lgjt.services.moments.admin.AdminLgChallengeService;
import lgjt.services.moments.challenge.LgChallengePkService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

/**
 * @author 赵天意
 * 跟拍后台相关
 */
@At("/admin/lgChallenge")
@IocBean
@Log4j
public class AdminLgChallengeModule {

    @Inject
    AdminLgChallengeService service;

    @Inject
    LgChallengePkService lgChallengePkService;

    /**
     * 竞技跟拍管理
     * @param obj
     * @return
     */
    @At("/managerList")
    public Object managerList(@Param("..") LgChallenge obj) {
        UserLoginInfo info = LoginUtil.getUserLoginInfo();
        String accountType = info.getInfos().get("extend5");
        //如果是班组长
        if(accountType.equals(3)) {
            obj.setGroupId(info.getInfos().get("orgId"));
        }

        return Results.parse(Constants.STATE_SUCCESS, null, service.managerList(obj));
    }

//    @At("/managerVerfilyList")
//    public Object managerVerfilyList(@Param("..") LgChallenge obj) {
//
//    }

    /**
     * 审核
     * @param id
     * @param verfilyAction
     * @param type
     * @return
     */
    @At("/verfilyAction")
    public Object verfilyAction(@Param("id") String id, @Param("verfilyAction") String verfilyAction, @Param("type") String type, @Param("checkMsg") String checkMsg) {

        String userName = LoginUtil.getUserName();
        if(type.equals("0")) {
            //发起
            return Results.parse(Constants.STATE_SUCCESS, null, service.verfilyAction(id, verfilyAction, userName, checkMsg));
        } else {
            return Results.parse(Constants.STATE_SUCCESS, null, lgChallengePkService.verfilyAction(id, verfilyAction, userName, checkMsg));
        }
    }
}
