package lgjt.web.backend.module.admin.app;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.domain.backend.app.AppVersion;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.app.SysAppVersionService;

import java.util.Date;

/***********************
 *Author:王昕禹
 ***********************/
@At("/admin/learnAppVersion")
@IocBean
@Log4j
public class AdminSysAppVersionModule {
	
	@Inject
	SysAppVersionService service;

	/**
	 * 增加APP安装包
	 */
	@At("/insertApp")
	public Object insertApp(@Param("..") AppVersion obj) {

		try {
			obj.setCrtUser(UserUtil.getAdminUser().getUserName());
			obj.setCrtTime(new Date());
			AppVersion av = null;
			if("0".equals(obj.getAdviceType())){
				//安卓
				if(StringTool.isNull(obj.getName()) || obj.getName().length()<=3){
					return Results.parse(Constants.STATE_FAIL,"请上传app安装包！");	
				}
				String name = obj.getName();
				if("apk".equals(name.substring(name.length()-3, name.length()))){
					obj.setAdviceType("0");
					av = service.insert(obj);	
				}else{
					return Results.parse(Constants.STATE_FAIL,"请核对您选择的平台类型！");	
				}
			}else if("1".equals(obj.getAdviceType())){
				obj.setName("IOS安装包");
					av = service.insert(obj);	
			}
			if(null!=av) {
				return Results.parse(Constants.STATE_SUCCESS,"添加成功",av);
			}else {
				return Results.parse(Constants.STATE_FAIL,"添加失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,"添加失败");
		}
	}
	
	/**
	 * 删除安装包
	 */
	@At("/deleteApp")
	public Object deleteApp(String ids) {
		try {
			int i = service.delete(ids);
			if(i>0) {
				return Results.parse(Constants.STATE_SUCCESS,"删除成功");
			}else {
				return Results.parse(Constants.STATE_SUCCESS,"删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_SUCCESS,"删除成功");
		}
	}
	
	/**
	 * 修改APP
	 */
	@At("/updateApp")
	public Object updateApp(@Param("..") AppVersion obj) {
		try {
			if (null == obj.getAppAttachId() || "".equals(obj.getAppAttachId())) {
				return Results.parse(Constants.STATE_FAIL,"请上传最新APK文件");
			}
			obj.setUpdTime(new Date());
			obj.setCrtUser(UserUtil.getAdminUser().getUserName());
			int i = service.updateIgnoreNull(obj);
			if(i>0) {
				return Results.parse(Constants.STATE_SUCCESS,"修改成功");
			}else {
				return Results.parse(Constants.STATE_FAIL,"修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,"修改失败");
		}
	}
	
	/**
	 * 查询APP
	 */
	@At("/queryApp")
	@Authority("")
	public Object queryApp(@Param("..") AppVersion obj) {
		SysUserAdmin user= UserUtil.getAdminUser();
		if(user!=null&&"admin"!=SysUserAdmin.ADMIN){
			return Results.parse(Constants.STATE_FAIL,"您无权限查看！");
		}
		 PageResult<AppVersion> av = service.queryPage(obj);
		 return Results.parse(Constants.STATE_SUCCESS,"查询成功",av);
	}
}
