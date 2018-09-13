package lgjt.web.task.Job;

import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.integration.quartz.QuartzJob;
import org.nutz.integration.quartz.QuartzManager;
import org.quartz.JobKey;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.utils.IocUtils;
import lgjt.domain.task.constants.SysTaskTaskStatus;
import lgjt.domain.task.systask.SysTask;
import lgjt.services.task.systask.SysTaskService;
import lgjt.web.task.utils.TaskQuartzUtils;

/**
 * 任务管理类
 * 
 * @Description: TODO(用一句话描述该类作用)
 * @author daijiaqi
 * @CreateDate: 2016-12-21 上午10:14:19
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-21 上午10:14:19
 * @UpdateRemark: 说明本次修改内容
 */
@Log4j
public class TaskQuartzManager {
	/**
	 * 扫描线程时间间隔 （秒）
	 */
	private  long interval = 5;
	private static TaskQuartzManager taskQuartzManager; 
	private TaskQuartzManager() {}
	public static TaskQuartzManager  getInstance(){
		if(taskQuartzManager==null){
			taskQuartzManager = new TaskQuartzManager();
		}
		return taskQuartzManager;
	}
	/**
	 * 具体工作内容
	 * 
	 * @author daijiaqi
	 * @date 2016-12-21 上午10:34:33
	 */
	private void work() {
		// 1 校验当前服务器中的任务 与 systask 表中的任务是否有区别
		// 修改和删除的要同步到服务器中
		// 判断是否有新增的任务
		try{
		SysTaskService sysTaskService = IocUtils.getBean(SysTaskService.class);
		List<SysTask> sysTasks = sysTaskService.queryAll();
		if (sysTasks != null) {
			QuartzManager quartzManager = IocUtils.getBean(QuartzManager.class);
			for (int i = 0; i < sysTasks.size(); i++) {
				SysTask sysTask = sysTasks.get(i);
				boolean isExist = quartzManager.exist(new JobKey(sysTask
						.getId(), sysTask.getId()));
				if (isExist) {// 存在
					QuartzJob qj = quartzManager.fetch(sysTask.getId(),
							sysTask.getId());
					if (sysTask.isDel()) {
						quartzManager.delete(qj);
					} else {
						boolean isChange = TaskQuartzUtils.isChange(qj, sysTask);
						if (isChange) {
							quartzManager.add(TaskQuartzUtils
									.getQuartzJobBySysTask(sysTask));
						}
					}
				} else {
					// 不存在 时 如果任务没有被删除，并且不是待执行
					if (!sysTask.isDel()
							&& (sysTask.getTaskStatus() == SysTaskTaskStatus.WATING)) {
						int lock = sysTaskService.updateStatus(sysTask.getId(),
								SysTaskTaskStatus.WATING,
								SysTaskTaskStatus.RUNNING);
						if (lock > 0) {
							try{
							quartzManager.add(TaskQuartzUtils
									.getQuartzJobBySysTask(sysTask));
							}catch(Exception e){
								 Chain chain=Chain.make("last_info", "添加任务异常")
										 .add("task_status", SysTaskTaskStatus.END)
										 .add("next_time",null);
								 sysTaskService.update(SysTask.class, chain, Cnd.where("id", "=", sysTask.getId()));
								 quartzManager.delete(new JobKey(sysTask.getId(), sysTask.getId()));
							}
						}
					}
				}
			}
		}
		}catch(Exception e){
			log.error("taskQuartzManager work is error", e);
		}
	}

	/**
	 * 初始化数据。
	 */
	public  void init() {
		boolean run = false;
		work();
		if (!run) {
			new Thread(new Runnable() {
				public void run() {
					Thread.currentThread().setName("TaskQuartzManager");
					while (true) {
						try {
							Thread.sleep(interval * 1000);
						} catch (Exception e) {
						}
						work();
					}
				}
			}).start();
		}
	}
}
