package lgjt.domain.backend.org;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("KS_UserDepartment")
public class KsOrg {

    @Column("DepartID")
    private String id;

    @Column("DepartMentName")
    private String name;

    @Column("ParentID")
    private String superId;

}
