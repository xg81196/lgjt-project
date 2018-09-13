package lgjt.common.base.utils;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IStringCache;
import com.ttsx.util.cache.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuguangwei
 * @date 2018/6/19
 * @Description:用户名和密码的生成规则
 */
public class CodeGenerateUtils {


    private static AtomicInteger code = new AtomicInteger();

    private final static String CODE="username:code";

    public static  String generateCode( ) {

            try {

                /**
                 * TODO:先这么整
                 */
                IStringCache cache = CacheFactory.getStringCache();
                //cache.set(CODE, 100000+"");
                if (StringUtils.isBlank(cache.get(CODE))){
                    cache.set(CODE, 100000+"");
                }

                Integer num = Integer.parseInt(cache.get(CODE));
                if ( num == 0 ){
                    num = 100000;
                }

                num = num + 1;
                // 流水编号为四位，超过重新计算
                if (num > 999999) {
                    num = 0;
                     cache.set(CODE, num+"");
                }
                cache.set(CODE, num+"");
                return num+"";

            } catch (Exception e) {
                e.printStackTrace();
            }

        // 为了防止redis挂了程序能够继续运行
        return code.getAndIncrement() + "";

    }



}
