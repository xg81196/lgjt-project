package lgjt.domain.help.vo;


import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

import java.sql.Date;

@Data
public class HelpInfoView extends BaseEntity {


    @Column("rel_name")
    private String rel_name;
    @Column("crt_user")
    private String crt_user;
    @Column("id_no")
    private String id_no;
    @Column("crt_time")
    private Date crt_time;
    @Column("type")
    private String type;
    @Column("StateNow")
    private String stateNow;






}
