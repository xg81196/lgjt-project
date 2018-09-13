package lgjt.common.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 日期格式化工具类
 * 
 * @Description: TODO(用一句话描述该类作用) 
 * @author daijiaqi
 * @CreateDate:   2016-12-16 下午6:37:48  
 * 
 * @UpdateUser:   daijiaqi 
 * @UpdateDate:   2016-12-16 下午6:37:48  
 * @UpdateRemark: 说明本次修改内容
 */
public class SimpleDateFormatUtils {
	
	public static final String PATTERN_TYPE_1="yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_TYPE_2="yyyy/MM/dd/HH/mm";
	public static final String PATTERN_TYPE_3="yyyyMMddHHmmss";
	public static final String PATTERN_TYPE_4="yyyy-MM-dd";
			
	/**
	 * 根据年月日编码获取对象
	 *  例如：yyyy-MM-dd
	 * @param pattern 根据日期格式获取SIMPLE对象
	 * @return  对象
	 * @author daijiaqi
	 * @date 2016-12-12 下午1:36:49
	 */
	private static SimpleDateFormat getSimpleDateFormat(String pattern)throws Exception{
		SimpleDateFormat simpleDateFormat = null;
		try{
			simpleDateFormat =  new SimpleDateFormat(pattern);
		}catch(Exception e){
			throw e;
		}
		return simpleDateFormat;
	}
	
	/**
	 * 根据字符串获取Date类型
	 * 	
	 * @param pattern 时间格式  例如：yyyyMMdd
	 * @param source 具体时间值 例如:20160909
	 * @return 返回日期格式
	 * @throws ParseException
	 * @throws Exception   
	 * @author daijiaqi
	 * @date 2016-12-12 下午1:46:21
	 */
	public static Date getDate(String source,String pattern) throws ParseException, Exception{
		return  getSimpleDateFormat(pattern).parse(source);
	}
	
	
	
	/**
	 * 获取当前时间字符串
	 * 	
	 * @param pattern 时间格式  例如：yyyyMMdd
	 * @return 返回日期格式字符串
	 * @throws ParseException
	 * @author daijiaqi
	 * @date 2016-12-12 下午1:46:21
	 */
	public static String getCurrentDate(String pattern) throws Exception{
		return  getSimpleDateFormat(pattern).format(new Date());
	}
	
	/**
	 * 获取指定日期的字符串
	 * 
	 * @param date	日期
	 * @param pattern	格式
	 * @return
	 * @throws ParseException   
	 * @author daijiaqi
	 * @date 2016-12-21 下午7:20:41
	 */
	public static String getStringDate(Date date,String pattern)throws  Exception{
		return  getSimpleDateFormat(pattern).format(date);
	}
	/**
	 * show 给指定字符串日期加固定时间后返回字符串，比如，date1为2018-06-22 10:43:00，固定时间为24小时，则返回时间为2018-06-23 10:43:00.
	 * @author wangyu
	 * @date 2018-06-22
	 * @param date1 初始时间（字符串类型）
	 * @param pattern（类型 yyyy-MM-dd HH:mm:ss）
	 * @param hour 固定时间（int型，以小时为单位）
	 * @return 返回字符串时间
	 */
	public static String getStringDate(String date1,String pattern,int hour) throws  Exception{
		Date date = getDate(date1,pattern);
		SimpleDateFormat time=getSimpleDateFormat(PATTERN_TYPE_1);
		String returnTime = time.format(date.getTime()+hour*3600*1000);
		return returnTime;
		
	}
}
