package lgjt.domain.backend.org;

import com.ttsx.platform.nutz.pojo.BaseEntity;
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
 * @date 2018/6/13
 * @Description:
 */

@Data
@Table("sys_union")
public class SysUnion extends CaseEntity {

    /**
     * 公会名称
     */
    @Column("name")
    private String name;

    /**
     * 工会地址
     */
    @Column("address")
    private String address;

    /**
     * 管辖范围
     */
    @Column("unit_scope")
    private String unitScope;


    /**
     * 上级工会
     */
    @Column("super_id")
    private String superId;

    //建会日期
    @Column("creation_date")
    private Date creationDate ;
    //基层工会类型。0 独立基层工会 1 联合基层工会
    @Column("union_type")
    private Integer unionType;
    //工会负责人
    @Column("union_leader")
    private String unionLeader ;
    //联系电话
    @Column("union_leader_phone")
    private String unionLeaderPhone ;
    //单位所属行业
    @Column("ind_id")
    private String indId ;
    //单位所属行业
    @Column("logo_url")
    private String logoUrl ;
    @Column("banner_url")
    private String bannerUrl ;
    @Column("introduce")
    private String introduce ;
    //状态。0：启用；1：禁用
    @Column("status")
    private Integer  status ;
    @Column("sort")
    private Integer  sort ;

    @Column("extend1")
    private String  extend1 ;

    @Readonly
    private String province;
    @Readonly
    private String city;
    @Readonly
    private String county;

    @Readonly
    private String orgId;

    @Readonly
    private String superOrgName;

    private List<SysUnion> unionList = new ArrayList<>();
}
