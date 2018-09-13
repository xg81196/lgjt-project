package lgjt.domain.moments.challenge;

import com.ttsx.platform.nutz.result.PageResult;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 *
 */
@Data
@Table("lg_challenge")
public class LgChallenge extends BaseEntity {


    /**
     * 挑战内容
     */
    @Column("content")
    private String content;
    /**
     * 审核状态 0：提交，1：通过，2：驳回
     */
    @Column("status")
    private Integer status;
    /**
     * 审核意见
     */
    @Column("check_msg")
    private String checkMsg;
    /**
     * 审核人名字
     */
    @Column("check_user")
    private String checkUser;
    /**
     * 发布时间
     */
    @Column("publish_time")
    private java.util.Date publishTime;
    /**
     * 班组ID
     */
    @Column("group_id")
    private String groupId;
    /**
     *
     */
    @Column("group_name")
    private String groupName;
    /**
     * 是否有效  0：是，1：否
     */
    @Column("is_disable")
    private Integer isDisable;
    /**
     * 是否删除 0：否，1：是
     */
    @Column("is_delete")
    private Integer isDelete;
    /**
     * 用户ID
     */
    @Column("user_id")
    private String userId;
    /**
     * 用户头像
     */
    @Column("user_profile")
    private String userProfile;
    /**
     * 真实姓名
     */
    @Column("real_name")
    private String realName;
    /**
     * 扩展字段1
     */
    @Column("extend1")
    private String extend1;
    /**
     * 扩展字段2
     */
    @Column("extend2")
    private String extend2;
    /**
     * 扩展字段3
     */
    @Column("extend3")
    private String extend3;
    /**
     * 扩展字段4
     */
    @Column("extend4")
    private Integer extend4;
    /**
     * 扩展字段5
     */
    @Column("extend5")
    private Integer extend5;
    /**
     * 扩展字段6
     */
    @Column("extend6")
    private java.util.Date extend6;
    /**
     * 创建人
     */
    @Column("crt_user")
    private String crtUser;
    /**
     * 提交时间
     */
    @Column("crt_time")
    private java.util.Date crtTime;
    /**
     * 创建IP
     */
    @Column("crt_ip")
    private String crtIp;
    /**
     * 更新人
     */
    @Column("upd_user")
    private String updUser;
    /**
     * 更新时间
     */
    @Column("upd_time")
    private java.util.Date updTime;
    /**
     * 更新人IP
     */
    @Column("upd_ip")
    private String updIp;

    /**
     * 是否当前登陆用户发布（1：是， 0：否）
     */
    @Readonly
    private String mark;

    /**
     * 发布题材的数量
     */
    @Readonly
    private Integer crtCount;

    /**
     * 挑战的数量
     */
    @Readonly
    private Integer challengerCount;

    /**
     * 资源JSON
     */
    @Readonly
    private String attachId;

    /**
     * 资源类型
     */
    @Readonly
    private String type;

    /**
     * 用户是否点赞
     * 0 否
     * 1 是
     */
    @Readonly
    private String likeMark;

    @Readonly
    private Integer likeCount;

    @Readonly
    private Integer rank;

    @Readonly
    private PageResult<LgChallengePkVo> challengers;
}