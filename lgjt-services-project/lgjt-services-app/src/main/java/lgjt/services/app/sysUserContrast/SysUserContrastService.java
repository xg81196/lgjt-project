package lgjt.services.app.sysUserContrast;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.CustomService;
import lgjt.domain.app.systask.sysUserContrast.SysUserContrast;
/**
 * show 用户对照表信息类.
 * <p>Title: SysUserContrastService</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Log4j
@IocBean
public class SysUserContrastService extends CustomService {


	/**
	 * show 查询用户对照信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param obj 用户对照对象，对应lgjt.domain.app.systask.sysUserContrast中的实体类SysUserContrast
	 * @return 查询结果对象
	 */
	public PageResult<SysUserContrast> queryPage(SysUserContrast obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getAppId())) {
			cri.where().andEquals("app_id", obj.getAppId());
		}
		if(StringTool.isNotNull(obj.getCompanyUserId())) {
			cri.where().andEquals("company_user_id", obj.getCompanyUserId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getSysUserId())) {
			cri.where().andEquals("sys_user_id", obj.getSysUserId());
		}
		if(StringTool.isNotNull(obj.getCompanyCode())) {
			cri.where().andEquals("company_code", obj.getCompanyCode());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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

		return super.queryPage(SysUserContrast.class, obj, cri);
	}

	/**
	 * show 查询用户对照信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param obj 用户对照对象，对应lgjt.domain.app.systask.sysUserContrast中的实体类SysUserContrast
	 * @return 查询结果集合
	 */
	public List<SysUserContrast> query(SysUserContrast obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getAppId())) {
			cri.where().andEquals("app_id", obj.getAppId());
		}
		if(StringTool.isNotNull(obj.getCompanyUserId())) {
			cri.where().andEquals("company_user_id", obj.getCompanyUserId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getSysUserId())) {
			cri.where().andEquals("sys_user_id", obj.getSysUserId());
		}
		if(StringTool.isNotNull(obj.getCompanyCode())) {
			cri.where().andEquals("company_code", obj.getCompanyCode());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		return super.query(SysUserContrast.class, cri);
	}

	/**
	 * show 通过id获取用户对照信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param id 用户id
	 * @return id对应的用户对照信息
	 */
   	public SysUserContrast get(String id) {
		return super.fetch(SysUserContrast.class, id);
	}

	/**
	 * show 通过id删除用户对照信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param ids 用户id
	 * @return 删除个数
	 */
	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysUserContrast.class, cri);
		}
		return 0;
	}

	/**
	 * show 检验是否存在id.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param value id的值
	 * @return 当前id下的用户对照信息
	 */
	public SysUserContrast checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUserContrast.class,cri);
	}


	/**
	 * show 获取用户对照信息
	 * @author daijiaqi
     * @date 2018年5月1日
	 * @param obj 用户对照对象，对应lgjt.domain.app.systask.sysUserContrast中的实体类SysUserContrast
	 * @return 用户对照信息
	 */
	public SysUserContrast get(SysUserContrast obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getAppId())) {
			cri.where().andEquals("app_id", obj.getAppId());
		}
		if(StringTool.isNotNull(obj.getCompanyUserId())) {
			cri.where().andEquals("company_user_id", obj.getCompanyUserId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getSysUserId())) {
			cri.where().andEquals("sys_user_id", obj.getSysUserId());
		}
		if(StringTool.isNotNull(obj.getCompanyCode())) {
			cri.where().andEquals("company_code", obj.getCompanyCode());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		return super.fetch(SysUserContrast.class,cri);
	}


}