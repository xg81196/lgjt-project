package lgjt.common.base.vo;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class Total {

    @Column("total")
    private String total;
}
