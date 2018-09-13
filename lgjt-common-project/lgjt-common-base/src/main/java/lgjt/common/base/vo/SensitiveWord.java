package lgjt.common.base.vo;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

@Data
@Table("sensitive_word")
public class SensitiveWord extends BaseEntity {
    /**
     * 敏感词
     */
    @Column("sensitivewords")
    private String sensitivewords;
    /**
     * 敏感词类型。
     */
    @Column("type")
    private String type;

    /*
     * 创建人。
     */
    @Column("crt_user")
    private String crtUser;
    /*
     * 创建时间。
     */
    @Column("crt_time")
    private Date crtTime;
    /*
     * 创建IP。
     */
    @Column("crt_ip")
    private String crtIp;
    /*
     * 最后修改人。
     */
    @Column("upd_user")
    private String updUser;
    /*
     * 最后修改时间。
     */
    @Column("upd_time")
    private Date updTime;
    /*
     * 最后修改IP。
     */
    @Column("upd_ip")
    private String updIp;
}
