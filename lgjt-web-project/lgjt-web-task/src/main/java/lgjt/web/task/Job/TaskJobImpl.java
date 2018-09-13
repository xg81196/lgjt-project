package lgjt.web.task.Job;

import lombok.extern.log4j.Log4j;

import org.quartz.JobDataMap;
/**
 *  测试类
 * <p>Title: TaskJobImpl</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@Log4j
public class TaskJobImpl extends TaskJob{@Override
	public String work(JobDataMap jobDataMap) throws Exception {
	 JobDataMap data = jobDataMap;
	    String hi = data.getString("hi");
	    log.error("Test Job hi::"+hi);
     	return "完事";
	}
}
