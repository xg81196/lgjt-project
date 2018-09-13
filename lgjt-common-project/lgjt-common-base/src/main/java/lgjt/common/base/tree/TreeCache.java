package lgjt.common.base.tree;

import com.ttsx.platform.tool.util.ObjectUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.lang.reflect.Field;
import java.util.*;

/**
 * tree redis 缓存类
 * @author daijiaqi
 * @date 2018/6/1315:28
 */
public abstract class TreeCache<T> {
    private static final Logger logger = Logger.getLogger(TreeCache.class);
    /**
     * 关系前缀
     */
    private  static String TYPE_REF = "REF";
    /**
     * 系統自帶的 用于识别
     */
    private static String redisKey = "tree";
    /**
     * 对于用户来说的唯一前缀
     */
    private  static String NAME = "tree";

    /**
     * 对象前缀
     */
    private  static String TYPE_OBJ = "OBJ";
    /**
     * 对于用户来说的唯一前缀
     */
    private static String UNIQUEPREFIX = "";
    /**
     * 主键
     */
    private static String T_KEY = "id";
    /**
     * 父ID
     */
    private static String T_SUPERID = "superId";
    /**
     * 是否执行
     */
    boolean run = false;
    /**
     * 更新时间间隔 单位秒
     */
    private int interval=3600;


    /**
     * 初始化方法
     * @param uniquePrefix 前缀
     * @param key 主键
     * @param superId 父ID'
     * @param interval 秒 最少10秒
     */
    public TreeCache(String uniquePrefix, String key, String superId,int interval) {
        UNIQUEPREFIX = uniquePrefix;
        T_KEY = key;
        T_SUPERID = superId;
        if(interval>0){
            this.interval=interval;
        }
        init();
    }

    /**
     *  获取数据需要子类实现
     * @return 数据集合
     */
    public abstract List<T> load();


