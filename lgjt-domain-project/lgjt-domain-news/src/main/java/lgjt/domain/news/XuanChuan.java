package lgjt.domain.news;


import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;
import java.sql.Date;

@Data
@Table("dbo.KS_U_XuanChuan")
public class XuanChuan   extends BaseEntity implements Serializable {

    /**
     * 新闻Id
     */
    @Id
    private Integer  InfoID;
    /**
     *文章标题
     */
    @Column("Title")
    private String  Title;
    /**
     *栏目号
     * 莱钢人物ClassID：1381工会新闻ClassID：1330
     */
    @Column("ClassID")
    private Integer ClassID;
    /**
     *文章内容
     */
    @Column("Content")
    private String Content;
    /**
     *
     */
    @Column("PageTitle")
    private String PageTitle;
    /**
     *
     */
    @Column("KeyTags")
    private String KeyTags;
    /**
     *文章简介
     */
    @Column("Intro")
    private String Intro;
    /**
     *
     */
    @Column("TitleFontColor")
    private String TitleFontColor;
    /**
     *属性：热点、头条、滚动、幻灯、允许评论等
     */
    @Column("Attribute")
    private String Attribute;
    /**
     *图片
     */
    @Column("DefaultPic")
    private String DefaultPic;
    /**
     *
     */
    @Column("TurnUrl")
    private String TurnUrl;
    /**
     *
     */
    @Column("priority")
    private Integer priority;
    /**
     *
     */
    @Column("Rank")
    private String Rank;
    /**
     *
     */
    @Column("Hits")
    private Integer Hits;
    /**
     *
     */
    @Column("HitsByDay")
    private Integer HitsByDay;
    /**
     *
     */
    @Column("HitsByWeek")
    private Integer HitsByWeek;
    /**
     *
     */
    @Column("HitsByMonth")
    private Integer HitsByMonth;
    /**
     *
     */
    @Column("LastHitsTime")
    private Date LastHitsTime;
    /**
     *
     */
    @Column("AddDate")
    private Date AddDate;
    /**
     *
     */
    @Column("VoteID")
    private Integer VoteID;
    /**
     *
     */
    @Column("TemplateFile")
    private String TemplateFile;
    /**
     *
     */
    @Column("Template3GFile")
    private String Template3GFile;
    /**
     *
     */
    @Column("ShowON3G")
    private Boolean ShowON3G;
    /**
     *
     */
    @Column("FileName")
    private String  FileName;
    /**
     *
     */
    @Column("RefreshTF")
    private Boolean RefreshTF;
    /**
     *
     */
    @Column("Verify")
    private Boolean Verify;
    /**
     *
     */
    @Column("DelTF")
    private Boolean DelTF;
    /**
     *
     */
    @Column("Inputer")
    private String Inputer;
    /**
     *
     */
    @Column("AttachmentCharge")
    private Integer AttachmentCharge;
    /**
     *
     */
    @Column("AttachmentChargeOnce")
    private Boolean AttachmentChargeOnce;
    /**
     *
     */
    @Column("SocialIDs")
    private String SocialIDs;
    /**
     *
     */
    @Column("CityID")
    private Integer CityID;
    /**
     *
     */
    @Column("CountyID")
    private Integer CountyID;
    /**
     *
     */
    @Column("SEOTitle")
    private String SEOTitle;
    /**
     *
     */
    @Column("SEOKeyWords")
    private String SEOKeyWords;
    /**
     *
     */
    @Column("SEODescription")
    private String SEODescription;
    /**
     *
     */
    @Column("CommentNum")
    private Integer CommentNum;
    /**
     *
     */
    @Column("CopyFrom")
    private String CopyFrom;
    /**
     * 作者
     */
    @Column("Author")
    private String Author;
    /**
     *
     */
    @Column("ProvinceID")
    private Integer  ProvinceID;
    /**
     *
     */
    @Column("ArrGroupID")
    private String ArrGroupID;
    /**
     *
     */
    @Column("ReadPoint")
    private Long ReadPoint;
    /**
     *
     */
    @Column("ChargeType")
    private Boolean ChargeType;
    /**
     *
     */
    @Column("PitchTime")
    private Integer PitchTime;
    /**
     *
     */
    @Column("ReadTimes")
    private Integer ReadTimes;
    /**
     *
     */
    @Column("DividePercent")
    private Integer   DividePercent;
    /**
     *
     */
    @Column("PKID")
    private Integer PKID;
    /**
     *
     */
    @Column("InfoPurview")
    private Boolean  InfoPurview;
    /**
     *
     */
    @Column("Channelid")
    private Integer Channelid;
    /**
     *
     */
    @Column("GoodNum")
    private Integer  GoodNum;
    /**
     *
     */
    @Column("BadNum")
    private Integer BadNum;
    /**
     *
     */
    @Column("FavNum")
    private Integer   FavNum;
    /**
     *
     */
    @Column("ShareNum")
    private Integer ShareNum;
    /**
     *
     */

}
