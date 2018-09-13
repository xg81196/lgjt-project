package lgjt.common.base.annotation;

import java.lang.annotation.*;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 数据过滤
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {

    /**  true：拥有下属企业数据权限 */
    boolean subUnit() default false;
}
