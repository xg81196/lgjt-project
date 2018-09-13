package lgjt.domain.moments.challenge;

import com.ttsx.platform.tool.util.StringTool;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;

/**
 *
 */
@Data
@Table("lg_challenge_pk")
public class LgChallengePk extends BaseEntity {


    /**
     * 要挑战的竞拍ID
     */
    @Column("super_id")
    private String superId;
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

    @Readonly
    private String attachId;

    @Readonly
    private String type;

    @Readonly
    private Integer likeCount;

    public LgChallengePk() {

    }

    public LgChallengePk(LgChallenge obj) {
        if (StringTool.isNotNull(obj.getContent())) {
            this.content = obj.getContent();
        }
        if (StringTool.isNotNull(obj.getStatus())) {
            this.status = obj.getStatus();
        }
        if (StringTool.isNotNull(obj.getCheckMsg())) {
            this.checkMsg = obj.getCheckMsg();
        }
        if (StringTool.isNotNull(obj.getCheckUser())) {
            this.checkUser = obj.getCheckUser();
        }
        if (StringTool.isNotNull(obj.getPublishTime())) {
            this.publishTime = obj.getPublishTime();
        }
        if (StringTool.isNotNull(obj.getGroupId())) {
            this.groupId = obj.getGroupId();
        }
        if (StringTool.isNotNull(obj.getGroupName())) {
            this.groupName = obj.getGroupName();
        }
        if (StringTool.isNotNull(obj.getIsDisable())) {
            this.isDisable = obj.getIsDisable();
        }
        if (StringTool.isNotNull(obj.getIsDelete())) {
            this.isDelete = obj.getIsDelete();
        }
        if (StringTool.isNotNull(obj.getUserId())) {
            this.userId = obj.getUserId();
        }
        if (StringTool.isNotNull(obj.getUserProfile())) {
            this.userProfile = obj.getUserProfile();
        }
        if (StringTool.isNotNull(obj.getRealName())) {
            this.realName = obj.getRealName();
        }
        if (StringTool.isNotNull(obj.getExtend1())) {
            this.extend1 = obj.getExtend1();
        }
        if (StringTool.isNotNull(obj.getExtend2())) {
            this.extend2 = obj.getExtend2();
        }
        if (StringTool.isNotNull(obj.getExtend3())) {
            this.extend3 = obj.getExtend3();
        }
        if (StringTool.isNotNull(obj.getExtend4())) {
            this.extend4 = obj.getExtend4();
        }
        if (StringTool.isNotNull(obj.getExtend5())) {
            this.extend5 = obj.getExtend5();
        }
        if (StringTool.isNotNull(obj.getExtend6())) {
            this.extend6 = obj.getExtend6();
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            this.crtUser = obj.getCrtUser();
        }
        if (StringTool.isNotNull(obj.getCrtTime())) {
            this.crtTime = obj.getCrtTime();
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            this.crtIp = obj.getCrtIp();
        }
        if (StringTool.isNotNull(obj.getUpdUser())) {
            this.updUser = obj.getUpdUser();
        }
        if (StringTool.isNotNull(obj.getUpdTime())) {
            this.updTime = obj.getUpdTime();
        }
        if (StringTool.isNotNull(obj.getUpdIp())) {
            this.updIp = obj.getUpdIp();
        }
    }
}