package lgjt.web.backend.module.admin.user;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.mvc.annotation.Authority;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringTool;
import lombok.extern.log4j.Log4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.ClientInfo;

import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.vo.SysUserVo2;
import lgjt.services.backend.user.ImportUserExcelService;
import lgjt.services.backend.user.SysUserService;
import lgjt.web.backend.utils.ExportUtils;
import lgjt.domain.backend.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * 用户管理
 * @author daijiaqi
 */
@At("/admin/sysUser")
@IocBean
@Log4j
public class SysUserModule {

	//请求的前缀
	String remoteUrlPrefix =  PropertyUtil.getProperty("REMOTE_URL_PREFIX");

    
	@Inject("sysUserService")
	SysUserService service;


	/**
	 * 根据ID查询用户
	 * @param id
	 * @return
	 */
	@At("/get")
	public Object get(String id) {
		SysUserVo2 obj = service.get2(id);
		if(null != obj) {
			if (!obj.getPassword().equals(obj.getExtend2())) {
				obj.setPassword("");
				return Results.parse(Constants.STATE_SUCCESS,null,obj);
			}
			obj.setPassword(obj.getExtend1());
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	/**
	 * 根据ID批量删除用户
	 * @param ids
	 * @return 删除条数
	 */
	@At("/delete")
	public Object delete(@Param("ids") String ids) {
		int count = service.delete(ids);
		if(count>0){
			/*Map<String, Object> params =  Maps.newHashMap();
			params.put("ids",ids);
			Results results = HttpClientUtil.get(remoteUrlPrefix+ PropertyUtil.getProperty("REMOTE_DELETE_USER"),params);
			if (!Constants.STATE_SUCCESS.equals(results.getCode())) {
				return Results.parse(Constants.STATE_FAIL,"删除失败");
			}*/
			return Results.parse(Constants.STATE_SUCCESS,"删除成功");
		}else{
			return Results.parse(Constants.STATE_FAIL,"删除失败");
		}
	
	}
	
	/**
	 * 新增用户
	 * @param obj 用户对象
	 * @return
	 */
	@At("/insert")
	@POST
	public Object insert(@Param("..") SysUser obj) {

		/**
		 * 2018年07月29日17:55:39 赵天意
		 * 验证用户名的唯一性
		 */

		SysUser user = service.checkUser_name(obj.getUserName());
		if( user != null ) {
			return Results.parse(Constants.STATE_FAIL,"用户名重复！");
		}

		if( service.checkPhoneNumber(obj.getPhoneNumber()) != null) {
			return Results.parse(Constants.STATE_FAIL,"手机号已被使用！");
		}

		if(service.checkPersonId(obj.getIdNo()) != null) {
			return Results.parse(Constants.STATE_FAIL,"身份证号已被使用！");
		}

		if( obj.getType() != null ) {
			if (obj.getType() == UserDataType.UNION_TYPE) {
				obj.setUnionId(obj.getOrgId());
			}else{
				obj.setComId(obj.getOrgId());
			}
		}else {
			obj.setUnionId(obj.getOrgId());
		}

		obj.setExtend3(obj.getSuperName());
		obj.setExtend4(obj.getType());
		//obj.setUnionId(obj.getOrgId());
		obj.setSalt(RandomStringUtils.randomAlphanumeric(20));
		String entryPass = DigestUtils.md5Hex(DigestUtils.md5Hex("111111")+obj.getSalt());

		obj.setPassword(entryPass);//密码
		obj.setExtend1("111111");
		obj.setExtend2(entryPass);//密码
		obj.setCrtIp(ClientInfo.getIp());
		obj.setCrtUser(UserUtil.getAdminUser().getUserName());
		obj.setCrtTime(new Date());

		SysUser sysUser = service.insert(obj);
		//Todo 1 权限验证，初步想法，平台管理员可以无限制，企业管理员添加本企业，工会管理员添加本公会。 后续再完善吧......
		
		
		if(sysUser!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,sysUser);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}




	/**
	 * 数据导入接口
	 * @author
	 * @date
	 * @param
	 */
	@At("/userUpload")
	@Authority("")
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object upload(@Param("file") TempFile file, @Param("orgId") String  orgId, @Param("type") int  type){
		try{
			Results results = new Results();
			if(file.getFile().getName().endsWith(".xls") || file.getFile().getName().endsWith(".xlsx")){
				ImportUserExcelService importUserExcel = new ImportUserExcelService();

				results = importUserExcel.readExcel(importUserExcel.getSheetByExcelType(file.getFile()),UserUtil.getAdminUser(),UserUtil.getAdminUnionAndComUserData(),orgId,type);
				if (results.getCode().equals(Constants.STATE_FAIL)) {

					return Results.parse(Constants.STATE_FAIL, results.getMsg());
				}
			}else{
				return Results.parse(Constants.STATE_FAIL,"只能上传.xls或.xlsx格式的文件！请确认后再次上传.");
			}
			return Results.parse(Constants.STATE_SUCCESS,"人员导入成功",results.getData());
		}catch(Exception e){
			e.printStackTrace();
			return Results.parse(Constants.STATE_FAIL,e.getMessage());
		}
	}

	/**
	 * 用户导入模板下载接口
	 * @Description:
	 * @param:  @return
	 * @return:  Object
	 * @throws
	 * @author  majinyong
	 * @date  2017-7-11
	 */
	@At("/downloadFile")
	@Authority("")
	@Ok("raw")
	public Object downloadFile() {
        try {
                HttpServletRequest request = Mvcs.getReq();
                HttpServletResponse resp = Mvcs.getResp();
                //获取文件路径
                String filepath = request.getServletContext().getRealPath("/downTemplate/userUploadTemplate.xlsx");
                //下载后的文件名
                String filename ="用户批量导入模板";
                ExportUtils.downLoadTemplateFromServer(filepath, resp, filename);//下载
        } catch (Exception e) {
        }
        return null;
	}
	
	
	@At("/update")
	@POST
	public Object update(@Param("..") SysUser obj) {
		int upd = service.updateIgnoreNull(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/queryPage")
	public Object queryPage(@Param("..") SysUser obj,@Param("orgId")String orgId,@Param("type")int type) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageUsers(obj,orgId,type));
		//return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageUsersNoTree(obj, UserUtil.getAdminUser()));
	}

	@At("/query")
	public Object query(@Param("..") SysUser obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.query(obj));
	}


	@At("/checkId")
	@GET
	public Object checkId(String value) {
		return service.checkId(value);
	}
	
	@At("/checkUser_name")
	@GET
	public Object checkUser_name(String value) {
		return service.checkUser_name(value);
	}


	@At("/updatePassword")
	@Authority("")
	public Object updatePassword(@Param("..") SysUser user) {
		try {

			String newpwd = user.getNewpwd();// 新密码

			if (StringTool.isEmpty(newpwd)) {
				return Results.parse(Constants.STATE_FAIL, "新密码不能为空");
			}

			SysUser sysUser = service.checkId(user.getId());
			if (sysUser != null) {
				sysUser.setSalt(RandomStringUtils.randomAlphanumeric(20));
				String entryPass = DigestUtils.md5Hex(DigestUtils.md5Hex(newpwd)+sysUser.getSalt());
				sysUser.setPassword(entryPass);
				service.updateIgnoreNull(sysUser);
			}

			return Results.parse(Constants.STATE_SUCCESS, "密码修改成功");
		} catch (Exception e) {
			log.error("edit manage user password error", e);
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/getSysUserBySQLServer")
    @POST
    public Object getSysUserBySQLServer(@Param("authKey") String authKey) {
	    if(authKey.equals("111111")) {
            Integer result = service.getSysUserBySQLServer();
            return Results.parse(Constants.STATE_SUCCESS, "共导入"+result+"条用户数据");
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

	/**
	 * 批量增加测试用户
	 * @return
	 */
	@At("/createTestUser")
	@POST
	public Object createTestUser() {
		return Results.parse(Constants.STATE_SUCCESS, service.createTestUser().toString());
	}

}