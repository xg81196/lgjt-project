package lgjt.services.moments.groupmessage;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.moments.groupmessage.LgGroupMessage;
import lgjt.domain.moments.groupmessage.LgGroupMessageInfoVo;
import lgjt.domain.moments.groupmessage.LgGroupMessageVo;
import lgjt.domain.moments.usercomment.LgUserComment;
import lgjt.domain.moments.userlike.LgUserLike;
import lgjt.services.moments.usercomment.LgUserCommentService;
import lgjt.services.moments.userlike.LgUserLikeService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhaotianyi
 * @depre 班圈相关
 */
@Log4j
@IocBean
public class LgGroupMessageService extends BaseService {

//    @Inject
//    LgUserLikeService lgUserLikeService;

    @Inject("lgUserCommentService")
    LgUserCommentService lgUserCommentService;

    @Inject
    LgUserLikeService lgUserLikeService;

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
            cri.where().andEquals("real_name", obj.getRealName());
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
            cri.where().andEquals("real_name", obj.getRealName());
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
     *
     */
    public PageResult<LgGroupMessageVo> messageList(LgGroupMessageVo obj) {

        String userId = obj.getUserId();
        obj.setUserId(null);
        //首先查询整体记录数
        SimpleCriteria cri = Cnd.cri();
        //审核过的
        cri.where().andEquals("verify_status", "1");
        //状态为启用的
        cri.where().andEquals("status", "1");
        //搜索条件
        if(StringUtils.isNotEmpty(obj.getGroupName())) {
            cri.where().andLike("group_name", obj.getGroupName());
        }
        //时间倒叙
        cri.desc("crt_time");
        PageResult<LgGroupMessageVo> lgGroupMessageVoPageResult = super.queryPage(LgGroupMessageVo.class, obj, cri);

        List<LgGroupMessageVo> lgGroupMessageVos = lgGroupMessageVoPageResult.getRows();
        //第二步，查询点赞数量
        for(LgGroupMessageVo vo: lgGroupMessageVos) {
            String messageId = vo.getId();
            LgUserLike lgUserLike = new LgUserLike();
            lgUserLike.setMessageId(messageId);
            List<LgUserLike> lgUserLikes = lgUserLikeService.query(lgUserLike);
            vo.setLikeCount(lgUserLikes.size());
        }

        //第二步，查询当前用户是否点赞
        for(LgGroupMessageVo vo: lgGroupMessageVos) {
            String messageId = vo.getId();
            LgUserLike lgUserLike = new LgUserLike();
            lgUserLike.setMessageId(messageId);
            lgUserLike.setUserId(userId);
            List<LgUserLike> lgUserLikes = lgUserLikeService.query(lgUserLike);
            vo.setIsLike(lgUserLikes.size());
        }

        lgGroupMessageVoPageResult.setRows(lgGroupMessageVos);
        return lgGroupMessageVoPageResult;
    }

    /**
     * 获取班圈详情信息
     */
    public LgGroupMessageInfoVo messageInfo(LgGroupMessage obj) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(obj.getId())) {
            cri.where().andEquals("id", obj.getId());
        }
        List<LgGroupMessageInfoVo> lgGroupMessageInfoVos = super.query(LgGroupMessageInfoVo.class, cri);
        LgGroupMessageInfoVo lgGroupMessageInfoVo = lgGroupMessageInfoVos.get(0);

        //查询评论
        cri = Cnd.cri();
        cri.where().andEquals("message_id", lgGroupMessageInfoVo.getId());
        //倒叙
        cri.desc("crt_time");
        List<LgUserComment> lgUserComments = lgUserCommentService.query(LgUserComment.class, cri);

        //放进
        lgGroupMessageInfoVo.setCommentsList(lgUserComments);
        return lgGroupMessageInfoVo;
    }

    public PageResult<LgGroupMessageVo> queryMyMessageList(LgGroupMessage obj) {
        String userId = obj.getUserId();
        //首先查询整体记录数
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("user_id", userId);
        PageResult<LgGroupMessageVo> lgGroupMessageVoPageResult = super.queryPage(LgGroupMessageVo.class, obj, cri);

        List<LgGroupMessageVo> lgGroupMessageVos = lgGroupMessageVoPageResult.getRows();
        //第二步，查询点赞数量
        for(LgGroupMessageVo vo: lgGroupMessageVos) {
            String messageId = vo.getId();
            LgUserLike lgUserLike = new LgUserLike();
            lgUserLike.setMessageId(messageId);
            List<LgUserLike> lgUserLikes = lgUserLikeService.query(lgUserLike);
            vo.setLikeCount(lgUserLikes.size());
        }

        //第二步，查询当前用户是否点赞
        for(LgGroupMessageVo vo: lgGroupMessageVos) {
            String messageId = vo.getId();
            LgUserLike lgUserLike = new LgUserLike();
            lgUserLike.setMessageId(messageId);
            lgUserLike.setUserId(userId);
            List<LgUserLike> lgUserLikes = lgUserLikeService.query(lgUserLike);
            vo.setIsLike(lgUserLikes.size());
        }

        lgGroupMessageVoPageResult.setRows(lgGroupMessageVos);
        return lgGroupMessageVoPageResult;

    }
}