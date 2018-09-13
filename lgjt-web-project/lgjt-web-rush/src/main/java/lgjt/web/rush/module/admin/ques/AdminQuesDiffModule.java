package lgjt.web.rush.module.admin.ques;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.rush.ques.QuesDiff;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.services.rush.ques.QuesDiffService;

import java.util.Date;

/**
 * 题库模块（后台）- 试题难度
 * @Description: 
 * 			1、新增-试题难度
 * 			2、删除-试题难度
 * 			3、修改-试题难度
 * 			3、刷新-试题难度
 * 			4、查询-试题难度
 * @author gaolei
 * @date 2017-7-6 上午11:03:41
 */
@At("/admin/diff")
@IocBean
@Log4j
public class AdminQuesDiffModule {

	@Inject("quesDiffService")
	QuesDiffService service;
	
	/**
	 * 新增-试题难度
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/insert")
	@lgjt.common.base.Authority("RUSH")
	//@Aop({"sixFieldInterceptor"})
	public Object insert(@Param("..") QuesDiff obj) {
		if (StringTool.isNull(obj.getId())) {
			obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setCrtTime(new Date());
			obj.setCrtIp(ClientInfo.getIp());
			service.insert(obj);
		} else {
			obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setUpdTime(new Date());
			obj.setUpdIp(ClientInfo.getIp());
			service.updateIgnoreNull(obj);
		}
		return Results.parse(Constants.STATE_SUCCESS);
	}
	
	/**
	 * 删除-试题难度
	 * @Description: 
	 * @param:  @param id
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/delete")
	@lgjt.common.base.Authority("RUSH")
	public Object delete(@Param("ids") String id) {
		if (service.delete(id) > 0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}
		return Results.parse(Constants.STATE_FAIL);
	}
	
	/**
	 * 修改-试题难度
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/update")
	@lgjt.common.base.Authority("RUSH")
	//@Aop({"sixFieldInterceptor"})
	public Object update(@Param("..") QuesDiff obj) {
		if (StringTool.isNull(obj.getId())) {
			obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setCrtTime(new Date());
			obj.setCrtIp(ClientInfo.getIp());
			service.insert(obj);
		} else {
			obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setUpdTime(new Date());
			obj.setUpdIp(ClientInfo.getIp());
			service.updateIgnoreNull(obj);
		}
		return Results.parse(Constants.STATE_SUCCESS);
	}
	
	/**
	 * 刷新-试题难度
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/list")
	@lgjt.common.base.Authority("RUSH")
	public Object queryPage(@Param("..") QuesDiff obj) {
		return Results.parse(Constants.STATE_SUCCESS, null,
				service.queryPage(obj));
	}
	
	
	/**
	 * 查询考试难度-考试模块专用
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/list2")
	@lgjt.common.base.Authority("RUSH")
	public Object queryPageDiff(@Param("..") QuesDiff obj) {
		return Results.parse(Constants.STATE_SUCCESS, null,service.queryPage2(obj));
	}
	
	/**
	 * 查询-试题难度
	 *    试题管理中 编辑试题  那用到它！！！！
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/query")
	@lgjt.common.base.Authority("RUSH")
	public Object query(@Param("..") QuesDiff obj) {
		return Results.parse(Constants.STATE_SUCCESS, null,service.query(obj));
	}
}