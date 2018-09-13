package lgjt.web.backend.init;

import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.vo.SysCityVo;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.dict.SysDict;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/5/3
 * @Description: redis实现省市区缓存
 */

public class SysCityRedis {

    private static final SysCityRedis INSTANCE = new SysCityRedis();

    private SysCityRedis() {

    }

    public static SysCityRedis getInstance() {
        return INSTANCE;
    }

    public void saveOrUpdate(String redisKey, List<SysCityVo> list) {

        IObjectCache objectCache = ObjectCache.getInstance();

        if(list == null){
            return ;
        }
        String key = RedisKeys.getSysCityKey(redisKey);
        objectCache.set(key, list);
    }

    public void delete(String dictKey) {
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysCityKey(dictKey);
        objectCache.del(key);
    }

    public List<SysCityVo> get(String dictKey){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysCityKey(dictKey);
        return (List<SysCityVo>)objectCache.get(key);
    }

}
