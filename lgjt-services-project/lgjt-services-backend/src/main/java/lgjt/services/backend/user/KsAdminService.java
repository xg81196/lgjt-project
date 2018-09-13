package lgjt.services.backend.user;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.backend.user.vo.KsAdmin;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoBackend"})
public class KsAdminService extends BaseService {

    private static String KS_ADMIN_QUERY = "get.ksadmin.sql";

    public List<KsAdmin> getKsAdminFromSQL() {
        SimpleCriteria cri = Cnd.cri();
        return super.query(KS_ADMIN_QUERY, KsAdmin.class, cri);
    }
}
