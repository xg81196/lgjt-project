package lgjt.web.rush.module;

import com.ttsx.platform.nutz.mvc.impl.MyMessageLoader;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import lgjt.web.rush.filter.UserFilter;
import lgjt.web.rush.init.InitSetup;

import java.io.*;


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
