package lgjt.web.app.module.upload;

import com.alibaba.fastjson.JSONObject;
import com.ttsx.util.cache.domain.UserLoginInfo;
import com.ttsx.util.cache.util.StringUtil;
import lgjt.domain.app.user.SysUserVo;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.HttpClients;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.domain.app.user.SysUser;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.services.app.user.SysUserService;
import lgjt.services.mongodb.company.MongodbService;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;
import lgjt.web.app.utils.HttpUtil;
import lgjt.web.app.utils.LoginUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
   * show 文件上传接口类.
   * @author daijiaqi
   * @date 2018/5/6 23:57
   */
@At("/upload")
@IocBean
@Log4j
public class UploadModule extends AppBaseModule {

	@Inject
	private SysSecretKeyService sysSecretKeyService;

	@Inject("sysUserService")
	SysUserService sysUserService;
	/**
	 * show 上传头像.
	 * @author daijiaqi
	 * @date 2018年4月26日
	 * @param file 文件内容
	 * @return 上传成功/失败信息+文件ID
	 */

	@POST
	@GET
	@At("/headPortrait")
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object headPortrait(@Param("file") TempFile file) {
		try {
			UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
			if (userLoginInfo == null) {
				return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
			}
			SysUserVo sysUser = sysUserService.getByUserName(userLoginInfo.getUserName());
			if (sysUser == null) {
				return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
			}
			if(sysUser.getStatus()== SysUser.STATUS_DISABLE){
				return ResultsImpl.parse(ReturnCode.CODE_103017.getCode(), ReturnCode.CODE_103017.getValue());
			}
			// 判断共有参数
			// 文件上传MONGODB
			String fId = MongodbService.getInstance("file").upload(file.getInputStream());
			if(fId!=null && fId.length()>0){
				SysUser su=new SysUser();
				su.setId(sysUser.getId());
				su.setHeadPortrait(fId);
				int updateCount = sysUserService.updateIgnoreNull(su);
				if(updateCount==0){
					return ResultsImpl.parse(ReturnCode.CODE_103026.getCode(), ReturnCode.CODE_103026.getValue());
				}

			/*	try {
					String url = AppConfig.USER_URL+"/admin/user/updateHeadImg";
					HashMap<String,String> params =new HashMap<>();
					params.put("id",sysUser.getId());
					params.put("userName",sysUser.getUserName());
					params.put("userHeadImg",su.getHeadPortrait());
					HashMap<String,String> headers=new HashMap<>();
					JSONObject obj = HttpUtil.post( HttpClients.createDefault(), url, params, headers);
					log.info("postObj="+(obj==null?null:obj.toJSONString()));
				} catch (Exception e) {
					log.error("http:("+su.getId()+","+su.getRealName()+"",e);
				}*/

			}
			//修改用戶表通向字段
			Map<String, String> result = new HashMap<String, String>();
			result.put("id", fId);
			return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
		} catch (IOException e) {
			log.error("UploadModule.uploadFile()", e);
		}
		return ResultsImpl.parse(ReturnCode.CODE_103026.getCode(), ReturnCode.CODE_103026.getValue());
	}
	

}