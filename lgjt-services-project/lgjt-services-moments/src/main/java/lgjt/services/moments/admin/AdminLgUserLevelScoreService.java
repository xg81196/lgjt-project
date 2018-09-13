package lgjt.services.moments.admin;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.moments.userlevelscore.AdminGroupMessage;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.domain.moments.userlevelscore.LgUserLevelScoreVo;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@IocBean
public class AdminLgUserLevelScoreService extends BaseService {

	private static final String QUERY_GM_ADMIN = "admin.message.record";

	public PageResult<AdminGroupMessage> getMessageScoreLog(String groupName, String realName, String accountType, String objectId) {
		SimpleCriteria cri = Cnd.cri();
		//加上查询条件
        if(StringUtils.isNotEmpty(groupName)) {
            cri.where().andEquals("lgm.group_name", groupName);
        }
        if(StringUtils.isNotEmpty(realName)) {
            cri.where().andEquals("lgm.real_name", realName);
        }
		if(accountType.equals("2")) {
			//企业
			cri.where().andEquals("luls.org_id", objectId);
		}
		if(accountType.equals("3")) {
			//班组长
			cri.where().andEquals("luls.group_id", objectId);
		}
		//只查询通过的
		cri.where().andEquals("lgm.verify_status", "1");
		return super.queryPage(QUERY_GM_ADMIN, AdminGroupMessage.class, new AdminGroupMessage(), cri);
	}
}