package lgjt.domain.resource;


import lombok.Data;

import java.util.Date;

/**
 * 文件表实体类
 */
@Data
public class TtsxFile extends BaseEntity {
    /**
     * *.files中对象所在空间
     */
    public final static String METADATA_NAME ="metadata";

    /**
     * fileFlag字段名
     */
    public final static String FIELD_FILEFLAG="fileFlag";

    /**
     * parentId字段名
     */
    public final static String FIELD_PARENTID="parentId";

    /**
     * stateFlag字段名 文件状态标识
     */
    public final static String FIELD_STATEFLAG="stateFlag";
    /**
     * referId 文件状态标识
     */
    public final static String FIELD_REFERID="referId";
    /**
     * fileName 文件名
     */
    public final static String FIELD_FILENAME="fileName";
    /**
     * metadata.fileFlag字段名
     */
    public final static String METADATA_FIELD_FILEFLAG=METADATA_NAME+"."+FIELD_FILEFLAG;
    /**
     * metadata.parentId字段名
     */
    public final static String METADATA_FIELD_PARENTID=METADATA_NAME+"."+FIELD_PARENTID;

    /**
     * metadata.stateFlag字段名
     */
    public final static String METADATA_FIELD_STATEFLAG=METADATA_NAME+"."+FIELD_STATEFLAG;

    /**
     * metadata.referId字段名
     */
    public final static String METADATA_FIELD_REFERID=METADATA_NAME+"."+FIELD_REFERID;
    /**
     * metadata.fileName字段名
     */
    public final static String METADATA_FIELD_FILENAME=METADATA_NAME+"."+FIELD_FILENAME;

    /**
     * 已经删除
     */
    public static long DELETEFLAG_DELETED=1;

    /**
     * 未删除 默认
     */
    public static long DELETEFLAG_UNDELETED=0;

    /**
     * 主文件
     */
    public final static long  FILEFLAG_DEFAULT=0;
    /**
     * 子文件
     */
    public final static long  FILEFLAG_CHILD=1;

    /**
     * 父节点默认值
     */
    public final static String PARENTID_DEFAULT="-1";
    /**
     * 业务关联字段默认值
     */
    public final static String REFERID_DEFAULT="-1";

    /**
     * 业务类型字段默认值
     */
    public final static String REFERTYPE_DEFAULT="-1";

    /**
     * 排序字段默认值
     */
    public final static long SORT_DEFAULT=0;

    /**
     * 临时文件
     */
    public final static long  STATEFLAG_TMP=0;
    /**
     * 常规文件
     */
    public final static long  STATEFLAG_NORMAL=1;
    /**
     * 已经删除
     */
    public final static long  STATEFLAG_DELETE=2;
    /**
     * 基础字段：文件名称
     */
    private String fileName;
    /**
     * 全名 名.后缀
     */
    private String fullFileName;

    /**
     * 基础字段：块大小
     */
    private Long chunkSize;
    /**
     * 基础字段：更新时间
     */
    private Date uploadDate;
    /**
     * 基础字段：文件大小
     */
    private Long length;
    /**
     * 基础字段：内容类型
     */
    private String contentType;
    /**
     * 基础字段：MD5
     */
    private String md5;
    /**
     * 基础字段：文件后缀
     */
    private String suffix;

    /**
     * 基础字段：年月日 yyyyMMdd
     */
    private String c_ytd;

    /**
     * 基础字段：年
     */
    private String c_year;
    /**
     * 基础字段：年
     */
    private String c_month;
    /**
     * 基础字段：年
     */
    private String c_day;
    /**
     * 基础字段：年
     */
    private String c_hour;
    /**
     * 基础字段：年
     */
    private String c_min;
    /**
     * 基础字段：年
     */
    private String c_second;

    /**
     * 基础字段： 0 主文件，1子文件
     */
    private Long fileFlag;

    /**
     * 基础字段：父节点
     */
    private String parentId;

    /**
     * 业务字段：文件标识
     *  0临时文件
     *  1正式文件
     *  2删除文件
     */
    private Long stateFlag;

    /**
     * 业务字段：删除标识
     *  0代表未删除，1代表已删除
     */
    private Long deleteFlag;

    /**
     * 业务字段：显示名称
     */
    private String displayName;

    /**
     * 业务字段：描述
     */
    private String description;

    /**
     * 业务字段：关联ID
     */
    private String referId;

    /**
     * 业务字段：关联类型
     */
    private String referType;

    /**
     * 业务字段：文件类型
     */
    private String fileType;

    /**
     * 业务字段：排序
     */
    private Long sort;

    /**
     * 业务字段：业务类型 0 一般附件
     */
    private Long businessType;

}
