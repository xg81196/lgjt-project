package lgjt.services.backend;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IStringCache;
import com.ttsx.util.cache.ObjectCache;
import com.ttsx.util.cache.StringCache;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.mvc.Mvcs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lgjt.common.base.utils.IocUtils;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.utils.StaticUtils;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.org.SysUnionComService;
import lgjt.services.backend.org.SysUnionService;
import lgjt.services.backend.user.SysUserDataService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author wuguangwei
 * @date 2018/6/13
 * @Description: 加入对数据范围支持
 */

public class BaseService2 extends BaseService {



    /**
     * 日志对象
     */
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 数据范围过滤
     * @param user 当前用户对象
     * @return 标准连接条件对象
     */
    public  String dataScopeFilter(SysUserAdmin user) {


        // 超级管理员，跳过权限过滤
        if (!user.isAdmin(user.getUserName())){

            return getSQLFilter(user);

        }

        return "";
    }



    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserAdmin user){

        //获取缓存数据权限

        String unionIds = UserUtil.getAdminUnionUserData();

        String comIds = UserUtil.getAdminComUserData();

        StringBuilder sqlFilter = new StringBuilder();

        if( StringUtils.isNotBlank(unionIds)){
            StringBuilder sb = new  StringBuilder();
            String[] ids = unionIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                sb.append("'"+ids[i]+"'");//拼接单引号,到数据库后台用in查询.
                if(i!=ids.length-1){//前面的元素后面全拼上",",最后一个元素后不拼
                    sb.append(",");
                }
            }
            sqlFilter.append(" and (");
            sqlFilter.append("(u.").append("union_id").append(" in (").append(sb.toString()).append("))");
        }

        //没有本企业数据权限，也能查询本人数据
        if(StringUtils.isNotBlank(comIds)){
            if (sqlFilter.length()>0) {
                sqlFilter.append(" or ");
            }else {
                sqlFilter.append(" and (");
            }

            StringBuilder sb = new  StringBuilder();
            String[] ids = comIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                sb.append("'"+ids[i]+"'");//拼接单引号,到数据库后台用in查询.
                if(i!=ids.length-1){//前面的元素后面全拼上",",最后一个元素后不拼
                    sb.append(",");
                }
            }

            sqlFilter.append("(u.").append("com_id").append(" in (").append(sb.toString()).append("))");
        }

        sqlFilter.append(")");

        return sqlFilter.toString();
    }


}
