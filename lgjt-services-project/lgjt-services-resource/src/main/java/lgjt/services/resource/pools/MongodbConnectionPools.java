/**
 * mongodb连接池管理器
 *
 * @author dai.jiaqi
 */
package lgjt.services.resource.pools;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lgjt.services.resource.config.MongodbConfig;
import lgjt.services.resource.util.StringTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class MongodbConnectionPools {

    /**
     * 链接类型 : 单点
     */
    private static final int CONNECTTYPE_SINGLE = 0;
    /**
     * 链接类型 : 集群
     */
    private static final int CONNECTTYPE_COLONY = 1;
    /**
     * 链接类型 : 异常
     */
    private static final int CONNECTTYPE_ERROR = -1;

    private static HashMap<String, MongoClient> MONGODB_CLIENTS = new HashMap<String, MongoClient>();

    private static MongodbConnectionPools MONGODBCONNECTIONPOOLS = null;

    static {
        MONGODBCONNECTIONPOOLS = new MongodbConnectionPools();
        MONGODBCONNECTIONPOOLS.init();
    }

    public static MongodbConnectionPools getInstance() {
        if (MONGODBCONNECTIONPOOLS == null) {
            MONGODBCONNECTIONPOOLS = new MongodbConnectionPools();
            MONGODBCONNECTIONPOOLS.init();
        }
        return MONGODBCONNECTIONPOOLS;
    }

    private void init() {
        List<Properties> keys = MongodbConfig.getMongoServersConfigs();
        setMongodbConnect(keys);
    }

    /**
     * 给mongodb连接池创建连接
     * @param properties  配置文件
     */
    private void setMongodbConnect(List<Properties> properties) {
        if (properties == null || properties.size() == 0) {
            return;
        }
        for (int i = 0; i < properties.size(); i++) {
            String hostValue = properties.get(i).getProperty(MongodbConfig.CONFIG_KEY_MONGODB_HOST);
            String userValue = properties.get(i).getProperty(MongodbConfig.CONFIG_KEY_MONGODB_USER);
            String passwordValue = properties.get(i).getProperty(MongodbConfig.CONFIG_KEY_MONGODB_PASSWORD);
            String dbnameValue = properties.get(i).getProperty(MongodbConfig.CONFIG_KEY_MONGODB_DBNAME);
            String index = properties.get(i).getProperty(MongodbConfig.CONFIG_KEY_MONGODB_INDEX);
            setMongoDatabase(index, hostValue, userValue, passwordValue, dbnameValue);

        }
    }

    /**
     * 获取mongodb链接
     * @param index 唯一标识
     * @param hosts ip:ports,ip:ports
     * @param user 用户名
     * @param password 密码
     * @param dbname 表名 方法没用到可以不传
     * @return 数据库连接
     */
    private void setMongoDatabase(String index, String hosts, String user, String password, String dbname) {
        int connectType = checkConnectType(hosts);
        MongoClient mongoClient = null;
        if (connectType == CONNECTTYPE_SINGLE) {
            String[] hostArray = hosts.split(":");
            if (hostArray != null && hostArray.length == 2) {
                MongoCredential credential = MongoCredential.createScramSha1Credential(user, dbname, password.toCharArray());
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);
                mongoClient = new MongoClient(new ServerAddress(hostArray[0], Integer.parseInt(hostArray[1])), credentials);
            }
        } else if (connectType == CONNECTTYPE_COLONY) {
            String[] hostArray = hosts.split(",");
            List<ServerAddress> serverAddresss = new ArrayList<ServerAddress>();
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            for (int i = 0; i < hostArray.length; i++) {
                String[] host = hostArray[i].split(":");
                ServerAddress serverAddress = new ServerAddress(host[0], Integer.parseInt(host[1]));
                serverAddresss.add(serverAddress);
                MongoCredential credential = MongoCredential.createScramSha1Credential(user, dbname, password.toCharArray());
                credentials.add(credential);
            }
            mongoClient = new MongoClient(serverAddresss, credentials);
        } else {
            //不处理
        }
        if (mongoClient != null) {
            MONGODB_CLIENTS.put(StringTools.trim(index), mongoClient);
        }
    }

    /**
     * 判断是否是单点
     * @param hostValue  mongodb-host* value
     * @return  -1 无效，0单点，1集群
     */
    private int checkConnectType(String hostValue) {
        int result = CONNECTTYPE_ERROR;
        hostValue = StringTools.trim(hostValue);
        if (hostValue.indexOf(",") > 0) {
            result = CONNECTTYPE_COLONY;
        } else {
            result = CONNECTTYPE_SINGLE;
        }
        return result;
    }

    /**
     * 获取数据库链接
     * @param index 前缀
     * @param dbName 表名
     * @return 数据库链接
     */
    public final DB getDB(String index, String dbName) {
        MongoClient mongoClient = MONGODB_CLIENTS.get(StringTools.trim(index));
        DB db = mongoClient.getDB(StringTools.trim(dbName));
        return db;
    }


    /**
     * 获取数据库链接
     * @param index 前缀
     * @param dbName 表名
     * @return 数据库链接
     */
    public final MongoDatabase getDatabase(String index, String dbName) {
        MongoClient mongoClient = MONGODB_CLIENTS.get(StringTools.trim(index));
        return mongoClient.getDatabase(StringTools.trim(dbName));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(MONGODB_CLIENTS.size());
    }
}