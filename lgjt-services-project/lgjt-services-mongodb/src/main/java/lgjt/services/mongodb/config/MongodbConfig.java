package lgjt.services.mongodb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import lgjt.services.mongodb.pools.MongodbConnectionPools;
import lgjt.services.mongodb.util.ObjectTools;
import lgjt.services.mongodb.util.StringTools;

/**
 * 解析 mongodb.properties 配置文件中
 * @author daijiaqi
 */
public class MongodbConfig {
	private static Properties properties = null;
	static {
		try {
			readMongodbConfig();
		} catch (IOException e) {
			System.out.println();
		}
	}
	
	/**
	 * 初始化配置文件
	 * @throws IOException 读取文件发生异常
	 */
	private static void readMongodbConfig() throws IOException{
		if(properties==null){
			properties=new Properties();
		}
		InputStream in = MongodbConnectionPools.class.getResourceAsStream("/mongodb.properties");
		properties.load(in);
	}
	
	/**
	 * 根据key获取value
	 * @param key 参数名
	 */
	public static String getValue(String key){
		return	ObjectTools.objectToStringTrim(properties.get(key));
	}
	
	/**
	 * 根据key获取value
	 * @param pKey 参数名
	 */
	public static List<String> getKeysByPrefix(String pKey){
		List<String> result=new ArrayList<String>();
		pKey = StringTools.trim(pKey);
		Enumeration<Object> keys =properties.keys();
		while(keys.hasMoreElements()){
			String key =StringTools.trim((String)keys.nextElement());
			if(key.length()>0 && key.startsWith(pKey)){
				result.add(key);
			}
		}
		return	result;
	}
	
}
