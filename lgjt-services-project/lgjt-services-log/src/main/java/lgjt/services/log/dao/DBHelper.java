package lgjt.services.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.ttsx.platform.tool.util.PropertyUtil;
/**
 * 
 * <p>Title: DBHelper</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class DBHelper {
	
	
	// 同一配置, 始终保持同一个连接
	private static final ConcurrentHashMap<String, Datastore> DATA_STORE_MAP = new ConcurrentHashMap<String, Datastore>();
	// 锁, 防止重复连接同一配置mongo
	private static final Object lock = new Object();
	public static Datastore getDataStore() {
		String addresses =PropertyUtil.getProperty("mongo-addresses");
		int port=PropertyUtil.getInt("mongo-port", 27017);
		String database = PropertyUtil.getProperty("mongo-database");
		String key = addresses+database;
		Datastore datastore = DATA_STORE_MAP.get(key);
		if (datastore == null) {
			try {
				synchronized (lock) {
					datastore = DATA_STORE_MAP.get(key);
					if (datastore != null) {
						return datastore;
					}
					Morphia morphia = new Morphia();
					morphia.mapPackage("lgjt.services.log");
					
					MongoCredential credential=MongoCredential.createCredential(PropertyUtil.getProperty("mongo-username"), database,PropertyUtil.getProperty("mongo-password").toCharArray());
					List<MongoCredential> listm=new ArrayList<MongoCredential>();
					listm.add(credential);
					List<ServerAddress> lists=new ArrayList<ServerAddress>();
					ServerAddress serverAddress =new ServerAddress(addresses,port);
					lists.add(serverAddress);
					MongoClient mongo = new MongoClient(lists,listm);
					datastore = morphia.createDatastore(mongo, database);
					DATA_STORE_MAP.put(key, datastore);
				}
			} catch (Exception e) {
				throw new IllegalStateException(String.format(
						"connect mongo failed! addresses: {}, database: {}",
						addresses, database), e);
			}
		}
		return datastore;
	}
	
	
}
