package lgjt.domain.app.group;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Cleanup;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.mvc.annotation.Param;

@Data
@Table("KS_UserDepartment")
public class KsGroup extends BaseEntity {

    @Column("DepartID")
    private String id;

    @Column("DepartMentName")
    private String groupName;

}
