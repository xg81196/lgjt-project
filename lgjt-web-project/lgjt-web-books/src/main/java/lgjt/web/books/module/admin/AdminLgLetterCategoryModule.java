package lgjt.web.books.module.admin;

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
@At("/admin/LgLetterCategory")
@IocBean
@Log4j
public class AdminLgLetterCategoryModule {

    
	@Inject
	LgLetterCategoryService service;

	/**
	 * 查询书香分类
	 * @param obj
	 * @return
	 */
	@At("/query")
	public Object query(@Param("..") LgLetterCategory obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}
}