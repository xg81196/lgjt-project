package lgjt.domain.rush.level_score;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

/**
* 闯关排行榜
*/
@Data
@Table("rush_level_score")
public class RushLevelRank extends BaseEntity {


		/**rushSequenceRecord
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
	 * 分值
	 */
		@Column("score")
		private double score;

		/*
		真实姓名
		 */
		@Column("real_name")
		private String realName;

		@Column("company_id")
		private String companyId;

		@Column("company_name")
		private String companyName;

			/*
		* 用户排名
		*/
	@Column("rank")
	private String rank;
}