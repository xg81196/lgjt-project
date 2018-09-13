package lgjt.common.base.tree;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import org.apache.commons.lang3.StringUtils;
import lgjt.common.base.utils.Reflections;

/**
 * @author wuguangwei
 * @date 2018/7/12
 * @Description: 数据Entity类
 */
public abstract class TreeEntity<T> extends CaseEntity {

    private static final long serialVersionUID = 1L;

    protected T parent;	// 父级编号

    protected String parentIds; // 所有父级编号

    protected String name; 	// 机构名称

    protected Integer sort;		// 排序

    public TreeEntity() {
        super();
        this.sort = 10;
    }

    public TreeEntity(String id) {
       // this.id = id;
    }

    public abstract T getParent();


    public abstract void setParent(T parent);


    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        String id = null;
        if (parent != null){
            id = (String) Reflections.getFieldValue(parent, "id");
        }
        return StringUtils.isNotBlank(id) ? id : "0";
    }



}
