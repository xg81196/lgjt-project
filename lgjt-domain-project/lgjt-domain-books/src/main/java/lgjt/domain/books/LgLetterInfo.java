package lgjt.domain.books;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 *
 */
@Data
@Table("lg_letter_info")
public class LgLetterInfo extends BaseEntity {

    /**
     * 推荐
     */
    public static final Integer ISRECOMMEND_YES = 0;
    /**
     * 不推荐
     */
    public static final Integer ISRECOMMEND_NO = 1;
    /**
     * 待审核
     */
    public static final Integer VERIFYSTATUS_VERIFYING = 0;
    /**
     * 通过
     */
    public static final Integer VERIFYSTATUS_SECCESS = 1;
    /**
     * 不通过
     */
    public static final Integer VERIFYSTATUS_FAILURE = 2;
    /**
     * 启用
     */
    public static final Integer PUBLISHSTATUS_ENABLED = 0;
    /**
     * 禁用
     */
    public static final Integer PUBLISHSTATUS_DISABLE = 1;
    /**
     * 文本
     */
    public static final Integer LETTERTYPE_TEXT = 0;
    /**
     * 图文
     */
    public static final Integer LETTERTYPE_IMAGE_TEXT = 1;
    /**
     * 音视频
     */
    public static final Integer LETTERTYPE_AUDIO_VIDEO = 2;

    /**
     * 家书名称。
     */
    @Column("book_name")
    private String bookName;
    /**
     * 家书作者。
     */
    @Column("book_author")
    private String bookAuthor;
    /**
     * 家书写作时间。
     */
    @Column("book_time")
    private java.util.Date bookTime;
    /**
     * 家书简介。
     */
    @Column("book_introduce")
    private String bookIntroduce;
    /**
     * 家书引导图。
     */
    @Column("book_picture")
    private String bookPicture;
    /**
     * 家书内容。
     */
    @Column("book_content")
    private String bookContent;
    /**
     * 家书分类ID。
     */
    @Column("book_category_id")
    private String bookCategoryId;
    /**
     * 是否推荐。0：推荐；1：不推荐
     * 取值范围：#[yes:0:推荐$no:1:不推荐]#
     */
    @Column("is_recommend")
    private Integer isRecommend;
    /**
     * 推荐语。
     */
    @Column("recommendation")
    private String recommendation;
    /**
     * 排序。
     */
    @Column("sort")
    private Integer sort;
    /**
     * 审核状态。0：待审核；1：通过；2：不通过
     * 取值范围：#[verifying:0:待审核$seccess:1:通过$failure:2:不通过]#
     */
    @Column("verify_status")
    private Integer verifyStatus;
    /**
     * 发布人。
     */
    @Column("publisher")
    private String publisher;
    /**
     * 发布时间。
     */
    @Column("publish_time")
    private java.util.Date publishTime;
    /**
     * 发布状态。0：启用；1：禁用
     * 取值范围：#[enabled:0:启用$disable:1:禁用]#
     */
    @Column("publish_status")
    private Integer publishStatus;
    /**
     * 点赞数。
     */
    @Column("like_count")
    private Integer likeCount;
    /**
     * 评论数。
     */
    @Column("comment_count")
    private Integer commentCount;
    /**
     * 浏览量。
     */
    @Column("pageviews")
    private Integer pageviews;
    /**
     * 家书类别。0：文本；1：图文；2：音视频
     * 取值范围：#[text:0:文本$image_text:1:图文$audio_video:2:音视频]#
     */
    @Column("book_type")
    private Integer bookType;
    /**
     * 创建人。
     */
    @Column("crt_user")
    private String crtUser;
    /**
     * 创建时间。年-月-日 时:分:秒
     */
    @Column("crt_time")
    private java.util.Date crtTime;
    /**
     * 创建IP。示例：192.168.100.172
     */
    @Column("crt_ip")
    private String crtIp;
    /**
     * 最后修改人。
     */
    @Column("upd_user")
    private String updUser;
    /**
     * 最后修改时间。年-月-日 时:分:秒
     */
    @Column("upd_time")
    private java.util.Date updTime;
    /**
     * 最后修改IP。示例：192.168.100.172
     */
    @Column("upd_ip")
    private String updIp;

    /**
     * 图书资源
     */
    @Column("book_resource")
    private String bookResource;
    /**
     * 反馈意见
     */
    @Column("audit_opinion")
    private String auditOpinion;

    @Column("real_name")
    private String realName;
}