package lgjt.web.moments.module.userlevelscore;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.services.moments.userlevelscore.LgUserLevelScoreSQLService;
import lgjt.services.moments.userlevelscore.LgUserLevelScoreService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import java.util.List;

/**
 * @author zhaotianyi
 * @depre 用户/班圈积分相关
 */
@At("/lgUserLevelScore")
@IocBean
@Log4j
public class LgUserLevelScoreModule {

    
	@Inject("lgUserLevelScoreService")
	LgUserLevelScoreService service;

	@Inject
	LgUserLevelScoreSQLService lgUserLevelScoreSQLService;

	/**
	 * 查询班圈积分排行
	 * @param obj
	 * @return
	 */
	@Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
	@At("/getGroupRank")
    public Object getGroupRank(@Param("..") LgUserLevelScore obj) {
        return Results.parse(Constants.STATE_SUCCESS,null,lgUserLevelScoreSQLService.getMessageScoreRank(obj));
    }


}