package lgjt.vo.app.user;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import lgjt.domain.app.user.SysUser;

/**
 * 用户信息转换类
 * <p>Title: SysUser</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Data
public class SysUserVo extends SysUser {

	/**
	 * 组织名称
	 */
	private String orgValue;


	/**
	 * 企业名称
	 */
	private String comValue;

	/**
	 * 工会名称
	 */
	private String unionValue;
	/**
	 * 设置性别
	 */
	private String sexValue;

//	/**
//	 * 出生日期
//	 */
//	private String birthDateValue;
	/**
	 * 民族名称
	 */
	private String nationValue;
	/**
	 * 就业状况
	 */
	private String workStatusValue;
	/**
	 * 户籍类型
	 */
	private String householdRegisterValue;
	/**
	 * 有效证件类别
	 */
	private String idTypeValue;
	/**
	 * 学历
	 */
	private String educationValue;

	/**
	 * 技術等級
	 */
	private String technicalLevelValue;

	/**
	 * 会计变化类型
	 */
	private String membershipChangeTypeValue;

	/**
	 *会计变化原因
	 * @return
	 */
	private String membershipChangeReasonValue;
	/**
	 * 授权状态
	 */
	private String authStatus;
	//授权状态名称
	private String authStatusValue;
//	会计变化日期
//	会计变化原因
	/**
	 * 用户id。
	 */

	private String userId;
}