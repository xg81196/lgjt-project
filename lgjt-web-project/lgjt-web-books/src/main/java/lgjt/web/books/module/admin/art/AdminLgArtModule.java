package lgjt.web.books.module.admin.art;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.books.art.LgArt;
import lgjt.services.books.art.LgArtService;
import lgjt.services.books.art.LgFileInfoService;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import java.lang.reflect.Type;

/**
 * 书香作品相关
 * @author xigexb @date 2018/9/12
 *
 */

@At("/admin/books")
@IocBean
@Log4j
public class AdminLgArtModule {


	@Inject("lgArtService")
	LgArtService service;

	@Inject
	LgFileInfoService lgFileInfoService;


	/**
	 * 管理书香作品
	 *
	 * @param art
	 * @return
	 */
	@At("/getArtList")
	public Object getBookList(@Param("..")LgArt art, @Param("type")String Type) {
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		System.out.println(art.toString());
		if (userLoginInfo == null) {
			return Results.parse(Constants.STATE_UNLOGIN, "请登录");
		}
		return Results.parse(Constants.STATE_SUCCESS, null, service.queryPageAdminArtList(art,Type));
	}


	/**
	 * 获取书香详情
	 *
	 * @param id 书香ID
	 * @return
	 */
	@At("/booksWorks/get")
	public Object getBookInfo(@Param("id") String id) {
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if (userLoginInfo == null) {
			return Results.parse(Constants.STATE_UNLOGIN, "请登录");
		}
		return Results.parse(Constants.STATE_UNLOGIN, null, service.checkId(id, userLoginInfo.getInfos().get("userId")));
	}


	/**
	 * 禁用书香作品
	 * @param art
	 * @return
	 */
	@At("/display")
	public Object disableBook(@Param("..")LgArt art) {
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if (userLoginInfo == null) {
			return Results.parse(Constants.STATE_UNLOGIN, "请登录");
		}
		Integer i = service.adminDisplay(art);
		if(i!=-1){
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL, "失败");
		}
	}

//	/**
//	 *  审核作品
//	 * @param id
//	 * @param checkMeg
//	 * @param status
//	 * @return
//	 */
//	@At("/check")
//	public Object  checkArt(@Param("id") String id,@Param("checkMsg")String checkMeg,@Param("status")String status){
//		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
//		if (userLoginInfo == null) {
//			return Results.parse(Constants.STATE_UNLOGIN, "请登录");
//		}
//		return Results.parse(Constants.STATE_SUCCESS, null,service.adminCheck(id,checkMeg,status));
//	}


	/**
	 * 审核作品
	 * @param art
	 * @return
	 */
	@At("/check")
	public Object  checkArt(@Param("..")LgArt art){
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if (userLoginInfo == null) {
			return Results.parse(Constants.STATE_UNLOGIN, "请登录");
		}
		art.setCheckUser(userLoginInfo.getUserName());
		return Results.parse(Constants.STATE_SUCCESS, null,service.adminCheck(art));
	}


}