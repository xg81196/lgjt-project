package lgjt.web.backend.module.admin.industry;

import lombok.extern.log4j.Log4j;

import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.Authority;
import lgjt.domain.backend.industry.SysIndustry;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.services.backend.industry.SysIndustryService;


@At("/admin/industry")
@IocBean
@Log4j
public class SysIndustryModule {

    
	@Inject("sysIndustryService")
	SysIndustryService service;

	@At("/get")
	@Authority("")
	public Object get(String id) {
		SysIndustry obj = service.get(id);
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
	@POST
	public Object insert(@Param("..") SysIndustry obj) {
		SysIndustry o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") SysIndustry obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/queryPage")
	@GET
	public Object queryPage(@Param("..") SysIndustry obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	public Object query(@Param("..") SysIndustry obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}


	/**
	 * 查询所有机构
	 * @param obj
	 * @return
	 */
	@At("/industryTree")
	@Authority("")
	public Object industryTree(@Param("..") SysIndustry obj) {

		return  Results.parse(Constants.STATE_SUCCESS, "",service.querySysIndustryTree());

	}


	/*@At("/industryImport")
	public Object industryImport(@Param("..") SysIndustry obj) {

		return  Results.parse(Constants.STATE_SUCCESS, "",service.importIndustryString());

	}*/
}