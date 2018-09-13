package lgjt.services.books.art;

import java.util.List;

import lgjt.domain.books.art.BookLike;
import lgjt.domain.books.art.LgArt;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;


@Log4j
@IocBean
public class BookLikeService extends BaseService {


    public PageResult<BookLike> queryPage(BookLike obj) {
        SimpleCriteria cri = Cnd.cri();

        if (StringTool.isNotNull(obj.getArtId())) {
            cri.where().andEquals("art_id", obj.getArtId());
        }
        if (StringTool.isNotNull(obj.getUserId())) {
            cri.where().andEquals("user_id", obj.getUserId());
        }
        if (StringTool.isNotNull(obj.getUserName())) {
            cri.where().andEquals("user_name", obj.getUserName());
        }
        if (StringTool.isNotNull(obj.getScore())) {
            cri.where().andEquals("score", obj.getScore());
        }
        if (StringTool.isNotNull(obj.getVoteDate())) {
            cri.where().andEquals("vote_date", obj.getVoteDate());
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

        return super.queryPage(BookLike.class, obj, cri);
    }

    public List<BookLike> query(BookLike obj) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("art_id", obj.getArtId());
        return super.query(BookLike.class, cri);
    }

    public BookLike get(String id) {
        return super.fetch(BookLike.class, id);
    }


    public int delete(BookLike obj) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("art_id", obj.getArtId());
        cri.where().andEquals("user_id", obj.getUserId());
        return super.delete(BookLike.class, cri);
    }

    public BookLike checkId(String value, String userId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("art_id", value);
        cri.where().andEquals("user_id", userId);
        return super.fetch(BookLike.class, cri);
    }

//    /**
//     * 刷新点赞数
//     * @param id
//     * @return
//     */
//    public List<LgArt> likeCount(String id){
//        SimpleCriteria cri = Cnd.cri();
//        cri.where().andEquals("art_id",id);
//        return  super.query("lgjt.wbe.books.queryLikeCount", LgArt.class);
//    }




}