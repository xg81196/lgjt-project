package lgjt.domain.rush.ques;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
* 
*/
@Data
@Table("ques_diff")
public class QuesDiff extends BaseEntity {
		
	
	public static final Integer STATE_YES=0;
	public static final Integer STATE_NO=1;
	
	/**
	 * 名称。
	 */
	@Column("name")
	private String name;
	
	/**
	 * 分值。
	 */
	@Column("score")
	private Integer score;
	
	/**
	 * 状态。
	 * 0:启用      1:禁用
	 */
	@Column("state")
	private Integer state;
	/**
	 * 描述。
	 */
	@Column("mark")
	private String mark;
	
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