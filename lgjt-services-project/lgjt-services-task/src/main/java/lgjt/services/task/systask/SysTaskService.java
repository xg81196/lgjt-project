package lgjt.services.task.systask;

import java.util.List;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.tool.util.StringTool;

import lgjt.common.base.CustomService;
import lgjt.domain.task.systask.SysTask;

/**
 * 任务业务逻辑类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-7-1 下午12:14:01  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-7-1 下午12:14:01  
 * @UpdateRemark: 说明本次修改内容
 */
@IocBean
public class SysTaskService extends CustomService {

	/**
	 * 查询任务（分页）
	 * 
	 * @param obj 任务实体类
	 * @return   
	 * @author daijiaqi
	 * @date 2016-12-21 上午11:32:45
	 */
	public PageResult<SysTask> queryPage(SysTask obj) {
		SimpleCriteria cri = getCondition(obj,true);
		return super.queryPage(SysTask.class, obj, cri);
	}

	/**
	 * 根据对象获取查询条件
	 * @param obj
	 * @return   
	 * @author daijiaqi
	 * @date 2016-7-1 下午12:11:16 
	 */
	public SimpleCriteria getCondition(SysTask obj,boolean isSortDef) {
		SimpleCriteria cri = super.getCommonCondition(obj);
		if(StringTool.isNotNull(obj.getTaskName())) {
			obj.setTaskName(obj.getTaskName().replace("%","\\".substring(0,1)+"%"));
			cri.where().andLike("task_name", obj.getTaskName());
		}
		if(StringTool.isNotNull(obj.getRunClass())) {
			obj.setRunClass(obj.getRunClass().replace("%","\\".substring(0,1)+"%"));
			cri.where().andLike("run_class", obj.getRunClass());
		}
		if(StringTool.isNotNull(obj.getTaskParams())) {
			cri.where().andEquals("task_params", obj.getTaskParams());
		}
		if(StringTool.isNotNull(obj.getBeginTime())) {
			cri.where().andEquals("begin_time", obj.getBeginTime());
		}
		if(StringTool.isNotNull(obj.getEndTime())) {
			cri.where().andEquals("end_time", obj.getEndTime());
		}
		if(StringTool.isNotNull(obj.getRunPolicy())) {
			cri.where().andEquals("run_policy", obj.getRunPolicy());
		}
		if(StringTool.isNotNull(obj.getTaskStatus())) {
			cri.where().andEquals("task_status", obj.getTaskStatus());
		}
		if(StringTool.isNotNull(obj.getNextTime())) {
			cri.where().andEquals("next_time", obj.getNextTime());
		}
		if(StringTool.isNotNull(obj.getLastTime())) {
			cri.where().andEquals("last_time", obj.getLastTime());
		}
		if(StringTool.isNotNull(obj.getLastDuration())) {
			cri.where().andEquals("last_duration", obj.getLastDuration());
		}
		if(StringTool.isNotNull(obj.getLastSuccTime())) {
			cri.where().andEquals("last_succ_time", obj.getLastSuccTime());
		}
		if(StringTool.isNotNull(obj.getRunCount())) {
			cri.where().andEquals("run_count", obj.getRunCount());
		}
		if(StringTool.isNotNull(obj.getLastInfo())) {
			cri.where().andEquals("last_info", obj.getLastInfo());
		}
		if(StringTool.isNotNull(obj.getTaskIsdel())) {
			cri.where().andEquals("task_isdel", obj.getTaskIsdel());
		}
		
		if(isSortDef){
			cri.desc("last_time");
		}
		
		return cri;
	}
	
	/**
	 * 查询全部任务
	 * 
	 * @return   任务集合
	 * @author daijiaqi
	 * @date 2016-12-21 上午11:35:34
	 */
	public List<SysTask> queryAll() {
		SysTask obj=new SysTask();
		SimpleCriteria cri = getCondition(obj,true);
		return super.query(SysTask.class, cri);
	}
	
	public List<SysTask> query(SysTask obj) {
		SimpleCriteria cri = getCondition(obj,true);
		return super.query(SysTask.class, cri);
	}

   	public SysTask get(String id) {
		return super.fetch(SysTask.class, id);
	}

   	/**
   	 * 假删除
   	 * 
   	 * @param ids
   	 * @return   
   	 * @author daijiaqi
   	 * @date 2016-12-21 下午2:52:56
   	 */
	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.update(SysTask.class, Chain.make("task_isdel", SysTask.TASK_ISDEL_YES), cri);
		}
		return 0;
	}
	/**
	 * 更新 状态
	 * 将当前记录由 原来的改为当前的
	 * 如果发生更改则返回受影响的条数
	 * @@param id 主键
	 * @return   
	 * @author daijiaqi
	 * @date 2016-7-4 下午2:36:14
	 */
	public int updateStatus(String id ,int oldStatus,int  newStatus){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",id);
		cri.where().andEquals("task_status", oldStatus);
		return super.update(SysTask.class,  Chain.make("task_status",newStatus), cri);
	}
	
	
	
	/**
	 * 更新 状态
	 * 将当前记录由 原来的改为当前的
	 * 如果发生更改则返回受影响的条数
	 * @param id 主键
	 * @param newStatus 新的状态
	 * @return   
	 * @author daijiaqi
	 * @date 2016-7-4 下午2:36:14
	 */
	public int updateStatus(String id ,int  newStatus){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",id);
		return super.update(SysTask.class,  Chain.make("task_status",newStatus), cri);
	}
	
	/**
	 * 查询指定状态的任务
	 * 
	 * @param taskStatus
	 * @return   
	 * @author daijiaqi
	 * @date 2016-12-21 上午11:25:55
	 */
	public List<SysTask> queryByTaskStatus(int[] taskStatus){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andInIntArray("task_status",taskStatus);
		return super.query(SysTask.class, cri);
	}
	
	/**
	 * 查询任务集合
	 * 
	 * @param ids 主键数组 ,号分隔
	 * @param status 状态
	 * @return   
	 * @author daijiaqi
	 * @date 2016-12-21 上午11:29:31
	 */
	public List<SysTask> queryWithIdsAndStatus(String[] ids,int status){
		SimpleCriteria cri = Cnd.cri();
		if(ids!=null && ids.length>0){
			cri.where().andIn("id", ids);
		}
		cri.where().andIn("task_status", status);
		return super.query( SysTask.class, cri);
	}
	
}