package lgjt.web.rush.module.front;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.services.rush.sequence.RushSequenceService;


@At("/front/rushSequence")
@IocBean
@Log4j
public class RushSequenceModule {
    
	@Inject("rushSequenceService")
	RushSequenceService service;

	@At("/?")
	@GET
	public Object get(String id) {
		RushSequence obj = service.get(id);
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
	public Object insert(@Param("..") RushSequence obj) {
		RushSequence o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") RushSequence obj) {
		int upd = service.update(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}






	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
}