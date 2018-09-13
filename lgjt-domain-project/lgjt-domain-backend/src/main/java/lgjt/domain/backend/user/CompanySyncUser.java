package lgjt.domain.backend.user;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import com.ttsx.platform.nutz.pojo.BaseEntity;

import lombok.Data;
/**
* 
*/
@Data
@Table("company_sync_user")
public class CompanySyncUser extends BaseEntity {


		/**
	 * 用户名。用户名
	 */
		@Column("user_name")
		private String userName;
		/**
	 * 是否实名。是否实名0是 1否
	 */
		@Column("realname_flag")
		private Integer realnameFlag;
		/**
	 * 身份证号。身份证号
	 */
		@Column("id_no")
		private String idNo;
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