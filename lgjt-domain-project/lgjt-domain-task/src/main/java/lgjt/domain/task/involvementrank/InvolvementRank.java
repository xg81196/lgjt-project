package lgjt.domain.task.involvementrank;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("involvement_rank")
public class InvolvementRank extends BaseEntity  {

    @Column("id")
    private String id;

    @Column("org_id")
    private String orgId;

    @Column("org_name")
    private String orgName;

    @Column("involvement")
    private Double involvement;
}
