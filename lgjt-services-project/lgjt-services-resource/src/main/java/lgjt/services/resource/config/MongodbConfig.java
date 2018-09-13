package lgjt.services.resource.config;

import lgjt.services.resource.exception.MongodbException;
import lgjt.services.resource.exception.MongodbExceptionEnum;
import lgjt.services.resource.util.MongodbConfigUtil;
import lgjt.services.resource.util.StringTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 解析properties 配置文件
 *
 * @author dai.jiaqi
 */
public class MongodbConfig {
    /**
     * 根 配置文件名
     */
    private static final String ROOT_CONFIG_FILE = "mongodb-root.properties";

    /**
     * 根 配置文件的MAP 唯一标识
     */
    public static final String ROOT_CONFIG_FILE_INDEX = "mongodb-root";

    /**
     * 根 子配置文件名集合 “;” 英文分号分隔
     */
    public static final String CONFIG_KEY_MONGODB_SERVER_INCLUDES = "mongodb-server-includes";

    /**
     * 子文件 服务器唯一标示的KEY
     */
    public static final String CONFIG_KEY_MONGODB_INDEX = "mongodb-index";

    /**
     * 子文件 服务器链接信息
     */
    public static final String CONFIG_KEY_MONGODB_HOST = "mongodb-host";
    /**
     * 子文件 数据库名
     */
    public static final String CONFIG_KEY_MONGODB_DBNAME = "mongodb-dbname";
    /**
     * 子文件 数据库用户名
     */
    public static final String CONFIG_KEY_MONGODB_USER = "mongodb-user";
    /**
     * 子文件 数据库密码
     */
    public static final String CONFIG_KEY_MONGODB_PASSWORD = "mongodb-password";
    /**
     * 子文件 数据库表名
     */
    public static final String CONFIG_KEY_MONGODB_TABLENAME = "mongodb-tablename";


    /**
     * 配置文件全部对象MAP
     */
    private static Map<String, Properties> CONFIG_MAP = new HashMap<String, Properties>();


    static {
        try {
            Properties rootConfig = configLoad(ROOT_CONFIG_FILE);
            CONFIG_MAP.put(ROOT_CONFIG_FILE_INDEX, rootConfig);
            String[] mongodbServers = MongodbConfigUtil.formatMongodbServerIncludes(rootConfig.getProperty(CONFIG_KEY_MONGODB_SERVER_INCLUDES));
            for (int i = 0; i < mongodbServers.length; i++) {
                Properties childConfig = configLoad(mongodbServers[i]);
                String mongodbIndex = StringTools.trim(childConfig.getProperty(CONFIG_KEY_MONGODB_INDEX));
                if (mongodbIndex.length() == 0) {
                    throw new MongodbException(MongodbExceptionEnum.config_00004, CONFIG_KEY_MONGODB_INDEX);
                }
                if (CONFIG_MAP.get(mongodbIndex) != null) {
                    throw new MongodbException(MongodbExceptionEnum.config_00005);
                }
                //判断子文件中的必填项 mongodb-index、mongodb-host、mongodb-dbname
                String mongodbHost = childConfig.getProperty(CONFIG_KEY_MONGODB_HOST);
                if (mongodbHost.length() == 0) {
                    throw new MongodbException(MongodbExceptionEnum.config_00004, CONFIG_KEY_MONGODB_HOST);
                }
                String mongodbDBname = childConfig.getProperty(CONFIG_KEY_MONGODB_DBNAME);
                if (mongodbDBname.length() == 0) {
                    throw new MongodbException(MongodbExceptionEnum.config_00004, CONFIG_KEY_MONGODB_DBNAME);
                }
                CONFIG_MAP.put(mongodbIndex, childConfig);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取根配置文件
     *
     * @return
     */
    public static Properties getRootConfig() {
        return (Properties) CONFIG_MAP.get(ROOT_CONFIG_FILE_INDEX).clone();
    }

    /**
     * 获取子配置文件集合
     *
     * @return
     */
    public static List<Properties> getMongoServersConfigs() {
        List<Properties> result = new ArrayList<Properties>();
        Iterator<String> iterator = CONFIG_MAP.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!key.equals(ROOT_CONFIG_FILE_INDEX)) {
                result.add((Properties) CONFIG_MAP.get(key).clone());
            }

        }
        return result;
    }
//    /**
//     * 给子配置文件设置全局变量 -- 废弃 应该是用到去 全局变量里拿
//     * @param rootConfig
//     * @param childConfig
//     */
//	private static void settingGlobalVariables(Map<String,String> rootConfig ,Map<String,String> childConfig ){
//	    if(childConfig==null){
//            return;
//        }
//        Iterator<String> iteratorsChild= childConfig.keySet().iterator();
//        while(iteratorsChild.hasNext()){
//            String key = iteratorsChild.next();
//            String value = childConfig.get(key);
//            if(value.length()==0){
//                String rootValue = rootConfig.get(key);
//                if(rootValue.length()>0){
//                    childConfig.put(key,rootValue);
//                }
//            }
//        }
//
//        Iterator<String> iteratorsRoot= rootConfig.keySet().iterator();
//        while(iteratorsRoot.hasNext()){
//            String key = iteratorsRoot.next();
//            if(key.equals(CONFIG_KEY_MONGODB_SERVER_INCLUDES)){
//                continue;
//            }
//
//            String value = childConfig.get(key);
//            if(value.length()==0){
//                String rootValue = rootConfig.get(key);
//                if(rootValue.length()>0){
//                    childConfig.put(key,rootValue);
//                }
//            }
//        }
//
//    }

    /**
     * 加载配置文件
     *
     * @param configFileName 配置文件名称
     * @return 当前配置文件的map对象
     * @throws IOException
     */
    private static Properties configLoad(String configFileName) throws Exception {
        Properties propertie = new Properties();
        InputStream in = null;
        try {
            System.out.println(configFileName);
            in = MongodbConfig.class.getClassLoader().getResourceAsStream(configFileName);
            propertie.load(in);
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }
        return propertie;
    }

    /**
     * 根据唯一标识和key获取对应的值
     *
     * @param mongodbIndex 唯一标示
     * @param key          参数名
     */
    public static String getValue(String mongodbIndex, String key) {
        Properties propertie = CONFIG_MAP.get(StringTools.trim(mongodbIndex));
        if (propertie == null) {
            return null;
        }
        String value = propertie.getProperty(StringTools.trim(key));
        if (value == null) {
            return CONFIG_MAP.get(MongodbConfig.ROOT_CONFIG_FILE_INDEX).getProperty(StringTools.trim(key));
        } else {
            return value;
        }
    }

//	/**
//	 * 根据key获取value
//	 * @param startKey 参数名
//	 */
//	public static List<String> getKeysStartsWith(String mongodbIndex,String startKey){
//		List<String> result=new ArrayList<String>();
//        startKey = StringTools.trim(startKey);
//        Map<String,String> map = CONFIG_MAP.get(StringTools.trim(mongodbIndex));
//        if(map==null){
//            return null;
//        }
//        Iterator<String> keys =map.keySet().iterator();
//		while(keys.hasNext()){
//			String key = StringTools.trim((String)keys.next());
//			if(key.length()>0 && key.startsWith(startKey)){
//				result.add(key);
//			}
//		}
//		return	result;
//	}
//
//    /**
//     * 根据KEY获取配置值
//     * @param map 配置文件
//     * @param key key
//     * @return value
//     */
//	private static String getValueTrim(Map<String,String> map,String key){
//	    if(map==null || key==null){
//            return  "";
//        }else{
//            return  map.get(key);
//        }
//    }
}
