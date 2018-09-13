package lgjt.domain.backend.config;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import lombok.Data;

import com.ttsx.platform.nutz.pojo.BaseEntity;
/**
* 
*/
@Data
@Table("sys_config")
public class SysConfig extends BaseEntity {


		/**
	 * 
	 */
		@Column("config_module")
		private String configModule;
		/**
	 * 配置名称。
	 */
		@Column("config_name")
		private String configName;
		/**
	 * 配置值。
	 */
		@Column("config_value")
		private String configValue;
		/**
	 * 配置说明。
	 */
		@Column("config_remark")
		private String configRemark;
		/**
	 * 排序。
	 */
		@Column("config_sort")
		private Integer configSort;
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