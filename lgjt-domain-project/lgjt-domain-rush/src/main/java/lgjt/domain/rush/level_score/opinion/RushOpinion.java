package lgjt.domain.rush.level_score.opinion;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("rush_opinion")
public class RushOpinion extends CaseEntity {
	
	public static final Integer ANONYMOUS_YES = 1;
	public static final Integer ANONYMOUS_NO = 0;

	public static final Integer STATE_PASS = 1;
	public static final Integer STATE_NOPASS = 0;
	/**
	 * 用户名。
	 */
	@Column("user_name")
	private String userName;
	/**
	 * 是否匿名。0不匿名，1匿名
	 */
	@Column("anonymous")
	private Integer anonymous;
	/**
	 * 反馈内容。
	 */
	@Column("content")
	private String content;
	/**
	 * 意见回复内容。
	 */
	@Column("reply")
	private String reply;
	/**
	 * 回复人。
	 */
	@Column("reply_user_name")
	private String replyUserName;
	/**
	 * 回复时间。
	 */
	@Column("reply_time")
	private java.util.Date replyTime;
	/**
	 * 问题类型。0试题问题，1系统问题
	 */
	@Column("type")
	private Integer type;
	/**
	 * 关联主键。type=0 时，存放试题ID
	 */
	@Column("foreign_id")
	private String foreignId;

	/**
	 * 题干
	 */
	@Column("title")
	private String title;
}