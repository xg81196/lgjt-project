package lgjt.services.backend.role;

import com.google.common.collect.Maps;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IStringCache;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.map.HashedMap;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.role.SysRole;
import lgjt.domain.backend.role.SysRoleMenu;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.domain.backend.user.vo.SysUserAdminVo;
import lgjt.domain.backend.utils.UserUtil;

import java.util.*;

/**
 * @author wuguangwei
 * @date 2018/4/17
 * @Description: 查询角色树 为角色分配菜单权限和人员
 */

@Log4j
@IocBean
public class SysRoleService  extends BaseService{

    private static final String ROLE_USERS = "sys.admin.roleUsers";

    private static final String QUERY_ROLE_USERS = "sys.sysrole.queryRoleUser";

    private static final String QUERY_USER_ROLES = "sys.sysrole.queryUserRole";


    //系统角色id，用于限制删除
    private static final String RoleNotDelId="1";


    /**
     *
     * @return
     */

    public List<SysRole> queryUserRole() {

        Map<String,Object> param = Maps.newHashMap();

        param.put("userId",UserUtil.getAdminUser().getId());
        return super.query(QUERY_USER_ROLES,SysRole.class , null,param);
    }

    /**
     * 分页查询角色
     * @param obj
     * @return
     */
    public PageResult<SysRole> queryPage(SysRole obj) {
        return super.queryPage(SysRole.class, obj, getCri(obj));
    }

    public int update(SysRole obj){
        int i=super.updateIgnoreNull(obj);
        List<SysRole> srList=new ArrayList<SysRole>();
        if(i>0&&obj.getStatus()==0){
            srList=queryAllNode(srList,obj.getId());
            if(srList!=null&&srList.size()>0){
                for(SysRole sr : srList){
                    sr.setStatus(0);
                    super.updateIgnoreNull(sr);
                }

            }
        }
        return i;
    }


    /**
     * 递归查询某一树节点下的所有子节点
     * @param srList
     * @param id
     * @return
     */
    public List<SysRole> queryAllNode(List<SysRole> srList,String id){
//		List<SysRole> srList=new ArrayList<SysRole>();
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("super_id", id);
        List<SysRole> sysRole=super.query(SysRole.class, cri);
        if(sysRole!=null&&sysRole.size()>0){
            srList.addAll(sysRole);
            for (SysRole sysRole2 : sysRole) {
                queryAllNode(srList,sysRole2.getId());
            }

        }
        return srList;
    }



    /**
     * 添加角色
     * @param obj
     * @return
     */
    public SysRole insert(SysRole obj,String userName) {
        SimpleCriteria cri= Cnd.cri();
        cri.where().andEquals("role_name", obj.getRoleName());
        SysRole sr=super.fetch(SysRole.class, cri);
        if(sr!=null){
            return null;
        }
        obj.setCrtTime(new Date());
        obj.setCrtUser(userName);
        obj.setCrtIp(ClientInfo.getIp());
        return super.insert(obj);
    }

    /**
     * 全表查询
     * @param obj
     * @return
     */
    public List<SysRole> query(SysRole obj) {
        return super.query(SysRole.class, getCri(obj));
    }

    /**
     * 删除某角色下的用户
     * @param ids
     * @param roleId
     */
    public String deleteRoleUser(String ids,String roleId ,SysUserAdmin sysUser) {

        if(StringTool.isNotNull(ids) && StringTool.isNotNull(roleId)) {
            String arr[] = ids.split(",");
            for (String string : arr) {
                if(sysUser.getUserName().equals(string)){
                    return "删除人员为当前登录人,不可删除";
                }
            }
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("user_name", arr);
            cri.where().andEquals("role_id", roleId);
            super.delete(SysUserRole.class, cri);
            return "";
        }
        return "";
    }


