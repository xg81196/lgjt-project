package lgjt.services.backend.user;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.EncryptUtil;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;
import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
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
import lgjt.common.base.utils.*;
import lgjt.domain.backend.attach.SysAttachment;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.services.backend.attach.SysAttachmentService;
import lgjt.services.backend.org.SysOrganizationService;
import lgjt.services.backend.org.SysUnionComService;
import lgjt.services.backend.org.SysUnionService;

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
public class ImportUserExcelService extends BaseService {

    private static final Logger logger = Logger.getLogger(ImportUserExcelService.class);

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


    public Results readExcel(Sheet sheet, SysUserAdmin userAdmin,String userData,String treeId,int type)
            throws  Exception {

        StringBuffer errorMsg  = new StringBuffer();

        int firstRowIndex = sheet.getFirstRowNum() + 1;
        int lastRowIndex = sheet.getLastRowNum();
        Row firstRow = sheet.getRow(firstRowIndex);
        List<Map<String, Object>> infoList = new ArrayList<>();
        // 读不到模板表头直接报错，不在记录错误信息
        if (firstRow == null) {
            // 记录日志
            return Results.parse(Constants.STATE_FAIL,"未成功读取模板表头行");
        }

        //判断表头是否正确
        if(firstRow.getPhysicalNumberOfCells() != 24)
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


        if (userNameCol != 0 || realNameCol == 0 || sexCol == 0
                || birthDateCol == 0 || nationCol == 0 || phoneNumberCol == 0
                || idTypeCol == 0 || idNoCol == 0 || orgIdCol == 0) {
            return Results.parse(Constants.STATE_FAIL,"表单标题列名称设置有问题，请重新设置后提交");
        }

        // 用户名List(用于判断用户是否重复)
        List<String> userNames = Lists.newArrayList();
        List<String> phoneNumbers = Lists.newArrayList();
        List<String> idNos = Lists.newArrayList();

        int countBlank = 0;
        for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {

            Row row = sheet.getRow(rIndex);
            //判断第一行是否有数据
          /*  if (row == null) {
                break;
            }*/


            String importUnique = tranferScientificNotation(row,
                    userNameCol);

            String importNameUnique = tranferScientificNotation(row,
                    realNameCol);

            String importSexUnique = tranferScientificNotation(row,
                    sexCol);

            String importBirthDateUnique = tranferScientificNotation(row,
                    birthDateCol);

            String importNationUnique = tranferScientificNotation(row,
                    nationCol);

            String importPhoneNumUnique = tranferScientificNotation(row,
                    phoneNumberCol);

            String importIdTypeUnique = tranferScientificNotation(row,
                    idTypeCol);

            String importInNoUnique = tranferScientificNotation(row,
                    idNoCol);

            String importOrgIdUnique = tranferScientificNotation(row,
                    orgIdCol);

            //有空格的空白行也视为表格为空
            if (rIndex > 1) {
                //连续两行为空
                if (StringUtils.isBlank(importUnique) && StringUtils.isBlank(importNameUnique)) {
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
                            row, userNameCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的用户名为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, realNameCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的姓名为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, sexCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的性别为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, birthDateCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的出生日期为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }


            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, nationCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的民族为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, phoneNumberCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的移动电话为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, idTypeCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的证件类别为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, idNoCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的证件号码为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }

            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isBlank(tranferScientificNotation(
                            row, orgIdCol))) {
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的所属企业/工会为必填项，请填写后再进行导入",
                                errorMsg);
                    }
                }
            }



            SimpleCriteria cri=Cnd.cri();
            cri.where().andEquals("user_name", importUnique);
            SysUser su=sysUserService.fetch(SysUser.class, cri);

            // 用户名文件内判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if ( sheet.getRow(i) != null ) {
                        if ( sheet.getRow(i).getCell(userNameCol) != null )  {
                            if (StringUtils.isNotBlank(tranferScientificNotation(
                                    row, userNameCol))
                                    && sheet.getRow(i).getCell(userNameCol).toString()
                                    .trim().equals(importUnique)) {
                                return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的用户名"+ importUnique +"已存在，请修改后再进行导入",
                                        errorMsg);
                            }
                        }

                    }

                }
            }
            // 用户名系统判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(tranferScientificNotation(
                            row, userNameCol))
                            && su != null && !userNames.contains(importUnique)) {
                        userNames.add(importUnique);
                        return Results.parse(Constants.STATE_FAIL,"您导入的文件中第--" + (rIndex + 1) + "--行中的用户名"+ importUnique +"在数据库中已存在，请修改后再进行导入",
                                errorMsg);
                    }
                }
            }

            // 手机号文件内判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if ( sheet.getRow(i) != null ) {
                        if ( sheet.getRow(i).getCell(phoneNumberCol) != null )  {
                            if (StringUtils.isNotBlank(tranferScientificNotation(
                                    row, phoneNumberCol))
                                    && sheet.getRow(i).getCell(phoneNumberCol).toString()
                                    .trim().equals(importPhoneNumUnique)) {
                                return Results.parse(Constants.STATE_FAIL,"您导入的文件中第--" + (rIndex + 1) + "--行中的移动电话"+ importPhoneNumUnique +"已存在，请修改后再进行导入",
                                        errorMsg);
                            }
                        }

                    }

                }
            }
            // 手机号系统判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(tranferScientificNotation(
                            row, phoneNumberCol))
                            && su != null && !phoneNumbers.contains(importPhoneNumUnique)) {
                        phoneNumbers.add(importPhoneNumUnique);
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的移动电话"+ importPhoneNumUnique +"在数据库中已存在，请修改后再进行导入",
                                errorMsg);
                    }
                }
            }

            // 证件号码文件内判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if ( sheet.getRow(i) != null ) {
                        if ( sheet.getRow(i).getCell(idNoCol) != null )  {
                            if (StringUtils.isNotBlank(tranferScientificNotation(
                                    row, idNoCol))
                                    && sheet.getRow(i).getCell(idNoCol).toString()
                                    .trim().equals(importInNoUnique)) {
                                return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的证件号码"+ importInNoUnique +"已存在，请修改后再进行导入",
                                        errorMsg);
                            }
                        }

                    }

                }
            }
            // 证件号码系统判重
            if (rIndex >= 1) {
                for (int i = rIndex - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(tranferScientificNotation(
                            row, idNoCol))
                            && su != null && !idNos.contains(importInNoUnique)) {
                        idNos.add(importInNoUnique);
                        return Results.parse(Constants.STATE_FAIL,"在您导入的文件中第--" + (rIndex + 1) + "--行中的证件号码"+ importInNoUnique +"在数据库中已存在，请修改后再进行导入",
                                errorMsg);
                    }
                }
            }


        }


        logger.debug("----------读取模板内容前的数据处理完成，开始读取内容----------");
        List<SysUser> userList = new ArrayList<>();
        int count = 0;
        // 读取内容
        for (int rIndex = firstRowIndex + 1; rIndex <= lastRowIndex; rIndex++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("wrong_row", rIndex);
            try {
                SysUser user = new SysUser();
                Row row = sheet.getRow(rIndex);

                //判断第一行是否有数据
              /*  if (row == null) {
                    break;
                }*/

                String username = tranferScientificNotation(row, userNameCol);

                if (rIndex > 1) {
                    //连续两行为空
                    if (StringUtils.isBlank( username )) {
                        count++;
                        continue;
                    }

                }

                if (count > 10) {

                    break;
                }


                String realName = row.getCell(realNameCol) == null ? "" : row
                        .getCell(realNameCol).toString().trim();

                String sex = row.getCell(sexCol) == null ? "" : row
                        .getCell(sexCol).toString().trim();

//                String birthDate = row.getCell(birthDateCol) == null ? "" : row
//                        .getCell(birthDateCol).toString().trim();
                String birthDate = tranferScientificNotation(row, birthDateCol);
                String nation = row.getCell(nationCol) == null ? "" : row
                        .getCell(nationCol).toString().trim();

                // 防止数字变成科学技术法的处理
                String phoneNumber = tranferScientificNotation(row,
                        phoneNumberCol);
                // java中和js中的正则表达式区别：需将\替换成\\
                String phonePattern = "(^13[0-9]{9}$)|(^15[0-9]{9}$)|(^18[0-9]{9}$)|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";
                if (StringUtils.isNotBlank(phoneNumber)
                        && phoneNumber.matches(phonePattern)) {
                    user.setPhoneNumber(phoneNumber);
                }

                String idType = row.getCell(idTypeCol) == null ? "" : row
                        .getCell(idTypeCol).toString().trim();

                // 防止数字变成科学技术法的处理
                String idNo = tranferScientificNotation(row,
                        idNoCol);

                String orgId = row.getCell(orgIdCol) == null ? "" : row
                        .getCell(orgIdCol).toString().trim();

                String email = row.getCell(emailCol) == null ? "" : row
                        .getCell(emailCol).toString().trim();

                String workStatus = row.getCell(workStatusCol) == null ? "" : row
                        .getCell(workStatusCol).toString().trim();

                String householdRegister = row.getCell(householdRegisterCol) == null ? "" : row
                        .getCell(householdRegisterCol).toString().trim();

                String education = row.getCell(educationCol) == null ? "" : row
                        .getCell(educationCol).toString().trim();

                String technicalLevel = row.getCell(technicalLevelCol) == null ? "" : row
                        .getCell(technicalLevelCol).toString().trim();

                String membershipChangeType = row.getCell(membershipChangeTypeCol) == null ? "" : row
                        .getCell(membershipChangeTypeCol).toString().trim();

                String membershipChangeDate = tranferScientificNotation(row,
                        membershipChangeDateCol);

                String membershipChangeReason = row.getCell(membershipChangeReasonCol) == null ? "" : row
                        .getCell(membershipChangeReasonCol).toString().trim();

                user.setUserName(username);
                user.setRealName(realName);

                IObjectCache objectCache = ObjectCache.getInstance();
                String sexKey = RedisKeys.getSysDictKey4User(RedisKeys.SEX_KEY);
                Map<String,String> dictMap4 =( Map<String,String> )objectCache.get(sexKey);
                if(StringUtils.isNotBlank(dictMap4.get(sex))){
                    user.setSex(Integer.valueOf(dictMap4.get(sex)));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行性别填写错误,请重新填写,例如:男性,女性等。");
                }
                if (StringUtils.isNotBlank(birthDate)) {
                    try {
                        Date f = new Date(birthDate);
                        user.setBirthDate(f);
                    }catch (Exception e ) {
                        e.printStackTrace();
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行出生日期填写异常，请重新填写。");
                    }
                }

                String nationKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_NATION_KEY);
                Map<String,String> dictMap =( Map<String,String> )objectCache.get(nationKey);
                if(StringUtils.isNotBlank(dictMap.get(nation))){
                    user.setNation(dictMap.get(nation));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行民族名称填写错误,请重新填写,例如:汉族,回族等。");
                }


                String idTypeKey = RedisKeys.getSysDictKey4User(RedisKeys.CERTIFICATE_KEY);
                Map<String,String> dictMap8 =( Map<String,String> )objectCache.get(idTypeKey);
                if(StringUtils.isNotBlank(dictMap8.get(idType))){
                    user.setIdType(Integer.valueOf(dictMap8.get(idType)));
                }else {
                    return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行证件类别填写错误,请重新填写,例如:身份证,护照,军官证。");
                }
                user.setIdNo(idNo);
                user.setEmail(email);
                user.setUserType(1);


                if (StringUtils.isNotBlank(workStatus)) {
                    String workStatusKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_WORKSTATUS_KEY);
                    Map<String,String> dictMap1 =( Map<String,String> )objectCache.get(workStatusKey);

                    if(StringUtils.isNotBlank(dictMap1.get(workStatus))){
                        user.setWorkStatus(Integer.valueOf(dictMap1.get(workStatus)));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行就业状况填写错误,请重新填写,例如:在岗、 待（下）岗、失业、退休、离休、退职、退养（内退）、病休、其他。");
                    }
                }

                if (StringUtils.isNotBlank(householdRegister)) {
                    String householdRegisterKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_REGISTERED_KEY);
                    Map<String,String> dictMap2 =( Map<String,String> )objectCache.get(householdRegisterKey);
                    if(StringUtils.isNotBlank(dictMap2.get(householdRegister))){
                        user.setHouseholdRegister(dictMap2.get(householdRegister));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行会籍类型填写错误，请重新填写，例如：未落常住户口、非农业家庭户口、农业家庭户口、非农业集体户口、农业集体户口、自理口粮户口、寄住户口、赞助户口、其他户口。");
                    }
                }

                if (StringUtils.isNotBlank(education)) {
                    String educationKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_EDUCATION_KEY);
                    Map<String,String> dictMap3 =( Map<String,String> )objectCache.get(educationKey);
                    if(StringUtils.isNotBlank(dictMap3.get(education))){
                        user.setEducation(dictMap3.get(education));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行学历名称填写错误，请重新填写，例如：专科毕业、大学毕业、大学本科、研究生毕业。");
                    }
                }

                if (StringUtils.isNotBlank(technicalLevel)) {
                    String technicalKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_TECHNICAL_LEVEL_KEY);
                    Map<String,String> dictMap5 =( Map<String,String> )objectCache.get(technicalKey);
                    if(StringUtils.isNotBlank(dictMap5.get(technicalLevel))){
                        user.setTechnicalLevel(Integer.valueOf(dictMap5.get(technicalLevel)));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行技术等级填写错误，请重新填写，例如：职业资格一级（高级技师）、职业资格二级（技师）、职业资格三级（高级）、职业资格四级（中级）、职业资格五级（初级）。");
                    }
                }

                if (StringUtils.isNotBlank(membershipChangeType)) {
                    String membershipChangeKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY);
                    Map<String,String> dictMap6 =( Map<String,String> )objectCache.get(membershipChangeKey);
                    if(StringUtils.isNotBlank(dictMap6.get(membershipChangeType))){
                        user.setMembershipChangeType(Integer.valueOf(dictMap6.get(membershipChangeType)));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行会籍变化类型填写错误，请重新填写，例如：入会、转入、保留、转出、除名、退会。");
                    }
                }

                if (StringUtils.isNotBlank(membershipChangeDate)) {
                    try {
                        Date f = new Date(membershipChangeDate);
                        user.setMembershipChangeDate(f);
                    }catch (Exception e ) {
                        e.printStackTrace();
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行会籍变化日期填写异常，请重新填写。");
                    }
                }

                if (StringUtils.isNotBlank(membershipChangeReason)) {
                    String membershipChangeReasonKey = RedisKeys.getSysDictKey4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY);
                    Map<String,String> dictMap7 =( Map<String,String> )objectCache.get(membershipChangeReasonKey);
                    if(StringUtils.isNotBlank(dictMap7.get(membershipChangeReason))){
                        user.setMembershipChangeReason(Integer.valueOf(dictMap7.get(membershipChangeReason)));
                    }else {
                        return Results.parse(Constants.STATE_FAIL, "第"+(rIndex + 1)+"行会籍变化原因填写错误，请重新填写，例如：劳动（工作）关系发生变化、在本单位下岗后待岗、失业、所在企业破产、个人要求退会、因犯罪被开除会籍。");
                    }
                }

                user.setSalt(RandomStringUtils.randomAlphanumeric(20));

                String entryPass = DigestUtils.md5Hex(DigestUtils.md5Hex("111111")+user.getSalt());

                user.setPassword(entryPass);//密码
                user.setExtend1("111111");
                user.setExtend2(entryPass);//密码
                user.setStatus(0);
                user.setCrtIp(ClientInfo.getIp());
                user.setCrtUser(userAdmin.getUserName());
                user.setCrtTime(new Date());
                user.setId(UUIDUtil.getUUID());
                //如果有导入的格式不正确，数据也不入库
                if (errorMsg.length() == 0) {

                    if(StringTool.isNotEmpty(orgId)){
                        String unionId = "";
                        String comId = "";
                        if (type == UserDataType.UNION_TYPE) {
                            List<SysUnion> sysUnionList = IocUtils.getBean(SysUnionService.class).querySubUnionList(treeId);
                            if (CollectionUtils.isNotEmpty(sysUnionList)) {
                                for (SysUnion sysUnion : sysUnionList) {
                                    if (sysUnion.getName().equals(orgId)) {
                                        unionId = sysUnion.getId();
                                        break;
                                    }
                                    List<SysUnionCom> sysUnionComList = IocUtils.getBean(SysUnionComService.class).getUnionComByUnionId(sysUnion.getId());
                                    if (CollectionUtils.isNotEmpty(sysUnionComList)) {

                                        for (SysUnionCom sysUnionCom : sysUnionComList) {
                                            SysOrganization sysOrganization =  IocUtils.getBean(SysOrganizationService.class).get(sysUnionCom.getComId());
                                             if (sysOrganization.getName().equals(orgId)){
                                                 comId = sysOrganization.getId();
                                                 break;
                                             }
                                        }
                                    }
                                }
                            }
                        }else {
                             SimpleCriteria cri4= Cnd.cri();
                             //cri4.where().andEquals("name", orgId);
                             cri4.where().andIn("id",treeId);
                            //SysUnion union = IocUtils.getBean(SysUnionService.class).fetch(SysUnion.class, cri4);
                            SysOrganization organization=sysOrganizationService.fetch(SysOrganization.class, cri4);
                            if (organization != null ) {
                                if (orgId.equals(organization.getName())) {
                                    comId = organization.getId();
                                }
                            }
                        }


                               if(StringUtils.isNotBlank(unionId) || StringUtils.isNotBlank(comId)){
                                   user.setComId(comId);
                                   user.setUnionId(unionId);
                                   //user.setOrgId(organization.getId());

                               } else {
                                   //穷举几位进行匹配
                                   String unit_0 = StringUtils.substring(orgId,0,2);
                                   String unit_1 = StringUtils.substring(orgId,0,3);
                                   String unit_2 = StringUtils.substring(orgId,0,4);
                                   String unit_3 = StringUtils.substring(orgId,0,5);
                                   SimpleCriteria cri5= Cnd.cri();
                                   cri5.where().and("name", "REGEXP", unit_0+"|"+unit_1+"|"+unit_2+"|"+unit_3).andIn("id",userData.split(","));
                                   List<SysOrganization> companyList = sysOrganizationService.query(SysOrganization.class, cri5);
                                   List<String> companyNamess = Lists.newArrayList();
                                   for (SysOrganization com : companyList)  {
                                       companyNamess.add(com.getName());
                                   }
                                   return Results.parse(Constants.STATE_FAIL,"第"+(rIndex + 1)+"行所属企业/工会:" +orgId+ "不存在于您的权限范围内,  系统存在您的权限范围内匹配度高的机构为:"+ Joiner.on(",").skipNulls().join(companyNamess), errorMsg);
                               }
                    }else{
                        return Results.parse(Constants.STATE_FAIL,"第"+(rIndex + 1)+"行所属企业/工会为必填项，请填写机构全称。", errorMsg);
                    }
                    if (errorMsg.length()==0) {
                        userList.add(user);
                    }
                }
            } catch (Exception e) {
                logger.error(e, e);
                return Results.parse(Constants.STATE_FAIL,"你上传的文件读取过程发生异常，请确认所传信息是否符合要求。", errorMsg);
            }

        }
        try {
            IocUtils.getBean(SysUserService.class).getDao().fastInsert(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Results.parse(Constants.STATE_FAIL,"你上传的文件读取过程发生异常。", errorMsg);
        }
        logger.debug(errorMsg);
        if (errorMsg.length()==0) {
            errorMsg.append("success");
            String msg = "";

            return Results.parse(Constants.STATE_SUCCESS,"导入数据成功！");
        } else {
//            saveFailloginExcel(infoList,userAdmin);
            // 插入不成功
            // TODO 回滚插入的企业部门
           // rollBackUnit(insertedCompanyList);
            return Results.parse(Constants.STATE_FAIL, errorMsg.toString());
        }
    }

