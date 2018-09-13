package lgjt.web.moments.module.challenge;

import com.ttsx.platform.nutz.result.PageResult;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.moments.challenge.LgChallenge;
import lgjt.domain.moments.challenge.LgChallengePk;
import lgjt.domain.moments.challenge.LgChallengePkVo;
import lgjt.domain.moments.challenge.LgChallengeRank;
import lgjt.services.moments.challenge.LgChallengePkService;
import lgjt.services.moments.challenge.LgChallengeService;
import lgjt.services.moments.challenge.LgFileInfoService;
import lgjt.services.moments.userlike.LgUserLikeService;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import sun.rmi.runtime.Log;

import java.util.*;

/**
 * @author 赵天意
 * 竞技跟拍接口
 */
@At("/lgChallenge")
@IocBean
@Log4j
public class LgChallengeModule {


    @Inject("lgChallengeService")
    LgChallengeService lgChallengeService;

    @Inject
    LgChallengePkService lgChallengePkService;

    @Inject
    LgFileInfoService fileInfoService;

    @Inject
    LgUserLikeService lgUserLikeService;

    /**
     * 发起/挑战 跟拍
     *
     * @param obj
     * @param challengerMark 0发起挑战 1挑战
     * @param challengerId   挑战的ID
     * @param attachId       资源JSON
     * @param type           资源类型 1视频 2图片
     * @return
     */
    @At("/insert")
    @POST
    public Object insert(@Param("..") LgChallenge obj,
                         @Param("challengerMark") String challengerMark,
                         @Param("challengerId") String challengerId,
                         @Param("attachId") String attachId,
                         @Param("type") String type) {
        //提前设置好当前登录人的信息
        String userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");
        String userName = LoginUtil.getUserLoginInfo().getUserName();
        String userProfile = LoginUtil.getUserLoginInfo().getInfos().get("userProfile");
        String crtIp = ClientInfo.getIp();
        String realName = LoginUtil.getUserLoginInfo().getRealName();

        obj.setUserId(userId);
        obj.setUserProfile(userProfile);
        obj.setRealName(realName);
        obj.setCrtTime(new Date());
        obj.setCrtIp(crtIp);
        obj.setCrtUser(userName);
        obj.setIsDisable(0);
        //extend1 存机构ID extend2存机构名称
        obj.setExtend1(LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
        obj.setExtend2(LoginUtil.getUserLoginInfo().getInfos().get("orgName"));

        //默认的审核状态
        obj.setStatus(0);
        //设置发布时间
        obj.setPublishTime(new Date());

        if (challengerMark.equals("0")) {
            //发起挑战
            LgChallenge insert = lgChallengeService.insert(obj);
            if (insert != null) {
                //附件表插入
                fileInfoService.insertFile(attachId, type, insert.getId());
            }
            return Results.parse(Constants.STATE_SUCCESS, null, null);
        } else {
            //挑战别人
            LgChallengePk pk = new LgChallengePk(obj);
            pk.setSuperId(challengerId);
            pk.setUserId(userId);
            pk.setUserProfile(userProfile);
            pk.setRealName(realName);
            pk.setCrtTime(new Date());
            pk.setCrtIp(crtIp);
            pk.setCrtUser(userName);
            pk.setIsDisable(0);
            pk.setExtend1(LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
            pk.setExtend2(LoginUtil.getUserLoginInfo().getInfos().get("orgName"));

            //默认的审核状态
            pk.setStatus(0);
            //设置发布时间
            pk.setPublishTime(new Date());

            LgChallengePk insert = lgChallengePkService.insert(pk);
            if (insert != null) {
                //附件表的插入
                fileInfoService.insertFile(attachId, type, insert.getId());
            }
            return Results.parse(Constants.STATE_SUCCESS, null, null);
        }
    }

    /**
     * 查询跟拍列表
     *
     * @return
     */
    @At("/queryChallengerList")
    public Object queryChallengerList(@Param("..") LgChallenge obj) {

        String userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");

        //查询的时候一定要查审核通过的
        obj.setStatus(1);

        PageResult<LgChallenge> result = lgChallengeService.queryPage(obj);
        List<LgChallenge> lgChallengeList = result.getRows();
        for (LgChallenge lgChallenge : lgChallengeList) {
            //第一步查询是否当前用户发送
            if (lgChallenge.getUserId().equals(userId)) {
                lgChallenge.setMark("1");
            } else {
                lgChallenge.setMark("0");
            }

            //第二步查询发起题材数量
            Integer crtCount = lgChallengeService.getCrtCount(lgChallenge.getUserId());
            lgChallenge.setCrtCount(crtCount);

            //第三步查询挑战的数量
            Integer challengerCount = lgChallengePkService.getChallengerCount(lgChallenge.getUserId());
            lgChallenge.setChallengerCount(challengerCount);

            //第四步查询资源
            Map<String, String> resourceMap = fileInfoService.getResourceMap(lgChallenge.getId());
            lgChallenge.setAttachId(resourceMap.get("attachId"));
            lgChallenge.setType(resourceMap.get("type"));
        }
        //重新设置参数
        result.setRows(lgChallengeList);
        return Results.parse(Constants.STATE_SUCCESS, null, result);
    }

    /**
     * 查看跟拍详情
     * @param obj
     * @return
     */
    @At("/challengerInfo")
    public Object challengerInfo(@Param("..") LgChallenge obj) {
        String userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");

        List<Map<String, Object>> rankList = new ArrayList();
        //第一步查询发起挑战的
        LgChallenge lgChallenge = lgChallengeService.get(obj.getId());
        if (lgChallenge.getUserId().equals(userId)) {
            lgChallenge.setMark("1");
        } else {
            lgChallenge.setMark("0");
        }
        //第二步查询发起题材数量
        lgChallenge.setCrtCount(lgChallengeService.getCrtCount(lgChallenge.getUserId()));
        //第三步查询挑战的数量
        lgChallenge.setChallengerCount(lgChallengePkService.getChallengerCount(lgChallenge.getUserId()));
        //第四步查询资源
        Map<String, String> resourceMap = fileInfoService.getResourceMap(lgChallenge.getId());
        lgChallenge.setAttachId(resourceMap.get("attachId"));
        lgChallenge.setType(resourceMap.get("type"));
        //第五步查询当前用户是否点赞
        lgChallenge.setLikeMark(lgUserLikeService.likeMark(userId, lgChallenge.getId()));
        //点赞数量
        lgChallenge.setLikeCount(lgUserLikeService.likeCount(lgChallenge.getId()));

        //用来排序的
        Map<String, Object> fatherMap = new HashMap<>();
        fatherMap.put("father", "1");
        fatherMap.put("id", lgChallenge.getId());
        fatherMap.put("likeCount", lgChallenge.getLikeCount().toString());
        rankList.add(fatherMap);

        //第六步查询跟拍者
        LgChallengePkVo vo = new LgChallengePkVo();
        vo.setPage(obj.getPage());
        vo.setPageSize(obj.getPageSize());
        vo.setSuperId(lgChallenge.getId());
//        PageResult<LgChallengePkVo> result = lgChallengePkService.queryPageVo(vo);
        List<LgChallengePkVo> lgChallengePkVos = lgChallengePkService.query(vo);
        //第七步跟拍者的各种参数查询
        for (LgChallengePkVo lgChallengePkVo : lgChallengePkVos) {
            //判断是否当前用户
            if (lgChallengePkVo.getUserId().equals(userId)) {
                lgChallengePkVo.setMark("1");
            } else {
                lgChallengePkVo.setMark("0");
            }
            //两个数量的查询
            lgChallengePkVo.setCrtCount(lgChallengeService.getCrtCount(lgChallengePkVo.getUserId()));
            lgChallengePkVo.setChallengerCount(lgChallengePkService.getChallengerCount(lgChallengePkVo.getUserId()));
            //点赞数量
            lgChallengePkVo.setLikeCount(lgUserLikeService.likeCount(lgChallengePkVo.getId()));
            lgChallengePkVo.setLikeMark(lgUserLikeService.likeMark(userId, lgChallengePkVo.getId()));

            //图片或者视频
            Map<String, String> resource = fileInfoService.getResourceMap(lgChallengePkVo.getId());
            lgChallengePkVo.setAttachId(resource.get("attachId"));
            lgChallengePkVo.setType(resource.get("type"));

            Map<String, Object> map = new HashMap<>();
            map.put("father", "0");
            map.put("id", lgChallengePkVo.getId());
            map.put("likeCount", lgChallengePkVo.getLikeCount());
            rankList.add(map);
        }

        /*
        排序用
         */
        Collections.sort(rankList, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer likeCount1 = Integer.valueOf(o1.get("likeCount").toString());
                Integer likeCount2 = Integer.valueOf(o2.get("likeCount").toString());
//                return likeCount1.compareTo(likeCount2);
                return likeCount2 - likeCount1;
            }
        });

        for (int i = 0; i < rankList.size(); i++) {
            Map<String, Object> map = rankList.get(i);
            if (map.get("father").equals("1")) {
                lgChallenge.setRank(i + 1);
            } else {
                //循环
                for (LgChallengePkVo lgChallengePkVo : lgChallengePkVos) {
                    String id = (String) map.get("id");
                    if (lgChallengePkVo.getId().equals(id)) {
                        lgChallengePkVo.setRank(i + 1);
                    }
                }

            }
        }

        List<LgChallengePkVo> result = new ArrayList<>();

        int start = (obj.getPage() - 1) * obj.getPageSize();
        int end = obj.getPageSize() * obj.getPage();

        if(end > lgChallengePkVos.size()) {
            end = lgChallengePkVos.size();
        }

        Boolean clear  = false;
        if(start > lgChallengePkVos.size()) {
            clear = true;
        }

        if(!clear) {
            result = lgChallengePkVos.subList(start, end);
        }


        PageResult<LgChallengePkVo> pageResult = new PageResult<LgChallengePkVo>(result, lgChallengePkVos.size());
        lgChallenge.setChallengers(pageResult);

        return Results.parse(Constants.STATE_SUCCESS, null, lgChallenge);
    }

