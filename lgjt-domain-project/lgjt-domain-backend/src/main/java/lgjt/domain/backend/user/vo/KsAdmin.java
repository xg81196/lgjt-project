package lgjt.domain.backend.user.vo;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("KS_Admin")
public class KsAdmin {

    @Column("id")
    private String id;

    @Column("UserName")
    private String userName;

    @Column("RealName")
    private String realName;

    @Column("PassWord")
    private String password;

    @Column("Tel")
    private String phoneNumber;

    @Column("DepartId")
    private String departId;

    @Column("Salt")
    private String salt;

    @Column("IsClass")
    private String isClass;

    @Column("ParentID")
    private String superId;


}