    /**
     * 刷新
     */
    public void refresh() {
        String keyValue = "";
        String superIdValue = "";
        try {
            List<T> list = load();
            if (list != null) {
                Map<String,Object>  treeObjects=new HashMap<>();
                List<String> idAndSuperIds= new ArrayList<>();
                for (T t : list) {
                    keyValue = getValueFromTByKeyName(T_KEY, t);
                    superIdValue = getValueFromTByKeyName(T_SUPERID, t);
                    setTREE_OBJECTS(keyValue, t);
                    putTreeRef(keyValue,superIdValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("TreeCache.refresh(" + keyValue + "," + superIdValue + ")", e);
        }
    }
//
//    /**
//     * 刷新
//     */
//    public void refreshold() {
//        String keyValue = "";
//        String superIdValue = "";
//        try {
//            List<T> list = load();
//            if (list != null) {
//                Map<String,Object>  treeObjects=new HashMap<>();
//                List<String> idAndSuperIds= new ArrayList<>();
//                //删除
//                clear();
//                for (T t : list) {
//                    keyValue = getValueFromTByKeyName(T_KEY, t);
//                    superIdValue = getValueFromTByKeyName(T_SUPERID, t);
//                    treeObjects.put(keyValue, t);
//                    idAndSuperIds.add(keyValue+";"+superIdValue);
////                    putTreeObject(keyValue, t);
////                    putTreeRef(keyValue, superIdValue);
//                }
//                setTREE_OBJECTS(treeObjects);
//                putTreeRef(idAndSuperIds);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//               logger.error("TreeCache.refresh(" + keyValue + "," + superIdValue + ")", e);
//        }
//    }
    private static void clear(){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = redisKey + "-" + UNIQUEPREFIX + "-ID_CHILD_REF";
        objectCache.del(key);
        key = redisKey + "-" + UNIQUEPREFIX+ "-ID_SUPER_REF";
        objectCache.del(key);
         key = redisKey + "-" + UNIQUEPREFIX + "-TREE_OBJECTS";
        objectCache.del(key);
    }
    /**
     * 插入节点
     *
     * @param t
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public   void insert(T t) throws NoSuchFieldException, IllegalAccessException {
        String keyTmp = getValueFromTByKeyName(T_KEY, t);
        String superIdTmp = getValueFromTByKeyName(T_SUPERID, t);
        setTREE_OBJECTS(keyTmp, t);//设置对象
        setID_CHILD_REF(superIdTmp, keyTmp);//设置孩子关系
        setID_SUPER_REF(keyTmp, superIdTmp);//设置父亲关系
    }
    /**
     * 修改树节点
     *
     * @param t 树节点
     */
    public void update(T t) throws Exception {
        String newKey = getValueFromTByKeyName(T_KEY, t);
        T oldT = get(newKey);
        String oldSuperId = getValueFromTByKeyName(T_SUPERID, oldT);
        String newSuperId = getValueFromTByKeyName(T_SUPERID, t);
        if (oldSuperId.equals(newSuperId)) {//不改关系
            setTREE_OBJECTS(newKey, t);
        } else {//改关系
            List<String> idChildRefOld = getID_CHILD_REF(oldSuperId);
            List<String> idChildRefNew = getID_CHILD_REF(newSuperId);

            if (idChildRefOld != null) {
                idChildRefOld.remove(newKey);//老父亲的孩子里要去掉这个节点
            }
            if (idChildRefNew != null) {
                idChildRefNew.add(newKey);//新父亲的孩子里要加上这个节点
            }
            setID_CHILD_REF(oldSuperId,idChildRefOld);
            setID_CHILD_REF(newSuperId,idChildRefNew);


            List<String> idSuperRef=  getID_SUPER_REF(newKey);//父亲
            if (idSuperRef != null) {
                idSuperRef.remove(oldSuperId);
                idSuperRef.add(newSuperId);
            }
            setID_SUPER_REF(newKey,idSuperRef);
        }
    }
    private static  List<String> getID_SUPER_REF(String id) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+"ID_SUPER_REF"+":"+id;
        Object obj =getObjectCache().get(key);
        if(obj!=null) {
            return (List<String>)obj;
        }else {
            return new ArrayList<String>();
        }
    }
    private  void setID_CHILD_REF(String id, List<String> idChildRef) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+ "ID_CHILD_REF"+":"+id;
        getObjectCache().set(key, idChildRef);
    }
    private  void setID_SUPER_REF(String id,List<String> idSuperRefs) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+"ID_SUPER_REF"+":"+id;
        getObjectCache().set(key, idSuperRefs);
    }
    /**
     * 删除节点
     *
     * @param t 树对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public   void delete(T t) throws Exception {
        String keyTmp = getValueFromTByKeyName(T_KEY, t);
        //处理当前节点
        remove(keyTmp);
    }






//    /**
//     * 存放实体
//     *
//     * @param mapKey
//     * @param t
//     */
//    private static <T> void putTreeObject(String mapKey, T t) {
//        Map<String,Object> treeObjects = getTREE_OBJECTS();
//        if(treeObjects==null){
//            treeObjects=new HashMap<>();
//        }
//        treeObjects.put(mapKey, t);
//        setTREE_OBJECTS(treeObjects);
//    }






    /**
     * 添加关系
     * @param id;superId
     * @param
     */
    private  void putTreeRef(String id ,String superId) {
        setID_CHILD_REF(superId, id);
        setID_SUPER_REF(id, superId);

    }

