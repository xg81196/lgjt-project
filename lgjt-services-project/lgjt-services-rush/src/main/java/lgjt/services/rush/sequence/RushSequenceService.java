package lgjt.services.rush.sequence;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.domain.rush.sequence.RushSequenceVo;
import lgjt.domain.rush.sequence_classify.RushSequenceClassify;


@Log4j
@IocBean
public class RushSequenceService extends BaseService {

	private static final String QUERY_BY_NAME = "rush.admin.sequence.queryPageByName";

	private static final String QUERY_BY_ID = "rush.sequence.queryById";

	public PageResult<RushSequence> queryPage(RushSequence obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		//add by jialx  增加两个字段
		if(StringTool.isNotNull(obj.getCategaryName())) {
			cri.where().andEquals("categary_name", obj.getCategaryName());
		}
		if(StringTool.isNotNull(obj.getCategaryCode())) {
			cri.where().andEquals("categary_code", obj.getCategaryCode());
		}
		
		if(StringTool.isNotNull(obj.getImageId())) {
			cri.where().andEquals("image_id", obj.getImageId());
		}
		if(StringTool.isNotNull(obj.getQuesNums())) {
			cri.where().andEquals("ques_nums", obj.getQuesNums());
		}
		if(StringTool.isNotNull(obj.getLimitTime())) {
			cri.where().andEquals("limit_time", obj.getLimitTime());
		}
		if(StringTool.isNotNull(obj.getSignalLimitFlag())) {
			cri.where().andEquals("signal_limit_flag", obj.getSignalLimitFlag());
		}
		if(StringTool.isNotNull(obj.getSignalLimitTime())) {
			cri.where().andEquals("signal_limit_time", obj.getSignalLimitTime());
		}
		if(StringTool.isNotNull(obj.getScoreWeight())) {
			cri.where().andEquals("score_weight", obj.getScoreWeight());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getPassContitionValue())) {
			cri.where().andEquals("pass_contition_value", obj.getPassContitionValue());
		}
		if(StringTool.isNotNull(obj.getPassFailureMarkWeight())) {
			cri.where().andEquals("pass_failure_mark_weight", obj.getPassFailureMarkWeight());
		}
		if(StringTool.isNotNull(obj.getAnswerWrongFlag())) {
			cri.where().andEquals("answer_wrong_flag", obj.getAnswerWrongFlag());
		}
		if(StringTool.isNotNull(obj.getAnswerWrongMark())) {
			cri.where().andEquals("answer_wrong_mark", obj.getAnswerWrongMark());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}

		PageResult<RushSequence> pageResult =  super.queryPage(RushSequence.class, obj, cri);

//		try {
//			for (RushSequence rushSequence : pageResult.getRows())  {
//				if (StringUtils.isNotBlank(rushSequence.getImageId())) {
//					JSONObject jsonObject = JSON.parseObject(rushSequence.getImageId());
//					JSONObject object= (JSONObject)jsonObject.getJSONArray("data").get(0);
//					object.replace("id", ResourceEncryptionUtil.base64Encoder((String)object.get("id")));
//					rushSequence.setImageId(jsonObject.toJSONString());
//				}
//			}
//		} catch ( Exception e ) {
//			log.error(e);
//		}

