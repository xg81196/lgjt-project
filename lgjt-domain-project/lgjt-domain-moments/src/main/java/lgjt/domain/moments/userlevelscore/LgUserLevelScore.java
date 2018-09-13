package lgjt.domain.moments.userlevelscore;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 *
 */
@Data
@Table("lg_user_level_score")
public class LgUserLevelScore extends BaseEntity {


    /**
     * 用户名。
     */
    @Column("user_name")
    private String userName;
    /**
     * 等级ID。
     */
    @Column("level_id")
    private String levelId;
    /**
     * 序列ID。type=0时有效存放 序列ID，type=1BBS等级编号
     */
    @Column("sequence_id")
    private String sequenceId;
    /**
     * 描述。
     */
    @Column("mark")
    private String mark;
    /**
     * 积分。
     */
    @Column("score")
    private Long score;
    /**
     * /**
     * 创建人。
     */
    @Column("crt_user")
    private String crtUser;
    /**
     * 创建时间。
     */
    @Column("crt_time")
    private java.util.Date crtTime;
    /**
     * 创建IP。
     */
    @Column("crt_ip")
    private String crtIp;
    /**
     * 最后修改人。
     */
    @Column("upd_user")
    private String updUser;
    /**
     * 最后修改时间。
     */
    @Column("upd_time")
    private java.util.Date updTime;
    /**
     * 最后修改IP。
     */
    @Column("upd_ip")
    private String updIp;
    /**
     * 班zuID
     */
    @Column("group_id")
    private String groupId;

    @Column("group_name")
    private String groupName;
    /**
     * 所属单位
     */
    @Column("org_id")
    private String orgId;

    @Column("message_id")
    private String messageId;
}