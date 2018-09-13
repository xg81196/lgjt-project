package lgjt.web.backend.module.admin.city;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.Results;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.dict.SysDict;
import lgjt.services.backend.city.SysCityService;


/**
 * @author wuguangwei
 */
@IocBean
@At("/admin/city")
public class SysCityModule {

	@Inject
	private SysCityService sysCityService;

	@At("/listSon")
	@Authority("")
	//@Authority("SYS_CITY")
	public Object listSon(@Param("pid") String pid) {
			return Results.parse(Constants.STATE_SUCCESS, null,
					sysCityService.listSons(pid,null));
	}
	
	@At("/save")
	@Authority("")
	//@Authority("SYS_CITY")
	public Object save(@Param("..") SysCity city) {
		boolean result = sysCityService.save(city);
		if(result) {
			return Results.parse(Constants.STATE_SUCCESS, null);
		}else {
			return Results.parse(Constants.STATE_FAIL, null);
		}
			
	}

	@At("/getCityName")
	@Authority("")
	public Object getCityName(@Param("..") SysDict dic) {
		try {
			return Results.parse(Constants.STATE_SUCCESS, null,
					sysCityService.getRegionString(dic.getId()));
		} catch (Exception e) {
			return Results.parse(Constants.STATE_FAIL);
		}
	}


	@At("/listTree")
	@Authority("")
	//@Authority("SYS_CITY")
	public Object listTree(@Param("pid") String pid) {
		return Results.parse(Constants.STATE_SUCCESS, null,
				sysCityService.querySysCityTree());
	}
}
