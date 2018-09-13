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
@Table("ques_questions")
public class QuesQuestions extends BaseEntity {
		
	private static final long serialVersionUID = 1L;
	public static final String TYPE_1 = "单选题";
	public static final String TYPE_2 = "多选题";
	public static final String TYPE_3 = "判断题";
	public static final String TYPE_4 = "填空题";
	public static final String TYPE_5 = "简答题";
	
	//public static final String STEM_TYPE_1 = "图片";
	//public static final String STEM_TYPE_2 = "音频";
	//public static final String STEM_TYPE_3 = "视频";
	
	/**
	 * 单选
	 */
	public static final Integer TYPE_CHOICES_SINGLE = 1;
	/**
	 * 多选
	 */
	public static final Integer TYPE_CHOICES_MULTIPLE = 2;
	/**
	 * 判断题
	 */
	public static final Integer TYPE_JUDGE = 3;
	/**
	 * 填空题
	 */
	public static final Integer TYPE_BLANK = 4;
	/**
	 * 简答题
	 */
	public static final Integer TYPE_SHORT_ANSWER =5;
	
	/**
	 * 初级
	 */
	public static final Integer DIFFICULTY_PRIMARY=1;
	
	/**
	 * 中级
	 */
	public static final Integer DIFFICULTY_INTERMEDIATE=2;
	/**
	 * 高级
	 */
	public static final Integer DIFFICULTY_ADVANCED=3;
	/**
	 * 技师
	 */
	public static final Integer DIFFICULTY_TECHNICIAN=4;
	/**
	 * 所属题库。
	 */
	@Column("classify_id")
	private String classifyId;
	/**
	 * 所属题库名称
	 */
	@Readonly
	@Column("classifyName")
	private String classifyName;

	/**
	 * 答题者错误答案
	 */
	private String errorAnswer;
	/**
	 * 试题难度。试题难度
	 */
	@Column("difficulty_id")
	private String difficultyId;
	
	@Column("difficultyName")
	@Readonly
	private String difficultyName;
	/**
	 * 附件ID
	 */
	@Column("attachment_id")
	private String attachmentId;
	/**
	 * 题干类型。
	 * 0：图片
	 * 1：音频
	 * 2：视频
	 */
	//@Column("stem_type")
	//private Integer stemType;
	/**
	 * 试题类型。题目类型，单选、多选、判断、填空、主观
	 */
	@Column("type")
	private Integer type;
	/**
	 * 试题类型，用于查询 “，”号分隔
	 */
	private String typeQuery;
	/**
	 * 题目文本。题目
	 */
	@Column("question")
	private String question;
	/**
	 * 答案文本。答案+选项  JSON格式 {
  "qs": {
    "q": [
      {
        "type": "4",
        "opt": [
          "人站在木桌上同时触及相线和中性线",
          "人站在地上触及一根带电导线",
          "人站在地上触及漏电设备的金属外壳",
          "人坐在接地的金属台上触及一根带电导线"
        ],
        "answer": [
          "人站在木桌上同时触及相线和中性线",
          "人站在地上触及一根带电导线"
        ]
      },
      {
        "type": "1",
        "answer": [
          "防护"
        ]
      },
      {
        "type": "2",
        "opt": [
          "正确",
          "错误"
        ],
        "answer": [
          "正确"
        ]
      }
    ]
  }
}
	 */
	@Column("answer")
	private String answer;

	/**
	 * 0 未答 1 正确 2错误
	 */
	@Readonly
	private int answerResult;
	
	/**
	 * 系统标识。所属系统
	 */
	@Column("remainsystem")
	private String remainsystem;
	
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

	/**
	 * 附件ID_名称
	 */
	@Column
	@Readonly
	private String attachmentName;
	
	
	@Column("file_type")
	private String fileType;

	@Column("source_name")
	@Readonly
	private String sourceName;
	
}