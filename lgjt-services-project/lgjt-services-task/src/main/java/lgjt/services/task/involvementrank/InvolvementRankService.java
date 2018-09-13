package lgjt.services.task.involvementrank;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.task.involvementrank.InvolvementRank;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

@Log4j
@IocBean(fields = {"dao:daoRush"})
public class InvolvementRankService extends BaseService {

    public void deleteAll() {
        SimpleCriteria cri=Cnd.cri();
        super.delete(InvolvementRank.class, cri);
    }
}
