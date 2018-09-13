package lgjt.services.backend.org;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.backend.org.KsOrg;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoBackend"})
public class KsOrgService extends BaseService {

    public List<KsOrg> getKsOrgList() {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andNotEquals("IsClass", "1");
        return super.query(KsOrg.class, cri);
    }

}
