package lgjt.web.backend.module.admin.org;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.platform.tool.util.UUIDUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.Authority;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.tree.CallBackTree;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.CodeGenerateUtils;
import lgjt.common.base.utils.IocUtils;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.org.vo.SysOrganizationVo;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.user.SysUserRole;
import lgjt.services.backend.org.*;
import lgjt.services.backend.role.SysRoleService;
import lgjt.services.backend.user.SysUserAdminService;
import lgjt.services.backend.user.SysUserDataService;
import lgjt.web.backend.init.TreeCacheImpl;
import lgjt.web.backend.utils.ExportUtils;
import lgjt.domain.backend.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@At("/admin/organization")
@IocBean
@Log4j
public class SysOrganizationModule {

	//请求的前缀
	String remoteUrlPrefix =  PropertyUtil.getProperty("REMOTE_URL_PREFIX");

    
	@Inject("sysOrganizationService")
	SysOrganizationService service;

	@Inject("sysUserDataService")
	SysUserDataService sysUserDataService;

	@Inject
	SysUserAdminService sysUserAdminService;

	@Inject
	SysRoleService sysRoleService;

	@Inject
	SysUnionComService sysUnionComService;

	@At("/get")
	@Authority("USER_COMP")
	public Object get(@Param("id") String id) {
		SysOrganization obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/delete")
	@Authority("USER_COMP")
	public Object delete(@Param("ids") String ids) {
		if(service.delete(ids)>0) {
			TreeCacheImpl.getInstance().refresh();
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL,"工会或企业下有子企业或人员");
		}
	}
	
	@At("/save")
	@Authority("USER_COMP")
	public Object insert(@Param("..") SysOrganization obj) {
		SysOrganization o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}


