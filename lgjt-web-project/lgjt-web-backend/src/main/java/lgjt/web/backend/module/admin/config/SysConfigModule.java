package lgjt.web.backend.module.admin.config;

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
import lgjt.domain.backend.config.SysConfig;
import lgjt.services.backend.config.SysConfigService;


@At("/admin/config")
@IocBean
@Log4j
public class SysConfigModule {

    
	@Inject("sysConfigService")
	SysConfigService service;

	@At("/get")
	public Object get(String id) {
		SysConfig obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/delete")
	public Object delete(String ids) {
		return service.delete(ids);
	}
	
	@At("/save")
	public Object insert(@Param("..") SysConfig obj) {
		SysConfig o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	public Object update(@Param("..") SysConfig obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/list")
	public Object queryPage(@Param("..") SysConfig obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	public Object query(@Param("..") SysConfig obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
	@At("/checkConfig_name")
	@GET
	public Object checkConfig_name(String value) {
		return service.checkConfig_name(value);
	}
}