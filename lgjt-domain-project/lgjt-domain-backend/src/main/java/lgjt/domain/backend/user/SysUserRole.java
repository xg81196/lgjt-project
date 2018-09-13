package lgjt.domain.backend.user;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:用户角色
 */

@Data
@Table("sys_user_role")
public class SysUserRole extends CaseEntity {

    @Column("admin_user_id")
    private String adminUserId;

    @Column("role_id")
    private String roleId;

    @Column("user_name")
    private String userName;
}
