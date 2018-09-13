package lgjt.domain.rush.sequence_classify;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("rush_sequence_classify")
public class RushSequenceClassify extends BaseEntity {


		/**
	 * 关口工种。工种ID
	 */
		@Column("sequence_id")
		private String sequenceId;
		/**
	 * 题库id
	 */
		@Column("classify_id")
		private String classifyId;
		/**
	 * 难度。难度ID“,”分隔
	 */
		@Column("difficulty")
		private String difficulty;
		/**
	 * 试题类型。类型“,”分隔
	 */
		@Column("question_type")
		private String questionType;
		/**
	 * 关口出题方式(系统配置)。1:随机出题2:顺序出题
	 */
		@Column("tests_type")
		private Integer testsType;
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
	 * 创建人
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 
	 */
		@Column("extend6")
		private java.util.Date extend6;
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
		@Column("extend5")
		private Integer extend5;
		/**
	 * 单题限时时间。单题限时时间（秒）
	 */
		@Column("upd_ip")
		private String updIp;

		@Readonly
		String sequenceIds;
}