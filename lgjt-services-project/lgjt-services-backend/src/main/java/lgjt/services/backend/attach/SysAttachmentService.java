package lgjt.services.backend.attach;


import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.upload.FieldMeta;
import org.nutz.mvc.upload.TempFile;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.CommonUtil;
import lgjt.common.base.utils.FileUtil;
import lgjt.domain.backend.attach.SysAttachment;

import java.io.File;
import java.util.Date;
import java.util.List;

@IocBean
public class SysAttachmentService extends BaseService {


	public PageResult<SysAttachment> queryPage(SysAttachment obj) {
		return super.queryPage(SysAttachment.class, obj, getCri(obj));
	}

	public List<SysAttachment> query(SysAttachment obj) {
		return super.query(SysAttachment.class, getCri(obj));
	}
	/**
	 * 根据IDS 获取 附件
	 * @param ids ,号分割
	 * @return
	 */
	public List<SysAttachment> queryByIds(String ids) {	
		SimpleCriteria cri = Cnd.cri();
		cri.where().andIn("id", ids.split(","));
		return super.query(SysAttachment.class, cri);
	}

	public String upload(TempFile[] files) {

		if(files==null){
			throw new RuntimeException("上传文件为空");
		}
		if(files!=null&&files.length==0){
			throw new RuntimeException("上传文件为空");
		}
		StringBuilder sb = new StringBuilder();
		for(TempFile file:files){
			FieldMeta meta = file.getMeta();
			String filePath = FileUtil.getFilePath();
			String fileName = FileUtil.getFileName(meta.getFileExtension());
			File dest = new File(filePath,fileName);
			//20171120修改
			try{
				FileUtil.copyFile(file.getFile(), dest);
				dest.setReadable(true, false);
				file.getFile().deleteOnExit();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			//file.getFile().renameTo(dest);
			//dest.setReadable(true, false);
			SysAttachment attachment = new SysAttachment(meta.getFileLocalName(),
					(filePath+File.separator+fileName).replace(CommonUtil.ROOT_PATH, ""),
					FileUtil.fileType(meta.getFileExtension()), dest.length());
			attachment.setCrtUser("admin");
			attachment.setCrtIp(ClientInfo.getIp());
			attachment.setCrtTime(new Date());
			attachment = super.insert(attachment);
			sb.append(attachment.getId()).append(",");
		}
		return sb.length()>0?sb.subSequence(0, sb.length()-1).toString():"";

	}



	public SysAttachment get(String id) {
		return super.fetch(SysAttachment.class, id);
	}
   	

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysAttachment.class, cri);
		}
		return 0;
	}

	public SysAttachment checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysAttachment.class,cri);
	}

	private SimpleCriteria getCri(SysAttachment obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSourceFilePath())) {
			cri.where().andEquals("source_file_path", obj.getSourceFilePath());
		}
		if(StringTool.isNotNull(obj.getFileType())) {
			cri.where().andEquals("file_type", obj.getFileType());
		}
		if(StringTool.isNotNull(obj.getSourceFileSize())) {
			cri.where().andEquals("source_file_size", obj.getSourceFileSize());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		return cri;
	}


}