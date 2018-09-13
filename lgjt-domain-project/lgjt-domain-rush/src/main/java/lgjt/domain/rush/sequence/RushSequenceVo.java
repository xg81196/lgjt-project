package lgjt.domain.rush.sequence;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("rush_sequence")
public class RushSequenceVo extends BaseEntity {
    /**
     * 过关条件要求类型   答题量
     */
    public final static int  PASSCONTITIONREQUEST_PASS = 1;

    /**
     * 答错扣分
     */
    public static  int ANSWERWRONGFLAG_YES=1;

    /**
     * 答错不扣分
     */
    public static int ANSWERWRONGFLAG_NO=0;


    /**
     * 是否统一分值 不统一
     */
    public final static int SCOREFLAG_NO=0;
    /**
     * 是否统一分值 统一
     */
    public final static int SCOREFLAG_YES=1;


    /**
     * 工种名称
     */
    @Column("name")
    private String name;
    /**
     * 工种图标
     */
    @Column("image_id")
    private String imageId;
    /**
     * 关口出题数量
     */
    @Column("ques_nums")
    private Integer quesNums;
    /**
     * 关口限时时间
     */
    @Column("limit_time")
    private Integer limitTime;
    /**
     * 单题限时开关。0不限时，1限时
     */
    @Column("signal_limit_flag")
    private Integer signalLimitFlag;
    /**
     * 单题限时时间。单题限时时间（秒）
     */
    @Column("signal_limit_time")
    private Integer signalLimitTime;

    @Column("score_flag")
    private Integer scoreFlag;
    /**
     *   是否统一分值
     */
    @Column("score_weight")
    private double scoreWeight;
    /**
     * 分值
     */
    @Column("score")
    private double score;
    /**
     * 过关条件要求类型
     */
    @Column("pass_contition_value")
    private Integer passContitionValue;
    /**
     * 闯关失败积分权重
     */
    @Column("pass_failure_mark_weight")
    private Double passFailureMarkWeight;
    /**
     * 答错是否扣分。答错是否扣分0（不扣分）1（扣分）
     */
    @Column("answer_wrong_flag")
    private Integer answerWrongFlag;
    /**
     * 答错扣分权重
     */
    @Column("answer_wrong_mark")
    private Long answerWrongMark;
    /**
     * 公司id
     */
    @Column("company_id")
    private String companyId;
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

    @Column("classify_name")
    private String classifyName;

    @Column("difficulty")
    private  String difficulty;

    @Column("tests_type")
    private String testsType;


}