	@At("/insertUpdate")
	@Authority("USER_COMP")
	public Object insertUpdate(@Param("..") SysOrganization obj) {

		SysOrganization sysOrganization;

		/**
		 * 2018-07-20 赵天意
		 * BUG 11289
		 * 判断条件有误
		 */
		boolean result = false;
		if(StringTool.isNull(obj.getId())) {

			sysOrganization = service.checkOrg_name(obj.getName());
			if (!Objects.isNull( sysOrganization )) {
				return Results.parse(Constants.STATE_FAIL,"企业名称重复");
			}

			sysOrganization = service.checkOrg_code(obj.getOrgCode());
			if (!Objects.isNull( sysOrganization )) {
				return Results.parse(Constants.STATE_FAIL,"企业代码重复");
			}

			sysOrganization = service.checkOrg_identifier_code(obj.getIdentifierCode());
			if (!Objects.isNull( sysOrganization )) {
				return Results.parse(Constants.STATE_FAIL,"社会信用代码重复");
			}

			if (StringUtils.isNotBlank(obj.getSuperId())){
				sysOrganization = service.checkOrg_name(obj.getSuperId());
				if (Objects.isNull( sysOrganization )) {
					return Results.parse(Constants.STATE_FAIL,"上级企业不存在");
				}

				if ( sysOrganization.getStatus() != null ) {
					if ( 1 == sysOrganization.getStatus() ) {
						return Results.parse(Constants.STATE_FAIL,"该企业已禁用");
					}
				}
				obj.setExtend3(obj.getSuperId());
				obj.setSuperId(sysOrganization.getId());
			}else{
				obj.setSuperId("-1");
				sysOrganization = service.checkOrg_code(obj.getOrgCode());
				if (!Objects.isNull( sysOrganization )) {
					return Results.parse(Constants.STATE_FAIL,"企业代码重复");
				}

				sysOrganization = service.checkOrg_identifier_code(obj.getIdentifierCode());
				if (!Objects.isNull( sysOrganization )) {
					return Results.parse(Constants.STATE_FAIL,"社会信用代码重复");
				}

			}

			String id = UUIDUtil.getUUID();
			obj.setId(id);
			obj.setExtend2(obj.getOrgId());
			service.insert(obj);

			//自动添加管理员权限
			/*if (SysUserAdmin.ADMIN.equals(UserUtil.getAdminUser().getUserName())) {*/
				initUserData(id, UserUtil.getAdminUser());
			/*} else {*/

				//添加对应的管理员
				SysUserAdmin sysAdmin = initSysUserAdmin(obj, id);

				//添加对应的菜单权限

				initUserRole(obj, sysAdmin);

				//添加数据权限
				initUserData(id, sysAdmin);

			/*}*/

			result = true;
		}else {

			if(StringUtil.isNotEmpty(obj.getSuperId())) {
				sysOrganization = service.checkOrg_name(obj.getSuperId());
				if (!Objects.isNull( sysOrganization )) {
					obj.setExtend3(obj.getSuperId());
					obj.setSuperId(sysOrganization.getId());
				} else {
					return Results.parse(Constants.STATE_FAIL,"上级企业不存在");
				}
			}

			obj.setExtend2(obj.getOrgId());
			int upd = service.updateIgnoreNull(obj);
			if(upd>0) {
				result = true;
				SimpleCriteria cri = Cnd.cri();
				cri.where().andEquals("com_id",obj.getId());
				sysUnionComService.delete(SysUnionCom.class,cri);
				//工会和企业对应关系
				SysUnionCom sysUnionCom = new SysUnionCom();
				sysUnionCom.setComName(obj.getName());
				sysUnionCom.setUnionName(obj.getSuperOrgName());
				sysUnionCom.setUnionId(obj.getOrgId());
				sysUnionCom.setComId(obj.getId());
				IocUtils.getBean(SysUnionComService.class).insert(sysUnionCom);
			}
		}
		TreeCacheImpl.getInstance().refresh();
		if(result) {
			return Results.parse(Constants.STATE_SUCCESS,null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	private void initUserData(String id, SysUserAdmin sysAdmin) {
		SysUserData sysUserData = new SysUserData();
		sysUserData.setId(UUIDUtil.getUUID());
		sysUserData.setAdminUserId(sysAdmin.getId());
		sysUserData.setObjectId(id);
		sysUserData.setObjectType(UserDataType.COM_TYPE);
		sysUserData.setCompatibility(0);
		sysUserData.setCrtIp(ClientInfo.getIp());
		sysUserData.setCrtUser(UserUtil.getAdminUser().getUserName());
		sysUserData.setCrtTime(new Date());
		sysUserDataService.insert(sysUserData);
	}

	private void initUserRole(SysOrganization obj, SysUserAdmin sysAdmin) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setAdminUserId(sysAdmin.getId());
		sysUserRole.setRoleId(sysRoleService.checkRole_name("企业管理员").getId());
		sysUserRole.setUserName(sysAdmin.getUserName());
		sysUserRole.setCrtUser(sysAdmin.getUserName());
		sysUserRole.setCrtTime(new Date());
		sysUserRole.setCrtIp(ClientInfo.getIp());
		sysRoleService.insert(sysUserRole);
	}

	private SysUserAdmin initSysUserAdmin(SysOrganization obj, String id) {
		SysUserAdmin sysAdmin = new SysUserAdmin();

		SimpleCriteria cri4 = Cnd.cri();
		cri4.where().andEquals("id", obj.getOrgId());
		SysUnion union = IocUtils.getBean(SysUnionService.class).fetch(SysUnion.class, cri4);
		if (union != null) {
			obj.setAreaName(union.getUnitScope());
		}

		/**
		 * 处理不选工会
		 */
		String area = "";
		if (StringUtils.isNotBlank(obj.getAreaName()))  {
			String[] areas = obj.getAreaName().split(",");

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
		}else{
			sysAdmin.setUserName(CodeGenerateUtils.generateCode());
		}

		//sysAdmin.setUserName( obj.getName());
		sysAdmin.setUserName( area+ CodeGenerateUtils.generateCode());
		String name = sysAdmin.getUserName().hashCode()+"";
		if (name.length()>6){
			sysAdmin.setExtend2(StringUtils.leftPad(name.substring(name.length()-6,name.length()),6,"0"));
		}else {
			sysAdmin.setExtend2(StringUtils.leftPad(name,6,"0"));
		}
		sysAdmin.setRealName( obj.getName());
		sysAdmin.setSalt(RandomStringUtils.randomAlphanumeric(20));
		sysAdmin.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(sysAdmin.getExtend2())+sysAdmin.getSalt()));
		sysAdmin.setExtend3(sysAdmin.getPassword());
		sysAdmin.setExtend4(sysRoleService.checkRole_name("企业管理员").getId());
		sysAdmin.setStatus(0);
		sysAdmin.setSex(1);
		sysAdmin.setPhoneNumber(obj.getUnionLeaderPhone());
		sysAdmin.setOrgId(id);
		sysAdmin.setCrtUser(UserUtil.getAdminUser().getUserName());
		sysAdmin.setCrtTime(new Date());
		sysAdmin.setCrtIp(ClientInfo.getIp());
		sysUserAdminService.insert(sysAdmin);

		//工会和企业对应关系
		SysUnionCom sysUnionCom = new SysUnionCom();
		sysUnionCom.setComName(obj.getName());
		sysUnionCom.setUnionName(obj.getSuperOrgName());
		sysUnionCom.setUnionId(obj.getOrgId());
		sysUnionCom.setComId(id);
		IocUtils.getBean(SysUnionComService.class).insert(sysUnionCom);
		return sysAdmin;
	}


