package lgjt.common.base;

import java.util.Date;

import org.apache.log4j.Logger;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocException;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.common.base.oplog.Oplog;

/**
 * 拦截Module方法。用于统一记录操作日志
 * @author daijiaqi
 * @date 2016-06-02
 *
 */
public class CommonInterceptor implements MethodInterceptor {

	
	private static Logger logger = Logger.getLogger(CommonInterceptor.class);  
	
	/**
	 * 拦截Module方法。用于统一操作日志处理
	 */
	public void filter(InterceptorChain chain) throws Throwable {
		Exception ex = null;
		Date start = new Date();
		String method = chain.getCallingMethod().getName();
		String className = chain.getCallingObj().getClass().getName();
		String  parameter="";
		Object[] obj = chain.getArgs();
//		if(obj!=null){
//			parameter=Json.toJson(obj);
//		}
		int inx = className.indexOf("$");
		if(-1 != inx) {
			className = className.substring(0, inx);
		}
		try {
			chain.doChain();
		}catch(Exception e) {
			ex = e;
			String message = "className="+className+",method="+method+",args="+parameter;
			logger.error(message, e);
			error(chain,e);
		}finally {
			log(chain.getReturn(),className,method,start,parameter.toString(),ex);
		}
	}

	/**
	 * 记录操作日志
	 * @param re          接口返回值
	 * @param className   接口全类名
	 * @param method      接口名称
	 * @param start       接口开始执行时间
	 */
	protected void log(Object re,String className,String method,Date start,String parameter,Exception ex) {
		Ioc ioc = Mvcs.ctx().getDefaultIoc();
		Oplog log = null;
		try {
			log = ioc.get(Oplog.class);
		} catch (IocException e1) {
		}
		if(null != log) {
			Results result = null;
			try {
				if(null != re && re instanceof Results) {
					result = (Results)re;
					String desp="";
					if(result!=null){
						desp = Json.toJson(result);
					}
					log.log(className, method, desp, result.getCode(), start, new Date(),parameter);
				}else if(ex != null) {
					log.log(className, method, "System Exception："+StringTool.stackTrace2String(ex), Constants.STATE_FAIL, start, new Date(),parameter);
				}
			}catch(Exception e) {
				logger.error("Save operate log error:", e);
			}
		}
	}

	protected void error(InterceptorChain chain,Exception e) throws Exception {
		throw e;
	}

}
