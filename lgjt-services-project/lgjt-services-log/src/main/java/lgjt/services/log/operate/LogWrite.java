package lgjt.services.log.operate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.output.FileWriterWithEncoding;

import com.ttsx.platform.tool.util.UUIDUtil;

import lgjt.common.base.utils.SimpleDateFormatUtils;

/**
 * 日志写入文件类
 * @author daijiaqi
 *
 */
public class LogWrite {
	
	/**
	 * 文件分割阀值：超过该值日志换文件
	 */
	private static int splitThresholdRecords=1000;
	
	/**
	 * 文件跟路径
	 */
	private static String rootPath="";

	/**
	 * 日志文件名后缀
	 */
	private static String logFileSuffix = ".txt";
	
	/**
	 * 日志文件编码
	 */
	private static String logFileEncoding="utf-8";

	/**
	 * 获取日志文件目录
	 *  	.../optlog/login/2018/03/13/16/20/
	 *  	根目录+模块名+年+月+日+时+分
	 * @return 目录
	 */
	private static String getFilePath() throws Exception{
		return SimpleDateFormatUtils.getCurrentDate(SimpleDateFormatUtils.PATTERN_TYPE_2);
	}
	
	/**
	 * 获取完整文件名
	 * @return
	 */
	private static String getFileName(){
		return UUIDUtil.getUUID()+logFileSuffix;
	}
	/**
	 * 获取文件路径+名
	 * @return
	 * @throws Exception
	 */
	private static String getFilePathName()throws Exception{
		String result = rootPath +"/" + getFilePath()+"/"+getFileName();
		result = result.replaceAll("//+", "/");
		return result;
	}

	/**
	 * 日志写入文件
	 * @param logRootPath 日志存放根路径
	 * @param operateLogVector 日志集合
	 */
	public static  void writeLogs(String  logRootPath , Vector<OperateLog> operateLogVector){
		if (operateLogVector == null) {
			return;
		}
		BufferedWriter bw =null;
		try {
			bw = new BufferedWriter(new FileWriterWithEncoding(getFilePathName(), logFileEncoding));
			for (int i = 0; i < operateLogVector.size(); i++) {
				bw.write(JSONObject.toJSONString(operateLogVector.get(i)));
				bw.newLine();
				if(1/1000==0){
					bw.flush();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(bw != null){
				try {
					bw.flush();
					bw.close();
					bw=null;
				} catch (IOException e) {}
			}
		}
	}
}
