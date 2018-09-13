package lgjt.domain.task.systask;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import com.ttsx.platform.nutz.pojo.CaseEntity;

import lombok.Data;
import lgjt.domain.task.constants.SysTaskTaskStatus;

/**
 * 任务实体类
 * 
 * @Description: 存放任务配置
 * @author daijiaqi
 * @CreateDate: 2016-7-1 上午11:58:02
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-7-1 上午11:58:02
 * @UpdateRemark: task_state 改成task_status做到统一
 */
@Data
@Table("sys_task")
public class SysTask extends CaseEntity {
	/**
	 * 未删除
	 */
	public static final int TASK_ISDEL_NO=0;
	/**
	 * 已经删除
	 */
	public static final int TASK_ISDEL_YES=1;
	/**
	 * 任务名称。
	 */
	@Column("task_name")
	private String taskName;
	/**
	 * 执行类。类路径
	 */
	@Column("run_class")
	private String runClass;
	/**
	 * 执行参数。JSON串
	 */
	@Column("task_params")
	private String taskParams;
	/**
	 * 开始执行时间。早于该时间就不会执行，为null表示不限制
	 */
	@Column("begin_time")
	private java.util.Date beginTime;
	/**
	 * 最晚执行时间。晚于该时间就不会执行，为null表示不限制
	 */
	@Column("end_time")
	private java.util.Date endTime;
	/**
	 * 执行策略。JSON串： 0:执行一次，是否必须成功 1:定期执行（休息时间相同），可指定执行次数 2:定期执行（严格执行时间），可指定执行次数
	 * 3:定时执行（每天、每周、每月、每年）多个时间点 MMDDWHHMM
	 */
	@Column("run_policy")
	private String runPolicy;
	/**
	 * 状态。0：待执行（任务线程未启动） 1：执行中 2：休眠中 3：停止（还能再启动） 4：完成（不能再启动） 5：人工终止（不能再启动）
	 * 取值范围：#
	 * [wating:0:待执行$running:1:执行中$sleep:2:休眠中$stop:3:停止$finish:4:完成$end:5:
	 * 人工终止]#
	 */
	@Column("task_status")
	private Integer taskStatus;
	/**
	 * 下次执行时间。
	 */
	@Column("next_time")
	private java.util.Date nextTime;
	/**
	 * 最后一次执行时间。
	 */
	@Column("last_time")
	private java.util.Date lastTime;
	/**
	 * 最后一次执行时长。
	 */
	@Column("last_duration")
	private Long lastDuration;
	/**
	 * 最后一次成功执行时间。
	 */
	@Column("last_succ_time")
	private java.util.Date lastSuccTime;
	/**
	 * 执行次数。
	 */
	@Column("run_count")
	private Integer runCount;
	/**
	 * 最后一次执行信息。
	 */
	@Column("last_info")
	private String lastInfo;
	/**
	 * 是否删除 
	 * 0代表未删除
	 * 1代表已经删除
	 */
	@Column("task_isdel")
	private Integer taskIsdel;
	/**
	 * 判断任务是否终止
	 * 
	 * @return   true 已经终止，false 还未终止
	 * @author daijiaqi
	 * @date 2016-12-20 下午12:14:44
	 */
	public boolean isStop(){
		if(this.taskStatus == SysTaskTaskStatus.FINISH || this.taskStatus == SysTaskTaskStatus.END){
			return true;
		}
		return false;
	}
	/**
	 * 判断是否已经删除
	 * 
	 * @return   true 删除了，false 没有删除
	 * @author daijiaqi
	 * @date 2016-12-21 下午2:22:28
	 */
	public boolean isDel(){
		if(this.taskIsdel == TASK_ISDEL_YES){
			return true;
		}else{
			return false;
		}
	}
	

}