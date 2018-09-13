package lgjt.web.backend.datascope;


import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.aop.interceptor.AbstractMethodInterceptor;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.utils.IocUtils;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.domain.backend.utils.UserUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * @author wuguangwei
 * 数据过滤，切面处理类
 */
@IocBean(name = "dataFilterScopeInterceptor")
public class DataScopeFilterInterceptor extends AbstractMethodInterceptor {

    /**
     * 方法调用前进行拦截，遍历参数进行验证
     * @return
     */
    public boolean beforeInvoke(Object obj, Method method, Object... args) {


        Object params = args[1];

        if(params != null && params instanceof Map){

            SysUserAdmin user = UserUtil.getAdminUser();

            //未登录，直接抛出异常信息
            Preconditions.checkNotNull(user,"当前用户为空");

            //如果不是超级管理员，则进行数据过滤
            if(!SysUserAdmin.SUPER_ADMIN.equals(user.getId())){
                Map map = (Map)params;
                map.put(SysUserAdmin.SQL_FILTER, getFilterSQL(user));
            }

            return true;
        }

        throw new RuntimeException("要实现数据权限接口的参数，只能是Map类型，且不能为NULL");
    }


    /**
     * @author wuguangwei
     *
     * 获取数据过滤的SQL
     */

    private String getFilterSQL(SysUserAdmin user){


        SysUserDataService dataPermissionScopeService = IocUtils.getBean(SysUserDataService.class);

        //查询管理员权限
        SimpleCriteria cri= Cnd.cri();
        cri.where().andEquals("admin_user_id", user.getId()).andEquals("object_type",0);

        List<SysUserData> dataPermissionScopes = dataPermissionScopeService.query(SysUserData.class,cri);

        //如果不是平台管理员，则只能查询本机构数据
        if (CollectionUtils.isEmpty(dataPermissionScopes)  )  {

            return dataPermissionScopes.stream().map(SysUserData :: getObjectId).collect(joining(","));

        }

        return null;


    }




}

