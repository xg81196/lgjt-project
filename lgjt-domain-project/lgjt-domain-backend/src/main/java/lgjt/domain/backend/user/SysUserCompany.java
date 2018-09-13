package lgjt.domain.backend.user;
	
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import com.ttsx.platform.nutz.pojo.BaseEntity;

import lombok.Data;
/**
 * 企业用户表
 * @author daijiaqi
 *
 */
@Data
@Table("sys_user_company")
public class SysUserCompany extends BaseEntity {


		/**
	 * 用户ID。关联QGNG_USER_SYS.ID
	 */
		@Column("user_id")
		private String userId;
		/**
	 * 用户名。QGNG_USER_SYS.USER_NAME
	 */
		@Column("user_name")
		private String userName;
		/**
	 * 企业用户名。企业用户用户名
	 */
		@Column("com_username")
		private String comUsername;
		/**
	 * 企业标识。企业标识
	 */
		@Column("company_flag")
		private String companyFlag;
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