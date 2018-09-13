package lgjt.domain.rush.ques;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 
*/
@Data
@Table("ques_classify")
public class QuesClassify extends BaseEntity {
		
	/**
	 * 上级id，平台
	 */
	public static final String SUPER_ID_1 = "hangye";
	/**
	 * 上级id，企业
	 */
	public static final String SUPER_ID_2 = "qiye";
	/**
	 * 平台
	 */
	public static final String TYPE_0 = "0";
	/**
	 * 企业
	 */
	public static final String TYPE_1 = "1";
	
	/**
	 * 根节点-1
	 */
	public static final String ROOT = "-1";
	/**
	 * 允许试题征集0
	 */
	public static final int ISCOLLECT_ON = 0;
	/**
	 * 不允许试题征集1
	 */
	public static final int ISCOLLECT_OFF = 1;
	/**
	 * 状态0，启用
	 */
	public static final int STATE_ON = 0;
	/**
	 * 状态1，禁用
	 */
	public static final int STATE_OFF  = 1;
	/**
	 * 父ID。
	 */
	@Column("super_id")
	private String superId;
	/**
	 * 企业ID。
	 */
	@Column("company_id")
	private String companyId;
	/**
	 * 题库类型
	 */
	@Column("type")
	private String type;
	/**
	 * 题库名称。
	 */
	@Column("name")
	private String name;
	/**
	 * 题库状态。0启用1 禁用
	 */
	@Column("state")
	private Integer state;
	/**
	 * 题库描述。
	 */
	@Column("mark")
	private String mark;
	/**
	 * 是否允许征集。0允许试题征集，1不允许试题征集
	 */
	@Column("is_collect")
	private Integer isCollect;
	/**
	 * 系统标识。所属系统
	 */
	@Column("remainsystem")
	private String remainsystem;
	
	/**
	 * 试题数量
	 */
	@Readonly
	@Column
	private Integer quesNum;
	
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
	
	List<QuesClassify> list = new ArrayList<QuesClassify>();

	/*
     * 当前用户用户名
     */
    @Column
    @Readonly 
    private String userName;

	/*
     * 组合查询上级name1
     */
    @Column
    @Readonly 
    private String name1;

	/*
     * 组合查询上级id1
     */
    @Column
    @Readonly 
    private String id1;

	/*
     * 组合查询上级name2
     */
    @Column
    @Readonly 
    private String name2;

	/*
     * 组合查询上级id2
     */
    @Column
    @Readonly 
    private String id2;
    
}