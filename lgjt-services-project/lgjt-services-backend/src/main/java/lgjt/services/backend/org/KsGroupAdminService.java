package lgjt.services.backend.org;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.backend.user.KsGroupAdmin;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoBackend"})
public class KsGroupAdminService extends BaseService {

    private static final String QUERY_GROUP_ADMIN = "get.group.admin.sql";

    public List<KsGroupAdmin> getKsGroupAdminList() {
        SimpleCriteria cri = Cnd.cri();
        return super.query(QUERY_GROUP_ADMIN, KsGroupAdmin.class, cri);
    }
}
