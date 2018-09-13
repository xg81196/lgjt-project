package lgjt.domain.backend.app;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/***********************
 *Author:王昕禹
 *APP版本数据
 ***********************/
@Data
@Table("sys_app_version")
public class AppVersion extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 终端类型Android/IOS
	 */
	@Column("advice_type")
	private String adviceType;
	
	/**
	 * 安装包名称
	 */
	@Column("app_name")
	private String appName;
	
	/**
	 * 安装包文件名称
	 */
	@Column("name")
	private String name;
	
	/**
	 * 安装包ID
	 */
	@Column("app_attach_id")
	private String appAttachId;
	
	/**
	 * 版本号
	 */
	@Column("version")
	private String version;
	
	/**
	 * 更新日志
	 */
	@Column("msg")
	private String msg;
	
	/**
	 * 状态
	 */
	@Column("state")
	private String state;
	
	/**
	 * 创建人。
	 */
	@Column("crt_user")
	private String crtUser;
	
	/**
	 * 创建时间。
	 */
	@Column("crt_time")
	private Date crtTime;
	
	/**
	 * 修改时间。
	 */
	@Column("upd_time")
	private Date updTime;
	
	/**
	 * 修改人。
	 */
	@Column("upd_user")
	private String updUser;
	
	/**
	 * 创建ip。
	 */
	@Column("crt_ip")
	private String crtIp;
	
	/**
	 * 最后修改ip。
	 */
	@Column("upd_ip")
	private String updIp;
}
