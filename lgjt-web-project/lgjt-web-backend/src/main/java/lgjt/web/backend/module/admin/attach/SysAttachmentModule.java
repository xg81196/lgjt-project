package lgjt.web.backend.module.admin.attach;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.domain.backend.attach.SysAttachment;
import lgjt.services.backend.attach.SysAttachmentService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@At("/file")
@IocBean
@Log4j
public class SysAttachmentModule {

	@Inject("sysAttachmentService")
	SysAttachmentService service;

	/**
	 * 根据文件ID获取文件对象
	 * @return
	 */
	@At("/getFileNameAndId")
	public Object getFileNameAndId(@Param("ids") String ids){
		if(StringTool.isNull(ids)){
			return null;
		}
		String id=ids;
		List<SysAttachment> list=new ArrayList<SysAttachment>();
		list.add(service.get(id));
		return Results.parse(Constants.STATE_SUCCESS, "查询数据成功",list );
	}
	
	/**
	 * 删除附件
	 * @param ids
	 * @return
	 */
	@At("/delete")
	public Object delete(@Param("ids") String ids){
		if(StringTool.isNull(ids)){
			return Results.parse(Constants.STATE_FAIL, "不存在此附件");
		}
		return Results.parse(Constants.STATE_SUCCESS, "删除数据成功",service.delete(ids));
	}



	/**
	 * 文件上传接口
	 *
	 * @param file
	 * @return
	 */
	@At("/upload")
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object upload(@Param("file") TempFile[] files) {

		try {
			String attachId = service.upload(files);
			return Results.parse(Constants.STATE_SUCCESS,null,attachId);
		} catch (Exception e) {
			log.error(e, e);
			return Results.parse(Constants.STATE_FAIL, "文件上传失败");
		}

	}
	


	
	/**
	 * 获取pdf文件大小
	 * @param fid
	 * @return
	 */
	@At("/viewSize")
	public Long viewSize(@Param("fid") String fid) {
		Long fileSize=0L;
			SysAttachment attach = service.get(fid);
			if(attach!=null){
				fileSize=attach.getSourceFileSize();
			}
			return fileSize;
			
	}
	
	@At("/view")
	@Ok("raw")
	public Object view(@Param("fid") String fid) {
		try {
			HttpServletResponse resp = Mvcs.getResp();
			SysAttachment attach = service.get(fid);
			String path = attach.getSourceFilePath();
			
//			path =PropertyUtil.getProperty("rootPath") + File.separator + path;
			path = PropertyUtil.getProperty("root-path") + path;
			if (StringTool.isNotNull(attach.getSourceName())) {
				resp.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode(attach.getSourceName(), "UTF-8"));
			}
			File dest = new File(path);
			return dest;
		} catch (Exception e) {
			log.error("upload img file error");
			return null;
		}
	}



	/*
 * 导入模板下载
 */
	@At("/downloadUserFile")
	@Ok("raw")
	public Object download() {
		try {
			HttpServletResponse resp = Mvcs.getResp();
			String path = "人员导入模版.xlsx";
			path = PropertyUtil.getProperty("rootPath") + File.separator
					+ path;
			String fname = "人员导入模版.xlsx";// 导出文件的文件名
			resp.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(fname, "UTF-8"));
			File dest = new File(path);
			return dest;
		} catch (Exception e) {
			return null;
		}
	}

}