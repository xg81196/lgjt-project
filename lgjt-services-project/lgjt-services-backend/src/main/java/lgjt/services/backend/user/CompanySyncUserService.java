package lgjt.services.backend.user;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.backend.user.CompanySyncUser;


@Log4j
@IocBean
public class CompanySyncUserService extends BaseService {


	public PageResult<CompanySyncUser> queryPage(CompanySyncUser obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRealnameFlag())) {
			cri.where().andEquals("realname_flag", obj.getRealnameFlag());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("id_no", obj.getIdNo());
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

		return super.queryPage(CompanySyncUser.class, obj, cri);
	}

	public List<CompanySyncUser> query(CompanySyncUser obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRealnameFlag())) {
			cri.where().andEquals("realname_flag", obj.getRealnameFlag());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("id_no", obj.getIdNo());
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
		return super.query(CompanySyncUser.class, cri);
	}

   	public CompanySyncUser get(String id) {
		return super.fetch(CompanySyncUser.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(CompanySyncUser.class, cri);
		}
		return 0;
	}

	public CompanySyncUser checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(CompanySyncUser.class,cri);
	}
	public CompanySyncUser checkRealname_flag(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("realname_flag",value);
		return super.fetch(CompanySyncUser.class,cri);
	}

}