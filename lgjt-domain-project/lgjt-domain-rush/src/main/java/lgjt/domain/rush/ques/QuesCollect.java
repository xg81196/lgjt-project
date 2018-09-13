package lgjt.domain.rush.ques;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
* 
*/
@Data
@Table("ques_collect")
public class QuesCollect extends BaseEntity {

	/**
	 * 所属题库。
	 */
	@Column("classify_id")
	private String classifyId;
	/**
	 * 试题难度。试题难度
	 */
	@Column("difficulty_id")
	private String difficultyId;
	/**
	 * 试题类型。题目类型，单选、多选、判断、填空、主观
	 */
	@Column("type")
	private Integer type;
	/**
	 * 题目文本。题目
	 */
	@Column("question")
	private String question;
	/**
	 * 答案文本。答案+ 选项
	 */
	@Column("answer")
	private String answer;
	
	/**
	 * 系统标识。所属系统,
	 */
	@Column("remainsystem")
	private String remainsystem;
	/**
	 * 上传人。
	 */
	@Column("upload_user_name")
	private String uploadUserName;
	/**
	 * 是否通过审核。0：未审核 1：审核通过 2：审核未通过
	 */
	@Column("verify")
	private Integer verify;
	
	public static Integer VERIFY_WAIT = 0;
	public static Integer VERIFY_YES = 1;
	public static Integer VERIFY_NO = 2;
	/**
	 * 审核人。
	 */
	@Column("verify_user_name")
	private String verifyUserName;
	/**
	 * 是否已奖励过积分。0：未奖励 1:已奖励
	 */
	@Column("is_reward")
	private Integer isReward;
	/**
	 * 审核时间。
	 */
	@Column("verify_time")
	private Date verifyTime;
	/**
	 * 奖励积分。
	 */
	@Column("reward")
	private Long reward;
	/**
	 * 题库名称。
	 */
	@Readonly
	@Column("classify_name")
	private String classifyName;
	
	/*
     * 创建人。
     */
    @Column("crt_user")
	private String crtUser;
	/*
     * 创建时间。
     */
    @Column("crt_time")
	private Date crtTime;
	/*
     * 创建IP。
     */
    @Column("crt_ip")
	private String crtIp;
	/*
     * 最后修改人。
     */
    @Column("upd_user")
	private String updUser;
	/*
     * 最后修改时间。
     */
    @Column("upd_time")
	private Date updTime;
	/*
     * 最后修改IP。
     */
    @Column("upd_ip")
	private String updIp;
	
	private String sequenceId;
}