    /**
     * 利用反射从对象返回属性值
     *
     * @param key
     * @param t
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static <T> String getValueFromTByKeyName(String key, T t) throws NoSuchFieldException, IllegalAccessException {
        try {
            Field fieldKey = t.getClass().getDeclaredField(key);
            if (fieldKey == null) {
                fieldKey = t.getClass().getSuperclass().getDeclaredField(key);
            }
            fieldKey.setAccessible(true);
            String fieldValue = fieldKey.get(t).toString();
            return fieldValue;
        } catch (NoSuchFieldException e) {
            throw e;
        } catch (IllegalAccessException e1) {
            throw e1;
        }
    }




    /**
     * 根据ID获取一个数据的所有孩子
     *
     * @param id    数据ID
     * @param level 获取多少层。如果为0或负数，获取所有的子孙
     * @param    children  <id,子节点集合>,子节点集合>子节点的映射关系
     * @return
     */
    public static <T> List<T> getChildren(String id, int level, Map<String, List<String>> children) {
        List<String> ids = new ArrayList<String>();
        return getChildren(id, level, ids, children);
    }
    public static int count=0;
    public static int index=10000;
    /**
     * 获取子节点
     *
     * @param id
     * @param level
     * @param ids
     * @param children 如果为空则从全局取，如果不为空从指定取
     * @param <T>
     * @return
     */
    private static <T> List<T> getChildren(String id, int level, List<String> ids, Map<String, List<String>> children) {
        int test=index++;
        id = StringUtils.trim(id);
        List<T> result = new ArrayList<T>();
        if (children == null) {
            children=new HashMap<String, List<String>>();
            children.put(id, getID_CHILD_REF(id));

        }
        if (children == null || children.size() <= 0) {
            return result;
        }
        List<String> list = children.get(id);
        if (list == null || list.size() <= 0) {
            return result;
        }


        for (int i = 0; i < list.size(); i++) {
            count++;
//            T t = get(list.get(i));
                T t=(T)get(list.get(i));
            if (t != null) {
                if (!ids.contains(list.get(i))) {
                    ids.add(list.get(i));
                    result.add(t);
                } else {
//                    continue;
                    return result;
                }
                if (level <= 0) {
                    result.addAll(getChildren(list.get(i), -1, ids, children));
                } else if (level > 1) {
                    result.addAll(getChildren(list.get(i), level - 1, children));
                }
            }
        }

        return result;
    }






    private  void setID_SUPER_REF(String id,String idSuperRef) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+"ID_SUPER_REF"+":"+id;
        List<String> refsRedis=getID_SUPER_REF(id);
        if(!refsRedis.contains(idSuperRef)) {
            refsRedis.add(idSuperRef);
            getObjectCache().set(key, refsRedis);
        }
    }
    /**
     * 根据id获取一级父ID集合
     * @param id
     * @return
     */
    public static List<String> getSuperIds(String id) {
        return getID_SUPER_REF(id);
    }


    private  void setTREE_OBJECTS(String id , T t) {
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = redisKey + ":" + NAME+":"+TYPE_OBJ  +":"+ "TREE_OBJECTS"+":"+id;
        objectCache.set(key, t);
//        objectCache.setExpireTime(key, -1);
    }
    public static <T> T get(String id) {
        String key = redisKey + ":" + NAME+":"+TYPE_OBJ  +":"+ "TREE_OBJECTS"+":"+id;
        return  (T)getObjectCache().get(key);
    }

    private  void removeID_SUPER_REF(String id) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+"ID_SUPER_REF"+":"+id;
        getObjectCache().del(key);
    }
    private static  List<String> getID_CHILD_REF(String id) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+"ID_CHILD_REF"+":"+id;
        Object obj =getObjectCache().get(key);
        if(obj!=null) {
            return (List<String>)obj;
        }else {
            return new ArrayList<String>();
        }
    }
    private void remove(String id) {
        String key = redisKey + ":" + NAME+":"+TYPE_OBJ  +":"+ "TREE_OBJECTS"+":"+id;
        getObjectCache().del(key);
        //删除父亲节点 只删除关系
        removeID_SUPER_REF(id);
        //删除孩子节点
        List<String> children = getID_CHILD_REF(id);
        if(children!=null && children.size()>0) {
            for (String cId : children) {
                remove( cId);
            }
        }

    }
    private static  IObjectCache getObjectCache() {
        return CacheFactory.getObjectCache();
    }
    private  void setID_CHILD_REF(String id, String idChildRef) {
        String key = redisKey + ":" + NAME+":"+TYPE_REF +":"+ "ID_CHILD_REF"+":"+id;
        List<String> refsRedis=getID_CHILD_REF(id);
        if(!refsRedis.contains(idChildRef)) {
            refsRedis.add(idChildRef);
            getObjectCache().set(key, refsRedis);
        }
    }


    /**
     * 初始化数据。
     */
    private void init(){
            refresh();
            if(!run){
                new Thread(new Runnable(){
                    public void run() {
                        Thread.currentThread().setName("TreeCache_"+redisKey+ "_" +UNIQUEPREFIX);
                        while(true){
                            try{
                                Thread.sleep(interval*1000);
                            }
                            catch(Exception e){}
                            refresh();
                        }
                    }
                }).start();
                run=true;
            }
    }
}
