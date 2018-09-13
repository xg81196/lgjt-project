package lgjt.web.backend.module.admin.dict;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.Results;
import org.apache.log4j.Logger;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.utils.RedisKeys;
import lgjt.domain.backend.dict.SysDict;
import lgjt.services.backend.dict.SysDictService;
import lgjt.web.backend.init.SysDictRedis;


/**
 * @author wuguangwei
 */
@IocBean
@At("/admin/dict")
public class SysDictModule {

	private static final Logger logger = Logger.getLogger(SysDictModule.class);

	@Inject
	private SysDictService sysDictService;

	@At("/query")
	@Authority("")
	public Object queryManageDicExtern(@Param("..") SysDict dic) {
		try {
			SysDictRedis sysDictRedis = SysDictRedis.getInstance();

			return Results.parse(Constants.STATE_SUCCESS, null,
					sysDictService.queryManageDic(dic));
		} catch (Exception e) {
			logger.error("save manage dictionary error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/save")
	@Authority("")
	//@Authority("SYS_DICT")
	public Object saveManageDicExtern(@Param("..") SysDict dic) {
		try {
			sysDictService.saveManageDic(dic);
			return Results.parse(Constants.STATE_SUCCESS);
		} catch (RuntimeException e) {
			return Results.parse(Constants.STATE_FAIL, "字典代码重复！");
		} catch (Exception e) {
			logger.error("save manage dictionary error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/saveOrUpdateDict")
	@Authority("")
	//@Authority("SYS_DICT")
	public Object saveOrUpdateManageDicExtern(@Param("..") SysDict dic) {
		try {
			sysDictService.saveOrUpdateManageDic(dic);
			return Results.parse(Constants.STATE_SUCCESS);
		} catch (Exception e) {
			logger.error("save or update dictionary error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/update")
	@Authority("")
	//@Authority("SYS_DICT")
	public Object updateManageDicExtern(@Param("..") SysDict dic) {
		try {
			sysDictService.updateManageDic(dic);
			return Results.parse(Constants.STATE_SUCCESS);
		} catch (RuntimeException e) {
			return Results.parse(Constants.STATE_FAIL, "字典代码重复！");
		}catch (Exception e) {
			logger.error("update manage dictionary error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/delete")
	@Authority("")
	//@Authority("SYS_DICT")
	public Object deleteManageDicExtern(@Param("id") String id) {
		try {
			sysDictService.deleteManageDic(id);
			return Results.parse(Constants.STATE_SUCCESS);
		} catch (Exception e) {
			logger.error("delete manage dictionary error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}


	@At("/getDictByCode")
	@Authority("")
	//@Authority("SYS_DICT")
	public Object getDictBycode(@Param("code") String code) {
		try {
			return Results.parse(Constants.STATE_SUCCESS,null,sysDictService.getDictBycode(code));
		} catch (Exception e) {
			return Results.parse(Constants.STATE_FAIL);
		}
	}


}
