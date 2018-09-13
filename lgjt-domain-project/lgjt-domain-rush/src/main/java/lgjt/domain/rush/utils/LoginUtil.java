package lgjt.domain.rush.utils;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.utils.StaticUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * show 登录工具类.
 * @author daijiaqi
 * @date 2018/5/2216:46
 */
public class LoginUtil {
    public static final String AUTHS = "AUTHS";
    /**
     * show 根据请求头中的header 获取当前用户???????????临时.
     * @author daijiaqi
     * @date 2018/5/2216:46
     * @return 用户信息 UserLoginInfo
     */
    public static UserLoginInfo getUserLoginInfoTest() {
        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        if (token == null) {
            token = "10000";
        }
        if (token == null || StringUtils.trim(token).length() == 0) {
            return null;
        }
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();

        UserLoginInfo loginInfo= loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);
        return loginInfo;
    }

    /**
     * show 根据请求头中的header 获取当前用户.
     * @return 用户信息 UserLoginInfo
     */
    public static UserLoginInfo getUserLoginInfo() {
        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
//        if (token == null) {
//            token = "1";
//        }
        if (token == null || StringUtils.trim(token).length() == 0) {
            return null;
        }
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        return loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getAuth() {
        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        IObjectCache cache = CacheFactory.getObjectCache();
        return cache.get(AUTHS+token) != null ? (Set<String>) cache
                .get(AUTHS+token) : null;
    }

    public static String getAuthString() {
        Set<String> auths= getAuth();
        if(auths==null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Iterator<String> keys=auths.iterator();
        while (keys.hasNext()){
            String key =keys.next();
            if(sb.length()>0){
                sb.append(";");
            }
            sb.append(key);
        }
        return ";"+sb.toString()+";";

    }

}
