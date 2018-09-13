package lgjt.services.help;

import com.ttsx.platform.nutz.service.BaseService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.IocBean;


@Log4j
@IocBean(fields = {"dao:daoHelpMysql"})
public class LgHelpInfoServiceMysql extends BaseService {

}