package lgjt.services.moments.config;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.moments.config.SysConfig;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;


@Log4j
@IocBean
public class SysConfigService extends BaseService {

	public String checkReplaceText() {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("config_name", "REPLACE_TEXT");
		SysConfig sc = super.fetch(SysConfig.class,cri);
		return sc.getConfigValue();
	}
}