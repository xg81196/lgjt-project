package lgjt.web.app.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.domain.app.user.SysUserAuth;
/**
 * show 用户审核工具类.
 * <p>Title: SysUserAuthUtil</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月4日
 */
public class SysUserAuthUtil {

	/**
	 * show 从用户审核对象内获取查询参数.
	 * <p>Title: getParamSignFromSysUser</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月3日  
	 * @param obj
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static List<String> getParamSignFromSysUserAuth(SysUserAuth obj){
		List<String> result = new ArrayList<String>();
		if(obj==null) {
			return result;
		}
		//当前类
		List<Field> fields = new ArrayList<>() ;
		Class tempClass = obj.getClass();
		//当父类为null的时候说明到达了最上层的父类(Object类).
		while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
			fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
		}
			  for (int j = 0; j < fields.size(); j++) {
				  if(fields.get(j).getModifiers() !=2) {
					 continue;
				  }
				  fields.get(j).setAccessible(true);
		            if (fields.get(j).getType().getName().equalsIgnoreCase(
		                    java.lang.String.class.getName())) {  
		                try {  
		                	Object value =fields.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fields.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            } else if (fields.get(j).getType().getName().equalsIgnoreCase(
		                    java.lang.Integer.class.getName())||fields.get(j).getType().getName().equals("int")) {
		                try {  
		                	Object value=fields.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fields.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            }   else if (fields.get(j).getType().getName().equalsIgnoreCase(
		            		 java.lang.Long.class.getName())||fields.get(j).getType().getName().equals("long")) {
		                try {  
		                	Date value=(Date)fields.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fields.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            }    else if (fields.get(j).getType().getName().equalsIgnoreCase(
		                    java.util.Date.class.getName())) {  
		                try {  
		                	Date value=(Date)fields.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fields.get(j).getName()+"="+SimpleDateFormatUtils.getStringDate(value, SimpleDateFormatUtils.PATTERN_TYPE_1));
		                	}
		                } catch (Exception e) {} 
		            }     else if (fields.get(j).getType().getName().equalsIgnoreCase(
							java.sql.Timestamp.class.getName())) {
						try {
							Timestamp value=(Timestamp)fields.get(j).get(obj);
							if(value!=null) {
								result.add(fields.get(j).getName()+"="+SimpleDateFormatUtils.getStringDate(value, SimpleDateFormatUtils.PATTERN_TYPE_1));
							}
						} catch (Exception e) {}
					}
			  }
			  return result;
	} 
	
	/**
	 * show 获取对象中非空字段名和值英文 “,”分隔.
	 * <p>Title: getNotNullParamsFromSysUserAuthForException</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月4日  
	 * @param obj
	 * @return
	 */
	public String getNotNullParamsFromSysUserAuthForException(SysUserAuth obj) {
		StringBuffer sb =new StringBuffer(); 
		List<String> paramsList =getParamSignFromSysUserAuth(obj);
		for (String param : paramsList) {
			if(sb.length()>0) {
				sb.append(",");
			}
			sb.append(param);
		}
		return sb.toString();
	}
	
}
