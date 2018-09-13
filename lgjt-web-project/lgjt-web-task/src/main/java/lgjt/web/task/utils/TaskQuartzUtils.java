package lgjt.web.task.utils;

import org.nutz.integration.quartz.QuartzJob;

import com.ttsx.platform.tool.util.StringTool;

import lgjt.domain.task.systask.SysTask;

/**
 * 任务表工具类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-12-21 上午9:22:55  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-12-21 上午9:22:55  
 * @UpdateRemark: 说明本次修改内容
 */
public class TaskQuartzUtils {
	/**
	 * 根据任务表获取定时任务
	 * 
	 * @param sysTask
	 * @return   
	 * @author daijiaqi
	 * @date 2016-12-21 上午9:24:22
	 */
	public static QuartzJob getQuartzJobBySysTask(SysTask sysTask){
		  QuartzJob qj = new QuartzJob();
          qj.setJobName(sysTask.getId());
          qj.setJobGroup(sysTask.getId());
          qj.setClassName(sysTask.getRunClass());
          qj.setCron(sysTask.getRunPolicy());
          qj.setComment(sysTask.getTaskName());
          qj.setDataMap(sysTask.getTaskParams());
          return qj;
	}
	
	/**
	 * 判断任务是否改变了
	 * 
	 * @param sysTask
	 * @return   true  改变 false 没改变
	 * @author daijiaqi
	 * @date 2016-12-21 下午12:40:14
	 */
	public static boolean isChange(QuartzJob qj ,SysTask sysTask){
		boolean result = false;
		if(!StringTool.trim(qj.getClassName()).equals(StringTool.trim(sysTask.getRunClass()))){
			result=true;
		}else 
		if(!StringTool.trim(qj.getCron()).equals(StringTool.trim(sysTask.getRunPolicy()))){
			result=true;
		}else 
		if(!StringTool.trim(qj.getComment()).equals(StringTool.trim(sysTask.getTaskName()))){
			result=true;
		}else if(!StringTool.trim(qj.getDataMap()).equals(StringTool.trim(sysTask.getTaskParams()))){
			result=true;
		}
        return result;
	}
	
	
	
	
	
}
