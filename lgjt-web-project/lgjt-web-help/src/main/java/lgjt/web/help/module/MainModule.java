package lgjt.web.help.module;

import com.ttsx.platform.nutz.mvc.impl.MyMessageLoader;
import lgjt.web.help.filter.UserFilter;
import lgjt.web.help.init.InitSetup;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;


/**
 * Nutz 加载类
 * @author wugaungwei
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
