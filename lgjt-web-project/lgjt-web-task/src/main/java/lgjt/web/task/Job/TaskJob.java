package lgjt.web.task.Job;

import java.util.Date;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.task.constants.SysTaskTaskStatus;
import lgjt.domain.task.systask.SysTask;
import lgjt.services.task.systask.SysTaskService;

/**
 * Job 任务抽象类
 * Systask 中的任务执行类必须继承该类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-12-21 下午3:49:18  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-12-21 下午3:49:18  
 * @UpdateRemark: 说明本次修改内容
 */
@IocBean
@Log4j
public abstract class TaskJob implements Job {
	@Inject
	protected SysTaskService sysTaskService;
	public abstract String work(JobDataMap jobDataMap)throws Exception;
	
	/**
	 * 维护状态
	 * @param context   
	 * @author daijiaqi
	 * @date 2016-12-20 下午2:41:22
	 */
	/**
	 * 任务执行后执行
	 * 
	 * @param context 对象
	 * @param cost 耗时
	 * @param isSucc	是否成功 true 成功，false 失败
	 * @param runInfo   执行结果
	 * @author daijiaqi
	 * @date 2016-12-21 下午6:19:59
	 */
	private void workAfter(JobExecutionContext context,long cost,boolean isSucc,String runInfo){
		 String taskId = StringTool.trim(context.getJobDetail().getKey().getName());
		 SysTask sysTask = sysTaskService.get(taskId);
		 int result=0;
		 if(sysTask!=null){
			 int taskStatus =0;
			 if(context.getNextFireTime()==null){//不会再执行
				 taskStatus = SysTaskTaskStatus.FINISH;
			 }else{//等待执行
				 taskStatus = SysTaskTaskStatus.SLEEP;
			 }
			 Date date = new Date();
			 Chain chain=Chain.make("lastTime", date)
					 .add("last_info",StringTool.trim(runInfo))
					 .add("task_status", taskStatus)
					 .add("next_time",context.getNextFireTime())
			 		 .add("run_count",(sysTask.getRunCount()==null?1:(sysTask.getRunCount()+1)))
			 		 .add("lastDuration", cost);
			 if(isSucc){
				 chain.add("last_succ_time", date);
			 }
			 result=  sysTaskService.update(SysTask.class, chain, Cnd.where("id", "=", taskId));
		 }
		 if(result == 0){
			 log.info("Task is not exist  (taskId = "+taskId+")");
		 }
	}
	
	/**
	 * 维护状态
	 * 
	 * @param context   
	 * @author daijiaqi
	 * @date 2016-12-20 下午2:41:22
	 */
	private int running(JobExecutionContext context){
		 	String taskId = StringTool.trim(context.getJobDetail().getKey().getName());
		 	return sysTaskService.update(SysTask.class, Chain.make("task_status", SysTaskTaskStatus.RUNNING), Cnd.where("id", "=", taskId).and("task_status", "!=", SysTaskTaskStatus.END).and("task_isdel", "!=", SysTask.TASK_ISDEL_YES));
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		 int num =  running(context);
		 String taskInfo = "";
		 boolean isSucc = true;
		 if(num > 0){
			 //任务实体
			 long beginTime = System.currentTimeMillis();
			 try {
				 taskInfo = work(context.getJobDetail().getJobDataMap());
			} catch (Exception e) {
				isSucc = false;
			}
			 long endTime = System.currentTimeMillis();
			//任务执行完记录
			 workAfter(context,(endTime-beginTime),isSucc,taskInfo);
		 }else{
			 log.error("TaskJob running return  0 task can not run");
		 }
	
	}
}
