package lgjt.services.mongodb.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.mongodb.util.JSON;
import jdk.nashorn.internal.parser.JSONParser;
import org.bson.json.JsonMode;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import lgjt.domain.mongodb.TtsxFiles;
import lgjt.services.mongodb.config.MongodbConfig;
import lgjt.services.mongodb.pools.MongodbConnectionPools;

/**
 * mongodb数据库基础类
 * <p>Title: MongodbService</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年4月27日
 */
public class MongodbService {
	private String index;
	private static Map<String,MongodbService> cache =new HashMap<String,MongodbService>();

	public static MongodbService getInstance(String index) {
		if(cache.get(index)==null){
			cache.put(index,new MongodbService(index));
		}
		return cache.get(index);
	}
	private MongodbService(String index){
		this.index=index;
	}
  	public DB getDB(){
        return MongodbConnectionPools.getInstance().getDB(index,MongodbConfig.getValue("mongodb-dbname-"+index));
    }
  	public GridFS getGridFS(){
        return new GridFS(this.getDB());
    }
    public DBCollection getDBCollectionByName(String collectionName){
        return getDB().getCollection(collectionName);
    }
  	/**
  	 * 文件上传
  	 * <p>Title: upload</p>  
  	 * <p>Description: </p>  
  	 * @author daijiaqi  
  	 * @date 2018年4月27日  
  	 * @param inputStream
  	 * @return 文件ID
  	 * @throws IOException
  	 */
    public String upload(InputStream inputStream){
        GridFS myFS=getGridFS();
        GridFSInputFile inputFile = myFS.createFile(inputStream);
        inputFile.save();
        return inputFile.getId().toString();
    }

	/**
	 * 文件上传
	 * <p>Title: upload</p>
	 * <p>Description: </p>
	 * @author daijiaqi
	 * @date 2018年4月27日
	 * @param inputStream
	 * @return 文件ID
	 * @throws IOException
	 */
	public String upload(InputStream inputStream,TtsxFiles ttsxFiles){
		GridFS myFS=getGridFS();
		GridFSInputFile inputFile = myFS.createFile(inputStream);
		if(ttsxFiles!=null){
			ttsxFiles.setMd5(inputFile.getMD5());
		}
		DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(ttsxFiles));
		inputFile.setMetaData(dbObject);
		inputFile.save();
		return inputFile.getId().toString();
	}
    
    /**
	 * 获取文件流
	 * <p>Title: readMongoFile</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月27日  
	 * @param id
	 * @return
	 */
	public InputStream readById(String id) {
		  	 GridFS gridFS = getGridFS();
	         DBObject query  = new BasicDBObject("_id", new ObjectId(id));
	         GridFSDBFile gridFSDBFile = gridFS.findOne(query);
	         return gridFSDBFile.getInputStream();
	}

	public void downloadOld(OutputStream outputStream, String id, long skip) throws IOException {
		InputStream inputStream =null;
		try {
		GridFS gridFS =getGridFS();
		DBObject query  = new BasicDBObject("_id", new ObjectId(id));
		GridFSDBFile gridFSDBFile = gridFS.findOne(query);
		 inputStream = gridFSDBFile.getInputStream();
		if(skip>0){
			inputStream.skip(skip);
		}
		byte[] bs=new byte[1024];
		int len;
		while(inputStream.read(bs)>-1){
			outputStream.write(bs);
			outputStream.flush();
		}
		outputStream.close();
		}catch(Exception e){
			throw e;
		}finally {
			if(inputStream!=null){
				inputStream.close();
			}
			if(outputStream!=null){
				outputStream.close();
			}
		}
	}
	public void download(OutputStream outputStream, String id, long skip,long length) throws IOException {
		InputStream inputStream=null;
		try {
			GridFS gridFS = getGridFS();
			DBObject query = new BasicDBObject("_id", new ObjectId(id));
			GridFSDBFile gridFSDBFile = gridFS.findOne(query);
//		long length=Long.parseLong(gridFSDBFile.getMetaData().get("length").toString().trim());
			 inputStream = gridFSDBFile.getInputStream();
			if (skip > 0) {
				inputStream.skip(skip);
			}
			byte[] bs = new byte[1024];
			int len;
			while ((len = inputStream.read(bs)) != -1) {
//			outputStream.write(bs);
//			outputStream.flush();
				if (length > 0) {
					if (length > len) {
						outputStream.write(bs, 0, len);
						outputStream.flush();
						length -= len;
					} else {
						outputStream.write(bs, 0, (int) length);
						outputStream.flush();
						break;
					}
				} else
					outputStream.write(bs, 0, len);
				outputStream.flush();
			}
		}catch(Exception e){
			throw e;
		}finally {
			if(inputStream!=null){
				inputStream.close();
			}
			if(outputStream!=null){
				outputStream.close();
			}
		}

	}
}
