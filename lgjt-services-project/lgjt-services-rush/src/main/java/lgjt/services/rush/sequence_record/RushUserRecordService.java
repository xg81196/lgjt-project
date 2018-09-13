package lgjt.services.rush.sequence_record;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.rush.sequence_record.RushUserRecord;
import lgjt.domain.rush.utils.LoginUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@IocBean
public class RushUserRecordService extends BaseService {

	private static final String GATE_USER_RECORD = "rush.gaterecord.list";

	public PageResult<RushUserRecord> queryPage(RushUserRecord obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSequenceName())) {//工种
			cri.where().andLike("rs.name", obj.getSequenceName());
		}

		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andLike("rgr.user_name", obj.getUserName());
		}


		cri.getOrderBy().desc("crt_time");
		return super.queryPage(GATE_USER_RECORD,RushUserRecord.class, obj, cri);
	}

	public List<RushUserRecord> query(RushUserRecord obj) {
		return super.query(RushUserRecord.class, getCri(obj));
	}

   	public RushUserRecord get(String id) {
		return super.fetch(RushUserRecord.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushUserRecord.class, cri);
		}
		return 0;
	}

	public RushUserRecord checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(RushUserRecord.class,cri);
	}

	private SimpleCriteria getCri(RushUserRecord obj) {
		SimpleCriteria cri = Cnd.cri();


		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}

		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getRushCount())) {
			cri.where().andEquals("rush_count", obj.getRushCount());
		}
		if(StringTool.isNotNull(obj.getWrongCount())) {
			cri.where().andEquals("wrong_count", obj.getWrongCount());
		}
		if(StringTool.isNotNull(obj.getRightCount())) {
			cri.where().andEquals("right_count", obj.getRightCount());
		}
		if(StringTool.isNotNull(obj.getState())) {
			cri.where().andEquals("state", obj.getState());
		}
		if(StringTool.isNotNull(obj.getCostTime())) {
			cri.where().andEquals("cost_time", obj.getCostTime());
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
		return cri;
	}
	
	/**
	 * 获取闯关统计集合
	 * @param userName 用户名
	 * @param gateId	关口ID
	 * @param state 闯关状态
	 * @return RushGateuserRecord 闯关统计集合
	 */
	public RushUserRecord getRushGateuserRecords(String userName,String gateId,int state){
		RushUserRecord rgr=new RushUserRecord();
		rgr.setUserName(userName);
		rgr.setSequenceId(gateId);
		rgr.setState(state);
		List<RushUserRecord> result = query(rgr);
		if(result!=null && result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取用户闯关信息，没有该用户在该工种下的排名
	 * @param sequenceId
	 * @return
	 * @return_type Map<String,Object>
	 */
	public Map<String, Object> getUserRushSeqInfo(String  sequenceId) {
		String str = "SELECT  so.name, rl.level_name, uls.score AS totalScore, sum(rgr.rush_count) AS rushCount " +
				     "FROM  user_level_score uls " +
				     "LEFT JOIN rush_gateuser_record  rgr ON uls.sequence_id = rgr.sequence_id AND uls.user_name = '" + LoginUtil.getUserLoginInfo().getUserName() + "' " +
				     "LEFT JOIN user_info ui ON ui.name = uls.user_name AND uls.sequence_id = '" + sequenceId + "' " +
				     "LEFT JOIN sys_organization so ON so.id = ui.org_id " +
				     "LEFT JOIN rush_level rl ON rl.id = uls.level_id " +
				     "WHERE rgr.user_name = '" + LoginUtil.getUserLoginInfo().getUserName() + "' AND rgr.sequence_id = '" + sequenceId +"'";
		Sql sql = Sqls.create(str);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				while(rs.next()) {
					map.put("orgName", rs.getString(1));
					map.put("levelName", rs.getString(2));
					map.put("totalScore", rs.getDouble(3));
					map.put("rushCount", rs.getInt(4));
				}
				return map;
			}
		});
		dao.execute(sql);
		return (Map<String, Object>) sql.getResult();
	}
	
	/**
	 * 获取用户在某工种下的积分总分排名
	 * @param sequenceId
	 * @return
	 */
	public int getUserRushRank(String  sequenceId) {
		String str = "SELECT temp.rank FROM " +
					 "(SELECT (@@rowNum:=@@rowNum+1) AS rank,t.user_name FROM ( " +
					 "SELECT SUM(uls.score) AS totalScore , uls.user_name FROM user_level_score uls " +
					 "WHERE uls.sequence_id = '" + sequenceId + "' GROUP BY uls.user_name ORDER BY totalScore DESC ) t ,(SELECT (@@rowNum :=0) ) rn) temp " +
					 "WHERE temp.user_name = '" + LoginUtil.getUserLoginInfo().getUserName() + "'";
		
		Sql sql = Sqls.create(str);
		sql.setCallback(new SqlCallback() {
			@Override
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				int rank = 0;
				if(rs.next())
					rank = rs.getInt(1);
				return rank;
			}
		});
		dao.execute(sql);
		return (Integer) sql.getResult();
	}
	
}