package lgjt.web.app.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

import com.ttsx.util.cache.util.StringUtil;
import lgjt.common.base.utils.IocUtils;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.domain.app.user.SysUser;
import lgjt.domain.app.user.SysUserAuth;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.user.SysUserAuthService;
import lgjt.vo.app.user.SysUserVo;

/**
 * show 用户信息工具类.
 * <p>Title: SysUserUtil</p>  
 * <p>Description: </p>  
 * @author daijiaqi  
 * @date 2018年5月3日
 */
public class SysUserUtil {
	
	/**
	 * show 从用户对象内获取查询参数.
	 * <p>Title: getParamSignFromSysUser</p>  
	 * <p>Description: </p>  
	 * @author daijiaqi  
	 * @date 2018年5月3日  
	 * @param obj 用户信息，对应lgjt.domain.app.user中的实体类SysUser
	 * @return 参数集合
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static List<String> getParamSignFromSysUser(SysUser obj) throws IllegalArgumentException, IllegalAccessException{
		List<String> result = new ArrayList<String>();
		if(obj==null) {
			return result;
		}
		List<Field> fieldList = new ArrayList<>() ;
		Class tempClass = obj.getClass();
		//当父类为null的时候说明到达了最上层的父类(Object类).
		while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
		}
			  for (int j = 0; j < fieldList.size(); j++) {
				  if(fieldList.get(j).getModifiers() !=2) {
					 continue;
				  }
				  fieldList.get(j).setAccessible(true);
		            // 字段名  
//		            System.out.println(fields[j].getName() + ","+fields[j].getType().getName());  
		            // 字段值  
		            if (fieldList.get(j).getType().getName().equalsIgnoreCase(
		                    java.lang.String.class.getName())) {  
		                try {  
		                	Object value =fieldList.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fieldList.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            } else if (fieldList.get(j).getType().getName().equalsIgnoreCase(
		                    java.lang.Integer.class.getName())|| fieldList.get(j).getType().getName().equals("int")) {
		                try {  
		                	Object value=fieldList.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fieldList.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            }   else if (fieldList.get(j).getType().getName().equalsIgnoreCase(
		            		 java.lang.Long.class.getName())||fieldList.get(j).getType().getName().equals("long")) {
		                try {  
		                	Date value=(Date)fieldList.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fieldList.get(j).getName()+"="+value);
		                	}
		                } catch (Exception e) {} 
		            }    else if (fieldList.get(j).getType().getName().equalsIgnoreCase(
		                    java.util.Date.class.getName())) {  
		                try {  
		                	Date value=(Date)fieldList.get(j).get(obj);
		                	if(value!=null) {
		                		  result.add(fieldList.get(j).getName()+"="+SimpleDateFormatUtils.getStringDate(value, SimpleDateFormatUtils.PATTERN_TYPE_1));
		                	}
		                } catch (Exception e) {} 
		            }      else if (fieldList.get(j).getType().getName().equalsIgnoreCase(
							java.sql.Timestamp.class.getName())) {
						try {
							Timestamp value=(Timestamp)fieldList.get(j).get(obj);
							if(value!=null) {
								result.add(fieldList.get(j).getName()+"="+SimpleDateFormatUtils.getStringDate(value, SimpleDateFormatUtils.PATTERN_TYPE_1));
							}
						} catch (Exception e) {}
					}
			  }
		
			  return result;
	}




    /**
     * show 封装用戶表ID字段对应的名称，无返回值.
     * @author daijiaqi  
	 * @date 2018年5月3日  
     * @param sysUserVo 用户转换对象，对应lgjt.vo.app.user中的实体类SysUserVo
	 * @falg "1" 代表认证审核那张表，为了认证失败。
     * @param sysOrganizationService 组织机构
     */
	public static void setUserPropertys(SysUserVo sysUserVo ,String flag, final SysOrganizationService sysOrganizationService){
		if(sysUserVo==null){
			return ;
		}
		StringBuffer sbOrgIds =new StringBuffer ();
		String comId =StringUtil.trim(sysUserVo.getComId());
		String orgId =StringUtil.trim(sysUserVo.getOrgId());
		String unionId =StringUtil.trim(sysUserVo.getUnionId());
		if(comId.length()>0){
			if(sbOrgIds.length()>0){
				sbOrgIds.append(",");
			}
			sbOrgIds.append(StringUtil.trim(comId));
		}
		if(orgId.length()>0){
			if(sbOrgIds.length()>0){
				sbOrgIds.append(",");
			}
			sbOrgIds.append(StringUtil.trim(orgId));
		}
		if(unionId.length()>0){
			if(sbOrgIds.length()>0){
				sbOrgIds.append(",");
			}
			sbOrgIds.append(StringUtil.trim(unionId));
		}
		if(sbOrgIds.length()>0){
			Map<String,String> orgMap =	sysOrganizationService.getByIds(sbOrgIds.toString());
			sysUserVo.setOrgValue(orgMap.get(orgId));
			sysUserVo.setComValue(orgMap.get(comId));
			sysUserVo.setUnionValue(orgMap.get(unionId));
		}
		//设置性别 ?
		if(DictUtil.getDictMapByKey(RedisKeys.SEX_KEY)!=null){
			sysUserVo.setSexValue(DictUtil.getDictMapByKey(RedisKeys.SEX_KEY).get(sysUserVo.getSex()+""));
		}
		//民族'
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_NATION_KEY)!=null){
			sysUserVo.setNationValue(DictUtil.getDictMapByKey(RedisKeys.DICT_NATION_KEY).get(sysUserVo.getNation()));
		}
		//就业状况
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_WORKSTATUS_KEY)!=null) {
			sysUserVo.setWorkStatusValue(DictUtil.getDictMapByKey(RedisKeys.DICT_WORKSTATUS_KEY).get(sysUserVo.getWorkStatus() + ""));
		}
		//户籍类型
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_REGISTERED_KEY)!=null) {
			sysUserVo.setHouseholdRegisterValue(DictUtil.getDictMapByKey(RedisKeys.DICT_REGISTERED_KEY).get(sysUserVo.getHouseholdRegister()));
		}
		//有效证件类别
		if(DictUtil.getDictMapByKey(RedisKeys.CERTIFICATE_KEY)!=null) {
			sysUserVo.setIdTypeValue(DictUtil.getDictMapByKey(RedisKeys.CERTIFICATE_KEY).get(sysUserVo.getIdType() + ""));
		}
		//学历
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_EDUCATION_KEY)!=null) {
			sysUserVo.setEducationValue(DictUtil.getDictMapByKey(RedisKeys.DICT_EDUCATION_KEY).get(sysUserVo.getEducation()));
		}
		//技術等級
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_TECHNICAL_LEVEL_KEY)!=null) {
			sysUserVo.setTechnicalLevelValue(DictUtil.getDictMapByKey(RedisKeys.DICT_TECHNICAL_LEVEL_KEY).get(sysUserVo.getTechnicalLevel() + ""));
		}

		//会计变化类型
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY)!=null) {
			sysUserVo.setMembershipChangeTypeValue(DictUtil.getDictMapByKey(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY).get(sysUserVo.getMembershipChangeType() + ""));
		}

	    //会计变化原因
		if(DictUtil.getDictMapByKey(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY)!=null) {
			sysUserVo.setMembershipChangeReasonValue(DictUtil.getDictMapByKey(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY).get(sysUserVo.getMembershipChangeReason() + ""));
		}

		//认证状态
		List<SysUserAuth> sysUserAuths =IocUtils.getBean(SysUserAuthService.class).getSysUserAuthByUserId(sysUserVo.getId());

		if(flag!=null &&flag.trim().equals("1") ){//shenhebiao
			sysUserAuths =IocUtils.getBean(SysUserAuthService.class).getSysUserAuthByUserId(sysUserVo.getUserId());
		}else{//zhubiao
			sysUserAuths =IocUtils.getBean(SysUserAuthService.class).getSysUserAuthByUserId(sysUserVo.getId());
		}
		if(sysUserVo!=null && sysUserVo.getUserType()!=null && sysUserVo.getUserType()==SysUser.USERTYPE_AUTHENTICATEDUSER){
			sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_PASS+"");
			sysUserVo.setAuthStatusValue("认证完成");
		}else {
			if (sysUserAuths != null && sysUserAuths.size() > 0) {
				SysUserAuth sysUserAuth = sysUserAuths.get(0);
//			if(sysUserVo.getUserType()==SysUser.USERTYPE_AUTHENTICATEDUSER){
//				sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_PASS+"");
//				sysUserVo.setAuthStatusValue("认证完成");
//			}else{

				if (sysUserAuth.getStatus() == SysUserAuth.STATUS_AUDIT_NOTPASS) {
					sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_NOTPASS + "");
					sysUserVo.setAuthStatusValue("认证不通过");
				} else if (sysUserAuth.getStatus() == SysUserAuth.STATUS_AUDIT_WAIT) {
					sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_WAIT + "");
					sysUserVo.setAuthStatusValue("认证中");
				} else if (sysUserAuth.getStatus() == SysUserAuth.STATUS_AUDIT_PASS) {
					sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_PASS + "");
					sysUserVo.setAuthStatusValue("认证完成");
				}
//			}
			} else {
				sysUserVo.setAuthStatus(SysUserAuth.STATUS_AUDIT_UNVERIFIED + "");
				sysUserVo.setAuthStatusValue("未认证");
			}
		}
	}

	/**
	 * show 设置防篡改属性，无返回值.
	 * @author daijiaqi  
	 * @date 2018年5月3日  
	 * @param sysUser 用户信息，对应lgjt.domain.app.user中的实体类SysUser
	 */
	public static void setAntiTamperForSysUser(SysUser sysUser){
		try {
			List<String> properties = getParamSignFromSysUser(sysUser);
			sysUser.setAntiTamper(ParameterVerificationUtils.md5(	ParameterVerificationUtils.parametersSort(properties)));
		}catch(Exception e){}
	}

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		SysUser sysUser =new SysUser();
		sysUser.setComId("zzzz");
//		sysUser.setBirthDate(new Date());
		sysUser.setIdType(1);
		List<String> result = getParamSignFromSysUser(sysUser);
		for (String string : result) {
			System.out.println(string);
		}
	}
	

}
