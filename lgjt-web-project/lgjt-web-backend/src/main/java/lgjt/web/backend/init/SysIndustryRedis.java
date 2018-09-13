package lgjt.web.backend.init;

import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import lgjt.common.base.utils.RedisKeys;
import lgjt.domain.backend.industry.SysIndustry;

import java.util.List;
import java.util.Map;

/**
 * @author wuguangwei
 * @date 2018/5/3
 * @Description: redis实现行业缓存
 */

public class SysIndustryRedis {

    private static final SysIndustryRedis INSTANCE = new SysIndustryRedis();

    private SysIndustryRedis() {

    }

    public static SysIndustryRedis getInstance() {
        return INSTANCE;
    }

    public void saveOrUpdate(String redisKey, List<SysIndustry> list) {

        IObjectCache objectCache = ObjectCache.getInstance();

        if(list == null){
            return ;
        }
        String key = RedisKeys.getSysIndustryKey(redisKey);
        objectCache.set(key, list);
    }

    public void saveOrUpdate4Org(String redisKey, Map<String,String> map ) {

        IObjectCache objectCache = ObjectCache.getInstance();

        if(map == null){
            return ;
        }
        String key = RedisKeys.getSysIndustryKey4Org(redisKey);
        objectCache.set(key, map);
    }

    public void delete(String dictKey) {
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysIndustryKey(dictKey);
        objectCache.del(key);
    }

    public List<SysIndustryRedis> get(String dictKey){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysIndustryKey(dictKey);
        return (List<SysIndustryRedis>)objectCache.get(key);
    }

}
