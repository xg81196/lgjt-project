package lgjt.services.moments.admin;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import javafx.scene.chart.ValueAxis;
import lgjt.domain.moments.challenge.AdminLgChallenge;
import lgjt.domain.moments.challenge.LgChallenge;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.Date;
import java.util.List;

/**
 * @author 赵天意
 * 后台管理竞技跟拍服务
 */
@Log4j
@IocBean
public class AdminLgChallengeService extends BaseService {

    /**
     * 竞技跟拍管理
     * @param obj
     * @return
     */
    public PageResult<AdminLgChallenge> managerList(LgChallenge obj) {
        SimpleCriteria cri = Cnd.cri();
        if(StringUtils.isNotEmpty(obj.getContent())) {
            cri.where().andEquals("ax.content", obj.getContent());
        }
        if(StringUtils.isNotEmpty(obj.getRealName())) {
            cri.where().andEquals("ax.real_name", obj.getRealName());
        }
        if(StringUtils.isNotEmpty(obj.getGroupName())) {
            cri.where().andEquals("ax.group_name", obj.getGroupName());
        }
        if(obj.getIsDisable()==null) {
            cri.where().andEquals("ax.is_disable", obj.getIsDisable());
        }
        if(StringUtils.isNotEmpty(obj.getGroupId())) {
            cri.where().andEquals("ax.group_id", obj.getGroupId());
        }
        cri.desc("ax.crt_time");
        return super.queryPage("admin.manageList", AdminLgChallenge.class, obj, cri);
    }

    /**
     * 审核
     * @param id
     * @param verfilyAction
     * @return
     */
    public Integer verfilyAction(String id, String verfilyAction, String userName, String checkMsg) {

        LgChallenge value = new LgChallenge();
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
