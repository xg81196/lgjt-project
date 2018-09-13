package lgjt.common.base.utils;

import java.util.Calendar;
import java.util.Date;
/**
 * 时间工具类
 * <p>Title: TimeUtils</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class TimeUtils {
	
	/**
	 * 获取指定时间 N天前的日期
	 * 
	 * @param nowDate
	 * @param day
	 * @return   
	 * @author daijiaqi
	 * @date 2016-12-21 下午6:02:44
	 */
    public static Date getDateBefore(Date nowDate, int day) {  
        Calendar now = Calendar.getInstance();  
        now.setTime(nowDate);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    }   
}
