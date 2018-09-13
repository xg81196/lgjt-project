package lgjt.web.backend.init;

import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import lgjt.common.base.utils.RedisKeys;

import java.util.Map;

/**
 * @author wuguangwei
 * @date 2018/5/3
 * @Description: 单利
 */

public class SysDictRedis {

    private static final SysDictRedis INSTANCE = new SysDictRedis();

    private SysDictRedis() {

    }

    public static SysDictRedis getInstance() {
        return INSTANCE;
    }

    public void saveOrUpdate(String redisKey, Map<String,String> map) {

        IObjectCache objectCache = ObjectCache.getInstance();

        if(map == null){
            return ;
        }
        String key = RedisKeys.getSysDictKey(redisKey);
        objectCache.set(key, map);
    }

    public void saveOrUpdate4User(String redisKey, Map<String,String> map) {

        IObjectCache objectCache = ObjectCache.getInstance();

        if(map == null){
            return ;
        }
        String key = RedisKeys.getSysDictKey4User(redisKey);
        objectCache.set(key, map);
    }

    public void delete(String dictKey) {
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysDictKey(dictKey);
        objectCache.del(key);
    }

    public Map<String,String> get(String dictKey){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysDictKey(dictKey);
        return (Map<String,String>)objectCache.get(key);
    }
    public Map<String,String> get4User(String dictKey){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysDictKey(dictKey);
        return (Map<String,String>)objectCache.get(key);
    }

}
