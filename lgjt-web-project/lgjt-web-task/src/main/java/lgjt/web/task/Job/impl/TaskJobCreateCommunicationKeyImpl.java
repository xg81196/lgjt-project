package lgjt.web.task.Job.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.quartz.JobDataMap;

import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.util.RedisUtil;

import lgjt.common.base.utils.RandomNumberUtils;
import lgjt.domain.task.systask.secretkey.SysSecretKey;
import lgjt.services.task.secretkey.SysSecretKeyService;
import lgjt.web.task.Job.TaskJob;

/**
 * 
 * <p>Title: 生成临时秘钥任务类</p>  
 * <p>Description: 根据需要生成临时秘钥存放到redis里</p>  
 * @author daijiaqi  
 * @date 2018年4月23日
 */
public class TaskJobCreateCommunicationKeyImpl extends TaskJob {
	@Inject
    private SysSecretKeyService sysSecretKeyService;
	
	private SysSecretKey sysSecretKeyQuery =null;
	
	private SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 默认生成多少天的key
	 */
	private static int createDays=5;
	

	/**
	 * 天的毫秒数
	 */
	private static int dayMseconds = 24*60*60*1000;
	
	private static String REDIS_PREFIX=StringUtil.trim(PropertyUtil.getProperty("redis-prefix"));
	
	private static DecimalFormat DECIMALFORMAT_00=new DecimalFormat("00");
	
	@Override
	public String work(JobDataMap jobDataMap) throws Exception {
		 StringBuffer lastInfo = new StringBuffer("生成临时秘钥:"); 
		 if(sysSecretKeyQuery==null) {
			 sysSecretKeyQuery = new SysSecretKey();
			 //状态为启用
			 sysSecretKeyQuery.setStatus(SysSecretKey.STATUS_ENABLED);
		 }
		 
		 JobDataMap data = jobDataMap;
		 try {
			 createDays = data.getInt("createDays");
		 }catch(Exception e) { }
		 lastInfo.append("createDays="+createDays+";");
		 List<SysSecretKey> sysSecretKeys= sysSecretKeyService.query(sysSecretKeyQuery);
		 if(sysSecretKeys==null || sysSecretKeys.size()==0) {
			 lastInfo.append("sys_secret_key表中无符合条件的记录;");
		 }else {
			;
			 for(int i=0;i<sysSecretKeys.size();i++) {
				 String appId=sysSecretKeys.get(i).getId();
			 List<String> nextDays=getNextNday(createDays);
			 String key ="";
			 for (String day : nextDays) {
				 key = REDIS_PREFIX+appId+day;
				 if(!CacheFactory.getObjectCache().exist(key)) {
					 createKeysByDate(day,key);
					 lastInfo.append(key+";");
				 }
			 }
		 }
		 }
		 return lastInfo.toString();
	}
	
	
	/**
	 * 
	 * <p>Title: 获取当天向后推day天的日期</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月23日  
	 * @param day
	 * @return
	 */
	private List<String> getNextNday(int day) {
		Date d = new Date();
		List<String> result =new ArrayList<String>();
		for(int i=0;i<day;i++) {
			result.add(sdf_yyyyMMdd.format(new Date(d.getTime()+i*dayMseconds)));
		}
		return result ;
	}
	
	/**
	 * 
	 * <p>Title: 根据yyyyMMdd生成key</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年4月23日  
	 * @param day
	 * @throws ParseException 
	 */
	private void createKeysByDate(String day,String key) throws ParseException {
		Map<String,String> map = new HashMap<String,String>();
		//00 -23
		for(int i=0;i<24;i++) {
			map.put(DECIMALFORMAT_00.format(i), RandomNumberUtils.getRandomNumberBy(32));
		}
		int seconds =(int)((sdf_yyyyMMdd.parse(day).getTime()+dayMseconds)/1000/60);
		IObjectCache<Map>  objectCache =CacheFactory.getObjectCache();
		objectCache.set(key, map);
		objectCache.setExpireTime(key, seconds);
	}
}
