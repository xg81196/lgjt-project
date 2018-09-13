package lgjt.services.moments.challenge;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lgjt.domain.moments.challenge.LgFileInfo;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;

@Log4j
@IocBean
public class LgFileInfoService extends BaseService {


	public PageResult<LgFileInfo> queryPage(LgFileInfo obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getSourceName())) {
			cri.where().andEquals("source_name", obj.getSourceName());
		}
		if(StringTool.isNotNull(obj.getSourceFilePath())) {
			cri.where().andEquals("source_file_path", obj.getSourceFilePath());
		}
		if(StringTool.isNotNull(obj.getFileType())) {
			cri.where().andEquals("file_type", obj.getFileType());
		}
		if(StringTool.isNotNull(obj.getSourceFileSize())) {
			cri.where().andEquals("source_file_size", obj.getSourceFileSize());
		}
		if(StringTool.isNotNull(obj.getCoverId())) {
			cri.where().andEquals("cover_id", obj.getCoverId());
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

		return super.queryPage(LgFileInfo.class, obj, cri);
	}

	public List<LgFileInfo> query(LgFileInfo obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getSourceName())) {
			cri.where().andEquals("source_name", obj.getSourceName());
		}
		if(StringTool.isNotNull(obj.getSourceFilePath())) {
			cri.where().andEquals("source_file_path", obj.getSourceFilePath());
		}
		if(StringTool.isNotNull(obj.getFileType())) {
			cri.where().andEquals("file_type", obj.getFileType());
		}
		if(StringTool.isNotNull(obj.getSourceFileSize())) {
			cri.where().andEquals("source_file_size", obj.getSourceFileSize());
		}
		if(StringTool.isNotNull(obj.getCoverId())) {
			cri.where().andEquals("cover_id", obj.getCoverId());
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
		return super.query(LgFileInfo.class, cri);
	}

   	public LgFileInfo get(String id) {
		return super.fetch(LgFileInfo.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(LgFileInfo.class, cri);
		}
		return 0;
	}

	public LgFileInfo checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(LgFileInfo.class,cri);
	}

	/**
	 * 将附件ID插入附件表
	 * @param json
	 * @return
	 */
	public Integer insertFile(String json, String type, String challengeId) {
		JSONArray jsonArray = JSON.parseArray(json);
		int count = 0;
		for(int i = 0; i < jsonArray.size(); i++) {
			JSONObject obj = jsonArray.getJSONObject(i);
			LgFileInfo lgFileInfo = new LgFileInfo();
			lgFileInfo.setFileType(Integer.valueOf(type));
			lgFileInfo.setSourceName((String)obj.get("fileName"));
			lgFileInfo.setSourceFilePath((String)obj.get("id"));
			lgFileInfo.setExtend1(challengeId);
			lgFileInfo.setCrtTime(new Date());
			super.insert(lgFileInfo);
			count++;
		}
		return count;
	}

	/**
	 * 获取附件Map
	 * map里面包括 attachId和type类型
	 * @param challengeId
	 * @return
	 */
	public Map<String, String> getResourceMap(String challengeId) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("extend1",challengeId);
		List<LgFileInfo> lgFileInfos = super.query(LgFileInfo.class, cri);

		HashMap<String, String> result = new HashMap<String, String>();
		if(lgFileInfos.size() > 0) {
			StringBuffer attachId = new StringBuffer("[");
			for(int i=0; i<lgFileInfos.size(); i++) {
				if(i==0) {
					attachId.append("{");
				} else {
					attachId.append(",{");
				}
				attachId.append("\"fileName\":\""+lgFileInfos.get(i).getSourceName()+"\",");
				attachId.append("\"id\":\""+lgFileInfos.get(i).getSourceFilePath()+"\"");
				attachId.append("}");
			}
			attachId.append("]");
			result.put("attachId", attachId.toString());
			result.put("type", String.valueOf(lgFileInfos.get(0).getFileType()));
		} else {
			result.put("attachId", "");
			result.put("type", "");
		}
		return result;
	}

}