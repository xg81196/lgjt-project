package lgjt.web.rush.module.admin.ques;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.BaseModule;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.rush.ques.QuesClassify;
import lgjt.domain.rush.ques.QuesCollect;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.services.rush.ques.QuesClassifyService;
import lgjt.services.rush.ques.QuesQuestionsService;

import java.util.Date;
import java.util.List;

/**
 * 题库模块（后台）- 行业通用题库分类管理
 * @Description: 
 * 			1、删除-题库分类	 
 * 			2、新增or修改-题库分类	
 * 			3、查询-公用结构树 
 * 			4、查询-公用结构树-显示禁用
 * @author gaolei
 * @date 2017-7-4 下午10:13:22
 */
@At("/admin/quesClassify")
@IocBean
@Log4j
public class AdminQuesClassifyModule extends BaseModule{

	@Inject("quesClassifyService")
	QuesClassifyService service;
	
	@Inject("quesQuestionsService")
	QuesQuestionsService service2;

	/**
	 * 新增or修改-题库分类
	 *   根据id是否为空
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-5
	 */
	@At("/insertUpdate")
	@lgjt.common.base.Authority("RUSH")
    //@Aop({"sixFieldInterceptor"})
	public Object insertUpdate(@Param("..") QuesClassify obj) {
		try {
			obj.setName(obj.getName().trim());
			if(!service.checkName(obj)) {
				return Results.parse(Constants.STATE_FAIL,"同级题库名称不能重复");
			}
			if(StringTool.isNull(obj.getId())) {
				obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
				obj.setCrtTime(new Date());
				obj.setCrtIp(ClientInfo.getIp());
				obj.setIsCollect(1);
				service.insert(obj);
			}else {
				obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
				obj.setUpdTime(new Date());
				obj.setUpdIp(ClientInfo.getIp());
				obj.setIsCollect(1);
				service.update(obj);
			}
			return Results.parse(Constants.STATE_SUCCESS,null,obj);
		}catch(Exception e) {
			return Results.parse(Constants.STATE_FAIL,"操作失败");
		}
		
		
	}
	
	/**
	 * 删除-题库分类
	 * @Description: 
	 * @param:  @param id
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-5
	 */
	@At("/delete")
	@lgjt.common.base.Authority("RUSH")
	public Object delete(@Param("id")String id) {
		if(id == null){
			return Results.parse(Constants.STATE_FAIL);
		}
		//判断题库下有没有试题,如果有试题不可以删除,如果试题征集里有试题也不可以删除
		SimpleCriteria cri = Cnd.cri();
		 cri.where().andEquals("classifyId", id);
		 List<QuesQuestions> list = service.query(QuesQuestions.class, cri);
			if(list.size()>0){
			return Results.parse(Constants.STATE_FAIL,"该题库下有试题，禁止删除!");	
			}
	/*	 List<QuesCollect> list1 = service.query(QuesCollect.class, cri);
			if(list1.size()>0){
			return Results.parse(Constants.STATE_FAIL,"该题库下有试题征集试题，禁止删除!");	
			}*/
		 if(service.delete(id)>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL,"题库下含有子题库");
		}
	}
	
	/**
	 * 查询-公用结构树
	 * @Description: 
	 * @param:  @param auth
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-4
	 */
	@At("/listTree")
	@lgjt.common.base.Authority("RUSH")
	public Object listTree(@Param("auth") String auth) {
		if("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))){
            List<QuesClassify> list = service.listTree1(auth);
			List<QuesClassify> listTree = service.listTree(auth);
            for (QuesClassify quesClassify : list) {
                for (QuesClassify classify : listTree) {
                    quesClassify.getList().addAll(classify.getList());
                }
            }
		    return Results.parse(Constants.STATE_SUCCESS,null,list);
        }else {
            List<QuesClassify> listTree = service.listTree(auth);
            return Results.parse(Constants.STATE_SUCCESS,null,listTree);
        }
	}
	
	/**
	 * 查询-公用结构树-显示禁用
	 * @Description: 
	 * @param:  @param auth
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-4
	 */
	@At("/listTreeAll")
	@lgjt.common.base.Authority("RUSH")
	public Object listTreeAll(@Param("state") Integer state,@Param("name") String name) {
		if(state==null){
			state = 1;
		}
		List<QuesClassify> listTreeAll = service.listTreeAll(state,name);
		return Results.parse(Constants.STATE_SUCCESS,null,listTreeAll);
	}
}