package lgjt.domain.rush.utils;

import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import lgjt.domain.rush.AnswerInfo;
import lgjt.domain.rush.ques.QuesQuestions;

import java.util.ArrayList;
import java.util.List;

/**
 * 答题信息工具类
 * @author Administrator
 *
 */
public class AnswerInfoUtil {
	
	//最后使用时间HASH_TIME是用于清除垃圾。放入redis后，通过redis的过期时间即可解决
    
    //redis中的主键：   AnswerInfo.getType()+"_"+AnswerInfo.getRelationId();
    //AnswerInfo.getType()固定为AnswerInfo.T_GATE
    //AnswerInfo.getRelationId()就是闯关记录ID
    
    //答对题目的主键为：RIGHT:AnswerInfo.getType()+"_"+AnswerInfo.getRelationId()
    //打错题目的主键为：WRONG:AnswerInfo.getType()+"_"+AnswerInfo.getRelationId()
    
    private static String RIGHT = "RIGHT:";
    private static String WRONG = "WRONG:";
	private static String RUSH_QUES = "RUSH_QUES:";
    
    /**
	 * 保存一个答题信息
	 * @param info
	 */
	public static void add(AnswerInfo info){
		if(info==null)return;
		String id = info.getType()+"_"+info.getRelationId();
		if(info.isRight()){
			String key = RIGHT+id;
			IObjectCache cache = CacheFactory.getObjectCache();
			Object o = cache.get(key);//从redis中取正确答案
			if(o==null){//之前没有。构造对象存入redis
				List<AnswerInfo> list = new ArrayList<AnswerInfo>();			
				list.add(info);
				cache.set(key, list);
				cache.setExpireTime(key, 86400);//一天之后过期
			}
			else{//redis中有，取出后更新，再存入redis
				List<AnswerInfo> list = (List<AnswerInfo>)o;
				list.add(info);
				cache.set(key, list);
				cache.setExpireTime(key, 86400);//一天之后过期
			}
		}
		else{
			String key = WRONG+id;
			IObjectCache cache = CacheFactory.getObjectCache();
			Object o = cache.get(key);//从redis中取错误答案
			if(o==null){//之前没有。构造对象存入redis
				List<AnswerInfo> list = new ArrayList<AnswerInfo>();			
				list.add(info);
				cache.set(key, list);
				cache.setExpireTime(key, 86400);//一天之后过期
			}
			else{//redis中有，取出后更新，再存入redis
				List<AnswerInfo> list = (List<AnswerInfo>)o;
				list.add(info);
				cache.set(key, list);
				cache.setExpireTime(key, 86400);//一天之后过期
			}
		}
	}
	/**
	 * 获取答题信息
	 * @param relationId   关联Id
	 * @param type   关联Id类型
	 * @param right  是否答错
	 * @return
	 */
	public static List<AnswerInfo> getAnswerInfo(String relationId,int type,boolean right){
		String id = type+"_"+relationId;
		if(right){
			String key = RIGHT+id;
			IObjectCache cache = CacheFactory.getObjectCache();
			Object o = cache.get(key);//从redis中取答案
			if(o==null){//构造一个空的list返回
				return new ArrayList<AnswerInfo>();	
			}
			else{
				return (List<AnswerInfo>)o;
			}
		}
		else{
			String key = WRONG+id;
			IObjectCache cache = CacheFactory.getObjectCache();
			Object o = cache.get(key);//从redis中取答案
			if(o==null){//构造一个空的list返回
				return new ArrayList<AnswerInfo>();	
			}
			else{
				return (List<AnswerInfo>)o;
			}
		}
	}
	/**
	 * 删除答题信息
	 * @param relationId   关联Id
	 * @param type   关联Id类型
	 */
	public static void remove(String relationId,int type){
		String id = type+"_"+relationId;
		IObjectCache cache = CacheFactory.getObjectCache();
		cache.del(RIGHT+id);
		cache.del(WRONG+id);
	}
	
	
	
	/**
	 * 获取内存信息。用于管理。
	 * @return
	 */
    public static String getMemoryInfo(){
    	//不使用内存，放入redis中，返回空即可
    	StringBuffer sb = new StringBuffer();
    	return sb.toString();
    }


	/**
	 * 添加用户闯关题库
	 * @param sequenceId 工种id
	 * @param userId 用户id
	 * @param quesIds 题库id字符串
	 */
	public static void addRushQuestion(String sequenceId,String userId,List<QuesQuestions> quesIds){
		IObjectCache cache = CacheFactory.getObjectCache();
		String key = RUSH_QUES + sequenceId + userId;
		Object o = cache.get(key);
		if(o==null){
			cache.set(key,quesIds);
			cache.setExpireTime(key, 600);//10分钟
		}
		else{
			cache.set(key, quesIds);
			cache.setExpireTime(key, 600);//10分钟
		}
	}

	/**
	 * 获取用户对战题库
	 * @param sequenceId 工种id
	 * @param userId 用户id
	 */
	public static List<QuesQuestions> getRushQuestion(String sequenceId,String userId){
		IObjectCache cache = CacheFactory.getObjectCache();
		String param = RUSH_QUES + sequenceId + userId;
		Object o = cache.get(param);
		if(o!=null)
			return  (List<QuesQuestions>)o;
		else
			return null;
	}
    
    //SubThread主要是清除垃圾数据，放入redis后，该线程不需要了。通过redis的过期时间解决垃圾数据
	
}
