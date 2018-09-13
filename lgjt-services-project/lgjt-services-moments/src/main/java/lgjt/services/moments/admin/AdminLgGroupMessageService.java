package lgjt.services.moments.admin;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.moments.groupmessage.LgGroupMessage;
import lgjt.domain.moments.groupmessage.LgGroupMessageInfoVo;
import lgjt.domain.moments.groupmessage.LgGroupMessageVo;
import lgjt.domain.moments.usercomment.LgUserComment;
import lgjt.services.moments.usercomment.LgUserCommentService;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

@Log4j
@IocBean
public class AdminLgGroupMessageService extends BaseService {

//    @Inject
//    LgUserLikeService lgUserLikeService;

    @Inject("lgUserCommentService")
    LgUserCommentService lgUserCommentService;

    private static final String MESSAGELIST_WITHCOUNT = "moments.messageList.hasLikeCount";

    public PageResult<LgGroupMessage> queryPage(LgGroupMessage obj) {
        SimpleCriteria cri = Cnd.cri();

        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        if (StringTool.isNotNull(obj.getGroupId())) {
            cri.where().andEquals("group_id", obj.getGroupId());
        }
        if (StringTool.isNotNull(obj.getContent())) {
            cri.where().andEquals("content", obj.getContent());
        }
        if (StringTool.isNotNull(obj.getAttachId())) {
            cri.where().andEquals("attach_id", obj.getAttachId());
        }
        if (StringTool.isNotNull(obj.getType())) {
            cri.where().andEquals("type", obj.getType());
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if (StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if (StringTool.isNotNull(obj.getUpdUser())) {
            cri.where().andEquals("upd_user", obj.getUpdUser());
        }
        if (StringTool.isNotNull(obj.getUpdTime())) {
            cri.where().andEquals("upd_time", obj.getUpdTime());
        }
        if (StringTool.isNotNull(obj.getUpdIp())) {
            cri.where().andEquals("upd_ip", obj.getUpdIp());
        }
        if (StringTool.isNotNull(obj.getRealName())) {
            cri.where().andLike("real_name", obj.getRealName());
        }
        if (StringTool.isNotNull(obj.getGroupName())) {
            cri.where().andLike("group_name", obj.getGroupName());
        }
        if (StringTool.isNotNull(obj.getVerifyStatus())) {
            cri.where().andEquals("verify_status", obj.getVerifyStatus());
        }
        if (StringTool.isNotNull(obj.getVerifyUser())) {
            cri.where().andEquals("verify_user", obj.getVerifyUser());
        }
        return super.queryPage(LgGroupMessage.class, obj, cri);
    }

    public List<LgGroupMessage> query(LgGroupMessage obj) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        if (StringTool.isNotNull(obj.getGroupId())) {
            cri.where().andEquals("group_id", obj.getGroupId());
        }
        if (StringTool.isNotNull(obj.getContent())) {
            cri.where().andEquals("content", obj.getContent());
        }
        if (StringTool.isNotNull(obj.getAttachId())) {
            cri.where().andEquals("attach_id", obj.getAttachId());
        }
        if (StringTool.isNotNull(obj.getType())) {
            cri.where().andEquals("type", obj.getType());
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if (StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if (StringTool.isNotNull(obj.getUpdUser())) {
            cri.where().andEquals("upd_user", obj.getUpdUser());
        }
        if (StringTool.isNotNull(obj.getUpdTime())) {
            cri.where().andEquals("upd_time", obj.getUpdTime());
        }
        if (StringTool.isNotNull(obj.getUpdIp())) {
            cri.where().andEquals("upd_ip", obj.getUpdIp());
        }
        if (StringTool.isNotNull(obj.getRealName())) {
            cri.where().andLike("real_name", obj.getRealName());
        }
        if (StringTool.isNotNull(obj.getGroupName())) {
            cri.where().andLike("group_name", obj.getGroupName());
        }
        return super.query(LgGroupMessage.class, cri);
    }

    public LgGroupMessage get(String id) {
        return super.fetch(LgGroupMessage.class, id);
    }

    public int delete(String ids) {
        if (StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("id", ids.split(","));
            return super.delete(LgGroupMessage.class, cri);
        }
        return 0;
    }

    public LgGroupMessage checkId(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id", value);
        return super.fetch(LgGroupMessage.class, cri);
    }

    /**
     * 获取班圈列表
     */
    public PageResult<LgGroupMessageVo> messageList(LgGroupMessageVo obj) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(obj.getGroupName())) {
            cri.where().andLike("lgm.group_name", obj.getGroupName());
        }
        return super.queryPage(MESSAGELIST_WITHCOUNT, LgGroupMessageVo.class, obj, cri);
    }

    /**
     * 获取班圈详情信息
     */
    public LgGroupMessageInfoVo messageInfo(LgGroupMessage obj) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        List<LgGroupMessageInfoVo> lgGroupMessageInfoVos = super.query(LgGroupMessageInfoVo.class, cri);
        LgGroupMessageInfoVo lgGroupMessageInfoVo = lgGroupMessageInfoVos.get(0);

        //查询评论
        cri = Cnd.cri();
        cri.where().andEquals("message_id", lgGroupMessageInfoVo.getId());
        List<LgUserComment> lgUserComments = lgUserCommentService.query(LgUserComment.class, cri);

        //放进

        lgGroupMessageInfoVo.setCommentsList(lgUserComments);

        return lgGroupMessageInfoVo;
    }

}