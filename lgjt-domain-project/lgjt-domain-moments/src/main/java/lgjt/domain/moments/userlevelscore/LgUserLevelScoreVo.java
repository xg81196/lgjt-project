package lgjt.domain.moments.userlevelscore;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class LgUserLevelScoreVo extends BaseEntity {

    @Column("score")
    private Long score;

    @Column("org_id")
    private String orgId;

    @Column("group_id")
    private String groupId;

    @Column("rank")
    private Integer rank;

    @Column("org_name")
    private String orgName;

    @Column("group_name")
    private String groupName;
}
