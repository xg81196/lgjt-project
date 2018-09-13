package lgjt.services.task.involvementrank;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.task.involvementrank.RushLevelRank;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoRush"})
public class RushService extends BaseService {

    public void getRushHumanCount() {
        List<RushLevelRank> rushLevelRanks = queryRushByCompanyName();

    }

    public List<RushLevelRank> queryRushByCompanyName() {
        SimpleCriteria cri=Cnd.cri();
        return super.query("rush.rush.getRankByCompanyName", RushLevelRank.class, cri);
    }
    
}
