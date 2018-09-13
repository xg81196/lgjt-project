package lgjt.web.backend.init;

import com.ttsx.platform.tool.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import lgjt.common.base.tree.SysTree;
import lgjt.common.base.tree.TreeCache;
import lgjt.common.base.utils.IocUtils;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.org.SysTreeService;

import java.util.*;

/**
 * @author daijiaqi
 * @date 2018/6/1416:34
 */
public class TreeCacheImpl extends TreeCache<SysTree> {

    public final static String ROOT_ID = "-1";
    private SysTreeService sysTreeService = IocUtils.getBean(SysTreeService.class);
    private static TreeCacheImpl treeCacheImpl=null;
    public TreeCacheImpl(String uniquePrefix, String key, String superId) {
        super(uniquePrefix, key, superId, -1);
    }
    public static TreeCacheImpl getInstance(){
        if(treeCacheImpl==null){
            treeCacheImpl= new TreeCacheImpl("tree","id","superId");
        }
         return treeCacheImpl;
    }

    @Override
    public List<SysTree> load() {
        SysTree st = new SysTree();
        st.setStatus(SysTree.STATUS_ENABLE);
        return IocUtils.getBean(SysTreeService.class).query(st);
    }
    /**
     * 获取完整的树结构
     * @param id 起始节点
     * @author dai.jiaqi
     * @date 2016-6-30 上午11:50:58
     */
    public static List<SysTree> getTreeListChildren(String id, Map<String, List<String>> superMap, String type) {
        type = StringUtil.trim(type);
        List<SysTree> list = new ArrayList<>();
        List<SysTree> listTmp = getChildren(id, 1, superMap);
        if (listTmp == null || listTmp.size() == 0) {
            return listTmp;
        }
        if (type.length() == 0) {
            list = listTmp;
        } else {
            for (SysTree sysTree : listTmp) {
                if (sysTree.getType().equals(type)) {
                    list.add(sysTree);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            List<SysTree> listChildren = getChildren(list.get(i).getId(), 1, superMap);
            if (listChildren != null) {
                if(listChildren.size()>0){//纠正父节点对应不上的问题，
                    for (int j=0;j<listChildren.size();j++ ) {
                        String superId= StringUtil.trim( listChildren.get(j).getSuperId());
                        if(!superId.equals(StringUtil.trim(list.get(i).getId()))){
                            listChildren.get(j).setSuperId(list.get(i).getId());
                        }
                    }
                }

                list.get(i).setList(listChildren);
//                for (int j = 0; j < listChildren.size(); j++) {
//                    if(!id.equalsIgnoreCase(listChildren.get(j).getId())){
//                        listChildren.get(j).setList(getTreeListChildren(listChildren.get(j).getId(), superMap, type));
//                    }
//                }
            }
        }
        return list;
    }

    /**
     * 根据叶子节点，返回对应的整个树
     * 如果type==100000值返回工会，如果200000返回企业
     * @param ids 叶子节点
     * @param type 类型 100000工会 200000企业
     * @return 完整的树
     */
    public static List<SysTree> getTreeListSuper(List<String> ids,String type) {
        Map<String, List<String>> superMap = new HashMap<>();
        getTreeSuperMap(superMap, ids,type);
        List<SysTree> list = getChildren(ROOT_ID, 1, superMap);
        for (int i = 0; i < list.size(); i++) {
            List<SysTree> listChildren = getChildren(list.get(i).getId(), 1, superMap);
            if (listChildren != null) {
                for (int j = 0; j < listChildren.size(); j++) {
                    listChildren.get(j).setList(getTreeListChildren(listChildren.get(j).getId(), superMap, type));
                }
                list.get(i).setList(listChildren);
            }
        }
        return list;
    }


    /**
     * 根据叶子节点，返回对应的整个树 - 带权限和标示
     * 如果type==100000值返回工会，如果200000返回企业
     * 支持是否向下兼容
     * @param idsMap <treeid,compatibility> compatibility 0代表向下兼容，1代表不向下兼容
     * @param type 类型 100000工会 200000企业
     * @return 完整的树
     */
    public static List<SysTree> getTreeListSuper(Map<String,String> idsMap ,String type) {


        List<String> ids = getAuthIds(idsMap,type);


        //向下兼容结束
        Map<String, List<String>> superMap = new HashMap<>();

        getTreeSuperMap(superMap, ids,type);



        List<SysTree> list = getChildren(ROOT_ID, 1, superMap);

        /**
         * 未认证的用户显示在“下岗人员”节点下，“下岗人员”节点只有平台管理员能看见
         */

        if (StringUtils.isNotBlank(UserUtil.getRoleNames())) {
            if (CollectionUtils.isNotEmpty(list)) {
                if (!UserUtil.getRoleNames().contains(SysUserAdmin.PLATFORM_ADMIN)) {

                    for(int i=0;i<list.size();i++){
                        if(list.get(i).getName().equals("下岗人员"))
                            list.remove(i);
                    }

                }
            }
        }


        for (int i = 0; i < list.size(); i++) {
            List<SysTree> listChildren = getChildren(list.get(i).getId(), 1, superMap);
            if (listChildren != null) {
                for (int j = 0; j < listChildren.size(); j++) {
                    listChildren.get(j).setList(getTreeListChildren(listChildren.get(j).getId(), superMap, type));
                    //纠正父节点对应不上的问题，
                    String superId= StringUtil.trim( listChildren.get(j).getSuperId());
                    if(!superId.equals(StringUtil.trim(list.get(i).getId()))){
                        listChildren.get(j).setSuperId(list.get(i).getId());
                    }
                }
                list.get(i).setList(listChildren);
            }
        }


        setAuth(list,ids);
        return list;
    }

    /**
     * 获取有权限的节点
     * @param idsMap 权限map
     * @param type 类型
     * @return 有权限的节点ID集合
     */
    private static List<String> getAuthIds(Map<String,String> idsMap,String type){
        List<String> ids = new ArrayList<>();//设置标记
        //处理是否向下兼容开始
        if(idsMap==null ){
            return null;
        }
        Iterator<String> keys = idsMap.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            String valueCompatibility = idsMap.get(StringUtils.trim(key));
            if(valueCompatibility.equals("0")){//向下兼容


//                List<SysTree> childRen = getChildren( key,-1,null); old
                List<SysTree> childRen = new ArrayList<SysTree> ();
                getTreeListAndChildren(get(key) ,childRen);

                for (SysTree sysTree:childRen) {
                    if(StringUtil.trim(type).length()>0){
                        if(StringUtil.trim(type).equals(sysTree.getType())){
                            ids.add(sysTree.getId());
                        }
                    }else {
                        ids.add(sysTree.getId());
                    }
                }

                ids.add(key);
            }else{//不向下兼容
                SysTree st =get(key);
                if(StringUtil.trim(type).length()>0){
                    if(st!=null &&StringUtil.trim(type).equals(st.getType())){
                        ids.add(st.getId());
                    }
                }else {
                    ids.add(st.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 逐级获取子节点 0814
     * @param sysTree
     * @return
     */
    public static  void getTreeListAndChildren( SysTree sysTree ,List<SysTree> sysTrees){
            List<SysTree> list = getChildren(
                    sysTree.getId(), 1,null);
//            sysTree.setList(list);
          sysTrees.addAll(list);
            for (int i = 0; i < list.size(); i++) {
                List<SysTree> listChildren = getChildren(list.get(i).getId(), 1, null);
                if (listChildren != null && listChildren.size()>0) {
                    sysTrees.addAll(listChildren);
                    for (int j = 0; j < listChildren.size(); j++) {
                        getTreeListAndChildren(listChildren.get(j), sysTrees);
                    }
                }
            }
    }


    /**
     * 根据叶子节点，返回对应的整个树 - 带权限和标示
     * 如果type==100000值返回工会，如果200000返回企业
     * 支持是否向下兼容
     * @param idsMap <treeid,compatibility> compatibility 0代表向下兼容，1代表不向下兼容
     * @param type 类型 100000工会 200000企业
     * @return 完整的树
     */
    public static List<SysTree> getTreeListSuperOld(Map<String,String> idsMap ,String type) {
        List<String> ids = new ArrayList<>();//设置标记
        //处理是否向下兼容开始
        if(idsMap==null ){
            return null;
        }
        Iterator<String> keys = idsMap.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            String valueCompatibility = idsMap.get(StringUtils.trim(key));
            if(valueCompatibility.equals("0")){//向下兼容
                List<SysTree> childRen = getChildren( key,-1,null);
                for (SysTree sysTree:childRen) {
                    if(StringUtil.trim(type).length()>0){
                        if(StringUtil.trim(type).equals(sysTree.getType())){
                            ids.add(sysTree.getId());
                        }
                    }else {
                        ids.add(sysTree.getId());
                    }
                }
            }else{//不向下兼容

                SysTree st =get(key);
                if(StringUtil.trim(type).length()>0){
                    if(st!=null &&StringUtil.trim(type).equals(st.getType())){
                        ids.add(st.getId());
                    }
                }else {
                    ids.add(st.getId());
                }
            }
        }
        //向下兼容结束
        Map<String, List<String>> superMap = new HashMap<>();
        getTreeSuperMap(superMap, ids,type);
        List<SysTree> list = getChildren(ROOT_ID, 1, superMap);
        for (int i = 0; i < list.size(); i++) {
            List<SysTree> listChildren = getChildren(list.get(i).getId(), 1, superMap);
            if (listChildren != null) {
                for (int j = 0; j < listChildren.size(); j++) {
                    listChildren.get(j).setList(getTreeListChildren(listChildren.get(j).getId(), superMap, type));
//                    if(listChildren.size()>0){//纠正父节点对应不上的问题，
//                        for (int j=0;j<listChildren.size();j++ ) {
                            String superId= StringUtil.trim( listChildren.get(j).getSuperId());
                            if(!superId.equals(StringUtil.trim(list.get(i).getId()))){
                                listChildren.get(j).setSuperId(list.get(i).getId());
                            }
//                        }
//                    }

                }
                list.get(i).setList(listChildren);
            }
        }
        setAuth(list,ids);
        return list;
    }

    /**
     *  设置权限标记
     * @param sysTrees 树集合
     * @param haveRightsIds 有权限的树ID集合
     */
    private static void setAuth(List<SysTree> sysTrees ,  List<String> haveRightsIds ){
        if(sysTrees==null){
            return ;
        }
        for (SysTree st: sysTrees) {
            if(haveRightsIds.contains(st.getId())){
                st.setAuthFlag(SysTree.AUTHFLAG_HAVE);
            }
            if(st.getList().size()>0){
                setAuth(st.getList(),haveRightsIds);
            }
        }
    }

    /**
     * 根据节点ID获取 所有分支上的子节点ID集合
     * @param id 节点ID
     * @return 子节点ID 集合
     */
    public static List<String> getTreeLastChild(String id) {
        List<String> result =new ArrayList<>();
        List<SysTree> sysTrees = getChildren(id, 1, null);
        if (sysTrees == null) {
            return result;
        }
        for (SysTree s : sysTrees) {
            List<String> tmp = getChildren(s.getId(), 1, null);
            if (tmp == null || tmp.size() == 0) {
                result.add(s.getId());
            } else {
                result.addAll(getTreeLastChild(s.getId()));
            }
        }
        return result;
    }

    /**
     * 获取以父ID 为key的 map
     * @param mapChildren  父亲，list<孩子>
     * @param ids
     */
    private static void getTreeSuperMap(Map<String, List<String>> mapChildren, List<String> ids,String type ) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        for (String id : ids) {
            List<String> superIds = getSuperIds(id);
            SysTree cst = get(id);
            if (superIds != null && superIds.size() > 0) {
                for (String superId : superIds) {
                    if(StringUtil.trim(type).length()>0){
                        if(cst!=null && !cst.getType().equals(type)){//当前节点
                            continue;
                        }
                        SysTree st = get(superId);
                        if(st==null || !st.getType().equals(type)){//当前节点
                            superId="-1";
                        }
                        //
//                        SysTree superSysTree = get(st.getSuperId());
//                        if(superSysTree!=null && !superSysTree.getType().equals(type)){
//                            superId="-1";
//                        }
                    }

                    //如果父亲节点是 工会要换成 -1
                    if (mapChildren.get(superId) == null) {
                        mapChildren.put(superId, new ArrayList<>());
                    }
                    if(!mapChildren.get(superId).contains(id)){
                        mapChildren.get(superId).add(id);
                    }

                }
//                //id特殊处理
                if(StringUtil.trim(type).length()>0) {
                    SysTree st = get(id);
                    String superId=st.getSuperId();
                    if (st != null && st.getSuperId() != null) {
                        if(!superId.equals("-1")){
                            SysTree superSysTree = get(superId);
                            if(superSysTree!=null && !superSysTree.getType().equals(type)){
                                superId="-1";
                            }
                        }
                        if(st.getType().equals(type)){
                            if (mapChildren.get(superId) == null) {
                                mapChildren.put(superId, new ArrayList<>());
                            }
                            if(!mapChildren.get(superId).contains(id)){
                                mapChildren.get(superId).add(id);
                            }
                        }
                    }
                }

//                getTreeSuperMap(mapChildren, superIds,type);
            }
        }
    }
}
