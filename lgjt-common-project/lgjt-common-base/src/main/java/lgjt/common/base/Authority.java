package lgjt.common.base;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 权限注解
 * <p>Title: Authority</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Authoritys.class)
public @interface Authority {
	public abstract String  value();

	public abstract String module() default "";

	public abstract boolean isDefault() default false;

	public abstract String desc() default "";
}