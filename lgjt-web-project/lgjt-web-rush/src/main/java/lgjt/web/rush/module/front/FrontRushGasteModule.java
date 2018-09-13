package lgjt.web.rush.module.front;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.IStringCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.vo.Total;
import lgjt.domain.rush.level_score.InvolvementRank;
import lgjt.services.rush.involvement.InvolvementRankService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.ReturnInfo;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.NumberFormatUtil;
import lgjt.domain.rush.level_score.RushLevelRank;
import lgjt.domain.rush.level_score.RushLevelScore;
import lgjt.domain.rush.sequence_record.RushUserRecord;
import lgjt.domain.rush.utils.QuesQuestionUtil;
import lgjt.common.base.utils.StaticUtils;
import lgjt.common.base.utils.TokenUtils;
import lgjt.domain.rush.AnswerInfo;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.ques.QuestionAnswerJson;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.domain.rush.sequence_classify.RushSequenceClassify;
import lgjt.domain.rush.sequence_record.RushSequenceRecord;
import lgjt.domain.rush.utils.AnswerInfoUtil;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.domain.rush.utils.RushGateUtil;
import lgjt.domain.rush.utils.ScoreUtil;
import lgjt.services.rush.level_score.RushLevelScoreService;
import lgjt.services.rush.ques.QuesClassifyService;
import lgjt.services.rush.ques.QuesDiffService;
import lgjt.services.rush.ques.QuesQuestionsService;
import lgjt.services.rush.sequence.RushSequenceService;
import lgjt.services.rush.sequence_classify.RushSequenceClassifyService;
import lgjt.services.rush.sequence_record.RushSequenceRecordService;
import lgjt.services.rush.sequence_record.RushUserRecordService;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuguangwei
 * @date 2018/7/24
 * @Description:前台闯关
 */
@At("/front/frontRushGaste")
@IocBean
@Log4j
public class FrontRushGasteModule {
    /**
     * 闯关记录表
     */
    @Inject
    RushSequenceRecordService rushSequenceRecordService;
    /**
     * 用户积分表
     */
    @Inject
    RushLevelScoreService rushLevelScoreService;
    /**
     * 闯关序列
     */
    @Inject
    RushSequenceService rushSequenceService;
    /**
     * 试题
     */
    @Inject
    QuesQuestionsService quesQuestionsService;

    @Inject
    RushSequenceClassifyService rushSequenceClassifyService;

    @Inject
    QuesDiffService quesDiffService;

    @Inject
    QuesClassifyService quesClassifyService;

    @Inject
    RushUserRecordService rushUserRecordService;

    @Inject("rushSequenceService")
    RushSequenceService service;

    @Inject
    InvolvementRankService involvementRankService;

