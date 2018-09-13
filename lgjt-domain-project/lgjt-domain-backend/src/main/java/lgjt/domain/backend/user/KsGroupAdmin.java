package lgjt.domain.backend.user;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class KsGroupAdmin {

    @Column("ID")
    private String id;

    @Column("UserName")
    private String userName;

    @Column("PassWord")
    private String password;

    @Column("PrUserName")
    private String orgId;

    @Column("RealName")
    private String realName;

    @Column("salt")
    private String salt;

}
