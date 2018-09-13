package lgjt.domain.news;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


/**
 * 附件表
 */
@Table("KS_UploadFiles")
@Data
public class UploadFiles extends BaseEntity {

    /**
     *
     */
    @Column("ChannelID")
    private Integer channelID;
    /**
     *
     */
    @Column("InfoID")
    private String infoID;
    /**
     *
     */
    @Column("FileName")
    private String fileName;
    /**
     *
     */
    @Column("DTimes")
    private Integer dTimes;
    /**
     *
     */
    @Column("DSize")
    private Integer dSize;
    /**
     *
     */
    @Column("AliasName")
    private String aliasName;
    /**
     *
     */
    @Column("isAffix")
    private Boolean isAffix;
    /**
     *
     */
    @Column("UserName")
    private String userName;


}
