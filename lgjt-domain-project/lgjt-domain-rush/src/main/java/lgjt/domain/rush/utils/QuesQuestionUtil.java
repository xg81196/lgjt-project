package lgjt.domain.rush.utils;

import com.alibaba.fastjson.JSONObject;
import com.ttsx.platform.tool.util.StringTool;
import org.apache.commons.lang3.StringUtils;
import lgjt.common.base.utils.NumberUtil;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.ques.QuestionAnswerJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Title : TODO       </p>
 * <p>Description : TODO </p>
 * <p>Company : 北京天拓               </p>
 * @date 2016-4-7 上午11:33:36
 * @author dai.jiaqi
 */
public class QuesQuestionUtil {

		/**
		 * 对试题正文进行加密
		 * @param content  试题正文
		 * @return   加密后内容
		 */
		public static String encrypt(String content){
			try {
//				content = StringTool.trimAll(content);
				if(content.length()<=0)
					return "";
				StringBuffer sb = new StringBuffer();
				int ff = 0x29;
				byte p = (byte)ff;
				byte[] bs = content.getBytes("utf-8");
				for(int i=bs.length-1;i>=0;i--){			
					byte b = (byte)(bs[i] ^ p); 
					sb.append(NumberUtil.byte2hex(b));
				}
				return sb.toString();
			} catch (Exception e) {
			}
			return content;
		}
		/**
		 * 把试题正文解密
		 * @param content  加密的正文
		 * @return  解密的正文
		 */
		public static String decrypt(String content){
			content = StringTool.trimAll(content);
			if(content.length()<=0)return "";
			int ff = 0x29;
			byte p = (byte)ff;
			if(content.length()%2!=0){
				return content;//有问题，不解密，直接返回内容
			}
			int length = content.length();
			byte[] bs = new byte[length/2];
			for(int i=0;i<length;i=i+2){
				String s = content.substring(i,i+2);
				try{
				    bs[length/2-1-i/2]=(byte)(Integer.parseInt(s, 16) ^ p);
				}
				catch(Exception e){
					return content;
				}			
			}
			try {
				return new String(bs,"utf-8");
			} catch (Exception e) {
				return content;
			}
		}
		
		/**
		 * 把试题正文解密
		 * @param content  加密的正文
		 * @return  解密的正文
		 */
		public static String decryptGBK(String content){
			content = StringTool.trimAll(content);
			if(content.length()<=0)return "";
			int ff = 0x29;
			byte p = (byte)ff;
			if(content.length()%2!=0){
				return content;//有问题，不解密，直接返回内容
			}
			int length = content.length();
			byte[] bs = new byte[length/2];
			for(int i=0;i<length;i=i+2){
				String s = content.substring(i,i+2);
				try{
				    bs[length/2-1-i/2]=(byte)(Integer.parseInt(s, 16) ^ p);
				}
				catch(Exception e){
					return content;
				}			
			}
			try {
				return new String(bs,"gbk");
			} catch (Exception e) {
				return content;
			}
		}

	/**
	 * 判断考试
	 * 注意多选题要用;;;分隔
	 * @param quesQuestions 考生试题
	 * @return true 正确 false 错误
	 * @throws Exception
	 */
	public static boolean rushQuestionCheck(QuesQuestions quesQuestions, String value)throws Exception {
		QuestionAnswerJson qaj=JSONObject.parseObject(quesQuestions.getAnswer(), QuestionAnswerJson.class);
		List<String> answer=qaj.getAnswer();
		List<String> userAnswers=new ArrayList<String>();
		String userAnswer = value;
		if(userAnswer==null || userAnswer.trim().length()==0){
			return false;
		}
		String[] userAnswerArray=value.replaceAll("\\[\"", "").replaceAll("\"\\]", "").split("\",\"");;
		if(quesQuestions.getType()==1){//单选
			for(int i=0;i<userAnswerArray.length;i++){
				userAnswers.add(userAnswerArray[i]);
			}
		}
		if(quesQuestions.getType()==2){//多选
			userAnswerArray= userAnswer.replaceAll("\\[\"", "").replaceAll("\"\\]", "").split("\",\"");//后台
			for(int i=0;i<userAnswerArray.length;i++){
				userAnswers.add(userAnswerArray[i]);
			}
//				userAnswerArray= value.replaceAll("\\[\"", "").replaceAll("\"\\]", "").split("\",\"");//后台
		}
//			else
//			{
//				userAnswerArray=userAnswer.split(";;;");
//			}
		if(quesQuestions.getType()==3){//判断
			for(int i=0;i<userAnswerArray.length;i++){
//					System.out.println(userAnswerArray[i]);
				if(userAnswerArray[i].equals("正确")){
					userAnswers.add("对");
				}else if(userAnswerArray[i].equals("对")){
					userAnswers.add("对");
				}else if(userAnswerArray[i].equals("错误")){
					userAnswers.add("错");
				}else if(userAnswerArray[i].equals("错")){
					userAnswers.add("错");
				}else {
					userAnswers.add(userAnswerArray[i]);
				}
			}
		}
//			else{
//				for(int i=0;i<userAnswerArray.length;i++){
//					userAnswers.add(userAnswerArray[i]);
//				}
//			}
		if(quesQuestions.getType()==4){//填空
			userAnswerArray= userAnswer.replaceAll("\\[\"", "").replaceAll("\"\\]", "").split("\",\"");//后台
			for(int i=0;i<userAnswerArray.length;i++){
				userAnswers.add(userAnswerArray[i]);
			}
		}
//			else{
//			boolean b1=answer.containsAll(userAnswers);
//			boolean b2=userAnswers.containsAll(answer);
//
		return ((answer.containsAll(userAnswers))&&(userAnswers.containsAll(answer)));
//			}
	}






