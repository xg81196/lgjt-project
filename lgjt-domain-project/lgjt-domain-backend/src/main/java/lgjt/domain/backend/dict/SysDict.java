package lgjt.domain.backend.dict;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
* 
*/
@Data
@Table("sys_dict")
public class SysDict extends BaseEntity {
	
	public static final String BILLTYPE = "BILLTYPE";
	public static final String EXPRESS = "EXPRESS";
	public static final String EFFECTIVE = "EFFECTIVE";
	public static final String CHANNEL="CHANNEL";
	public static final String LOGISTICS="LOGISTICS";
	public static final String GOODTYPE="GOODTYPE";
	public static final String STATE="STATE";
	
	public SysDict() {
	}

	public SysDict(SysDict dictionary, List<SysDict> list) {
		this.setId(dictionary.getId());
		this.setDictCode(dictionary.getDictCode());
		this.setCodeName(dictionary.getCodeName());
		this.setCodeValue(dictionary.getCodeValue());
		this.setRemark(dictionary.getRemark());
		this.setParentId(dictionary.getParentId());
		this.list = list;

	}

	/**
	 * 字典代码
	 */
	@Column("code")
	private String dictCode;
	/**
	 * 名称
	 */
	@Column("name")
	private String codeName;
	/**
	 * 值
	 */
	@Column("value")
	private String codeValue;

	/**
	 * 父id
	 */
	@Column("parent_id")
	private String parentId;
	/**
	 * 备注
	 */
	@Column("remark")
	private String remark;
	/**
	 * 创建人
	 */
	@Column("crt_userid")
	private String crtUserid;
	/**
	 * 创建时间
	 */
	@Column("crt_time")
	private java.util.Date crtTime;

	@Column("aa")
	@Readonly
	private String aa;
	@Readonly
	private String xid;
	private List<SysDict> list = new ArrayList<SysDict>();
}