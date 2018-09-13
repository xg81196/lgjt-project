package lgjt.domain.news;


import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;
import java.sql.Date;

@Data
@Table("KS_Class")
public class KsClass implements Serializable {



    /**
     *栏目ID
     */
    @Column("ClassID")
    private Integer ClassID;
    /**
     * 所属模型组ID
     */
    @Column("ChannelID")
    private Integer ChannelID;


    /**
     * 栏目中文名称
     */
    @Column("ClassName")
    private String ClassName;
    /**
     * 栏目英文名称
     */
    @Column("ClassEname")
    private String ClassEname;


    /**
     *
     */
    @Column("ParentID")
    private Integer ParentID;
    /**
     *
     */
    @Column("Root")
    private Integer  Root;
    /**
     *
     */
    @Column("Depth")
    private  Integer Depth;
    /**
     *
     */
    @Column("OrderID")
    private  Integer OrderID;
    /**
     *
     */
    @Column("ParentIDPath")
    private String ParentIDPath;
    /**
     *
     */
    @Column("Child")
    private   Integer Child;
    /**
     *
     */
    @Column("PrevID")
    private Integer PrevID;
    /**
     *
     */
    @Column("NextID")
    private   Integer NextID;
    /**
     *
     */
    @Column("ClassDir")
    private String  ClassDir;
    /**
     *
     */
    @Column("ClassType")
    private Boolean ClassType;
    /**
     *
     */
    @Column("UserName")
    private String UserName;
    /**
     *
     */
    @Column("AddDate")
    private Date AddDate;
    /**
     *
     */
    @Column("ClassTemplate")
    private String ClassTemplate;
    /**
     *
     */
    @Column("ContentTemplate")
    private String ContentTemplate;
    /**
     *
     */
    @Column("Class3GTemplate")
    private String Class3GTemplate;
    /**
     *
     */
    @Column("Content3GTemplate")
    private String Content3GTemplate;
    /**
     *
     */
    @Column("ShowInTop")
    private Boolean ShowInTop;
    /**
     *
     */
    @Column("ShowOn3G")
    private Boolean ShowOn3G;
    /**
     *
     */
    @Column("ShowInCir")
    private Boolean ShowInCir;
    /**
     *
     */
    @Column("AllowPubInfo")
    private Boolean AllowPubInfo;
    /**
     *
     */
    @Column("PubFlag")
    private Boolean PubFlag;
    /**
     *
     */
    @Column("AllowPubGroupID")
    private String AllowPubGroupID;
    /**
     *
     */
    @Column("PubAddMoney")
    private Integer PubAddMoney;
    /**
     *
     */
    @Column("PubAddPoint")
    private Integer PubAddPoint;
    /**
     *
     */
    @Column("PubAddScore")
    private Integer PubAddScore;
    /**
     *
     */
    @Column("CmtAddScore")
    private Integer CmtAddScore;
    /**
     *
     */
    @Column("ShareAddScore")
    private Integer ShareAddScore;
    /**
     *
     */
    @Column("CreateHtmlFlag")
    private Boolean CreateHtmlFlag;
    /**
     *
     */
    @Column("AutoCreateType")
    private String  AutoCreateType;
    /**
     *
     */
    @Column("ClassFsoIndex")
    private Integer ClassFsoIndex;
    /**
     *
     */
    @Column("ClassFsoDirRule")
    private String ClassFsoDirRule;
    /**
     *
     */
    @Column("ClassFsoPre")
    private String ClassFsoPre;
    /**
     *
     */
    @Column("CreateInfoPath")
    private String CreateInfoPath;
    /**
     *
     */
    @Column("CreateInfoFname")
    private String CreateInfoFname;
    /**
     *
     */
    @Column("ClassDomain")
    private String ClassDomain;
    /**
     *
     */
    @Column("DomainBindType")
    private Boolean DomainBindType;
    /**
     *
     */
    @Column("ClassPurview")
    private Integer ClassPurview;
    /**
     *
     */
    @Column("DefaultArrGroupID")
    private String DefaultArrGroupID;
    /**
     *
     */
    @Column("DefaultReadPoint")
    private Integer DefaultReadPoint;
    /**
     *
     */
    @Column("DefaultChargeType")
    private Integer DefaultChargeType;
    /**
     *
     */
    @Column("DefaultPitchTime")
    private Integer DefaultPitchTime;
    /**
     *
     */
    @Column("DefaultReadTimes")
    private Integer DefaultReadTimes;
    /**
     *
     */
    @Column("DefaultDividePercent")
    private Integer DefaultDividePercent;
    /**
     *
     */
    @Column("ClassBasicInfo")
    private String ClassBasicInfo;
    /**
     *
     */
    @Column("ClassDefineContent")
    private String ClassDefineContent;
    /**
     *
     */
    @Column("AdminPurview")
    private String  AdminPurview;
    /**
     *
     */
    @Column("InfoNum")
    private Integer InfoNum;
    /**
     *
     */
    @Column("ProSpecialID")
    private Integer ProSpecialID;

}
