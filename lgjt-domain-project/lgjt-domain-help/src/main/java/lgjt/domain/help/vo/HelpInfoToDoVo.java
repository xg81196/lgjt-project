package lgjt.domain.help.vo;


import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

import java.sql.Date;

@Data
public class HelpInfoToDoVo {

    @Column("FormName")
    private String formName;
    @Column("StateNow")
    private String stateNow;
    @Column("RealName")
    private String realName;
    @Column("LateTime")
    private Date lateTime;



}
