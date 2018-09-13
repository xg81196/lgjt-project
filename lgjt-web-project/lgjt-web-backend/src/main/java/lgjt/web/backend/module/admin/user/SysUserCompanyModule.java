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
import lgjt.domain.backend.user.SysUserCompany;
import lgjt.services.backend.user.SysUserCompanyService;


@At("/sysUserCompany")
@IocBean
@Log4j
public class SysUserCompanyModule {

    
	@Inject("sysUserCompanyService")
	SysUserCompanyService service;

	@At("/?")
	@GET
	public Object get(String id) {
		SysUserCompany obj = service.get(id);
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
	public Object insert(@Param("..") SysUserCompany obj) {
		SysUserCompany o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") SysUserCompany obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@GET
	public Object queryPage(@Param("..") SysUserCompany obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@GET
	public Object query(@Param("..") SysUserCompany obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
}