package lgjt.web.app.module.group;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.app.group.LgGroup;
import lgjt.services.app.group.LgGroupService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;

import java.util.List;

/**
 * @author zhaotianyi
 * @depre 班组相关
 */
@At("/lgGroup")
@IocBean
@Log4j
public class LgGroupModule {

    
	@Inject("lgGroupService")
	LgGroupService service;

    /**
     * 通过班组ID查
     * @param id
     * @return
     */
	@At("/queryById")
	@GET
	public Object get(String id) {
		LgGroup obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
    /**
     * 通过用户查询班组信息
     * @param userId
     * @return
     */
	@At("/queryByUserId")
	@GET
	public Object queryByUserId(@Param("userId") String userId) {
		if(StringUtils.isEmpty(userId)) {
			userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");
		}

		List<LgGroup> groups = service.queryByUserId(userId);
		if (groups.size() > 0) {
			return Results.parse(Constants.STATE_SUCCESS,null,groups.get(0));
		}
		return Results.parse(Constants.STATE_SUCCESS,null, null);
	}

//
//	@At("/checkId")
//	@GET
//	public Object checkId(String value) {
//		return service.checkId(value);
//	}
}