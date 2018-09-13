package lgjt.domain.help;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("lg_help_info")
public class LgHelpInfo extends BaseEntity {



	/**
	 * 姓名
	 */
	@Column("rel_name")
	private String rel_name;

		/**
	 * 省份证号
	 */
		@Column("id_no")
		private String idNo;
		/**
	 * 类型
	 */
		@Column("type")
		private String  type;
		/**
	 * 二级类型
二级类型
	 */
		@Column("type2")
		private String type2;
		/**
	 * 附件id
	 */
		@Column("attach_id")
		private String attachId;
		/**
	 * 申请事由
	 */
		@Column("cause")
		private String cause;
		/**
	 * 
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 
	 */
		@Column("crt_time")
		private java.sql.Date crt_time;
		/**
	 * 
	 */
		@Column("crt_ip")
		private String crtIp;
		/**
	 * 
	 */
		@Column("upd_user")
		private String updUser;
		/**
	 * 
	 */
		@Column("upd_time")
		private java.util.Date updTime;
		/**
	 * 
	 */
		@Column("upd_ip")
		private String updIp;
}