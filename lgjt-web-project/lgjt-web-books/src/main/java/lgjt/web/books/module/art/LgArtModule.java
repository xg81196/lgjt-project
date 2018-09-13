package lgjt.web.books.module.art;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.books.art.LgArt;
import lgjt.domain.books.art.LgFileInfo;
import lgjt.services.books.art.LgArtService;
import lgjt.services.books.art.LgFileInfoService;
import lombok.extern.log4j.Log4j;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;

import java.util.Date;


/**
 * 书香作品相关
 * @author xigexb
 */

@At("/art")
@IocBean
@Log4j
public class LgArtModule {

    
	@Inject
	 LgArtService service;

	@Inject
	 LgFileInfoService lgFileInfoService;



	/**
	 * 查询书香作品首页数据
	 * @param art
	 * @return
	 */
	@At("/getArtList")
	public Object getBookList(@Param("..")LgArt art){
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if(userLoginInfo==null){
			return Results.parse(Constants.STATE_UNLOGIN,"请登录");
		}
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageArtList(art,userLoginInfo.getInfos().get("userId")));
	}

	/**
	 * 个人中心我的作品
	 * @param art
	 * @return
	 */
	@At("/getArtListForMyCenter")
	public Object getBookListForMyCenter(@Param("..")LgArt art){
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if(userLoginInfo==null){
			return Results.parse(Constants.STATE_UNLOGIN,"请登录");
		}
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageArtListForMyCenter(art,userLoginInfo.getInfos().get("userId")));
	}


	/**
	 * 获取书香详情
	 * @param id 书香ID
	 * @return
	 */
	@At("/getBookInfo")
	public Object getBookInfo(@Param("id")String id){
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if(userLoginInfo==null){
			return Results.parse(Constants.STATE_UNLOGIN,"请登录");
		}
		return Results.parse(Constants.STATE_SUCCESS,null,service.checkId(id,userLoginInfo.getInfos().get("userId")));
	}

	/**
	 * 发布书香作品
	 * @param art
	 * @return
	 */
	@At("/artWorks/insert")
	public Object insetBooks(@Param("..")LgArt art){
		UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();
		if(userLoginInfo==null){
			return Results.parse(Constants.STATE_UNLOGIN,"请登录");
		}
		art.setUserId(userLoginInfo.getInfos().get("userId"));
		art.setRealName(userLoginInfo.getRealName());
		art.setCrtIp(ClientInfo.getIp());
		art.setCrtUser(userLoginInfo.getUserName());
		art.setCrtTime(new Date());
		art.setStatus(0);
		art.setIsDisable(0);
		art.setIsDelete(0);
		art.setOrgId(userLoginInfo.getInfos().get("orgId"));

		LgArt insert = service.insert(art);

		String t1 = "bmp,jpg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp";
		String[] types = t1.split(",");
		if("06".trim().equals(art.getCategoryId().trim()) || "07".trim().equals(art.getCategoryId().trim()) || "08".trim().equals(art.getCategoryId().trim())){
			//增加附件表
			JSONArray objects = JSONArray.parseArray(art.getExtend2());
			for (Object oj : objects){
				JSONObject jsonObject =  JSONObject.parseObject(oj.toString());
				System.out.println(jsonObject.toJSONString());
				LgFileInfo li = new LgFileInfo();
				li.setId(jsonObject.get("id").toString());
				li.setExtend1(art.getId());
				li.setSourceName(jsonObject.get("fileName").toString());
				Integer fileType = -1;  // 0 图片格式   1  视频格式
				try {
					for(int i = 0; i<types.length;i++){
						System.out.println(types[i].trim().toLowerCase());
						if(types[i].trim().toLowerCase().equals(jsonObject.get("type").toString().trim().toLowerCase())){
							fileType=0;
							break;
						}else {
							fileType=1;
						}
					}
				}catch (Exception e){
					e.printStackTrace();
					continue;
				}
				li.setFileType(fileType);
				li.setCrtTime(new Date());
				li.setCrtIp(ClientInfo.getIp());
				li.setCrtUser(userLoginInfo.getUserName());
				lgFileInfoService.insert(li);
			}
		}

		if(insert != null){
			return Results.parse(Constants.STATE_SUCCESS,"成功");
		}else {
			return Results.parse(Constants.STATE_FAIL,"失败");
		}
	}
}