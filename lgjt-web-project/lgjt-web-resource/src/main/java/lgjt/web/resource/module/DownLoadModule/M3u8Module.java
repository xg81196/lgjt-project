package lgjt.web.resource.module.DownLoadModule;

import lombok.extern.log4j.Log4j;
import org.apache.http.HttpStatus;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.ConstantsCommon;

import java.io.File;
import java.util.Enumeration;

/**
 * @author daijiaqi
 * @date 2018/6/269:16
 */
@At("/m3u8111")
@IocBean
@Log4j
public class M3u8Module {

    @POST
    @GET
    @At("/download/?")
    @Ok("raw")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public File download(String path,String xx) {
        try {
            System.out.println(path);
            System.out.println(Mvcs.getReq().getParameterNames());

            Enumeration<String> pNames= Mvcs.getReq().getParameterNames();
            while (pNames.hasMoreElements()){
                String key = pNames.nextElement();
                System.out.println(key+"="+Mvcs.getReq().getParameterValues(key));
            }

            if(path.endsWith("005.ts")){
                Mvcs.getResp().setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            File f =new File("D:/worksoft/nginx-1.14.0/html/ckplayer/1/",path);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @POST
    @GET
    @At("/test")
    @Ok("raw")
    @Authority(ConstantsCommon.AUTHORITY_NO_LOGIN)
    public File test(@Param("path") String path) {
        try {
            System.out.println(path);
            if(path.endsWith("005.ts")){
                Mvcs.getResp().setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            File f =new File("D:/worksoft/nginx-1.14.0/html/ckplayer/1/",path);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
