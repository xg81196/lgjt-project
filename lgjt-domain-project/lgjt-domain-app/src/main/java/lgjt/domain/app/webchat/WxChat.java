package lgjt.domain.app.webchat;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("wx_chat")
public class WxChat extends BaseEntity {

			/**
			启用 
			*/
			public static final Integer STATUS_ENABLED   = 0 ;
			/**
			禁用 
			*/
			public static final Integer STATUS_DISABLE   = 1 ;

		/**
	 * 令牌
	 */
		@Column("token")
		private String token;
		/**
	 * 微信公众号ID
	 */
		@Column("app_id")
		private String appId;
		/**
	 * 微信加密
	 */
		@Column("app_secre")
		private String appSecre;
		/**
	 * 微信公众号名称。
	 */
		@Column("name")
		private String name;
		/**
	 * 状态。0：启用；1：禁用
取值范围：#[enabled:0:启用$disable:1:禁用]#
	 */
		@Column("status")
		private Integer status;
		/**
	 * 排序。
	 */
		@Column("sort")
		private Integer sort;
		/**
	 * 备注。
	 */
		@Column("remark")
		private String remark;
		/**
	 * 扩展
	 */
		@Column("type")
		private Integer type;
		/**
	 * 上级id
	 */
		@Column("super_id")
		private String superId;
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