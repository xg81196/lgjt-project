package lgjt.web.app.module.notice.noticemessage;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.app.noticemessage.LgNoticeMessage;
import lgjt.domain.app.noticereceive.LgNoticeReceive;
import lgjt.services.app.noticemessage.LgNoticeMessageService;
import lgjt.services.app.noticereceive.LgNoticeReceiveService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;
import redis.clients.jedis.Client;

import java.sql.Date;

//import lgjt.web.notice.module.websocket.WebSocket;


@At("/noticemessage")
@IocBean
@Log4j
public class LgNoticeMessageModule {

    /**
     * 信息表
     */
    @Inject
    private LgNoticeMessageService messageService;

    /**
     * 消息关系表
     */
    @Inject
    private LgNoticeReceiveService noticeReceiveService;

    /**
     * 询全部消息通知
     * 状态： 1 已读，0 未读
     * 查询全部 未读消息列表，已读消息列表
     * @param noticeType 查询类型  0 未读 1 已读  2 全部
     * @return
     */
    @At("/getallNotice")
    @GET
    public Object getAllNotice(@Param("..")LgNoticeMessage obj,@Param("type")
            Integer noticeType) {
        UserLoginInfo userLoginInfo = CacheFactory.getLoginInfoCache().
                getLoginInfo(PropertyUtil.getProperty("redis-prefix-login")+
                        Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME));

        if(userLoginInfo ==null){
            return Results.parse(Constants.STATE_UNLOGIN, "请登录");
        }else {
            return Results.parse(Constants.STATE_SUCCESS, null, messageService.queryreadAndUnreadMessage(obj,
                    userLoginInfo.getInfos().get("userId"),noticeType));
        }

    }

    /**
     *
     * 获取消息详情信息
     * @param noticeId 消息id
     * @return 消息详情实体
     */
    @At("/getNotice")
    @GET
    public Object getNotice(@Param("noticeId") String noticeId) {

        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login")+token);

        if(userLoginInfo ==null){
            return Results.parse(Constants.STATE_UNLOGIN, "请登录");
        }

        if(noticeId == null){
             return Results.parse(Constants.STATE_FAIL, "此消息不存在！");
        }


        // 获取到当前这条消息
        LgNoticeMessage obj = messageService.get(noticeId);


        //查询关系表 查询出这条数据。
        LgNoticeReceive notice = noticeReceiveService.getForNoticeId(noticeId,userLoginInfo.getInfos().get("userId"));

        //如果notice是null，说明没有这个记录
        if(notice == null) {
            return Results.parse(Constants.STATE_FAIL, "此消息不存在！");
        }
        if(notice !=null &&  notice.getStatus()!=1){
            notice.setCrtUser(userLoginInfo.getUserName());
            notice.setUpdUser(userLoginInfo.getUserName());
            notice.setCrtIp(ClientInfo.getIp());
            notice.setUpdIp(ClientInfo.getIp());
            notice.setCrtTime(new Date(System.currentTimeMillis()));
            notice.setUpdTime(new Date(System.currentTimeMillis()));
            notice.setStatus(1);
            noticeReceiveService.updateIgnoreNull(notice);
        }

        //返回信息详情
        if (null != obj) {
            return Results.parse(Constants.STATE_SUCCESS, null, obj);
        } else {
            return Results.parse(Constants.STATE_FAIL, "此消息不存在！");
        }
    }


}