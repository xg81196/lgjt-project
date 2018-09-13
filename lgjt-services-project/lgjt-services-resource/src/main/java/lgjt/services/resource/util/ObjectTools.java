package lgjt.services.resource.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * 
 * <p>Title: ObjectTools</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class ObjectTools {
	
	/**
	 * object对象转换成String 
	 * @param parameter 参数
	 * @return 参数为空返回空, 反之返回字符串且去空格
	 */
	public  static String objectToStringTrim(Object parameter){
		if(parameter!=null){
			return ((String)parameter).trim();
		}else{
			return null;
		}
	}

	/**
	 * 获取DBObject对象
	 * @param map map对象
	 * @return DBObject 对象
	 */
	public static DBObject mapToDBObject(Map map){
		DBObject dbObject =new BasicDBObject();
		if(map!=null){
			for (String s : (Iterable<String>) map.keySet()) {
				String key = StringTools.trim(s);
				Object value = map.get(key);
				if (value != null) {
					if(key.equals("_id")){
						dbObject.put(key, new ObjectId(value.toString()));
					}else{
						dbObject.put(key, value);
					}

				}
			}
		}
		return dbObject;
	}

//	public static TtsxFiles objectToTtsxFiles(DBObject dbObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//
//		if(dbObject!=null){
//			TtsxFiles ttsxFiles = new TtsxFiles();
//			Field[] fields = ttsxFiles.getClass().getDeclaredFields();
//			Field[] superFields = TtsxFiles.class.getSuperclass().getDeclaredFields();
//			setValue(ttsxFiles,dbObject,fields);
//			setValue(ttsxFiles,dbObject,superFields);
//
//			Iterator<String> keys =	dbObject.keySet().iterator();
//			while(keys.hasNext()){
//				String key = keys.next();
//				ttsxFiles.getExtFieldMap().put(key,dbObject.get(key));
//			}
//			return ttsxFiles;
//		}
//		return null;
//	}


//	public static void setValue(TtsxFiles ttsxFiles,DBObject dbObject,Field[] fields) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//		for (Field field : fields) {
//			String varName = field.getName();
//			Object object = dbObject.get(varName);
//			if (object != null) {
//				String methodName=varName.substring(0,1).toUpperCase()+varName.substring(1);
//				if(varName.startsWith("_")){methodName=varName;}
////				System.out.println(methodName+"=="+object+"=="+field.getType());
//				Method setValue = ttsxFiles.getClass().getMethod("set"+methodName, field.getType());
//				if( object instanceof String || object instanceof ObjectId){
//					setValue.invoke(ttsxFiles,object.toString());
//				}else if( object instanceof Long ||  object instanceof Integer){
//
//					setValue.invoke(ttsxFiles,Long.parseLong(object.toString()));
//				}else if( object instanceof Double){
//					setValue.invoke(ttsxFiles,(Double)object);
//				}else if( object instanceof Float){
//					setValue.invoke(ttsxFiles,(Float)object);
//				}else if( object instanceof Boolean){
//					setValue.invoke(ttsxFiles,(Boolean)object);
//				}else if( object instanceof Date){
//					setValue.invoke(ttsxFiles,(Date)object);
//				}else if( object instanceof Byte){
//					setValue.invoke(ttsxFiles,(Byte)object);
//				}else{
//					setValue.invoke(ttsxFiles,object);
//				}
//				dbObject.removeField(varName);
//			}
//		}
//	}
	public static DBObject getDBObject(String key ,Object value){
		return new BasicDBObject(key,value);
	}
}