		return pageResult;

	}

	public List<RushSequence> query(RushSequence obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getImageId())) {
			cri.where().andEquals("image_id", obj.getImageId());
		}
		if(StringTool.isNotNull(obj.getQuesNums())) {
			cri.where().andEquals("ques_nums", obj.getQuesNums());
		}
		if(StringTool.isNotNull(obj.getLimitTime())) {
			cri.where().andEquals("limit_time", obj.getLimitTime());
		}
		if(StringTool.isNotNull(obj.getSignalLimitFlag())) {
			cri.where().andEquals("signal_limit_flag", obj.getSignalLimitFlag());
		}
		if(StringTool.isNotNull(obj.getSignalLimitTime())) {
			cri.where().andEquals("signal_limit_time", obj.getSignalLimitTime());
		}
		if(StringTool.isNotNull(obj.getScoreWeight())) {
			cri.where().andEquals("score_weight", obj.getScoreWeight());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getPassContitionValue())) {
			cri.where().andEquals("pass_contition_value", obj.getPassContitionValue());
		}
		if(StringTool.isNotNull(obj.getPassFailureMarkWeight())) {
			cri.where().andEquals("pass_failure_mark_weight", obj.getPassFailureMarkWeight());
		}
		if(StringTool.isNotNull(obj.getAnswerWrongFlag())) {
			cri.where().andEquals("answer_wrong_flag", obj.getAnswerWrongFlag());
		}
		if(StringTool.isNotNull(obj.getAnswerWrongMark())) {
			cri.where().andEquals("answer_wrong_mark", obj.getAnswerWrongMark());
		}
		if(StringTool.isNotNull(obj.getCompanyId())) {
			cri.where().andEquals("company_id", obj.getCompanyId());
		}
		if(StringTool.isNotNull(obj.getExtend1())) {
			cri.where().andEquals("extend1", obj.getExtend1());
		}
		if(StringTool.isNotNull(obj.getExtend2())) {
			cri.where().andEquals("extend2", obj.getExtend2());
		}
		if(StringTool.isNotNull(obj.getExtend3())) {
			cri.where().andEquals("extend3", obj.getExtend3());
		}
		if(StringTool.isNotNull(obj.getExtend4())) {
			cri.where().andEquals("extend4", obj.getExtend4());
		}
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
		}
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if(StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if(StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if(StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		return super.query(RushSequence.class, cri);
	}

	/**
	 * 通过工种名称查询
	 * @param obj
	 * @return
	 */
	public PageResult<RushSequenceVo> queryPageByName(RushSequence obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andLike("rs.name", obj.getName());
		}
		cri.desc("crt_time");
		PageResult<RushSequenceVo> pageResult = super.queryPage(QUERY_BY_NAME, RushSequenceVo.class, obj, cri);

	   return pageResult;

	}

    /**
     * 通过ID查询工种
     * @param id
     * @return
     */
	public List<RushSequenceVo> queryById(String id) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(id)) {
			cri.where().andEquals("rs.id", id);
		}
		return super.query(QUERY_BY_ID, RushSequenceVo.class, cri);
	}

    /**
     * 多条数据更新
     * @param ids
     * @param obj
     * @return
     */
	public int multipleUpdate(String ids, RushSequence obj) {
		int success = 1;
		String[] idsArray = ids.split(",");
		for(String id: idsArray) {
			obj.setId(id);
			success = super.updateIgnoreNull(obj);
		}
		return success;
	}


   	public RushSequence get(String id) {
		return super.fetch(RushSequence.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushSequence.class, cri);
		}
		return 0;
	}

	public RushSequence checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(RushSequence.class,cri);
	}

	/**
	 * 工种发布
	 * @param ids
	 * @return
	 */
	public String release(String ids) {
		//已经发布的数量
		int isRelease = 0;
		//发布成功的数量
		int release = 0;
		//没有题库的
        List<RushSequence> noneClassfity = new ArrayList<>();
        //没有权重的
        List<RushSequence> noneWeight = new ArrayList<>();

		String[] idArray = ids.split(",");
		for(String id: idArray) {
            //是否断掉
		    Boolean breaked = false;

            SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("id",id);
			List<RushSequence> rs = super.query(RushSequence.class, cri);
			RushSequence obj = rs.get(0);

			/*
			首先检查是否设置题库，是否设置权重
			首先查询权重
			 */
			if(obj.getPassFailureMarkWeight()==null) {
			    //没有权重
                noneWeight.add(obj);
                breaked = true;
            }
            cri = Cnd.cri();
            cri.where().andEquals("sequence_id", obj.getId());
            List<RushSequenceClassify> rscList = super.query(RushSequenceClassify.class, cri);
            if(rscList.size() == 0) {
                //没有题库
                noneClassfity.add(obj);
                breaked = true;
            }

            if(breaked==false) {
				if(StringUtils.isNotEmpty(obj.getExtend1()) && obj.getExtend1().equals("1")) {
					//如果本身就已经发布，那么什么都不做
					isRelease++;
				} else {
					//未发布的，发布;
					obj.setExtend1("1");
					release += super.updateIgnoreNull(obj);
				}
            }

		}

		StringBuffer result = new StringBuffer();
		result.append("共"+idArray.length+"条数据，本次成功发布"+release+"条，已发布过"+isRelease+"条。");

		if(noneClassfity.size() > 0 || noneWeight.size() > 0) {
		    result.append("以下工种不能发布，");
        }

		if(noneClassfity.size() > 0)  {
            result.append("未设置题库:");
            for(RushSequence rs: noneClassfity) {
                result.append(rs.getName()+";");
            }
        }

        if(noneWeight.size() > 0) {
            result.append("未设置权重：");
            for(RushSequence rs: noneWeight) {
                result.append(rs.getName()+";");
            }
        }

        return result.toString();
	}


	/**
	 * 工种取消发布
	 * @param ids
	 * @return
	 */
	public String deRelease(String ids) {
		//未发布数量
		int unRelease = 0;
		//成功取消发布的数量
		int deRelsase = 0;
		String[] idArray = ids.split(",");
		for(String id: idArray) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("id",id);
			List<RushSequence> rs = super.query(RushSequence.class, cri);
			RushSequence obj = rs.get(0);
            if(StringUtils.isNotEmpty(obj.getExtend1()) && obj.getExtend1().equals("0")) {
				//如果本身就没有发布，那么什么都不做
				unRelease++;
			} else {
				//发布的，取消发布
				obj.setExtend1("0");
				deRelsase += super.updateIgnoreNull(obj);
			}
		}

		return "共"+idArray.length+"条数据，本次成功取消发布"+deRelsase+"条，从未发布过"+unRelease+"条";
	}

}