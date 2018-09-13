package lgjt.common.base.utils;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.constants.ConstantsCommon;

import java.util.Iterator;
import java.util.Set;

/**
 * show 登录工具类.
 *
 * @author daijiaqi
 * @date 2018/5/2216:46
 */
public class LoginUtil {


    /**
     * show 根据请求头中的header 获取当前用户???????????临时.
     * @author daijiaqi
     * @date 2018/5/2216:46
     * @return 用户信息 UserLoginInfo
     */
//    public static UserLoginInfo getUserLoginInfoTest() {
//        String token = Mvcs.getReq().getHeader(LetterConfig.HEAD_TOKEN_NAME);
//        if (token == null) {
//            token = "10000";
//        }
//        if (token == null || StringUtils.trim(token).length() == 0) {
//            return null;
//        }
//        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
//
//        UserLoginInfo loginInfo= loginInfoCache.getLoginInfo(LetterConfig.REDIS_PREFIX_LOGIN + token);
//        if(loginInfo==null){
//            loginInfo=  loginInfoCache.login("login-10000","wangpeizhi", "王培志", "127.0.0.1",  null);
//        }
//        return loginInfo;
//    }


    /**
     * 从请求头中获取token
     *
     * @return 用户信息 UserLoginInfo
     * @author daijiaqi
     * @date 2018/5/22 16:46
     */
    public static String getToken() {
        return Mvcs.getReq().getHeader(ConstantsCommon.HEAD_TOKEN_NAME);
    }

    /**
     * 用户登录用户名
     * 如果登录用户为空，返回null
     * @return 用户登录名
     */
    public static String getUserName() {
        UserLoginInfo uli = getUserLoginInfo();
        if (uli != null) {
            return uli.getUserName();
        }
        return null;
    }

    /**
     * 用户登录用户名
     * 如果登录用户为空，返回“”
     * @return 用户登录名
     */
    public static String getUserNameTrim() {
        UserLoginInfo uli = getUserLoginInfo();
        if (uli != null) {
            return uli.getUserName();
        }
        return "";
    }

    /**
     * show 根据请求头中的header 获取当前用户.
     *
     * @return 用户信息 UserLoginInfo
     * @author daijiaqi
     * @date 2018/5/22 16:46
     */
    public static UserLoginInfo getUserLoginInfo() {
        String token = getToken();
        if (token == null || StringUtils.trim(token).length() == 0) {
            return null;
        }
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        return loginInfoCache.getLoginInfo(ConstantsCommon.TOKEN_REDIS_PREFIX + token);
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getAuth() {
        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
        IObjectCache cache = CacheFactory.getObjectCache();
        return cache.get(ConstantsCommon.USERLOGININFO_AUTHS_KEY + token) != null ? (Set<String>) cache
                .get(ConstantsCommon.USERLOGININFO_AUTHS_KEY + token) : null;
    }

    public static String getAuthString() {
        Set<String> auths = getAuth();
        if (auths == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Iterator<String> keys = auths.iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            if (sb.length() > 0) {
                sb.append(";");
            }
            sb.append(key);
        }
        return ";" + sb.toString() + ";";
    }

}
