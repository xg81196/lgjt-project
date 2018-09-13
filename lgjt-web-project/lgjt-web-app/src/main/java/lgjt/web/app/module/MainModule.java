package lgjt.web.app.module;

import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.ttsx.platform.nutz.mvc.impl.MyMessageLoader;

import lgjt.web.app.filter.UserFilter;
import lgjt.web.app.init.InitSetup;

/**
 * Nutz 加载类
 * @author daijiaqi
 */
@Localization(type = MyMessageLoader.class, value = "message", defaultLocalizationKey = "zh_CN")
@IocBy(type=ComboIocProvider.class,args={"*org.nutz.ioc.loader.json.JsonLoader","ioc/",
	  "*org.nutz.ioc.loader.annotation.AnnotationIocLoader","lgjt"})
@Encoding(input = "UTF-8", output = "UTF-8")
@Modules(scanPackage=true)
@Filters(@By(type = UserFilter.class))
@SetupBy(InitSetup.class)
@Ok("json")
@Fail("http:500")
public class MainModule {
	
}
