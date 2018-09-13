package lgjt.domain.backend.org;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author wuguangwei
 * @date 2018/6/13
 * @Description: 工会企业
 */

@Data
@Table("sys_union_com")
public class SysUnionCom  extends BaseEntity {

    @Column("union_id")
    private String unionId;

    @Column("com_id")
    private String comId;

    @Column("union_name")
    private String unionName;

    @Column("com_name")
    private String comName;
}
