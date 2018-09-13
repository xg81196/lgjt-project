package lgjt.services.app.wxchat;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;

import lgjt.common.base.CustomService;
import lgjt.domain.app.webchat.WxChat;


@Log4j
@IocBean
public class WxChatService extends CustomService {


	public PageResult<WxChat> queryPage(WxChat obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getToken())) {
			cri.where().andEquals("token", obj.getToken());
		}
		if(StringTool.isNotNull(obj.getAppId())) {
			cri.where().andEquals("app_id", obj.getAppId());
		}
		if(StringTool.isNotNull(obj.getAppSecre())) {
			cri.where().andEquals("app_secre", obj.getAppSecre());
		}
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
		}
		if(StringTool.isNotNull(obj.getRemark())) {
			cri.where().andEquals("remark", obj.getRemark());
		}
		if(StringTool.isNotNull(obj.getType())) {
			cri.where().andEquals("type", obj.getType());
		}
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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

		return super.queryPage(WxChat.class, obj, cri);
	}

	public List<WxChat> query(WxChat obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getToken())) {
			cri.where().andEquals("token", obj.getToken());
		}
		if(StringTool.isNotNull(obj.getAppId())) {
			cri.where().andEquals("app_id", obj.getAppId());
		}
		if(StringTool.isNotNull(obj.getAppSecre())) {
			cri.where().andEquals("app_secre", obj.getAppSecre());
		}
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
		}
		if(StringTool.isNotNull(obj.getRemark())) {
			cri.where().andEquals("remark", obj.getRemark());
		}
		if(StringTool.isNotNull(obj.getType())) {
			cri.where().andEquals("type", obj.getType());
		}
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		return super.query(WxChat.class, cri);
	}

   	public WxChat get(String id) {
		return super.fetch(WxChat.class, id);
	}

	/**
	 * 根据APPID返回密码
	 * @param appId
	 * @return
	 */
	public WxChat getByAppId(String appId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("app_id",appId);
		return super.fetch(WxChat.class, cri);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(WxChat.class, cri);
		}
		return 0;
	}

	public WxChat checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(WxChat.class,cri);
	}

}