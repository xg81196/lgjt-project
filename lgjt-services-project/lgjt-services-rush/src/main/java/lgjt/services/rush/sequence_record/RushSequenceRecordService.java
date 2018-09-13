package lgjt.services.rush.sequence_record;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.rush.sequence_record.RushSequenceRecord;


@Log4j
@IocBean
public class RushSequenceRecordService extends BaseService {

	public PageResult<RushSequenceRecord> queryPage(RushSequenceRecord obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRushStartTime())) {
			cri.where().andEquals("rush_start_time", obj.getRushStartTime());
		}
		if(StringTool.isNotNull(obj.getRushEndTime())) {
			cri.where().andEquals("rush_end_time", obj.getRushEndTime());
		}
		if(StringTool.isNotNull(obj.getCostTime())) {
			cri.where().andEquals("cost_time", obj.getCostTime());
		}
		if(StringTool.isNotNull(obj.getRushCount())) {
			cri.where().andEquals("rush_count", obj.getRushCount());
		}
		if(StringTool.isNotNull(obj.getRightQuantity())) {
			cri.where().andEquals("right_quantity", obj.getRightQuantity());
		}
		if(StringTool.isNotNull(obj.getRemainQues())) {
			cri.where().andEquals("remain_ques", obj.getRemainQues());
		}
		if(StringTool.isNotNull(obj.getFaultQuantity())) {
			cri.where().andEquals("fault_quantity", obj.getFaultQuantity());
		}
		if(StringTool.isNotNull(obj.getRightQuestionId())) {
			cri.where().andEquals("right_question_id", obj.getRightQuestionId());
		}
		if(StringTool.isNotNull(obj.getFaultQuestionId())) {
			cri.where().andEquals("fault_question_id", obj.getFaultQuestionId());
		}
		if(StringTool.isNotNull(obj.getFaultQuestionIds())) {
			cri.where().andEquals("fault_question_ids", obj.getFaultQuestionIds());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getState())) {
			cri.where().andEquals("state", obj.getState());
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
        cri.desc("crt_time");
		return super.queryPage(RushSequenceRecord.class, obj, cri);
	}

	public List<RushSequenceRecord> query(RushSequenceRecord obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSequenceId())) {
			cri.where().andEquals("sequence_id", obj.getSequenceId());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}
		if(StringTool.isNotNull(obj.getRushStartTime())) {
			cri.where().andEquals("rush_start_time", obj.getRushStartTime());
		}
		if(StringTool.isNotNull(obj.getRushEndTime())) {
			cri.where().andEquals("rush_end_time", obj.getRushEndTime());
		}
		if(StringTool.isNotNull(obj.getCostTime())) {
			cri.where().andEquals("cost_time", obj.getCostTime());
		}
		if(StringTool.isNotNull(obj.getRushCount())) {
			cri.where().andEquals("rush_count", obj.getRushCount());
		}
		if(StringTool.isNotNull(obj.getRightQuantity())) {
			cri.where().andEquals("right_quantity", obj.getRightQuantity());
		}
		if(StringTool.isNotNull(obj.getRemainQues())) {
			cri.where().andEquals("remain_ques", obj.getRemainQues());
		}
		if(StringTool.isNotNull(obj.getFaultQuantity())) {
			cri.where().andEquals("fault_quantity", obj.getFaultQuantity());
		}
		if(StringTool.isNotNull(obj.getRightQuestionId())) {
			cri.where().andEquals("right_question_id", obj.getRightQuestionId());
		}
		if(StringTool.isNotNull(obj.getFaultQuestionId())) {
			cri.where().andEquals("fault_question_id", obj.getFaultQuestionId());
		}
		if(StringTool.isNotNull(obj.getFaultQuestionIds())) {
			cri.where().andEquals("fault_question_ids", obj.getFaultQuestionIds());
		}
		if(StringTool.isNotNull(obj.getScore())) {
			cri.where().andEquals("score", obj.getScore());
		}
		if(StringTool.isNotNull(obj.getState())) {
			cri.where().andEquals("state", obj.getState());
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
		cri.desc("rush_end_time");
		return super.query(RushSequenceRecord.class, cri);
	}

	public PageResult<RushSequenceRecord> queryPageBySearch(RushSequenceRecord obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getCompanyName())) {
			cri.where().andLike("company_name", obj.getCompanyName());
		}
		if(StringTool.isNotNull(obj.getSequenceName())) {
			cri.where().andLike("sequence_name", obj.getSequenceName());
		}
		if(StringTool.isNotNull(obj.getUserName())) {
			cri.where().andEquals("user_name", obj.getUserName());
		}

		cri.desc("rush_start_time");

		return super.queryPage(RushSequenceRecord.class, obj, cri);
	}

   	public RushSequenceRecord get(String id) {
		return super.fetch(RushSequenceRecord.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(RushSequenceRecord.class, cri);
		}
		return 0;
	}

	public RushSequenceRecord checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(RushSequenceRecord.class,cri);
	}

	public String getOneMillionRecordTestData() {

		Integer count = 10000000;
		for(int i=1; i <= count; i++) {
			RushSequenceRecord rsr = new RushSequenceRecord();
			rsr.setId(""+i);
			rsr.setSequenceId("1");
			rsr.setSequenceName("钳工");
			rsr.setUserName("test"+i);
			rsr.setRushStartTime(new Date());
			rsr.setRushEndTime(new Date());
			rsr.setRushCount(1);
			rsr.setRightQuantity(10);
			rsr.setRemainQues(0);
			rsr.setFaultQuantity(0);
			rsr.setScore(100.0);
			rsr.setState(1);
			rsr.setCompanyId("680");
			rsr.setCompanyName("机电安装工程部");
			rsr.setCrtIp("127.0.0.1");
			rsr.setCrtTime(new Date());
			rsr.setCrtUser("admin");
			super.insert(rsr);
		}

		return "OK";

	}


}