    /**
     * 查询首页用户个人信息
     *
     * @return
     */
    @At("/queryUserInfo")
    public Object queryUserInfo() {

        String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);

        ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
        UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);
        Integer score = rushLevelScoreService.sumScore(userLoginInfo.getUserName());
        Map<String, String> result = Maps.newHashMap();
        result.put("userName", userLoginInfo.getUserName());
        result.put("score", score == null ? "0" : score + "");
        //result.put("headImage",userLoginInfo.getInfos().get("headImage"));

        return Results.parse(Constants.STATE_SUCCESS, null, result);
    }

    /**
     * 查询我的闯关记录
     *
     * @param obj
     * @return
     */
    @At("/queryMyRushRecord")
    public Object queryMyRushRecord(@Param("..") RushSequenceRecord obj) {
        obj.setUserName(LoginUtil.getUserLoginInfo().getUserName());
        return Results.parse(Constants.STATE_SUCCESS, null, rushSequenceRecordService.queryPage(obj));
    }


    /**
     * 查询对应工种
     *
     * @param obj
     * @return
     */

    @At("/querySequence")
    public Object queryPage(@Param("..") RushSequence obj) {
        /**
         * 查询已发布的工种
         */
        if (LoginUtil.getUserLoginInfo() == null) {
            return Results.parse(ReturnCode.CODE_2.getCode(), null, null);
        }

        obj.setExtend1("1");
        return Results.parse(Constants.STATE_SUCCESS, null, service.queryPage(obj));
    }


    /**
     * 查询我的最近闯关
     *
     * @param obj
     * @return
     */
    @At("/queryMyLastRush")
    public Object queryMyLastRush(@Param("..") RushSequenceRecord obj) {

        obj.setUserName(LoginUtil.getUserLoginInfo().getUserName());
        List<RushSequenceRecord> rushSequenceRecordList = rushSequenceRecordService.query(obj);

        try {
            if (CollectionUtils.isNotEmpty(rushSequenceRecordList)) {
                RushSequence rushSequence = rushSequenceService.checkId(rushSequenceRecordList.get(0).getSequenceId());
                if (StringUtils.isNotBlank(rushSequence.getImageId())) {
                    JSONObject jsonObject = JSON.parseObject(rushSequence.getImageId());
                    JSONObject object = (JSONObject) jsonObject.getJSONArray("data").get(0);
                    object.replace("id", ResourceEncryptionUtil.base64Encoder((String) object.get("id")));
                    rushSequence.setImageId(jsonObject.toJSONString());
                }
                return Results.parse(Constants.STATE_SUCCESS, null, rushSequence);
            }
        } catch (Exception e) {
            log.error(e);
        }

        return Results.parse(Constants.STATE_SUCCESS, "暂无最近闯关!");
    }


    /**
     * 前台个人中心__查看闯关信息__查看错题信息
     *
     * @param rushRecordId
     * @return Object
     */
    @At("/getMyRushRecordError")
    public Object get(@Param("rushRecordId") String rushRecordId, @Param("isError") String isError) {

        RushSequenceRecord obj = rushSequenceRecordService.get(rushRecordId);

        List<QuesQuestions> qqList = new ArrayList<QuesQuestions>();//题目list

        /**
         * 取出对应的试题信息
         */
		/*List<QuesQuestions> quesQuestionsList = AnswerInfoUtil.getRushQuestion(obj.getSequenceId(),LoginUtil.getUserLoginInfo().getUserName());


		if(null != obj) {

			for (QuesQuestions quesQuestion : quesQuestionsList) {
				quesQuestion.setQuestion(QuesQuestionUtil.decrypt(quesQuestion.getQuestion()));
			}*/

        /**
         * 答对题目ID
         */

        if (!"0".equals(isError)) {
            if (StringUtils.isNotBlank(obj.getRightQuestionId())) {
                String[] rightQuess = obj.getRightQuestionId().split(",");
                for (String rightId : rightQuess) {

                    SimpleCriteria cri = Cnd.cri();
                    cri.where().andEquals("id", rightId);
                    QuesQuestions qq = this.quesQuestionsService.fetch(QuesQuestions.class, cri);
                    if (qq.getQuestion() != null) {
                        qq.setQuestion(QuesQuestionUtil.decrypt(qq.getQuestion()));
                    }
                    qqList.add(qq);
                    /*for (QuesQuestions quesQuestion : quesQuestionsList) {*/
							/*if (rightId.equals(quesQuestion.getId())) {

								quesQuestion.setAnswerResult(1);
								qqList.add(quesQuestion);
							}*/


                }
                /*}*/


            }
        }


        /**
         * 错题
         */

        if ((obj.getFaultQuestionId() != null) && (obj.getFaultQuestionIds() != null)) {
            String s1 = obj.getFaultQuestionId();
            String s2 = obj.getFaultQuestionIds();
            String str1[] = s1.split(",");
            String str2[] = s2.split("---");

            for (int i = 0; i < str1.length; i++) {
                SimpleCriteria cri = Cnd.cri();
                cri.where().andEquals("id", str1[i]);
                QuesQuestions qq = this.quesQuestionsService.fetch(QuesQuestions.class, cri);
                /*for (QuesQuestions qq : quesQuestionsList) {*/

                if (qq != null) {
                    if (str1[i].equals(qq.getId())) {

                        if (qq.getQuestion() != null) {
                            qq.setQuestion(QuesQuestionUtil.decrypt(qq.getQuestion()));
                        }
                        qq.setAnswerResult(2);
                        qq.setErrorAnswer(str2[i]);
                        qqList.add(qq);
                        //break;
                    }
                }



                /*}*/

            }
            /*}*/

        }
        return Results.parse(Constants.STATE_SUCCESS, null, qqList);
    }


    /**
     * 查询本关回顾
     *
     * @param obj
     * @return
     */
    @At("/queryMyRushLookBack")
    public Object queryMyRushLookBack(@Param("..") RushSequenceRecord obj) {

        /**
         * 取出对应的试题信息
         */
        List<QuesQuestions> quesQuestionsList = AnswerInfoUtil.getRushQuestion(obj.getSequenceId(), LoginUtil.getUserLoginInfo().getUserName());

        obj.setUserName(LoginUtil.getUserLoginInfo().getUserName());
        List<RushSequenceRecord> rushSequenceRecordList = rushSequenceRecordService.query(obj);

        if (CollectionUtils.isNotEmpty(rushSequenceRecordList)) {
            RushSequenceRecord rushSequenceRecord = rushSequenceRecordList.get(0);
            /**
             * 答对题目ID
             */
            if (StringUtils.isNotBlank(rushSequenceRecord.getRightQuestionId())) {
                String[] rightQuess = rushSequenceRecord.getRightQuestionId().split(",");
                for (String rightId : rightQuess) {
                    for (QuesQuestions quesQuestion : quesQuestionsList) {
                        if (rightId.equals(quesQuestion.getId())) {
                            quesQuestion.setQuestion(QuesQuestionUtil.decrypt(quesQuestion.getQuestion()));
							/*QuestionAnswerJson qaj = JSONObject.parseObject(quesQuestion.getAnswer(),
									QuestionAnswerJson.class);*/
                        }
                    }
                }


                //List<QuesQuestions> quesQuestionsList = quesQuestionsService.queryQuesQuestions(rightQuess);

            }

            if (StringUtils.isNotBlank(rushSequenceRecord.getFaultQuestionId())) {
                String[] wrongQuess = rushSequenceRecord.getFaultQuestionId().split(",");
                for (String rightId : wrongQuess) {
                    for (QuesQuestions quesQuestion : quesQuestionsList) {
                        if (rightId.equals(quesQuestion.getId())) {
                            quesQuestion.setQuestion(QuesQuestionUtil.decrypt(quesQuestion.getQuestion()));
							/*QuestionAnswerJson qaj = JSONObject.parseObject(quesQuestion.getAnswer(),
									QuestionAnswerJson.class);*/
                        }
                    }
                }
                //List<QuesQuestions> quesQuestionsList = quesQuestionsService.queryQuesQuestions(wrongQuess);

            }

        }
        return Results.parse(Constants.STATE_SUCCESS, null, quesQuestionsList);
    }


    /**
     * 查询全部排行,包括自己的排行
     *
     * @param obj
     * @return
     * @author
     * @date
     */
    @At("/queryRanking")
    public Object queryRanking(@Param("..") RushLevelScore obj) {

        try {
            if (obj.getPage() == null) {
                obj.setPage(0);
            } else {
                obj.setPage(obj.getPage() - 1);
            }
            if (obj.getPageSize() == null) {
                obj.setPageSize(10);
            }
            if (LoginUtil.getUserLoginInfo() == null) {
                return Results.parse(Constants.STATE_FAIL, "用户未登录");
            }

            String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
            ILoginInfoCache loginInfoCache = CacheFactory.getLoginInfoCache();
            UserLoginInfo userLoginInfo = loginInfoCache.getLoginInfo(PropertyUtil.getProperty("redis-prefix-login") + token);

            obj.setUserName(userLoginInfo.getUserName());
            // 查询全部排名信息
            List<RushLevelRank> allUser = rushLevelScoreService.queryRushAllRank(obj);
            // 查询自己的排名信息
            Map<String, Object> map = new HashMap<String, Object>();
            //List<RushLevelRank> myUser = rushLevelScoreService.queryRushMyRank(obj.getUserName(),obj.getSequenceName());

            RushLevelRank myRanking = null;
            for (RushLevelRank rushLevelRank : allUser) {
                if (userLoginInfo.getUserName().equals(rushLevelRank.getUserName())) {
                    myRanking = rushLevelRank;
                    break;
                }
            }

            List<Total> totals = rushLevelScoreService.queryRushAllRankTotal(obj);
            map.put("allRanking", allUser);
            map.put("myRanking", "");
            map.put("total", totals.get(0).getTotal());

            if (myRanking == null) {
                if (LoginUtil.getUserLoginInfo() == null) {
                    return Results.parse(Constants.STATE_FAIL, "用户未登录");
                }
                myRanking = new RushLevelRank();
                myRanking.setUserName(userLoginInfo.getUserName());
                myRanking.setRealName(userLoginInfo.getRealName());
                myRanking.setScore(0);
                //这个地方最好是前端给
                myRanking.setSequenceName(obj.getSequenceName());
            }
            map.put("myRanking", myRanking);
            return Results.parse(Constants.STATE_SUCCESS, "查询成功！", map);
        } catch (Exception e) {
            log.error("allRanking exception", e);
            return Results.parse(Constants.STATE_FAIL, "查询全部排行失败！");
        }

        //return Results.parse(Constants.STATE_SUCCESS,null,rushSequenceRecordService.queryPage(obj));
    }

    @At("/queryRankByCompany")
    public Object queryRankByCompany(@Param("..") RushLevelScore obj) {
        if (obj.getPage() == null) {
            obj.setPage(0);
        } else {
            obj.setPage(obj.getPage() - 1);
        }
        if (obj.getPageSize() == null) {
            obj.setPageSize(10);
        }
        if (LoginUtil.getUserLoginInfo() == null) {
            return Results.parse(Constants.STATE_FAIL, "用户未登录");
        }

        // 查询全部排名信息
        List<RushLevelRank> allCompany = rushLevelScoreService.queryRushByCompanyName(obj);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("allCompany", allCompany);
        List<Total> totals = rushLevelScoreService.queryRushByCompanyNameTotal(obj);
        map.put("total", totals.get(0).getTotal());

        return Results.parse(Constants.STATE_SUCCESS, "查询成功！", map);
    }

    @At("/queryCompanyInvolvementRank")
    public Object queryCompanyInvolvementRank(@Param("..") InvolvementRank obj) {
        if (obj.getPage() == null) {
            obj.setPage(0);
        } else {
            obj.setPage(obj.getPage() - 1);
        }
        if (obj.getPageSize() == null) {
            obj.setPageSize(10);
        }
        if (LoginUtil.getUserLoginInfo() == null) {
            return Results.parse(Constants.STATE_FAIL, "用户未登录");
        }
        return Results.parse(Constants.STATE_SUCCESS, "查询成功！", involvementRankService.queryPage(obj));
    }


    /**
     * 开始闯关
     *
     * @param sequenceId 工种ID
     * @return Object JSON
     */
    @At("/startRushGate")
    public Object startRushGate(@Param("sequenceId") String sequenceId, @Param("..") RushSequence rs)
            throws Exception {


        if (LoginUtil.getUserLoginInfo() == null) {
            return Results.parse(Constants.STATE_FAIL, "获取用户信息失败！请刷新页面重新闯关！");
        }

        String sessionId = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);

        RushSequence rushSequence = rushSequenceService.checkId(sequenceId);

        if (rushSequence == null) {
            return Results.parse(Constants.STATE_FAIL, "工种不存在！");
        }


        // 看当前用户是否在闯关
        RushSequenceRecord rushGateRecord = RushGateUtil.get(sessionId);

        RushLevelScore userlevel = rushLevelScoreService
                .getByUserNameAndSequenceId(LoginUtil.getUserLoginInfo().getUserName(), sequenceId);

        if (userlevel == null) {
            userlevel = new RushLevelScore();
            userlevel.setSequenceId(sequenceId);
            userlevel.setSequenceName(rushSequence.getName());
            userlevel.setScore(0d);
            userlevel.setUserName(LoginUtil.getUserLoginInfo().getUserName());
            userlevel.setRealName(LoginUtil.getUserLoginInfo().getRealName());
            String companyName = LoginUtil.getUserLoginInfo().getInfos().get("orgName");
            if (StringUtils.isNotBlank(companyName)) {
                userlevel.setCompanyName(companyName);
            }
            String companyId = LoginUtil.getUserLoginInfo().getInfos().get("orgId");
            if (StringUtils.isNotBlank(companyId)) {
                userlevel.setCompanyId(companyId);
            }

            userlevel.setMark("");
            userlevel.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
            userlevel.setCrtTime(new Date());
            userlevel.setCrtIp(ClientInfo.getIp());
            rushLevelScoreService.insert(userlevel);
        }

        boolean isRepeat = false;
        RushUserRecord rushGateuserRecord = rushUserRecordService
                .getRushGateuserRecords(LoginUtil.getUserLoginInfo().getUserName(), sequenceId,
                        RushUserRecord.STATE_FINISH);
        //TokenUtils tokenUtil=new TokenUtils();
        if (rushGateuserRecord != null) {
            isRepeat = true;
        }

        if (rushGateRecord == null) {
            //公司ID 公司名称
            //剩余题数
            rushGateRecord = new RushSequenceRecord();
            rushGateRecord.setUserName(LoginUtil.getUserLoginInfo().getUserName());
            rushGateRecord.setSequenceId(sequenceId);
            rushGateRecord.setSequenceName(rushSequence.getName());
            rushGateRecord.setState(RushSequenceRecord.STATE_NO_FINISH);
            rushGateRecord.setRushStartTime(new Timestamp(System
                    .currentTimeMillis()));
            rushGateRecord.setRushCount(1);
            rushGateRecord.setFaultQuantity(0);
            rushGateRecord.setRightQuantity(0);
            rushGateRecord.setScore(0d);
            rushGateRecord.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
            rushGateRecord.setCrtTime(new Date());
            rushGateRecord.setCrtIp(ClientInfo.getIp());
            if (!"null".equals(LoginUtil.getUserLoginInfo().getInfos().get("orgId"))) {
                rushGateRecord.setCompanyId(LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
            }

            if (!"null".equals(LoginUtil.getUserLoginInfo().getInfos().get("orgName"))) {
                rushGateRecord.setCompanyName(LoginUtil.getUserLoginInfo().getInfos().get("orgName"));
            }
            rushGateRecord = rushSequenceRecordService.insert(rushGateRecord);
            RushGateUtil.add(sessionId, rushGateRecord);// 保存到内存中
        } else {
            // 如果当前关口跟 session中记录的关口不一致，则将之前的设置为失败
            if (!rushGateRecord.getSequenceId().equals(sequenceId)) {
                checkPassGateSuccess(rushSequence, rushSequence, rushGateRecord, userlevel, isRepeat, sessionId, false);
//				HttpSession session = Mvcs.getReq().getSession();
                TokenUtils tokenUtil = new TokenUtils();
                tokenUtil.removeAttribute("question" + sessionId);
            }
        }
        return nextQuestion(null, null, rushGateRecord.getId(), rs);
    }

    /**
     * 获取闯关试题
     *
     * @param value
     * @param questionId
     * @param rushRecordId
     * @return
     */
    @At("/nextQuestion")
    public Object nextQuestion(@Param("value") String value, @Param("questionId") String questionId,
                               @Param("rushRecordId") String rushRecordId, @Param("..") RushSequence rs) {

        try {

            TokenUtils tokenUtil = new TokenUtils();

            String sessionId = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);

            RushSequenceRecord gateRecord = RushGateUtil.get(sessionId);

            RushSequence rushGate = null;

            if (gateRecord != null) {
                if (StringUtils.isNotBlank(gateRecord.getSequenceId())) {
                    rushGate = rushSequenceService.get(gateRecord.getSequenceId());
                }
            }


            // 装题
            if (tokenUtil.getAttribute("questions" + sessionId) == null || ((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId)).size() == 0) {
                // 确认 题库的关联方式
                RushSequenceClassify rgcQuery = new RushSequenceClassify();
                rgcQuery.setSequenceId(rushGate.getId());
                List<RushSequenceClassify> rushGateClassifyList = rushSequenceClassifyService
                        .query(rgcQuery);

                List<QuesQuestions> quesQuestions = null;
                if (rushGateClassifyList == null
                        || rushGateClassifyList.size() == 0) {
                    return Results.parse(Constants.STATE_FAIL, "该工种未设置题库");
                } else {
                    QuesQuestions qq = new QuesQuestions();
                    qq.setDifficultyId(rushGateClassifyList.get(0).getDifficulty());
                    qq.setTypeQuery(rushGateClassifyList.get(0).getQuestionType());
                    qq.setClassifyId(rushGateClassifyList.get(0).getClassifyId());
                    qq.setPage(1);
                    qq.setPageSize(rushGate.getQuesNums());
                    quesQuestions = quesQuestionsService.queryPageForRush(qq);
                }
                if (quesQuestions == null || quesQuestions.size() == 0) {
                    return Results.parse(Constants.STATE_FAIL, "该工种未设置题库");
                }

                tokenUtil.setAttribute("questions" + sessionId, quesQuestions);

            } else if (((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId))
                    .size() < 2) {
                RushSequenceClassify rgcQuery = new RushSequenceClassify();
                rgcQuery.setSequenceId(rushGate.getId());
                List<RushSequenceClassify> rushGateClassifyList = rushSequenceClassifyService
                        .query(rgcQuery);
                List<QuesQuestions> quesQuestions = null;

                if (rushGateClassifyList == null
                        || rushGateClassifyList.size() == 0) {
                    return Results.parse(Constants.STATE_FAIL, "该工种未设置题库");
                } else {
                    QuesQuestions qq = new QuesQuestions();
                    qq.setDifficultyId(rushGateClassifyList.get(0).getDifficulty());
                    qq.setTypeQuery(rushGateClassifyList.get(0).getQuestionType());
                    qq.setClassifyId(rushGateClassifyList.get(0).getClassifyId());
                    qq.setPage(1);
                    qq.setPageSize(10);
                    quesQuestions = quesQuestionsService.queryPageForRush(qq);
                }
                if (quesQuestions == null || quesQuestions.size() == 0) {
                    return Results.parse(Constants.STATE_FAIL, "该工种未设置题库");
                }

                ((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId))
                        .addAll(quesQuestions);
            }

            List<QuesQuestions> quesQuestionsList = ((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId));

            //List<String> quesIds = quesQuestionsList.stream().map(QuesQuestions::getId).collect(Collectors.toList());
            /**
             * 存入试题id
             */
            AnswerInfoUtil.addRushQuestion(gateRecord.getSequenceId(), LoginUtil.getUserLoginInfo().getUserName(), quesQuestionsList);

            QuesQuestions currentQues = quesQuestionsList.get(0);


            /**
             * 校验答案
             */
            long isOvertime = 10l;
            /*	if (rs.getCrtTime() != null)  {
             *//**
             * 限时
             *//*
			 isOvertime = Math.abs(new Date().getTime()/1000-rs.getCrtTime().getTime()/1000) ;

		}*/

            /**
             * 如果是超时则认为答题失败
             */
/*		if( isOvertime > 10 ){
			int dScore = 1;
			try {
				dScore=quesDiffService.get(currentQues.getDifficultyId())
						.getScore();
			} catch (Exception e) {
			}

			AnswerInfo ai = new AnswerInfo(currentQues.getId(), -1,
					currentQues.getDifficultyId(), dScore,
					currentQues.getClassifyId(), 1, false, rushRecordId,
					AnswerInfo.T_GATE);
			ai.setFaultQuestion(value);
			AnswerInfoUtil.add(ai);

		}
		 else*/
            if (questionId != null && value != null) {
                boolean questionChek = QuesQuestionUtil.rushQuestionCheck(
                        currentQues, value);
                if (questionChek) {

                    int dScore = 1;
                    try {
                        dScore = quesDiffService.get(currentQues.getDifficultyId())
                                .getScore();
                    } catch (Exception e) {
                    }
                    AnswerInfo ai = new AnswerInfo(currentQues.getId(), -1,
                            currentQues.getDifficultyId(), dScore,
                            currentQues.getClassifyId(), 1, true, rushRecordId,
                            AnswerInfo.T_GATE);
                    AnswerInfoUtil.add(ai);

                } else {
                    int dScore = 1;
                    try {
                        dScore = quesDiffService.get(currentQues.getDifficultyId())
                                .getScore();
                    } catch (Exception e) {
                    }

                    AnswerInfo ai = new AnswerInfo(currentQues.getId(), -1,
                            currentQues.getDifficultyId(), dScore,
                            currentQues.getClassifyId(), 1, false, rushRecordId,
                            AnswerInfo.T_GATE);
                    ai.setFaultQuestion(value);
                    AnswerInfoUtil.add(ai);
                }

                List<QuesQuestions> list1 = ((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId));

                list1.remove(0);

                tokenUtil.setAttribute("questions" + sessionId, list1);
            }

            // 内存中答对的题数
            List<AnswerInfo> answerlist = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, true);

            // 内存中答错的题数
            List<AnswerInfo> faillist = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, false);

            int answerListSize = 0;
            int failListSize = 0;
            if (answerlist != null) {
                answerListSize = answerlist.size();
            }
            if (faillist != null) {
                failListSize = faillist.size();
            }
            // // 答过的总题
            int totalSize = answerListSize + failListSize;
            //
            //
