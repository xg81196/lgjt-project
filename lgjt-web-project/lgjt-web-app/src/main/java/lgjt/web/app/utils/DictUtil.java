package lgjt.web.app.utils;

import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import lgjt.common.base.utils.RedisKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * show 词典工具类
 * @author daijiaqi
 * @date 2018/5/918:48
 */
public class DictUtil {


    /**
     * show 根据词典kYE 获取词典信息.
     * @author daijiaqi
     * @date 2018/5/918:48
     * @param dictKey 词典key
     * @return 词典信息集合(key+value)
     */
    public static Map<String,String> getDictMapByKey(String dictKey){
        IObjectCache objectCache = ObjectCache.getInstance();
        String key = RedisKeys.getSysDictKey(dictKey);
        Object obj= objectCache.get(key);

        if(obj!=null){
            return (Map<String,String>)obj;
        }else{
            return new HashMap<String,String>();
        }
    }
}
