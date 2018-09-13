package lgjt.web.books.module.admin;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringUtil;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.books.LgLetterInfo;
import lgjt.services.books.LgLetterInfoService;
import lgjt.services.books.admin.AdminLgLetterInfoService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import java.util.Date;

/**
 * @author zhaotianyi
 * 书香莱钢后台管理
 */
@At("/admin/lgBookInfo")
@IocBean
@Log4j
public class AdminLgLetterInfoModule {


    @Inject
    AdminLgLetterInfoService service;

    /**
     * 通过ID删除或批量删除
     *
     * @param ids
     * @return
     */
    @At("/delete")
    public Object delete(@Param("ids") String ids) {
        int del = service.delete(ids);
        if (del > 0) {
            return Results.parse(Constants.STATE_SUCCESS, null);
        } else {
            return Results.parse(Constants.STATE_FAIL, null);
        }
    }

    /**
     * 添加一个书
     *
     * @param obj
     * @return
     */
    @At("/insert")
    public Object insert(@Param("..") LgLetterInfo obj) {
        obj.setCrtTime(new Date());
        obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setBookType(0);//开发一期默认是音频，只能是音频
        obj.setBookTime(new Date());
        obj.setPublishStatus(0);
        if (StringUtils.isNotEmpty(LoginUtil.getUserLoginInfo().getRealName())) {
            obj.setRealName(LoginUtil.getUserLoginInfo().getRealName());
        }
        LgLetterInfo o = service.insert(obj);
        if (o != null) {
            return Results.parse(Constants.STATE_SUCCESS, null, o);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    /**
     * 编辑一个书(包括单独编辑内容)
     *
     * @param obj
     * @return
     */
    @At("/update")
    public Object update(@Param("..") LgLetterInfo obj) {
        obj.setUpdTime(new Date());
        obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
        obj.setCrtIp(ClientInfo.getIp());
        int upd = service.updateIgnoreNull(obj);
        if (upd > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

    @At("/list")
    public Object list(@Param("..") LgLetterInfo obj) {
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryPage(obj));
    }

}