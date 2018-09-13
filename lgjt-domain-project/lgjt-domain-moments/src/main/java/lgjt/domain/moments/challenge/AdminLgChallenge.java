package lgjt.domain.moments.challenge;

import lombok.Data;
import org.nutz.dao.entity.annotation.Column;

import java.util.Date;

@Data
public class AdminLgChallenge {

    @Column("id")
    private String id;

    @Column("content")
    private String content;

    @Column("content")
    private String type;

    @Column("content")
    private String groupName;

    @Column("realName")
    private String realName;

    @Column("likeCount")
    private String likeCount;

    @Column("crtTime")
    private Date crtTime;

    @Column("isDisable")
    private Integer isDisable;

    @Column("status")
    private String status;
}
