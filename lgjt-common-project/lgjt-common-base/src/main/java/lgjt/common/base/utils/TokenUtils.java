package lgjt.common.base.utils;


import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;

/**
 * 基于Redis操作的会话存储实现
 * 原有的session用token替代的一种方式
 * Created by zhanglongping on 2017/9/14 0014.
 */
public class TokenUtils {
    /**
     * 设置属性
     * @param key
     * @param value
     * @return
     */
    public Object setAttribute(String key,Object value){

        //请求合法性校验
//        String token = Mvcs.getReq().getHeader(StaticUtils);
//        if(StringUtils.isBlank(token)){
//            throw new RuntimeException("令牌为空，服务无法提供响应");
//        }

        //将数据保存至redis
        IObjectCache cache = CacheFactory.getObjectCache();
        cache.set(key,value);
        return cache.get(key);
    }

    /**
     * 获取属性
     * @param key
     * @return
     */
    public Object getAttribute(String key){
        //从redis获取对象按key
        IObjectCache cache = CacheFactory.getObjectCache();
        return cache.get(key);
    }
    
    public void removeAttribute(String key){
    	IObjectCache cache = CacheFactory.getObjectCache();
        cache.del(key);
    }
}
