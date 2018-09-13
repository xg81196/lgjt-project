package lgjt.web.rush.module.front;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.rush.level_score.RushLevelScore;
import lgjt.services.rush.level_score.RushLevelScoreService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;

import java.util.List;

@At("/front/rushLevelScore")
@Log4j
@IocBean
public class FrontRushLevelScoreModule {

    @Inject
    RushLevelScoreService rushLevelScoreService;

    @At("/getMyRushLevelScore")
    public Object getMyRushLevelScore(@Param("..")RushLevelScore obj) {
        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login")+token);
        obj.setUserName(userLoginInfo.getUserName());
        PageResult<RushLevelScore> rushLevelScores = rushLevelScoreService.queryPage(obj);
        if(rushLevelScores.getTotal() > 0) {
            Integer count = rushLevelScoreService.sumScore(userLoginInfo.getUserName());
            rushLevelScores.getRows().get(0).setExtend5(count);
        }
        return Results.parse(Constants.STATE_SUCCESS, null, rushLevelScores);
    }
}
