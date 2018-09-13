package lgjt.domain.moments.challenge;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;

/**
 * @author 赵天意
 * 排行榜
 */
@Data
public class LgChallengeRank {

    @Column("id")
    private String id;

    @Column("extend1")
    private String orgId;

    @Column("extend2")
    private String orgName;

    @Column("group_name")
    private String groupName;

    @Column("real_name")
    private String realName;

    @Column("likeCount")
    private Integer likeCount;

    @Column("rank")
    private Integer rank;
}
