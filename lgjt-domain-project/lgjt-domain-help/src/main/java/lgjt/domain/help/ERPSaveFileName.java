package lgjt.domain.help;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Create By xigexb At 2018/9/11
 */
@Table("ERPSaveFileName")
@Data
public class ERPSaveFileName {

    /**
     * 文件现有名称
     */
    @Column("NowName")
    private String nowName;

    /**
     *文件历史名称
     */
    @Column("OldName")
    private String oldName;

    /**
     * 时间
     */
    @Column("rec_creat_time")
    private Date recCreatTime;


}
