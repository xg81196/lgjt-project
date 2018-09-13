package lgjt.web.app.module.notice.admin.receive;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.domain.app.noticereceive.LgNoticeReceive;
import lgjt.domain.app.noticereceive.vo.LgNoticeReceiveVo;
import lgjt.services.app.noticereceive.AdminLgNoticeReceiveService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

@At("/admin/notice/receive")
@IocBean
@Log4j
public class AdminLgNoticeReceiveModule {

    @Inject
    AdminLgNoticeReceiveService adminLgNoticeReceiveService;

    /**
     * 查询用户是否已读消息
     * 按姓名查询用户是否已读消息
     * @param obj
     * @return
     */
    @At("/noticeReceiveStatusIsRead")
    public Object noticeReceiveStatusIsRead(@Param("..") LgNoticeReceiveVo obj){
       return Results.parse(Constants.STATE_SUCCESS,null,adminLgNoticeReceiveService.queryPageForStatusIsRead(obj));
    }
}
