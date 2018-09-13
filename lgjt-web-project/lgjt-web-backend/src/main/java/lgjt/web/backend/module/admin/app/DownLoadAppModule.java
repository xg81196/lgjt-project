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
import lgjt.domain.backend.app.SysAttachment;
import lgjt.services.backend.app.SysAppVersionService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 下载APK，二维码短连接
 * @author wangxinyu
 */
@At("/a")
@Log4j
@IocBean
public class DownLoadAppModule {

	@Inject
	SysAppVersionService service;
	
	/**
	 * 下载APK，二维码短连接
	 * @author wangxinyu
	 */
	@At("/a")
	@Ok("raw")
	public Object download(){
		SysAttachment sa = new SysAttachment();
		List<SysAttachment> sas = service.queryApp();
		if (sas.size() > 0) {
			sa.setId(sas.get(0).getId());
			HttpServletResponse resp = Mvcs.getResp();
			//根据包名查询数据
			SysAttachment s = service.downloadApp(sa);
			if(s!=null){				
				//拼接地址
				String path = s.getSourceFilePath();
				path = PropertyUtil.getProperty("root-path") + File.separator + path;
				if (StringTool.isNotNull(s.getSourceName())) {
					try {
						resp.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode(s.getSourceName(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("下载失败");
						return null;
					}
				}
				//转成字符流
				File file = new File(path);
				return file;
			}
		} else {
			return Results.parse(Constants.STATE_FAIL, "下载失败，该附件不存在");
		}
		return null;
	}
}
