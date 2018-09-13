package lgjt.domain.moments.groupmessage;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lgjt.domain.moments.usercomment.LgUserComment;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.List;

@Table("lg_group_message")
public class LgGroupMessageInfoVo extends BaseEntity {

    /**
     * 用户id
     */
    @Column("user_id")
    private String userId;
    /**
     * 用户头像
     */
    @Column("user_profile")
    private String userProfile;
    /**
     * 所属班组
     */
    @Column("group_id")
    private String groupId;
    /**
     * 所属班组名称
     */
    @Column("group_name")
    private String groupName;
    /**
     * 发送内容
     */
    @Column("content")
    private String content;
    /**
     * 图片或视频
     */
    @Column("attach_id")
    private String attachId;
    /**
     * 图片类型 1视频2图片
     */
    @Column("type")
    private Integer type;
    /**
     *
     */
    @Column("crt_user")
    private String crtUser;
    /**
     *
     */
    @Column("crt_time")
    private java.util.Date crtTime;
    /**
     *
     */
    @Column("crt_ip")
    private String crtIp;
    /**
     *
     */
    @Column("upd_user")
    private String updUser;
    /**
     *
     */
    @Column("upd_time")
    private java.util.Date updTime;
    /**
     *
     */
    @Column("upd_ip")
    private String updIp;

    @Column("real_name")
    private String realName;


    private List<LgUserComment> commentsList;

    public void setCommentsList(List<LgUserComment> value) {
        this.commentsList = value;
    }

    public List<LgUserComment> getCommentsList() {
        return this.commentsList;
    }

}
