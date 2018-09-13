package lgjt.domain.backend.role.vo;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 角色菜单
 */

@Data
@Table("sys_role_menu")
public class SysRoleMenuVo extends CaseEntity {

    @Column("role_id")
    private String roleId;

    @Column("menu_id")
    private String menuId;

    @Column("menu_code")
    private String menuCode;

    @Column("menu_name")
    private String menuName;

}
