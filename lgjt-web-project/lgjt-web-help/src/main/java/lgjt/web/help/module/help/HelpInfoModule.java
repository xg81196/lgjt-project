package lgjt.web.help.module.help;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.help.ERPSaveFileName;
import lgjt.domain.help.ERPWorkToDo;
import lgjt.domain.help.LgHelpInfo;
import lgjt.services.help.ERPSaveFileNameService;
import lgjt.services.help.LgHelpInfoService;
import lgjt.services.help.LgHelpInfoServiceMysql;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import java.sql.Date;

/**
 * Create by @author xigexb At @date 2018/9/11
 *
 */
@At("/helpInfo")
@IocBean
@Log4j
public class HelpInfoModule {


    @Inject
    LgHelpInfoService service;


    @Inject
    LgHelpInfoServiceMysql lgHelpInfoServiceMysql;

    @Inject
    ERPSaveFileNameService erpSaveFileNameService;

    /**
     * 查询我的申报
     *
     * @param
     * @return
     */
    @At("/queryMyDeclare")
    @GET
    public Object queryPage(@Param("pageSize") Integer pageSize, @Param("page") Integer page,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("type")Integer type) {
        log.info("module:------"+pageSize+"------"+page);
        System.out.println(startTime+"======="+endTime);
        //根据token获取用户信息
        UserLoginInfo userLoginInfo = CacheFactory.getLoginInfoCache().
                getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") +
                        Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME));
        if (userLoginInfo != null && userLoginInfo.getUserName() != null) {
            Results o = Results.parse(Constants.STATE_SUCCESS, null, service.queryPage(userLoginInfo.getUserName(), pageSize, page,startTime,endTime,type));
            if(o==null)
                return Results.parse(Constants.STATE_SUCCESS, null);
            else
                return o;
        }
            return Results.parse(Constants.STATE_UNLOGIN, "用户信息不存在，请重新登录！");
    }


    /**
     * 查询代办事项
     * @param obj
     * @return
     */
    @At("/queryMyToDo")
    @GET
    public Object queryCount(@Param("..") ERPWorkToDo obj,@Param("startTime")String startTime,@Param("endTime")String endTime) {
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        System.out.println("帮扶救助---我的代办----用户信息 ----"+userLoginInfo);
        if(userLoginInfo != null && userLoginInfo.getUserName() != null){
            Results o = Results.parse(Constants.STATE_SUCCESS, null, service.queryCount(obj, userLoginInfo.getUserName(),startTime,endTime));
            if(o==null)
               return Results.parse(Constants.STATE_SUCCESS, null);
            else
                return o;
        }
        return Results.parse(Constants.STATE_UNLOGIN, "用户信息不存在，请重新登录！");
    }


    /**
     * 增加帮助信息信息
     * @param obj
     * @return
     */
    @At("/insert")
    @POST
    public Object insert(@Param("..") LgHelpInfo obj) {
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        if(userLoginInfo == null){
            return Results.parse(Constants.STATE_UNLOGIN,"请登录");
        }
        obj.setCrtUser(userLoginInfo.getUserName());
        obj.setCrt_time(new Date(System.currentTimeMillis()));
        obj.setCrtIp(ClientInfo.getIp());
        LgHelpInfo obj1 = lgHelpInfoServiceMysql.insert(obj);
        // 在客户SQLserver数据库插入信息
        LgHelpInfo o = service.insert(obj);
        JSONArray objects = JSONArray.parseArray(o.getAttachId());
        for (Object oj : objects){
            ERPSaveFileName u = new ERPSaveFileName();
            JSONObject jsonObject =  JSONObject.parseObject(oj.toString());
            u.setNowName(jsonObject.get("filePath")+"/"+jsonObject.get("fileName2")+"|");
            u.setOldName(jsonObject.get("filePath")+"/"+jsonObject.get("fileName2")+"|");
            u.setRecCreatTime(new java.util.Date());
            erpSaveFileNameService.insertMssql(u);
        }
        //返回结果实例
        //[{"fileName":"焊工@2x.png","filePath":"/file/2018-08-31","fileName2":"1535712606534焊工@2x.png","id":"xz4nVAJ2Rk4_REMd1EoG1E4_VERbxEH2xLNN16FlxExBe6_UxZl6xzyN1Alyqzap1AYWqLNGV6HPV6ta"}]
        if(o!=null) {
            return Results.parse(Constants.STATE_SUCCESS,"申报成功!");
        }else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }


    /**
     * 查询申报详情
     * @return
     */
    @At("/gethelpinfo")
    public Object get(@Param("helpId")String helpId){
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        if(userLoginInfo == null){
            return Results.parse(Constants.STATE_UNLOGIN,"请登录");
        }
        LgHelpInfo lgHelpInfo = service.checkId(helpId);
        if(lgHelpInfo == null){
            Results.parse(Constants.STATE_SUCCESS,"没有此记录",null);
        }
        return Results.parse(Constants.STATE_SUCCESS,null,lgHelpInfo);
    }


    /**
     * @return
     */
    @At("/update")
    public Object update(@Param("..") LgHelpInfo obj){
        System.out.println(obj.toString());
        UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
        if(userLoginInfo == null){
            return Results.parse(Constants.STATE_UNLOGIN,"请登录");
        }

        obj.setUpdUser(userLoginInfo.getUserName());
        obj.setUpdIp(ClientInfo.getIp());
        obj.setUpdTime(new java.util.Date());
        int update = service.updateIgnoreNull(obj);
        if(update>0){
            return Results.parse(Constants.STATE_SUCCESS,"更新成功");
        }else {
            return Results.parse(Constants.STATE_FAIL,"更新失败");
        }
    }

}
