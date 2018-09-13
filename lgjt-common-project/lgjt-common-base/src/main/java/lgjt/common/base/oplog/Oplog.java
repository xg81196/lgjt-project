package lgjt.common.base.oplog;

import java.util.Date;
/**
 * 系统操作日志统一处理接口。平台提供统一处理机制
 * @author daijiaqi
 * @date 2016-06-01
 *
 */
public interface Oplog {

	/**
	 * 记录操作日志
	 * @param className   类名
	 * @param method  方法名
	 * @param logInfo  需要记录到日志的信息。默认为Results.getMsg();
	 * @param result   执行结果。参见{@link com.ttsx.platform.nutz.common.Constants}
	 * @param startTime  方法执行开始时间
	 * @param endTime   方法执行结束时间
	 */
	public void log(String className,String method,Object logInfo,String result,Date startTime,Date endTime,String parementer);
}
