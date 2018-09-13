package lgjt.services.resource.util;
/**
 * 
 * <p>Title: StringTools</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class StringTools{
	
	
	/**
	 * 字符串去空格
	 * @param parameter 参数
	 * @return 去空格后的内容
	 */
	public static String trim(String parameter){
		return (parameter==null?"":parameter.trim());
	}
}
