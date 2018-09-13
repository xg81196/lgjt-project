package lgjt.services.rush.involvement;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.rush.level_score.InvolvementRank;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

@Log4j
@IocBean
public class InvolvementRankService extends BaseService {

    public PageResult<InvolvementRank> queryPage(InvolvementRank obj) {
        SimpleCriteria cri=Cnd.cri();
        cri.desc("involvement");
        return super.queryPage(InvolvementRank.class, obj, cri);
    }
}
