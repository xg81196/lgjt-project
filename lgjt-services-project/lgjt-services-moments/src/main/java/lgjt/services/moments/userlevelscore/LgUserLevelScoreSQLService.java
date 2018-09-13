package lgjt.services.moments.userlevelscore;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.domain.moments.userlevelscore.LgUserLevelScoreVo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.*;

@Log4j
@IocBean(fields = {"dao:daoMoments"})
public class LgUserLevelScoreSQLService extends BaseService {

    @Inject
    LgUserLevelScoreService lgUserLevelScoreService;

    private static final String SCORE_SUM = "get.score.sum.sql";

    private static final String SCORE_SUM_MYSQL = "get.score.sum.mysql";

    public List<Map<String, Object>> getMessageScoreRank(LgUserLevelScore obj) {
        SimpleCriteria cri = Cnd.cri();
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", "2018-01-01");
        try {
            String nowDate = SimpleDateFormatUtils.getStringDate(new Date(), "yyyy-MM-dd");
            params.put("endTime", nowDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        List<LgUserLevelScoreVo> sqlServer = super.query(SCORE_SUM, LgUserLevelScoreVo.class, cri, params);
        //继续查询自己的库表
        List<LgUserLevelScoreVo> mySqlServer = lgUserLevelScoreService.query(SCORE_SUM_MYSQL, LgUserLevelScoreVo.class, cri);
        //返回值
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        //循环遍历
        int rank = 1;
        for (LgUserLevelScoreVo sql : sqlServer) {
            Map<String, Object> value = new TreeMap<>();
            value.put("orgName", sql.getOrgName());
            value.put("groupName", sql.getGroupName());
            value.put("rank", rank);
            long score = sql.getScore();
            for (LgUserLevelScoreVo mysql : mySqlServer) {

                if (StringUtils.isNotEmpty(sql.getOrgId()) && StringUtils.isNotEmpty(mysql.getOrgId())) {

                    if (StringUtils.isNotEmpty(sql.getGroupName()) && StringUtils.isNotEmpty(mysql.getGroupName())) {
                        //实际上查出来的orgId就是班组id
                        if (sql.getOrgId().equals(mysql.getGroupId())) {
                            //他们是一样的
                            score += mysql.getScore();
                        }
                    }
                }
            }
            value.put("score", score);
            result.add(value);
            rank++;
        }
        return result;
    }
}
