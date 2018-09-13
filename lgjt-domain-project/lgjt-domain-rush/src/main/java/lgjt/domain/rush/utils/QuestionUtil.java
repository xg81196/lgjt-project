package lgjt.domain.rush.utils;


import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.json.Json;
import lgjt.domain.rush.ques.QuesQuestions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
public class QuestionUtil {
	public static final String SHEET_SINGLE = "单选题";
	public static final String SHEET_MULTIPLE = "多选题";
	public static final String SHEET_JUDGE = "判断题";
	public static final String SHEET_BLANK = "填空题";
	public static final String SHEET_SHORTANSWER = "简答题";

	public static List<QuesQuestions> fromTemplate(File f,
												   Map<String, String> diffMap) {
		List<QuesQuestions> list = new ArrayList<QuesQuestions>();
		HSSFWorkbook workbook = null;
		try {
			FileInputStream in = new FileInputStream(f);
			workbook = new HSSFWorkbook(in);
			List<String> sheels = getSheels(workbook);
			for (int i = 0; i < sheels.size(); i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				int type = 1;
				if (sheet.getSheetName().equals(SHEET_SINGLE)) {
					type = QuesQuestions.TYPE_CHOICES_SINGLE;
				} else if (sheet.getSheetName().equals(SHEET_MULTIPLE)) {
					type = QuesQuestions.TYPE_CHOICES_MULTIPLE;
				} else if (sheet.getSheetName().equals(SHEET_JUDGE)) {
					type = QuesQuestions.TYPE_JUDGE;
				} else if (sheet.getSheetName().equals(SHEET_BLANK)) {
					type = QuesQuestions.TYPE_BLANK;
				} else if (sheet.getSheetName().equals(SHEET_SHORTANSWER)) {
					type = QuesQuestions.TYPE_SHORT_ANSWER;
				}
				List<Map<String, List<String>>> datas = getRows(sheet, type);
				for (Map<String, List<String>> m : datas) {
					String title = "";
					try{
						QuesQuestions q = new QuesQuestions();
					q.setType(type);
					if (m.get("title").size() > 0) {
						title = m.get("title").get(0);
					}
					q.setQuestion(title);
					Map<String, Object> answerMap = new HashMap<String, Object>();
					answerMap.put("type", type);
					answerMap.put("opt", m.get("opt"));
					answerMap.put("answer", m.get("answer"));
					q.setAnswer(Json.toJson(answerMap));
					//难度为空设为默认初级
					if (m.get("diff").size() == 0) {
						q.setDifficultyId("1");
					}else{
						q.setDifficultyId(diffMap.get(m.get("diff").get(0)));
					}
					String question = q.getQuestion();
					// question = QuesQuestionUtil.encrypt(question);
					q.setQuestion(question);
					list.add(q);
					}catch(Exception e){
						log.error("QuestionUtil.fromTemplate is error "+ title, e);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != workbook) {
				try {
					workbook.close();
				} catch (IOException e) {
				}
			}
		}

		return list;
	}

	private static List<String> getSheels(HSSFWorkbook workbook) {
		List<String> sheels = new ArrayList<String> ();
		if(workbook.getSheet(SHEET_SINGLE)!=null){
			sheels.add("单选题");
		}
		if(workbook.getSheet(SHEET_MULTIPLE)!=null){
			sheels.add("多选题");
		}
		if(workbook.getSheet(SHEET_JUDGE)!=null){
			sheels.add("判断题");
		}
		if(workbook.getSheet(SHEET_BLANK)!=null){
			sheels.add("填空题");
		}
		if(workbook.getSheet(SHEET_SHORTANSWER)!=null){
			sheels.add("简答题");
		}
		return sheels;
	}

	/**
	 * 解析试题
	 * 
	 * @param sheet
	 * @param cols
	 * @return array[0] 标题 array[1] 标题
	 */
	private static List<Map<String, List<String>>> getRows(HSSFSheet sheet,
			int type) {
		List<Map<String, List<String>>> result = new ArrayList<Map<String, List<String>>>();
		HSSFRow row0 = sheet.getRow(0);// 标题模板
		int colsCount = 0;
		if (row0.getLastCellNum() >= 0) {
			colsCount = row0.getLastCellNum() - 1;
		}
		List<String> heads = new ArrayList<String>();
		for (int i = 0; i < colsCount; i++) {
			if (row0.getCell(i) == null) {
				break;
			} else {
				heads.add(StringTool.trimAll(row0.getCell(i).getStringCellValue()));
			}
		}
		int idx = 1;
		while (true) {
			HSSFRow row = sheet.getRow(idx++);
			if (null == row || null == row.getCell(0)) {
				break;
			}
			if (null == row.getCell(0) || null == row.getCell(1)) {
				break;
			}
			Map<String, String> opts = new HashMap<String, String>();
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			map.put("title", new ArrayList<String>());// 标题
			map.put("opt", new ArrayList<String>());// 选项
			map.put("answer", new ArrayList<String>());// 答案
			map.put("diff", new ArrayList<String>());// 难度
			map.put("cate", new ArrayList<String>());// 分类
			for (int i = 0; i < colsCount; i++) {
				HSSFCell cell = row.getCell(i);
				if (cell == null) {
					continue;
				}
				
				int cellType = cell.getCellType();
				String value ="";
				if(cellType==0){
					 value = StringTool.trimAll(cell.getNumericCellValue());
				}else {
					 value = StringTool.trimAll(cell.getStringCellValue());
				}
				
				if (heads.get(i).length() == 0) {
					continue;
				} else if (heads.get(i).toLowerCase().startsWith("题目内容")) {
					map.get("title").add(value);
				} else if (heads.get(i).toLowerCase().startsWith("答案")) {
					// 填空题这就是答案
					if (type == QuesQuestions.TYPE_BLANK) {
						map.get("answer").add(value);
					} else {
						map.get("opt").add(value);
						opts.put(heads.get(i).substring(0, 3), value);
					}
				} else if (heads.get(i).toLowerCase().indexOf("正确答案") > -1) {
					if (type == QuesQuestions.TYPE_CHOICES_SINGLE
							|| type == QuesQuestions.TYPE_CHOICES_MULTIPLE) {
						char[] answers = StringTool.trimAll(value).toCharArray();
						for (int an = 0; an < answers.length; an++) {
							String key = "答案" + answers[an];
							map.get("answer").add(opts.get(key));
						}
					} else if (type == QuesQuestions.TYPE_JUDGE) {
						if (value.equalsIgnoreCase("对")
								|| value.equalsIgnoreCase("正确")) {
							map.get("answer").add("对");
						} else {
							map.get("answer").add("错");
						}
					} else if (type == QuesQuestions.TYPE_SHORT_ANSWER) {
						map.get("answer").add(value);
					}
				} else if (heads.get(i).toLowerCase().indexOf("难度") > -1) {
					map.get("diff").add(value);
				} else if (heads.get(i).toLowerCase().indexOf("子分类") > -1) {
					map.get("cate").add(value);
				}
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * 试题难度 key=name ,value = id
	 * 
	 * @return
	 */
	public static Map<String, String> getDiffMapNameId() {
		Map<String, String> diffMap = new HashMap<String, String>();// ID NAME
		diffMap.put("初级", QuesQuestions.DIFFICULTY_PRIMARY + "");
		diffMap.put("中级", QuesQuestions.DIFFICULTY_INTERMEDIATE + "");
		diffMap.put("高级", QuesQuestions.DIFFICULTY_ADVANCED + "");
		diffMap.put("技师", QuesQuestions.DIFFICULTY_TECHNICIAN + "");
		return diffMap;
	}

	/**
	 * 试题难度 key=id ,value = name
	 * 
	 * @return
	 */
	public static Map<String, String> getDiffMapIdName() {
		Map<String, String> diffMap = new HashMap<String, String>();// ID NAME
		diffMap.put(QuesQuestions.DIFFICULTY_PRIMARY + "", "初级");
		diffMap.put(QuesQuestions.DIFFICULTY_INTERMEDIATE + "", "中级");
		diffMap.put(QuesQuestions.DIFFICULTY_ADVANCED + "", "高级");
		diffMap.put(QuesQuestions.DIFFICULTY_TECHNICIAN + "", "技师");
		return diffMap;
	}

	/**
	 * 根据模板类型返回模板位置
	 * 
	 * @param templateType
	 * @return
	 */
	public static String getTemplateName(int templateType) {
		switch (templateType) {
		case 1:
			return "template/Template_BKD_1.xls";
		case 2:
			return "template/Template_GDJ_2.xls";
		case 3:
			return "template/Template_HD_3.xls";
		case 4:
			return "template/Template_HG_4.xls";
		case 5:
			return "template/Template_JYJC_5.xls";
		case 6:
			return "template/Template_LWFGS_6.xls";
		case 7:
			return "template/Template_SDS_7.xls";
		case 8:
			return "template/Template_TJGG_8.xls";
		case 9:
			return "template/Template_XQY_9.xls";
		default:
			return null;
		}
	}

}
