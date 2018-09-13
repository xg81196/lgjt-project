package lgjt.domain.app.group;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 *
 */
@Data
@Table("lg_group")
public class LgGroup extends BaseEntity {


    /**
     * 班组名称
     */
    @Column("group_name")
    private String groupName;
    /**
     * 班组简介
     */
    @Column("group_desc")
    private String groupDesc;
    /**
     * 班组愿景
     */
    @Column("group_vision")
    private String groupVision;
    /**
     * 特色介绍
     */
    @Column("group_feature")
    private String groupFeature;
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

    public void setValueByKsGroup(KsGroup ksGroup) {

        this.setId(ksGroup.getId());
        this.groupName = ksGroup.getGroupName();
        this.crtIp = "127.0.0.1";
        this.crtUser = "admin";
        this.crtTime = new Date();
        this.realName = "系统管理员";
    }
}