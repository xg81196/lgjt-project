package lgjt.services.rush.level_score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lgjt.common.base.vo.Total;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.rush.level_score.RushLevelRank;
import lgjt.domain.rush.level_score.RushLevelScore;
import lgjt.domain.rush.level_score.RushLevelScoreVo;


@Log4j
@IocBean
public class RushLevelScoreService extends BaseService {

	/**
	 * 全部排行
	 */
	private static final String RUSH_ALLRANK ="rush.rush.getAllRank";

	private static final String RUSH_MYRANK_ALL ="rush.rush.getMyRank";

	private static final String RUSH_SEARCHRANK = "rush.admin.rush.level.score.rank";


	public PageResult<RushLevelScore> queryPage(RushLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
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

		return super.queryPage(RushLevelScore.class, obj, cri);
	}

	public List<RushLevelScore> query(RushLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
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
		return super.query(RushLevelScore.class, cri);
	}

	public PageResult<RushLevelScoreVo> queryPageBySearch(RushLevelScore obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getCompanyName())) {
			cri.where().andLike("a.company_name", obj.getCompanyName());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("a.user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("a.real_name", obj.getRealName());
		}
		Map<String,Object> param = new HashMap<>();
		param.put("page", (obj.getPage() - 1)*obj.getPageSize());
		param.put("pageSize", obj.getPageSize() * obj.getPage());
		List<RushLevelScoreVo> rlsList = super.query(RUSH_SEARCHRANK, RushLevelScoreVo.class, cri,param);
		PageResult<RushLevelScoreVo> result = new PageResult<>(rlsList);
		return result;

	}

   	public RushLevelScore get(String id) {
		return super.fetch(RushLevelScore.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushLevelScore.class, cri);
		}
		return 0;
	}

	public RushLevelScore checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(RushLevelScore.class,cri);
	}


	public Integer sumScore(String userName) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_name",userName);
		return super.getDao().func(RushLevelScore.class,"sum","score",cri);
	}


	public RushLevelScore getByUserNameAndSequenceId(String userName,String sequenceId){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_name", userName).andEquals("sequence_id", sequenceId);
		List<RushLevelScore> userLevelScores = super.query(RushLevelScore.class, cri);
		if(userLevelScores!=null && userLevelScores.size()>0){
			return userLevelScores.get(0);
		}else{
			return null;
		}
	}


	/**
	 * 查询我在全部排行里的排名信息
	 * @param userName 用户userName
	 * @return 返回我在全部排行里的排名
	 */
	public List<RushLevelRank> queryRushMyRank(String userName,String sequenceName){
		Map<String,Object> param = new HashMap<>();
		SimpleCriteria cri=Cnd.cri();
		cri.where().andEquals("t.user_name",userName);
		cri.where().andEquals("t.sequence_name",sequenceName);
		return super.query(RUSH_MYRANK_ALL, RushLevelRank.class, cri,param);
	}


	/**
	 * 查询全部排行信息
	 * @param rushLevelScore
	 * @return 返回全部排行列表
	 */
	public List<RushLevelRank> queryRushAllRank(RushLevelScore rushLevelScore){
		Map<String,Object> param = new HashMap<>();
		SimpleCriteria cri=Cnd.cri();
		if(StringUtils.isNotEmpty(rushLevelScore.getSequenceId())) {
			cri.where().andEquals("t.sequence_id",rushLevelScore.getSequenceId());
		}
		param.put("page", rushLevelScore.getPage()*rushLevelScore.getPageSize());
		param.put("pageSize", rushLevelScore.getPageSize());
		return super.query(RUSH_ALLRANK, RushLevelRank.class, cri,param);
	}

	public List<Total> queryRushAllRankTotal(RushLevelScore rushLevelScore) {
		SimpleCriteria cri=Cnd.cri();
		if(StringUtils.isNotEmpty(rushLevelScore.getSequenceId())) {
			cri.where().andEquals("t.sequence_id",rushLevelScore.getSequenceId());
		}
		return super.query("rush.rush.getAllRank.total", Total.class, cri);

	}


	/**
	 * 根据公司名称查询公司排行榜
	 * @param obj
	 * @return
	 */
	public List<RushLevelRank> queryRushByCompanyName(RushLevelScore obj) {
		Map<String,Object> param = new HashMap<>();
		SimpleCriteria cri=Cnd.cri();
		if(StringUtils.isNotEmpty(obj.getCompanyId())) {
			cri.where().andEquals("x.company_id",obj.getCompanyId());
		}
		param.put("page", obj.getPage()*obj.getPageSize());
		param.put("pageSize", obj.getPageSize());
		return super.query("rush.rush.getRankByCompanyName", RushLevelRank.class, cri,param);
	}

	public List<Total> queryRushByCompanyNameTotal(RushLevelScore obj) {
		SimpleCriteria cri=Cnd.cri();
		if(StringUtils.isNotEmpty(obj.getCompanyId())) {
			cri.where().andEquals("t.company_id",obj.getCompanyId());
		}
		return super.query("rush.rush.getRankByCompanyName.total", Total.class, cri);

	}


}