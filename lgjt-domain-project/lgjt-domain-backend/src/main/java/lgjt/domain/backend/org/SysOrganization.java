package lgjt.domain.backend.org;

import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:机构
 */

@Data
@Table("sys_organization")
public class SysOrganization extends CaseEntity {

    //单位名称
    @Column("name")
    private String name ;
    //组织机构代码
    @Column("org_code")
    private String orgCode ;
    //社会信用代码
    @Column("identifier_code")
    private String identifierCode ;
    //单位性质类别
    @Column("unit_property_category")
    private String unitPropertyCategory;
    //经济类型
    @Column("economic_type")
    private String economicType ;
    //上级工会名称
    @Column("super_id")
    private String superId ;
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
    //类别。0：企业；1：工会；2：组织机构
    @Column("type")
    private Integer type;
    //状态。0：启用；1：禁用
    @Column("status")
    private Integer  status ;
    @Column("sort")
    private Integer  sort ;
    @Column("address")
    private String address;
    @Column("extend1")
    private String extend1;

    @Column("extend2")
    private String extend2;

    @Column("extend3")
    private String extend3;

    @Readonly
    private String province;
    @Readonly
    private String city;
    @Readonly
    private String county;
    @Readonly
    private String superOrgName;
    @Readonly
    private String areaName;
    @Readonly
    private String orgId;


    @Readonly
    private List<SysOrganization> list = Lists.newArrayList();

    public void setValueForKsOrg(KsOrg value) {
        this.setId(value.getId());
        this.name = value.getName();
        this.superId = value.getSuperId();
        this.setCrtIp("127.0.0.1");
        this.setCrtUser("admin");
        this.setCrtTime(new Date());
        this.status = 0;
        this.type = 0;
    }

}