//		 long limitTime = -1;
//		 boolean isLimitTime = false;
//		 long beginTime = gateRecord.getRushStartTime().getTime();
//		 long nowTime = System.currentTimeMillis();
//		 // 关口限时
//		 if (gate.getLimitTime() != null) {
//		 limitTime = gate.getLimitTime();
//		 isLimitTime = true;
//		 limitTime = limitTime - (nowTime - beginTime) / 1000;
//		 }
            //
            double success = 0;
            double fail = 0;
            int successCount = 0;
            int failCount = 0;
            boolean isRepeat = false;

            // 闯关成功获得积分
            success = ScoreUtil.computeScore(gateRecord, rushGate, true, true);
            // 闯关失败获得积分
            fail = ScoreUtil.computeScore(gateRecord, rushGate, true, false);

            // 格式化保留两位小数
            success = NumberFormatUtil.numberFormat(2, success);

            fail = NumberFormatUtil.numberFormat(2, fail);

            // 获得答对题目数量
            answerlist = AnswerInfoUtil.getAnswerInfo(gateRecord.getId(),
                    AnswerInfo.T_GATE, true);
            if (answerlist != null) {
                successCount = answerlist.size();
            }
            // 答错题目数量
            faillist = AnswerInfoUtil.getAnswerInfo(gateRecord.getId(),
                    AnswerInfo.T_GATE, false);
            if (faillist != null) {
                failCount = faillist.size();
            }
            int optType = 0;
            QuesQuestions nextQQ = null;
            QuestionAnswerJson qaj = null;
