package lgjt.common.base.service;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.common.base.utils.SensitivewordFilterUtil;
import lgjt.common.base.vo.SensitiveWord;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 敏感词过滤2
 * @author  majinyong
 * @date  2017-12-15
 */
@Log4j
@IocBean
public class SensitiveSearchService extends BaseService {
    private static SensitivewordFilterUtil sensitivewordFilterUtil = SensitivewordFilterUtil.getSensitivewordFilterUtil();

    /**
     * 初始化查询敏感词表
     * @author majinyong
     */
    public List<String> initSensitivewordsByTxt(){
        SimpleCriteria cri=Cnd.cri();
        List<String> list = new ArrayList<>();
        List<SensitiveWord> query = super.query(SensitiveWord.class, cri);
        for (SensitiveWord bbsSensitive : query) {
            list.add(bbsSensitive.getSensitivewords());
        }
        return list;
    }



    /**
     * 文本敏感词过滤
     * @param txt
     * @author majinyong
     */
    public Set<String> findSensitivewordsByTxt(String txt){
        return sensitivewordFilterUtil.getSensitiveWord(txt,1);
    }


    /**
     * 替换敏感词
     * @param txt
     * @param replaceText
     * @return
     */
    public String replaceSensitiveWord(String txt, String replaceText) {
        return sensitivewordFilterUtil.replaceSensitiveWord(txt, 2 , replaceText);
    }
}