package lgjt.domain.books.art;

import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("lg_art")
@ToString
public class LgArt extends BaseEntity {

	@Name
	@Column("id")
	private String id;
		/**
	 * 标题
	 */
		@Column("title")
		private String title;
		/**
	 * 内容
	 */
		@Column("content")
		private String content;
		/**
	 * 单位ID
	 */
		@Column("org_id")
		private String orgId;
		/**
	 * 单位名称
	 */
		@Column("org_name")
		private String orgName;
		/**
	 * 信息类型,lg_letter_category
	 */
		@Column("category_id")
		private String categoryId;
		/**
	 * 审核状态 0：提交，1：通过，2：驳回
	 */
		@Column("status")
		private Integer status;
		/**
	 * 审核意见
	 */
		@Column("check_msg")
		private String checkMsg;
		/**
	 * 审核人
	 */
		@Column("check_user")
		private String checkUser;
		/**
	 * 作者，演出者
	 */
		@Column("author")
		private String author;
		/**
	 * 发布时间
	 */
		@Column("publish_time")
		private java.util.Date publishTime;
		/**
	 * 是否置顶 0：否，1：是
	 */
		@Column("is_top")
		private Integer isTop;
		/**
	 * 是否有效 0：是，1：否
	 */
		@Column("is_disable")
		private Integer isDisable;
		/**
	 * 是否删除 0：否，1：是
	 */
		@Column("is_delete")
		private Integer isDelete;
		/**
	 * 用户ID
	 */
		@Column("user_id")
		private String userId;
		/**
	 * 用户头像
	 */
		@Column("user_profile")
		private String userProfile;
		/**
	 * 真实姓名
	 */
		@Column("real_name")
		private String realName;
		/**
	 * 摄影扩展格式：{"shootAddress":"bj","shootTime":"2017"}
	 */
		@Column("extend1")
		private String extend1;
		/**
	 * 扩展字段2
	 */
		@Column("extend2")
		private String extend2;
		/**
	 * 扩展字段3
	 */
		@Column("extend3")
		private String extend3;
		/**
	 * 扩展字段4
	 */
		@Column("extend4")
		private Integer extend4;
		/**
	 * 扩展字段5
	 */
		@Column("extend5")
		private Integer extend5;
		/**
	 * 扩展字段6
	 */
		@Column("extend6")
		private java.util.Date extend6;
		/**
	 * 创建人
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 创建人IP
	 */
		@Column("crt_ip")
		private String crtIp;
		/**
	 * 创建时间
	 */
		@Column("crt_time")
		private java.util.Date crtTime;
		/**
	 * 修改人
	 */
		@Column("upd_user")
		private String updUser;
		/**
	 * 修改时间
	 */
		@Column("upd_time")
		private java.util.Date updTime;
		/**
	 * 修改人IP
	 */
		@Column("upd_ip")
		private String updIp;


	/**
	 * 是否点赞
	 */
	private Boolean likeStatus;

	/**
	 * 点赞数
	 */
	private Integer countlike;


}