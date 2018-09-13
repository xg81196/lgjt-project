package lgjt.common.base.utils;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.impl.ScopeContext;
import org.nutz.ioc.loader.combo.ComboIocLoader;
import org.nutz.mvc.Mvcs;

import lombok.extern.log4j.Log4j;

/**
 * Nutz ioc 容器工具类
 * 
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-16 下午6:35:56
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-16 下午6:35:56
 * @UpdateRemark: 说明本次修改内容
 */
@Log4j
public class IocUtils {

	/**
	 * 获取IOC容器中缓存的对象
	 * 
	 * @param type
	 *            class对象 *.class
	 * @return 内存中的缓存类
	 * @author daijiaqi
	 * @date 2016-12-16 下午6:36:54
	 */
	public static <K> K getBean(Class<K> type) {
		Ioc ioc = Mvcs.getIoc();
		if (ioc == null) {
			ioc = Mvcs.ctx.getDefaultIoc();
		}
		if (ioc == null) {
			try {
				ioc = new NutIoc(new ComboIocLoader(
						"*org.nutz.ioc.loader.json.JsonLoader", "ioc/",
						"*org.nutz.ioc.loader.annotation.AnnotationIocLoader",
						"lgjt"), new ScopeContext("app"), "app");
			} catch (ClassNotFoundException e) {
				log.error("IocUtils.getbean(" + type
						+ ") classNotFoundException", e);
			}
		}
		if (ioc == null) {
			return null;
		}
		return ioc.get(type);
	}
}
