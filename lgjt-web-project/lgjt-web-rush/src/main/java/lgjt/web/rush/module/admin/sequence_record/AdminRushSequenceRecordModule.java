package lgjt.web.rush.module.admin.sequence_record;

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
import lgjt.domain.rush.sequence_record.RushSequenceRecord;
import lgjt.services.rush.sequence_record.RushSequenceRecordService;


@At("/admin/rushSequenceRecord")
@IocBean
@Log4j
public class AdminRushSequenceRecordModule {

    
	@Inject("rushSequenceRecordService")
	RushSequenceRecordService service;

	@At("/?")
	@POST
	public Object get(String id) {
		RushSequenceRecord obj = service.get(id);
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
	public Object insert(@Param("..") RushSequenceRecord obj) {
		RushSequenceRecord o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") RushSequenceRecord obj) {
		int upd = service.updateIgnoreNull(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@POST
	public Object queryPage(@Param("..") RushSequenceRecord obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@POST
	public Object query(@Param("..") RushSequenceRecord obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}

	@At("/queryPageBySearch")
	@POST
	public Object queryPageBySearch(@Param("..") RushSequenceRecord obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageBySearch(obj));
	}


	@At("/checkId")
	@POST
	public Object checkId(String value) {
		return service.checkId(value);
	}

	@At("/getOneMillionRecordTestData")
	@POST
	public Object getOneMillionRecordTestData() {
		return Results.parse(Constants.STATE_SUCCESS,null,service.getOneMillionRecordTestData());
	}
}