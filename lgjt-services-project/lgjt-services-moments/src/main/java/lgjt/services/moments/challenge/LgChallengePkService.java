package lgjt.services.moments.challenge;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lgjt.common.base.vo.Count;
import lgjt.domain.moments.challenge.LgChallengePk;
import lgjt.domain.moments.challenge.LgChallengePkVo;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;


@Log4j
@IocBean
public class LgChallengePkService extends BaseService {


	public PageResult<LgChallengePk> queryPage(LgChallengePk obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
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

		return super.queryPage(LgChallengePk.class, obj, cri);
	}

    public PageResult<LgChallengePkVo> queryPageVo(LgChallengePkVo obj) {
        SimpleCriteria cri = Cnd.cri();

        if(StringTool.isNotNull(obj.getSuperId())) {
            cri.where().andEquals("super_id", obj.getSuperId());
        }
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

        return super.queryPage(LgChallengePkVo.class, obj, cri);
    }

	public List<LgChallengePk> query(LgChallengePk obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
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

		return super.query(LgChallengePk.class, cri);
	}

    public List<LgChallengePkVo> query(LgChallengePkVo obj) {
        SimpleCriteria cri = Cnd.cri();
        if(StringTool.isNotNull(obj.getSuperId())) {
            cri.where().andEquals("super_id", obj.getSuperId());
        }
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

        return super.query(LgChallengePkVo.class, cri);
    }

   	public LgChallengePk get(String id) {
		return super.fetch(LgChallengePk.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgChallengePk.class, cri);
		}
		return 0;
	}

	public LgChallengePk checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgChallengePk.class,cri);
	}

	public Integer getChallengerCount(String userId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_id", userId);
		List<Count> result = super.query("get.challengeCount.user", Count.class, cri);
		if(result.size() > 0) {
			return result.get(0).getCount();
		} else {
			return 0;
		}
	}

    /**
     * 查询挑战者
     * 按照点赞量排序
     * 没有点赞量的按照时间倒叙排序
     * @param superId
     * @return
     */
	public List<LgChallengePkVo> queryChallengers(String superId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("super_id", superId);
        cri.desc("crt_time");
        return super.query(LgChallengePkVo.class, cri);
    }

	/**
	 * 审核
	 * @param id
	 * @param verfilyAction
	 * @return
	 */
	public Integer verfilyAction(String id, String verfilyAction, String userName, String checkMsg) {

		LgChallengePk value = new LgChallengePk();
		value.setId(id);
		if(verfilyAction.equals("1")) {
			//通过
			value.setStatus(1);
		} else {
			//不同过
			value.setStatus(2);
			if(StringUtils.isNotEmpty(checkMsg)) {
				value.setCheckMsg(checkMsg);
			}
		}
		value.setPublishTime(new Date());
		value.setCheckUser(userName);
		value.setUpdUser(userName);
		value.setUpdTime(new Date());
		return super.updateIgnoreNull(value);
	}
}