package lgjt.services.backend.dict;

import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.backend.dict.SysDict;

import java.util.*;

@IocBean
public class SysDictService extends BaseService {

	public List<SysDict> queryManageDic(SysDict dic) {
		Criteria cri = Cnd.cri();
		cri.where().andEquals("PARENT_ID", -1);
		SysDict fixTopDic = this.getFixedTopDic();

		List<SysDict> topDics = super.dao.query(SysDict.class, cri);
		for (SysDict topDic : topDics) {
			topDic.setList(this.queryChildDics(topDic.getId()));
		}

		fixTopDic.setList(topDics);

		List<SysDict> result = new ArrayList<SysDict>();

		result.add(fixTopDic);

		return result;
	}

	private SysDict getFixedTopDic() {
		SysDict manageDictionary = new SysDict();
		manageDictionary.setId("-1");
		manageDictionary.setCodeName("字典树");
		manageDictionary.setCodeValue("defaultDicValueUnchangeable");
		manageDictionary.setDictCode("defaultDicCodeUnchangeable");
		return manageDictionary;
	}

	private List<SysDict> queryChildDics(String parentId) {
		Criteria cri = Cnd.cri();
		cri.where().andEquals("parentId", parentId);
		List<SysDict> children = super.dao.query(SysDict.class, cri);
		for (SysDict child : children) {
			child.setList(this.queryChildDics(child.getId()));
		}
		return children;
	}

	public void saveOrUpdateManageDic(SysDict dic) throws Exception {
		if (StringTool.isNull(dic.getId()) || dic.getId().equals("null")) {
			this.saveManageDic(dic);
		} else {
			this.updateManageDic(dic);
		}
	}
	
	public List<SysDict> getDictBycode(String code) {
		Criteria cri = Cnd.cri();
		//cri.where().andEquals("code", code);
		cri.where().andEquals("parent_id", code);
		cri.getOrderBy().desc("value");
		return  super.query(SysDict.class, cri);
		/*if(null != dict) {
			cri = Cnd.cri();
			cri.where().andEquals("parent_id", dict.getId());
			List<SysDict> children = super.query(SysDict.class, cri);
			Collections.sort(children,new Comparator<SysDict>() {
				@Override
				public int compare(SysDict o1, SysDict o2) {
					return o1.getCodeValue().compareTo(o2.getCodeValue());
				}

			});
			return children;
		}
		return null;*/
	}

	public void saveManageDic(SysDict dic) throws Exception {
		String dictCode = dic.getDictCode();
		if (!StringTool.isEmpty(dictCode)) {
			Criteria cri = Cnd.cri();
			cri.where().andEquals("dictCode", dictCode);
			Integer count = super.dao.count(SysDict.class, cri);
			if (count > 0) {
				throw new RuntimeException("the dictCode : [ " + dictCode
						+ " ] already existed..");
			}
		}
		dic.setId(dic.getXid());
		dic.setCrtUserid("admin");
		dic.setCrtTime(new Date());
		super.insert(dic);
	}

	public void updateManageDic(SysDict dic) throws Exception {
		String dictCode = dic.getDictCode();
		if (!StringTool.isEmpty(dictCode)) {
			Criteria cri = Cnd.cri();
			cri.where().andEquals("dictCode", dictCode);
			Integer count = super.dao.count(SysDict.class, cri);
			if (count > 1) {
				throw new RuntimeException("the dictCode : [ " + dictCode
						+ " ] already existed..");
			}
//			super.updateIgnoreNull(obj)
			super.updateIgoneField(SysDict.class, dic, "(code|name|value|remark)",
					null, false, dao);
		}
	}

	public void deleteManageDic(String id) throws Exception{
		Criteria cri = Cnd.cri();
		cri.where().andEquals("parentId", id);
		SysDict dict = super.fetch(SysDict.class, cri);
		if(null!=dict)throw new RuntimeException("该分类下包含子类，无法删除！");
		super.delete(SysDict.class, id);
	}
	/**
	 * @method 根据字典代码得到字典名称
	 * @param code 字典代码
	 * @return 字典名称
	 * @author zhangyuan 2016-9-28
	 */
	public String getCodeName(String code) {
		Criteria cri = Cnd.cri();
		cri.where().andEquals("code", code);
		
		SysDict dict = super.fetch(SysDict.class, cri);
		if(null != dict) {
			if(dict.getCodeName()!=null){
				return dict.getCodeName();
			}
		}
		return null;
	}
	/**
	 * @method 根据code获取字典列表
	 * @param codes 字典代码
	 * @return 返回结果
	 * @author zhangyuan 2016-9-28
	 */
	public List<SysDict> getDicByCode(String codes) {
		String[] code = codes.split(",");
		Criteria cri = Cnd.cri();
		cri.where().andIn("code", code);
		cri.getOrderBy().asc("code");
		List<SysDict> list = super.query(SysDict.class, cri);
		return list;
	}
}
