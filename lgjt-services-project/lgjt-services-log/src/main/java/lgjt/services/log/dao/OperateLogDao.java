package lgjt.services.log.dao;

import org.nutz.ioc.loader.annotation.IocBean;

import lgjt.services.log.operate.OperateLog;


/**
 * 
 * <p>Title: OperateLogDao</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月1日
 */
@IocBean
public class OperateLogDao extends MongoDao<OperateLog, String> {
	
}
