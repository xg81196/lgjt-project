package lgjt.web.backend.module.admin.app;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import lgjt.domain.backend.app.AppVersion;
import lgjt.domain.backend.app.SysAttachment;
import lgjt.services.backend.app.SysAppVersionService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/***********************
 *
 ***********************/
@At("/sysAppVersionModule")
@Log4j
@IocBean
public class SysAppVersionModule {
	
	@Inject
	SysAppVersionService service;
	
	/**
	 * 查询文件的版本号，文件名，更新信息
	 */
	@At("/queryJson")
	public Object queryJson(@Param("..") AppVersion obj) {
		AppVersion androidav = service.queryJson(0);
		AppVersion iOSav = service.queryJson(1);
		List<AppVersion> avList = new  ArrayList<AppVersion>();
		avList.add(androidav);
		avList.add(iOSav);
		return Results.parse(Constants.STATE_SUCCESS,"查询成功",avList);
	}
	
	/**
	 * 查询最新版本的app更新信息
	 */
	@At("/getNewest")
	public Object getNewest(@Param("..") AppVersion obj) {
		String str= Mvcs.getReq().getHeader("clientType");
		if(StringTool.isNotEmpty(str)){
			if(str.contains("Android")){
				obj.setAdviceType("0");
			}else if(str.contains("iOS")){
				obj.setAdviceType("1");
			}else{
				return Results.parse(Constants.STATE_FAIL, "无法识别的终端类型");
			}
		}
		AppVersion av = service.getNewest(obj);
		String version = Mvcs.getReq().getHeader("appVersion");
		String version1=av.getVersion();
		
		if(version1.compareTo(version)>0){
			return Results.parse(Constants.STATE_SUCCESS,"有最新版本,请更新！",av);
		}
			return Results.parse(Constants.STATE_SUCCESS,"已是最新版本！");
	}
	
	/**
	 * 下载APK
	 * @param obj 文件名
	 */
	@At("/downloadApp")
	@Ok("raw")
	public Object downloadApp(@Param("..") SysAttachment obj) {
		try {
			HttpServletResponse resp = Mvcs.getResp();
			//根据包名查询数据
			SysAttachment sa = service.downloadApp(obj);
			if(sa!=null){				
				//拼接地址
				String path = sa.getSourceFilePath();
				path = PropertyUtil.getProperty("root-path") + File.separator + path;
				if (StringTool.isNotNull(sa.getSourceName())) {
					resp.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode(sa.getSourceName(), "UTF-8"));
				}
				//转成字符流
				File dest = new File(path);
				return dest;
			}else{
				return Results.parse(Constants.STATE_FAIL, "下载失败，该附件不存在");
			}
		} catch (UnsupportedEncodingException e) {
			log.error("下载失败");
			return null;
		}
	}
}
