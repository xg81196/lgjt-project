package lgjt.common.base;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ttsx.platform.nutz.common.Constants;
import lgjt.common.base.constants.ReturnCode;

/**
 * 后台JAVA返回给前台JS的数据对象
 * @author daijiaqi
 */
public class ResultsImpl  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat SDF_YYYYMMDDHHMMSS =new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 返回的数据
	 */
	private Object data;
	/**
	 * 返回结果。参见{@link com.ttsx.platform.nutz.common.Constants}
	 */
	private String code;
	/**
	 * 返回信息
	 */
	private String msg;
	/**
	 * 日志信息，用于操作日志记录时特殊信息的传递
	 */
	private Object log;
	/**
	 * 时间戳
	 */
	private String timestamp;

	/**
	 * 签名
	 */
	private String sign;


	/**
	 * 构造函数
	 */
	public ResultsImpl() {
	}
	/**
	 * 构造一个Results对象
	 * @param code  返回结果。参见{@link com.ttsx.platform.nutz.common.Constants}
	 * @return
	 */
	public static ResultsImpl parse(String code) {
		
		return parse(code, "");
	}
	/**
	 * 不含返回数据的结果
	 * @param code
	 * @param msg
	 * @return
	 */
	public static ResultsImpl parse(String code, String msg) {
		return parse(code, msg, null);
	}
	/**
	 * 不含返回数据但包含特殊日志信息的结果
	 * @param code
	 * @param msg
	 * @param log
	 * @return
	 */
	public static ResultsImpl parseWithLog(String code, String msg,Object log) {
		return parseWithLog(code, msg, null,null,null);
	}
	/**
	 * 含返回数据的结果
	 * @param code
	 * @param msg
	 * @param result
	 * @return
	 */
	public static ResultsImpl parse(String code, String msg, Object result) {
		return  parseWithLog(code, msg, result,null,null);
	}
	/**
	 * 含返回数据但包含特殊日志信息的结果
	 * @param code
	 * @param msg
	 * @param result
	 * @param log
	 * @return
	 */
	public static ResultsImpl parseWithLog(String code, String msg, Object result,Object log,String sign) {
		ResultsImpl r = new ResultsImpl();
		r.setCode(code);
		r.setMsg(msg);
		r.setData(result);
		r.setLog(log);
		r.setTimestamp(SDF_YYYYMMDDHHMMSS.format(new Date()));
		r.setSign(sign);
		return r;
	}

	@SuppressWarnings("rawtypes")
	public static ResultsImpl parse(ResultsImpl result) {
		ResultsImpl r = new ResultsImpl();
		r.setCode(Constants.STATE_SUCCESS);
		r.setData(result);
		return r;
	}


	public static boolean isSuccess(String code) {
		return ReturnCode.CODE_100000.toString().equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getLog() {
		return log;
	}

	public void setLog(Object log) {
		this.log = log;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
