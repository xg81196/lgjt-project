package lgjt.domain.moments.userlevelscore;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class AdminGroupMessage extends BaseEntity {

    @Column("group_name")
    private String groupName;

    @Column("crt_user")
    private String crtUser;

    @Column("real_name")
    private String realName;

    @Column("crt_time")
    private java.util.Date crtTime;

    @Column("verify_status")
    private Integer verifyStatus;
    /**
     * 审核人
     */
    @Column("verify_user")
    private String verifyUser;

    @Column("score")
    private Integer score;
}
