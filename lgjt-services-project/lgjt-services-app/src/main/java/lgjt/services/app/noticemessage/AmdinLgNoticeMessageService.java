package lgjt.services.app.noticemessage;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.app.noticemessage.LgNoticeMessage;
import lgjt.domain.app.org.SysOrganization;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.user.SysUserService;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 管理员 管理消息
 */
@Log4j
@IocBean(fields = {"dao:daoApp"})
public class AmdinLgNoticeMessageService extends BaseService {

    @Inject
    SysUserService sysUserService;


    /**
     * 企业信息表
     */
    @Inject
    SysOrganizationService organizationService;

    /**
     * 删除消息
     *
     * @param ids 消息ID
     * @return
     */
    public int delete(String ids) {
        if (StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("id", ids.split(","));
            return super.delete(LgNoticeMessage.class, cri);
        }
        return 0;
    }

    /**
     * 分页查询消息列表
     *
     * @param CurrentPage 当前页码
     * @param PageSize    每页显示多少数据
     * @param InfoTitle   搜索标题
     * @return
     */
    public PageResult<LgNoticeMessage> queryPageLgNoticeMessageList(LgNoticeMessage message, String InfoTitle) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(InfoTitle)) {
            cri.where().andLike("title", InfoTitle);
            cri.desc("send_date");
        }
        //获取消息
        PageResult<LgNoticeMessage> temp = super.queryPage(LgNoticeMessage.class, new LgNoticeMessage(), cri);

        //循环遍历
        for (LgNoticeMessage dd : temp.getRows()) {

            //个人用户
            System.out.println("-------------------------     目标类型：" + dd.getTarget());

            //个人用户
            if ( dd != null  &&  dd.getTarget() != null && dd.getTarget() == 2 ) {
                List<String> names = new ArrayList<>();
                names.clear();
                List<SysUser> sysUsers = sysUserService.queryUserList(dd.getReceiveId());
                String[] n = dd.getReceiveId().split(",");

                for (SysUser sysUser : sysUsers) {
                    for (int i = 0; i < n.length; i++) {
                        if (sysUser.getId().equals(n[i])) {
                            names.add(sysUser.getRealName());
                        }
                    }
                }
                dd.setUserNames(names);
            }else if(dd != null  &&  dd.getTarget() != null &&  dd.getTarget() == 1){
                //
                List<String> names = new ArrayList<>();
                names.clear();
                //id
                String[] n = dd.getReceiveId().split(",");
                List<SysOrganization> org = organizationService.queryORgNameForId(dd.getReceiveId());
                for (SysOrganization o : org) {
                    for (int i = 0; i < n.length; i++) {
                        if (o.getId().equals(n[i])) {
                            names.add(o.getName());
                        }
                    }
                }
                dd.setUserNames(names);
            }
        }
        return temp;
    }



    /**
     * 获取消息
     *
     * @param id
     * @return
     */
    public LgNoticeMessage getNotice(String id) {
        return super.fetch(LgNoticeMessage.class, id);
    }

}
