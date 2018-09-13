package lgjt.web.rush.module.front;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.CommonUtil;
import lgjt.domain.rush.level_score.opinion.RushOpinion;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.services.rush.opinion.RushOpinionService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

@At("/front/sysOpinion")
@IocBean
@Log4j
public class RushOpinionModule {

    
	@Inject
	RushOpinionService service;

	@At("/get")
	@GET
	public Object get(String id) {
		RushOpinion obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/delete")
	@DELETE
	public Object delete(String ids) {
		return service.delete(ids);
	}
	
	/**
	 * 提交我的反馈
	 * @param obj
	 * @return
	 * @return_type Object
	 */
	@At("/insert")
	@POST
	public Object insert(@Param("..") RushOpinion obj) {
		if (null == obj) {
			return Results.parse(Constants.STATE_FAIL);
		}
		if (null != obj.getContent() && obj.getContent().length() > CommonUtil.CONTENT_LENGTH) {
				return Results.parse(Constants.STATE_FAIL, "反馈内容过长");
		}
		obj.setUserName(LoginUtil.getUserLoginInfo().getUserName());
		if(obj.getType()==null){
			obj.setType(0);
		}
		obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
		obj.setCrtIp(ClientInfo.getIp());
		obj.setCrtTime(new Date());
		RushOpinion o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	
	/**
	 * 获得全部反馈的分页
	 * @param obj
	 * @return
	 * @return_type Object
	 */
	@At("/queryPage")
	@GET
	public Object queryPage(@Param("..") RushOpinion obj) {
		try {
			obj.setContent(URLDecoder.decode((URLDecoder.decode(obj.getContent(),"UTF-8")),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}
	
	/**
	 * 获取我的反馈的分页
	 * @param obj
	 * @return
	 * @return_type Object
	 */
	@At("/getMyFeedback")
	@GET
	public Object getMyFeedback(@Param("..") RushOpinion obj) {
		if (null == obj) {
			obj = new RushOpinion();
		}
		try {
			obj.setContent(URLDecoder.decode((URLDecoder.decode(obj.getContent(),"UTF-8")),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		obj.setUserName(LoginUtil.getUserLoginInfo().getUserName());
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

}