package lgjt.services.app.user;

import lgjt.domain.app.user.SysUserVo;
import lgjt.vo.app.user.SysUserOrgVo;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.CustomService;
import lgjt.domain.app.user.SysUser;

import java.util.List;

/**
 * show 用户管理服务类.
 * @author daijiaqi
 * @date 2018年5月1日
 */
@Log4j
@IocBean
public class SysUserService extends CustomService {

	/**
	 * show 分页查询.
	 * @author daijiaqi
     * @date 2018年5月1日
	 * @param sysUser 用户对象，对应lgjt.domain.app.user中的实体类SysUser
	 * @return 分页集合
	 */
	public PageResult<SysUser> queryPage(SysUser sysUser) {
		SimpleCriteria cri = getSimpleCriteria(sysUser);
		return super.queryPage(SysUser.class, sysUser, cri);
	}
	/**
	 * show 根据对象获取查询条件.
	 * @author daijiaqi
     * @date 2018年5月1日
	 * @param sysUser 用户对象，对应lgjt.domain.app.user中的实体类SysUser
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
	 * show 根据主键查询用户信息
	 * @author daijiaqi
     * @date 2018年5月1日
	 * @param id 用户表ID
	 * @return 用户对象
	 */
   	public SysUser get(String id) {
		return super.fetch(SysUser.class, id);
	}
   
	/**
	 * show 检验是否存在id.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param value id的值
	 * @return 当前id下的用户对象
	 */
	public SysUser checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysUser.class,cri);
	}

	/**
	 * show 根据用户名获取用户对象.
	 * @author zhaotianyi
     * @date 2018年08月25日
	 * @param userName 用户名
	 * @return 用户对象
	 */
	public SysUserVo getByUserName(String userName) {
		//修改
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("su.user_name", userName);

		List<SysUserVo> sysUserVos = super.query("get.login.userinfo", SysUserVo.class, cri);

		if(sysUserVos.size() > 0) {
			return sysUserVos.get(0);
		} else {
			return null;
		}
	}

	/**
	 * show 根据电话号获取用户.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param phoneNumber 手机号
	 * @return 用户对象
	 */
	public SysUser getByPhoneNumber(String phoneNumber) {
		return super.fetch(SysUser.class, Cnd.where("phone_number","=",phoneNumber));
	}


	/**
	 * show 根据用户名或者手机号获取用户个数.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param userName 用户名
	 * @param phoneNumber 手机号
	 * @return 命中的用户个数
	 */
	public int getByUserNameOrPhoneNumber(String userName,String phoneNumber) {
		return	super.count(SysUser.class, Cnd.where("user_name","=",userName).or("phone_number","=",phoneNumber));
	}

	/**
	 * show 排除当前用户包含此手机号的记录条数.
	 * @author daijiaqi  
     * @date 2018年5月1日
	 * @param userId 用户名
	 * @param phoneNumber 手机号
	 * @return 条数
	 */
	public int getPhoneNumberCountExcludeUserId(String userId,String phoneNumber) {
		return	super.count(SysUser.class, Cnd.where("phone_number","=",phoneNumber).and("id","!=",userId));
	}
	
	/**
	 * show 通过手机号获取用户信息.
	 * @author wangyu  
     * @date 2018年5月1日
	 * @param phoneNumber 手机号
	 * @return 用户对象
	 */
	public SysUser getIdNoByPhoneNumber(String phoneNumber){
		if(StringTool.isNotNull(phoneNumber)){
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("phoneNumber",phoneNumber);
			return super.fetch(SysUser.class, cri);
		}
		return null;
	}
	
	/**
	 * show 通过身份证号获取用户信息.
	 * @author wangyu  
     * @date 2018年5月1日
	 * @param idNo 身份证号
	 * @return 用户对象
	 */
	public SysUser getPhoneNumberByIdNo(String idNo){
		if(StringTool.isNotNull(idNo)){
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("idNo",idNo);
			return super.fetch(SysUser.class, cri);
		}
		return null;
	}

    /**
     * 微信号绑定
     * @param userId
     * @param openId
     * @return
     */
	public int bindOpenIdByUserId(String userId,String openId){
		SysUser sysUser =get(userId);
		if(sysUser!=null){
		    if(sysUser.getOpenIds()==null){
                sysUser.setOpenIds(openId);
            }else{
                sysUser.setOpenIds(sysUser.getOpenIds().trim()+","+openId.trim());
            }

		    return	super.updateIgnoreNull(sysUser);
		}
		return 0;
	}


	public SysUser getSysUserByOpenId(String openId){
		SimpleCriteria sc =Cnd.cri();
		sc.where().andLike("open_ids",openId);
		return super.fetch(SysUser.class,sc);
	}


	public PageResult<SysUserOrgVo> queryUserAndOrgInfo(SysUserOrgVo userOrg) {
		SimpleCriteria cri = Cnd.cri();
		if (StringTool.isNotEmpty(userOrg.getUserName())) {
			cri.where().andLike("user_name", userOrg.getUserName());
			return super.queryPage("sys.user.queryUserAndOrgInfoForUser", SysUserOrgVo.class, userOrg, cri);
		} else {
			return super.queryPage("sys.user.queryUserAndOrgInfo", SysUserOrgVo.class, userOrg, cri);
		}
	}


	/**
	 * 根据ID查询首页用户信息
	 * @param ids
	 * @return
	 */
	public List<SysUser> queryUserList(String ids){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andIn("id",ids.split(","));
		return super.query(SysUser.class,cri);
	}



	public List<SysUser> queryUserForOrgList(String orgs){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andIn("org_id",orgs.split(","));
		return super.query(SysUser.class,cri);
	}

}