package lgjt.services.backend.org;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.tree.SysTree;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.vo.SysUnionVo;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.services.backend.city.SysCityService;
import lgjt.services.backend.industry.SysIndustryService;

import java.util.*;

/**
 * @author wuguangwei
 * @date 2018/6/13
 * @Description:
 */
@IocBean
public class SysTreeService extends BaseService {

    /**
     * 返回全部
     * @return
     */
  public List<SysTree>  query(SysTree obj){
      SimpleCriteria cri = Cnd.cri();
      if(StringTool.isNotNull(obj.getStatus())) {
          cri.where().andEquals("status", obj.getStatus());
      }
      cri.desc("sort").asc("name");

//      return super.query(SysConfig.class, cri);


    return super.query(SysTree.class,cri);
  }
}
