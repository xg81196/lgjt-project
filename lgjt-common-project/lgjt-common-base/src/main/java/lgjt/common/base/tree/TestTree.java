package lgjt.common.base.tree;

/**
 * @author wuguangwei
 * @date 2018/7/12
 * @Description:
 */
public class TestTree extends TreeEntity<TestTree> {

    private static final long serialVersionUID = 1L;
    private TestTree parent;		// 父级编号
    private String parentIds;		// 所有父级编号
    private String name;		// 名称
    private Integer sort;		// 排序

    public TestTree() {
        super();
    }

    public TestTree(String id){
        super(id);
    }


    public TestTree getParent() {
        return parent;
    }

    public void setParent(TestTree parent) {
        this.parent = parent;
    }

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
        return parent != null && parent.getId() != null ? parent.getId() : "0";
    }
}