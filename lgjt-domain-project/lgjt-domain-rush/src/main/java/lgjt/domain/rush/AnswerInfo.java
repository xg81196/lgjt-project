package lgjt.domain.rush;

import java.io.Serializable;

/**
 * 答题信息
 *
 */
public class AnswerInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String qId;
	private double qScore;
	private String qDiff;
	private double dScore;
	private String qClass;
	private double qcWeight;
	private boolean right;
	private String relationId;
	private int type;
	private String faultQuestion; 	
	/**
	 * 闯关
	 */
	public static final int T_GATE = 1;
	/**
	 * 构造一个答题信息
	 * @param qId   试题Id
	 * @param qScore   试题分数
	 * @param qDiff   试题难度
	 * @param dScore   难度分数
	 * @param qClass  所属题库
	 * @param qcWeight  题库权重
	 * @param right  是否答对试题
	 * @param relationId  关联ID
	 * @param type  关联ID类型
	 */
	public AnswerInfo(String qId, float qScore, String qDiff, float dScore, String qClass, float qcWeight, boolean right, String relationId, int type){
		this.qId = qId;
		this.qScore = qScore;
		this.qDiff = qDiff;
		this.dScore = dScore;
		this.qClass = qClass;
		this.qcWeight = qcWeight;
		this.right = right;
		this.relationId = relationId;
		this.type = type;		
	}
	public String getFaultQuestion() {
	    return faultQuestion;
	}
	public void setFaultQuestion(String faultQuestion) {
	    this.faultQuestion = faultQuestion;
	}
	/**
	 * 试题分数
	 * @return
	 */
	public double getQScore() {
		return qScore;
	}
	/**
	 * 试题分数
	 * @param score
	 */
	public void setQScore(double score) {
		qScore = score;
	}
	/**
	 * 试题ID
	 * @return
	 */
	public String getQId() {
		return qId;
	}
	/**
	 * 试题ID
	 * @param id
	 */
	public void setQId(String id) {
		qId = id;
	}
	/**
	 * 试题难度
	 * @return
	 */
	public String getQDiff() {
		return qDiff;
	}
	/**
	 * 试题难度
	 * @param qdiff
	 */
	public void setQDiff(String qDiff) {
		this.qDiff = qDiff;
	}
	/**
	 * 难度分值
	 * @return
	 */
	public double getDScore() {
		return dScore;
	}
	/**
	 * 难度分值
	 * @param score
	 */
	public void setDScore(double score) {
		dScore = score;
	}
	/**
	 * 所属题库
	 * @return
	 */
	public String getQClass() {
		return qClass;
	}
	/**
	 * 所属题库
	 * @param class1
	 */
	public void setQClass(String class1) {
		qClass = class1;
	}
	/**
	 * 题库积分权重
	 * @return
	 */
	public double getQcWeight() {
		return qcWeight;
	}
	/**
	 * 题库积分权重
	 * @param qcWeight
	 */
	public void setQcWeight(double qcWeight) {
		this.qcWeight = qcWeight;
	}
	/**
	 * 是否回答正确
	 * @return
	 */
	public boolean isRight() {
		return right;
	}
	/**
	 * 是否回答正确
	 * @param right
	 */
	public void setRight(boolean right) {
		this.right = right;
	}
	/**
	 * 关联Id
	 * @return
	 */
	public String getRelationId() {
		return relationId;
	}
	/**
	 * 关联Id
	 * @param relationId
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	/**
	 * 关联Id类型
	 * @return
	 */
	public int getType() {
		return type;
	}
	/**
	 * 关联Id类型
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
}
