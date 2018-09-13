package lgjt.services.app.noticereceive;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.app.noticereceive.LgNoticeReceive;
import lgjt.domain.app.noticereceive.vo.LgNoticeReceiveVo;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.user.SysUserService;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.HashMap;

/**
 * 消息 日志管理
 */
@Log4j
@IocBean(fields = {"dao:daoApp"})
public class AdminLgNoticeReceiveService  extends BaseService {


    /**
     * 查询用户是否已读
     * @param obj
     * @return
     */
    public PageResult<LgNoticeReceiveVo> queryPageForStatusIsRead(LgNoticeReceiveVo obj) {
        SimpleCriteria cri = Cnd.cri();
        //按照用户名查询
        if(obj != null && obj.getCrtUser() != null  &&  !"".equals(obj.getCrtUser())){
            cri.where().andLike("r.crt_user",obj.getCrtUser());
            return super.queryPage("lgjt.web.app.queryPageForStatusIsReadToUserName", LgNoticeReceiveVo.class, obj, cri);
        }else {
            return super.queryPage("lgjt.web.app.queryPageForStatusIsRead", LgNoticeReceiveVo.class, obj, cri);
        }
    }
}
