package lgjt.web.api.init;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

import org.nutz.lang.Encoding;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.util.RedisUtil;

import lombok.extern.log4j.Log4j;

/**
 * 项目启动类
 * @author daijiaqi
 */
@Log4j
public class InitSetup implements Setup {

	private AtomicBoolean started = new AtomicBoolean(false);

	public void init(NutConfig nc) {
		
		    if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
                log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
            }
		    String url =  StringUtil.trim(PropertyUtil.getProperty("redis-url"));
			String passwd = StringUtil.trim(PropertyUtil.getProperty("redis-pwd"));
			try {
				RedisUtil.getInstance().init(url, passwd,RedisUtil.TYPE_SINGLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void destroy(NutConfig nc) {
	}
}
