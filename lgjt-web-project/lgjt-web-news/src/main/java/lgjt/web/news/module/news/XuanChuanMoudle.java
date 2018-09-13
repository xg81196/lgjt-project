package lgjt.web.news.module.news;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.domain.news.XuanChuan;
import lgjt.services.news.KsClassService;
import lgjt.services.news.XuanChuanServcice;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.Param;



@At("/news")
@IocBean
@Log4j
public class XuanChuanMoudle {

    /**
     * 新闻表
     */
    @Inject("xuanChuanServcice")
    XuanChuanServcice xuanChuanServcice;

    @Inject("ksClassService")
    KsClassService ksClassService;

//    @Inject
//    NoticeServer server;

    /**
     * 获取新闻列表
     */
    @At("/getnewslist")
    public Object getNewsList(@Param("..") XuanChuan xuanChuan) {
        return Results.parse(Constants.STATE_SUCCESS, null, xuanChuanServcice.queryList(xuanChuan));
    }



    /**
     * 获取新闻详情
     *
     * @param infoID
     * @return
     */
    @At("/getnewsxiangqing")
    @GET
    public Object getNews(@Param("infoID") Integer infoID) {
        return Results.parse(Constants.STATE_SUCCESS, null, xuanChuanServcice.get(infoID));
    }



    /**
     * 首页轮播图
     * ok
     * @return
     */
    @At("/getHomePageChart")
    @GET
    public Object getHomePageChart() {
        return Results.parse(Constants.STATE_SUCCESS, null, xuanChuanServcice.homePageChart());
    }

    /**
     *
     * 查询首页小窗口新闻类型列表和新闻详情页面头部选择新闻类型
     * ok
     * @return
     */
    @At("/queryHomePageWindoNewsType")
    @GET
    public Object queryHomePageWindoNewsType() {
        return Results.parse(Constants.STATE_SUCCESS, null, ksClassService.queryHomePageWindoNewsType());
    }

    /**
     * 按照新闻类型查询新闻 首页小窗口和新闻列表页面新闻信息
     * @param newsType
     * @return
     */
    @At("/queryListForNewsType")
    @GET
    public Object queryListForNewsType(@Param("..")XuanChuan xuanChuan,@Param("newsType") Integer newsType, @Param("pageSize") Integer size,@Param("page") Integer page) {
        return Results.parse(Constants.STATE_SUCCESS, null, xuanChuanServcice.queryListForNewsTypeFor4(xuanChuan,newsType, size,page));
    }


    @At("/test")
    @GET
    public Object ttt() {
        return Results.parse(Constants.STATE_SUCCESS, "测试成功");
    }


    @At("/queryFuJian")
    @GET
    public Object queryFuJian(@Param("id")String ids){
        return Results.parse(Constants.STATE_SUCCESS, null,xuanChuanServcice.queryUploadFile(ids));
    }


}
