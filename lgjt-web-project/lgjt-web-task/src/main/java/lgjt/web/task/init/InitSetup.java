package lgjt.web.task.init;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.quartz.QuartzManager;
import org.nutz.lang.Encoding;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import org.quartz.Scheduler;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.util.RedisUtil;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.utils.IocUtils;
import lgjt.domain.task.constants.SysTaskTaskStatus;
import lgjt.domain.task.systask.SysTask;
import lgjt.services.task.systask.SysTaskService;
import lgjt.web.task.Job.TaskQuartzManager;
import lgjt.web.task.utils.TaskQuartzUtils;

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
			initSysTask(nc);
			//初始化开启任务
			System.out.println(" TaskQuartzManager start ");
			TaskQuartzManager.getInstance().init();
			System.out.println(" TaskQuartzManager end ");
	}
	
	/**
	 * 定时任务初始化(只有待执行的才会重新启动)
	 *    
	 * @author daijiaqi
	 * @date 2016-12-20 下午3:09:41
	 */
	private void initSysTask(NutConfig nc){
//		 QuartzManager quartzManager = nc.getIoc().get(QuartzManager.class);
//	     quartzManager.clear();
	     SysTaskService sysTaskService = IocUtils.getBean(SysTaskService.class);
//	     Cnd.where().andInIntArray("task_status", SysTaskTaskStatus.WATING,SysTaskTaskStatus.RUNNING,SysTaskTaskStatus.SLEEP);
	    int result = sysTaskService.update(SysTask.class,  Chain.make("task_status",0), Cnd.where("task_status", "in",new int[]{SysTaskTaskStatus.WATING,SysTaskTaskStatus.RUNNING,SysTaskTaskStatus.SLEEP}));
System.out.println("task init "+result);
	    //	     List<SysTask> taskList = sysTaskService.query(SysTask.class, Cnd.where("task_status", "=", 0));
//	       for (SysTask sysTask : taskList) {
//	            try {
//	            	 quartzManager.add(TaskQuartzUtils.getQuartzJobBySysTask(sysTask));
//	            } catch (Exception e) {
//	                log.error("initSysTask:::"+e);
//	                e.printStackTrace();
//	            }
//	        }
	}

	@Override
	public void destroy(NutConfig nc) {
		  try {
	            nc.getIoc().get(Scheduler.class).shutdown(true);
	        } catch (Exception e) {
	        }
	}
}
