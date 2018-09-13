package lgjt.web.books.module.book;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.domain.books.LgLetterInfo;
import lgjt.services.books.LgLetterInfoService;
import lgjt.services.books.LgLetterInfoService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;

/**
 * @author zhaotianyi
 * 书香莱钢
 */
@At("/lgBookInfo")
@IocBean
@Log4j
public class LgLetterInfoModule {

    
	@Inject("lgLetterInfoService")
	LgLetterInfoService service;

//	@At("/?")
//	@GET
//	public Object get(String id) {
//		LgLetterInfo obj = service.get(id);
//		if(null != obj) {
//			return Results.parse(Constants.STATE_SUCCESS, null,obj);
//		}else {
//			return Results.parse(Constants.STATE_FAIL,"数据不存在");
//		}
//	}
//
//	@At("/?")
//	@DELETE
//	public Object delete(String ids) {
//		return service.delete(ids);
//	}
//
//	@At("/")
//	@POST
//	public Object insert(@Param("..") LgLetterInfo obj) {
//		LgLetterInfo o = service.insert(obj);
//		if(o!=null) {
//			return Results.parse(Constants.STATE_SUCCESS,null,o);
//		}else {
//			return Results.parse(Constants.STATE_FAIL);
//		}
//	}
//	@At("/update")
//	@POST
//	public Object update(@Param("..") LgLetterInfo obj) {
//		int upd = service.update(obj);
//		if(upd>0) {
//			return Results.parse(Constants.STATE_SUCCESS);
//		}else {
//			return Results.parse(Constants.STATE_FAIL);
//		}
//	}
//
//	@At("/")
//	@GET
//	public Object queryPage(@Param("..") LgLetterInfo obj) {
//		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
//	}
//
//	@At("/query")
//	@GET
//	public Object query(@Param("..") LgLetterInfo obj) {
//		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
//	}
//
//
//	@At("/checkId")
//	@GET
//	public Object checkId(String value) {
//		return service.checkId(value);
//	}

	/*
	获取书香列表
	 */
	@At("/list")
	@GET
	public Object list(@Param("..") LgLetterInfo obj) {
        return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	/*
	获取书香详情
	 */
	@At("/info")
	@GET
	public Object info(@Param("id") String id) {
		LgLetterInfo obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}

	/**
	 * 获取分类下的列表
	 * @return
	 */
	@At("/queryListWith")
	@GET
	public Object queryListWith() {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryListWithCategory());
	}

}