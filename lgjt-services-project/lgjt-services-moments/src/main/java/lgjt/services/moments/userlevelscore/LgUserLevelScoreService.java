package lgjt.services.moments.userlevelscore;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.moments.userlevelscore.LgUserLevelScore;
import lgjt.domain.moments.userlevelscore.LgUserLevelScoreVo;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@IocBean
public class LgUserLevelScoreService extends BaseService {

    private static String RANK_GROUP = "app.rank.group";

	public PageResult<LgUserLevelScore> queryPage(LgUserLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getLevelId())) {
			cri.where().andEquals("level_id", obj.getLevelId());
		}
		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getMark())) {
			cri.where().andEquals("mark", obj.getMark());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}

		return super.queryPage(LgUserLevelScore.class, obj, cri);
	}

	public List<LgUserLevelScore> query(LgUserLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getLevelId())) {
			cri.where().andEquals("level_id", obj.getLevelId());
		}
		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getMark())) {
			cri.where().andEquals("mark", obj.getMark());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}
		return super.query(LgUserLevelScore.class, cri);
	}

   	public LgUserLevelScore get(String id) {
		return super.fetch(LgUserLevelScore.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgUserLevelScore.class, cri);
		}
		return 0;
	}

	public LgUserLevelScore checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgUserLevelScore.class,cri);
	}

	public List<LgUserLevelScoreVo> getGroupRank(LgUserLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();
        Map<String,Object> param = new HashMap<>();
        param.put("page", (obj.getPage() - 1)*obj.getPageSize());
        param.put("pageSize", obj.getPageSize() * obj.getPage());
        return super.query(RANK_GROUP, LgUserLevelScoreVo.class, cri, param);
	}


}