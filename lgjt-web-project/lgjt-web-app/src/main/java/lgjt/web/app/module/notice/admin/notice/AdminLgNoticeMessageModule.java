package lgjt.web.app.module.notice.admin.notice;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.app.noticemessage.LgNoticeMessage;
import lgjt.domain.app.noticereceive.LgNoticeReceive;
import lgjt.domain.app.org.SysOrganization;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.noticemessage.AmdinLgNoticeMessageService;
import lgjt.services.app.noticereceive.AdminLgNoticeReceiveService;
import lgjt.services.app.noticereceive.LgNoticeReceiveService;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.user.SysUserService;
import lgjt.vo.app.user.SysUserOrgVo;
import lgjt.web.app.module.websocket.NoticeServer;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@At("/admin/notice/message")
@IocBean
@Log4j
public class AdminLgNoticeMessageModule {

    /**
     * 信息表
     */
    @Inject
    AmdinLgNoticeMessageService messageService;

    /**
     * 接收通知表
     */
    @Inject
    AdminLgNoticeReceiveService receiveService;


    @Inject
    LgNoticeReceiveService lgNoticeReceiveService;
    @Inject
    SysUserService sysUserService;


    /**
     * webSocket
     */
    @Inject
    NoticeServer server;


    /**
     * 企业信息表
     */
    @Inject
    SysOrganizationService organizationService;


    @Inject
    SysUserService sysUserServicer;


    /**
     * 分页查询消息列表
     */
    @At("/queryPageLgNoticeMessageList")
    public Object queryPage(@Param("InfoTitle") String infoTitle,
                            @Param("..") LgNoticeMessage message) {
        return Results.parse(Constants.STATE_SUCCESS, null, messageService.queryPageLgNoticeMessageList(message, infoTitle));
    }

    /**
     * 删除消息
     *
     * @param ids 单个id 或者id集合
     * @return
     */
    @At("/delete")
    public Object delete(@Param("ids") String ids) {
        return Results.parse(Constants.STATE_SUCCESS, "删除成功！", messageService.delete(ids));
    }