//		if (totalSize >= rushGate.getTestsNum()) {
            // 本关Id：可继续闯关。下一关ID：闯关成功。-1：闯关失败。0：闯关成功。但下一关不存在
            RushLevelScore userlevel = rushLevelScoreService
                    .getByUserNameAndSequenceId(LoginUtil.getUserLoginInfo().getUserName(),
                            gateRecord.getSequenceId());

            String result = RushGateUtil.passGate(gateRecord, rushGate, true);

            if (result.equals("0")) {
                optType = 1;
                checkPassGateSuccess(rushGate, rushGate, gateRecord, userlevel, isRepeat, sessionId, true);
                RushGateUtil.remove(sessionId);
                tokenUtil.removeAttribute("questions" + sessionId);
            }
            if (result.equals("-1")) {
                optType = 2;// 失败
                checkPassGateSuccess(rushGate, rushGate, gateRecord, userlevel, isRepeat, sessionId, false);
                RushGateUtil.remove(sessionId);
                tokenUtil.removeAttribute("questions" + sessionId);
            }
            if (result.equals("-12")) {
                optType = 2;// 失败（超时）
                checkPassGateSuccess(rushGate, rushGate, gateRecord, userlevel, isRepeat, sessionId, false);
                RushGateUtil.remove(sessionId);
                tokenUtil.removeAttribute("questions" + sessionId);
            }
