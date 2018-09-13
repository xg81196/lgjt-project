package lgjt.services.rush.ques;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.rush.ques.QuesDiff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@IocBean
public class QuesDiffService extends BaseService {

	/**
	 * 返回map集合，key为难度名字，val为难度id
	 * @return
	 * @return_type Map<String,String>
	 * @author Chen.ruiwen
	 * @date 2016-4-7
	 */
	public Map<String, String> queryDiffNameAndId() {
		String str = "SELECT name , id FROM ques_diff ";
		Sql sql = Sqls.create(str);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				while(rs.next()) {
					map.put(rs.getString(1), rs.getString(2));
				}
				return map;
			}
		});
		dao.execute(sql);
		return (Map<String, String>) sql.getResult();
	}
	
	/**
	 * 刷新-试题难度
	 *   分页显示
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  PageResult<QuesDiff>
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	public PageResult<QuesDiff> queryPage(QuesDiff obj) {
		return super.queryPage(QuesDiff.class, obj, getCri(obj));
	}
	
	/**
	 * 查询-试题难度
	 *    试题管理中的 修改试题 中用到他
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  List<QuesDiff>
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	public List<QuesDiff> query(QuesDiff obj) {
		return super.query(QuesDiff.class, getCri(obj));
	}
	
	/**
	 * 
	 * @Description: 
	 * @param:  @param id
	 * @param:  @return   
	 * @return:  QuesDiff
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-9
	 */
   	public QuesDiff get(String id) {
		return super.fetch(QuesDiff.class, id);
	}
   	/**
   	 * 删除-试题难度
   	 * @Description: 
   	 * @param:  @param ids
   	 * @param:  @return   
   	 * @return:  int
   	 * @throws
   	 * @author  gaolei 
   	 * @date  2017-7-6
   	 */
	public int delete(String ids) {
		if(StringTool.isNotEmpty(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(QuesDiff.class, cri);
		}
		return 0;
	}

	public QuesDiff checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(QuesDiff.class,cri);
	}

	private SimpleCriteria getCri(QuesDiff obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getId())) {
			String[] idArr = obj.getId().split(",");
			cri.where().andIn("id", idArr);
		}
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andLike("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getState())) {
			cri.where().andEquals("state", obj.getState());
		}
		if(StringTool.isNotNull(obj.getMark())) {
			cri.where().andEquals("mark", obj.getMark());
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
		cri.asc("score");
		return cri;
	}

	/**
	 * 查询试题难度-考试模块-试卷定义里用
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-8-1  
	 */
	public PageResult<QuesDiff> queryPage2(QuesDiff obj) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("state", QuesDiff.STATE_YES);
		cri.desc("crt_time");
		return super.queryPage(QuesDiff.class, obj, cri);
	}
	
}