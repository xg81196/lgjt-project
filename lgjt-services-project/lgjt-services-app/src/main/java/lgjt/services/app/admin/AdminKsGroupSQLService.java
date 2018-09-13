package lgjt.services.app.admin;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.app.group.KsGroup;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoApp"})
public class AdminKsGroupSQLService extends BaseService {

    public List<KsGroup> getKsGroupList() {
        SimpleCriteria cri = Cnd.cri();
        return super.query(KsGroup.class, cri);
    }
}