    /**
     * 修改通知消息
     *
     * @param obj
     * @return
     */
    @At("/update")
    public Object update(@Param("..") LgNoticeMessage obj) {
        int upd = messageService.update(obj);
        if (upd > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    /**
     * 查询消息详情
     *
     * @param id 消息 id
     * @return
     */
    @At("/getNoticeForId")
    public LgNoticeMessage get(@Param("id") String id) {
        return messageService.getNotice(id);
    }


    /**
     * 发布通知
     *
     * @param obj
     * @param
     * @return
     */
    @At("/insert")
    @POST
    public Object insert(@Param("..") LgNoticeMessage obj, @Param("..") SysUser sysUser) {
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        if(userLoginInfo==null){
            return Results.parse(Constants.STATE_UNLOGIN, "请登录");
        }
        obj.setCrtUser(userLoginInfo.getUserName());
        obj.setCrtTime(new Timestamp(System.currentTimeMillis()));
        obj.setCrtIp(ClientInfo.getIp());
        obj.setBeginTime(new Timestamp(System.currentTimeMillis()));
        obj.setEndTime(new Timestamp(System.currentTimeMillis()));
        obj.setStatus(1);
        obj.setSendDate(new Date(System.currentTimeMillis()).toString());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setCrtTime(new Timestamp(System.currentTimeMillis()));
        //添加消息
        LgNoticeMessage o = messageService.insert(obj);

        if (o == null && o.getTarget() == null) {
            return Results.parse(Constants.STATE_FAIL, "添加消息数据不正确");
        }



//        插入关系表中

        //获取所有用户消息
        PageResult<SysUser> sysUserPageResult = sysUserService.queryPage(sysUser);


        //所有人发送
        if (o.getTarget() == 0) {
            System.out.println("-------------------------------给所有人发送消息");
            for (SysUser sysUser1 : sysUserPageResult.getRows()) {
                LgNoticeReceive newReceive = new LgNoticeReceive();
                String userid = sysUser1.getId();
                newReceive.setUserId(userid);
                System.out.println("------------------------------用户ID：  "+newReceive.getUserId());
                newReceive.setMessageId(o.getId());
                newReceive.setCrtUser(sysUser1.getUserName());
                newReceive.setCrtTime(obj.getCrtTime());
                newReceive.setBeginTime(o.getBeginTime());
                newReceive.setEndTime(o.getEndTime());
                newReceive.setStatus(0);
                lgNoticeReceiveService.insert(newReceive);
                // example:> "INSERT INTO lg_notice_receive(ID,message_id,user_id,company_id,begin_time,end_time,status,crt_user,crt_time,
            }
        }


        //给指定用户添加信息
        if (o.getTarget() == 2) {
            System.out.println("-------------------------------给指定人员人发送消息");
            String[] ids = o.getReceiveId().split(",");
            for (SysUser sysUser1 : sysUserPageResult.getRows()) {
                for (int i = 0; i < ids.length; i++) {
                    if (ids[i] == sysUser1.getId()) {
                        LgNoticeReceive newReceive = new LgNoticeReceive();
                        newReceive.setUserId(sysUser1.getId());
                        newReceive.setMessageId(o.getId());
                        newReceive.setUserId(sysUser.getId());
                        newReceive.setCrtUser(sysUser1.getUserName());
                        newReceive.setCrtTime(obj.getCrtTime());
                        newReceive.setBeginTime(o.getBeginTime());
                        newReceive.setEndTime(o.getEndTime());
                        newReceive.setStatus(0);
                        receiveService.insert(newReceive);
                    }
                }
            }
        }

        //给所属企业信息插入消息
        if(o.getTarget() == 1){
            System.out.println("-------------------------------给企业人员发送消息");
            String[] ids = o.getReceiveId().split(",");
            List<SysUser> sysUsers = sysUserService.queryUserForOrgList(o.getReceiveId());
            for ( SysUser sysUser1 : sysUsers ){
                LgNoticeReceive newReceive = new LgNoticeReceive();
                newReceive.setUserId(sysUser1.getId());
                newReceive.setMessageId(o.getId());
                newReceive.setUserId(sysUser.getId());
                newReceive.setCrtUser(sysUser1.getUserName());
                newReceive.setCrtTime(obj.getCrtTime());
                newReceive.setBeginTime(o.getBeginTime());
                newReceive.setEndTime(o.getEndTime());
                newReceive.setStatus(0);
                receiveService.insert(newReceive);
            }
        }



//        开始给客户端发数据
//        target是0就给所有人发送消息
        try {
            if (o != null && o.getTarget() == 0) {
                server.sendMessageToAllUser(Json.toJson(o));
                System.out.println(System.currentTimeMillis() + "----操作：发送消息 目标：全部用户 状态：推送成功！ ----------------------------------------------");

                // target是2 发送给给指定的用户
            } else if (o != null && o.getTarget() == 2) {
                System.out.println("给指定人员发送消息  ------------    状态： -------------正在发送中---------------");
                List<SysUser> sysUsers = sysUserService.queryUserList(o.getReceiveId());
                List<String> userNameList = new ArrayList<>();
                for (SysUser user : sysUsers) {
                    userNameList.add(user.getUserName());
                }
                server.sendMessageToUsers(userNameList, Json.toJson(o));
                System.out.println(System.currentTimeMillis() + "-----操作：发送消息 目标：个人用户 状态：推送成功！ ----------------------------------------------");
            } else if (o != null && o.getTarget() == 1) {
                List<SysUser> sysUsers = sysUserService.queryUserForOrgList(o.getReceiveId());
                List<String> userNameList = new ArrayList<>();
                for (SysUser user : sysUsers) {
                    userNameList.add(user.getUserName());
                }
                server.sendMessageToUsers(userNameList, Json.toJson(o));
                System.out.println(System.currentTimeMillis() + "----操作：发送消息 目标：企业用户 状态：推送成功！ ----------------------------------------------");
            } else {
                System.out.println(System.currentTimeMillis() + "----操作：发送消息  状态：推送失败！  原因：可能是要发送的消息为空，或者非法发送目标，发送目标值：" + o.getTarget());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (o != null) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    /**
     * 获取企业信息
     *
     * @param organization
     * @return
     */
    @At("/getorganizationList")
    public Object getorganizationList(@Param("..") SysOrganization organization) {
        return Results.parse(Constants.STATE_SUCCESS, null, organizationService.queryPage(organization));
    }


    /**
     * 查询所有用户信息
     *
     * @param sysUser
     * @return
     */
    @At("/querySys")
    public Object getUserInfo(@Param("..") SysUserOrgVo sysUser) {
        return Results.parse(Constants.STATE_SUCCESS, null, sysUserService.queryUserAndOrgInfo(sysUser));
    }
}