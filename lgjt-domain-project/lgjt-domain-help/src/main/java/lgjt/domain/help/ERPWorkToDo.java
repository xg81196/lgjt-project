package lgjt.domain.help;


import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.sql.Date;

@Data
@Table("ERPWorkToDo")
public class ERPWorkToDo extends BaseEntity {

    @Column("App_ID")
    private String App_ID;

    @Column("WorkName")
    private String  WorkName;

    @Column("TimeStr")
    private Date timeStr;

    @Column("FormName")
    private String fromName;

    @Column("StateNow")
    private String stateNow;



}
