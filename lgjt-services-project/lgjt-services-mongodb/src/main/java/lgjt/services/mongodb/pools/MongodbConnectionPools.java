package lgjt.services.mongodb.pools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import lgjt.services.mongodb.config.MongodbConfig;
import lgjt.services.mongodb.util.StringTools;
/**
 * mongodb连接池管理器
 * @author daijiaqi
 */
public class MongodbConnectionPools{
    /**
     * mongodb 连接串的前缀
     */
	private static final  String MONGODB_HOST ="mongodb-host";
    private static final  String MONGODB_USER ="mongodb-user";
    private static final  String MONGODB_PASSWORD ="mongodb-password";
    private static final  String MONGODB_DBNAME ="mongodb-dbname";

    /**
     * 链接类型 : 单点
     */
    private static final  int CONNECTTYPE_SINGLE= 0;
    /**
     * 链接类型 : 集群
     */
    private static final  int CONNECTTYPE_COLONY= 1;
    /**
     * 链接类型 : 异常
     */
    private static final  int CONNECTTYPE_ERROR= -1;

	private static HashMap<String,MongoClient> MONGODB_CLIENTS=new  HashMap<String,MongoClient>();
//    private static HashMap<String,DB> MONGODB_DBS=new  HashMap<String,DB>();;
    private static MongodbConnectionPools MONGODBCONNECTIONPOOLS=null;
     static{
            MONGODBCONNECTIONPOOLS=new MongodbConnectionPools();
            MONGODBCONNECTIONPOOLS.init();
    }
    public static MongodbConnectionPools getInstance(){
         if(MONGODBCONNECTIONPOOLS==null){
             MONGODBCONNECTIONPOOLS=new MongodbConnectionPools();
             MONGODBCONNECTIONPOOLS.init();
         }
        return MONGODBCONNECTIONPOOLS;
    }
    private void init(){
		List<String> keys = MongodbConfig.getKeysByPrefix(MONGODB_HOST);
        setMongodbConnect(keys);
	}

    /**
     * 给mongodb连接池创建连接
     * @param keys  配置文件KEY集合
     */
	private void setMongodbConnect(List<String> keys){
        if(keys==null || keys.size()==0){
            return;
        }
        for(int i =0;i<keys.size();i++){
            String hostKey =keys.get(i);
            String[] hosts =hostKey.split("-");
            if(hosts.length==3){
                String suffix =  hosts[2];
                String hostValue = MongodbConfig.getValue(MONGODB_HOST+"-"+suffix);
                String userValue = MongodbConfig.getValue(MONGODB_USER+"-"+suffix);
                String passwordValue =  MongodbConfig.getValue(MONGODB_PASSWORD+"-"+suffix);
                String dbnameValue =  MongodbConfig.getValue(MONGODB_DBNAME+"-"+suffix);
                setMongoDatabase(hostValue,userValue,passwordValue,dbnameValue,suffix);
            }
        }
    }

    /**
     * 获取mongodb链接
     * @param hosts ip:ports,ip:ports
     * @param user 用户名
     * @param password 密码
     * @param dbname 表名 方法没用到可以不传
     * @return 数据库连接
     */
    private  void setMongoDatabase(String hosts,String user,String password,String dbname,String suffix){
	    int connectType = checkConnectType(hosts);
        MongoClient mongoClient= null;
        if(connectType==CONNECTTYPE_SINGLE){
            String[] hostArray=hosts.split(":");
            if(hostArray!=null && hostArray.length==2){
                MongoCredential credential = MongoCredential.createScramSha1Credential(user, dbname, password.toCharArray());
                List<MongoCredential> credentials = new ArrayList<MongoCredential>();
                credentials.add(credential);
                mongoClient = new MongoClient(new ServerAddress(hostArray[0], Integer.parseInt(hostArray[1])),credentials);
            }
        }else if(connectType==CONNECTTYPE_COLONY){
            String[] hostArray=hosts.split(",");
            List<ServerAddress> serverAddresss=new ArrayList<ServerAddress>();
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            for(int i=0;i<hostArray.length;i++){
                String[] host=hostArray[i].split(":");
                ServerAddress serverAddress=new ServerAddress(host[0], Integer.parseInt(host[1]));
                serverAddresss.add(serverAddress);
                MongoCredential credential = MongoCredential.createScramSha1Credential(user, dbname, password.toCharArray());
                credentials.add(credential);
            }
            mongoClient = new MongoClient(serverAddresss,credentials);
        }else{
            //不处理
        }
        if(mongoClient!=null){
            MONGODB_CLIENTS.put(StringTools.trim(suffix),mongoClient);
        }
    }

    /**
     * 判断是否是单点
     * @param hostValue  mongodb-host* value
     * @return  -1 无效，0单点，1集群
     */
    private int checkConnectType(String hostValue){
        int result =CONNECTTYPE_ERROR;
        hostValue = StringTools.trim(hostValue);
        if(hostValue.indexOf(",")>0){
            result = CONNECTTYPE_COLONY;
        }else{
            result =  CONNECTTYPE_SINGLE;
        }
        return result;
    }

    /**
     * 获取数据库链接
     * @param index 前缀
     * @param dbName 表名
     * @return 数据库链接
     */
    public final DB getDB(String index,String dbName){
        MongoClient mongoClient  =  MONGODB_CLIENTS.get(StringTools.trim(index));
        DB db = mongoClient.getDB(StringTools.trim(dbName));
        return db;
    }


    /**
     * 获取数据库链接
     * @param index 前缀
     * @param dbName 表名
     * @return 数据库链接
     */
    public final MongoDatabase getDatabase(String index,String dbName){
        MongoClient mongoClient  =  MONGODB_CLIENTS.get(StringTools.trim(index));
        return mongoClient.getDatabase(StringTools.trim(dbName));
    }
}