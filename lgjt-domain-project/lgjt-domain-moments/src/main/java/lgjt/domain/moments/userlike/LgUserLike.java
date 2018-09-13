package lgjt.domain.moments.userlike;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("lg_user_like")
public class LgUserLike extends BaseEntity {


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
	 * 用户名
	 */
		@Column("user_name")
		private String userName;
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