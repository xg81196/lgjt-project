package lgjt.web.moments.module.userlike;

import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.moments.userlike.LgUserLike;
import lgjt.services.moments.userlike.LgUserLikeService;
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
import java.util.List;


@At("/lgUserLike")
@IocBean
@Log4j
public class LgUserLikeModule {

    
	@Inject("lgUserLikeService")
	LgUserLikeService service;

	@At("/likeAction")
	public Object likeAction(@Param("..") LgUserLike obj, @Param("action") String action) {
		UserLoginInfo uli = LoginUtil.getUserLoginInfo();
		if("1".equals(action)) {
			//1是点赞,插入信息
			obj.setCrtUser(uli.getUserName());
			obj.setUserId(uli.getInfos().get("userId"));
			obj.setCrtTime(new Date());
			obj.setCrtIp(uli.getLoginIP());
			LgUserLike lgUserLike = service.insert(obj);
			if(lgUserLike != null) {
				return Results.parse(Constants.STATE_SUCCESS, "点赞",lgUserLike);
			} else {
				return Results.parse(Constants.STATE_FAIL);
			}
		} else {
			//0是取消点赞,就是删除信息
			obj.setUserId(uli.getInfos().get("userId"));
			List<LgUserLike> lgUserLikes = service.query(obj);
			LgUserLike lgUserLike = lgUserLikes.get(0);
			int del = service.delete(lgUserLike.getId());
			if(del > 0) {
				return Results.parse(Constants.STATE_SUCCESS, "取消点赞");
			} else {
				return Results.parse(Constants.STATE_FAIL);
			}
		}
	}

}