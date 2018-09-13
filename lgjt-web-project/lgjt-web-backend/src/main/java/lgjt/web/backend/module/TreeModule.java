package lgjt.web.backend.module;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.nutz.common.Constants;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.ResultsImpl;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.web.backend.init.TreeCacheImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author daijiaqi
 * @date 2018/6/1911:43
 */
@At("/admin/tree")
@IocBean
@Log4j
public class TreeModule {

    @Inject
    SysUserDataService sysUserDataService;





    @At("/getTreeAndChildren")
    public Object getTreeAndChildren(@Param("id") String id ,@Param("type") String type ) {
        return  ResultsImpl.parse(Constants.STATE_SUCCESS, "",  TreeCacheImpl.getTreeListChildren(id,null,type));
    }


    @At("/getTreeAndSuper")
    public Object getTreeAndSuper(@Param("id") String id ,@Param("type") int type) {

        SysUserAdmin sysUserAdmin = UserUtil.getAdminUser();
        String tempType = type+"";
        List<SysUserData> sysUserDataList = null;
        /**
         * 管理员
         */
        if (type == -1 ) {
            tempType = "";
        }
        if (SysUserAdmin.ADMIN.equalsIgnoreCase(sysUserAdmin.getUserName()) && type == 0) {
            sysUserDataList = sysUserDataService.query(new SysUserData());
            tempType = "";
        } else {
            sysUserDataList  = sysUserDataService.getOrgsByUserId(sysUserAdmin.getId());
        }


        Map<String,String> idsMap  = new HashMap<>();

        for (SysUserData sysUserData : sysUserDataList) {
            idsMap.put(sysUserData.getObjectId(),sysUserData.getCompatibility()+"");
           // idsMap.put("633267e8-00e6-4d7d-960e-6ac0e0f5ab60",0+"");
        }



        return  ResultsImpl.parse(Constants.STATE_SUCCESS, "",  TreeCacheImpl.getTreeListSuper(idsMap,tempType));
    }


    @At("/getTreeLastChild")
    public Object getTreeLastChild(@Param("id") String id ) {

        return  ResultsImpl.parse(Constants.STATE_SUCCESS, "",  TreeCacheImpl.getTreeLastChild(id));
    }
}
