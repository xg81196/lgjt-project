package lgjt.domain.rush.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import lgjt.common.base.utils.IpValidateUtil;
import lgjt.domain.rush.ques.QuesQuestions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuesUtil {


	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";

	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY= "";

	public static List<QuesQuestions> fromTemplate(File f, Map<String,String> diffMap) {
		List<QuesQuestions> list = new ArrayList<QuesQuestions>();
		HSSFWorkbook workbook = null;
		try {
			FileInputStream in = new FileInputStream(f);
			workbook = new HSSFWorkbook(in);
			
			for(int i=0;i<5;i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				
				if(sheet.getSheetName().equals(QuesQuestions.TYPE_1)) {
					List<String[]> datas = getRows(sheet,11,1);
					int a=0;
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(1);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 1);
						answer.put("opt", getArr(arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]));
						if(StringTool.isNull(arr[8])){
							continue;
						}
						int pos = arr[8].trim().charAt(0) - 'A';
						answer.put("answer",new String[] {arr[2+pos]});
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[9]));
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("初级");
						}
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_2)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(2);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 2);
						answer.put("opt", getArr(arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]));
						if(StringTool.isNull(arr[8])){
							continue;
						}
						String answers = arr[8].trim();
						//answers = answers.replace(",","");
						String[] answerArr = new String[answers.length()];
						for(int k=0;k<answers.length();k++) {
							int pos = answers.charAt(k) - 'A';
							answerArr[k] = arr[2+pos];
						}
						answer.put("answer",answerArr);
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[9]));
						
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_3)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(3);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 3);
						String ansser0 = arr[2];
						List<String> list3=new ArrayList<String>();
						if(StringTool.isNotNull(ansser0)){
							ansser0 = ansser0.trim();
							if(ansser0.equals("对") || ansser0.equals("正确")){
								list3.add("对");
								answer.put("answer",list3);
							}else if(ansser0.equals("错") || ansser0.equals("错误")){
								list3.add("错");
								answer.put("answer",list3);
							}else{
								continue;
							}
						}else{
							continue;
						}
						//answer.put("answer",new String[] {arr[2]});
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[3]));
						
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_4)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(4);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 4);
						answer.put("answer",getArr(arr[2],arr[3],arr[4],arr[5]));
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[6]));
						
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_5)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(5);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 5);
						answer.put("answer",new String[] {arr[2]});
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[3]));
						
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						list.add(q);
					}
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
				}
			}
		}
		
		return list;
	}



	public static List<QuesQuestions> fromTemplate2(File f,String classifyId, Map<String,String> diffMap) {
		List<QuesQuestions> list = new ArrayList<QuesQuestions>();
		HSSFWorkbook workbook = null;
		try {
			FileInputStream in = new FileInputStream(f);
			workbook = new HSSFWorkbook(in);

			for(int i=0;i<5;i++) {
				HSSFSheet sheet = null;
				try {
					 sheet = workbook.getSheetAt(i);
				} catch (Exception e ) {
					//有很多不按模板导入，防止越界
				}


				if(sheet.getSheetName().equals(QuesQuestions.TYPE_1)) {
					List<String[]> datas = getRows(sheet,11,1);
					int a=0;
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(1);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 1);
						answer.put("opt", getArr(arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]));
						if(StringTool.isNull(arr[8])){
							continue;
						}
						int pos = arr[8].trim().charAt(0) - 'A';
						answer.put("answer",new String[] {arr[2+pos]});
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[9]));
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("1000");
						}
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						q.setClassifyId(classifyId);
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_2)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(2);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 2);
						answer.put("opt", getArr(arr[2],arr[3],arr[4],arr[5],arr[6],arr[7]));
						if(StringTool.isNull(arr[8])){
							continue;
						}
						String answers = arr[8].trim();
						//answers = answers.replace(",","");
						String[] answerArr = new String[answers.length()];
						for(int k=0;k<answers.length();k++) {
							int pos = answers.charAt(k) - 'A';
							answerArr[k] = arr[2+pos];
						}
						answer.put("answer",answerArr);
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[9]));
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("1000");
						}
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						q.setClassifyId(classifyId);
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_3)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(3);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 3);
						String ansser0 = arr[2];
						List<String> list3=new ArrayList<String>();
						if(StringTool.isNotNull(ansser0)){
							ansser0 = ansser0.trim();
							if(ansser0.equals("对") || ansser0.equals("正确")){
								list3.add("对");
								answer.put("answer",list3);
							}else if(ansser0.equals("错") || ansser0.equals("错误")){
								list3.add("错");
								answer.put("answer",list3);
							}else{
								continue;
							}
						}else{
							continue;
						}
						//answer.put("answer",new String[] {arr[2]});
						q.setAnswer(JSON.toJSONString(answer));
						q.setDifficultyId(diffMap.get(arr[3]));
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("1000");
						}
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						q.setClassifyId(classifyId);
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_4)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(4);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 4);
						answer.put("answer",getArr(arr[2],arr[3],arr[4],arr[5]));
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[6]));

						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("1000");
						}
						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						q.setClassifyId(classifyId);
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
						list.add(q);
					}
				}else if(sheet.getSheetName().equals(QuesQuestions.TYPE_5)) {
					List<String[]> datas = getRows(sheet,11,1);
					for(String[] arr:datas) {
						QuesQuestions q = new QuesQuestions();
						q.setType(5);
						q.setQuestion(arr[1]);
						Map<String,Object> answer = new HashMap<String,Object>();
						answer.put("type", 5);
						answer.put("answer",new String[] {arr[2]});
						q.setAnswer(Json.toJson(answer));
						q.setDifficultyId(diffMap.get(arr[3]));
						if(StringTool.isNull(q.getDifficultyId())) {
							q.setDifficultyId("1000");
						}

						String question = q.getQuestion();
						question = QuesQuestionUtil.encrypt(question);
						q.setQuestion(question);
						q.setClassifyId(classifyId);
						q.setId(UUIDUtil.getUUID());
						q.setCrtIp(IpValidateUtil.getIpAddress(Mvcs.getReq()));
						q.setCrtTime(new Date());
						q.setCrtUser(LoginUtil.getUserLoginInfo().getUserName());
						list.add(q);
					}
				}

			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
				}
			}
		}

		return list;
	}
	
	
	private static List<String> getArr(String... data) {
		List<String> list = new ArrayList<String>();
		for(String s:data) {
			if(StringUtils.isNotBlank(s)) {
				list.add(s);
			}
		}
		return list;
	}
	
	private static List<String[]> getRows(HSSFSheet sheet,int cols,int rows) {
		List<String[]> list = new ArrayList<String[]>();
		int idx = rows;
		int count = 0;
		while(true) {
			HSSFRow row = sheet.getRow(idx++);

			if ( count > 10 )  {
				break;
			}

			if(null == row) {
				count++;
				continue;
				//break;
			}

		/*	if(null == row.getCell(1)) {
				break;
			}*/
			String[] data = new String[cols];
			for(int i=1;i<cols;i++) {
				HSSFCell cell = row.getCell(i);
				if(null == cell) {
					data[i] = "";
				}else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					data[i] = cell.getStringCellValue();
				}
				
			}
			list.add(data);
		}
		return list;
	}



	public static List<QuesQuestions> fromXWTemplate(File f,Map<String,String> diffMap) {
		List<QuesQuestions> list = new ArrayList<QuesQuestions>();
		//HSSFWorkbook workbook = null;
		try {
			//FileInputStream in = new FileInputStream(f);
			//workbook = new HSSFWorkbook(in);
			QuesQuestions q ;
			for(int i=0;i<1;i++) {
				//HSSFSheet sheet = workbook.getSheetAt(0);
				HSSFSheet sheet = (HSSFSheet)getSheetByExcelType(f);
					List<String[]> datas = getRows(sheet,24,3);
					for(String[] arr : datas) {

						 q = new QuesQuestions();
						Map<String,Object> answer = new HashMap<>();
						//单选
						if (Objects.equals(QuesQuestions.TYPE_1,arr[21])) {
							q.setType(1);
							answer.put("type", 1);
							q.setQuestion(arr[2]);
							answer.put("opt", getArr(arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],arr[9],arr[10]));
							if(StringUtils.isBlank(arr[11])){
								continue;
							}
							int pos = arr[11].charAt(0) - 'A';
							answer.put("answer",new String[] {arr[3+pos]});
							q.setAnswer(Json.toJson(answer));
							if (StringUtils.isNotBlank(arr[17]))
							   q.setDifficultyId(diffMap.get(arr[17]));
							else
								q.setDifficultyId("chuji");

							String question = q.getQuestion();
							question = QuesQuestionUtil.encrypt(question);
							q.setQuestion(question);
							q.setCrtUser(arr[13]);
							if (StringUtils.isNotBlank(arr[14]))  {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								q.setCrtTime(sdf.parse(arr[14]));
							}
							list.add(q);
						}else {
							q = new QuesQuestions();
							q.setType(2);
							q.setQuestion(arr[2]);
							Map<String,Object> multiAnswer = new HashMap<>();
							multiAnswer.put("type", 2);
							multiAnswer.put("opt", getArr(arr[3],arr[4],arr[5],arr[6],arr[7],arr[8],arr[9],arr[10]));
							String answers = arr[11].replaceAll("\\|","");
							arr[11] = answers;
							String[] answerArr = new String[answers.length()];
							for(int k=0;k<answers.length();k++) {
								int pos = arr[11].charAt(k) - 'A';
								answerArr[k] = arr[3+pos];
							}
							multiAnswer.put("answer",answerArr);
							q.setAnswer(Json.toJson(multiAnswer));
							q.setDifficultyId(diffMap.get(arr[9]));

							String question = q.getQuestion();
							question = QuesQuestionUtil.encrypt(question);
							q.setQuestion(question);
							q.setCrtUser(arr[13]);
							if (StringUtils.isNotBlank(arr[14]))  {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								q.setCrtTime(sdf.parse(arr[14]));
							}
							list.add(q);
						}

					}

			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {

		}

		return list;
	}


	/**
	 * 读取 Excel file
	 *
	 * @param file
	 *            Excel file
	 * @return
	 * @throws IOException
	 */
	public static Sheet getSheetByExcelType(File file) throws IOException {
		if ( file == null ) {
			return null;
		} else {
			HSSFWorkbook wb = null;
			XSSFWorkbook xssfWorkbook = null;
			try {
				FileInputStream in = new FileInputStream(file);
				String postfix = Files.getFileExtension(file.getPath());
				if (!EMPTY.equals(postfix)) {
					if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
						POIFSFileSystem poi = new POIFSFileSystem(in);
						 wb = new HSSFWorkbook(poi);
						return  wb.getSheetAt(0);
					} else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
						 xssfWorkbook = new XSSFWorkbook(in);
						return xssfWorkbook.getSheetAt(0);
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
				if (null != xssfWorkbook){
					try {
						xssfWorkbook.close();
					} catch (IOException e) {
					}
				}

			}

		}
		return null;
	}



	public static void main(String[] args) {
		File f = new File("d:\\新为导出问价0925.xls");
		List<QuesQuestions> list = fromXWTemplate(f,new HashMap<String,String>());
		for(QuesQuestions q:list) {
			System.out.println(q.getQuestion()+":" + q.getAnswer());
		}
		JSONObject jo = JSON.parseObject(list.get(0).getAnswer());
		Object o = jo.get("opt");
		System.out.println(o);


	}
}
