package lgjt.web.task.Job.impl;

import org.quartz.JobDataMap;

import lgjt.web.task.Job.TaskJob;
/**
 * 
 * <p>Title: 日志清理任务</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年4月23日
 */
public class TaskJobClearSysLogImpl extends TaskJob {

	@Override
	public String work(JobDataMap jobDataMap) throws Exception {
		 String lastInfo = "生成通讯秘钥:"; 
//		 JobDataMap data = jobDataMap;
//		 int saveDays = data.getInt("saveDays");
//		 SysLogService sysLogService = IocUtils.getBean(SysLogService.class);
//		 int result = sysLogService.deleteBeforeDaysLog(saveDays);
//		 lastInfo +=  result;
		 System.out.println(System.currentTimeMillis());
		 return lastInfo;
	}
}
