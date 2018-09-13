package lgjt.domain.backend.attach;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
* 
*/
@Data
@Table("sys_attachment")
public class SysAttachment extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
//	文件类型。0-pdf， 1-video， 2-mp3，3-swf，4-scrom，5-txt，6-img
	/**
	 * PDF
	 */
	public static final Integer TYPE_PDF = 1;
	/**
	 * 视频
	 */
	public static final Integer TYPE_VIDEO = 2;
	/**
	 * 音频
	 */
	public static final Integer TYPE_AUDIO = 3;
	/**
	 * SWF
	 */
	public static final Integer TYPE_SWF = 4;
	/**
	 * SCROM
	 */
	public static final Integer TYPE_SCROM = 5;
	/**
	 * 文本
	 */
	public static final Integer TYPE_TEXT = 6;
	/**
	 * 图片
	 */
	public static final Integer TYPE_IMG = 7;


	/**
	 * 源文件名称。限制输入字符长度300
	 */
	@Column("source_name")
	private String sourceName;

	/**
	 * 源文件地址。
	 */
	@Column("source_file_path")
	private String sourceFilePath;
	/**
	 * 文件类型。0-文本， 1-图片， 2-音视频，3-swf，4-pdf，
	 */
	@Column("file_type")
	private Integer fileType;
	/**
	 * 源文件大小。
	 */
	@Column("source_file_size")
	private Long sourceFileSize;
	
	public SysAttachment(){}

	public SysAttachment(String sourceName, String sourceFilePath,
                         Integer fileType, Long sourceFileSize) {
		super();
		this.sourceName = sourceName;
		this.sourceFilePath = sourceFilePath;
		this.fileType = fileType;
		this.sourceFileSize = sourceFileSize;
	}
	
	/*
     * 创建人。
     */
    @Column("crt_user")
	private String crtUser;
	/*
     * 创建时间。
     */
    @Column("crt_time")
	private Date crtTime;
	/*
     * 创建IP。
     */
    @Column("crt_ip")
	private String crtIp;
	/*
     * 最后修改人。
     */
    @Column("upd_user")
	private String updUser;
	/*
     * 最后修改时间。
     */
    @Column("upd_time")
	private Date updTime;
	/*
     * 最后修改IP。
     */
    @Column("upd_ip")
	private String updIp;
}