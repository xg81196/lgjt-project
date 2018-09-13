package lgjt.web.app.utils;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.domain.UserLoginInfo;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.mvc.Mvcs;

import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.services.app.config.SysConfigService;
import lgjt.web.app.config.AppConfig;


/**
 * show 登录工具类.
 * @author daijiaqi
 * @date 2018/5/2216:46
 */
public class LoginUtil {

	@Inject("sysConfigService")
	SysConfigService sysConfigService;

    /**
     * show 根据请求头中的header 获取当前用户.
     * @author daijiaqi
     * @date 2018/5/2216:46
     * @return 当前用户信息 UserLoginInfo
     */
    public static UserLoginInfo getUserLoginInfo() {
        String token = Mvcs.getReq().getHeader(AppConfig.HEAD_TOKEN_NAME);
        if (token == null || StringUtils.trim(token).length() == 0) {
            return null;
        }
        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        return loginInfoCache.getLoginInfo(AppConfig.REDIS_PREFIX_LOGIN + token);
    }


    /**
     * show 根据请求头中的header 获取当前用户.
     * @author daijiaqi
     * @date 2018/5/2216:46
     * @return 当前用户信息 UserLoginInfo
     */
    public static boolean loginOut() {
        boolean result = false;
        try{
            String token = Mvcs.getReq().getHeader(AppConfig.HEAD_TOKEN_NAME);
            if (token != null && StringUtils.trim(token).length() >0) {

            ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
            loginInfoCache.logout(AppConfig.REDIS_PREFIX_LOGIN + token);
                result=true;
            }
        }catch(Exception e){}
        return result;

    }
    
    public static void insertRecordToRedis(String key, HashMap<String,String> object, int temporaryLockNumber, int permanentLockNumber, int number) throws Exception{
    	IObjectCache objectCache = CacheFactory.getObjectCache();
    	
    	if(!objectCache.exist(key)) {
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("number", 1+"");
    		map.put("time", SimpleDateFormatUtils.getCurrentDate(SimpleDateFormatUtils.PATTERN_TYPE_1));
    		objectCache.set(key, map);
    	}else if(number > 0 && number <temporaryLockNumber){
    		HashMap<String, String> map = new HashMap<String, String>();
			map.put("number", number+1+"");
			map.put("time",  SimpleDateFormatUtils.getCurrentDate(SimpleDateFormatUtils.PATTERN_TYPE_1));
			objectCache.set(key, map);
    	}else if(number >= temporaryLockNumber && number < permanentLockNumber) {
    		HashMap<String, String> map = new HashMap<String, String>();
			map.put("number", number+1+"");
			map.put("time", object.get("time"));
			objectCache.set(key, map);
    	}else {
    		objectCache.del(key);
    	}
    	
    }
    
}
