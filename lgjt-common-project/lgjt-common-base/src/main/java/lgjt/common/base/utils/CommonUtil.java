package lgjt.common.base.utils;

import com.ttsx.platform.tool.util.PropertyUtil;

public class CommonUtil {
	
	/**
	 * 模块未授权
	 */
	public static final String STATE_UNCOMMITTED="4";

	/**
	 * 状态为启用
	 */
	public static final int STATE_ON = 0;
	/**
	 * 状态为禁用
	 */
	public static final int STATE_OFF = 1;
	
	/**
	 *  按sort字段排序
	 */
	public static final String ORDER_BY_SORT = "sort";
	
	
	/**
	 *  按crt_time字段排序
	 */
	public static final String ORDER_BY_CRTTIME = "crt_time";
	
	/**
	 * 排序倒序
	 */
	public static final String ORDER_DESC = "desc";
	
	/**
	 * 排序正序
	 */
	public static final String ORDER_ASC =  "asc";
	
	
	public static final String ROOT_PATH = PropertyUtil.getProperty("root-path");
	
	public static final String TEMP_PATH = PropertyUtil.getProperty("temp-path");
	
	/**
	 * 默认的pageSize
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 内容长度
	 */
	public static final int CONTENT_LENGTH =  500;
	
	/**
	 * 闯关验证码
	 */
	public static final String RUSHGATE_CHECKCODE = "CHECKCODE";
	
	/**
	 * 验证码
	 */
	public static final String CHECKCODE = "code";
}
