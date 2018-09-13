package lgjt.services.app.config;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.app.config.SysConfig;


@Log4j
@IocBean
public class SysConfigService extends BaseService {


	public PageResult<SysConfig> queryPage(SysConfig obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getConfigModule())) {
			cri.where().andEquals("config_module", obj.getConfigModule());
		}
		if(StringTool.isNotNull(obj.getConfigName())) {
			cri.where().andEquals("config_name", obj.getConfigName());
		}
		if(StringTool.isNotNull(obj.getConfigValue())) {
			cri.where().andEquals("config_value", obj.getConfigValue());
		}
		if(StringTool.isNotNull(obj.getConfigRemark())) {
			cri.where().andEquals("config_remark", obj.getConfigRemark());
		}
		if(StringTool.isNotNull(obj.getConfigSort())) {
			cri.where().andEquals("config_sort", obj.getConfigSort());
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

		return super.queryPage(SysConfig.class, obj, cri);
	}

	public List<SysConfig> query(SysConfig obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getConfigModule())) {
			cri.where().andEquals("config_module", obj.getConfigModule());
		}
		if(StringTool.isNotNull(obj.getConfigName())) {
			cri.where().andEquals("config_name", obj.getConfigName());
		}
		if(StringTool.isNotNull(obj.getConfigValue())) {
			cri.where().andEquals("config_value", obj.getConfigValue());
		}
		if(StringTool.isNotNull(obj.getConfigRemark())) {
			cri.where().andEquals("config_remark", obj.getConfigRemark());
		}
		if(StringTool.isNotNull(obj.getConfigSort())) {
			cri.where().andEquals("config_sort", obj.getConfigSort());
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
		return super.query(SysConfig.class, cri);
	}

   	public SysConfig get(String id) {
		return super.fetch(SysConfig.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysConfig.class, cri);
		}
		return 0;
	}

	public SysConfig checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysConfig.class,cri);
	}
	public SysConfig checkConfig_name(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("config_name",value);
		return super.fetch(SysConfig.class,cri);
	}
	
	/**
	 * show 获取登录失败临时锁定次数，默认3.
	 * @author wangyu
	 * @date 2018/6/20
	 * @return 次数
	 */
	public int  getTemporaryLockNumber(){
		String configName="TEMPORARY_LOCK_NUMBER";
		int defaultValue=3;
		try{
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("config_name",configName);
			SysConfig sc  = super.fetch(SysConfig.class,cri);
			if(sc!=null){
				defaultValue= Integer.parseInt(sc.getConfigValue());
			}
		}catch(Exception e){}
		return  defaultValue;
	}


	/**
	 * show 获取登录失败永久锁定次数，默认10.
	 * @author wangyu
	 * @date 2018/6/20
	 * @return 次数
	 */
	public int  getPermanentLockNumber(){
		String configName="PERMANENT_LOCK_NUMBER";
		int defaultValue=10;
		try{
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("config_name",configName);
			SysConfig sc  = super.fetch(SysConfig.class,cri);
			if(sc!=null){
				defaultValue= Integer.parseInt(sc.getConfigValue());
			}
		}catch(Exception e){}
		return  defaultValue;
	}
	
	/**
	 * show 获取锁定时长，默认24小时.
	 * @author wangyu
	 * @date 2018/6/20
	 * @return 锁定时长
	 */
	public int  getLockTime(){
		String configName="LOCK_TIME";
		int defaultValue=24;
		try{
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("config_name",configName);
			SysConfig sc  = super.fetch(SysConfig.class,cri);
			if(sc!=null){
				defaultValue= Integer.parseInt(sc.getConfigValue());
			}
		}catch(Exception e){}
		return  defaultValue;
	}
	
	/**
	 * show 获取是否需要登录失败锁定机制开关.
	 * @author wangyu
	 * @date 2018/5/20
	 * @return on代表开启锁定机制；off代表关闭锁定机制
	 */
	public String  getLoginLockSwitch(){
		String configName="LOCK_SWITCH";
		String defaultValue="on";
		try{
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("config_name",configName);
			SysConfig sc  = super.fetch(SysConfig.class,cri);
			if(sc!=null && sc.getConfigValue() != null && sc.getConfigValue().equalsIgnoreCase("off")){
				defaultValue="off";
			}
		}catch(Exception e){}
		return  "off";
	}

}