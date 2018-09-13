package lgjt.common.base.vo;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

@Data
public class Count {

    @Column("count")
    private Integer count;

}
