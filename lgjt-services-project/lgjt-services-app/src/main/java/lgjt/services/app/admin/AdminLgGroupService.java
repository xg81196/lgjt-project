package lgjt.services.app.admin;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.app.group.KsGroup;
import lgjt.domain.app.group.LgGroup;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.List;

@Log4j
@IocBean
public class AdminLgGroupService extends BaseService {

    @Inject
    AdminKsGroupSQLService adminKsGroupSQLService;

    public Integer getLgGroupBySQLServer() {
        SimpleCriteria cri = Cnd.cri();
        //先删除
        super.delete(LgGroup.class, cri);

        cri.where().andEquals("IsClass", "1");
        List<KsGroup> ksGroups = adminKsGroupSQLService.query(KsGroup.class, cri);
        int count = 0;
        for(KsGroup ksGroup: ksGroups) {
            LgGroup lgGroup = new LgGroup();
            lgGroup.setValueByKsGroup(ksGroup);
            super.insert(lgGroup);
            count++;
        }
        return count;
    }
}
