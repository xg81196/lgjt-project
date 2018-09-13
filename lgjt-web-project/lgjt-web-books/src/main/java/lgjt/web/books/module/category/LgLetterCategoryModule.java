package lgjt.web.books.module.category;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.domain.books.LgLetterCategory;
import lgjt.services.books.LgLetterCategoryService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;

/**
 * @author zhaotianyi
 * 书香莱钢分类
 */
@At("/LgLetterCategory")
@IocBean
@Log4j
public class LgLetterCategoryModule {

    
	@Inject("LgLetterCategoryService")
	LgLetterCategoryService service;

//	@At("/?")
//	@GET
//	public Object get(String id) {
//		LgLetterCategory obj = service.get(id);
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
//	public Object insert(@Param("..") LgLetterCategory obj) {
//		LgLetterCategory o = service.insert(obj);
//		if(o!=null) {
//			return Results.parse(Constants.STATE_SUCCESS,null,o);
//		}else {
//			return Results.parse(Constants.STATE_FAIL);
//		}
//	}
//	@At("/update")
//	@POST
//	public Object update(@Param("..") LgLetterCategory obj) {
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
//	public Object queryPage(@Param("..") LgLetterCategory obj) {
//		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
//	}

	/**
	 * 查询书香分类
	 * @param obj
	 * @return
	 */
	@At("/query")
	@GET
	public Object query(@Param("..") LgLetterCategory obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}

//
//	@At("/checkId")
//	@GET
//	public Object checkId(String value) {
//		return service.checkId(value);
//	}
}