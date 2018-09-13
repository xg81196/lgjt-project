package lgjt.services.moments.userlike;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.common.base.vo.Count;
import lgjt.domain.moments.usercomment.LgUserComment;
import lgjt.domain.moments.userlike.LgUserLike;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean
public class LgUserLikeService extends BaseService {


	public PageResult<LgUserLike> queryPage(LgUserLike obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
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

		return super.queryPage(LgUserLike.class, obj, cri);
	}

	public List<LgUserLike> query(LgUserLike obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getMessageId())) {
			cri.where().andEquals("message_id", obj.getMessageId());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
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
		return super.query(LgUserLike.class, cri);
	}

   	public LgUserLike get(String id) {
		return super.fetch(LgUserLike.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgUserLike.class, cri);
		}
		return 0;
	}

	public LgUserLike checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgUserLike.class,cri);
	}

	public Integer deleteByMessageId(String ids) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andIn("message_id", ids.split(","));
		return super.delete(LgUserLike.class, cri);
	}

	/**
	 * 查询用户是否点赞
	 * @param userId
	 * @return 0没有， 1点了
	 */
	public String likeMark(String userId, String messageId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_id",userId);
		cri.where().andEquals("message_id", messageId);
		List<LgUserLike> lgUserLikes = super.query(LgUserLike.class, cri);
		if(lgUserLikes.size() > 0) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 查询一条数据的点赞数量
	 * @param messageId
	 * @return
	 */
	public Integer likeCount(String messageId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("message_id", messageId);
		List<Count> counts = super.query("get.like.count", Count.class, cri);
		return counts.get(0).getCount();
	}


}