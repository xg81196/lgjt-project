package lgjt.services.log.dao;


import org.mongodb.morphia.dao.BasicDAO;

/**
 * 
 * <p>Title: MongoDao</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
public class MongoDao<T,K> extends BasicDAO<T, K>{
	public MongoDao() {
		super(DBHelper.getDataStore());
	}
}