//		}
            String upLevel = "否";
            if ((optType == 1 || optType == 2)) {
		/*	UserLevelScore userlevelCurrent = userLevelScoreService
					.getByUserNameAndSequenceId(UserUtil.getUser().getUserName(),
							gateRecord.getSequenceId(), UserLevelScore.TYPE_RUSH);
			if(!userlevelCurrent.getLevelId().equals(currentLevelId)){
				upLevel="是";
			}*/
                //修复BUG4922
                if (optType == 1) {
                    upLevel = "是";
                } else {
                    upLevel = "否";
                }
            }
            if (optType == 0) {
                List<QuesQuestions> list = ((List<QuesQuestions>) tokenUtil.getAttribute("questions" + sessionId));
                if (null != list && list.size() > 0) {
                    nextQQ = list.get(0);
                    qaj = JSONObject.parseObject(nextQQ.getAnswer(),
                            QuestionAnswerJson.class);
                } else {
                    nextQQ = new QuesQuestions();
                    qaj = new QuestionAnswerJson();
                }
            } else {
                nextQQ = new QuesQuestions();
                qaj = new QuestionAnswerJson();
            }

            qaj.setTitle(QuesQuestionUtil.decrypt(nextQQ.getQuestion()));
            qaj.setId(nextQQ.getId());
            // qaj.setTitle(nextQQ.getQuestion());
            if (result.equals("-12")) {
                qaj.setType("-12");
            } else {
                qaj.setType(nextQQ.getType() + "");
            }
            qaj.answerRemove();
            /**
             * 当前题目信息
             */
            //QuesQuestions qq = quesQuestionsService.queryQuestion(questionId);


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rushStartTime", gateRecord.getRushStartTime());
            map.put("successCount", successCount);
            map.put("failCount", failCount);
            map.put("successScore", success);
            map.put("failScore", fail);
            map.put("quesSum", rushGate.getQuesNums());
            map.put("quesFinish", totalSize);
            map.put("isUpgrade", upLevel);// 0是不升级，1是升级。
            map.put("quesInfo", qaj);
            if (questionId != null && value != null) {
                QuestionAnswerJson questionAnswerJson = JSONObject.parseObject(currentQues.getAnswer(),
                        QuestionAnswerJson.class);
                map.put("answer", questionAnswerJson.getAnswer());
            }
            map.put("selectAnswer", value);
            long rushTime = (System.currentTimeMillis() - gateRecord
                    .getRushStartTime().getTime()) / 1000;
            map.put("rushTime", rushTime);
            map.put("rushRecordId", gateRecord.getId());
            map.put("quesNo", totalSize + 1);
            map.put("optType", optType);// 0 正常出题,1成功2失败.
            return Results.parse(Constants.STATE_SUCCESS, "", map);
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL, "您已经退出闯关，请刷新页面重新闯关");
        }
    }


    /**
     * 验证闯关是否成功
     *
     * @param nowGate
     * @param nextPassGate
     * @param gateRecord
     * @param sessionId
     * @param rushResult
     * @return String
     */
    private String checkPassGateSuccess(RushSequence nowGate, RushSequence nextPassGate,
                                        RushSequenceRecord gateRecord, RushLevelScore userlevel, boolean isRepeat, String sessionId,
                                        boolean rushResult) throws Exception {
        String levelId = null;
        try {
            sessionId = StringTool.trimAll(sessionId);
            if (nowGate == null) {
                throw new Exception("当前所闯关口不存在，请联系管理员。");
            }
            if (gateRecord == null) {
                throw new Exception("用户当前闯关记录不存在。");
            }

            if (("").equals(sessionId)) {
                throw new Exception("您没有登录系统，请登录系统。");
            }
            if (rushResult) {
                gateRecord.setState(RushSequenceRecord.STATE_FINISH);
            } else {
                gateRecord.setState(RushSequenceRecord.STATE_FAIL);
            }
            // 计算积分
            double gateMark = ScoreUtil.computeScore(gateRecord, nowGate, isRepeat, rushResult);
            gateRecord.setScore(gateMark);
            gateRecord.setRushEndTime(new Date());
//			gateRecord.setRushStartTime(new Timestamp(System
//					.currentTimeMillis()));
            // int costTime = (int) (new Date().getTime() - gateRecord
            // .getRushStartTime().getTime()) / 1000;
            //gateRecord.setRushEndTime(new Date());
            int costTime = (int) (gateRecord.getRushEndTime().getTime() - gateRecord
                    .getRushStartTime().getTime()) / 1000;
            gateRecord.setCostTime(costTime);
            gateRecord.setUpdIp(ClientInfo.getIp());
            gateRecord.setUpdTime(new Date());
            gateRecord.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());

            // **获得所有答对的题目Id
            List<AnswerInfo> answerQuestionList = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, true);
            StringBuffer answerSb = new StringBuffer();
            if (answerQuestionList != null) {
                for (int i = 0; i < answerQuestionList.size(); i++) {
                    if (answerSb.length() > 0) {
                        answerSb.append(",");
                    }
                    answerSb.append(answerQuestionList.get(i).getQId());
                }
                gateRecord.setRightQuantity(answerQuestionList.size());
            }
            gateRecord.setRightQuestionId(answerSb.toString());

            // **获得所有答错题目的Id
            List<AnswerInfo> failQuestionList = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, false);
            StringBuffer failSb = new StringBuffer();
            StringBuffer failAnswerSb = new StringBuffer();
            if (failQuestionList != null) {
                for (int i = 0; i < failQuestionList.size(); i++) {
                    if (failSb.length() > 0) {
                        failSb.append(",");
                    }
                    if (failAnswerSb.length() > 0) {
                        failAnswerSb.append("---");
                    }
                    failSb.append(failQuestionList.get(i).getQId());
                    failAnswerSb.append(failQuestionList.get(i).getFaultQuestion());
                }
                gateRecord.setFaultQuantity(failQuestionList.size());
            }
            gateRecord.setRemainQues(Math.abs(nowGate.getQuesNums() - (answerQuestionList.size() + failQuestionList.size())));
            gateRecord.setFaultQuestionId(failSb.toString());//错题ID。
            gateRecord.setFaultQuestionIds(failAnswerSb.toString());//错题答案。

            // 取出旧的积分
            RushLevelScore level = rushLevelScoreService.get(userlevel.getId());
            // 用户等级原有积分
            double olduselevelScore = level.getScore();
            // 原有积分+闯关获得积分
            double newSocre = olduselevelScore + gateMark;
            // 计算等级
	/*		levelId = rushLevelService.computeUserLevel(gateRecord, nowGate,
					nextPassGate, isRepeat, userlevel.getLevelId(),
					olduselevelScore, rushResult);*/
            userlevel.setScore(newSocre);
            //userlevel.setLevelId(levelId);
            gateRecord.setUpdIp(ClientInfo.getIp());
            gateRecord.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
            String description1 = "用户原有积分=" + olduselevelScore + ";用户现有积分="
                    + newSocre;
            userlevel.setMark(description1);
            String gateId = nowGate.getId();
            String description = "闯Id=" + gateId + "的关口获得"
                    + gateRecord.getScore() + "分";
            gateRecord.setMark(description);
            gateRecord.setUpdIp(ClientInfo.getIp());
            gateRecord.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
            rushLevelScoreService.update(userlevel);
            rushSequenceService.update(gateRecord);

            // 处理闯关统计表
            RushUserRecord rushUserRecord = new RushUserRecord();
            rushUserRecord.setUserName(LoginUtil.getUserLoginInfo().getUserName());
            rushUserRecord.setSequenceId(gateId);
            rushUserRecord.setState(RushUserRecord.STATE_NOTFINISH);
            List<RushUserRecord> rlist = rushUserRecordService
                    .query(rushUserRecord);

            /**
             * 积分计入缓存
             */
            String token = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);

            IStringCache cache = CacheFactory.getStringCache();
            cache.set(token + ":score", newSocre + "");

            RushUserRecord rushrecord = null;
            //闯关人员得分统计表、
            //状态为：未完成0
            if (rlist != null && rlist.size() > 0) {
                rushrecord = rlist.get(0);
                rushrecord.setCostTime(gateRecord.getCostTime());
                rushrecord.setScore(NumberFormatUtil.numberFormat(2,
                        gateRecord.getScore()));
                rushrecord.setWrongCount(gateRecord.getFaultQuantity());
                rushrecord.setRightCount(gateRecord.getRightQuantity());
                rushrecord.setUpdIp(ClientInfo.getIp());
                rushrecord.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
                rushrecord.setState(gateRecord.getState());
                rushUserRecordService.update(rushrecord);
            } else {
                //状态为：闯关失败2
                if (gateRecord.getState() == RushUserRecord.STATE_FAIL) {
                    rushUserRecord.setState(RushUserRecord.STATE_FAIL);
                } else {
                    //状态为：闯关成功1
                    rushUserRecord.setState(RushUserRecord.STATE_FINISH);
                }
                rlist = rushUserRecordService.query(rushUserRecord);
                if (rlist != null && rlist.size() > 0) {
                    rushrecord = rlist.get(0);
                    //闯关次数
                    rushrecord.setRushCount(rushrecord.getRushCount() + 1);
                    //闯关用时
                    rushrecord.setCostTime(rushrecord.getCostTime()
                            + gateRecord.getCostTime());
                    //闯关描述
                    if (StringTool.isNotEmpty(rushrecord.getMark())) {
                        rushrecord.setMark(NumberFormatUtil.numberFormat(2,
                                gateRecord.getScore()) + "");
                    } else {
                        rushrecord.setMark(NumberFormatUtil.numberFormat(2,
                                gateRecord.getScore()) + "");
                    }
                    //答错题目个数
                    int wrongCount = 0;
                    if (rushrecord.getWrongCount() != null) {
                        wrongCount = rushrecord.getWrongCount();
                    }
                    int faultquantity = 0;
                    if (gateRecord.getFaultQuantity() != null) {
                        faultquantity = gateRecord.getFaultQuantity();
                    }
                    rushrecord.setWrongCount(wrongCount
                            + faultquantity);
                    //答错题目个数
                    int rightCount = 0;
                    if (rushrecord.getRightCount() != null) {
                        rightCount = rushrecord.getRightCount();
                    }
                    int rightquantity = 0;
                    if (gateRecord.getRightQuantity() != null) {
                        rightquantity = gateRecord.getRightQuantity();
                    }
                    rushrecord.setRightCount(rightCount
                            + rightquantity);
                    //得分
                    rushrecord.setScore(NumberFormatUtil.numberFormat(2, rushrecord.getScore()
                            + gateRecord.getScore()));
                    //更新ip
                    rushrecord.setUpdIp(ClientInfo.getIp());
                    //更新用户
                    rushrecord.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
                    rushUserRecordService.update(rushrecord);
                } else {
                    rushrecord = new RushUserRecord();
                    rushrecord.setUserName(gateRecord.getUserName());
                    rushrecord.setSequenceId(nowGate.getId());
                    rushrecord.setSequenceName(nowGate.getName());
                    rushrecord.setRushCount(1);
                    rushrecord.setCostTime(gateRecord.getCostTime());
                    if (gateRecord.getFaultQuantity() == null) {
                        rushrecord.setWrongCount(0);
                    } else {
                        rushrecord.setWrongCount(gateRecord.getFaultQuantity());
                    }

                    if (gateRecord.getRightQuantity() == null) {
                        rushrecord.setRightCount(0);
                    } else {
                        rushrecord.setRightCount(gateRecord.getRightQuantity());
                    }

                    rushrecord.setScore(NumberFormatUtil.numberFormat(2,
                            gateRecord.getScore()));
                    rushrecord.setCrtIp(ClientInfo.getIp());
                    rushrecord.setCrtTime(new Date());
                    rushrecord.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
                    rushrecord.setState(gateRecord.getState());
                    rushrecord.setCompanyId(LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
                    rushrecord.setCompanyName(LoginUtil.getUserLoginInfo().getInfos().get("orgName"));
                    rushUserRecordService.insert(rushrecord);
                }
            }
            return levelId;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // 删除闯关信息
            RushGateUtil.remove(sessionId);
            // 删除出题信息
            // QuestionsUtil.remove(sessionId, QuestionsUtil.TYPE_GATERECORD,
            // tmpgrid);
            // 删除答题信息
            AnswerInfoUtil.remove(gateRecord.getId(), AnswerInfo.T_GATE);
        }
    }

    /**
     * 闯关答题页面 获取该用户在当前工种下的闯关信息
     *
     * @param sequenceId
     * @return
     * @author dai.jiaqi
     */
	/*@At("/getUserRushInfo")
	public Object getUserRushInfo(@Param("gatesequenceId") String sequenceId) {
		if(StringTool.isEmpty(sequenceId)){
			return Results.parse(Constants.STATE_FAIL, "传参错误",  "传参错误");
		}
		SysUser userInfo = UserUtil.getUser();
		RushSequence rushSequence = rushSequenceService.get(sequenceId);
		if(rushSequence==null){
			return Results.parse(Constants.STATE_FAIL, "操作错误，验证工种信息失败！",  "操作错误，验证工种信息失败！");
		}
		Map<String, String> map = rushSequenceService.getUserRushInfo(rushSequence.getSuperId(), userInfo.getUserName());
		HashMap<String, Object> map1 = new HashMap<>();
        map1.putAll(map);
        for (String s : map1.keySet()) {
            if (map1.get(s) == null && s.equals("realName")) {
                map = new HashMap<String, String>();
                map.put("realName", userInfo.getUserRealName());
                map.put("totalScore", 0 + "");
                map.put("levelName", "未知");

                SysOrganization sysOrganization = sysOrganizationService
                        .get(userInfo.getOrgId());
                if (sysOrganization != null) {
                    map.put("orgName", sysOrganization.getName());
                } else {
                    map.put("orgName", "未知");
                }
                map.put("rank", "未知");
                break;
            }
        }


		map.get("totalScore");
		return Results.parse(Constants.STATE_SUCCESS, null, map);
	}*/


    /**
     * @param sequenceId
     * @param state/0,正常退出闯关，1,为非正常退出，闯关前清理用户闯关信息
     * @return
     */
    @At("/exitRushGate")
    public Object exitRushGate(@Param("sequenceId") String sequenceId, @Param("state") String state) {
        try {
            if (StringTool.isNotEmpty(state) && state.equals("1")) {
                //1，为非正常退出，闯关前清理用户闯关信息
//				System.out.println("考前清理闯关缓存！");
                String sessionId = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
                RushGateUtil.remove(sessionId);
                return Results.parse(Constants.STATE_SUCCESS, null, null);
            }
//		 checkPassGateSuccess(rushGate, nextRushGate, rushGateRecord,
//		 userlevel, isRepeat, sessionId, false);
            RushSequence rushGate = rushSequenceService.get(sequenceId);
            String sessionId = Mvcs.getReq().getHeader(StaticUtils.TOKEN_NAME);
            RushSequenceRecord gateRecord = RushGateUtil.get(sessionId);
            RushLevelScore userlevel = rushLevelScoreService
                    .getByUserNameAndSequenceId(LoginUtil.getUserLoginInfo().getUserName(),
                            gateRecord.getSequenceId());

            // 是否是重复闯关
            boolean isRepeat = false;
            RushUserRecord rushGateuserRecord = rushUserRecordService
                    .getRushGateuserRecords(LoginUtil.getUserLoginInfo().getUserName(), sequenceId,
                            RushUserRecord.STATE_FINISH);
            if (rushGateuserRecord != null) {
                isRepeat = true;
            }

            // 内存中答对的题数
            List<AnswerInfo> answerlist = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, true);
            // 内存中答错的题数
            List<AnswerInfo> faillist = AnswerInfoUtil.getAnswerInfo(
                    gateRecord.getId(), AnswerInfo.T_GATE, false);
            double success = 0;
            double fail = 0;
            int successCount = 0;
            int failCount = 0;
            // 闯关成功获得积分
            success = ScoreUtil.computeScore(gateRecord, rushGate, isRepeat, true);
            // 闯关失败获得积分
            fail = ScoreUtil.computeScore(gateRecord, rushGate, isRepeat, false);

            // 格式化保留两位小数
            success = NumberFormatUtil.numberFormat(2, success);
            fail = NumberFormatUtil.numberFormat(2, fail);

            // 获得答对题目数量
            answerlist = AnswerInfoUtil.getAnswerInfo(gateRecord.getId(),
                    AnswerInfo.T_GATE, true);
            if (answerlist != null) {
                successCount = answerlist.size();
            }
            // 答错题目数量
            faillist = AnswerInfoUtil.getAnswerInfo(gateRecord.getId(),
                    AnswerInfo.T_GATE, false);
            if (faillist != null) {
                failCount = faillist.size();
            }
            gateRecord.setFaultQuantity(failCount);
            gateRecord.setRightQuantity(successCount);
            gateRecord.setCostTime(gateRecord.getCostTime());

            String result = RushGateUtil
                    .passGate(gateRecord, rushGate, isRepeat);
            int optType = 2;
            if (result.equals("0")) {
                optType = 1;//chenggong
            }
            if (result.equals("-1")) {//失败
                optType = 2;
            }
            boolean rushResult = false;
            if (optType == 1) {
                rushResult = true;
            }
            checkPassGateSuccess(rushGate, rushGate, gateRecord, userlevel,
                    isRepeat, sessionId, rushResult);
            HttpSession session = Mvcs.getReq().getSession();
            session.removeAttribute("question");

            int answerListSize = 0;
            int failListSize = 0;
            if (answerlist != null) {
                answerListSize = answerlist.size();
            }
            if (faillist != null) {
                failListSize = faillist.size();
            }
		/*	RushLevelScore userlevelCurrent = rushLevelScoreService
				.getByUserNameAndSequenceId(LoginUtil.getUserLoginInfo().getUserName(),
						gateRecord.getSequenceId());
		String upLevel = "否";*/
		/*if((optType==1 || optType==2)){
			if(!userlevelCurrent.getLevelId().equals(currentLevelId)){
				upLevel="是";
			}
		}*/
            // // 答过的总题
            int totalSize = answerListSize + failListSize;
            QuestionAnswerJson qaj = new QuestionAnswerJson();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rushStartTime", gateRecord.getRushStartTime());
            map.put("successCount", successCount);
            map.put("failCount", failCount);
            map.put("successScore", success);
            map.put("failScore", fail);
            map.put("quesSum", rushGate.getQuesNums());
            map.put("quesFinish", totalSize);
            //map.put("isUpgrade", upLevel);// 0是不升级，1是升级。
            map.put("quesInfo", qaj);
            long rushTime = (System.currentTimeMillis()
                    - gateRecord.getRushStartTime().getTime()) / 1000;
            map.put("rushTime", rushTime);
            map.put("rushRecordId", gateRecord);
            map.put("quesNo", totalSize + 1);
            map.put("optType", optType);// 1成功2失败.
            TokenUtils tu = new TokenUtils();
            tu.removeAttribute("questions" + sessionId);//清理redis闯关试题对象
            RushGateUtil.remove(sessionId);
            session.removeAttribute("questions");
            return Results.parse(Constants.STATE_SUCCESS, "", map);
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL, "您已经退出闯关，请刷新页面从新闯关!");
        }
    }

}
