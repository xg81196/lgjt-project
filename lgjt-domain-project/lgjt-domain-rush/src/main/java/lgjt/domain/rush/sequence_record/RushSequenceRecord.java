package lgjt.domain.rush.sequence_record;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("rush_sequence_record")
public class RushSequenceRecord extends BaseEntity {

	public static final int STATE_NO_FINISH=0;//未完成

	public static final int STATE_FAIL=2;//失败

	public static final int STATE_FINISH=1;//完成

		/**
	 * 工种id
	 */
		@Column("sequence_id")
		private String sequenceId;

	    @Column("sequence_name")
		private String sequenceName;
		/**
	 * 用户名
	 */
		@Column("user_name")
		private String userName;
		/**
	 * 闯关开始时间
	 */
		@Column("rush_start_time")
		private java.util.Date rushStartTime;
		/**
	 * 闯关结束时间
	 */
		@Column("rush_end_time")
		private java.util.Date rushEndTime;
		/**
	 * 闯关用时
	 */
		@Column("cost_time")
		private Integer costTime;
		/**
	 * 闯关次数
	 */
		@Column("rush_count")
		private Integer rushCount;
		/**
	 * 对题数
	 */
		@Column("right_quantity")
		private Integer rightQuantity;
		/**
	 * 剩余题数
	 */
		@Column("remain_ques")
		private Integer remainQues;
		/**
	 * 错题数
	 */
		@Column("fault_quantity")
		private Integer faultQuantity;
		/**
	 * 答对题目ID。逗号","分隔
	 */
		@Column("right_question_id")
		private String rightQuestionId;
		/**
	 * 答错题目ID。逗号","分隔
	 */
		@Column("fault_question_id")
		private String faultQuestionId;
		/**
	 * 答错题目ID。错题答案信息。3个减号"---"分隔
	 */
		@Column("fault_question_ids")
		private String faultQuestionIds;
		/**
	 * 分值
	 */
		@Column("score")
		private Double score;
		/**
	 * 状态。状态。0，未完成，1成功，2失败
	 */
		@Column("state")
		private Integer state;

	/**
	 * 闯关描述。
	 */
	@Column("mark")
	private String mark;

		/**
	 * 公司id
	 */
		@Column("company_id")
		private String companyId;

	@Column("company_name")
	private String companyName;
		/**
	 * 
	 */
		@Column("extend1")
		private String extend1;
		/**
	 * 
	 */
		@Column("extend2")
		private String extend2;
		/**
	 * 
	 */
		@Column("extend3")
		private String extend3;
		/**
	 * 
	 */
		@Column("extend4")
		private Integer extend4;
		/**
	 * 
	 */
		@Column("extend5")
		private Integer extend5;
		/**
	 * 
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