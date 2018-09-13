package lgjt.domain.rush.sequence_record;

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
@Table("rush_user_record")
public class RushUserRecord extends BaseEntity {
	/**
	 *闯关未完成
	 */
	public static final int STATE_NOTFINISH = 0;
	
	/**
	 * 闯关完成，成功
	 */
	public static final int STATE_FINISH = 1;
	
	/**
	 * 闯关失败
	 */
	public static final int STATE_FAIL = 2;

	//工种名称
	@Column("sequence_name")
	private String sequenceName;

	/**
	 * 用户名。
	 */
	@Column("user_name")
	private String userName;
	/**
	 * 工种序列ID。102
	 */
	@Column("sequence_id")
	private String sequenceId;
	/**
	 * 闯关次数。
	 */
	@Column("rush_count")
	private Integer rushCount;
	/**
	 * 答错次数。
	 */
	@Column("wrong_count")
	private Integer wrongCount;
	/**
	 * 成功次数。
	 */
	@Column("right_count")
	private Integer rightCount;
	/**
	 * 分数。
	 */
	@Column("score")
	private double score;
	/**
	 * 状态。闯关状态,0代表未成完 1代表已完成 2代表闯关失败
	 */
	@Column("state")
	private Integer state;
	
	/**
	 * 闯关用时。
	 */
	@Column("cost_time")
	private Integer costTime;
	/**
	 * 描述。
	 */
	@Column("mark")
	private String mark;

	@Column("company_id")
	private String companyId;

	@Column("company_name")
	private String companyName;
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
	//查询条件的开始时间和结束时间
	private Date startTime;
	
	private Date endTime;
}