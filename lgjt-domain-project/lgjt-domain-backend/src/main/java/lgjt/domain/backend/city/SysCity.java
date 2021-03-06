package lgjt.domain.backend.city;

import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.List;

@Data
@Table("sys_city")
public class SysCity extends BaseEntity {

	public static final String CHINA_ID = "100000";
	
	public static final Integer BASE_YES = 1;
	public static final Integer BASE_NO = 2;
	
	public static final Integer STATUS_OPEN = 1;
	public static final Integer STATUS_CLOSE = 2;
	
	public static final Integer COLD_YES = 1;
	public static final Integer COLD_NO= 2;
	/**
	 * 城市名称
	 */
	@Column("name")
	private String name;
	
	/**
	 * 父ID
	 */
	@Column("parentid")
	private String parentid;
	
	/**
	 * 简称
	 */
	@Column("short")
	private String shortName;
	
	/**
	 * 层级
	 */
	@Column("level")
	private int level;
	
	
	/**
	 * 电话区号
	 */
	@Column("code")
	private String code;
	
	/**
	 * 邮编
	 */
	@Column("zip")
	private String zip;
	
	/**
	 * 全名
	 */
	@Column("mergername")
	private String mergername;
	
	/**
	 * 拼音
	 */
	@Column("pinyin")
	private String pinyin;
	
	/**
	 * 经度
	 */
	@Column("ing")
	private String ing;
	
	/**
	 * 纬度
	 */
	@Column("lat")
	private String lat;
	
	/**
	 * 状态
	 */
	@Column("status")
	private Integer status;
	
	/**
	 * 是否基地
	 */
	@Column("base")
	private Integer base;
	/**
	 * 是否冷链
	 */
	@Column("cold")
	private Integer cold;

	private List<SysCity> list = Lists.newArrayList();
}
