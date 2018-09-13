package lgjt.common.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * 权限注解
 * <p>Title: Authoritys</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Retention(RetentionPolicy.RUNTIME)
	public @interface Authoritys {
	Authority[] value();
}