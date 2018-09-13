package lgjt.services.log.operate;

import java.util.Date;
import java.util.Vector;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;

import lgjt.common.base.oplog.Oplog;
import lgjt.common.base.utils.ClientInfo;
import lombok.extern.log4j.Log4j;
import lgjt.common.base.utils.LoginUtil;

/**
 * 日志收集類
 */
@Log4j
public  class OperateLogUtil implements Oplog {
	
	/**
	 * 当前日志存储集合
	 */
	protected static Vector<OperateLog> currentVector = new Vector<OperateLog>();
	/**
	 * 日志分组存储集合
	 */
	private static  Vector<Vector<OperateLog>> vectorGroup = new Vector<Vector<OperateLog>>();

	/**
	 *  日志分组阈值 默认10000
	 */
	private static  final int VECTOR_SIZE = 5;

	/**
	 * 入库时间 5分钟入库 单位毫秒
	 */
	private static  final int TABLE_TIME = 5*60*1000;

	/**
	 * 线程循环时间间隔 单位毫秒 默认5秒
	 */
	private  static final int THREAD_TIME = 5 * 1000;

	/**
	 * 上一次的执行时间
	 */
	private  static long LAST_RUNTIME = System.currentTimeMillis();

	/**
	 * 日志文件存放路徑
	 */
	private  static final String LOG_ROOT_PATH = PropertyUtil.getProperty("operate_log_root_path");


	@Override
	public void log(String moduleid, String operateid, Object description, String result, Date startTime, Date endTime,String parementer) {
			OperateLog log = new OperateLog();
//			System.out.println("日志被添加");
			log.setStartTime(startTime);
			log.setEndTime(endTime);
			int cost = (int) (endTime.getTime() - startTime.getTime());
			log.setCostTime(cost);
			moduleid = StringTool.trim(moduleid);
			log.setLogModule(moduleid);
			log.setOptType(operateid);
			String returnValue = StringTool.trim(description);
			log.setResultInfo(returnValue);

			result =  StringTool.trim(result);
			if(result.equals(Constants.STATE_SUCCESS)){
				log.setResultInfo("成功");
			}else if(result.equals(Constants.STATE_FAIL)){
				log.setResultInfo("失败");
			}else if(result.equals(Constants.STATE_UNAUTH)){
				log.setResultInfo("没有权限");
			}else if(result.equals(Constants.STATE_UNLOGIN)){
				log.setResultInfo("未登录");
			}
			log.setParameters(parementer);
			log.setUserName(LoginUtil.getUserNameTrim());
			log.setLogIp(ClientInfo.getIp());
			currentVector.add(log);
	}

	/**
	 * 日志存放分组集合
	 * @throws Exception
	 */
	private static void moveCurrentVectorToVectorGroup() throws Exception {
		if (currentVector.size() >= VECTOR_SIZE) {
			refresh();
		} else {
			long currentTime = System.currentTimeMillis();
			if (currentTime - LAST_RUNTIME >= TABLE_TIME  && currentVector.size()>0) {
				refresh();
			}
		}
	}

	/**
	 * 日志从当前集合刷新到分组集合
	 * @author daijiaqi
	 * @date 2016-7-2 下午5:48:31 
	 */
	private static void refresh() {
		//迁移
		Vector<OperateLog> currentVector1=currentVector;
		//清空当前接收日志的集合
		currentVector=new Vector<OperateLog>();
		Vector<OperateLog> list = new Vector<OperateLog>();
		for (int i = 0; i < currentVector1.size(); i++) {
			if ((i / 1000 == 0) && i != 0) {
				vectorGroup.add(list);
				list = new Vector<OperateLog>();
			}
			list.add(currentVector1.get(i));
		}
		if (list.size() > 0) {
			vectorGroup.add(list);
		}
	}
	/**
	 * 判断是否有待入库日志
	 * @return true 有，false 没有
	 */
	private static boolean inInsertCheck() {
		if (vectorGroup.size() > 0) {
			return true;
		}
		return false;
	}

	static {
		new Thread() {
			@Override
			public void run() {
				 System.out.println("入库启动了");
				while (true) {
					try {
						// 转义log
						boolean isInser = inInsertCheck();
						if (isInser) {
							LogWrite.writeLogs(LOG_ROOT_PATH,vectorGroup.get(0));
						}else{
							moveCurrentVectorToVectorGroup();
						}
					} catch (Exception e) {
						log.error("OperateLogUtil.static.thread. is error!", e);
					} finally {
						try {
							sleep(THREAD_TIME);
						} catch (Exception e) {
							log.error("OperateLogUtil.static.thread.sleep is error!",
									e);
						}
					}
				}
			}
		}.start();
	}
}

