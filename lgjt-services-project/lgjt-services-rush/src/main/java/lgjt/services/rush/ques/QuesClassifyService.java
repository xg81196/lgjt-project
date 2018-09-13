package lgjt.services.rush.ques;


import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.dao.util.cri.Static;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.utils.CommonUtil;
import lgjt.domain.rush.ques.QuesClassify;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.utils.LoginUtil;

import java.util.*;

@Log4j
@IocBean
public class QuesClassifyService extends BaseService {


	
	/**
	 * 试题征集题库库分类查询
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  List<QuesClassify>
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-30
	 */
	public List<QuesClassify> collectList(QuesClassify obj) {
		SimpleCriteria cri = getCri(obj);
		cri.where().andEquals("state", CommonUtil.STATE_ON).andEquals("isCollect", QuesClassify.ISCOLLECT_ON).andEquals("superId", QuesClassify.SUPER_ID_1);
		return super.query(QuesClassify.class, cri);
	}
	
	public boolean checkName(QuesClassify obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getId())) {
			cri.where().andNotEquals("id", obj.getId());
		}
		cri.where().andEquals("name", obj.getName());
		cri.where().andEquals("superId", obj.getSuperId());
		if(super.fetch(QuesClassify.class, cri) != null) {
			return false;
		}
		return true;
			
	}

    /**
     * @author majinyong
     * @param sauth
     * @return
     */
	public List<QuesClassify> listTree(String sauth) {
		Map<String, List<QuesClassify>> p2son = getP2son(null);
	/*	boolean auth = "true".equals(sauth) ? true : false;
		Set<String> ids = null;
		if (auth) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("userName", UserUtil.getUser().getUserName());
			List<UserClassifyMapping> ucmList = super.query(
					UserClassifyMapping.class, cri);
			Set<String> classifySet = new HashSet<String>();
			if (ucmList != null && ucmList.size() > 0) {
				for (UserClassifyMapping ucm : ucmList) {
					classifySet.add(ucm.getClassifyId());
				}

				ids = new HashSet<String>();
				for (String id : classifySet) {
					ids.add(id);
					ids.addAll(getSubId(id, p2son));
					ids.addAll(getParentId(id, p2son));
				}
			}
		}
		p2son = getP2son(ids);*/
		List<QuesClassify> result = new ArrayList<QuesClassify>();
		if (p2son.containsKey(QuesClassify.ROOT)) {
			for (QuesClassify org : p2son.get(QuesClassify.ROOT)) {
				getSub(org, p2son);
				result.add(org);
			}
		}

		QuesClassify org = getFixedTop();
		org.setList(result);
		List<QuesClassify> list = new ArrayList<QuesClassify>();
		list.add(org);
		return list;
	}

    /**
     * @author majinyong
     * @param sauth
     * @return
     */
	public List<QuesClassify> listTree1(String sauth) {
		Map<String, List<QuesClassify>> p2son = getP2son1(null);
		List<QuesClassify> result = new ArrayList<QuesClassify>();
		if (p2son.containsKey(QuesClassify.ROOT)) {
			for (QuesClassify org : p2son.get(QuesClassify.ROOT)) {
				getSub(org, p2son);
				result.add(org);
			}
		}

		QuesClassify org = getFixedTop();
		org.setList(result);
		List<QuesClassify> list = new ArrayList<QuesClassify>();
		list.add(org);
		return list;
	}

	// 根据当前set集合里的id，找到直接上游节点及所有下游节点；
	private Set<String> getSubId(String id,
			Map<String, List<QuesClassify>> p2son) {
		List<QuesClassify> sons = p2son.get(id);
		Set<String> set = new HashSet<String>();
		set.add(id);
		if (null != sons) {
			for (QuesClassify qc : sons) {
				set.addAll(getSubId(qc.getId(), p2son));
			}
		}
		return set;
	}

	private Set<String> getParentId(String id,
			Map<String, List<QuesClassify>> p2son) {
		Set<String> set = new HashSet<String>();
		for (List<QuesClassify> qcList : p2son.values()) {
			for (QuesClassify qc : qcList) {
				if (qc.getId().equals(id)) {
					set.addAll(getParentId(qc.getSuperId(), p2son));
					set.add(qc.getSuperId());
				}
			}
		}
		return set;
	}


    /**
     * @author majinyong
     * @return
     */
	private QuesClassify getFixedTop() {
		QuesClassify org = new QuesClassify();
		org.setId("-1");
		org.setName("题库树");
		return org;
	}

    /**
     * @author majinyong
     * @param org
     * @param p2son
     */
	private void getSub(QuesClassify org, Map<String, List<QuesClassify>> p2son) {
		if (p2son.containsKey(org.getId())) {
			for (QuesClassify obj : p2son.get(org.getId())) {
				getSub(obj, p2son);
			}
			org.setList(p2son.get(org.getId()));
		}
	}

    /**
     * @author majinyong
     * @param ids
     * @return
     */
	public Map<String, List<QuesClassify>> getP2son(Set<String> ids) {

		SimpleCriteria cri = Cnd.cri();


		String companyId = LoginUtil.getUserLoginInfo().getInfos().get("orgId");
		Integer accountType = 1;


		/*if(null != ids && ids.size()>0) {
			cri.where().andIn("id", ids.toArray(new String[0]));
		}*/
		//企业
		if (2 == accountType) {
			cri.where().andEquals("companyId",companyId).orEquals("name","企业题库");
			//老师
//		} else  if (1 == accountType) {
//			if (/*!"超级管理员".equals(user.getUserRealName().trim()) && */!"admin".equals(user.getUserName().trim())){
//				cri.where().andEquals("companyId","").orIsNull("companyId");
//			}
//			//cri.where().andIsNull("companyId");

		}

		cri.where().andIn("state", CommonUtil.STATE_ON);
		cri.getOrderBy().asc("crt_time");
		QuesClassify qc=new QuesClassify();
		qc.setPage(-1);
		qc.setPageSize(-1);
		PageResult<QuesClassify> queryPage = super.queryPage(QuesClassify.class, qc, cri);
		List<QuesClassify> list = queryPage.getRows();

		/*List<QuesClassify>  quesList = null;
		List<QuesClassify>  quesListResult = Lists.newArrayList();

		while(CollectionUtils.isNotEmpty(quesList)) {
			SimpleCriteria cri1 = Cnd.cri();
			cri1.where().andEquals("id",list.get(0).getSuperId());
			quesList= super.query(QuesClassify.class,cri1);
			quesListResult.addAll(quesList);
		}*/

//		List<QuesClassify> list = super.query(QuesClassify.class, cri);
		Map<String, List<QuesClassify>> p2son = new HashMap<String, List<QuesClassify>>();

		for (QuesClassify org : list) {
			if (p2son.containsKey(org.getSuperId())) {
				p2son.get(org.getSuperId()).add(org);
			} else {
				List<QuesClassify> sons = new ArrayList<QuesClassify>();
				sons.add(org);
				p2son.put(org.getSuperId(), sons);
			}
		}
		return p2son;
	}

    /**
     * @author majinyong
     * @param ids
     * @return
     */
	public Map<String, List<QuesClassify>> getP2son1(Set<String> ids) {

		SimpleCriteria cri = Cnd.cri();

		Integer accountType = 1;



		//企业
		if (2 == accountType) {
			cri.where().andNotEquals("name","企业题库").andNotEquals("super_id","qiye");

		}
		cri.where().andIn("state", CommonUtil.STATE_ON);
		cri.getOrderBy().asc("crt_time");
		QuesClassify qc=new QuesClassify();
		qc.setPage(-1);
		qc.setPageSize(-1);
		PageResult<QuesClassify> queryPage = super.queryPage(QuesClassify.class, qc, cri);
		List<QuesClassify> list = queryPage.getRows();

		/*List<QuesClassify>  quesList = null;
		List<QuesClassify>  quesListResult = Lists.newArrayList();

		while(CollectionUtils.isNotEmpty(quesList)) {
			SimpleCriteria cri1 = Cnd.cri();
			cri1.where().andEquals("id",list.get(0).getSuperId());
			quesList= super.query(QuesClassify.class,cri1);
			quesListResult.addAll(quesList);
		}*/

//		List<QuesClassify> list = super.query(QuesClassify.class, cri);
		Map<String, List<QuesClassify>> p2son = new HashMap<String, List<QuesClassify>>();

		for (QuesClassify org : list) {
			if (p2son.containsKey(org.getSuperId())) {
				p2son.get(org.getSuperId()).add(org);
			} else {
				List<QuesClassify> sons = new ArrayList<QuesClassify>();
				sons.add(org);
				p2son.put(org.getSuperId(), sons);
			}
		}
		return p2son;
	}


	/**
	 * @author majinyong
	 * @param state
	 * @param name
	 * @return
	 */
	public List<QuesClassify> listTreeAll(Integer state , String name) {

		//select * from ques_classify q left join sys_user u on q.company_id=u.company_id where u.id=''
		Map<String, List<QuesClassify>> p2son = getP2sonAll(state,name);
		/*boolean auth = "true".equals(sauth) ? true : false;
		Set<String> ids = null;
		if (auth) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("userName", UserUtil.getUser().getUserName());
			List<UserClassifyMapping> ucmList = super.query(
					UserClassifyMapping.class, cri);
			Set<String> classifySet = new HashSet<String>();
			if (ucmList != null && ucmList.size() > 0) {
				for (UserClassifyMapping ucm : ucmList) {

					classifySet.add(ucm.getClassifyId());
				}

				ids = new HashSet<String>();
				for (String id : classifySet) {

					ids.add(id);
					ids.addAll(getSubId(id, p2son));
					ids.addAll(getParentId(id, p2son));

				}
			}
		}
		p2son = getP2sonAll(ids);*/
		List<QuesClassify> result = new ArrayList<QuesClassify>();
		if (p2son.containsKey(QuesClassify.ROOT)) {
			for (QuesClassify org : p2son.get(QuesClassify.ROOT)) {
				getSub(org, p2son);
				result.add(org);
			}
		}

		QuesClassify org = getFixedTop();
		org.setList(result);
		List<QuesClassify> list = new ArrayList<QuesClassify>();
		list.add(org);
		return list;
	}

	/**
	 *
	 * @author majinyong
	 * @param state
	 * @param name
	 * @return
	 */
	public Map<String, List<QuesClassify>> getP2sonAll(Integer state,String name) {

		String companyId = LoginUtil.getUserLoginInfo().getInfos().get("orgId");
		Integer accountType = 1;

		SimpleCriteria cri = Cnd.cri();

		if (2 == accountType) {
			cri.where().andEquals("companyId", companyId).orEquals("type", "1").andEquals("super_id", "-1");
			cri.where().andNotEquals("name", "行业通用题库");
//		}
//		else  if (1 == accountType) {
//			if (/*!"超级管理员".equals(user.getUserRealName().trim()) && */!"admin".equals(user.getUserName().trim())){
//				cri.where().andEquals("companyId","").orIsNull("companyId");
//			}

		}


		if(state==0){
			cri.where().andIn("state", CommonUtil.STATE_ON);
		}
		if(StringTool.isNotEmpty(name)) {
			cri.where().andEquals("name", name);
		}
		cri.getOrderBy().asc("crt_time");
		QuesClassify qc=new QuesClassify();
		qc.setPage(-1);
		qc.setPageSize(-1);
		PageResult<QuesClassify> queryPage = super.queryPage(QuesClassify.class, qc, cri);
		List<QuesClassify> list = queryPage.getRows();
//		List<QuesClassify> list = super.query(QuesClassify.class, cri);
		Map<String, List<QuesClassify>> p2son = new HashMap<String, List<QuesClassify>>();
		/*SimpleCriteria cri1 = Cnd.cri();
		for (QuesClassify q : list) {
			cri1.where().andEquals("id", q.getSuperId());
			super.query(QuesClassify.class,)
		}*/

		for (QuesClassify org : list) {
			org.setUserName(LoginUtil.getUserLoginInfo().getUserName());
			if (p2son.containsKey(org.getSuperId())) {
				p2son.get(org.getSuperId()).add(org);
			} else {
				List<QuesClassify> sons = new ArrayList<QuesClassify>();
				sons.add(org);
				p2son.put(org.getSuperId(), sons);
			}
		}
		return p2son;
	}

	public PageResult<QuesClassify> queryPage(QuesClassify obj) {
		return super.queryPage(QuesClassify.class, obj, getCri(obj));
	}

	/**
	 * 静态循环——根据父节点查询所有子节点+父节点
	 * @param parentId
	 * @param result
	 * @author majinyong
	 * @return
	 */
    public String getIdsByParentIds(String parentId, String result) {
        QuesClassify quesClassify = new QuesClassify();
        quesClassify.setSuperId(parentId);
        List<QuesClassify> query = super.query(QuesClassify.class, getCri(quesClassify));
        if(query.size()<1){
            return result;
        } else{
            for (int i=0 ; i<query.size() ; i++ ) {
            	if(i==0){
            		if(StringTool.isEmpty(result)){
            			result = query.get(i).getId()+",";
            		}else{
            			result = result+query.get(i).getId()+",";
            		}
            	}else{
            		result = result+query.get(i).getId()+",";
            	}
                getIdsByParentIds(result+query.get(i).getId(), result);
            }
        }
        return result;
    }
    
	public List<QuesClassify> query(QuesClassify obj) {
		return super.query(QuesClassify.class, getCri(obj));
	}

	public List<QuesClassify> queryLeaf() {
		SimpleCriteria cri0 = Cnd.cri();
		cri0.where().andEquals("state", CommonUtil.STATE_ON);
		List<QuesClassify> list = super.query(QuesClassify.class, cri0);
		Map<String,QuesClassify> id2obj = new HashMap<String,QuesClassify>();
		Map<String,QuesClassify> id2parent = new HashMap<String,QuesClassify>();
		for(QuesClassify qc:list) {
			id2obj.put(qc.getId(), qc);
		} 
		for(QuesClassify qc:list) {
			id2parent.put(qc.getId(), id2obj.get(qc.getSuperId()));
		}
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("state", CommonUtil.STATE_ON);

		cri.where().and(
				new Static("id not in (select super_id from ques_classify)"));
		List<QuesClassify> result =  super.query(QuesClassify.class, cri);
		for(QuesClassify qc:result) {
			String name = getFull(qc,id2parent);
			qc.setName(name);
		}
		return result;
	}
	
	private String getFull(QuesClassify qc,Map<String,QuesClassify> id2parent) {
		
		if(id2parent.get(qc.getId()) == null) {
			return qc.getName();
		}else {
			return getFull(id2parent.get(qc.getId()),id2parent) + "/" + qc.getName();
		}
	}

	public QuesClassify get(String id) {
		return super.fetch(QuesClassify.class, id);
	}
	
	/**
	 * 删除题库下分类
	 * @Description: 
	 * @param:  @param ids
	 * @param:  @return   
	 * @return:  int
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-28
	 */
	public int delete(String ids) {

		if (StringTool.isNotEmpty(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andEquals("superId", ids);
			if (null != super.fetch(QuesClassify.class, cri)) {
				return 0;
			}
			cri = Cnd.cri();
			cri.where().andEquals("classifyId", ids);
			super.delete(QuesQuestions.class, cri);
			return super.delete(QuesClassify.class, ids);
		}
		return 0;

	}

	public QuesClassify checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id", value);
		return super.fetch(QuesClassify.class, cri);
	}

	private SimpleCriteria getCri(QuesClassify obj) {
		SimpleCriteria cri = Cnd.cri();

		if (StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if (StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if (StringTool.isNotNull(obj.getState())) {
			cri.where().andEquals("state", obj.getState());
		}
		if (StringTool.isNotNull(obj.getMark())) {
			cri.where().andEquals("mark", obj.getMark());
		}
		if (StringTool.isNotNull(obj.getIsCollect())) {
			cri.where().andEquals("is_collect", obj.getIsCollect());
		}
		if (StringTool.isNotNull(obj.getRemainsystem())) {
			cri.where().andEquals("remainsystem", obj.getRemainsystem());
		}
		if (StringTool.isNotNull(obj.getCrtUser())) {
			cri.where().andEquals("crt_user", obj.getCrtUser());
		}
		if (StringTool.isNotNull(obj.getCrtTime())) {
			cri.where().andEquals("crt_time", obj.getCrtTime());
		}
		if (StringTool.isNotNull(obj.getCrtIp())) {
			cri.where().andEquals("crt_ip", obj.getCrtIp());
		}
		if (StringTool.isNotNull(obj.getUpdUser())) {
			cri.where().andEquals("upd_user", obj.getUpdUser());
		}
		if (StringTool.isNotNull(obj.getUpdTime())) {
			cri.where().andEquals("upd_time", obj.getUpdTime());
		}
		if (StringTool.isNotNull(obj.getUpdIp())) {
			cri.where().andEquals("upd_ip", obj.getUpdIp());
		}
		return cri;
	}

	public Integer getQuesNumByClassifyId(String id) {
		SimpleCriteria cri = new SimpleCriteria();
		cri.where().andEquals("classify_id", cri);
		QuesClassify qc = super.fetch(QuesClassify.class, cri);
		return qc.getQuesNum();
	}

	
	/**
	 * 根据父节点id，查询子节点下id，并组合拼装ids，返回ids
	 * @author yangleihong
	 * @date 2017-5-9下午2:39:27
	 * @param superId
	 * @param classifyIds
	 * @return
	 */
	public String classifyIds(String superId,String classifyIds) {
		//根据传参superId查询下级结构树id，并对ID做组合处理，组合后返回classifyIds
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(superId)) {
			cri.where().andEquals("super_id", superId);
		}
		List<QuesClassify> classify = super.query(QuesClassify.class, cri);
		if(classify.size()>0){
			for(int i=0 ; i<classify.size() ; i++){
				if(i==(classify.size()-1)){
					classifyIds = classifyIds+","+classify.get(i).getId();
				}else{
					classifyIds = classifyIds+","+classify.get(i).getId()+",";
				}
				classifyIds = this.classifyIds(classify.get(i).getId(),classifyIds);
			}
		}
		return classifyIds ;
	}
	


	
	/**
	 * 闯关选择题库查询。
	 * @author yangleihong
	 * @date 2017-9-4下午12:08:35
	 * @return
	 */
	public List<QuesClassify> queryWithRole(Integer isCollect) {
		//根据用户信息确认数据权限。
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("qc3.state", QuesClassify.STATE_ON);
		cri.where().and("qc3.super_id", "!=", "-1");
		if(isCollect!=null&&isCollect==QuesClassify.ISCOLLECT_ON){
			cri.where().andEquals("qc3.is_collect", QuesClassify.ISCOLLECT_ON);
			
		}
		//admin：全部，
		if(LoginUtil.getUserLoginInfo().getUserName().equals("admin")){
			
		}else
		//平台管理员：平台，
		if("platformAdmin".equals(LoginUtil.getUserLoginInfo().getInfos().get("platformAdmin"))){
			cri.where().andEquals("qc3.company_id", "");
		}else 
		//企业管理员：自己企业的
		if("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))){
			cri.where().andEquals("qc3.company_id", LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
			cri.where().orEquals("qc3.company_id", "");
			cri.where().andNotEquals("qc3.super_id", "-1");
		}else{
			return null;
		}
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
	

	
	/**
	 * 考试选择题库查询。
	 * @author yangleihong
	 * @date 2017-9-4下午12:08:35
	 * @return
	 */
	public List<QuesClassify> queryWithExamRole(Integer isCollect) {
		//根据用户信息确认数据权限。
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("qc3.state", QuesClassify.STATE_ON);
		cri.where().and("qc3.super_id", "!=", "-1");
		if(isCollect!=null&&isCollect==QuesClassify.ISCOLLECT_ON){
			cri.where().andEquals("qc3.is_collect", QuesClassify.ISCOLLECT_ON);
		}
		//admin：全部，
		if(LoginUtil.getUserLoginInfo().getUserName().equals("admin")){
			
		}else 
		//企业管理员：自己企业的
		if("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))){
            cri.where().andEquals("qc3.company_id", LoginUtil.getUserLoginInfo().getInfos().get("companyId"));
            cri.where().orEquals("qc3.company_id", "");
            cri.where().andNotEquals("qc3.super_id", "-1");
		}
		//平台管理员：平台，
		else{
//			cri.where().andEquals("qc3.company_id", "");
		}
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

	

	public List<QuesClassify> query(Integer isCollect) {
		QuesClassify quesClassify = new QuesClassify();
		return super.query(QuesClassify.class, getCri(quesClassify));
	}
}