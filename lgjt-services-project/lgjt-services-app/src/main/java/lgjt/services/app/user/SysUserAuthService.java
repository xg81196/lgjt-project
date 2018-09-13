package lgjt.services.app.user;

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
import lgjt.domain.app.user.SysUserAuth;
/**
 * show 用户认证信息类.
 * <p>Title: SysUserAuthService</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Log4j
@IocBean
public class SysUserAuthService extends CustomService {


	/**
	 * show 查询用户认证信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param obj 用户认证对象，对应lgjt.domain.app.user中的实体类SysUserAuth
	 * @return 查询结果对象
	 */
	public PageResult<SysUserAuth> queryPage(SysUserAuth obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("real_name", obj.getRealName());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getPhoneNumber())) {
			cri.where().andEquals("phone_number", obj.getPhoneNumber());
		}
		if(StringTool.isNotNull(obj.getOrgId())) {
			cri.where().andEquals("org_id", obj.getOrgId());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("id_no", obj.getIdNo());
		}
		if(StringTool.isNotNull(obj.getIdType())) {
			cri.where().andEquals("id_type", obj.getIdType());
		}
		if(StringTool.isNotNull(obj.getUnionId())) {
			cri.where().andEquals("UNION_ID", obj.getUnionId());
		}
		if(StringTool.isNotNull(obj.getJobNo())) {
			cri.where().andEquals("job_no", obj.getJobNo());
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

		return super.queryPage(SysUserAuth.class, obj, cri);
	}

	/**
	 * show 查询用户认证信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param obj 用户认证对象，对应lgjt.domain.app.user中的实体类SysUserAuth
	 * @return 查询结果集合
	 */
	public List<SysUserAuth> query(SysUserAuth obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getUserId())) {
			cri.where().andEquals("user_id", obj.getUserId());
		}
		if(StringTool.isNotNull(obj.getRealName())) {
			cri.where().andEquals("real_name", obj.getRealName());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getPhoneNumber())) {
			cri.where().andEquals("phone_number", obj.getPhoneNumber());
		}
		if(StringTool.isNotNull(obj.getOrgId())) {
			cri.where().andEquals("org_id", obj.getOrgId());
		}
		if(StringTool.isNotNull(obj.getIdNo())) {
			cri.where().andEquals("id_no", obj.getIdNo());
		}
		if(StringTool.isNotNull(obj.getIdType())) {
			cri.where().andEquals("id_type", obj.getIdType());
		}
		if(StringTool.isNotNull(obj.getUnionId())) {
			cri.where().andEquals("UNION_ID", obj.getUnionId());
		}
		if(StringTool.isNotNull(obj.getJobNo())) {
			cri.where().andEquals("job_no", obj.getJobNo());
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
		return super.query(SysUserAuth.class, cri);
	}

	/**
	 * show 通过id获取用户认证信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param id 用户id
	 * @return id对应的用户认证信息
	 */
   	public SysUserAuth get(String id) {
		return super.fetch(SysUserAuth.class, id);
	}

	/**
	 * show 通过id删除用户认证信息.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param ids 用户id
	 * @return 删除个数
	 */
	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysUserAuth.class, cri);
		}
		return 0;
	}

	/**
	 * show 检验是否存在id.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param value id的值
	 * @return 当前id下的用户认证信息
	 */
	public SysUserAuth checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUserAuth.class,cri);
	}
	/**
	 * show 检验是否存在userId.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param value userId的值
	 * @return 当前userId下的用户认证信息
	 */
	public SysUserAuth checkUser_id(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_id",value);
		return super.fetch(SysUserAuth.class,cri);
	}
	/**
	 * show 判断是否有待审核的记录.
	 * <p>Title: isExist</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月1日  
	 * @param userId 用户id 
	 * @param status 状态
	 * @return
	 */
	public int isExist(String userId,int status) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_id",userId);
		cri.where().andEquals("status",status);
		return  super.count(SysUserAuth.class, cri);
	}

	/**
	 * show 根据用户ID 获取关系.
	 * @author daijiaqi  
	 * @date 2018年5月1日
	 * @param userId 用户id
	 * @return 用户认证信息
	 */
	public List<SysUserAuth> getSysUserAuthByUserId(String userId){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("user_id",userId);
		cri.desc("crt_time");
		return super.query(SysUserAuth.class,cri);
	}
}