package lgjt.domain.backend.secret;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("sys_secret_key")
public class SysSecretKey extends BaseEntity {

			/**
			启用 
			*/
			public static final Integer STATUS_ENABLED   = 0 ;
			/**
			禁用 
			*/
			public static final Integer STATUS_DISABLE   = 1 ;

		/**
	 * 系统名称。
	 */
		@Column("system_name")
		private String systemName;
		/**
	 * 系统秘钥。
	 */
		@Column("secret_key")
		private String secretKey;
		/**
	 * 状态。0：启用；1：禁用
取值范围：#[enabled:0:启用$disable:1:禁用]#
	 */
		@Column("status")
		private Integer status;
		/**
	 * 所属企业。
	 */
		@Column("org_id")
		private String orgId;
		/**
	 * 对接系统负责人名称。
	 */
		@Column("leader_name")
		private String leaderName;
		/**
	 * 对接系统负责人电话。
	 */
		@Column("leader_phone")
		private String leaderPhone;
		/**
	 * 对接系统负责人QQ。
	 */
		@Column("leader_qq")
		private String leaderQq;
		/**
	 * 对接系统负责人微信。
	 */
		@Column("leader_wechat")
		private String leaderWechat;
		/**
	 * 对接系统负责人性别。
	 */
		@Column("leader_sex")
		private Integer leaderSex;
		/**
	 * 对接系统模板ID。
	 */
		@Column("template_id")
		private String templateId;
		/**
	 * 对接系统模板版本号。
	 */
		@Column("template_version")
		private String templateVersion;
		/**
	 * 对接系统版本号。
	 */
		@Column("system_version")
		private String systemVersion;
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