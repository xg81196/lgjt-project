package lgjt.services.backend.industry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.tool.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;

import lombok.extern.log4j.Log4j;
import lgjt.domain.backend.industry.SysIndustry;



@Log4j
@IocBean
public class SysIndustryService extends BaseService {


	public PageResult<SysIndustry> queryPage(SysIndustry obj) {
		SimpleCriteria cri = Cnd.cri();

		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getLevel())) {
			cri.where().andEquals("level", obj.getLevel());
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

		return super.queryPage(SysIndustry.class, obj, cri);
	}

	public List<SysIndustry> query(SysIndustry obj) {
		SimpleCriteria cri = Cnd.cri();
		if(StringTool.isNotNull(obj.getName())) {
			cri.where().andEquals("name", obj.getName());
		}
		if(StringTool.isNotNull(obj.getSuperId())) {
			cri.where().andEquals("super_id", obj.getSuperId());
		}
		if(StringTool.isNotNull(obj.getStatus())) {
			cri.where().andEquals("status", obj.getStatus());
		}
		if(StringTool.isNotNull(obj.getLevel())) {
			cri.where().andEquals("level", obj.getLevel());
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
		return super.query(SysIndustry.class, cri);
	}

   	public SysIndustry get(String id) {
		return super.fetch(SysIndustry.class, id);
	}

	public int delete(String ids) {
		if(StringTool.isNotNull(ids)) {
			SimpleCriteria cri = Cnd.cri();
			cri.where().andIn("id", ids.split(","));
			return super.delete(SysIndustry.class, cri);
		}
		return 0;
	}

	public SysIndustry checkId(String value) {
		SimpleCriteria cri = Cnd.cri();
		cri.where().andEquals("id",value);
		return super.fetch(SysIndustry.class,cri);
	}



	/**
	 * 根据父节点查询
	 * @author wuguangwei
	 * @param parentId
	 * @return
	 */
	public List<SysIndustry> queryIndustryIdList(String parentId) {
		SimpleCriteria cri=Cnd.cri();
		cri.where().andEquals("super_id", parentId);

		return  super.query(SysIndustry.class, cri);

	}


	/**
	 * 查询包括本机构及下属机构
	 * @author wuguangwei
	 * @param companyId
	 * @return
	 */
	public List<SysIndustry> querySubIndustryList(String companyId) {

		//公司及下面公司ID列表
		List<SysIndustry> companyIdList = new ArrayList<>();

		//获取下面公司ID
		List<SysIndustry> subIdList = queryIndustryIdList(companyId);
		getCompanyTreeList(subIdList, companyIdList);
		return companyIdList;

	}



	/**
	 * 递归
	 * @author wuguangwei
	 */
	private void getCompanyTreeList(List<SysIndustry> subIdList, List<SysIndustry> companyIdList){

		for(SysIndustry company : subIdList){
			List<SysIndustry> list = queryIndustryIdList(company.getId());
			if(list != null && list.size() > 0){
				getCompanyTreeList(list, companyIdList);
			}

			companyIdList.add(company);
		}
	}



	/**
	 * 一次性全部查询出机构树
	 * @return
	 */
	public List<SysIndustry> querySysIndustryTree() {

		Criteria cri = Cnd.cri();
		cri.where().andEquals("status",0);
		List<SysIndustry> organizations = super.query(SysIndustry.class, cri);

		Map<String,List<SysIndustry>> p2sons = new HashMap<>();
		for(SysIndustry organization:organizations) {
			if(p2sons.containsKey(organization.getSuperId())) {
				p2sons.get(organization.getSuperId()).add(organization);
			}else {
				List<SysIndustry> list = new ArrayList<>();
				list.add(organization);
				p2sons.put(organization.getSuperId(), list);
			}
		}
		SysIndustry fixedTopRes = this.getFixedTopRes();

		List<SysIndustry> topRess = p2sons.get("-1");
		if(topRess!=null&&topRess.size()>0){
			for (SysIndustry topRes : topRess) {
				topRes.setList(p2sons.get(topRes.getId()));
			}
		}
		fixedTopRes.setList(topRess);
		List<SysIndustry> result = new ArrayList<>();
		result.add(fixedTopRes);
		return result;
	}

	private SysIndustry getFixedTopRes() {
		SysIndustry resource = new SysIndustry();
		resource.setId("-1");
		resource.setName("行业树");
		return resource;
	}


	public String getIndustryString(String regionId) {

		if(regionId == null) {
			return "";
		}
		SysIndustry region = this.fetch(SysIndustry.class,regionId);
		if(region != null) {
			return getIndustryString(region.getSuperId()) +"/"+ region.getName();  //  递归调用方法getRegionString(Long regionId)，停止条件设为regionId==null为真
		}
		return "";
	}




	public  String importIndustryString() {

		Workbook wb =null;
		Sheet sheet = null;
		Row row = null;
		List<Map<String,String>> list = null;
		String cellData = null;
		String filePath = "F:\\女工項目\\相关类别识别代码.xlsx";
		String columns[] = {"type1","type2","type3","type4","name"};
		wb = readExcel(filePath);
		if(wb != null){
			//用来存放表中数据
			list = new ArrayList<Map<String,String>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(2);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取第一行
			row = sheet.getRow(0);
			//获取最大列数
			int colnum = row.getPhysicalNumberOfCells();
			for (int i = 1; i<rownum; i++) {
				Map<String,String> map = new LinkedHashMap<String,String>();
				row = sheet.getRow(i);
				if(row !=null){
					for (int j=0;j<colnum;j++){
						cellData = (String) getCellFormatValue(row.getCell(j));
						map.put(columns[j], cellData);
					}
				}else{
					break;
				}
				list.add(map);
			}
		}
		String cell1 = null ;
		String cell2 = null ;
		String cell3 = null ;
		List<SysIndustry> dataList = new ArrayList<>();
		//遍历解析出来的list
		for (int i = 0; i < list.size(); i++){


			String type1 = list.get(i).get("type1").toString();

			String type2 =list.get(i).get("type2").toString();

			String type3 =list.get(i).get("type3").toString();

			String type4 =list.get(i).get("type4").toString();

			String name =list.get(i).get("name").toString().trim();

			if (StringUtils.isNotBlank(type1)) {
				cell1 = UUIDUtil.getUUID();
				SysIndustry sysIndustry1 = new SysIndustry();
				sysIndustry1.setId(cell1);
				sysIndustry1.setName(name);
				sysIndustry1.setSuperId("-1");
				sysIndustry1.setStatus(0);
				sysIndustry1.setLevel(1);
				sysIndustry1.setCrtTime(new Date());
				dataList.add(sysIndustry1);
			} else {
				if (StringUtils.isNotBlank(type2)) {
					cell2 = UUIDUtil.getUUID();
					SysIndustry sysIndustry2 = new SysIndustry();
					sysIndustry2.setId(cell2);
					sysIndustry2.setName(name);
					sysIndustry2.setSuperId(cell1);
					sysIndustry2.setStatus(0);
					sysIndustry2.setLevel(2);
					sysIndustry2.setCrtTime(new Date());
					dataList.add(sysIndustry2);
				}else {
					if (StringUtils.isNotBlank(type3) && StringUtils.isBlank(type4)) {
						cell3 = UUIDUtil.getUUID();
						SysIndustry sysIndustry3 = new SysIndustry();
						sysIndustry3.setId(cell3);
						sysIndustry3.setName(name);
						sysIndustry3.setSuperId(cell2);
						sysIndustry3.setStatus(0);
						sysIndustry3.setLevel(3);
						sysIndustry3.setCrtTime(new Date());
						dataList.add(sysIndustry3);
					}
					if (StringUtils.isBlank(type3) && StringUtils.isNotBlank(type4)) {
						SysIndustry sysIndustry4 = new SysIndustry();
						sysIndustry4.setId(UUIDUtil.getUUID());
						sysIndustry4.setName(name);
						sysIndustry4.setSuperId(cell3);
						sysIndustry4.setStatus(0);
						sysIndustry4.setLevel(4);
						sysIndustry4.setCrtTime(new Date());
						dataList.add(sysIndustry4);
					}

					if (StringUtils.isNotBlank(type3) && StringUtils.isNotBlank(type4)) {
						cell3 = UUIDUtil.getUUID();
						SysIndustry  sysIndustry3 = new SysIndustry();
						sysIndustry3.setId(cell3);
						sysIndustry3.setName(name);
						sysIndustry3.setSuperId(cell2);
						sysIndustry3.setStatus(0);
						sysIndustry3.setLevel(3);
						sysIndustry3.setCrtTime(new Date());
						dataList.add(sysIndustry3);

						SysIndustry  sysIndustry4 = new SysIndustry();
						sysIndustry4.setId(UUIDUtil.getUUID());
						sysIndustry4.setName(name);
						sysIndustry4.setSuperId(cell3);
						sysIndustry4.setStatus(0);
						sysIndustry4.setLevel(4);
						sysIndustry4.setCrtTime(new Date());
						dataList.add(sysIndustry4);
					}
				}

			}
		}

          super.getDao().fastInsert(dataList);
		System.out.println("finished");
      return "finished";
	}

	//读取excel
	public static Workbook readExcel(String filePath){
		Workbook wb = null;
		if(filePath==null){
			return null;
		}
		String extString = filePath.substring(filePath.lastIndexOf("."));
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			if(".xls".equals(extString)){
				return wb = new HSSFWorkbook(is);
			}else if(".xlsx".equals(extString)){
				return wb = new XSSFWorkbook(is);
			}else{
				return wb = null;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}
	public static Object getCellFormatValue(Cell cell){
		Object cellValue = null;
		DecimalFormat df = new DecimalFormat("0");// 格式化 number
		if(cell!=null){
			//判断cell类型
			switch(cell.getCellType()){
				case Cell.CELL_TYPE_NUMERIC:{
					cellValue = subZeroAndDot(String.valueOf(cell.getNumericCellValue()));
					break;
				}
				case Cell.CELL_TYPE_FORMULA:{
					//判断cell是否为日期格式
					if(DateUtil.isCellDateFormatted(cell)){
						//转换为日期格式YYYY-mm-dd
						cellValue = cell.getDateCellValue();
					}else{
						//数字
						cellValue = subZeroAndDot(String.valueOf(cell.getNumericCellValue()));
					}
					break;
				}
				case Cell.CELL_TYPE_STRING:{
					cellValue = cell.getRichStringCellValue().getString();
					break;
				}
				default:
					cellValue = "";
			}
		}else{
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s){
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}


}