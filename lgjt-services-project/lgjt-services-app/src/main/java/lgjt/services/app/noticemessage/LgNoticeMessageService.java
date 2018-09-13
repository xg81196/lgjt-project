package lgjt.services.app.noticemessage;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.app.noticemessage.LgNoticeMessage;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;


@Log4j
@IocBean(fields = {"dao:daoApp"})
public class LgNoticeMessageService extends BaseService {


	public PageResult<LgNoticeMessage> queryPage(LgNoticeMessage obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getTitle())) {
			cri.where().andEquals("title", obj.getTitle());
		}
		if(StringTool.isNotNull(obj.getMessageType())) {
			cri.where().andEquals("message_type", obj.getMessageType());
		}
		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andEquals("content", obj.getContent());
		}
		if(StringTool.isNotNull(obj.getEnclosure())) {
			cri.where().andEquals("enclosure", obj.getEnclosure());
		}
		if(StringTool.isNotNull(obj.getBeginTime())) {
			cri.where().andEquals("begin_time", obj.getBeginTime());
		}
		if(StringTool.isNotNull(obj.getEndTime())) {
			cri.where().andEquals("end_time", obj.getEndTime());
		}
		if(StringTool.isNotNull(obj.getReceiveId())) {
			cri.where().andEquals("receive_id", obj.getReceiveId());
		}
		if(StringTool.isNotNull(obj.getIsPublic())) {
			cri.where().andEquals("is_public", obj.getIsPublic());
		}
		if(StringTool.isNotNull(obj.getNoticeRang())) {
			cri.where().andEquals("notice_rang", obj.getNoticeRang());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getSendDate())) {
			cri.where().andEquals("send_date", obj.getSendDate());
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

		return super.queryPage(LgNoticeMessage.class, obj, cri);
	}

	public List<LgNoticeMessage> query(LgNoticeMessage obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getTitle())) {
			cri.where().andEquals("title", obj.getTitle());
		}
		if(StringTool.isNotNull(obj.getMessageType())) {
			cri.where().andEquals("message_type", obj.getMessageType());
		}
		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andEquals("content", obj.getContent());
		}
		if(StringTool.isNotNull(obj.getEnclosure())) {
			cri.where().andEquals("enclosure", obj.getEnclosure());
		}
		if(StringTool.isNotNull(obj.getBeginTime())) {
			cri.where().andEquals("begin_time", obj.getBeginTime());
		}
		if(StringTool.isNotNull(obj.getEndTime())) {
			cri.where().andEquals("end_time", obj.getEndTime());
		}
		if(StringTool.isNotNull(obj.getReceiveId())) {
			cri.where().andEquals("receive_id", obj.getReceiveId());
		}
		if(StringTool.isNotNull(obj.getIsPublic())) {
			cri.where().andEquals("is_public", obj.getIsPublic());
		}
		if(StringTool.isNotNull(obj.getNoticeRang())) {
			cri.where().andEquals("notice_rang", obj.getNoticeRang());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getSendDate())) {
			cri.where().andEquals("send_date", obj.getSendDate());
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
		return super.query(LgNoticeMessage.class, cri);
	}

   	public LgNoticeMessage get(String id) {
		return super.fetch(LgNoticeMessage.class, id);
	}


	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgNoticeMessage.class, cri);
		}
		return 0;
	}

	public LgNoticeMessage checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgNoticeMessage.class,cri);
	}

	/**
	 * 查询是否未读消息
	 * @param noticeType
	 * @return
	 */
	public PageResult<LgNoticeMessage> queryreadAndUnreadMessage(LgNoticeMessage obj,String userID,Integer noticeType) {
		SimpleCriteria cri = Cnd.cri();
		//查询全部
		if (noticeType == 2) {
			cri.where().andEquals("r.user_id",userID);
			return super.queryPage("lgjt.web.notice.queryAllNotice", LgNoticeMessage.class, obj, cri);
			//查询是否读取该消息
		} else{
			cri.where().andEquals("r.status", noticeType);
			cri.where().andEquals("r.user_id", userID);
			return super.queryPage("lgjt.web.notice.queryNoticeForType", LgNoticeMessage.class, obj, cri);
		}
	}
}