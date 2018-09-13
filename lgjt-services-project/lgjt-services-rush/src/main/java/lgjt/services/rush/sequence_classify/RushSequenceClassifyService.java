package lgjt.services.rush.sequence_classify;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.rush.ques.QuesClassify;
import lgjt.domain.rush.sequence_classify.RushSequenceClassify;
import lgjt.domain.rush.utils.LoginUtil;


@Log4j
@IocBean
public class RushSequenceClassifyService extends BaseService {


	public PageResult<RushSequenceClassify> queryPage(RushSequenceClassify obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getClassifyId())) {
			cri.where().andEquals("classify_id", obj.getClassifyId());
		}
		if(StringTool.isNotNull(obj.getDifficulty())) {
			cri.where().andEquals("difficulty", obj.getDifficulty());
		}
		if(StringTool.isNotNull(obj.getQuestionType())) {
			cri.where().andEquals("question_type", obj.getQuestionType());
		}
		if(StringTool.isNotNull(obj.getTestsType())) {
			cri.where().andEquals("tests_type", obj.getTestsType());
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
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}

		return super.queryPage(RushSequenceClassify.class, obj, cri);
	}

	public List<RushSequenceClassify> query(RushSequenceClassify obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getClassifyId())) {
			cri.where().andEquals("classify_id", obj.getClassifyId());
		}
		if(StringTool.isNotNull(obj.getDifficulty())) {
			cri.where().andEquals("difficulty", obj.getDifficulty());
		}
		if(StringTool.isNotNull(obj.getQuestionType())) {
			cri.where().andEquals("question_type", obj.getQuestionType());
		}
		if(StringTool.isNotNull(obj.getTestsType())) {
			cri.where().andEquals("tests_type", obj.getTestsType());
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
		if(StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if(StringTool.isNotNull(obj.getExtend6())) {
			cri.where().andEquals("extend6", obj.getExtend6());
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
		if(StringTool.isNotNull(obj.getExtend5())) {
			cri.where().andEquals("extend5", obj.getExtend5());
		}
		if(StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		return super.query(RushSequenceClassify.class, cri);
	}

   	public RushSequenceClassify get(String id) {
		return super.fetch(RushSequenceClassify.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushSequenceClassify.class, cri);
		}
		return 0;
	}

	public RushSequenceClassify checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(RushSequenceClassify.class,cri);
	}


	public int insertUpdateClassify(RushSequenceClassify obj) {
		int success = 1;
		String[] sequenceArray = obj.getSequenceIds().split(",");
		for(String sequencenId: sequenceArray) {
			//首先查询
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("sequence_id", sequencenId);
			List<RushSequenceClassify> rushSequenceClassifiesList = super.query(RushSequenceClassify.class, cri);
			if(rushSequenceClassifiesList.size() > 0) {
				//update
				for(RushSequenceClassify queryobj: rushSequenceClassifiesList) {
					obj.setId(queryobj.getId());
					success = super.updateIgnoreNull(obj);
				}
			} else {
				//insert
				obj.setSequenceId(sequencenId);
				RushSequenceClassify insert = new RushSequenceClassify();
				insert.setSequenceId(sequencenId);
				insert.setClassifyId(obj.getClassifyId());
				insert.setQuestionType(obj.getQuestionType());
				insert.setDifficulty(obj.getDifficulty());
				super.insert(insert);
			}
		}
		return success;
	}

	/**
	 * 闯关选择题库查询。
	 * @return
	 */
	public List<QuesClassify> queryWithRole(Integer isCollect) {
		//根据用户信息确认数据权限。
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("qc3.state", QuesClassify.STATE_ON);
		cri.where().and("qc3.super_id", "!=", "-1");
		/*if(isCollect!=null&&isCollect==QuesClassify.ISCOLLECT_ON){
			cri.where().andEquals("qc3.is_collect", QuesClassify.ISCOLLECT_ON);

		}*/
		//不考虑企业
		//admin：全部，
	/*	if(LoginUtil.getUserLoginInfo().getUserName().equals("admin")){

		}else
			//平台管理员：平台，
			if("userPlatform".equals(LoginUtil.getUserLoginInfo().getInfos().get("userPlatform"))){
				cri.where().andEquals("qc3.company_id", "");
			}else
				//企业管理员：自己企业的
				if("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))){
					cri.where().andEquals("qc3.company_id", LoginUtil.getUserLoginInfo().getInfos().get("companyId"));
					cri.where().orEquals("qc3.company_id", "");
					cri.where().andNotEquals("qc3.super_id", "-1");
				}else{
					return null;
				}*/
		cri.asc("qc1.name").asc("qc2.name").asc("qc3.name");
		List<QuesClassify> classify = super.query("rush.adminGateClassify.queryWithRole",QuesClassify.class, cri);
		if(classify.size()>0){
			for(int i=0 ; i<classify.size() ; i++){
				QuesClassify qc = new QuesClassify();
				qc = classify.get(i);
				if(qc.getName1()==null){
					if(qc.getName2()==null){
						qc.setUserName(qc.getName());
					}else{
						qc.setUserName(qc.getName2()+"/"+qc.getName());
					}
				}else{
					qc.setUserName(qc.getName1()+"/"+qc.getName2()+"/"+qc.getName());
				}
				classify.set(i, qc);
			}
		}
		return classify ;
	}


}