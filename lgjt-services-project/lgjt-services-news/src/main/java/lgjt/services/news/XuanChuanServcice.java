package lgjt.services.news;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.news.UploadFiles;
import lgjt.domain.news.XuanChuan;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Log4j
@IocBean(fields = {"dao:daoNews"})
public class XuanChuanServcice extends BaseService implements Serializable {


    /**
     * 查询新闻列表
     *
     * @param obj
     * @return
     */
    public List<XuanChuan> queryList(XuanChuan obj) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andNotEquals(" DelTF", 1);
        return super.query(XuanChuan.class, cri);
    }


    /**
     * 查询新闻详情
     *
     * @param id
     * @return
     */
    public XuanChuan get(Integer id) {
        return super.fetch(XuanChuan.class, id);
    }

    /**
     * 获取首页轮播图地址
     */
    public List<XuanChuan> homePageChart() {
        SimpleCriteria cri = Cnd.cri();
        return super.query("lgjt.news.homePageChart", XuanChuan.class, cri);
    }

    /**
     * ok
     * 按照新闻类型查询新闻 首页小窗口
     * @param
     * @return
     */
    public PageResult<XuanChuan> queryListForNewsTypeFor4(XuanChuan xuanChuan, Integer newsType, Integer size, Integer page) {
        SimpleCriteria cri = Cnd.cri();
        xuanChuan.setPageSize(size);
        xuanChuan.setPage(page);
        cri.where().andEquals("ClassID", newsType);
        cri.desc("AddDate");
        return super.queryPage(XuanChuan.class, xuanChuan, cri);
    }


    /**
     * 查询附件
     *
     * @param Ids 附件ID
     * @return
     */
    public List<UploadFiles> queryUploadFile(String Ids) {
        SimpleCriteria cri = Cnd.cri();
        String[] ids = Ids.split(",");

        List<UploadFiles> files = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {
            cri.where().andEquals("ID", Integer.valueOf(ids[i]));
            files.add(super.fetch(UploadFiles.class, cri));
        }
        return files;
    }

}

