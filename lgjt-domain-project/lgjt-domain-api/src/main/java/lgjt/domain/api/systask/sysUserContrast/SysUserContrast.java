package lgjt.domain.api.systask.sysUserContrast;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import com.ttsx.platform.nutz.pojo.BaseEntity;

import lombok.Data;
/**
 * 
 * <p>Title: SysUserContrast</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Data
@Table("sys_user_contrast")
public class SysUserContrast extends BaseEntity {

	@Column("app_id")
	private String appId;
		/**
	 * 企业用户ID。
	 */
		@Column("company_user_id")
		private String companyUserId;
		/**
	 * 用户名。标识系统用户账号
	 */
		@Column("user_name")
		private String userName;
		/**
	 * 系统用户ID。标识系统用户ID
	 */
		@Column("sys_user_id")
		private String sysUserId;
		/**
	 * 企业唯一标识。标识企业
	 */
		@Column("company_code")
		private String companyCode;
		/**
	 * 扩展字段1。扩展字段
	 */
		@Column("extend1")
		private String extend1;
		/**
	 * 扩展字段2。扩展字段
	 */
		@Column("extend2")
		private String extend2;
		/**
	 * 扩展字段3。扩展字段
	 */
		@Column("extend3")
		private String extend3;
		/**
	 * 扩展字段4。扩展字段
	 */
		@Column("extend4")
		private Integer extend4;
		/**
	 * 扩展字段5。扩展字段
	 */
		@Column("extend5")
		private Integer extend5;
		/**
	 * 扩展字段6。扩展字段
	 */
		@Column("extend6")
		private java.util.Date extend6;
		/**
	 * 创建人。
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 创建时间。年-月-日 时:分:秒
	 */
		@Column("crt_time")
		private java.util.Date crtTime;
		/**
	 * 创建IP。示例：192.168.100.172
	 */
		@Column("crt_ip")
		private String crtIp;
		/**
	 * 最后修改人。
	 */
		@Column("upd_user")
		private String updUser;
		/**
	 * 最后修改时间。年-月-日 时:分:秒
	 */
		@Column("upd_time")
		private java.util.Date updTime;
		/**
	 * 最后修改IP。示例：192.168.100.172
	 */
		@Column("upd_ip")
		private String updIp;
}