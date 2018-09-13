package lgjt.services.backend.app;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.backend.app.AppVersion;
import lgjt.domain.backend.app.SysAttachment;

import java.util.List;

/***********************
 *Author:王昕禹
 ***********************/
@IocBean
@Log4j
public class SysAppVersionService extends BaseService{
	
	public static final String LEARN_APP_JSON = "learn.app.json";
	public static final String LEARN_APP_DOWN = "learn.app.down";
	/**
	 * 删除APP安装包
	 * @param ids
	 * @return
	 */
	public int delete(String ids){
	   try{
		   if(StringTool.isNotNull(ids)){
			   SimpleCriteria cri=Cnd.cri();
			   cri.where().andIn("id", ids.split(","));
			   return super.delete(AppVersion.class, cri);
		   }else{
			   return 0;
		   }
	   }catch(Exception e){
		   throw new RuntimeException(e.getMessage());
	   }
   }
	
	/**
	 * 查询APP安装包
	 */
	public PageResult<AppVersion> queryPage(AppVersion obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getId())) {
			cri.where().andEquals("id", obj.getId());
		}
		cri.desc("crt_time");
		return super.queryPage(AppVersion.class, obj, cri);
	}
	
	/**
	 * 查询json
	 */
	public AppVersion queryJson(int adviceType) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("advice_type", adviceType);
		cri.where().andEquals("state", 1);
		cri.desc("upd_time");
		return super.fetch(AppVersion.class, cri);
	}
	
	/**
	 * 获取最新的app信息
	 */
	public AppVersion getNewest(AppVersion obj) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("advice_type", obj.getAdviceType());
		cri.where().andEquals("state", 1);
		cri.desc("upd_time");
		return super.fetch(AppVersion.class, cri);
	}
	
	/**
	 * 下载APK
	 */
	public SysAttachment downloadApp(SysAttachment obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getId())) {
			cri.where().andEquals("id", obj.getId());
//			cri.where().andEquals("source_name", obj.getSourceName());
			cri.getOrderBy().desc("crt_time");
			SysAttachment sa = super.fetch(SysAttachment.class, cri);
			return sa;
		}
		return null;
	}

	public List<SysAttachment> queryApp() {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andLike("source_name", "apk");
		cri.getOrderBy().desc("crt_time");
		List<SysAttachment> sa = super.query(SysAttachment.class, cri);
		return sa;
	}
}
