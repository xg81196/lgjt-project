package lgjt.services.news;


import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.news.KsClass;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@IocBean(fields ={"dao:daoNews"})
public class KsClassService extends BaseService implements Serializable {

    /**
     * 查询首页小窗口新闻类型和新闻列表头部类型选择
     */
    public List<KsClass> queryHomePageWindoNewsType(){
        Map<Integer,String> ksMap = new HashMap<>();
        SimpleCriteria cri = Cnd.cri();
        List<KsClass>  queryList = super.query("lgjt.news.queryHomePageWindoNewsType",KsClass.class, cri);
        return queryList;
    }





}
