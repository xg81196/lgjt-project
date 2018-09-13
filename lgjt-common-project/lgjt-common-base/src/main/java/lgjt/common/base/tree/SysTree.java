package lgjt.common.base.tree;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author daijiaqi
 * @date 2018/6/1416:34
 */
@Data
@Table("sys_tree")
public class SysTree implements Serializable {
    private static final long serialVersionUID = -616001664611751664L;
    /**
     * 有权限
     */
    public final static int  AUTHFLAG_HAVE=1;
    /**
     * 没有权限
     */
    public final static int  AUTHFLAG_NOTHAVE=0;


    /**
     * 状态:启用
     */
    public final static int STATUS_ENABLE=0;
    /**
     * 状态:禁用
     */
    public final static int STATUS_DISABLE=1;

    /**
     * 表ID
     */
    @Column("id")
    private String id;
    /**
     * 名称
     */
    @Column("name")
    private String name;
    /**
     * 父ID
     */
    @Column("super_id")
    private String superId;
    /**
     * 状态
     */
    @Column("status")
    private Integer status;
    /**
     * 类型 0 工会，1企业
     */
    @Column("type")
    private String type;

    /**
     * 排序字段
     */
    @Column("sort")
    private Integer sort;
    /**
     * 权限标记 默认0代表没有权限 1代表有权限
     */
    @Readonly
    private Integer authFlag=SysTree.AUTHFLAG_NOTHAVE;

    private List<SysTree> list =new ArrayList<>();
}
