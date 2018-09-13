package lgjt.web.app.init;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

import lgjt.web.app.utils.SensitiveWordServiceInit;
import org.nutz.dao.SqlManager;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.lang.Encoding;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.util.RedisUtil;

import lombok.extern.log4j.Log4j;
import lgjt.common.sms.Sms;

/**
 * 项目启动类
 *
 * @author daijiaqi
 */
@Log4j
public class InitSetup implements Setup {

    private AtomicBoolean started = new AtomicBoolean(false);

    public void init(NutConfig nc) {
        if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
            log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
        }
        //redis 初始化
        String url = StringUtil.trim(PropertyUtil.getProperty("redis-url"));
        String passwd = StringUtil.trim(PropertyUtil.getProperty("redis-pwd"));
        try {
            RedisUtil.getInstance().init(url, passwd, RedisUtil.TYPE_SINGLE);
        } catch (Exception e) {
            log.error("redis init (" + url + "," + passwd + ")", e);
            e.printStackTrace();
        }

        //初始化短信服务
        String account = PropertyUtil.getProperty("sms_account");
        String server = PropertyUtil.getProperty("sms_server");
        String token = PropertyUtil.getProperty("sms_token");
        String appid = PropertyUtil.getProperty("sms_appid");
        String smsPort = PropertyUtil.getProperty("sms_port");
        try {
            Sms.init(account, server, token, appid, smsPort);
        } catch (Exception e) {
            log.error("Sms.init(account=" + account + ";server=" + server + ";token=" + token + ";appid=" + appid + ";smsPort=" + smsPort + ")");
            e.printStackTrace();
        }

        //敏感词初始化相关
        SensitiveWordServiceInit.getInstance().init();

    }

    @Override
    public void destroy(NutConfig nc) {
    }
}
