package lgjt.web.rush.module.admin.ques;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.rush.utils.QuesQuestionUtil;
import lgjt.domain.rush.ques.QuesClassify;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.services.rush.ques.QuesClassifyService;
import lgjt.services.rush.ques.QuesQuestionsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 题库模块（后台）- 通用题库试题管理
 * @Description: 
 * 			1、删除-试题
 * 			2、新增-试题
 * 			3、修改-试题
 * 			4、查询-试题
 * 			5、导入-试题
 * 			6、导出-试题
 * 			7、根据id获取试题信息
 * @author gaolei
 * @date 2017-7-6 上午10:02:32
 */
@At("/admin/ques")
@IocBean
@Log4j
public class AdminQuestionsModule {

    
	@Inject("quesQuestionsService")
	QuesQuestionsService service;
	@Inject
	QuesClassifyService quesClassifyService;

	/**
	 * 删除-试题
	 * @Description: 
	 * @param:  @param ids
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/delete")
	@Authority("")
	public Object delete(@Param("ids") String ids) {
		if(service.delete(ids)>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}
		return Results.parse(Constants.STATE_FAIL);
	}
	
	/**
	 * 新增-试题
	 *    判断添加或更新试题，根据id是否为空。
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/insert")
	@Authority("")
	//@Aop({"sixFieldInterceptor"})
	public Object insert(@Param("..") QuesQuestions obj) {
		String question = obj.getQuestion();
		question = QuesQuestionUtil.encrypt(question);
		if(obj.getType() == 3){
			obj.setAnswer(obj.getAnswer().trim().replaceAll("正确", "对"));
			obj.setAnswer(obj.getAnswer().trim().replaceAll("错误", "错"));
		}

		String quesId = obj.getAttachmentId();
		if (StringTool.isNotEmpty(quesId)){

			String[] quesIds = quesId.split("\\,");
			obj.setAttachmentId(quesIds[0]);
			obj.setFileType(quesIds[1]);
			//SysAttachment sysAttachment = new SysAttachment();
			//sysAttachment.setId(quesIds[0]);
			//sysAttachment.setFileType(Integer.parseInt(quesIds[1]));
			//sysAttachmentService.updateIgnoreNull(sysAttachment);

		}
		obj.setQuestion(question);
		if (StringTool.isNull(obj.getId())) {
			obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setCrtTime(new Date());
			obj.setCrtIp(ClientInfo.getIp());
			service.insert(obj);
			//irService.insertIntegralRecord(IntegralCode.TEA_CREATE_QUESTION,UserUtil.getUser().getUserName());//此处传参为枚举类型；
		} else {
			obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setUpdTime(new Date());
			obj.setUpdIp(ClientInfo.getIp());
			service.updateIgnoreNull(obj);
		}
		return Results.parse(Constants.STATE_SUCCESS);
	}
	
	/**
	 * 编辑-试题
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/update")
	@Authority("")
	//@Aop({"sixFieldInterceptor"})
	public Object update(@Param("..") QuesQuestions obj) {
		String question = obj.getQuestion();
		question = QuesQuestionUtil.encrypt(question);
		if(obj.getType() == 3){
			obj.setAnswer(obj.getAnswer().replaceAll("正确", "对"));
			obj.setAnswer(obj.getAnswer().replaceAll("错误", "错"));
		}

		String quesId = obj.getAttachmentId();

		if (StringTool.isNotEmpty(quesId)){

			String[] quesIds = quesId.split("\\,");
			obj.setAttachmentId(quesIds[0]);
			obj.setFileType(quesIds[1]);
			//SysAttachment sysAttachment = new SysAttachment();
			//sysAttachment.setId(quesIds[0]);
			//sysAttachment.setFileType(Integer.parseInt(quesIds[1]));
			//sysAttachmentService.updateIgnoreNull(sysAttachment);
		}

		obj.setQuestion(question);
		if (StringTool.isNull(obj.getId())) {
			obj.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setCrtTime(new Date());
			obj.setCrtIp(ClientInfo.getIp());
			service.insert(obj);
		} else {
			obj.setUpdUser(LoginUtil.getUserLoginInfo().getUserName());
			obj.setUpdTime(new Date());
			obj.setUpdIp(ClientInfo.getIp());
			service.updateIgnoreNull(obj);
		}
		return Results.parse(Constants.STATE_SUCCESS);
	}
	
	/**
	 * 查询-试题
	 *   根据题库查询试题
	 *   刷新
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/list")
	@Authority("")
	public Object queryPage(@Param("..") QuesQuestions obj) {
		try {
			if(StringTool.isNull(obj.getClassifyId())){
				return Results.parse(Constants.STATE_SUCCESS,null,null);
			}

			/*if(StringTool.isEmpty(user)||StringTool.isEmpty(user.getUserName())){
				return Results.parse(Constants.STATE_FAIL,"查询失败！获取当前用户信息失败！");
			}
			UserClassifyMapping user1 = new UserClassifyMapping();
			user1.setUserName(user.getUserName());
			if(StringTool.isNotNull(obj.getClassifyId())&&!obj.getClassifyId().equals("-1")){
				user1.setClassifyId(obj.getClassifyId());
			}
			//判断用户的角色
			String mark = null;
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setUserName(user.getUserName());
			sysUserRole.setRoleId("1");
			List<SysUserRole> list = sysUserRoleService.query(sysUserRole);
			if(list.size()>0){
				mark="1";
			}
			//根据用户获取的权限，传递当前用户细信息，查看拥有所有题库id，返回字段classifyId，多个题库ID，","分隔；
			String classifyId = quesClassifyService.queryAndCheckclassifyId(user1,mark);
			if(StringTool.isEmpty(classifyId)){
				return Results.parse(Constants.STATE_FAIL,"查询失败！你没有查询该题库试题的权限！");
			}else{
				obj.setClassifyId(classifyId);
			}*/
			if ("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))) {
                /*SysUser sysUser = new SysUser();
                String result = null;
                sysUser.setCompanyId(UserUtil.getUser().getCompanyId());
                sysUser.setPageSize(-1);
                sysUser.setPage(-1);*/
				QuesClassify  quesClassify = quesClassifyService.fetch(QuesClassify.class,obj.getClassifyId());
               /* PageResult<SysUser> sysUserPageResult = sysUserService.queryPageUser(sysUser,quesClassify.getType());
                for (int i=0 ; i<sysUserPageResult.getRows().size() ; i++ ) {
                    if(i==0){
                        if(StringTool.isEmpty(result)){
                            result = sysUserPageResult.getRows().get(i).getUserName()+",";
                        }else{
                            result = result+sysUserPageResult.getRows().get(i).getUserName()+",";
                        }
                    }else{
                        result = result+sysUserPageResult.getRows().get(i).getUserName()+",";
                    }
                }*/

				if ("1".equals(quesClassify.getType())){
					/*if (sysUserPageResult.getRows() != null  ) {*/
						/*SysUser u = sysUserPageResult.getRows().get(0);*/
						SimpleCriteria cri=Cnd.cri();
						//cri.where().andIn("company_id",u.getCompanyId().split(","));
						List<QuesClassify>  quesClassifyList = quesClassifyService.query(QuesClassify.class,cri);
						StringBuilder sb = new StringBuilder(quesClassifyList.size());
						if ( CollectionUtils.isNotEmpty(quesClassifyList) ) {

							for ( int i=0;i<quesClassifyList.size();i++ ) {

								if ( i == 0) {
									sb.append(quesClassifyList.get(i).getId());
								}else {
									sb.append(",").append(quesClassifyList.get(i).getId());
								}
							}

						}
						obj.setClassifyId(sb.toString());
					/*}*/
				}

                PageResult<QuesQuestions> quesQuestionsPageResult = service.queryPage4Company(obj, null);
                for(QuesQuestions q:quesQuestionsPageResult.getRows()) {
                    q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
                }
                return Results.parse(Constants.STATE_SUCCESS,null,quesQuestionsPageResult);
            }else {
				PageResult<QuesQuestions> result = service.queryPage(obj);
				for(QuesQuestions q:result.getRows()) {
					q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
				}
				return Results.parse(Constants.STATE_SUCCESS,null,result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,"查询失败！");
		}
	}

	/**
	 * 查询-试题
	 *   根据题库查询试题
	 *   考试添加试题时调用
	 * @Description:
	 * @param:  @param obj
	 * @param:  @return
	 * @return:  Object
	 * @throws
	 * @author  gaolei
	 * @date  2017-7-6
	 */
	@At("/list4ExamAddQues")
	@Authority("")
	public Object list4ExamAddQues(@Param("..") QuesQuestions obj) {
		try {
			if(StringTool.isNull(obj.getClassifyId())){
				return Results.parse(Constants.STATE_SUCCESS,null,null);
			}
			//验证数据权限
			//SysUser user = UserUtil.getUser();
			if ("userCompany".equals(LoginUtil.getUserLoginInfo().getInfos().get("userCompany"))) {
				PageResult<QuesQuestions> quesQuestionsPageResult = service.queryPage(obj);
				for(QuesQuestions q:quesQuestionsPageResult.getRows()) {
					q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
				}
				return Results.parse(Constants.STATE_SUCCESS,null,quesQuestionsPageResult);
			}else {
				PageResult<QuesQuestions> result = service.queryPage(obj);
				for(QuesQuestions q:result.getRows()) {
					q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
				}
				return Results.parse(Constants.STATE_SUCCESS,null,result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,"查询失败！");
		}
	}

	/**
	 * 查询-试题
	 *   根据题库查询试题
	 *   刷新
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  Object
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	@At("/listWithCourse")
	@Authority("")
	public Object listWithCourse(@Param("..") QuesQuestions obj) {
		try {
			if(obj!=null&&obj.getPage()==null){
				obj.setPage(1);
			}
			if(obj!=null&&obj.getPageSize()==null){
				obj.setPageSize(30);
			}
			PageResult<QuesQuestions> result = service.queryPageWithCourse(obj);
			for(QuesQuestions q:result.getRows()) {
				q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
			}
			return Results.parse(Constants.STATE_SUCCESS,null,result);
		} catch (Exception e) {
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,"查询失败！");
		}
	}



	
	/**
	 * 导入-试题
	 * @Description:
	 * @param:  @param files
	 * @param:  @param classifyId
	 * @param:  @return
	 * @return:  Object
	 * @throws
	 * @author  gaolei
	 * @date  2017-7-6
	 */
	@At("/upload")
	@Authority("")
	@AdaptBy(type=UploadAdaptor.class,args={"ioc:fileUpload"})
	public Object upload(@Param("file")TempFile files,@Param("classifyId") String classifyId,@Param("type") String type){
		try {
			String username = null;
			/*if(UserUtil.getUser().getAccountType() == 2){
				SysUser sysUser = new SysUser();
				sysUser.setCompanyId(UserUtil.getUser().getCompanyId());
				List<SysUser> sysUsers = sysUserService.query(sysUser);
				for (int i=0 ; i<sysUsers.size() ; i++ ) {
					if(i==0){
						if(StringTool.isEmpty(username)){
							username = sysUsers.get(i).getUserName()+",";
						}else{
							username = username+sysUsers.get(i).getUserName()+",";
						}
					}else{
						username = username + sysUsers.get(i).getUserName()+",";
					}
				}
			}*/
			Object[] result = service.fromFile(files, classifyId, type, username);
			if("".equals(result[2].toString())){
				result[2] = "试题导入成功";
			}
			if(!Boolean.parseBoolean(result[3].toString())){
                return Results.parse(Constants.STATE_FAIL, result[2].toString());
			}

			final String res = result[1].toString();
		/*	final String userName = UserUtil.getUser().getUserName();
			final HttpServletRequest request = Mvcs.getReq();
			EXECUTOR_SERVICE.execute(new Runnable() {
				@Override
				public void run() {
					// 这里是异步处理的，比较耗时的逻辑，比如数据库操作
					for (int i = 0; i <= Integer.parseInt((res)); i++) {
						irService.insertIntegralRecord1(IntegralCode.TEA_CREATE_QUESTION,userName,request);
					}
				}
			});*/

			return Results.parse(Constants.STATE_SUCCESS, result[2].toString(),result);
		} catch (Exception e) {
			log.error(e,e);
			return Results.parse(Constants.STATE_FAIL, "试题导入失败,请到批量下载列表下载错误提示信息");
		}

	}
//	/**
//	 * 敏感词导入接口
//	 * @author
//	 * @date
//	 * @param f
//	 */
//	@At("/upload")
//	@Authority("")
//	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
//	public Object upload(@Param("file") TempFile file){
//		try{
//
//			if(file.getFile().getName().endsWith(".xls") || file.getFile().getName().endsWith(".xlsx")){
//				String result=service.insertToSensitivewords(file.getFile());
//				if(StringTool.isEmpty(result)){
//					return TTSXResults.parse(Constants.STATE_SUCCESS,"导入数据成功！");
//				}else{
//					return TTSXResults.parse(Constants.STATE_FAIL,result);
//				}
//			}else{
//				return TTSXResults.parse(Constants.STATE_FAIL,"只能上传.xls或.xlsx格式的文件！请确认后再次上传.");
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			return TTSXResults.parse(Constants.STATE_FAIL,e.getMessage());
//		}
//	}


		/**
         * 导出-试题
         * @Description:
         * @param:  @param obj
         * @param:  @return
         * @return:  Object
         * @throws
         * @author  gaolei
         * @date  2017-7-17
         */
/*	@At("/downloadFile")
	@Authority("")
	@Ok("raw")
	public Object downloadFile(@Param("..")QuesQuestions obj ) {
		if(obj.getPage()==null){
			obj.setPage(-1);
		}
		if(obj.getPageSize()==null){
			obj.setPageSize(-1);
		}
		try{
			if(StringTool.isNull(obj.getClassifyId())){
				return null;
			}
//			PageResult<QuesQuestions> pr = queryQuestions(obj);
			PageResult<QuesQuestions> pr = service.queryPage(obj);
			//循环遍历文件  解密
			for (QuesQuestions qq : pr.getRows()) {
				String queetion= QuesQuestionUtil.decrypt(qq.getQuestion());
				qq.setQuestion(queetion);
			}
			String templateName = QuestionUtil.getTemplateName(9);
			ExportQuestionUtil exportQuestionUtil=new ExportQuestionUtil();
			File f = exportQuestionUtil.exportQuestions( templateName,pr.getRows(),9);
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmss");
			//查询分类名称
			SimpleCriteria cri=Cnd.cri();
			cri.where().andEquals("id", obj.getClassifyId());
			QuesClassify fetch = quesClassifyService.fetch(QuesClassify.class, cri);
			String fileName=null;
			if(fetch!=null){
			fileName =fetch.getName()+templateName.substring(templateName.lastIndexOf("."));	
			}else{
				fileName = df.format(new Date())+templateName.substring(templateName.lastIndexOf("."));	
			}
			
			Mvcs.getResp().setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
			return f;
		}catch(Exception e){
			log.error("AdminQuestionModule.export error  ", e);
			e.printStackTrace();
		}
		return null;
	}*/

    /**
     * 根据要求查询试题--用于下载
     * @param 
     * @result PageResult<QuesQuestions>
     * @author lilei
     * @version 
     * @date 下午2:08:02
     *
     */
	/*private PageResult<QuesQuestions> queryQuestions(QuesQuestions obj) {

		try {
			//验证数据权限
			SysUser user = UserUtil.getUser();
			if(StringTool.isNull(user)||StringTool.isNull(user.getUserName())){
				return null;
			}
			UserClassifyMapping user1 = new UserClassifyMapping();
			user1.setUserName(user.getUserName());
			if(StringTool.isNotNull(obj.getClassifyId())&&!obj.getClassifyId().equals("-1")){
				user1.setClassifyId(obj.getClassifyId());
			}
			//判断用户的角色
			String mark = null;
			SysUserRole userRole = new SysUserRole();
			userRole.setUserName(user.getUserName());
			userRole.setRoleId("1");
			List<SysUserRole> userRoleMapping = sysUserRoleService.query(userRole);
			if(userRoleMapping.size()>0){
				mark="1";
			}
			//根据用户获取的权限，传递当前用户细信息，查看拥有所有题库id，返回字段classifyId，多个题库ID，","分隔；
			String classifyId = quesClassifyService.queryAndCheckclassifyId(user1,mark);
			List<String> fids=new ArrayList<String>();
			for (int i = 0; i < classifyId.split(",").length; i++) {
				fids.add(classifyId.split(",")[i]);
			}
//			System.out.println(fids.toString());
			if(StringTool.isNull(classifyId)){
				return null;
			}else{
				obj.setClassifyId(classifyId);
			}
			PageResult<QuesQuestions> result = service.queryPageForDownload(obj);
			for(QuesQuestions q:result.getRows()) {
				q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}*/
	
	/**
	 * 根据id，获取试题信息，——课程——练习题接口。
	 * @author yangleihong
	 * @date 2017-7-10上午11:23:36
	 * @param id（必传项）
	 * @param type（如果type=5，则为最后一道题，学习记录表插入一条数据。代表练习题已经学习完毕。）
	 * @return
	 */
	@At("/get")
	@Authority("")
	public Object get(@Param("id")String id , @Param("type") Integer type, @Param("classify_id") String classifyId, @Param("difficulty_id") String difficultyId){
		try {
			if(StringTool.isNull(id)){
				return Results.parse(Constants.STATE_FAIL, "获取练习题失败！传参不得为空！");
			}
			if(LoginUtil.getUserLoginInfo() == null){
				return Results.parse(Constants.STATE_FAIL, "获取练习题失败！获取用户登录信息失败！");
			}
			QuesQuestions qus = new QuesQuestions();
			qus.setId(id);
			PageResult<QuesQuestions> result = service.queryPageWithID(qus);
			if(result.getTotal()!=1){
				return Results.parse(Constants.STATE_FAIL, "获取练习题失败！没有对应的练习题！");
			}else{
				for(QuesQuestions q:result.getRows()) {
					q.setQuestion(QuesQuestionUtil.decrypt(q.getQuestion()));
				}
				qus=result.getRows().get(0);
				//特殊判断判断，如果type=5，则为最后一道题，学习记录表更新学习记录。代表练习题已经学习完毕。
				//需要和张陇平联调学习
				if(type != null && type == 5 && StringTool.isNotNull(classifyId) && StringTool.isNotNull(difficultyId)){
					//更新练习记录
//					LearnCourseRecord learnCourseRecord = new LearnCourseRecord();
//					learnCourseRecord.setUserId(UserUtil.getUser().getId());//用户id
//					learnCourseRecord.setCourseId(classifyId);//课程id
//					learnCourseRecord.setPartId(difficultyId);//节点id
//					PageResult<LearnCourseRecord> learn= learnCourseRecordService.queryPageSelectorLearnCourseRecord(learnCourseRecord);
//					if(learn.getTotal()!=1){
//						return Results.parse(Constants.STATE_FAIL, "获取练习题失败！更新学习记录失败！");
//					}else{
//						learnCourseRecord = learn.getRows().get(0);
//						learnCourseRecord.setState(learnCourseRecord.STATE_1);
//						learnCourseRecord.setUpdIp(ClientInfo.getIp());
//						learnCourseRecord.setUpdUser(UserUtil.getUser().getUserName());
//						learnCourseRecord.setUpdTime(new Date());
//						learnCourseRecordService.updateIgnoreNull(learnCourseRecord);
//					}
				}
				return Results.parse(Constants.STATE_SUCCESS, null,qus);
			}
		} catch (Exception e) {
			log.error(e,e);
			return Results.parse(Constants.STATE_FAIL, "获取练习题失败！");
		}
		
	}


	/**
	 * 试题模板下载接口
	 * @Description:
	 * @param:  @return
	 * @return:  Object
	 * @throws
	 * @author  gaolei
	 * @date  2017-7-11
	 */

	@At("/downloadFile")
	@Ok("raw")
	public Object downloadFile() {
		try {
			HttpServletRequest request = Mvcs.getReq();
			HttpServletResponse resp = Mvcs.getResp();
//			PropertyUtil.class.getResourceAsStream("/conf.properties");
//			String filePath = request.getServletContext().getRealPath("/quesTemplate/quesTemplate.xls");

			InputStream is =	AdminQuestionsModule.class.getResourceAsStream("/quesTemplate.xls");
			resp.setHeader("Content-Disposition","attachment; filename=" + java.net.URLEncoder.encode("试题模板.xls", "UTF-8"));
//			File dest = new File(filePath);
			return is;
		} catch (Exception e) {
			log.error("upload img file error");
			return null;
		}
	}

}