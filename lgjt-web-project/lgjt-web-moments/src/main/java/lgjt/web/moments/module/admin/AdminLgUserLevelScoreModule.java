package lgjt.web.moments.module.admin;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.common.base.utils.TokenUtils;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.services.moments.admin.AdminLgUserLevelScoreService;
import lgjt.services.moments.userlevelscore.LgUserLevelScoreService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import java.util.Properties;

/**
 * @author zhaotianyi
 * @depre 用户/班圈积分相关
 */
@At("/admin/lgUserLevelScore")
@IocBean
@Log4j
public class AdminLgUserLevelScoreModule {

    
	@Inject
	AdminLgUserLevelScoreService service;

	/**
	 * 查询积分
	 * @return
	 */
	@At("/getMessageScore")
    public Object getMessageScore(@Param("groupName") String groupName, @Param("realName") String realName) {
		/**
		 * admin的extend5字段存取角色type
		 * 0 平台管理员 2企业管理员 3班组长
		 */

		UserLoginInfo info = LoginUtil.getUserLoginInfo();
		String accountType = info.getInfos().get("extend5");
		String objectId = info.getInfos().get("orgId");
        return Results.parse(Constants.STATE_SUCCESS,null,service.getMessageScoreLog(groupName, realName, accountType, objectId));
    }


}