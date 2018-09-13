package lgjt.web.moments.module.groupmessage;

import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.service.SensitiveSearchService;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.moments.config.SysConfig;
import lgjt.domain.moments.groupmessage.LgGroupMessage;
import lgjt.domain.moments.groupmessage.LgGroupMessageVo;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.services.moments.config.SysConfigService;
import lgjt.services.moments.groupmessage.LgGroupMessageService;
import lgjt.services.moments.usercomment.LgUserCommentService;
import lgjt.services.moments.userlevelscore.LgUserLevelScoreService;
import lgjt.services.moments.userlike.LgUserLikeService;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhaotianyi
 * @depre 班圈相关
 */
@At("/lgGroupMessage")
@IocBean
@Log4j
/**
 *
 */
public class LgGroupMessageModule {


    @Inject("lgGroupMessageService")
    LgGroupMessageService service;

    @Inject
    LgUserLevelScoreService lgUserLevelScoreService;

    @Inject
    SensitiveSearchService sensitiveSearchService;

    @Inject
    LgUserCommentService lgUserCommentService;

    @Inject
    LgUserLikeService lgUserLikeService;

    @Inject
    SysConfigService sysConfigService;

    @At("/?")
    @GET
    public Object get(String id) {
        LgGroupMessage obj = service.get(id);
        if (null != obj) {
            return Results.parse(Constants.STATE_SUCCESS, null, obj);
        } else {
            return Results.parse(Constants.STATE_FAIL, "数据不存在");
        }
    }

    @At("/?")
    @DELETE
    public Object delete(String ids) {
        return service.delete(ids);
    }


    /**
     * 插入一个班圈
     *
     * @param obj
     * @return
     */
    @At("/insert")
    @POST
    public Object insert(@Param("..") LgGroupMessage obj) {
        //首先检查敏感词
        String context = obj.getContent();

        //获取敏感词的替换字眼
        String replaceText = sysConfigService.checkReplaceText();
        context = sensitiveSearchService.replaceSensitiveWord(context, replaceText);
        obj.setContent(context);

        UserLoginInfo uli = LoginUtil.getUserLoginInfo();
        obj.setUserId(uli.getInfos().get("userId"));
        obj.setCrtUser(uli.getUserName());
        obj.setCrtIp(uli.getLoginIP());
        obj.setCrtTime(new Date());
        if (StringUtils.isNotEmpty(uli.getRealName())) {
            obj.setRealName(uli.getRealName());
        }
        obj.setVerifyStatus(0);//默认是待审核
        obj.setStatus(1);//默认是启用
        LgGroupMessage o = service.insert(obj);
        if (o != null) {
            //这里面加入一个积分，需要审核，所以现在不加分
            LgUserLevelScore uls = new LgUserLevelScore();
            uls.setUserName(o.getCrtUser());
            uls.setMark("发布一条班圈");
            uls.setScore(Long.valueOf("0"));
            uls.setCrtUser(o.getCrtUser());
            uls.setCrtTime(new Date());
            uls.setUpdTime(new Date());
            uls.setCrtIp(o.getCrtIp());
            //设置班圈ID
            uls.setMessageId(o.getId());
            //设置班圈名称
            uls.setGroupName(o.getGroupName());
            //设置班组ID
            uls.setGroupId(o.getGroupId());
            //设置机构ID
            uls.setOrgId(uli.getInfos().get("orgId"));
            lgUserLevelScoreService.insert(uls);
            return Results.parse(Constants.STATE_SUCCESS, null, o);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    /**
     * 更新班圈
     * @param obj
     * @return
     */
    @At("/update")
    @POST
    public Object update(@Param("..") LgGroupMessage obj) {
        obj.setUpdTime(new Date());
        obj.setUpdIp(LoginUtil.getUserLoginInfo().getLoginIP());
        if (StringUtils.isNotEmpty(LoginUtil.getUserLoginInfo().getRealName())) {
            obj.setUpdUser(LoginUtil.getUserLoginInfo().getRealName());
        }
        int upd = service.update(obj);
        if (upd > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    @At("/")
    @GET
    public Object queryPage(@Param("..") LgGroupMessage obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryPage(obj));
    }

    @At("/query")
    public Object query(@Param("..") LgGroupMessage obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.query(obj));
    }


    @At("/checkId")
    @GET
    public Object checkId(String value) {
        return service.checkId(value);
    }

    /**
     * 班圈列表
     * @param obj
     * @return
     */
    @At("/messageList")
    @GET
    public Object messageList(@Param("..") LgGroupMessageVo obj) {
	    obj.setUserId(LoginUtil.getUserLoginInfo().getInfos().get("userId"));
        return Results.parse(Constants.STATE_SUCCESS, null, service.messageList(obj));
    }

    /**
     * 班圈详情信息
     * @param obj
     * @return
     */
    @At("/messageInfo")
    @GET
    public Object messageInfo(@Param("..") LgGroupMessage obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.messageInfo(obj));
    }

    /**
     * 我的班圈
     * @return
     */
    @At("/queryMessageList")
    public Object queryMyMessageList(@Param("..") LgGroupMessage obj) {
        obj.setUserId(LoginUtil.getUserLoginInfo().getInfos().get("userId"));
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryMyMessageList(obj));
    }

    /**
     * 删除班圈
     * 仅仅是单删
     */
    @At("/delete")
    public Object deleteGroupMessage(@Param("id") String id) {
        int del = service.delete(id);
        if (del > 0) {
            //删除成功后删除喜欢的，还要删除评论
            lgUserLikeService.deleteByMessageId(id);
            lgUserCommentService.deleteByMessageId(id);
            return Results.parse(Constants.STATE_SUCCESS, null);
        } else {
            return Results.parse(Constants.STATE_FAIL, null);
        }
    }
}