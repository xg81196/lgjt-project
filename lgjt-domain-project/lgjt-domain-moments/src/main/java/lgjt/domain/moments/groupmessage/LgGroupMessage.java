package lgjt.domain.moments.groupmessage;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("lg_group_message")
public class LgGroupMessage extends BaseEntity {

	/**
	 * 待审核
	 */
	public static final Integer VERIFYSTATUS_VERIFYING = 0;
	/**
	 * 通过
	 */
	public static final Integer VERIFYSTATUS_SECCESS = 1;
	/**
	 * 不通过
	 */
	public static final Integer VERIFYSTATUS_FAILURE = 2;

		/**
	 * 用户id
	 */
		@Column("user_id")
		private String userId;
	/**
	 * 用户头像
	 */
	@Column("user_profile")
	private String userProfile;
		/**
	 * 所属班组
	 */
		@Column("group_id")
		private String groupId;
	/**
	 * 所属班组名称
	 */
	@Column("group_name")
	private String groupName;
		/**
	 * 发送内容
	 */
		@Column("content")
		private String content;
		/**
	 * 图片或视频
	 */
		@Column("attach_id")
		private String attachId;
		/**
	 * 图片类型 1视频2图片
	 */
		@Column("type")
		private Integer type;
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

	/**
	 * 审核状态。0：待审核；1：通过；2：不通过
	 * 取值范围：#[verifying:0:待审核$seccess:1:通过$failure:2:不通过]#
	 */
	@Column("verify_status")
	private Integer verifyStatus;

	/**
	 * 审核人
	 */
	@Column("verify_user")
	private String verifyUser;
	/**
	 * 状态， 0 禁用 1启用
	 */
	@Column("status")
	private Integer status;

	@Column("real_name")
	private String realName;
}