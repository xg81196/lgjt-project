package lgjt.domain.app.dict;

import com.ttsx.platform.nutz.pojo.BaseEntity;
import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
* 词典表实体类
*/
@Data
@Table("sys_dict")
public class SysDict extends CaseEntity {

	/**
	 * 字典代码
	 */
	@Column("code")
	private String code;
	/**
	 * 名称
	 */
	@Column("name")
	private String name;
	/**
	 * 值
	 */
	@Column("value")
	private String value;

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

}