package lgjt.web.app.utils;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import lgjt.common.base.service.SensitiveSearchService;
import lgjt.common.base.utils.IocUtils;

import java.util.List;
import java.util.Map;

/**
 * 初始化敏感词库
 * @author majinyong
 * @date 2017-12-15å
 */
public class SensitiveWordServiceInit {
    private static final SensitiveWordServiceInit INSTANCE = new SensitiveWordServiceInit();

    private SensitiveWordServiceInit() {

    }

    public static SensitiveWordServiceInit getInstance () {
        return INSTANCE;
    }

    @SuppressWarnings("rawtypes")
    private static Map sensitiveWordMap = null;

    /**
     * 初始化敏感词
     * @author majinyong
     */

    public void init () {
        List<String> list = initSensitivewordsByTxt();
        if (list.size() > 0){
            init1(list);
        }

    }

    /**
     * 初始化查询敏感词表
     * @author majinyong
     */
    public List<String> initSensitivewordsByTxt(){
        List<String> list = IocUtils.getBean(SensitiveSearchService.class).initSensitivewordsByTxt();
        return list;
    }

    /**
     * 构造函数，初始化敏感词库
     * @author majinyong
     */
    public void init1(List<String> list){
       sensitiveWordMap = new SensitiveWordInit().initKeyWord(list);
        IObjectCache cache = CacheFactory.getObjectCache();
        cache.set("SENSITIVE",sensitiveWordMap);
    }

}
