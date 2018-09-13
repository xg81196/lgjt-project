package lgjt.services.app.group;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.app.group.LgGroup;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;


@Log4j
@IocBean
public class LgGroupService extends BaseService {

	private static final String QUEY_BY_USERID = "group.query.userid";

	public PageResult<LgGroup> queryPage(LgGroup obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getGroupName())) {
			cri.where().andEquals("group_name", obj.getGroupName());
		}
		if(StringTool.isNotNull(obj.getGroupDesc())) {
			cri.where().andEquals("group_desc", obj.getGroupDesc());
		}
		if(StringTool.isNotNull(obj.getGroupVision())) {
			cri.where().andEquals("group_vision", obj.getGroupVision());
		}
		if(StringTool.isNotNull(obj.getGroupFeature())) {
			cri.where().andEquals("group_feature", obj.getGroupFeature());
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

		return super.queryPage(LgGroup.class, obj, cri);
	}

	public List<LgGroup> query(LgGroup obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getGroupName())) {
			cri.where().andEquals("group_name", obj.getGroupName());
		}
		if(StringTool.isNotNull(obj.getGroupDesc())) {
			cri.where().andEquals("group_desc", obj.getGroupDesc());
		}
		if(StringTool.isNotNull(obj.getGroupVision())) {
			cri.where().andEquals("group_vision", obj.getGroupVision());
		}
		if(StringTool.isNotNull(obj.getGroupFeature())) {
			cri.where().andEquals("group_feature", obj.getGroupFeature());
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
		return super.query(LgGroup.class, cri);
	}

   	public LgGroup get(String id) {
		return super.fetch(LgGroup.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgGroup.class, cri);
		}
		return 0;
	}

	public LgGroup checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgGroup.class,cri);
	}

	public List<LgGroup> queryByUserId(String userId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("lsu.id",  userId);
		return super.query(QUEY_BY_USERID, LgGroup.class, cri);
	}

}