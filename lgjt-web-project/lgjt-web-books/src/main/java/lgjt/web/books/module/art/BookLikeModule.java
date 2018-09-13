package lgjt.web.books.module.art;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.books.art.BookLike;
import lgjt.services.books.art.BookLikeService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import java.util.Date;

/**
 *
 * Create By xigexb At 2018/9/10
 *
 */
@At("/art")
@IocBean
@Log4j
public class BookLikeModule {

    @Inject
    BookLikeService bookLikeService;



    @At("/DoLike")
    public Object doLike(@Param("type") String type,@Param("id")String bookId){
        // 获取当前用户信息
        UserLoginInfo uli = LoginUtil.getUserLoginInfo();
        //构造一个对象
        BookLike b = new BookLike();
        if(uli == null){
            return Results.parse(Constants.STATE_UNLOGIN, "请登录");
        }
        String flag = "1";
        //type 1 点赞 0 取消点赞
        if(flag.trim().equals(type)){
            b.setArtId(bookId);
            b.setUserId(uli.getInfos().get("userId"));
            b.setCrtIp(ClientInfo.getIp());
            b.setCrtTime(new Date());
            b.setCrtUser(uli.getUserName());
            b = bookLikeService.insert(b);
        }else {
            b.setArtId(bookId);
            b.setUserId(uli.getInfos().get("userId"));
            int isDelete = bookLikeService.delete(b);
            if(isDelete>0){
                return Results.parse(Constants.STATE_SUCCESS, "取消点赞");
            }
        }
        if(b != null) {
            return Results.parse(Constants.STATE_SUCCESS, "点赞成功");
        } else {
            return Results.parse(Constants.STATE_FAIL,"点赞失败");
        }
    }






}
