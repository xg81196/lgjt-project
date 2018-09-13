package lgjt.domain.task.involvementrank;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class HumanCount {

    @Column("org_id")
    private String orgId;

    @Column("org_name")
    private String  orgName;

    @Column("human_count")
    private Integer humanCount;

    @Column("rush_count")
    private Integer rushCount;
}