    /**
     * 为角色分配资源
     * @param roleId
     * @param resIds
     */
    public String insertRoleRes(String roleId, String resIds ,String username) {

        Criteria cri = Cnd.cri();
        cri.where().andEquals("ROLE_ID", roleId);
        super.dao.clear(SysRoleMenu.class, cri);
        String[] resIdsSplit = resIds.split(",");
        for (String resId : resIdsSplit) {
            SysRoleMenu roleRight = new SysRoleMenu();
            roleRight.setRoleId(roleId);
            roleRight.setMenuId(resId);
            roleRight.setCrtIp(ClientInfo.getIp());
            roleRight.setCrtUser(username);
            roleRight.setCrtTime(new Date());
            super.insert(roleRight);
        }
        return "";
    }

    /**
     * 为用户分配角色
     * @param roleId
     * @param userIds
     */
    public String insertRoleUser(String roleId, String userIds ,SysUserAdmin sysUser) {

        try {
            String msg = null;
            String[] userIdsSplit = userIds.split(",");
            for (String userId : userIdsSplit) {
                Criteria cri = Cnd.cri();
                cri.where().andEquals("id", userId);
                SysUserAdmin su=super.fetch(SysUserAdmin.class, cri);
                if(su==null){
                    msg = "添加角色失败！获取人员信息失败！" ;
                    return msg;
                }

                cri = Cnd.cri();
                cri.where().andEquals("role_id", roleId);
                cri.where().andEquals("user_name", su.getUserName());
                Integer count = super.dao.count(SysUserRole.class, cri);
                if (count ==0) {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setRoleId(roleId);
                    userRole.setAdminUserId(su.getId());
                    userRole.setUserName(su.getUserName());
                    userRole.setCrtIp(ClientInfo.getIp());
                    userRole.setCrtUser(sysUser.getUserName());
                    userRole.setCrtTime(new Date());
                    super.insert(userRole);
                }
            }
            return msg;
        } catch (Exception e) {
            log.error(e,e);
            return "操作失败！";
        }
    }


    /**
     * 查询改角色对应的用户
     * @param roleId
     * @return
     */
    public PageResult<SysUserAdminVo> queryRoleUser(String roleId,String username) {

        Criteria cri = Cnd.cri();
        cri.where().andEquals("urm.role_id", roleId);

        if(StringUtil.isNotNull(username)) {
            cri.where().andLike("ui.user_name", username);
        }
        /**
         * /*SELECT ur.* FROM ( SELECT user_data.* FROM sys_user_data user_data WHERE user_data.object_id IN (@orgId)) ud LEFT JOIN (
         SELECT ui.* FROM sys_user_admin ui LEFT JOIN sys_user_role urm ON ui.id = urm.admin_user_id
         $condition ) ur ON ur.id = ud.admin_user_id*/


      /*  String unionComIds = UserUtil.getAdminUnionAndComUserData();

        Map<String,Object> param = new HashMap<>(1);
        param.put("orgId",unionComIds.split(","));*/
        //cri.where().andEquals("ui.crt_user", username);
        return this.queryPage(QUERY_ROLE_USERS,SysUserAdminVo.class,new SysUserAdmin(),cri,null);

    }

