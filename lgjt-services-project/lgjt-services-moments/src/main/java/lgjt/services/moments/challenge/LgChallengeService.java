package lgjt.services.moments.challenge;


import com.ttsx.platform.nutz.result.PageResult;
import lgjt.common.base.vo.Count;
import lgjt.domain.moments.challenge.LgChallenge;
import lgjt.domain.moments.challenge.LgChallengeRank;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 赵天意
 * 跟拍相关
 */
@Log4j
@IocBean
public class LgChallengeService extends BaseService {


	public PageResult<LgChallenge> queryPage(LgChallenge obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andLike("content", obj.getContent());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getCheckMsg())) {
			cri.where().andEquals("check_msg", obj.getCheckMsg());
		}
		if(StringTool.isNotNull(obj.getCheckUser())) {
			cri.where().andEquals("check_user", obj.getCheckUser());
		}
		if(StringTool.isNotNull(obj.getPublishTime())) {
			cri.where().andEquals("publish_time", obj.getPublishTime());
		}
		if(StringTool.isNotNull(obj.getGroupId())) {
			cri.where().andEquals("group_id", obj.getGroupId());
		}
		if(StringTool.isNotNull(obj.getGroupName())) {
			cri.where().andLike("group_name", obj.getGroupName());
		}
		if(StringTool.isNotNull(obj.getIsDisable())) {
			cri.where().andEquals("is_disable", obj.getIsDisable());
		}
		if(StringTool.isNotNull(obj.getIsDelete())) {
			cri.where().andEquals("is_delete", obj.getIsDelete());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getUserProfile())) {
			cri.where().andEquals("user_profile", obj.getUserProfile());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andLike("real_name", obj.getRealName());
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
        cri.desc("crt_time");
		return super.queryPage(LgChallenge.class, obj, cri);
	}

	public List<LgChallenge> query(LgChallenge obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getContent())) {
			cri.where().andLike("content", obj.getContent());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getCheckMsg())) {
			cri.where().andEquals("check_msg", obj.getCheckMsg());
		}
		if(StringTool.isNotNull(obj.getCheckUser())) {
			cri.where().andEquals("check_user", obj.getCheckUser());
		}
		if(StringTool.isNotNull(obj.getPublishTime())) {
			cri.where().andEquals("publish_time", obj.getPublishTime());
		}
		if(StringTool.isNotNull(obj.getGroupId())) {
			cri.where().andEquals("group_id", obj.getGroupId());
		}
		if(StringTool.isNotNull(obj.getGroupName())) {
			cri.where().andEquals("group_name", obj.getGroupName());
		}
		if(StringTool.isNotNull(obj.getIsDisable())) {
			cri.where().andEquals("is_disable", obj.getIsDisable());
		}
		if(StringTool.isNotNull(obj.getIsDelete())) {
			cri.where().andEquals("is_delete", obj.getIsDelete());
		}
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getUserProfile())) {
			cri.where().andEquals("user_profile", obj.getUserProfile());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("real_name", obj.getRealName());
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
		cri.desc("crt_time");
		return super.query(LgChallenge.class, cri);
	}

   	public LgChallenge get(String id) {
		return super.fetch(LgChallenge.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgChallenge.class, cri);
		}
		return 0;
	}

	public LgChallenge checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgChallenge.class,cri);
	}

	//查询用户发起题材数量
	public Integer getCrtCount(String userId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("user_id", userId);
        List<Count> result = super.query("get.crtCount.user", Count.class, cri);
        if(result.size() > 0) {
            return result.get(0).getCount();
        } else {
            return 0;
        }
	}

    /**
     * 跟拍查询所有排行
     * @param challengeId
     * @return
     */
	public List<LgChallengeRank> queryChallengerAllRank(String challengeId, Integer page, Integer pageSize) {
        SimpleCriteria cri = Cnd.cri();
        Map<String, Object> param = new HashMap<>();
        param.put("challengeId", challengeId);
        int start = (page - 1) * pageSize;
        int end = pageSize * page;
        param.put("page", start);
        param.put("pageSize", end);
        return super.query("get.challengeInfo.rank", LgChallengeRank.class, cri, param);
    }

    /**
     * 跟拍查询用户排行
     * @param challengeId
     * @param userId
     * @return
     */
    public List<LgChallengeRank> queryChallengerMyRank(String challengeId, String userId) {
        SimpleCriteria cri = Cnd.cri();
        Map<String, Object> param = new HashMap<>();
        param.put("challengeId", challengeId);
        cri.where().andEquals("base.user_id", userId);
        param.put("page", 0);
        param.put("pageSize", 1);
        return super.query("get.challengeInfo.rank", LgChallengeRank.class, cri, param);
    }
}