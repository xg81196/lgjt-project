package lgjt.services.rush.ques;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.upload.TempFile;
import lgjt.common.base.utils.FileUtil;
import lgjt.domain.rush.utils.QuesQuestionUtil;
import lgjt.common.base.utils.ResultExceltLoginInfo;
import lgjt.domain.rush.ques.QuesDiff;
import lgjt.domain.rush.ques.QuesQuestions;
import lgjt.domain.rush.utils.LoginUtil;
import lgjt.domain.rush.utils.QuesUtil;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

@Log4j
@IocBean
public class QuesQuestionsService extends BaseService {
	public static final String QUEST_LIST = "ques.question.list";
	public static final String QUEST_LIST_ATTACHMENT = "ques.question.list.attachment";
	public static final String QUEST_LIST_Download = "ques.question.listForDownload";

	public static final String RUSH_GETQUESTION = "rush.rush.getQuestions";


	private static final Logger logger = Logger.getLogger(QuesQuestionsService.class);



	/**
	 * 查询试题分页
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  PageResult<QuesQuestions>
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	public PageResult<QuesQuestions> queryPage(QuesQuestions obj) {
		SimpleCriteria cri = getCri(obj);
		cri.getOrderBy().desc("crt_time");
		return super.queryPage(QUEST_LIST_ATTACHMENT,QuesQuestions.class, obj, cri);
	}
    /**
     * 查询-试题(企业管理员)
     *   根据题库查询试题
     *   刷新
     * @Description:
     * @param:  @param obj
     * @param:  @return
     * @return:  Object
     * @throws
     * @author  majinyong
     * @date  2017-7-6
     */
	public PageResult<QuesQuestions> queryPage4Company(QuesQuestions obj, String result) {
        String[] id = new String[0];
        if (result != null) {
            id = result.split(",");
        }
	    SimpleCriteria cri = getCri(obj);
		cri.where().andIn("qq.crt_user", id);
		cri.getOrderBy().desc("crt_time");
		return super.queryPage(QUEST_LIST_ATTACHMENT,QuesQuestions.class, obj, cri);
	}
	/**
	 * 查询试题分页
	 * @Description: 
	 * @param:  @param obj
	 * @param:  @return   
	 * @return:  PageResult<QuesQuestions>
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	public PageResult<QuesQuestions> queryPageWithCourse(QuesQuestions obj) {
		SimpleCriteria cri = getCri(obj);
		cri.where().andNotEquals("type", 5);
		cri.getOrderBy().desc("crt_time");
		return super.queryPage(QUEST_LIST,QuesQuestions.class, obj, cri);
	}
	
	public PageResult<QuesQuestions> queryPageWithID (QuesQuestions obj) {
		SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("qq.id", obj.getId());
		return super.queryPage(QUEST_LIST,QuesQuestions.class, obj, cri);
	}
	public List<QuesQuestions> query(QuesQuestions obj) {
		return super.query(QuesQuestions.class, getCri(obj));
	}

	public PageResult<QuesQuestions> queryPageForDownload(QuesQuestions obj) {
		SimpleCriteria cri = getCri(obj);
		return super.queryPage(QUEST_LIST_Download,QuesQuestions.class, obj, cri);
	}
	
   	public QuesQuestions get(String id) {
		return super.fetch(QuesQuestions.class, id);
	}
   	
   	/**
   	 * 删除-试题
   	 *   单个删除和批量删除，多个id以逗号隔开
   	 * @Description: 
   	 * @param:  @param ids
   	 * @param:  @return   
   	 * @return:  int
   	 * @throws
   	 * @author  gaolei 
   	 * @date  2017-7-6
   	 */
	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(QuesQuestions.class, cri);
		}
		return 0;
	}

	/**
	 * 试题导入
	 * @Description: 
	 * @param:  @param file
	 * @param:  @param classifyId
	 * @param:  @return   
	 * @return:  int[]
	 * @throws
	 * @author  gaolei 
	 * @date  2017-7-6
	 */
	public Object[] fromFile(TempFile file,String classifyId,String type,String username) {
		List<QuesDiff> diffList = super.query(QuesDiff.class, Cnd.cri());
		Map<String,String> map = new HashMap<String, String>();
		for(QuesDiff diff:diffList) {
			map.put(diff.getName(), diff.getId());
		}

        int listCount = 0;
		if(username != null){
            QuesQuestions quesQuestions = new QuesQuestions();
            listCount = queryPage4Company(quesQuestions, username).getTotal();
        }
        List<QuesQuestions> list = new ArrayList<>();
        String msg = "";
		try {
			list = readExcel(getWorkbookByExcelType(file.getFile()),file.getFile(), classifyId , map);
		} catch (Exception e) {
			log.error("上传文件失败，错误原因：" + e, e);
		}
		int suc=0;
        if(list != null && list.size() == 0){
            Object[] result = new Object[4];
            msg = "导入文件信息为空或导入试题有误";
            result[2] = msg;
            result[3] = false;
            return result;
        }

        if (CollectionUtils.isNotEmpty(list)) {
	/*		for(QuesQuestions q:list) {
				q.setClassifyId(classifyId);
//			System.out.println(suc+"-------------------");
				*//*if(!quesEqual(q)) {*//*
					try {
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId(diffList.get(0).getId());
						}
						//如果类型是判断题，正确答案
						if(q.getType()==3){
							JSONObject json=JSONObject.parseObject(q.getAnswer());
							String string = json.get("answer").toString();
							String string2 = string.substring(2, string.length()-2);
							List<String> list3=new ArrayList<String>();
							if(string2.equals("对") || string2.equals("正确")){
								list3.add("对");
								json.put("answer", list3);
							}else{
								list3.add("错");
								json.put("answer", list3);
							}
							q.setAnswer(json.toString());
						}
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(UserUtil.getUser().getUserName());
						//super.insert(q);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//suc +=1;
//				System.out.println(suc);
				*//*}else {
					Object[] result = new Object[4];
					msg = "试题导入失败,试题重复";
					result[2] = msg;
					result[3] = false;
					return result;
				}*//*
			}*/
			super.dao.fastInsert(list);
		}else {
			Object[] result = new Object[4];
			msg = "试题导入失败,请到批量下载列表下载错误提示信息";
			result[2] = msg;
			result[3] = false;
			return result;
		}

		Object[] result = new Object[4];
		result[0] = list != null ?list.size():0;
		result[1] = suc;
		result[2] = msg;
		result[3] = true;
		return result;
	}


	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";

	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY= "";


	/**
	 * 读取 Excel file
	 *
	 * @param file
	 *            Excel file
	 * @return
	 * @throws IOException
	 */
	public Workbook getWorkbookByExcelType(File file) throws IOException {
		if ( file == null ) {
			return null;
		} else {
			Workbook wb = null;
			try {
				FileInputStream in = new FileInputStream(file);
				String postfix = Files.getFileExtension(file.getPath());
				if (!EMPTY.equals(postfix)) {
					if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
						POIFSFileSystem poi = new POIFSFileSystem(in);
						return  new HSSFWorkbook(poi);
					} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
						return  new XSSFWorkbook(in);
					}
				} else {
					throw new RuntimeException("不支持导入的格式 "+postfix) ;
				}
			} catch (Exception e) {
				throw new RuntimeException("读取失败",e) ;
			}finally{
				if (null != wb){
					try {
						wb.close();
					} catch (IOException e) {
					}
				}

			}

		}
		return null;
	}



	/**
	 * 上传文件，返回导入的试题
	 * @param file
	 * @param diffMap
	 * @author wuguangwei
	 * @return
	 */
	private List<QuesQuestions> readExcel ( Workbook wb ,File file,String classifyId , Map<String,String> diffMap ) {

		Map<String,List<Map<String, Object>>>  map = Maps.newLinkedHashMapWithExpectedSize(5);

		List<Map<String, Object>> positions = Lists.newArrayList();

		List<QuesQuestions> questions = Lists.newArrayList();

		try {
			// 把 File 对象解析为一个 Workbook 对象
	/*		FileInputStream in = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(in);*/
			// 得到 name="xxx" 的 Sheet 对象
			String[] sheets = { QuesQuestions.TYPE_1, QuesQuestions.TYPE_2, QuesQuestions.TYPE_3, QuesQuestions.TYPE_4, QuesQuestions.TYPE_5 };
			for (int i = 0; i < sheets.length; i++) {
				Sheet sheet = wb.getSheet(sheets[i]);
				if(sheet==null){
					continue;
				}
				// 得到位置集合
				positions = checkExcel(sheet , classifyId);
				// 若存在错误，则放到map中去
				if (CollectionUtils.isNotEmpty(positions)) {
					map.put(sheets[i]+"批量导入出错返回", positions);
				}
			}

			if (MapUtils.isNotEmpty(map))  {
				try {
					//保存错误信息
					saveFailExcel(map);
				} catch (Exception e) {
					log.error("保存出错信息失败，错误原因：" + e, e);
				}

				return null;
			}

			 questions = QuesUtil.fromTemplate2(file, classifyId, diffMap);

			if (questions == null || questions.size() == 0) {
				questions = Lists.newArrayList();
				return questions;
			}


		} catch (Exception e) {
			log.error("上传文件失败，错误原因：" + e, e);
		}

		return questions;
	}


	/**
	 * 得到所有的错误位置集合
	 *
	 * @param sheet
	 * @return List<Map<String, Object>>
	 * @author wuguangwei
	 */
	private List<Map<String, Object>>  checkExcel(Sheet sheet ,String classifyId) {

		StringBuilder sb = new StringBuilder();


		//用于文件内去重
		List<String> choiceques = Lists.newArrayList();
		List<String> multiChoiceques = Lists.newArrayList();
		List<String> checkques = Lists.newArrayList();
		List<String> fillques = Lists.newArrayList();
		List<String> answerques = Lists.newArrayList();

		List<Map<String, Object>> choiceInfoList = new ArrayList<>();
		List<Map<String, Object>> multiChoiceInfoList = new ArrayList<>();
		List<Map<String, Object>> checkInfoList = new ArrayList<>();
		List<Map<String, Object>> fillInfoList = new ArrayList<>();
		List<Map<String, Object>> answerInfoList = new ArrayList<>();

		String sheetName = sheet.getSheetName();

			if (QuesQuestions.TYPE_1.equals(sheetName)) {
				parseChoice(sheetName,sheet,sb,choiceques,choiceInfoList ,classifyId);
			}else if(QuesQuestions.TYPE_2.equals(sheetName)){
				parseChoice(sheetName,sheet,sb,multiChoiceques,multiChoiceInfoList ,classifyId);
			}else if(QuesQuestions.TYPE_3.equals(sheetName)){
				parseCheck(sheet,sb,checkques,checkInfoList ,classifyId);
			}else if(QuesQuestions.TYPE_4.equals(sheetName)){
				parseFill(sheet,sb,fillques,fillInfoList ,classifyId);
			}else if(QuesQuestions.TYPE_5.equals(sheetName)){
				parseAnswer(sheet,sb,answerques,answerInfoList ,classifyId);
			}



		// 不为空则创建导入失败的文件
		if (!CollectionUtils.isEmpty(choiceInfoList)) {
			return choiceInfoList;
		}
		if (!CollectionUtils.isEmpty(multiChoiceInfoList)) {
			return multiChoiceInfoList;
		}

		if (!CollectionUtils.isEmpty(checkInfoList)) {
			return checkInfoList;
		}

		if (!CollectionUtils.isEmpty(fillInfoList)) {
			return fillInfoList;
		}

		if (!CollectionUtils.isEmpty(answerInfoList)) {
			return answerInfoList;
		}

		return null;

	}




	/**
	 * 验证简答题
	 *
	 * @param errorMsg
	 * @param list
	 * @param sheet
	 * @return_type void
	 * @author wuguangwei
	 */
	private void parseAnswer(Sheet sheet,StringBuilder errorMsg ,List<String> list ,List<Map<String, Object>> infoList ,String classifyId) {

		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(firstRowIndex);

		// 读不到模板表头直接报错，不在记录错误信息
		if (firstRow == null) {
			// 记录日志
			addLogAndErrorMsg("未成功读取模板表头行", errorMsg);
			addWrongMap("无", "无", "未成功读取模板表头行", infoList);
			return ;
		}
		Cell cell = firstRow.getCell(1);
		Object cellVal = getCellValue(cell);
		//判断表头是否正确
		if(firstRow.getPhysicalNumberOfCells() != 5)
		{
			// 记录日志
			addLogAndErrorMsg("表头的数量不对,请使用模版进行导入", errorMsg);
			addWrongMap("无", "无", "表头的数量不对,请使用模版进行导入", infoList);

			return ;
		}

		int countBlank = 0;

		int count = 0;

		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

			Row row = sheet.getRow(rIndex);
			//判断第一行是否有数据


			if (row != null) {
				// 验证第2列
				cell = row.getCell(1);
				cellVal = getCellValue(cell);
				int rowNum = row.getRowNum();
				Cell cell1 = row.getCell(2);
				Object cellVal1 = getCellValue(cell1);

					//连续两行为空
					String val = (String)cellVal;
					//String val1 = (String)cellVal1;
					if (StringUtils.isBlank(val)) {
						countBlank++;
						continue;
					}


				if (countBlank > 10) {
					break;
				}

				if (rIndex >= 1) {
					for (int i = rIndex - 1; i >= 0; i--) {

						if ( StringUtils.isBlank(tranferScientificNotation(row,1)) ) {
							addLogAndErrorMsg("题目内容-----" + cellVal
											+ "------在您导入的文件中第--" + (rowNum + 1) + "--为必填项，不能导入",
									errorMsg);
							addWrongMap(rIndex, "文件内题目内容为必填项", "题目内容为空"
											+ "------在您导入的文件中第--" + rIndex + "--行为必填项，不能导入",
									infoList);
							break;
						}
					}
				}

				validateItemContent(rIndex,row,cellVal ,cell, sheet,errorMsg ,infoList,list ,classifyId);

			}else{
				count++;
				if ( count > 10) {
					break;
				}else{
					continue;
				}
			}




		}


	}

	/**
	 * 验证填空题
	 *
	 * @param sheet
	 * @param errorMsg
	 * @param list
	 * @return_type void
	 * @author wuguangwei
	 */
	private void parseFill( Sheet sheet,StringBuilder errorMsg ,List<String> list ,List<Map<String, Object>> fillInfoList ,String classifyId) {


		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(firstRowIndex);

		// 读不到模板表头直接报错，不在记录错误信息
		if (firstRow == null) {
			// 记录日志
			addLogAndErrorMsg("未成功读取模板表头行", errorMsg);
			addWrongMap("无", "无", "未成功读取模板表头行", fillInfoList);

			return ;

		}

		//判断表头是否正确
		if(firstRow.getPhysicalNumberOfCells() != 8)
		{
			// 记录日志
			addLogAndErrorMsg("表头的数量不对,请使用模版进行导入", errorMsg);
			addWrongMap("无", "无", "表头的数量不对,请使用模版进行导入", fillInfoList);

			return ;
		}

		int countBlank = 0;

		int count = 0;

		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

			Row row = sheet.getRow(rIndex);
			//判断第一行是否有数据

			if ( row != null ) {
				// 验证第2列
				Cell cell = row.getCell(1);
				Object cellVal = getCellValue(cell);
				int rowNum = row.getRowNum();
				Cell cell1 = row.getCell(2);
				Object cellVal1 = getCellValue(cell1);

					//连续两行为空
					String val = (String)cellVal;
					//String val1 = (String)cellVal1;
					if (StringUtils.isBlank(val)) {
						countBlank++;
						continue;
					}


				if (countBlank > 10) {
					break;
				}

				if (rIndex >= 1) {
					for (int i = rIndex - 1; i >= 0; i--) {

						if (StringUtils.isBlank(tranferScientificNotation(row,1))) {
							addLogAndErrorMsg("题目内容-----" + cellVal
											+ "------在您导入的文件中第--" + (rowNum + 1) + "--为必填项，不能导入",
									errorMsg);
							addWrongMap(rIndex, "文件内题目内容为必填项", "题目内容为空"
											+ "------在您导入的文件中第--" + rIndex + "--行为必填项，不能导入",
									fillInfoList);
							break;
						}
					}
				}

				validateItemContent(rIndex,row,cellVal ,cell, sheet,errorMsg ,fillInfoList,list,classifyId);


			}else {
				count++;
				if ( count > 10) {
					break;
				}else{
					continue;
				}
			}


		}

	}

	/**
	 * 解析判断题，添加错误位置
	 *
	 * @param sheet
	 * @param errorMsg
	 * @param list
	 * @return_type void
	 * @author wuguangwei
	 */
	private void parseCheck(Sheet sheet,StringBuilder errorMsg ,List<String> list ,List<Map<String, Object>> fillInfoList ,String classifyId) {

		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(firstRowIndex);

		// 读不到模板表头直接报错，不在记录错误信息
		if (firstRow == null) {
			// 记录日志
			addLogAndErrorMsg("未成功读取模板表头行", errorMsg);
			addWrongMap("无", "无", "未成功读取模板表头行", fillInfoList);
			return ;
		}

		//判断表头是否正确
		if(firstRow.getPhysicalNumberOfCells() != 5)
		{
			// 记录日志
			addLogAndErrorMsg("表头的数量不对,请使用模版进行导入", errorMsg);
			addWrongMap("无", "无", "表头的数量不对,请使用模版进行导入", fillInfoList);
			return ;

		}

		int countBlank = 0;
		int count = 0;
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

			Row row = sheet.getRow(rIndex);
			//判断第一行是否有数据
			/*if (row == null) {
				break;
			}*/

			if ( row != null ) {

				// 验证第2列
				Cell cell = row.getCell(1);
				Object cellVal = getCellValue(cell);
				int rowNum = row.getRowNum();
				Cell cell1 = row.getCell(2);
				Object cellVal1 = getCellValue(cell1);

					//连续两行为空
					String val = (String)cellVal;
					//String val1 = (String)cellVal1;
					if (StringUtils.isBlank(val) && cellVal1 == null) {
						countBlank++;
						continue;
					}


				if (countBlank > 10) {
					break;
				}

				if (rIndex >= 1) {
					for (int i = rIndex - 1; i >= 0; i--) {

						if (StringUtils.isBlank(tranferScientificNotation(row,1))) {
							addLogAndErrorMsg("题目内容-----" + cellVal
											+ "------在您导入的文件中第--" + (rowNum + 1) + "--为必填项，不能导入",
									errorMsg);
							addWrongMap((rowNum+1), "文件内题目内容为必填项", "题目内容为空"
											+ "------在您导入的文件中第--" + (rowNum+1) + "--行为必填项，不能导入",
									fillInfoList);
							break;
						}
					}
				}

				validateItemContent(rIndex,row,cellVal ,cell, sheet,errorMsg ,fillInfoList,list ,classifyId);

				// 验证第3列
				cell = row.getCell(2);
				//cellVal = getCellValue(cell);
				validateCheckCorrect(rIndex,row,cell,errorMsg ,fillInfoList);
			}else {
				count++;
				if ( count > 10) {
					break;
				}else{
					continue;
				}
			}

		}


	}

	/**
	 * 解析单选或双选，添加错误位置
	 *
	 * @param sheet
	 * @param errorMsg
	 * @param sheetName
	 * @return_type void
	 * @author wuguangwei
	 */
	private void parseChoice(String sheetName ,Sheet sheet ,StringBuilder errorMsg ,List<String> list ,List<Map<String, Object>> choiceinfoList ,String classifyId) {

		int firstRowIndex = sheet.getFirstRowNum();
		int lastRowIndex = sheet.getLastRowNum();

		Row firstRow = sheet.getRow(firstRowIndex);

		// 读不到模板表头直接报错，不在记录错误信息
		if (firstRow == null) {
			// 记录日志
			addLogAndErrorMsg("未成功读取模板表头行", errorMsg);
			addWrongMap("无", "无", "未成功读取模板表头行", choiceinfoList);
			return ;

		}

		//判断表头是否正确
		if(firstRow.getPhysicalNumberOfCells() != 11)
		{
			// 记录日志
			addLogAndErrorMsg("表头的数量不对,请使用模版进行导入", errorMsg);
			addWrongMap("无", "无", "表头的数量不对,请使用模版进行导入", choiceinfoList);

			return ;
			}

		int countBlank = 0;
		int count = 0;
		for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

			Row row = sheet.getRow(rIndex);
			//判断第一行是否有数据
			/*if (row == null) {
				break;
			}*/

			if ( row != null ) {
				// 验证第2列
				Cell cell = row.getCell(1);
				Object cellVal = getCellValue(cell);
				int rowNum = row.getRowNum();
				Cell cell1 = row.getCell(2);
				Object cellVal1 = getCellValue(cell1);

					//连续两行为空
					String cellVal2 = (String)cellVal;
					//String cellVal3 = (String)cellVal1;
					if (StringUtils.isBlank(cellVal2) ) {
						countBlank++;
						continue;
					}

					if (countBlank > 10) {

						break;
					}
			/*	if (cellVal == "" && cellVal1 == "" ) {
					countBlank++;
					//break;
				}*/


				if (rIndex >= 1) {
					for (int i = rIndex - 1; i >= 0; i--) {

						if (StringUtils.isBlank(tranferScientificNotation(row,1))) {
							addLogAndErrorMsg("题目内容-----" + cellVal
											+ "------在您导入的文件中第--" + rIndex + "--为必填项，不能导入",
									errorMsg);
							addWrongMap(rowNum+1, "文件内题目内容为必填项", "题目内容为空"
											+ "------在您导入的文件中第--" + (rowNum+1) + "--行为必填项，不能导入",
									choiceinfoList);
							break;
						}
					}
				}

				validateItemContent(rIndex,row,cellVal ,cell, sheet,errorMsg ,choiceinfoList,list ,classifyId);

				// 验证3~8列
				List<String> answer = new ArrayList<>();//已有的答案，用于验证答案
				char a = 'A';
				for (int i = 2; i < 8; i++) {
					cell = row.getCell(i);
					if(cell == null)
						continue;
					cellVal = getCellValue(cell);
//			flag = validateChoice(cellVal);
//			if (!flag) {
//				positions.add(new String[] { (rowNum + 1) + "", (i + 1) + "" });
//			}
					answer.add(String.valueOf(a));
					a++;
				}

				// 验证9列
				cell = row.getCell(8);
				cellVal = getCellValue(cell);
				if (QuesQuestions.TYPE_1.equals(sheetName)) {
					validateSingleCorrect(rIndex,row,cellVal ,cell,errorMsg ,choiceinfoList, answer);

				} else if (QuesQuestions.TYPE_2.equals(sheetName)) {
					validateMultipleCorrect(rIndex,row,cellVal ,cell,errorMsg ,choiceinfoList, answer);
				}
			}else {
				count++;
				if ( count > 10) {
					break;
				}else{
					continue;
				}

			}


		}


	}

	// 验证判断的正确答案
	private boolean validateCheckCorrect(int rIndex ,Row row,Cell cell,StringBuilder errorMsg ,List<Map<String, Object>> infoList) {

		    int rowNum = row.getRowNum();
			if (rIndex >= 1) {
			for (int i = rIndex - 1; i >= 0; i--) {

				    String str = tranferScientificNotation(row,2);

				    if (!StringUtils.isBlank(str)) {
						List<String> listAnswer = Arrays.asList("正确", "错误");
						if (!listAnswer.contains(str)) {
							addLogAndErrorMsg("判断题正确答案-----" + str
											+ "------在您导入的文件中第--" + rIndex + "--只能为正确或者错误，不能导入",
									errorMsg);
							addWrongMap(rIndex, "文件内判断题正确答案只能为正确或者错误", "判断题正确答案只能为正确或者错误"
											+ "------在您导入的文件中第--" + rIndex + "--行只能为正确或者错误，不能导入",
									infoList);
							break;
						}
					}else {
						addLogAndErrorMsg("判断题正确答案-----" + str
										+ "------在您导入的文件中第--" + rIndex + "--只能为正确或者错误，不能导入",
								errorMsg);
						addWrongMap(rowNum+1, "文件内判断题正确答案只能为正确或者错误", "判断题正确答案只能为正确或者错误,不能为空",
								infoList);
						break;
					}




			}
		}

		return true;
	}

	/*// 验证难度范围((初级、中级、高级、技师))
	private boolean validateDifficulty(Object cellVal) {
		if (cellVal instanceof String) {
			String str = (String) cellVal;
			List<String> list = Arrays.asList("初级", "中级", "高级", "技师");
			if (!list.contains(str)) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}*/

	//多选正确答案(只能是ABCDEFGH这几个字母中的一个或几个，而且必须是大写的，字母间不能有其他字符)
	private boolean validateMultipleCorrect(int rIndex ,Row row ,Object cellVal, Cell cell,StringBuilder sb ,List<Map<String, Object>> infoList, List<String> answer) {

		List<String> list = Arrays.asList("A", "B", "C", "D", "E", "F",
				"G", "H");
		int rowNum = row.getRowNum();
		if (rIndex >= 1) {
			for (int i = rIndex - 1; i >= 0; i--) {
				    String str = tranferScientificNotation(row,8);

				    if (!StringUtils.isBlank(str)) {
						str = str.trim();
						char[] cs = str.toCharArray();
						List<String> cellList = new ArrayList<>();
						for (int j = 0; j < cs.length; j++) {
							cellList.add(String.valueOf(cs[j]));
						}
						if (!list.containsAll(cellList) || (answer.size() > 0 && !answer.containsAll(cellList))) {

							addLogAndErrorMsg("多选题正确答案-----" + cellVal
											+ "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
									sb);
							addWrongMap((rowNum + 1), "文件内多选题正确答案", "验证多选正确答案只能是ABCDEFGH这几个字母中的一个或几个，而且必须是大写的，字母间不能有其他字符-----" + cellVal
											+ "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
									infoList);
							break;
						}
					}else {
						addLogAndErrorMsg("单选正确答案-----" + cellVal
										+ "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
								sb);
						addWrongMap((rowNum + 1), "文件内多选题正确答案", "验证多选题正确答案只能是ABCDEFGH这几个字母中的一个或几个，而且必须是大写的,字母间不能有其他字符,且不能为空-----",
								infoList);
						break;
					}


				}

		}

		return true;
	}

	// 验证单选正确答案(只能是ABCDEFGH这几个字母中的一个，而且必须是大写的)
	private boolean validateSingleCorrect(int rIndex ,Row row ,Object cellVal, Cell cell,StringBuilder sb ,List<Map<String, Object>> infoList, List<String> answer) {

		List<String> list = Arrays.asList("A", "B", "C", "D", "E", "F",
				"G", "H");
		int rowNum = row.getRowNum();
		if (rIndex >= 1) {
			for (int i = rIndex - 1; i >= 0; i--) {

				   String str = tranferScientificNotation(row,8);

				   if (!StringUtils.isBlank(str))  {

					   str = str.trim();

					   if (str.length() != 1 || !list.contains(str) || (answer.size() > 0 && !answer.contains(str))) {
						   addLogAndErrorMsg("单选正确答案-----" + cellVal
										   + "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
								   sb);
						   addWrongMap((rowNum + 1), "文件内单选题正确答案", "验证单选正确答案只能是ABCDEFGH这几个字母中的一个，而且必须是大写的,字母间不能有其他字符-----"
										   ,infoList);
						   break;
					   }
				   }else {
					   addLogAndErrorMsg("单选正确答案-----" + cellVal
									   + "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
							   sb);
					   addWrongMap((rowNum + 1), "文件内单选题正确答案", "验证单选正确答案只能是ABCDEFGH这几个字母中的一个，而且必须是大写的,字母间不能有其他字符,且不能为空-----"
									, infoList);
					   break;
				   }


				}

		}

		return true;
	}


	private boolean validateItemContent(int rIndex ,Row row,Object cellVal, Cell cell, Sheet sheet, StringBuilder sb ,List<Map<String, Object>> infoList , List<String> list ,String classifyId) {
		boolean flag = true;
		int rowNum = row.getRowNum();
		String val = "";
		if (cellVal instanceof  String ) {
			 val = (String)cellVal;
		}else {
			if (cellVal != null ) {
				val = cellVal.toString();
			}
		}


		if (StringUtils.isNotBlank(val)) {
			String question = val;

			question = QuesQuestionUtil.encrypt(question);

			QuesQuestions q = new QuesQuestions();
			q.setClassifyId(classifyId);
			q.setQuestion(question);
			String sheetName = sheet.getSheetName();
			if (QuesQuestions.TYPE_1.equals(sheetName)) {
				q.setType(1);
			}else if(QuesQuestions.TYPE_2.equals(sheetName)){
				q.setType(2);
			}else if(QuesQuestions.TYPE_3.equals(sheetName)){
				q.setType(3);
			}else if(QuesQuestions.TYPE_4.equals(sheetName)){
				q.setType(4);
			}else if(QuesQuestions.TYPE_5.equals(sheetName)){
				q.setType(5);
			}
			boolean isRepeat = quesEqual(q);
		/*	SimpleCriteria cri=Cnd.cri();
			cri.where().andEquals("classify_id", classifyId);
			cri.where().andEquals("question", question);
			QuesQuestions quesQuestions=this.fetch(QuesQuestions.class, cri);*/

			// 题目内容文件内判重
			if (rIndex >= 1) {
				for (int i = rIndex - 1; i >= 0; i--) {
					if (sheet.getRow(i) != null) {

						if (sheet.getRow(i).getCell(1) != null ) {

							if (StringUtils.isNotBlank(tranferScientificNotation(row,1))
									&& sheet.getRow(i).getCell(1).toString()
									.trim().equals(cellVal)) {
								addLogAndErrorMsg("题目内容-----" + cellVal
												+ "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
										sb);
								addWrongMap(rowNum+1, "文件内排重题目内容", "题目内容-----" + cellVal
												+ "------在您导入的文件中第--" + (rowNum+1) + "--行已存在，不能导入",
										infoList);
								break;
							}
						}

					}

				}
			}
			// 题目内容系统判重
			if (rIndex >= 1) {
				for (int i = rIndex - 1; i >= 0; i--) {
					if (StringUtils.isNotBlank(tranferScientificNotation(row,1))
							&& isRepeat && !list.contains(cellVal.toString())) {
						addLogAndErrorMsg("题目内容-----" + cellVal
										+ "------在您导入的文件中第--" + rIndex + "--行已存在，不能导入",
								sb);
						addWrongMap((rowNum+1), "系统排重题目内容", "题目内容-----" + cellVal
										+ "------在您导入的文件中第--" + (rowNum+1) + "--行已存在，不能导入",
								infoList);
						list.add(cellVal.toString());
						break;

					}
				}
			}
		}

		return flag;
	}



	/**
	 * 防止模板里的数据变成科学计数法的处理
	 *
	 * @param row
	 *            模板读取数据所在行
	 * @param sourceCol
	 *            读取数据所在列的索引
	 * @return
	 */
	public static String tranferScientificNotation(Row row, int sourceCol) {
		Cell desCell = row.getCell(sourceCol);
		String desStr = null;
		// 对日期可能被填成数字的情况做处理
		if (desCell != null && !"".equals(desCell)) {
			if (desCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(desCell)) {
					// 如果是date类型则 ，获取该cell的date值
					desStr = HSSFDateUtil
							.getJavaDate(desCell.getNumericCellValue())
							.toString().trim();
				} else { // 纯数字
					DecimalFormat df = new DecimalFormat("0");
					desStr = df.format(desCell.getNumericCellValue()).trim();
				}
			} else {
				desCell.setCellType(Cell.CELL_TYPE_STRING);
				desStr = desCell.toString().trim();
			}
		}
		return desStr;

	}

	// 获取单元格的值
	private Object getCellValue(Cell cell) {
		if(cell == null){
			return null;
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return cell.getRichStringCellValue().getString();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					return cell.getNumericCellValue();
				}
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			default:
				return null;
		}
	}


	private Object saveFailExcel(Map<String,List<Map<String, Object>>>  info)
			throws Exception {

		XSSFWorkbook wb = new XSSFWorkbook();

		for ( Map.Entry<String,List<Map<String, Object>>> map : info.entrySet()) {

			// 创建Excel的工作sheet,对应到一个excel文档的tab
			XSSFSheet sheet = wb.createSheet(map.getKey());

			// 设置excel每列宽度
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 2000);
			sheet.setColumnWidth(3, 20000);
			// 创建cellStyle
			XSSFCellStyle style = wb.createCellStyle();
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);    //设置cell底色
			style.setFillForegroundColor(new XSSFColor(Color.red));

			// 创建Excel的sheet的一行 (表头)
			XSSFRow row = sheet.createRow(0);
			row.setHeight((short) 500);// 设定行的高度
			// 创建一个Excel的单元格
			row.createCell(0).setCellValue(ResultExceltLoginInfo.COMMON_SORT);
			row.createCell(1).setCellValue(ResultExceltLoginInfo.FAIL_WRONG_ROW);
			row.createCell(2).setCellValue(ResultExceltLoginInfo.FAIL_WRONG_COLUMN);
			row.createCell(3).setCellValue(ResultExceltLoginInfo.FAIL_WRONG_REASON);

			// 创建Excel的sheet的数据行

			List<Map<String, Object>> infoList = map.getValue();

			for (int i = 0; i < infoList.size(); i++) {
				XSSFRow dataRow = sheet.createRow(i + 1);
				dataRow.setHeight((short) 500);

				dataRow.createCell(0).setCellValue(i + 1);
				dataRow.createCell(1).setCellValue(
						infoList.get(i).get("wrong_row").toString());
				dataRow.createCell(2).setCellValue(
						infoList.get(i).get("wrong_column").toString());
				Cell cell = dataRow.createCell(3);
				cell.setCellValue(infoList.get(i).get("wrong_reason").toString());
				// 最后一个单元格设置样式
				cell.setCellStyle(style);
			}
		}

		String resultLoginInfoPath = FileUtil.getFilePath();
		String filePath = resultLoginInfoPath + File.separator;
		StringBuffer fileNameSb = new StringBuffer();
		String fileName= LoginUtil.getUserLoginInfo().getUserName()
				+ "--题库试题导入错误信息_" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmss")
				+ ".xlsx";
		fileNameSb.append(filePath+fileName);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(fileNameSb.toString());
		FileOutputStream os = new FileOutputStream(file);
		wb.write(os);
		// 存入上传记录表
		if (os != null) {
			os.flush();
			os.close();
		}

		return Results.parse(Constants.STATE_SUCCESS,"题库错误信息保存成功！");

	}





	/**
	 * 记录errorMsg的值和出错日志
	 *
	 * @param appendStr
	 * @param errorMsg
	 */
	private void addLogAndErrorMsg(String appendStr, StringBuilder errorMsg) {
		if (errorMsg == null) {
			errorMsg = new StringBuilder();
		}
		errorMsg.append(appendStr);
	}

	/**
	 * 记录用户导入的出错信息
	 *
	 * @param rowNum
	 * @param colNum
	 * @param reason
	 * @param infoList
	 */
	private void addWrongMap(Object rowNum, Object colNum, String reason,
							 List<Map<String, Object>> infoList) {
		Map<String, Object> exceptionMap = new HashMap<String, Object>();
		exceptionMap.put("wrong_row", rowNum);
		exceptionMap.put("wrong_column", colNum);
		exceptionMap.put("wrong_reason", reason);
		infoList.add(exceptionMap);
	}
	
	/**
	 * 根据试题类型、题干和答案判断是否重复 ,如果重复返回true
	 * @param q
	 * @return
	 */
	private boolean quesEqual(QuesQuestions q) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("classify_id", q.getClassifyId());
		cri.where().andEquals("type", q.getType());
		cri.where().andEquals("question", q.getQuestion());
		List<QuesQuestions> list = super.query(QuesQuestions.class, cri);
		/*for(QuesQuestions ques:list) {
			JSONArray src = (JSONArray)JSON.parseObject(ques.getAnswer()).get("opt");
			JSONArray dst = (JSONArray)JSON.parseObject(q.getAnswer()).get("opt");
			if(null == src || null == dst) {
				return true;
			}
			if(arrEqual(src,dst)) {
				return true;
			}
		}*/
		if (CollectionUtils.isNotEmpty(list))
			return true;
		return false;
	}
	
	private boolean arrEqual(JSONArray src, JSONArray dst) {
		if(src.size() != 0 && src.size() == dst.size()) {
			Object[] srcArr = src.toArray();
			Object[] dstArr = dst.toArray();
			Arrays.sort(srcArr);
			Arrays.sort(dstArr);
			for(int i=0;i<srcArr.length;i++) {
				if(!srcArr[i].equals(dstArr[i])) {
					return false;
				}
			}
		}
		return true;
	}


	private SimpleCriteria getCri(QuesQuestions obj) {


		SimpleCriteria cri = Cnd.cri();




		/*if (3 == accountType && 2 == isTeach) {
			cri.where().andEquals("qq.crt_user", LoginUtil.getUserLoginInfo().getUserName());
		}
*/
		if(StringTool.isNotEmpty(obj.getClassifyId())) {
			cri.where().andIn("qq.classify_id", obj.getClassifyId().split(","));
		}
		if(StringTool.isNotEmpty(obj.getDifficultyId())) {
			cri.where().andEquals("qq.difficulty_id", obj.getDifficultyId());
		}
		if(StringTool.isNotEmpty(obj.getType())) {
			cri.where().andEquals("qq.type", obj.getType());
		}
		if(StringTool.isNotEmpty(obj.getQuestion())) {
			cri.where().andLike("qq.question", QuesQuestionUtil.encrypt(obj.getQuestion()));
		}
		
		return cri;
	}

	public List<QuesQuestions> queryQuesQuestions(String[] quesIds) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andIn("id", quesIds);

		return super.query(QuesQuestions.class,cri);
	}

	/**
	 * 闯关随机出题
	 * @param quesQuestions
	 * @return
	 * @author dai.jiaqi
	 */
	public List<QuesQuestions> queryPageForRush(QuesQuestions quesQuestions) {
		SimpleCriteria cri = Cnd.cri();
		//难度
		if(quesQuestions.getDifficultyId()!=null && quesQuestions.getDifficultyId().trim().length() > 0){
			String[] tmp = quesQuestions.getDifficultyId().split(",");
				cri.where().andIn("difficulty_id", tmp);
		}

		//类型
		if(quesQuestions.getTypeQuery()!=null && quesQuestions.getTypeQuery().trim().length() > 0){
			String[] tmp = quesQuestions.getTypeQuery().split(",");
			cri.where().andIn("type", tmp);
		}

		//题库
		if(quesQuestions.getClassifyId()!=null && quesQuestions.getClassifyId().trim().length() > 0){
			cri.where().andIn("classify_id", quesQuestions.getClassifyId().split(","));
		}

		/**
		 * 查询总数
		 */
	/*	String sqlStr = "SELECT count(1) FROM ques_questions ";
		Sql sql = Sqls.create(sqlStr);
		sql.setCallback(Sqls.callback.integer());
		dao.execute(sql);
		*//**
		 * 生成随机开始数
		 *//*
		int count = sql.getInt();
		Random random = new Random();
		int s = random.nextInt(count - quesQuestions.getPageSize());
		quesQuestions.setPage(s);*/

		cri.getOrderBy().desc("rand()");
		PageResult<QuesQuestions> pr =super.queryPage(QuesQuestions.class, quesQuestions, cri);
		if(pr==null){
			return null ;
		}else{
			return  pr.getRows();
		}
	}


	/**
	 * 获取题目
	 * @param questionId 问题id
	 * @return 返回题目信息
	 */
	public QuesQuestions queryQuestion(String questionId){
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("ques_questions.id", questionId);
		List<QuesQuestions> pr =super.query(RUSH_GETQUESTION,QuesQuestions.class, cri);
		if(pr!=null && pr.size() > 0){
			return  pr.get(0);
		}else{
			return null;
		}
	}

//    /**
//     * 新增talent_evalution_behavior_level
//     * @param obj
//     * @return
//     */
//    @Aop({"sixFieldInterceptor","validationInterceptor"})
//    public SensitiveWord insertSensitivewords(SensitiveWord sensitiveWord) {
//        return super.insert(sensitiveWord);
//    }
//
//	public String insertToSensitivewords(File file) {
//		ExcelToBean etb = new ExcelToBean();
//		ExcelDataFormatter edf = new ExcelDataFormatter();
//		try {
//			List<SensitiveWord> list = etb.readFromFile(edf, file, SensitiveWord.class);
//			for (SensitiveWord list1 : list) {
//				list1.setId(UUIDUtil.getUUID());
//				list1.setSensitivewords(list1.getSensitivewords());
//				list1.setType(list1.getType());
//				list1.setCrtIp("127.0.0.1");
//				list1.setCrtUser("admin");
//				list1.setCrtTime(new Date());
//                insertSensitivewords(list1);
//			}
//			return "";
//		} catch (Exception e) {
//			e.printStackTrace();
//			// 有一个插入失败则整体导入失败
//			return"批量导入失败";
//		}
//	}
}