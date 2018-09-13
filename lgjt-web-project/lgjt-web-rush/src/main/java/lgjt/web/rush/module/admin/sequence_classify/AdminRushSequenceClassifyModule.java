package lgjt.web.rush.module.admin.sequence_classify;

import com.ttsx.platform.nutz.mvc.annotation.Authority;
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
import lgjt.domain.rush.ques.QuesClassify;
import lgjt.domain.rush.sequence_classify.RushSequenceClassify;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.services.rush.sequence_classify.RushSequenceClassifyService;

import java.util.List;


@At("/admin/rushSequenceClassify")
@IocBean
@Log4j
public class AdminRushSequenceClassifyModule {

    
	@Inject("rushSequenceClassifyService")
	RushSequenceClassifyService service;

	@At("/?")
	@GET
	public Object get(String id) {
		RushSequenceClassify obj = service.get(id);
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
	public Object insert(@Param("..") RushSequenceClassify obj) {
		RushSequenceClassify o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") RushSequenceClassify obj) {
		int upd = service.updateIgnoreNull(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/")
	@POST
	public Object queryPage(@Param("..") RushSequenceClassify obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	@At("/query")
	@POST
	public Object query(@Param("..") RushSequenceClassify obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}

	/**
	 * 2018年07月25日 赵天意
	 * 更新或设置工种题库
	 * @param obj
	 * @return
	 */
	@At("/insertUpdateClassify")
	public Object insertUpdateClassify(@Param("..") RushSequenceClassify obj) {
		int upd = service.insertUpdateClassify(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}

	}

	@At("/checkId")
	@POST
	public Object checkId(String value) {
		return service.checkId(value);
	}


	/**
	 * 题库选择接口
	 * @return
	 */
	@At("/queryQuesClassify")
	@POST
	public Object queryQuesClassify(@Param("isCollect") Integer isCollect) {

		if(LoginUtil.getUserLoginInfo()==null){
			return Results.parse(Constants.STATE_FAIL,"操作失败！获取用户登录信息失败！");
		}

		List<QuesClassify> list = service.queryWithRole(isCollect);
		return Results.parse(Constants.STATE_SUCCESS, null,list);
	}
}