    /**
     * 查询我的跟拍
     * @param pk
     * @param content
     * @return
     */
    @At("/queryMyChallenger")
    public Object queryMyChallenger(@Param("pk") String pk, @Param("content") String content,
        @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
        String userId = LoginUtil.getUserLoginInfo().getInfos().get("userId");
        if(pk.equals("0")) {
            //发起的
            LgChallenge lgChallenge = new LgChallenge();
            lgChallenge.setUserId(userId);
            lgChallenge.setPage(page);
            lgChallenge.setPageSize(pageSize);
            if(StringUtils.isNotEmpty(content)) {
                lgChallenge.setContent(content);
            }
            PageResult<LgChallenge> result = lgChallengeService.queryPage(lgChallenge);
            List<LgChallenge> lgChallenges = result.getRows();
            for(LgChallenge obj: lgChallenges) {
                Map<String, String> resource = fileInfoService.getResourceMap(obj.getId());
                obj.setAttachId(resource.get("attachId"));
                obj.setType(resource.get("type"));
                obj.setLikeCount(lgUserLikeService.likeCount(obj.getId()));
            }


            return Results.parse(Constants.STATE_SUCCESS, null, result);
        } else {
            //挑战的
            LgChallengePk lgChallengePk = new LgChallengePk();
            lgChallengePk.setUserId(userId);
            lgChallengePk.setPage(page);
            lgChallengePk.setPageSize(pageSize);
            if(StringUtils.isNotEmpty(content)) {
                lgChallengePk.setContent(content);
            }

            PageResult<LgChallengePk> result = lgChallengePkService.queryPage(lgChallengePk);
            List<LgChallengePk> lgChallenges = result.getRows();
            for(LgChallengePk obj: lgChallenges) {
                Map<String, String> resource = fileInfoService.getResourceMap(obj.getId());
                obj.setAttachId(resource.get("attachId"));
                obj.setType(resource.get("type"));
                obj.setLikeCount(lgUserLikeService.likeCount(obj.getId()));
            }
            return Results.parse(Constants.STATE_SUCCESS, null, result);
        }
    }

    /**
     * 查询某一条跟拍排行
     * @param challengeId
     * @return
     */
    @At("/queryChallengerRank")
    public Object queryChallengerRank(@Param("id") String challengeId, @Param("page")Integer page, @Param("pageSize") Integer pageSize) {
        //第一步查询所有的排行
        List<LgChallengeRank> allRanks = lgChallengeService.queryChallengerAllRank(challengeId, page, pageSize);
        //第二步查询当前用户的排行
        List<LgChallengeRank> myRank = lgChallengeService.queryChallengerMyRank(challengeId, LoginUtil.getUserLoginInfo().getInfos().get("userId"));

        Map<String, Object> result = new HashMap<>();
        result.put("allRanks", allRanks);

        if(myRank.size() > 0) {
            result.put("myRank", myRank.get(0));
        } else {
            result.put("myRank", null);
        }

        return Results.parse(Constants.STATE_SUCCESS, null, result);
    }
}