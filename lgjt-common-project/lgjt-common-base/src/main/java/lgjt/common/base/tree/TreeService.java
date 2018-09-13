package lgjt.common.base.tree;

import com.ttsx.platform.nutz.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import lgjt.common.base.utils.Reflections;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/7/12
 * @Description: Tree Service基类
 */
public abstract  class TreeService<T extends TreeEntity<T>> extends BaseService {


    public void save(T entity) {

        @SuppressWarnings("unchecked")
        Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);

        // 如果没有设置父节点，则代表为跟节点，有则获取父节点实体
        if (entity.getParent() == null || StringUtils.isBlank(entity.getParentId())
                || "-1".equals(entity.getParentId())){
            entity.setParent(null);
        }else{
            entity.setParent(get(entity.getParentId()));
        }
        if (entity.getParent() == null){
            T parentEntity = null;
            try {
                parentEntity = entityClass.getConstructor(String.class).newInstance("-1");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            entity.setParent(parentEntity);
            entity.getParent().setParentIds(StringUtils.EMPTY);
        }

        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = entity.getParentIds();

        // 设置新的父节点串
        entity.setParentIds(entity.getParent().getParentIds()+entity.getParent().getId()+",");

        // 保存或更新实体
        super.insert(entity);

        // 更新子节点 parentIds
        T o = null;
        try {
            o = entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        o.setParentIds("%,"+entity.getId()+",%");
        List<T> list = findByParentIdsLike(o);
        for (T e : list){
            if (e.getParentIds() != null && oldParentIds != null){
                e.setParentIds(e.getParentIds().replace(oldParentIds, entity.getParentIds()));
                preUpdateChild(entity, e);
                updateParentIds(e);
            }
        }

    }

    protected abstract T get( String parentId);


    /**
     * 找到所有子节点
     * @param entity
     * @return
     */
    protected abstract  List<T> findByParentIdsLike(T entity);


    /**
     * 更新所有父节点字段
     * @param entity
     * @return
     */
    public abstract int updateParentIds(T entity);




    /**
     * 预留接口，用户更新子节前调用
     * @param childEntity
     */
    protected void preUpdateChild(T entity, T childEntity) {

    }


}
