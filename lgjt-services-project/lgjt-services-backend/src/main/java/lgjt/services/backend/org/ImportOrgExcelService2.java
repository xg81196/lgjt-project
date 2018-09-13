package lgjt.services.backend.org;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.tree.CallBackTree;
import lgjt.common.base.utils.*;
import lgjt.domain.backend.attach.SysAttachment;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.services.backend.attach.SysAttachmentService;
import lgjt.services.backend.city.SysCityService;
import lgjt.services.backend.role.SysRoleService;
import lgjt.services.backend.user.SysUserAdminService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.services.backend.user.SysUserService;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


/**
 *
 *  Excel读取，兼容2003-2007和2010两个版本即excel的后缀名为：xls和xlsx。
 *
 * @Description: Created by wuguangwei on 2017/12/14.
 */

@IocBean
public class ImportOrgExcelService2 extends BaseService {

    private static final Logger logger = Logger.getLogger(ImportOrgExcelService2.class);

    //private StringBuffer errorMsg  = new StringBuffer();

    private SysUserService sysUserService = IocUtils.getBean(SysUserService.class);

    private SysAttachmentService commonAttachmentService = IocUtils.getBean(SysAttachmentService.class);

    private SysOrganizationService sysOrganizationService = IocUtils.getBean(SysOrganizationService.class);

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
    public  Sheet getSheetByExcelType(File file) throws IOException {
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


    public Results readExcel(Sheet sheet, SysUserAdmin userAdmin,String orgId,CallBackTree callBackTree)
            throws  Exception {

        StringBuffer errorMsg  = new StringBuffer();

        int firstRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        Row firstRow = sheet.getRow(firstRowIndex);
        List<Map<String, Object>> infoList = new ArrayList<>();
        // 读不到模板表头直接报错，不在记录错误信息
        if (firstRow == null) {
            // 记录日志
            return Results.parse(Constants.STATE_FAIL,"未成功读取模板表头行");
        }

        //判断表头是否正确
        if(firstRow.getPhysicalNumberOfCells() != 13)
        {
            // 记录日志
            return Results.parse(Constants.STATE_FAIL,"表头的数量不对,请使用模版进行导入");
        }

        try {
            int startIndex = firstRow.getFirstCellNum();
            int lastIndex = firstRow.getLastCellNum();
            // 确定读取内容位置(给用户模板的每一列设置索引)
            setCellIndex(firstRow, startIndex, lastIndex);
        } catch (Exception e) {
            logger.error(e, e);
            return Results.parse(Constants.STATE_FAIL,"确定读取内容位置(给用户模板的每一列设置索引)异常");
        }


        if (nameCol != 0 || superIdCol == 0 || orgCodeCol == 0
                || addressCol == 0 || unitDistrictCol == 0 || unitPropertyCategoryCol == 0
                || economicTypeCol == 0 || unionLeaderCol == 0 || unionLeaderPhoneCol == 0
                || indIdCol == 0) {
            return Results.parse(Constants.STATE_FAIL,"表单标题列名称设置有问题，请重新设置后提交");
        }

        // 用户名List(用于判断用户是否重复)
        List<String> names = Lists.newArrayList();
        List<String> orgCodes = Lists.newArrayList();

        int countBlank = 0;
        for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

            Row row = sheet.getRow(rIndex);
            //判断第一行是否有数据
          /*  if (row == null) {
                break;
            }*/


            String importUnique = tranferScientificNotation(row,
                    nameCol);

            String importOrgCodeUnique = tranferScientificNotation(row,
                    orgCodeCol);


            //有空格的空白行也视为表格为空
            if (rIndex > 1) {
                //连续两行为空
                if (StringUtils.isBlank(importUnique) && StringUtils.isBlank(importOrgCodeUnique)) {
                    countBlank++;
                    continue;
                }

            }

            if (countBlank > 10) {

                break;
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, nameCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex +1) + "--行中的单位名称为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, superIdCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的上级工会名称为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, orgCodeCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的组织机构代码为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, addressCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的地址为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, unitDistrictCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的单位所在政区为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, unitPropertyCategoryCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的单位性质类别为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, economicTypeCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的经济类型为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, unionLeaderCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的工会/企业负责人为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, unionLeaderPhoneCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的联系电话为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, indIdCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的单位所属行业为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }



            SimpleCriteria cri=Cnd.cri();
            cri.where().andEquals("name", importUnique);
            SysOrganization su=sysOrganizationService.fetch(SysOrganization.class, cri);

                // 企业名称文件内判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if ( sheet.getRow(i) != null ) {
                        if ( sheet.getRow(i).getCell(nameCol) != null )  {
                            if (StringUtils.isNotBlank(tranferScientificNotation(
                                    row, nameCol))
                                    && sheet.getRow(i).getCell(nameCol).toString()
                                    .trim().equals(importUnique)) {
                                return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的单位名称"+ importUnique +"已存在，请修改后再进行导入",
                                        errorMsg);
                            }
                        }

                    }

                }
            }
            // 企业名称系统判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(tranferScientificNotation(
                            row, nameCol))
                            && su != null && !names.contains(importUnique)) {
                        names.add(importUnique);
                        return Results.parse(Constants.STATE_FAIL,"您导入的文件中第--" + (rIndex + 1) + "--行中的单位名称"+ importUnique +"在数据库中已存在，请修改后再进行导入",
                                errorMsg);
                    }
                }
            }

            // 组织机构代码文件内判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if ( sheet.getRow(i) != null ) {
                        if ( sheet.getRow(i).getCell(orgCodeCol) != null )  {
                            if (StringUtils.isNotBlank(tranferScientificNotation(
                                    row, orgCodeCol))
                                    && sheet.getRow(i).getCell(orgCodeCol).toString()
                                    .trim().equals(importOrgCodeUnique)) {
                                return Results.parse(Constants.STATE_FAIL,"您导入的文件中第--" + (rIndex + 1) + "--行中的组织机构代码"+ importOrgCodeUnique +"已存在，请修改后再进行导入",
                                        errorMsg);
                            }
                        }

                    }

                }
            }
            // 组织机构代码系统判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(tranferScientificNotation(
                            row, orgCodeCol))
                            && su != null && !orgCodes.contains(importOrgCodeUnique)) {
                        orgCodes.add(importOrgCodeUnique);
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的组织机构代码"+ importOrgCodeUnique +"在数据库中已存在，请修改后再进行导入",
                                errorMsg);
                    }
                }
            }
        }


        logger.debug("----------读取模板内容前的数据处理完成，开始读取内容----------");
        List<SysOrganization> organizationList = new ArrayList<>();
        int count = 0;
        // 读取内容
        for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("wrong_row", rIndex);
            try {
                SysOrganization org = new SysOrganization();
                Row row = sheet.getRow(rIndex);

                //判断第一行是否有数据
              /*  if (row == null) {
                    break;
                }*/

                String name = tranferScientificNotation(row, nameCol);

                if (rIndex > 1) {
                    //连续两行为空
                    if (StringUtils.isBlank( name )) {
                        count++;
                        continue;
                    }

                }

                if (count > 10) {

                    break;
                }


                String superId = row.getCell(superIdCol) == null ? "" : row
                        .getCell(superIdCol).toString().trim();

                String orgCode = tranferScientificNotation(row,
                        orgCodeCol);

                String address = row.getCell(addressCol) == null ? "" : row
                        .getCell(addressCol).toString().trim();

                // 防止数字变成科学技术法的处理
                String unitDistrict = tranferScientificNotation(row,
                        unitDistrictCol);

                String unitPropertyCategory = row.getCell(unitPropertyCategoryCol) == null ? "" : row
                        .getCell(unitPropertyCategoryCol).toString().trim();

                String economicType = row.getCell(economicTypeCol) == null ? "" : row
                        .getCell(economicTypeCol).toString().trim();

                String unionLeader = row.getCell(unionLeaderCol) == null ? "" : row
                        .getCell(unionLeaderCol).toString().trim();

                String unionLeaderPhone = tranferScientificNotation(row,
                        unionLeaderPhoneCol);

                String indId = row.getCell(indIdCol) == null ? "" : row
                        .getCell(indIdCol).toString().trim();

                String identifierCode = row.getCell(identifierCodeCol) == null ? "" : row
                        .getCell(identifierCodeCol).toString().trim();

                String creationDate = tranferScientificNotation(row,
                        creationDateCol);

                String unionType = tranferScientificNotation(row,
                        unionTypeCol);

                org.setName(name);
                //组织机构代码
                org.setOrgCode(orgCode);
                //地址
                org.setAddress(address);
                //单位所在政区
                /*String unitDistrictSub = unitDistrict.substring(unitDistrict.length() - 6, unitDistrict.length());
                String s = unitDistrictSearch(unitDistrictSub);
                if (StringUtils.isBlank(s)) {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行单位所在政区填写错误,请重新填写。");
                }
                if(s.startsWith(",")){
                    String str2 = s.substring(1, s.length());
                    String str3 = str2.substring(7, str2.length());
                    //org.setUnitDistrict(str3);
                }*/
                //单位性质类别
                IObjectCache objectCache = ObjectCache.getInstance();
                String unitPropertyCategoryKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_PROPERTY_KEY);
                Map<String,String> dictMap4 =( Map<String,String> )objectCache.get(unitPropertyCategoryKey);
                if(StringUtils.isNotBlank(dictMap4.get(unitPropertyCategory))){
                    org.setUnitPropertyCategory(dictMap4.get(unitPropertyCategory));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行单位性质类别填写错误,请重新填写,例如:国有企业,国有控股企业。");
                }


                //经济类型
                String econmicsKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_ECONOMICS_KEY);
                Map<String,String> dictMap =( Map<String,String> )objectCache.get(econmicsKey);
                if(StringUtils.isNotBlank(dictMap.get(economicType))){
                    org.setEconomicType(dictMap.get(economicType));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex +1)+"行经济类型填写错误,请重新填写,例如:有限责任公司,国有全资,股份有限公司,中外合资,其他等。");
                }
                //工会企业负责人
                org.setUnionLeader(unionLeader);
                //联系电话
                org.setUnionLeaderPhone(unionLeaderPhone);
                //单位所属行业
                String indIdKey = RedisKeys.getSysIndustryKey4Org(RedisKeys.INDUSTRY_KEY);
                Map<String,String> dictMap8 =( Map<String,String> )objectCache.get(indIdKey);
                if(StringUtils.isNotBlank(dictMap8.get(indId))){
                    org.setIndId(dictMap8.get(indId));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行单位所属行业填写错误,请重新填写,例如:软件开发,互联网公共服务平台,中国共产党机关,金属制品修理等。");
                }
                //社会信用代码
                org.setIdentifierCode(orgCode);
                //排序
                org.setSort(0);
                org.setStatus(0);
                org.setCrtUser(userAdmin.getUserName());
                org.setCrtTime(new Date());
                org.setCrtIp(ClientInfo.getIp());
                //如果有导入的格式不正确，数据也不入库
                if (errorMsg.length() == 0) {

                    if(StringTool.isNotEmpty(superId)) {
                        //List<SysUnion> sysUnionList = IocUtils.getBean(SysUnionService.class).querySubUnionList(orgId);
                        SysUnion union = IocUtils.getBean(SysUnionService.class).checkName(superId);
                        if (union != null) {
                            org.setSuperId(union.getId());
                            org.setAreaName(union.getUnitScope());
                            org.setSuperOrgName(union.getName());
                        } else {
                            //穷举几位进行匹配
                            /*String unit_0 = StringUtils.substring(superId, 0, 2);
                            String unit_1 = StringUtils.substring(superId, 0, 3);
                            String unit_2 = StringUtils.substring(superId, 0, 4);
                            String unit_3 = StringUtils.substring(superId, 0, 5);*/
                            SimpleCriteria cri5 = Cnd.cri();
                            cri5.where().and("name", "REGEXP", superId);
                            List<SysUnion> companyList = IocUtils.getBean(SysUnionService.class).query(SysUnion.class, cri5);
                            List<String> companyNamess = Lists.newArrayList();
                            for (SysUnion com : companyList) {
                                companyNamess.add(com.getName());
                            }
                            return Results.parse(Constants.STATE_FAIL, "第" + (rIndex + 1) + "行上级单位:" + superId + "不存在,  系统存匹配度高的机构为:" + Joiner.on(",").skipNulls().join(companyNamess), errorMsg);
                        }
                    }
                    }else{
                        return Results.parse(Constants.STATE_FAIL,"第"+(rIndex + 1)+"行上级单位为必填项，请填写机构全称。", errorMsg);
                    }
                    if (errorMsg.length()==0) {
                        organizationList.add(org);
                    }
            } catch (Exception e) {
                logger.error(e, e);
                return Results.parse(Constants.STATE_FAIL,"你上传的文件读取过程发生异常，请确认所传信息是否符合要求。", errorMsg);
            }

        }
        //为添加数据权限做准备
        List<SysOrganization> sysOrganizationAdd = new ArrayList<>();
        List<SysUserAdmin> sysUserAdminAdd = new ArrayList<>();
        try {
            for (SysOrganization organization : organizationList) {
                organization.setId(UUIDUtil.getUUID());
                SysOrganization insert = IocUtils.getBean(SysOrganizationService.class).getDao().insert(organization);
                sysOrganizationAdd.add(insert);
                SysUserAdmin sysAdmin = new SysUserAdmin();
                String[] areas = organization.getAreaName().split(",");
                String area = "";
                if (areas.length > 0) {
                    if (areas.length == 1) {
                        area = areas[0];
                    }else if (areas.length == 2){
                        area = areas[1];
                    }else {
                        area = areas[2];
                    }
                }
                sysAdmin.setUserName( area+ CodeGenerateUtils.generateCode());
                String name = sysAdmin.getUserName().hashCode()+"";
                if (name.length()>6){
                    sysAdmin.setExtend2(StringUtils.leftPad(name.substring(name.length()-6,name.length()),6,"0"));
                }else {
                    sysAdmin.setExtend2(StringUtils.leftPad(name,6,"0"));
                }

                //sysAdmin.setUserName( insert.getName());
                sysAdmin.setRealName( insert.getName());
                sysAdmin.setStatus(0);
                sysAdmin.setSex(1);
                sysAdmin.setPhoneNumber(organization.getUnionLeaderPhone());
                sysAdmin.setOrgId(insert.getId());
                sysAdmin.setSalt(RandomStringUtils.randomAlphanumeric(20));
                //String pass  = DigestUtils.md5Hex("96e79218965eb72c92a549dd5a330112"+sysAdmin.getSalt());
                //sysAdmin.setExtend2("111111");
                sysAdmin.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(sysAdmin.getExtend2())+sysAdmin.getSalt()));
                sysAdmin.setExtend3(sysAdmin.getPassword());
                sysAdmin.setCrtUser(userAdmin.getUserName());
                sysAdmin.setCrtTime(new Date());
                sysAdmin.setCrtIp(ClientInfo.getIp());
                SysUserAdmin insert1 = IocUtils.getBean(SysUserAdminService.class).insert(sysAdmin);
                sysUserAdminAdd.add(insert1);
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setAdminUserId(insert1.getId());
                sysUserRole.setRoleId(IocUtils.getBean(SysRoleService.class).checkRole_name("企业管理员").getId());
                sysUserRole.setUserName(insert.getName());
                sysUserRole.setCrtUser(sysAdmin.getUserName());
                sysUserRole.setCrtTime(new Date());
                sysUserRole.setCrtIp(ClientInfo.getIp());
                SysUserRole insert2 = IocUtils.getBean(SysRoleService.class).insert(sysUserRole);

               //工会和企业对应关系
                SysUnionCom sysUnionCom = new SysUnionCom();
                sysUnionCom.setComName(organization.getName());
                sysUnionCom.setUnionName(organization.getSuperOrgName());
                sysUnionCom.setUnionId(organization.getSuperId());
                sysUnionCom.setComId(insert.getId());
                IocUtils.getBean(SysUnionComService.class).insert(sysUnionCom);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL,"你上传的文件读取过程发生异常。", errorMsg);
        }
        //添加权限
        try {
            for (SysOrganization organization : sysOrganizationAdd) {
            /*    if(organization.getName().lastIndexOf("工会") != -1){
                    SysOrganization organ = new SysOrganization();
                    organ.setSuperId(organization.getId());
                    List<SysOrganization> query = IocUtils.getBean(SysOrganizationService.class).query(organ);
                    //查出来父Id是该工会的企业
                    for (SysOrganization sysOrganization : query) {
                        for (SysUserAdmin sysUserAdmin : sysUserAdminAdd) {
                            if(organization.getId().equals(sysUserAdmin.getOrgId())){
                                SysUserData sysUserData = new SysUserData();
                                sysUserData.setAdminUserId(sysUserAdmin.getId());
                                sysUserData.setObjectId(sysOrganization.getId());
                                sysUserData.setObjectType(0);
                                sysUserData.setCompatibility(0);
                                sysUserData.setCrtUser(userAdmin.getUserName());
                                sysUserData.setCrtTime(new Date());
                                sysUserData.setCrtIp(ClientInfo.getIp());
                                IocUtils.getBean(SysUserDataService.class).insert(sysUserData);
                            }
                        }
                    }
                }else {*/
                    for (SysUserAdmin sysUserAdmin : sysUserAdminAdd) {
                        if(organization.getId().equals(sysUserAdmin.getOrgId())){
                            SysUserData sysUserData = new SysUserData();
                            sysUserData.setAdminUserId(sysUserAdmin.getId());
                            sysUserData.setObjectId(organization.getId());
                            sysUserData.setObjectType(UserDataType.COM_TYPE);
                            sysUserData.setCompatibility(0);
                            sysUserData.setCrtUser(userAdmin.getUserName());
                            sysUserData.setCrtTime(new Date());
                            sysUserData.setCrtIp(ClientInfo.getIp());
                            IocUtils.getBean(SysUserDataService.class).insert(sysUserData);
                        }
                    }
               /* }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL,"添加企业管理员权限时候发生异常。", errorMsg);
        }
        if (errorMsg.length()==0) {
            errorMsg.append("success");
            String msg = "";
            /**
             * 刷新树方法
             */
            callBackTree.callBack();
            return Results.parse(Constants.STATE_SUCCESS,"导入数据成功！");
        } else {
            // 插入不成功
            // TODO 回滚插入的企业部门
            return Results.parse(Constants.STATE_FAIL, errorMsg.toString());
        }
    }


    private Object saveFailloginExcel(List<Map<String, Object>> infoList,SysUserAdmin userAdmin)
            throws Exception {
        logger.debug("导入出错,导出错误信息");

        XSSFWorkbook wb = new XSSFWorkbook();

        // 创建Excel的工作sheet,对应到一个excel文档的tab
        XSSFSheet sheet = wb.createSheet("用户批量导入出错返回");

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
        String resultLoginInfoPath = FileUtil.getFilePath();
        String filePath = resultLoginInfoPath + File.separator;
        StringBuffer fileNameSb = new StringBuffer();
        String fileName= userAdmin.getUserName()
                + "--用户批量导入错误信息_" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmss")
                + ".xlsx";
        fileNameSb.append(filePath+fileName);
/*        String fileName = "用户批量导入错误信息--"+UserUtil.getUser().getUserName()+"--"
                + DateFormatUtils.format(new Date(),"yyyyMMddHHmmss")+".xlsx";*/
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
        //便于前台下载查看出错信息
        SysAttachment attachment = new SysAttachment(fileName,
                (filePath+fileName).replace(CommonUtil.ROOT_PATH, ""),
                3, file.length());
        attachment.setCrtUser(userAdmin.getUserName());
        attachment.setCrtIp(ClientInfo.getIp());
        attachment.setCrtTime(new Date());
        commonAttachmentService.insert(attachment);
        return Results.parse(Constants.STATE_SUCCESS,"用户错误信息保存成功！");

    }





    /**
     * 记录errorMsg的值和出错日志
     *
     * @param appendStr
     * @param errorMsg
     */
    private void addLogAndErrorMsg(String appendStr, StringBuffer errorMsg) {
        if (errorMsg == null) {
            errorMsg = new StringBuffer();
        }
        errorMsg.append(appendStr);
        logger.error(appendStr);
    }

    /**
     * 递归查询单位所在政区
     *
     * @param appendStr
     * @param errorMsg
     */
    private static String unitDistrictSearch(String unitDistrict) {
        if(unitDistrict == null) {
            return "";
        }
        SysCity sysCity = IocUtils.getBean(SysCityService.class).fetchCity(unitDistrict);
        if(sysCity != null){
            return unitDistrictSearch(sysCity.getParentid()) + "," + unitDistrict;
        }
        return "";
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

    private void setCellIndex(Row firstRow, int startIndex, int lastIndex) {
        // 给用户模板的每一列设置索引
        for (int cIndex = startIndex; cIndex <= lastIndex; cIndex++) {
            Cell cell = firstRow.getCell(cIndex);

            if (null != cell && StringUtils.isNotBlank(cell.toString())) {

                if (cell.toString().contains(
                        SysOrganiationConstants.SYSORGANIATION_NAME)) {
                    nameCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(
                        SysOrganiationConstants.SYSORGANIATION_SUPERID)) {
                    superIdCol = cIndex;
                    continue;
                }

                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_ORGID)) {
                    orgCodeCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_ADDRESS)) {
                    addressCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_UNIT_DISTRICT)) {
                    unitDistrictCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_UNIT_PROPERTY_CATEORY)) {
                    unitPropertyCategoryCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_ECONOMIC_TYPE)) {
                    economicTypeCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_UNION_LEADER)) {
                    unionLeaderCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_UNION_LEADER_PHONE)) {
                    unionLeaderPhoneCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_IND_ID)) {
                    indIdCol = cIndex;
                    continue;
                }

                // 非必填字段
                if (cell.toString().contains(
                        SysOrganiationConstants.SYSORGANIATION_IDENTIFIER_CODE)) {
                    identifierCodeCol = cIndex;
                    continue;
                }

                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_CREATION_DATE)) {
                    creationDateCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(SysOrganiationConstants.SYSORGANIATION_UNION_TYPE)) {
                    unionTypeCol = cIndex;
                    continue;
                }
            }
        }

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

        String desStr = null;
        if (row != null) {
            Cell desCell = row.getCell(sourceCol);

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

        return desStr;
    }

    // ==========一些字段=========================
    //公司名称
    private int nameCol;

    //上级工会名称
    private int superIdCol;

    //组织机构代码
    private int orgCodeCol;

    //地址
    private int addressCol;

    //单位所在政区
    private int unitDistrictCol;

    //单位性质类别
    private int unitPropertyCategoryCol;

    //经济类型
    private int economicTypeCol;

    //工会企业负责人
    private int unionLeaderCol;

    //联系电话
    private int unionLeaderPhoneCol;

    //单位所属行业
    private int indIdCol;

    //社会信用代码
    private int identifierCodeCol;

    //建会日期
    private int creationDateCol;

    //基层工会类型
    private int unionTypeCol;







    private static ArrayList<String> getUnUnitList(List<HashMap<String, Object>> allUnitList) {
        logger.debug("--------------开始处理部门信息（查询不存在的）-----------");

        List<String> orgCs = new ArrayList<>();
        orgCs.add("应用事业部");
        // 二级单位
        String importsecondUnitStr = "/北京/天拓数信/职业教育事业部";
        // 记录不存在的部门名称list
        ArrayList<String> unOrgList = new ArrayList<String>();
        if (importsecondUnitStr != null
                && importsecondUnitStr.trim().length() > 0) {
            if (StringUtils.isNotBlank(importsecondUnitStr)) {

                if (importsecondUnitStr.startsWith("/")) {
                    importsecondUnitStr = importsecondUnitStr.substring(
                            importsecondUnitStr.indexOf("/"),
                            importsecondUnitStr.length());
                }
                if (importsecondUnitStr.endsWith("/")) {
                    importsecondUnitStr = importsecondUnitStr.substring(
                            importsecondUnitStr.lastIndexOf("/"),
                            importsecondUnitStr.length());
                }
            }

            ArrayList<String> list = new ArrayList<String>();
            list.add(importsecondUnitStr);
            // 将二级和三级拼接成 /xxx/xxx/xxx格式的字符串
            StringBuffer importOrgSb = new StringBuffer();

            String importOrg = importOrgSb.toString();
            // 查看在本单位的部门list和已插入的部门list是否存在，存在则不用进行拆解
            boolean orgFlag = false;
            for (int i = 0; i < allUnitList.size(); i++) {
                logger.debug(importsecondUnitStr.toString());
                logger.error(allUnitList.get(i).get("name") + ":"
                        + importsecondUnitStr);
                if (importsecondUnitStr.equals(allUnitList.get(i).get("name"))) {
                    orgFlag = true;
                    break;
                }
            }
            if (!orgFlag) {
                // 将/A/B/C拆解成(/A/B/C,/A/B,/A)
                if (importOrg.length() > 0) {
                    list.add(importOrg);
                    while (importOrg.lastIndexOf("/") > 0) {
                        importOrg = importOrg.substring(0,
                                importOrg.lastIndexOf("/"));
                        list.add(importOrg);
                    }
                }
            }

            if (list.size() > 0) {
                for (String str : list) {
                    // 标记导入文件内的部门是否在本部门存在
                    boolean flag = false;
                    for (int i = 1; i < orgCs.size(); i++) {
                        if (str.toString().equals(orgCs.get(i))) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        unOrgList.add(str.toString());
                    }
                }
            }
            logger.debug("--------------处理部门信息（查询不存在的）完毕，不存在部门信息获取成功-----------");

        }
        return unOrgList;
    }

    /**
     * map集合中模糊匹配
     * @param map
     * @param keyLike
     * @return
     */
    public static List<String> getLikeByMap(Map<String, String>map,String keyLike){
        List<String> list=new Vector<>();
        for (Map.Entry<String, String> entity : map.entrySet()) {
            if(entity.getKey().indexOf(keyLike)!= -1){
                list.add((String) entity.getValue());
            }

        }

        return list;
    }

}