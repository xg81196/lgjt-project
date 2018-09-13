package lgjt.domain.rush.level_score;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("rush_level_score")
public class RushLevelScoreVo extends BaseEntity {
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
        @Column("score_count")
        private double score;
        /**
         * 公司id
         */
        @Column("company_id")
        private String companyId;

        @Column("company_name")
        private String companyName;

        @Column("mark")
        private String mark;
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

        /*
        真实姓名
         */
        @Column("real_name")
        private String realName;

        @Column("rank")
        private Integer rank;
}
