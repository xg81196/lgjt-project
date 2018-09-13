package lgjt.services.api.user;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.api.user.SysUser;

/**
 * 用户管理服务类
 * @author daijiaqi
 */
@Log4j
@IocBean
public class SysUserService extends BaseService {

	/**
	 * 分页查询
	 * @param sysUser
	 * @return 分页集合
	 */
	public PageResult<SysUser> queryPage(SysUser sysUser) {
		SimpleCriteria cri = getSimpleCriteria(sysUser);
		return super.queryPage(SysUser.class, sysUser, cri);
	}
	/**
	 * 根据对象获取查询条件
	 * @param sysUser
	 * @return 查询对象
	 */
	private SimpleCriteria getSimpleCriteria(SysUser sysUser) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(sysUser.getUserName())) {
			cri.where().andEquals("user_name", sysUser.getUserName());
		}
		if(StringTool.isNotNull(sysUser.getPassword())) {
			cri.where().andEquals("user_password", sysUser.getPassword());
		}
		if(StringTool.isNotNull(sysUser.getRealName())) {
			cri.where().andEquals("real_name", sysUser.getRealName());
		}
		
		if(StringTool.isNotNull(sysUser.getEmail())) {
			cri.where().andEquals("email", sysUser.getEmail());
		}
		if(StringTool.isNotNull(sysUser.getIdNo())) {
			cri.where().andEquals("id_no", sysUser.getIdNo());
		}
		if(StringTool.isNotNull(sysUser.getComId())) {
			cri.where().andEquals("com_id", sysUser.getComId());
		}
		
		if(StringTool.isNotNull(sysUser.getUnionId())) {
			cri.where().andEquals("union_id", sysUser.getUnionId());
		}
		if(StringTool.isNotNull(sysUser.getNumberNo())) {
			cri.where().andEquals("number_no", sysUser.getNumberNo());
		}
		if(StringTool.isNotNull(sysUser.getJob())) {
			cri.where().andEquals("job", sysUser.getJob());
		}
		if(StringTool.isNotNull(sysUser.getJobNo())) {
			cri.where().andEquals("job_no", sysUser.getJobNo());
		}

		if(StringTool.isNotNull(sysUser.getWorkYear())) {
			cri.where().andEquals("work_year", sysUser.getWorkYear());
		}
		if(StringTool.isNotNull(sysUser.getWechat())) {
			cri.where().andEquals("wechat", sysUser.getWechat());
		}
		if(StringTool.isNotNull(sysUser.getAlipay())) {
			cri.where().andEquals("alipay", sysUser.getAlipay());
		}

		return cri;
	}

	/**
	 * 根据主键
	 * @param id 用户表ID
	 * @return 用户对象
	 */
   	public SysUser get(String id) {
		return super.fetch(SysUser.class, id);
	}
   
	public SysUser checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUser.class,cri);
	}
	
	public SysUser getByUserName(String userName) {
		return super.fetch(SysUser.class, Cnd.where("user_name","=",userName).and("status", "=", SysUser.STATUS_ENABLED));
	}

}