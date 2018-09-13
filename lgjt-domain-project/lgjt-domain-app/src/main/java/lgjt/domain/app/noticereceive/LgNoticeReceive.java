package lgjt.domain.app.noticereceive;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("lg_notice_receive")
public class LgNoticeReceive extends BaseEntity {


		/**
	 * 消息id
	 */
		@Column("message_id")
		private String messageId;
		/**
	 * 用户id
	 */
		@Column("user_id")
		private String userId;
		/**
	 * 公司id
	 */
		@Column("company_id")
		private String companyId;
		/**
	 * 开始有效期
	 */
		@Column("begin_time")
		private java.util.Date beginTime;
		/**
	 * 截止有效期
	 */
		@Column("end_time")
		private java.util.Date endTime;
		/**
	 * 是否已读 0 未读 1 已读
	 */
		@Column("status")
		private Integer status;
		/**
	 * 
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 
	 */
		@Column("crt_time")
		private java.util.Date crtTime;
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