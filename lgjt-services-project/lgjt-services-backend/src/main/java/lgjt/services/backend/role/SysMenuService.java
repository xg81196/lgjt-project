package lgjt.services.backend.role;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.backend.role.SysMenu;
import lgjt.domain.backend.role.vo.SysRoleMenuVo;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.utils.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuguangwei
 * @date 2018/4/17
 * @Description:菜单
 */

@Log4j
@IocBean
public class SysMenuService extends BaseService {

    public static final String GET_USER_MENUS = "sys.admin.getUserRoleMenuByUserId";

    /**
     * 查询菜单
     * @param sysUser
     * @return
     */
    public List<SysMenu> queryMenuTree(SysUserAdmin sysUser) {

        List<SysMenu> menus;
        Criteria cri = Cnd.cri();
        if(sysUser!=null&&!sysUser.getUserName().equals("admin")){
            cri.where().andEquals("sur.user_name", sysUser.getUserName());
            cri.getOrderBy().asc("sm.menu_sort");
            menus = super.query("sys.role.queryMenuTree",SysMenu.class, cri);
        } else{
            menus = super.query(SysMenu.class, cri);
        }

        Map<String,List<SysMenu>> p2sons = new HashMap<String,List<SysMenu>>();
        for(SysMenu menu:menus) {
            if(p2sons.containsKey(menu.getParentId())) {
                p2sons.get(menu.getParentId()).add(menu);
            }else {
                List<SysMenu> list = new ArrayList<SysMenu>();
                list.add(menu);
                p2sons.put(menu.getParentId(), list);
            }
        }
        SysMenu fixedTopRes = this.getFixedTopRes();

        List<SysMenu> topRess = p2sons.get("-1");
        if(topRess!=null&&topRess.size()>0){
            for (SysMenu topRes : topRess) {
                topRes.setList(p2sons.get(topRes.getId()));
            }
        }
        fixedTopRes.setList(topRess);
        List<SysMenu> result = new ArrayList<SysMenu>();
        result.add(fixedTopRes);
        return result;
    }

    private SysMenu getFixedTopRes() {
        SysMenu resource = new SysMenu();
        resource.setId("-1");
        resource.setName("菜单树");
        return resource;
    }


    public PageResult<SysMenu> queryPage(SysMenu obj) {
        return super.queryPage(SysMenu.class, obj, getCri(obj));
    }

    public List<SysMenu> query(SysMenu obj) {
        return super.query(SysMenu.class, getCri(obj));
    }

    public SysMenu get(String id) {
        return super.fetch(SysMenu.class, id);
    }

    public int delete(String ids) {
        if(StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("id", ids.split(","));
            return super.delete(SysMenu.class, cri);
        }
        return 0;
    }

    public SysMenu checkId(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id",value);
        return super.fetch(SysMenu.class,cri);
    }


    private SimpleCriteria getCri(SysMenu obj) {
        SimpleCriteria cri = Cnd.cri();

        if(StringTool.isNotNull(obj.getId())) {
            cri.where().andEquals("id", obj.getId());
        }
        if(StringTool.isNotNull(obj.getResCode())) {
            cri.where().andEquals("menu_code", obj.getResCode());
        }
        if(StringTool.isNotNull(obj.getName())) {
            cri.where().andEquals("menu_name", obj.getName());
        }
        if(StringTool.isNotNull(obj.getIsHeadline())) {
            cri.where().andEquals("is_headline", obj.getIsHeadline());
        }
        if(StringTool.isNotNull(obj.getImg())) {
            cri.where().andEquals("menu_img", obj.getImg());
        }
        if(StringTool.isNotNull(obj.getView())) {
            cri.where().andEquals("menu_view", obj.getView());
        }
        if(StringTool.isNotNull(obj.getOption())) {
            cri.where().andEquals("menu_options", obj.getOption());
        }
        if(StringTool.isNotNull(obj.getParentId())) {
            cri.where().andEquals("parent_id", obj.getParentId());
        }
        if(StringTool.isNotNull(obj.getAction())) {
            cri.where().andEquals("menu_action", obj.getAction());
        }
        if(StringTool.isNotNull(obj.getSort())) {
            cri.where().andEquals("sort", obj.getSort());
        }
        if(StringTool.isNotNull(obj.getUrl())) {
            cri.where().andEquals("menu_url", obj.getUrl());
        }
        return cri;
    }

    /**
     * 根据用户查询角色菜单
     * @return
     */
    public List<SysRoleMenuVo> queryUserRoleMenuByUserId( String userId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("u.id",userId);
        /*Map<String,Object> params = new HashMap<>(1);
        params.put("userId", userId);*/
        return super.query(GET_USER_MENUS,SysRoleMenuVo.class,cri);
    }

}