	@At("/queryPage")
	@Authority("USER_COMP")
	public Object queryPage(@Param("..") SysOrganization obj) {
		PageResult<SysOrganizationVo> pageResult  = service.queryPage(obj,UserUtil.getAdminUser().getUserName());
		return Results.parse(Constants.STATE_SUCCESS,null,pageResult);
	}

	/**
	 * 	列表（含模糊检索）
	 */

	@At("/query")
	@Authority("USER_COMP")
	public Object query(@Param("..") SysOrganization obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryOrg(obj,UserUtil.getAdminUser(),UserUtil.getAdminComUserData()));
	}


	/**
	 * 查询所有机构
	 * @param obj
	 * @return
	 */
	@At("/orgTree")
	@Authority("USER_COMP")
	public Object orgTree(@Param("..") SysOrganization obj) {

		return  Results.parse(Constants.STATE_SUCCESS, "",service.queryOrganizationTree(obj.getType(),UserUtil.getAdminComUserData(),UserUtil.getAdminUser().getUserName()));

	}

	/**
	 * 查询权限机构
	 * @param obj
	 * @return
	 */
	@At("/orgAuthTree")
	@Authority("USER_COMP")
	public Object orgAuthTree(@Param("..") SysOrganization obj) {

		SysOrganization fixedTopRes = sysUserDataService.getFixedTopRes();
		List<SysOrganization> organizationList = sysUserDataService.querySubOrgList(UserUtil.getAdminUser().getOrgId());
		fixedTopRes.setList(organizationList);
		List<SysOrganization> result = new ArrayList<>();
		result.add(fixedTopRes);

		return  Results.parse(Constants.STATE_SUCCESS, "",result);

	}

	/**
	 * 数据导入接口
	 * @author
	 * @date
	 * @param
	 */
	@At("/orgUpload")
	@com.ttsx.platform.nutz.mvc.annotation.Authority("")
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object upload(@Param("file") TempFile file,@Param("orgId") String  orgId){
		try{
			Results results = new Results();
			if(file.getFile().getName().endsWith(".xls") || file.getFile().getName().endsWith(".xlsx")){
				if (!"all".equals(orgId)) {
					ImportOrgExcelService importUserExcel = new ImportOrgExcelService();

					results = importUserExcel.readExcel(importUserExcel.getSheetByExcelType(file.getFile()), UserUtil.getAdminUser(), orgId, new CallBackTree() {
						@Override
						public void callBack() {
							TreeCacheImpl.getInstance().refresh();
						}
					});
					if (results.getCode().equals(Constants.STATE_FAIL)) {

						return Results.parse(Constants.STATE_FAIL, results.getMsg());
					}
				}else{
					ImportOrgExcelService2 importUserExcel2 = new ImportOrgExcelService2();

					results = importUserExcel2.readExcel(importUserExcel2.getSheetByExcelType(file.getFile()),UserUtil.getAdminUser(),orgId,new CallBackTree() {
						@Override
						public void callBack() {
							TreeCacheImpl.getInstance().refresh();
						}
					});
					if (results.getCode().equals(Constants.STATE_FAIL)) {

						return Results.parse(Constants.STATE_FAIL, results.getMsg());
					}
				}

			}else{
				return Results.parse(Constants.STATE_FAIL,"只能上传.xls或.xlsx格式的文件！请确认后再次上传.");
			}
			return Results.parse(Constants.STATE_SUCCESS,"企业/工会导入成功",results.getData());
		}catch(Exception e){
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,e.getMessage());
		}
	}
    /**
     * 企业导入模板下载接口
     * @Description:
     * @param:  @return
     * @return:  Object
     * @throws
     * @author  majinyong
     * @date  2017-7-11
     */
    @At("/downloadFile")
    @com.ttsx.platform.nutz.mvc.annotation.Authority("")
    @Ok("raw")
    public Object downloadFile() {
        try {
            HttpServletRequest request = Mvcs.getReq();
            HttpServletResponse resp = Mvcs.getResp();
            //获取文件路径
            String filepath = request.getServletContext().getRealPath("/downTemplate/orgUploadTemplate.xlsx");
            //下载后的文件名
            String filename ="企业/工会批量导入模板";
            ExportUtils.downLoadTemplateFromServer(filepath, resp, filename);//下载
        } catch (Exception e) {
        }
        return null;
    }

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(new String("澶т緺".getBytes("gb2312")));
	}

	@At("/")
    @POST
    public Object getOrgBySQLServer(@Param("authKey") String authKey) {
        if(authKey.equals("111111")) {
            int count = service.getOrgBySQLServer();
            return Results.parse(Constants.STATE_SUCCESS,"成功导入企业信息"+count+"条");
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }
}