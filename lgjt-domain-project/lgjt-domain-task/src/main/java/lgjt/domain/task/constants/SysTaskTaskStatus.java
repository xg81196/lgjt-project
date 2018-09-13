package lgjt.domain.task.constants;

import java.util.ArrayList;
import java.util.List;

import com.ttsx.platform.nutz.mvc.BaseModule;

/**
 * 任务表，任务状态字段，taskstatus 的静态变量
 * 
 * @Description: 任务当前状态
 * @author daijiaqi
 * @CreateDate: 2016-6-29 下午4:34:19
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-6-29 下午4:34:19
 * @UpdateRemark:
 */
public class SysTaskTaskStatus {

	/**
	 * 待执行
	 */
	public static final int WATING = 0;
	/**
	 * 执行中
	 */
	public static final int RUNNING = 1;
	/**
	 * 休眠中
	 */
	public static final int SLEEP = 2;
	/**
	 * 停止
	 */
	public static final int STOP = 3;
	/**
	 * 完成
	 */
	public static final int FINISH = 4;
	/**
	 * 人工终止
	 */
	public static final int END = 5;

	private static List<Integer> LIST = new ArrayList<Integer>();

	static {
		LIST.add(WATING);
		LIST.add(RUNNING);
		LIST.add(SLEEP);
		LIST.add(STOP);
		LIST.add(FINISH);
		LIST.add(END);
	}

	/**
	 * 键值是否合法
	 * 
	 * @param key
	 *            变量值
	 * @return true 代表合法，反之不合法
	 */
	public static boolean valid(int key) {
		return LIST.contains(key);
	}

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public static List<IntKeyInfo> getList() {
		List<IntKeyInfo> result = new ArrayList<IntKeyInfo>();
		result.add(new IntKeyInfo(LIST.get(0),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.wating")));
		result.add(new IntKeyInfo(LIST.get(1),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.running")));
		result.add(new IntKeyInfo(LIST.get(2),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.sleep")));
		result.add(new IntKeyInfo(LIST.get(3),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.stop")));
		result.add(new IntKeyInfo(LIST.get(4),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.finish")));
		result.add(new IntKeyInfo(LIST.get(5),
				com.ttsx.platform.tool.util.MessageUtil
						.getText("sys.systask.taskstatus.end")));
		return result;
	}

	/**
	 * 获取变量集合
	 * 
	 * @param locale
	 *            国际化
	 * @return 国际化后的信息
	 */
	public static List<IntKeyInfo> getList(String locale) {
		List<IntKeyInfo> result = new ArrayList<IntKeyInfo>();
		result.add(new IntKeyInfo(LIST.get(0),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.wating", locale)));
		result.add(new IntKeyInfo(LIST.get(1),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.running", locale)));
		result.add(new IntKeyInfo(LIST.get(2),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.sleep", locale)));
		result.add(new IntKeyInfo(LIST.get(3),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.stop", locale)));
		result.add(new IntKeyInfo(LIST.get(4),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.finish", locale)));
		result.add(new IntKeyInfo(LIST.get(5),
				com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
						"sys.systask.taskstatus.end", locale)));
		return result;
	}

	/**
	 * 获取列表
	 * 
	 * @param module
	 *            module 层
	 * @return 国际化信息
	 */
	public static List<IntKeyInfo> getList(BaseModule module) {
		List<IntKeyInfo> result = new ArrayList<IntKeyInfo>();
		result.add(new IntKeyInfo(LIST.get(0), module
				.getText("sys.systask.taskstatus.wating")));
		result.add(new IntKeyInfo(LIST.get(1), module
				.getText("sys.systask.taskstatus.running")));
		result.add(new IntKeyInfo(LIST.get(2), module
				.getText("sys.systask.taskstatus.sleep")));
		result.add(new IntKeyInfo(LIST.get(3), module
				.getText("sys.systask.taskstatus.stop")));
		result.add(new IntKeyInfo(LIST.get(4), module
				.getText("sys.systask.taskstatus.finish")));
		result.add(new IntKeyInfo(LIST.get(5), module
				.getText("sys.systask.taskstatus.end")));
		return result;
	}

	/**
	 * 根据代码获取名称
	 * 
	 * @param code
	 *            值代码
	 * @param defVal
	 *            如果没有该代码，返回的值
	 * @return
	 */
	public static String getName(int code, String defVal) {
		if (!valid(code)) {
			return defVal;
		}
		if (code == WATING) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.wating");
		} else if (code == RUNNING) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.running");
		} else if (code == SLEEP) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.sleep");
		} else if (code == STOP) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.stop");
		} else if (code == FINISH) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.finish");
		} else if (code == END) {
			return com.ttsx.platform.tool.util.MessageUtil
					.getText("sys.systask.taskstatus.end");
		}
		return defVal;
	}

	/**
	 * 根据代码获取名称
	 * 
	 * @param code
	 *            值代码
	 * @param defVal
	 *            如果没有该代码，返回的值
	 * @return
	 */
	public static String getNameByLocal(int code, String defVal, String locale) {
		if (!valid(code)) {
			return defVal;
		}
		if (code == WATING) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.wating", locale);
		} else if (code == RUNNING) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.running", locale);
		} else if (code == SLEEP) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.sleep", locale);
		} else if (code == STOP) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.stop", locale);
		} else if (code == FINISH) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.finish", locale);
		} else if (code == END) {
			return com.ttsx.platform.tool.util.MessageUtil.getTextByLocale(
					"sys.systask.taskstatus.end", locale);
		}
		return defVal;
	}
}
