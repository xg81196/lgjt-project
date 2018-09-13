package lgjt.services.books.art;

import java.util.List;

import lgjt.domain.books.art.BookLike;
import lgjt.domain.books.art.LgArt;
import lgjt.domain.books.art.LgFileInfo;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import org.nutz.mvc.annotation.At;

/**
 * 书香作品service
 * Create By
 *
 * @author xigexb At @date 2018/9/12
 */
@Log4j
@IocBean
public class LgArtService extends BaseService {


    /**
     * 点赞表
     */
    @Inject
    BookLikeService bookLikeService;

    /**
     * 附件表
     */
    @Inject
    LgFileInfoService lgFileInfoService;


    public PageResult<LgArt> queryPage(LgArt obj) {
        SimpleCriteria cri = Cnd.cri();

        if (StringTool.isNotNull(obj.getTitle())) {
            cri.where().andEquals("title", obj.getTitle());
        }
        if (StringTool.isNotNull(obj.getContent())) {
            cri.where().andEquals("content", obj.getContent());
        }
        if (StringTool.isNotNull(obj.getOrgId())) {
            cri.where().andEquals("org_id", obj.getOrgId());
        }
        if (StringTool.isNotNull(obj.getOrgName())) {
            cri.where().andEquals("org_name", obj.getOrgName());
        }
        if (StringTool.isNotNull(obj.getCategoryId())) {
            cri.where().andEquals("category_id", obj.getCategoryId());
        }
        if (StringTool.isNotNull(obj.getStatus())) {
            cri.where().andEquals("status", obj.getStatus());
        }
        if (StringTool.isNotNull(obj.getCheckMsg())) {
            cri.where().andEquals("check_msg", obj.getCheckMsg());
        }
        if (StringTool.isNotNull(obj.getCheckUser())) {
            cri.where().andEquals("check_user", obj.getCheckUser());
        }
        if (StringTool.isNotNull(obj.getAuthor())) {
            cri.where().andEquals("author", obj.getAuthor());
        }
        if (StringTool.isNotNull(obj.getPublishTime())) {
            cri.where().andEquals("publish_time", obj.getPublishTime());
        }
        if (StringTool.isNotNull(obj.getIsTop())) {
            cri.where().andEquals("is_top", obj.getIsTop());
        }
        if (StringTool.isNotNull(obj.getIsDisable())) {
            cri.where().andEquals("is_disable", obj.getIsDisable());
        }
        if (StringTool.isNotNull(obj.getIsDelete())) {
            cri.where().andEquals("is_delete", obj.getIsDelete());
        }
        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        if (StringTool.isNotNull(obj.getUserProfile())) {
            cri.where().andEquals("user_profile", obj.getUserProfile());
        }
        if (StringTool.isNotNull(obj.getRealName())) {
            cri.where().andEquals("real_name", obj.getRealName());
        }
        if (StringTool.isNotNull(obj.getExtend1())) {
            cri.where().andEquals("extend1", obj.getExtend1());
        }
        if (StringTool.isNotNull(obj.getExtend2())) {
            cri.where().andEquals("extend2", obj.getExtend2());
        }
        if (StringTool.isNotNull(obj.getExtend3())) {
            cri.where().andEquals("extend3", obj.getExtend3());
        }
        if (StringTool.isNotNull(obj.getExtend4())) {
            cri.where().andEquals("extend4", obj.getExtend4());
        }
        if (StringTool.isNotNull(obj.getExtend5())) {
            cri.where().andEquals("extend5", obj.getExtend5());
        }
        if (StringTool.isNotNull(obj.getExtend6())) {
            cri.where().andEquals("extend6", obj.getExtend6());
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if (StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
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

        return super.queryPage(LgArt.class, obj, cri);
    }

    public List<LgArt> query(LgArt obj) {
        SimpleCriteria cri = Cnd.cri();
        if (StringTool.isNotNull(obj.getTitle())) {
            cri.where().andEquals("title", obj.getTitle());
        }
        if (StringTool.isNotNull(obj.getContent())) {
            cri.where().andEquals("content", obj.getContent());
        }
        if (StringTool.isNotNull(obj.getOrgId())) {
            cri.where().andEquals("org_id", obj.getOrgId());
        }
        if (StringTool.isNotNull(obj.getOrgName())) {
            cri.where().andEquals("org_name", obj.getOrgName());
        }
        if (StringTool.isNotNull(obj.getCategoryId())) {
            cri.where().andEquals("category_id", obj.getCategoryId());
        }
        if (StringTool.isNotNull(obj.getStatus())) {
            cri.where().andEquals("status", obj.getStatus());
        }
        if (StringTool.isNotNull(obj.getCheckMsg())) {
            cri.where().andEquals("check_msg", obj.getCheckMsg());
        }
        if (StringTool.isNotNull(obj.getCheckUser())) {
            cri.where().andEquals("check_user", obj.getCheckUser());
        }
        if (StringTool.isNotNull(obj.getAuthor())) {
            cri.where().andEquals("author", obj.getAuthor());
        }
        if (StringTool.isNotNull(obj.getPublishTime())) {
            cri.where().andEquals("publish_time", obj.getPublishTime());
        }
        if (StringTool.isNotNull(obj.getIsTop())) {
            cri.where().andEquals("is_top", obj.getIsTop());
        }
        if (StringTool.isNotNull(obj.getIsDisable())) {
            cri.where().andEquals("is_disable", obj.getIsDisable());
        }
        if (StringTool.isNotNull(obj.getIsDelete())) {
            cri.where().andEquals("is_delete", obj.getIsDelete());
        }
        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        if (StringTool.isNotNull(obj.getUserProfile())) {
            cri.where().andEquals("user_profile", obj.getUserProfile());
        }
        if (StringTool.isNotNull(obj.getRealName())) {
            cri.where().andEquals("real_name", obj.getRealName());
        }
        if (StringTool.isNotNull(obj.getExtend1())) {
            cri.where().andEquals("extend1", obj.getExtend1());
        }
        if (StringTool.isNotNull(obj.getExtend2())) {
            cri.where().andEquals("extend2", obj.getExtend2());
        }
        if (StringTool.isNotNull(obj.getExtend3())) {
            cri.where().andEquals("extend3", obj.getExtend3());
        }
        if (StringTool.isNotNull(obj.getExtend4())) {
            cri.where().andEquals("extend4", obj.getExtend4());
        }
        if (StringTool.isNotNull(obj.getExtend5())) {
            cri.where().andEquals("extend5", obj.getExtend5());
        }
        if (StringTool.isNotNull(obj.getExtend6())) {
            cri.where().andEquals("extend6", obj.getExtend6());
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if (StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
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
        return super.query(LgArt.class, cri);
    }

    public LgArt get(String id) {
        return super.fetch(LgArt.class, id);
    }

    public int delete(String ids) {
        if (StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("id", ids.split(","));
            return super.delete(LgArt.class, cri);
        }
        return 0;
    }


    public LgArt checkId(String value, String userId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id", value);
        LgArt art = super.fetch(LgArt.class, cri);
        if (art == null) {
            return null;
        }

        LgFileInfo fileInfo = new LgFileInfo();
        fileInfo.setExtend1(art.getId());
        List<LgFileInfo> queryList = lgFileInfoService.query(fileInfo);
        if (queryList == null) {
            return art;
        }

        // 点赞数
        BookLike b = new BookLike();
        b.setArtId(art.getId());
        //当前数据的点赞信息
        List<BookLike> query = bookLikeService.query(b);
        art.setCountlike(query.size());


        //是否点赞
        BookLike bookLike = bookLikeService.checkId(art.getId(), userId);
        if (bookLike != null) {
            art.setLikeStatus(true);
        } else {
            art.setLikeStatus(false);
        }


        //循环附件
        for (LgFileInfo v : queryList) {
            StringBuffer sb = new StringBuffer();
            sb.append(v.getId());
            sb.append(",");
            art.setExtend3(sb.toString());
        }
        return art;
    }


    /**
     * 书香莱钢作品
     *
     * @param obj
     * @return
     */
    public PageResult<LgArt> queryPageArtList(LgArt obj, String userId) {
        SimpleCriteria cri = Cnd.cri();
        // 审核状态  0 提交 1 通过  2 驳回
        cri.where().andEquals("status", 1);
        //是否有效
        cri.where().andEquals("is_disable", 0);
        // 查询是否删除
        cri.where().andEquals("is_delete", 0);
        //时间倒叙
        cri.desc("publish_time");

        //类型查询
        cri.where().andEquals("category_id", obj.getCategoryId());

        // 查询书香作品数据
        PageResult<LgArt> lgArtPageResult = super.queryPage(LgArt.class, obj, cri);

        // 点赞数
        for (LgArt art : lgArtPageResult.getRows()) {
            BookLike b = new BookLike();
            b.setArtId(art.getId());
            //当前数据的点赞信息
            List<BookLike> query = bookLikeService.query(b);
            art.setCountlike(query.size());
        }

        //是否点赞
        for (LgArt art : lgArtPageResult.getRows()) {
            BookLike bookLike = bookLikeService.checkId(art.getId(), userId);
            if (bookLike != null) {
                art.setLikeStatus(true);
            } else {
                art.setLikeStatus(false);
            }
        }

        // 附件处理
        for (LgArt art : lgArtPageResult.getRows()) {
            LgFileInfo fileInfo = new LgFileInfo();
            fileInfo.setExtend1(art.getId());
            List<LgFileInfo> queryList = lgFileInfoService.query(fileInfo);
            if (queryList == null) {
                return lgArtPageResult;
            }
            //循环附件
            for (LgFileInfo v : queryList) {
                StringBuffer sb = new StringBuffer();
                sb.append(v.getId());
                sb.append(",");
                art.setExtend3(sb.toString());
            }
        }

        return lgArtPageResult;
    }


    /**
     * 个人中心书香莱钢作品
     *
     * @param obj
     * @return
     */
    public PageResult<LgArt> queryPageArtListForMyCenter(LgArt obj, String userId) {
        SimpleCriteria cri = Cnd.cri();
        //是否有效
        cri.where().andEquals("is_disable", 0);
        // 查询是否删除
        cri.where().andEquals("is_delete", 0);
        //时间倒叙
        cri.desc("publish_time");

        //类型查询
        cri.where().andEquals("category_id", obj.getCategoryId());
        //查询当前用户发布的作品
        cri.where().andEquals("user_id", userId);
        // 查询书香作品数据
        PageResult<LgArt> lgArtPageResult = super.queryPage(LgArt.class, obj, cri);
        // 点赞数
        for (LgArt art : lgArtPageResult.getRows()) {
            if (Integer.valueOf(art.getStatus()) != 2) {
                BookLike b = new BookLike();
                b.setArtId(art.getId());
                //当前数据的点赞信息
                List<BookLike> query = bookLikeService.query(b);
                art.setCountlike(query.size());
            }
        }
        //是否点赞
        for (LgArt art : lgArtPageResult.getRows()) {
            if (Integer.valueOf(art.getStatus()) != 2) {
                BookLike bookLike = bookLikeService.checkId(art.getId(), userId);
                if (bookLike != null) {
                    art.setLikeStatus(true);
                } else {
                    art.setLikeStatus(false);
                }
            }
        }
        // 附件处理
        for (LgArt art : lgArtPageResult.getRows()) {
            if(!(art.getCategoryId().trim().equals("04".trim())|| art.getCategoryId().trim().equals("05".trim()))) {
                LgFileInfo fileInfo = new LgFileInfo();
                fileInfo.setExtend1(art.getId());
                List<LgFileInfo> queryList = lgFileInfoService.query(fileInfo);
                if (queryList == null) {
                    return lgArtPageResult;
                }
                //循环附件
                for (LgFileInfo v : queryList) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(v.getId());
                    sb.append(",");
                    art.setExtend3(sb.toString());
                }
            }
        }

        return lgArtPageResult;
    }


    /**
     * 后台管理 查询书香作品列表
     *
     * @param obj
     * @return
     */
    public PageResult<LgArt> queryPageAdminArtList(LgArt obj, String Type) {
        SimpleCriteria cri = Cnd.cri();
        //类型查询
        cri.where().andEquals("category_id", obj.getCategoryId());

        if (StringTool.isNotEmpty(obj.getTitle())) {
            cri.where().andLike("title", obj.getTitle());
        }
        if (StringTool.isNotEmpty(obj.getOrgName())) {
            cri.where().andLike("org_name", obj.getOrgName());
        }
        if (StringTool.isNotEmpty(obj.getAuthor())) {
            cri.where().andLike("author", obj.getAuthor());
        }
        if (StringTool.isNotEmpty(obj.getIsDisable()) && Integer.valueOf(obj.getIsDisable()) != -1) {
            cri.where().andEquals("is_disable", obj.getIsDisable());
        }
        //时间倒叙
        cri.desc("publish_time");

        if (StringTool.isNotEmpty(Type) && "1".trim().equals(Type.trim())) {
            cri.where().andEquals("status", 0);
        }

        // 查询书香作品数据
        PageResult<LgArt> lgArtPageResult = super.queryPage(LgArt.class, obj, cri);

        // 点赞数
        for (LgArt art : lgArtPageResult.getRows()) {
            BookLike b = new BookLike();
            b.setArtId(art.getId());
            //当前数据的点赞信息
            List<BookLike> query = bookLikeService.query(b);
            art.setCountlike(query.size());
        }
        return lgArtPageResult;
    }


    /**
     * 后台管理
     * 禁用书香作品
     *
     * @param art
     * @return
     */
    public Integer adminDisplay(LgArt art) {
        return super.updateIgnoreNull(art);
    }


//    /**
//     * 后台管理
//     * 审核作品
//     *
//     * @param id
//     * @return
//     */
//    public Integer adminCheck(String id, String checkMeg, String status) {
//        LgArt art = new LgArt();
//        art.setId(id);
//        //审核状态 0：提交，1：通过，2：驳回
//        if ("2".trim().equals(status.trim())) {
//            art.setCheckMsg(checkMeg);
//        }
//        art.setStatus(Integer.valueOf(status));
//        return super.updateIgnoreNull(art);
//    }


    /**
     * 后台管理
     * 审核作品
     *
     * @param art
     * @return
     */
    public Integer adminCheck(LgArt art) {
        //审核状态 0：提交，1：通过，2：驳回
        if (Integer.valueOf(art.getStatus()) == 2) {
            art.setCheckMsg(art.getCheckMsg());
        } else {
            art.setCheckMsg(null);
        }
        return super.updateIgnoreNull(art);
    }

}