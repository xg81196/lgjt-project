package lgjt.web.app.module.download;

import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;
import lgjt.common.base.constants.ReturnCode;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.user.SysUserService;
import lgjt.services.mongodb.company.MongodbService;
import lgjt.web.app.module.base.AppBaseModule;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * show 下载接口.
 * @author daijiaqi
 * @date 2018/5/1114:39
 */
@At("/download")
@IocBean
@Log4j
public class DownLoadModule extends AppBaseModule {
    @Inject("sysUserService")
    SysUserService sysUserService;

    /**
     * show 头像浏览，无返回值.
     * @author daijiaqi
     * @date 2018/5/1114:39
     * @param id 文件ID
     */
    @POST
    @GET
    @At("/headPortrait")
    @Ok("raw")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public void headPortrait(@Param("id") String id) {
        try {
            // 文件上传MONGODB
            HttpServletResponse resp = Mvcs.getResp();
            resp.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("head", "UTF-8"));
            MongodbService.getInstance("file").download(resp.getOutputStream(), id, 0,-1);
        } catch (IOException e) {
            log.error("DownLoadModule.downloadHeadPortrait(" + id + ").", e);
        }
    }

    /**
     * show 根据用户ID获取头像，无返回值.
     * @author daijiaqi
     * @date 2018/5/1114:39
     * @param userId 用户ID
     */
    @POST
    @GET
    @At("/headPortraitByUserId")
    @Ok("raw")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public void headPortraitByUserId(@Param("userId") String userId) {
        String id = "";
        try {
            if (userId == null) {
                return;
            }
            SysUser sysUser = sysUserService.get(userId);
            if (sysUser == null || sysUser.getHeadPortrait() == null) {
                return;
            }
            id = sysUser.getHeadPortrait();
            // 文件上传MONGODB
            HttpServletResponse resp = Mvcs.getResp();
            resp.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("head", "UTF-8"));
            MongodbService.getInstance("file").download(resp.getOutputStream(), id, 0,-1);
        } catch (IOException e) {
            log.error("UploadModule.headPortraitByUserId(" + id + ").", e);
        }
    }

    /**
     * show 根据用户名获取头像，无返回值.
     * @author daijiaqi
     * @date 2018/5/1114:39
     * @param userName 用户名
     */
    @POST
    @GET
    @At("/headPortraitByUserName")
    @Ok("raw")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public void headPortraitByUserName(@Param("userName") String userName) {
        String id = "";
        try {
            if (userName == null) {
                return;
            }
            lgjt.domain.app.user.SysUserVo sysUser = sysUserService.getByUserName(userName);
            if (sysUser == null || sysUser.getHeadPortrait() == null) {
                return;
            }
            id = sysUser.getHeadPortrait();
            // 文件上传MONGODB
            HttpServletResponse resp = Mvcs.getResp();
            resp.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("head", "UTF-8"));
            MongodbService.getInstance("file").download(resp.getOutputStream(), id, 0,-1);
        } catch (IOException e) {
            log.error("UploadModule.headPortraitByUserId(" + id + ").", e);
        }
    }
}