    /**
     * 使用用户名查询
     * @param userName
     * @return
     */
    public PageResult<SysUserAdminVo> queryRoleUserByName(String userName) {
        Criteria cri = Cnd.cri();
        if(StringUtil.isNotNull(userName)) {
            cri.where().andLike("ui.user_name", userName);
        }
        return this.queryPage(QUERY_ROLE_USERS,SysUserAdminVo.class,new SysUserAdmin(),cri,null);
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    public String delete(String ids) {
        String msg =null;
        if(StringTool.isNotNull(ids)) {
            String arr[] = ids.split(",");
            for (int i = 0; i < arr.length; i++) {
                String s = RoleNotDelId;
                if(s.contains(arr[i])){
                    msg = "删除失败，该角色为系统默认角色，禁止删除！";
                    return msg;
                }
            }
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("roleId", arr);
            if(null != super.fetch(SysUserRole.class, cri)) {
                msg = "删除失败！该角色下有用户，禁止删除！";
                return msg;
            }
            cri = Cnd.cri();
            cri.where().andIn("id", arr);
            super.delete(SysRole.class, cri);
        }
        return msg;
    }

    /**
     * 权限分配查询角色树
     * @return
     */
    public List<SysRole> queryRoleTree(String state,String name,SysUserAdmin sysUser) {

        Map<String,List<SysRole>> p2son = getP2son(state,name, sysUser);
        List<SysRole> result = new ArrayList<>();
        if(p2son.containsKey(SysRole.ROOT)) {
            for(SysRole org:p2son.get(SysRole.ROOT)) {
                getSub(org,p2son);
//				this.setSelectedRes(org);
                result.add(org);
            }
        }
        SysRole sr = getFixedRole();
        sr.setList(result);
        List<SysRole> list = new ArrayList<SysRole>();
        list.add(sr);
        return list;
    }


    public String queryCheckedRess(String roleId) {
        Criteria cri = Cnd.cri();
        cri.where().andEquals("roleId", roleId);
        List<SysRoleMenu> roleRights = super.dao.query(
                SysRoleMenu.class, cri);
        String checkedResString = "";
        if (roleRights.size() > 0) {
            for (SysRoleMenu rm : roleRights) {
                checkedResString += rm.getMenuId() + ",";
            }
        }
        return checkedResString;
    }

    private SysRole getFixedRole() {
        SysRole role = new SysRole();
        role.setId("-1");
        role.setRoleName("角色树");
        return role;
    }

    /**
     * 查询角色树
     * @return
     */
    public List<SysRole> listTree(SysRole obj,SysUserAdmin sysUser) {
        Map<String,List<SysRole>> p2son = getP2son(null,obj.getRoleName(),sysUser);

        List<SysRole> result = new ArrayList<>();
        if(p2son.containsKey(SysRole.ROOT)) {
            for(SysRole org:p2son.get(SysRole.ROOT)) {
                getSub(org,p2son);
                result.add(org);
            }
        }

        SysRole sr = getFixedRole();
        sr.setList(result);
        List<SysRole> list = new ArrayList<SysRole>();
        list.add(sr);
        return list;
    }

    /**
     * 查询角色树
     * @param state
     * @return
     */
    private Map<String,List<SysRole>> getP2son(String state,String name,SysUserAdmin sysUser) {
        List<SysRole> list=new ArrayList<>();
        SimpleCriteria cri = Cnd.cri();
        if(sysUser!=null&&sysUser.getUserName().equals("admin")){
            cri.getOrderBy().desc("sort");
           /* if(StringTool.isNotNull(state)) {
                cri.where().andEquals("status", state);

            }*/
            if(StringTool.isNotEmpty(name)) {
                cri.where().andEquals("role_name", name);
            }
            list =  super.query(SysRole.class,cri);
        }else{

            //获取属于当前登录人的角色
            String superId=getSuperId(sysUser);
            cri = Cnd.cri();
            cri.where().andIn("id", superId.split(","));
            List<SysRole> list1 =  super.query(SysRole.class,cri);
            SysRole sList = list1.get(0);
            sList.setSuperId("-1");

            /*
            2018年07月27日15:14:02 赵天意
            BUG 11315 写死，平台管理员查询所有
             */

            if(sList.getRoleName().equals("平台管理员")) {
                cri = Cnd.cri();
                cri.getOrderBy().desc("sort");
                if(StringTool.isNotEmpty(name)) {
                    cri.where().andEquals("role_name", name);
                }
                list =  super.query(SysRole.class,cri);
            } else {
                cri = Cnd.cri();
                cri.getOrderBy().desc("sort");
            /*if(StringTool.isNotNull(state)) {
                cri.where().andEquals("status", state);
            }*/
                if(StringTool.isNotEmpty(name)) {
                    cri.where().andEquals("role_name", name);
                }

                cri.where().andIn("super_id", superId.split(","));
                list =  super.query(SysRole.class,cri);
                if(list!=null&&list.size()>0&&list1!=null&&list1.size()>0){
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list1.size(); j++) {
                            if(list.get(i).getId().equals(list1.get(j).getId())){
                                list1.remove(j);
                            }
                        }
                    }

                }
                list.addAll(list1);
            }
        }
        for (SysRole sr : list) {
            this.setSelectedRes(sr);
        }
        Map<String,List<SysRole>> p2son = new HashMap<>();

