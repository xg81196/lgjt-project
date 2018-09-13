package lgjt.vo.app.user;


import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

/**
 *
 */
@Data
public class SysUserOrgVo extends BaseEntity {



    @Column("userid")
    private String userId;

    /**
     * 用户名。
     */
    @Column("userName")
    private String userName;
    /**
     * 姓名。
     */
    @Column("realName")
    private String realName;

    /**
     * 所属企业
     */
    @Column("name")
    private String name;

    /**
     * 状态
     */
    @Column("status")
    private Integer status;



}
