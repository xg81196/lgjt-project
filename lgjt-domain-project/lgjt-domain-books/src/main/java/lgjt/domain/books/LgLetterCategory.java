package lgjt.domain.books;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
* 
*/
@Data
@Table("lg_letter_category")
public class LgLetterCategory extends BaseEntity {

			/**
			启用 
			*/
			public static final Integer STATUS_ENABLED   = 0 ;
			/**
			禁用 
			*/
			public static final Integer STATUS_DISABLE   = 1 ;

		/**
	 * 上级ID。
	 */
		@Column("super_id")
		private String superId;
		/**
	 * 分类编号。
	 */
		@Column("cate_code")
		private String cateCode;
		/**
	 * 分类名称。
	 */
		@Column("cate_name")
		private String cateName;
		/**
	 * 排序。
	 */
		@Column("sort")
		private Integer sort;
		/**
	 * 状态。0：启用；1：禁用
取值范围：#[enabled:0:启用$disable:1:禁用]#
	 */
		@Column("status")
		private Integer status;
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
}