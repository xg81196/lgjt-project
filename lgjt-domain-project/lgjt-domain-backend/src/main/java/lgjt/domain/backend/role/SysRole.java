package lgjt.domain.backend.role;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 系统角色
 */
@Data
@Table("sys_role")
public class SysRole extends CaseEntity {

    public static final String ROOT = "-1";

    /**
     * 角色名称。
     */
    @Column("role_name")
    private String roleName;
    /**
     * 角色状态。0：启用；1：禁用
     */
    @Column("status")
    private Integer status;


    /**
     * 排序。
     */
    @Column("sort")
    private Integer sort;
    /**
     * 描述。
     */
    @Column("remark")
    private String remark;

    /**
     * 级别。0：级别1；1：级别2；\n2：级别3；3：级别
     */
    @Column("level")
    private Integer level;

    /**
     * 分类。角色类别。
     */
    @Column("cate")
    private String cate;

    @Column("super_id")
    private String superId;

    @Column("type")
    private Integer type;

    /**
     * 已选中的权限列表
     */
    private String data;

    private List<SysRole> list = new ArrayList<SysRole>();


}
