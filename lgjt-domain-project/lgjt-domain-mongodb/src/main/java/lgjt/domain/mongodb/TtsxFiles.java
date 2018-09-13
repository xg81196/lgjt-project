package lgjt.domain.mongodb;


import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文件表实体类
 */
@Data
public class TtsxFiles extends MongoDBBaseEntity{

    /**
     * 已经删除
     */
    public static long DELETEFLAG_DELETED=1;

    /**
     * 未删除 默认
     */
    public static long DELETEFLAG_UNDELETED=0;

    /**
     * 图片
     */
    public static String FILETYPE_IMG="image";

    /**
     * 文件
     */
    public static String FILETYPE_FILE="file";

    /**
     * 视频
     */
    public static String FILETYPE_VIDEO="video";

    /**
     * 音频
     */
    public static String FILETYPE_AUDIO="audio";

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
