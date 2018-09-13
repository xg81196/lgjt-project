package lgjt.services.help;

import lgjt.common.base.CustomService;
import lgjt.domain.help.ERPSaveFileName;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Create By xigexb At 2018/9/11
 */
@Log4j
@IocBean(fields = {"dao:daoHelp"})
public class ERPSaveFileNameService extends CustomService {

    public ERPSaveFileName insertMssql(ERPSaveFileName obj) {
        return this.getDao().insert(obj);
    }
}
