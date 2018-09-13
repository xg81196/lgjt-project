package lgjt.services.app.dict;

import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.util.cache.util.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.app.dict.SysDict;

import java.util.*;

@IocBean
public class SysDictService extends BaseService {

	/**
	 * show 通过父级id查询词典. 
	 * @param parentId 词典父级id
	 * @return 系统词典信息集
	 */
	public List<SysDict> queryDictsByParentId(String parentId){
		if(StringUtil.trim(parentId).length()==0){
			return new  ArrayList<SysDict>();
		}
		Criteria cri = Cnd.cri();
		cri.where().andEquals("PARENT_ID", parentId);
		return super.query(SysDict.class,cri);
	}
}
