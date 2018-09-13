package lgjt.services.app.org;


import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.CustomService;
import lgjt.domain.app.org.SysOrganization;

import java.util.*;

/**
 * show 组织机构业务类.
 * @author daijiaqi
 * @date 2018年4月23日
 */
@Log4j
@IocBean
public class SysOrganizationService extends CustomService {

    /**
     * show 根据ID获取组织机构对象.
     * @author daijiaqi
     * @date 2018年4月23日
     * @param id 组织id
     * @return id对应的组织机构对象
     */
    public SysOrganization getById(String id ){
      return  super.fetch(SysOrganization.class,id);
    }


    /**
     * 查询企业信息
     * @param obj
     * @return
     */
    public PageResult<SysOrganization> queryPage(SysOrganization obj) {
        SimpleCriteria cri = Cnd.cri();
        if(StringTool.isNotNull(obj.getName())) {
            cri.where().andLike("name", obj.getName());
        }
        return super.queryPage(SysOrganization.class, obj, cri);
    }



    /**
     * show 根据ID 获取map集合.
     * @author daijiaqi
     * @date 2018年4月23日
     * @param ids 组织机构id
     * @return ids对应的组织机构id+名称集合
     */
    public Map<String,String> getByIds(String ids ){
        Map<String,String> result =new HashMap<>();
        String[] idsArr=StringUtils.trim(ids).split(",");
        if(StringUtils.trim(ids).length()== 0 || idsArr.length==0){
            return result;
        }
        Criteria cri = Cnd.cri();
        cri.where().andIn("id",idsArr);
        List<SysOrganization>  sysOrganizations =super.query(SysOrganization.class,cri);
        if(sysOrganizations!=null){
            for(SysOrganization sysOrganization :sysOrganizations){
                result.put(sysOrganization.getId(),sysOrganization.getName());
            }
        }
        return  result;
    }

    /**
     * show 根据父节点ID 查询一级节点.
     * @param superId 父级id
     * @return 父级节点id下的一级节点组织机构对象集合
     */
    public List<SysOrganization> queryBySuperId(String superId ){
        Criteria cri = Cnd.cri();
        cri.where().andEquals("super_id",superId);
        return super.query(SysOrganization.class,cri);
    }


    public List<SysOrganization> queryORgNameForId(String ids){
        SimpleCriteria cri = Cnd.cri();
        cri.where().andIn("id",ids.split(","));
        return super.query(SysOrganization.class,cri);
    }

}
