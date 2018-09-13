package lgjt.services.app.noticereceive;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.app.noticereceive.LgNoticeReceive;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean(fields = {"dao:daoApp"})
public class LgNoticeReceiveService extends BaseService {


	public PageResult<LgNoticeReceive> queryPage(LgNoticeReceive obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
		}
		if(StringTool.isNotNull(obj.getBeginTime())) {
			cri.where().andEquals("begin_time", obj.getBeginTime());
		}
		if(StringTool.isNotNull(obj.getEndTime())) {
			cri.where().andEquals("end_time", obj.getEndTime());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
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

		return super.queryPage(LgNoticeReceive.class, obj, cri);
	}

	public List<LgNoticeReceive> query(LgNoticeReceive obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
		}
		if(StringTool.isNotNull(obj.getBeginTime())) {
			cri.where().andEquals("begin_time", obj.getBeginTime());
		}
		if(StringTool.isNotNull(obj.getEndTime())) {
			cri.where().andEquals("end_time", obj.getEndTime());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
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
		return super.query(LgNoticeReceive.class, cri);
	}

   	public LgNoticeReceive get(String id) {
		return super.fetch(LgNoticeReceive.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgNoticeReceive.class, cri);
		}
		return 0;
	}

	public LgNoticeReceive checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgNoticeReceive.class,cri);
	}


	public LgNoticeReceive getForNoticeId(String noticeId,String userId){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("message_id",noticeId);
		cri.where().andEquals("user_id",userId);
		return super.fetch(LgNoticeReceive.class,cri);
	}

	public LgNoticeReceive getForNoticeId2(String noticeId){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("message_id",noticeId);
		return super.fetch(LgNoticeReceive.class,cri);
	}

}