        for(SysRole org:list) {
            if(p2son.containsKey(org.getSuperId())) {
                p2son.get(org.getSuperId()).add(org);
            }else {
                List<SysRole> sons = new ArrayList<>();
                sons.add(org);
                p2son.put(org.getSuperId(), sons);
            }
        }
        return p2son;
    }

    private void getSub(SysRole org,Map<String,List<SysRole>> p2son) {
        if(p2son.containsKey(org.getId())) {
            for(SysRole obj:p2son.get(org.getId())) {
                getSub(obj,p2son);
            }
            org.setList(p2son.get(org.getId()));
        }
    }

    public String getSuperId(SysUserAdmin sysUser){
        String str="";
        List<String> strId=new ArrayList<>();
        StringBuffer sb=new StringBuffer();
        StringBuffer sb1=new StringBuffer();
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("user_name", sysUser.getUserName());
        List<SysUserRole> sur=super.query(SysUserRole.class,cri);
        cri=Cnd.cri();
        cri.where().andEquals("crt_user", sysUser.getUserName());
        List<SysRole> sr=super.query(SysRole.class,cri);
        if(sur!=null&&sur.size()>0){
            for (int i = 0; i < sur.size(); i++) {
                strId.add(sur.get(i).getRoleId());
            }
        }
        if(sr!=null&&sr.size()>0){
            for (int j = 0; j < sr.size(); j++) {
                strId.add(sr.get(j).getId());
            }
        }
        if(strId!=null&&strId.size()>0){
            for (int i = 0; i < strId.size(); i++) {
                sb.append(strId.get(i));
                if(i<strId.size()-1){
                    sb.append(",");
                }
            }
            str=sb.toString();
            return str;
        }

        return str;
    }


    private void setSelectedRes(SysRole role) {
        String roleId = role.getId();
        role.setData(this.queryCheckedRess(roleId));
    }


    private SimpleCriteria getCri(SysRole obj) {
        SimpleCriteria cri = Cnd.cri();

        if(StringTool.isNotNull(obj.getRoleName())) {
            cri.where().andLike("role_name", obj.getRoleName());
        }
        if(StringTool.isNotNull(obj.getStatus())) {
            cri.where().andEquals("status", obj.getStatus());
        }
        if(StringTool.isNotNull(obj.getSort())) {
            cri.where().andEquals("sort", obj.getSort());
        }

        if(StringTool.isNotNull(obj.getType())) {
            cri.where().andEquals("type", obj.getType());
        }

        if(StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if(StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
        }
        if(StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if(StringTool.isNotNull(obj.getUpdUser())) {
            cri.where().andEquals("upd_user", obj.getUpdUser());
        }
        if(StringTool.isNotNull(obj.getUpdTime())) {
            cri.where().andEquals("upd_time", obj.getUpdTime());
        }
        if(StringTool.isNotNull(obj.getUpdIp())) {
            cri.where().andEquals("upd_ip", obj.getUpdIp());
        }
        cri.getOrderBy().desc("sort").desc("crt_time");
       // cri.desc("sort");
        return cri;
    }

    public SysRole checkRole_name(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("role_name",value);
        return super.fetch(SysRole.class,cri);
    }

}