	public static void main(String[] args) {
//			String content ="!@#$%^^&**(_++dsada2131﹢﹣·/=﹥≦≧≯≡∅≠≈≥≤＝×－＋﹢﹣∶∴∝∽⊙∣∆∭¹≌￠￥mol％¹∠≌";
//			System.out.println(content);
//			System.out.println(encrypt(content));
//			System.out.println(decrypt(encrypt(content)));
	//==========2		
//			String answer="{"+
//        "\"type\": \"4\","+
//        "\"opt\": ["+
//        " \"人站在木桌上同时触及相线和中性线\","+
//        " \"人站在地上触及一根带电导线\","+
//        "  \"人站在地上触及漏电设备的金属外壳\","+
//        "  \"人坐在接地的金属台上触及一根带电导线\""+
//        "  ],"+
//        "  \"answer\": ["+
//        "   \"人站在木桌上同时触及相线和中性线\","+
//        "   \"人站在地上触及一根带电导线\""+
//        "  ]"+
//        "}";
////			for(int i=0;i<5;i++){
////				System.out.println("------------------");
////				QuestionAnswerJson qaj=JSONObject.parseObject(answer, QuestionAnswerJson.class);
////				System.out.println(JSONObject.toJSON(qaj));
////				Collections.shuffle(qaj.getOpt());
////				System.out.println(JSONObject.toJSON(qaj));
////			}
//			ExamQuestion examQuestion=new ExamQuestion();
//			examQuestion.setAnswer(answer);
//			ExamQuestion examQuestion1=new ExamQuestion();
//			examQuestion1.setAnswer(answer);
//			
//			List<ExamQuestion> list =new ArrayList<ExamQuestion>();
//			list.add(examQuestion);
//			list.add(examQuestion1);
//			try {
//				questionAnswerConfuse(list);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			for(int i=0;i<list.size();i++){
//				System.out.println("------------------");
////				QuestionAnswerJson qaj=JSONObject.parseObject(answer, QuestionAnswerJson.class);
//				System.out.println(JSONObject.toJSON(list.get(i).getAnswer()));
////				Collections.shuffle(list.get(i).getAnswer());
////				System.out.println(JSONObject.toJSON(list.get(i).getAnswer()));
//			}
			
			//==========2	
			
//			ArrayList<String> all=new ArrayList<String> ();
//			all.add("中国");
//			all.add("美国");
//			all.add("日本");
//			
//			ArrayList<String> all1=new ArrayList<String> ();
//			all1.add("中国");
//			System.out.println(all1.containsAll(all) && all.containsAll(all1));
//			all1.add("美国");
//			System.out.println(all1.containsAll(all) && all.containsAll(all1));
//			all1.add("日本");
//			System.out.println(all1.containsAll(all) && all.containsAll(all1));
//			all1.add("日本1");
//			System.out.println(all1.containsAll(all) && all.containsAll(all1));
//		String tmp="074D455B465E4C415D5A5A465B4A484D4C5340474E464A4C5B5A405D474C445946454C5F4C4D7676767676767676764E47405A484C5B4A47405A405A4C405D404A4F465B4C4B445C474C415D05484740414A4760";
//		System.out.println(decrypt(tmp));
//		
		
		String tmp=" ";
		String tmp1=encrypt(tmp);
		String tmp2=decrypt(tmp1);
//		System.out.println("加密："+tmp1);
//		System.out.println("解密："+tmp2);
		
		}
}
