package lgjt.domain.books.art;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("lg_user_like")
public class BookLike extends BaseEntity {


		/**
	 *书香ID
	 */
		@Column("art_id")
		private String artId;
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
	 * 评分
	 */
		@Column("score")
		private Integer score;
		/**
	 * 点赞日期
	 */
		@Column("vote_date")
		private java.util.Date voteDate;
		/**
	 * 扩展字段1
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