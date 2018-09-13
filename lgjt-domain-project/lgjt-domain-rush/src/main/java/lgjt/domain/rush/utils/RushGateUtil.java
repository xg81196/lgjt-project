package lgjt.domain.rush.utils;

import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import lgjt.domain.rush.AnswerInfo;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.domain.rush.sequence_record.RushSequenceRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/7/24
 * @Description:闯关工具类
 */
public class RushGateUtil {


    /**
     * 判断是否过关
     * @param gr   当前闯关信息
     * @param nowPG  当前关口
     * @param isRepeat  是否是重复闯关
     * @return   本关Id：可继续闯关。下一关ID：闯关成功。-1：闯关失败。0：闯关成功。但下一关不存在
     */
    public static String passGate(RushSequenceRecord gr, RushSequence nowPG,boolean isRepeat) throws Exception{
        if(gr==null || nowPG==null){
            throw new Exception("parameter is null");
        }
        List<AnswerInfo> list_right = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, true);
        if(list_right==null)list_right = new ArrayList<>();
        List<AnswerInfo> list_wrong = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, false);
        if(list_wrong==null)list_wrong=new ArrayList<>();
        int count_right = list_right.size();//答对题目数
        int count_wrong = list_wrong.size();//答错题目数
        int passCondition = 1;//过关方式

        //判断答题时间。先以关口定义为准
        int limitTime = nowPG.getLimitTime();
        if(limitTime>=0){
            limitTime = limitTime*1000;
            if(limitTime>0){
                long beginTime = gr.getRushStartTime().getTime();
                long nowTime = System.currentTimeMillis();
                if((nowTime-beginTime)>limitTime){
                    System.out.println("闯关失败-----  -12");
                    return -12+"";//闯关失败（）超时
                }
            }
        }

    if(passCondition==RushSequence.PASSCONTITIONREQUEST_PASS){//答对一定题目数
            long value = -1;//要求答对的题目数
            try{
                value = Long.parseLong(nowPG.getPassContitionValue()+"");
            }
            catch(Exception e){}
            if(value<=0 && count_wrong>0){//不允许有错题
                System.out.println("不允许有错题-----  -1");
                return -1+"";//闯关失败
            }
            int maxNumber = nowPG.getQuesNums();//最多出题数
            int maxWrong = -1;//最多出错数，负数表示无限制
            if(maxNumber>0 && value>0){
                maxWrong = maxNumber - (int)value;
            }
        System.out.println("----maxWrong="+maxWrong +"-----!!!!!!!!!count_wrong="+count_wrong);
            if(maxWrong>=0 && count_wrong>maxWrong){//错题太多，闯关失败
                System.out.println("错题太多，闯关失败-----  -1");
                return -1+"";
            }
            if(value>0 && count_right>=value){//过关
//				if(nowPG.getTestsNum()>0 && (count_right+count_wrong)>nowPG.getTestsNum()){
//					return -1+"";//闯关失败
//				}

                    return 0+"";
            }
            else{
                //判断题目数量
                if(nowPG.getQuesNums()>0 && (count_right+count_wrong)>=nowPG.getQuesNums()){
                    System.out.println("闯关失败----------==  -1");
                    return -1+"";//闯关失败
                }
                //判断答题时间。先以关口定义为准
                limitTime= nowPG.getLimitTime();
                if(limitTime>=0){
                    limitTime =limitTime*1000;
                    if(limitTime>0){
                        long beginTime = gr.getRushStartTime().getTime();
                        long nowTime = System.currentTimeMillis();
                        if((nowTime-beginTime)>limitTime){
                            System.out.println("闯关失败22222----------==  -12");
                            return -12+"";
                        }
                    }
                }
                return 1+"";
            }
        }

        else{
        System.out.println("闯关失败222234342322----------==  -1");
            return -1+"";
        }
    }
    /**
     * 正在闯关的信息。一个session只能同时闯一个关
     */
    private static Hashtable<String,RushSequenceRecord> HASH_GR = new Hashtable<String,RushSequenceRecord>();
    /**
     * 用户闯关。
     * @param sessionid  sessionid
     * @param gr  闯关信息
     * @return  0：增加成功。1：本session有其他闯关信息。2:参数错误。-1：未知错误
     * @throws Exception
     */
    public static synchronized int add(String sessionid,RushSequenceRecord gr){
        sessionid =	StringTool.trimAll(sessionid);
//		if(!IS_RUN){
//			try{
//				new RushGateUtil().new SubThread().start();
//			}
//			catch(Exception e){}
//		}
        try{
            if(sessionid.length()<=0 || gr==null){
                return 2;
            }
//			RushGateRecord gr1 = HASH_GR.get(sessionid);
//			if(gr1!=null){
//				return 1;
//			}
//			HASH_GR.put(sessionid, gr);
            IObjectCache cache = CacheFactory.getObjectCache();
            RushSequenceRecord gr1 =  (RushSequenceRecord) cache.get("RUSH"+sessionid);
            if(gr1!=null){
                return 1;
            }else{
                cache.set("RUSH"+sessionid,gr);
            }
            return 0;
        }
        catch(Exception e){}
        return -1;
    }
    /**
     * 获取用户闯关信息
     * @return
     */
    public static RushSequenceRecord get(String sessionid){
        sessionid =	StringTool.trimAll(sessionid);
//		return HASH_GR.get(sessionid);
        IObjectCache cache = CacheFactory.getObjectCache();
        return (RushSequenceRecord) cache.get("RUSH"+sessionid);
    }
    /**
     * 删除用户闯关信息
     * @param sessionid
     */
    public static void remove(String sessionid){
        sessionid =	StringTool.trimAll(sessionid);
//		HASH_GR.remove(sessionid);
        IObjectCache cache = CacheFactory.getObjectCache();
        cache.del("RUSH"+sessionid);
    }

    private static boolean IS_RUN = false;

    private class SubThread extends Thread{
        @Override
        public void run() {
            IS_RUN = true;
            while(true){
                try{
                    Enumeration<String> keys = HASH_GR.keys();
                    while(keys.hasMoreElements()){
                        String key = keys.nextElement();
                        try{

//							if(OnlineUserInfo.getOnlineInfoBySessionId(key)==null){
                            RushSequenceRecord gr = HASH_GR.get(key);
//								remove(key);//删除闯关信息
                            if(gr!=null){
                                //删除出题信息
//									QuestionsUtil.remove(key, QuestionsUtil.TYPE_GATERECORD, gr.getId());
                                //删除答题信息
//									AnswerInfoUtil.remove(gr.getId(), AnswerInfo.T_GATE);
//								}
                            }
                        }
                        catch(Exception e){
                        }
                    }
                }
                catch(Exception e){
                }
                finally{
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
