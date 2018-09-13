package lgjt.web.moments.module.usercomment;

import lgjt.common.base.service.SensitiveSearchService;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.moments.usercomment.LgUserComment;
import lgjt.services.moments.config.SysConfigService;
import lgjt.services.moments.usercomment.LgUserCommentService;
import lombok.extern.log4j.Log4j;

import org.nutz.ioc.annotation.InjectName;
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

/**
 * @author zhaotianyi
 * @depre 班圈评论相关
 */
@At("/lgUserComment")
@IocBean
@Log4j
public class LgUserCommentModule {

    
	@Inject("lgUserCommentService")
	LgUserCommentService service;

    @Inject
	SensitiveSearchService sensitiveSearchService;

	@Inject
	SysConfigService sysConfigService;
	/**
	 * 添加一个评论
	 * @param obj
	 * @return
	 */
	@At("/insert")
	@POST
	public Object insert(@Param("..") LgUserComment obj) {
	    //敏感词查询
        String context = obj.getContent();
		//获取敏感词的替换字眼
		String replaceText = sysConfigService.checkReplaceText();
		context = sensitiveSearchService.replaceSensitiveWord(context, replaceText);
        obj.setContent(context);
		obj.setCrtTime(new Date());
		//获取用户信息
		String userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");
		String userName = LoginUtil.getUserLoginInfo().getRealName();
		obj.setCrtUser(userName);
		obj.setUserName(userName);
		obj.setCrtIp(LoginUtil.getUserLoginInfo().getLoginIP());
		obj.setUserId(userId);//headPortrait
		//设置头像
		obj.setUserProfile(LoginUtil.getUserLoginInfo().getInfos().get("headPortrait"));

		LgUserComment o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") LgUserComment obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@GET
	public Object queryPage(@Param("..") LgUserComment obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@GET
	public Object query(@Param("..") LgUserComment obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
}