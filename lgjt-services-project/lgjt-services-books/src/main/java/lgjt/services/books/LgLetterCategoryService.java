package lgjt.services.books;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.books.LgLetterCategory;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;


@Log4j
@IocBean
public class LgLetterCategoryService extends BaseService {


	public PageResult<LgLetterCategory> queryPage(LgLetterCategory obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getCateCode())) {
			cri.where().andEquals("cate_code", obj.getCateCode());
		}
		if(StringTool.isNotNull(obj.getCateName())) {
			cri.where().andEquals("cate_name", obj.getCateName());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
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

		return super.queryPage(LgLetterCategory.class, obj, cri);
	}

	public List<LgLetterCategory> query(LgLetterCategory obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getCateCode())) {
			cri.where().andEquals("cate_code", obj.getCateCode());
		}
		if(StringTool.isNotNull(obj.getCateName())) {
			cri.where().andEquals("cate_name", obj.getCateName());
		}
		if(StringTool.isNotNull(obj.getSort())) {
			cri.where().andEquals("sort", obj.getSort());
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
		return super.query(LgLetterCategory.class, cri);
	}

   	public LgLetterCategory get(String id) {
		return super.fetch(LgLetterCategory.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgLetterCategory.class, cri);
		}
		return 0;
	}

	public LgLetterCategory checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgLetterCategory.class,cri);
	}

}