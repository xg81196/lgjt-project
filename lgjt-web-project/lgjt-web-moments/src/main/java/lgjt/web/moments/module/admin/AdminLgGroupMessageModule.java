package lgjt.web.moments.module.admin;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.common.base.utils.TokenUtils;
import lgjt.domain.moments.groupmessage.LgGroupMessage;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.services.moments.admin.AdminLgGroupMessageService;
import lgjt.services.moments.usercomment.LgUserCommentService;
import lgjt.services.moments.userlevelscore.LgUserLevelScoreService;
import lgjt.services.moments.userlike.LgUserLikeService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author zhaotianyi
 * @depre 班圈后台相关
 */
@At("/admin/lgGroupMessage")
@IocBean
@Log4j
public class AdminLgGroupMessageModule {


    @Inject("adminLgGroupMessageService")
    AdminLgGroupMessageService service;

    @Inject
    LgUserLikeService lgUserLikeService;

    @Inject
    LgUserCommentService lgUserCommentService;

    @Inject
    LgUserLevelScoreService lgUserLevelScoreService;

    /**
     * 班圈审核管理列表
     */
    @At("/queryGroupMessageVerify")
    public Object queryGroupVerify(@Param("..") LgGroupMessage obj) {
        //仅仅查询未通过的班圈
        UserLoginInfo info = LoginUtil.getUserLoginInfo();
        String accountType = info.getInfos().get("extend5");
        obj.setVerifyStatus(2);

        //如果是班组长
        if(accountType.equals(3)) {
            obj.setGroupId(info.getInfos().get("orgId"));
        }
        obj.setVerifyStatus(0);
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryPage(obj));
    }

    /**
     * 审核接口
     */
    @At("/verifyAction")
    public Object verifyAction(@Param("..") LgGroupMessage obj) {
        //获取到当前登录管理员用户
        UserLoginInfo uli = LoginUtil.getUserLoginInfo();
        //设置审核人
        if (StringUtils.isNotEmpty(uli.getRealName())) {
            obj.setVerifyUser(uli.getRealName());
        }
        //设置更新时间
        obj.setUpdTime(new Date());
        int upd = service.updateIgnoreNull(obj);
        if (upd > 0) {
            //成功后判断审核状态
            Integer verifyStatus =  obj.getVerifyStatus();
            if(verifyStatus==1) {
                //1通过 加分
                LgUserLevelScore lull = new LgUserLevelScore();
                lull.setMessageId(obj.getId());
                //查询
                List<LgUserLevelScore> lgUserLevelScores = lgUserLevelScoreService.query(lull);
                lull = lgUserLevelScores.get(0);
                long score = Long.valueOf("2");
                lull.setScore(score);
                lgUserLevelScoreService.updateIgnoreNull(lull);
            }

            return Results.parse(Constants.STATE_SUCCESS, null);
        } else {
            return Results.parse(Constants.STATE_FAIL, null);
        }
    }

    /**
     * 禁用启用班圈
     */
    @At("/statusAction")
    public Object statusAction(@Param("..") LgGroupMessage obj) {
        obj.setUpdTime(new Date());
        int upd = service.updateIgnoreNull(obj);
        if (upd > 0) {
            return Results.parse(Constants.STATE_SUCCESS, null);
        } else {
            return Results.parse(Constants.STATE_FAIL, null);
        }
    }

    /**
     * 删除班圈
     */
    @At("/deleteGroupMessage")
    public Object deleteGroupMessage(@Param("ids") String ids) {
        int del = service.delete(ids);
        if (del > 0) {
            //删除成功后删除喜欢的，还要删除评论
            lgUserLikeService.deleteByMessageId(ids);
            lgUserCommentService.deleteByMessageId(ids);
            return Results.parse(Constants.STATE_SUCCESS, null);
        } else {
            return Results.parse(Constants.STATE_FAIL, null);
        }
    }


}