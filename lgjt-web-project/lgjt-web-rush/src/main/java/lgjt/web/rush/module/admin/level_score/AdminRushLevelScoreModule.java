package lgjt.web.rush.module.admin.level_score;

import lombok.extern.log4j.Log4j;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.Authority;
import lgjt.domain.rush.level_score.RushLevelScore;
import lgjt.services.rush.level_score.RushLevelScoreService;


@At("/admin/rushLevelScore")
@IocBean
@Log4j
public class AdminRushLevelScoreModule {

    
	@Inject("rushLevelScoreService")
	RushLevelScoreService service;

	@At("/?")
	@GET
	@Authority("RUSH")
	public Object get(String id) {
		RushLevelScore obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/?")
	@DELETE
	@Authority("RUSH")
	public Object delete(String ids) {
		return service.delete(ids);
	}
	
	@At("/")
	@POST
	@Authority("RUSH")
	public Object insert(@Param("..") RushLevelScore obj) {
		RushLevelScore o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	@Authority("RUSH")
	public Object update(@Param("..") RushLevelScore obj) {
		int upd = service.updateIgnoreNull(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@POST
	@Authority("RUSH")
	public Object queryPage(@Param("..") RushLevelScore obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@POST
	@Authority("RUSH")
	public Object query(@Param("..") RushLevelScore obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}

	@At("/queryPageBySearch")
	@Authority("RUSH")
	public Object queryPageBySearch(@Param("..") RushLevelScore obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageBySearch(obj));
	}

	@At("/checkId")
	@POST
	@Authority("RUSH")
	public Object checkId(String value) {
		return service.checkId(value);
	}
}