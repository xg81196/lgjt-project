package lgjt.web.backend.module.admin.user;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.domain.backend.user.SysUserAuth;
import lgjt.services.backend.user.SysUserAuthService;
import lgjt.domain.backend.utils.UserUtil;


/**
 * 用户认证管理
 * @author wuguangwei
 */
@At("/admin/userauth")
@IocBean
@Log4j
public class SysUserAuthModule {

    
	@Inject("sysUserAuthService")
	SysUserAuthService service;



	
	/**
	 * 根据ID批量删除用户
	 * @param ids
	 * @return 删除条数
	 */
	@At("/delete")
	public Object delete(String ids) {
		int count = service.delete(ids);
		if(count>0){
			return Results.parse(Constants.STATE_SUCCESS,"删除成功");
		}else{
			return Results.parse(Constants.STATE_FAIL,"删除失败");
		}
	
	}

	
	
	@At("/update")
	public Object update(@Param("..") SysUserAuth obj) {
		int upd = service.updateAuditStatusAndSyncUser(obj,UserUtil.getAdminUser().getUserName());
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/queryPage")
	public Object queryPage(@Param("..") SysUserAuth obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,
				service.queryPageAuthUser(obj,UserUtil.getAdminUser(),null));
	}

	@At("/queryUser")
	public Object queryUser(@Param("..") SysUserAuth obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,
				service.checkId(obj.getId()));
	}


}