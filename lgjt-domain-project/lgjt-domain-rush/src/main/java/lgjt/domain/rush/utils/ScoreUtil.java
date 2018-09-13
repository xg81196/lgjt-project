package lgjt.domain.rush.utils;



import lgjt.domain.rush.AnswerInfo;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.domain.rush.sequence_record.RushSequenceRecord;

import java.util.List;


/**
 * 积分相关工具类
 */
public class ScoreUtil {
	/**
	 * 计算积分
	 * @param gr   当前闯关信息
	 * @param nowPG  当前关口
	 * @param isRepeat  是否是重复闯关
	 * @param  success   是否闯关成功。
	 * @return	当前关口所获积分
	 * @throws Exception
	 */
	public static double computeScore(RushSequenceRecord gr, RushSequence nowPG,boolean isRepeat, boolean success) throws Exception{
		int sameScore = nowPG.getScoreFlag() == null?0:nowPG.getScoreFlag() ;//统一分值
		if(sameScore == RushSequence.SCOREFLAG_YES ){//统一分值
			double score = nowPG.getScoreWeight() == null ? 0:nowPG.getScoreWeight();//统一分值
			/*if(isRepeat){
				score *= nowPG.getGateRepeatMarkWeight();
			}*/
			if(!success){
				score *= nowPG.getPassFailureMarkWeight();
			}
			double gateWrong = 0;//答错积分权重
			if(nowPG.getAnswerWrongFlag() == RushSequence.ANSWERWRONGFLAG_YES){
				gateWrong = nowPG.getAnswerWrongMark() * -1;
			}
			
			double result = 0;// 统一分值，只有答对算分。
			List<AnswerInfo> list_right = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, true);
				if(list_right!=null && list_right.size()>0){
					result += list_right.size() * score;
				}
				
				List<AnswerInfo> list_wrong = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, false);
				if(list_wrong!=null && list_wrong.size()>0){
					result += list_wrong.size() * score * gateWrong;
				}
			return result;
		}
		else{//不统一分值
			float result = 0;//返回结果
			double gateWeight = getWeight(nowPG,success);//关口权重
			double gateWrong = 0;//答错积分权重
			List<AnswerInfo> list_right = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, true);
			if(nowPG.getAnswerWrongFlag() == RushSequence.ANSWERWRONGFLAG_YES){
				gateWrong = nowPG.getAnswerWrongMark() * -1;
			}
			if(list_right!=null && list_right.size()>0){
				
				for(int i=0;i<list_right.size();i++){
					AnswerInfo ai = list_right.get(i);
					double score = ai.getQScore();
					if(score==-1){
						score = ai.getDScore();
					}
					result += score * ai.getQcWeight() * gateWeight;
					
				}
			}
			List<AnswerInfo> list_wrong = AnswerInfoUtil.getAnswerInfo(gr.getId(), AnswerInfo.T_GATE, false);
			if(list_wrong!=null && list_wrong.size()>0){
				for(int i=0;i<list_wrong.size();i++){
					AnswerInfo ai = list_wrong.get(i);
					double score = ai.getQScore();
					if(score==-1){
						score = ai.getDScore();
					}
					result += score * ai.getQcWeight() * gateWeight  * gateWrong;					
				}
			}	
			return result;		
		}
	}



	/**
	 * 获取关口积分权重。非统一分值
	 * @param pg   关口信息
	 * @param  success   是否闯关成功。
	 * @return
	 */
	public static double getWeight(RushSequence pg,boolean success){
		double gateWeight = pg.getScore();
		if(pg.getLimitTime()!=null && pg.getLimitTime()>0){
			gateWeight += 0;//限时权重
		}
		/*if(isRepeat){
			gateWeight *= pg.getGateRepeatMarkWeight();//重复闯关权重
		}*/
		if(!success){
			gateWeight *= pg.getPassFailureMarkWeight();//闯关失败权重
		}
		
		return gateWeight;
	}

	
}
