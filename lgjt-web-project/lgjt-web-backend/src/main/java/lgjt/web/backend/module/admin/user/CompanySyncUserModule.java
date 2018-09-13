package lgjt.web.backend.module.admin.user;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;

import lombok.extern.log4j.Log4j;
import lgjt.domain.backend.user.CompanySyncUser;
import lgjt.services.backend.user.CompanySyncUserService;


@At("/companySyncUser")
@IocBean
@Log4j
public class CompanySyncUserModule {

    
	@Inject("companySyncUserService")
	CompanySyncUserService service;

	@At("/?")
	@GET
	public Object get(String id) {
		CompanySyncUser obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/?")
	@DELETE
	public Object delete(String ids) {
		return service.delete(ids);
	}
	
	@At("/")
	@POST
	public Object insert(@Param("..") CompanySyncUser obj) {
		CompanySyncUser o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") CompanySyncUser obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@GET
	public Object queryPage(@Param("..") CompanySyncUser obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@GET
	public Object query(@Param("..") CompanySyncUser obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
	@At("/checkRealname_flag")
	@GET
	public Object checkRealname_flag(String value) {
		return service.checkRealname_flag(value);
	}
}