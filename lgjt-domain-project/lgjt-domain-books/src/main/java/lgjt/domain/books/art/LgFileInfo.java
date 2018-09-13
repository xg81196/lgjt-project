package lgjt.domain.books.art;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("lg_file_info")
public class LgFileInfo extends BaseEntity {


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
	 * 文件类型。0-图片，1-视频，3-文本
	 */
		@Column("file_type")
		private Integer fileType;
		/**
	 * 源文件大小。
	 */
		@Column("source_file_size")
		private Long sourceFileSize;
		/**
	 * 如果是视频，取对应封面ID
	 */
		@Column("cover_id")
		private String coverId;
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
	 * 创建人。
	 */
		@Column("crt_user")
		private String crtUser;
		/**
	 * 创建时间。
	 */
		@Column("crt_time")
		private java.util.Date crtTime;
		/**
	 * 创建IP。
	 */
		@Column("crt_ip")
		private String crtIp;
		/**
	 * 最后修改人。
	 */
		@Column("upd_user")
		private String updUser;
		/**
	 * 最后修改时间。
	 */
		@Column("upd_time")
		private java.util.Date updTime;
		/**
	 * 最后修改IP。
	 */
		@Column("upd_ip")
		private String updIp;
}