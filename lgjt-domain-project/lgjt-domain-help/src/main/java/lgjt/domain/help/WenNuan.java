package lgjt.domain.help;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("WW_Wennuan")
@Data
public class WenNuan extends BaseEntity {


    @Column("App_ID")
    private String app_id;
    @Column("wn_sfz")
    private String wn_sfz;




}
