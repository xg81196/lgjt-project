package lgjt.domain.rush.ques;

import lombok.Data;

import java.util.List;

/**
 * <p>Title : TODO       </p>
 * <p>Description : TODO </p>
 * <p>Company : 北京天拓               </p>
 * @author dai.jiaqi
 */
@Data
public class QuestionAnswerJson {
	private String id;
	private String title;
	private String type;
	private List<String> opt ;
	private List<String> answer ;
	
	public void  answerRemove(){
		if(answer!=null){
			answer.clear();
		}
	}

}
