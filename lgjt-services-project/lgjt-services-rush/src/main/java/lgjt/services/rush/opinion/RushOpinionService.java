package lgjt.services.rush.opinion;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.rush.level_score.opinion.RushOpinion;
import lgjt.domain.rush.utils.LoginUtil;

import java.util.Date;
import java.util.List;

@Log4j
@IocBean
public class RushOpinionService extends BaseService {

	public PageResult<RushOpinion> queryPage(RushOpinion obj) {
		SimpleCriteria cri = getCri(obj);
		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andLike("content", obj.getContent());
		}

		cri.getOrderBy().desc("crt_time");
		return super.queryPage(RushOpinion.class, obj, cri);
	}
	
	public List<RushOpinion> query(RushOpinion obj) {
		return super.query(RushOpinion.class, getCri(obj));
	}
	
	public boolean reply(RushOpinion obj) {
		obj.setReplyTime(new Date());
		obj.setReplyUserName(LoginUtil.getUserLoginInfo().getUserName());
		int result = super.updateIgnoreNull(obj);
		if(result >0 ) {
			return true;
		}
		return false;
	}

   	public RushOpinion get(String id) {
		return super.fetch(RushOpinion.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushOpinion.class, cri);
		}
		return 0;
	}


	private SimpleCriteria getCri(RushOpinion obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getAnonymous())) {
			cri.where().andEquals("anonymous", obj.getAnonymous());
		}
//		if(StringTool.isNotNull(obj.getContent())) {
//			cri.where().andEquals("content", obj.getContent());
//		}
		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andLike("content", obj.getContent());
		}
		if(StringTool.isNotNull(obj.getReply())) {
			cri.where().andEquals("reply", obj.getReply());
		}
		if(StringTool.isNotNull(obj.getReplyUserName())) {
			cri.where().andEquals("reply_user_name", obj.getReplyUserName());
		}
		if(StringTool.isNotNull(obj.getReplyTime())) {
			cri.where().andEquals("reply_time", obj.getReplyTime());
		}
		if(StringTool.isNotNull(obj.getType())) {
			cri.where().andEquals("type", obj.getType());
		}
		if(StringTool.isNotNull(obj.getForeignId())) {
			cri.where().andEquals("foreign_id", obj.getForeignId());
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
		return cri;
	}

}