//    private void importOrg(SysUser user, String dept, SysOrganization organization) {
//
//        SimpleCriteria cri;
//        user.setOrgId(organization.getId());
//
//        if(StringTool.isNotEmpty(dept)){
//            cri= Cnd.cri();
//            cri.where().andEquals("name", dept);
//            cri.where().andEquals("super_id", company.getId());
//            SysOrganization so=sysOrganizationService.fetch(SysOrganization.class, cri);
//            if(so!=null){
//                user.setOrgId(so.getId());
//            }else{
//                SysOrganization so1=new SysOrganization();
//                so1.setId(UUID.randomUUID().toString());
//                so1.setName(dept);
//            /*    cri=Cnd.cri();
//                cri.where().andLikeL("org_number", company.getCompSerialNum());
//                cri.getOrderBy().desc("org_number");
//                SysOrganization list1=sysOrganizationService.fetch(SysOrganization.class, cri);
//                if(list1!=null){
//                    String s=list1.getOrgNumber();
//                    Integer in=Integer.parseInt(s)+1;
//                    so1.setOrgNumber(in.toString());
//                }*/
//                so1.setSuperId(company.getId());
//                so1.setSort(1);
//                so1.setState(1);
////								so1.setCrtIp("127.0.0.1");
////								so1.setCrtUser("admin");
//                so1.setCrtIp(ClientInfo.getIp());
//                so1.setCrtUser(UserUtil.getUser().getUserName());
//                SysOrganization so2=sysOrganizationService.insert(so1);
//                if(so2!=null){
//                    user.setOrgId(so2.getId());
//                }
//            }
//        }else {
///*cri= Cnd.cri();
//cri.where().andLike("name", dept);
//List<SysOrganization> organizationList = sysOrganizationService.query(SysOrganization.class, cri);
//List<String> orgNames = Lists.newArrayList();
//for (SysOrganization org : organizationList)  {
//orgNames.add(org.getName());
//}
//addLogAndErrorMsg("企业" +unit+ "不存在,系统存在匹配度高的企业为"+ Joiner.on(",").skipNulls().join(companyList), errorMsg);*/
//            //addLogAndErrorMsg("所属部门为必填项", errorMsg);
//        }
//                                       /* } else {
//                                            addLogAndErrorMsg("企业" +unit+ "不存在,  系统存在匹配度高的企业为", errorMsg);
//                                            addWrongMap(rIndex, "企业" +unit+ "不属于所在企业", "只能导入自己所属企业或下级企业",
//                                                    infoList);
//                                        }*/
//    }

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
                        UserInfoConstants.USERINFO_USERNAME)) {
                    userNameCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(
                        UserInfoConstants.USERINFO_REALNAME)) {
                    realNameCol = cIndex;
                    continue;
                }

                if (cell.toString().contains(UserInfoConstants.USERINFO_SEX)) {
                    sexCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_BIRTHDATE)) {
                    birthDateCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_NATION)) {
                    nationCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_PHONENUMBER)) {
                    phoneNumberCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_IDTYPE)) {
                    idTypeCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_IDNO)) {
                    idNoCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_ORGID)) {
                    orgIdCol = cIndex;
                    continue;
                }

                // 非必填字段
                if (cell.toString().contains(
                        UserInfoConstants.USERINFO_EMAIL)) {
                    emailCol = cIndex;
                    continue;
                }

                if (cell.toString().contains(UserInfoConstants.USERINFO_WORKSTATUS)) {
                    workStatusCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_HOUSEHOLDREGISTER)) {
                    householdRegisterCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_EDUCATION)) {
                    educationCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_TECHNICALLEVEL)) {
                    technicalLevelCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_MEMBERSHIPCHANGETYPE)) {
                    membershipChangeTypeCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_MEMBERSHIPCHANGEDATE)) {
                    membershipChangeDateCol = cIndex;
                    continue;
                }
                if (cell.toString().contains(UserInfoConstants.USERINFO_MEMBERSHIPCHANGEREASON)) {
                    membershipChangeReasonCol = cIndex;
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
    //用户名
    private int userNameCol;

    //姓名
    private int realNameCol;

    //性别
    private int sexCol;

    //出生日期
    private int birthDateCol;

    //民族
    private int nationCol;

    //移动电话
    private int phoneNumberCol;

    //有效证件类别
    private int idTypeCol;

    //证件号码
    private int idNoCol;

    //所属企业/工会
    private int orgIdCol;

    //邮箱
    private int emailCol;

    //就业状况
    private int workStatusCol;

    //户籍类型
    private int householdRegisterCol;

    //学历
    private int educationCol;

    //技术等级
    private int technicalLevelCol;

    //会籍变化类型
    private int membershipChangeTypeCol;

    //会籍变化日期
    private int membershipChangeDateCol;

    //会籍变化原因
    private int membershipChangeReasonCol;


    public static void main(String[] args) {
        List<HashMap<String, Object>> allUnitList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","监理事业部");
        allUnitList.add(map);
        getUnUnitList(allUnitList);
    }


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
