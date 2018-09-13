package lgjt.domain.backend.industry;

import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 行业
 */

@Data
@Table("sys_industry")
public class SysIndustry extends CaseEntity {

    //行业名称
    @Column("name")
    private String name  ;
    @Column("super_id")
    private String superId ;
    //状态。0：启用；1：禁用
    @Column("status")
    private Integer status ;
    //级别。1 ，2 ，3 ，4 代表几级节点
    @Column("level")
    private Integer level ;

    private List<SysIndustry> list = Lists.newArrayList();
}
