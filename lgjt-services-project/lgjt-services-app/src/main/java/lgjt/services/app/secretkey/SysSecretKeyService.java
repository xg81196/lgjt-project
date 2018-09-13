package lgjt.services.app.secretkey;

import java.util.List;

import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.CustomService;
import lgjt.domain.app.systask.secretkey.SysSecretKey;

/**
 * show 秘钥业务类.
 * <p>Title: 秘钥业务类</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年4月23日
 */
@Log4j
@IocBean
public class SysSecretKeyService extends CustomService {
	
	/**
	 * show 查询全部.
	 * <p>Title: 查詢全部</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月23日  
	 * @param obj 秘钥对象，对应lgjt.domain.app.systask.secretkey中的实体类SysSecretKey
	 * @return 秘钥集合
	 */
	public List<SysSecretKey> query(SysSecretKey obj) {
		SimpleCriteria cri = getCondition(obj);
		return super.query(SysSecretKey.class, cri);
	}
	
	/**
	 * show 根据对象获取查询条件.
	 * @author daijiaqi
	 * @date 2016-7-1 下午12:11:16 
	 * @param obj 秘钥对象，对应lgjt.domain.app.systask.secretkey中的实体类SysSecretKey
	 * @return 查询条件
	 */
	public SimpleCriteria getCondition(SysSecretKey obj) {
		SimpleCriteria cri = super.getCommonCondition(obj);
		if(StringTool.isNotNull(obj.getSystemName())) {
			cri.where().andEquals("system_name", obj.getSystemName());
		}
		if(StringTool.isNotNull(obj.getSecretKey())) {
			cri.where().andEquals("secret_key", obj.getSecretKey());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getOrgId())) {
			cri.where().andEquals("org_id", obj.getOrgId());
		}
		if(StringTool.isNotNull(obj.getLeaderName())) {
			cri.where().andEquals("leader_name", obj.getLeaderName());
		}
		if(StringTool.isNotNull(obj.getLeaderPhone())) {
			cri.where().andEquals("leader_phone", obj.getLeaderPhone());
		}
		if(StringTool.isNotNull(obj.getLeaderQq())) {
			cri.where().andEquals("leader_qq", obj.getLeaderQq());
		}
		if(StringTool.isNotNull(obj.getLeaderWechat())) {
			cri.where().andEquals("leader_wechat", obj.getLeaderWechat());
		}
		if(StringTool.isNotNull(obj.getLeaderSex())) {
			cri.where().andEquals("leader_sex", obj.getLeaderSex());
		}
		if(StringTool.isNotNull(obj.getTemplateId())) {
			cri.where().andEquals("template_id", obj.getTemplateId());
		}
		if(StringTool.isNotNull(obj.getTemplateVersion())) {
			cri.where().andEquals("template_version", obj.getTemplateVersion());
		}
		if(StringTool.isNotNull(obj.getSystemVersion())) {
			cri.where().andEquals("system_version", obj.getSystemVersion());
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
		return cri;
	}

	/**
	 * show 通过id查询秘钥信息.
	 * @author daijiaqi
	 * @date 2016-7-1 下午12:11:16 
	 * @param id 秘钥id
	 * @return 秘钥信息
	 */
   	public SysSecretKey get(String id) {
		return super.fetch(SysSecretKey.class, id);
	}
}