package lgjt.web.app.module.dict;

import com.ttsx.util.cache.IObjectCache;
import com.ttsx.util.cache.ObjectCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lombok.extern.log4j.Log4j;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.RedisKeys;
import lgjt.domain.app.dict.SysDict;
import lgjt.services.app.dict.SysDictService;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;

import java.util.*;

/**
 * show 词典接口.
 * @author daijiaqi
 * @date 2018/5/723:55
 */
@At("/app/dict")
@IocBean
@Log4j
public class DictModule extends AppBaseModule {
    @Inject("sysSecretKeyService")
    SysSecretKeyService sysSecretKeyService;

    @Inject("sysDictService")
    SysDictService sysDictService;
    /**
     * show 获取词典内容.
     * @author daijiaqi
     * @date 2018年4月25日
     * @param dictKey 词典名称
     * @return 获取成功/失败信息+词典名称和值的对象信息
     */
    
    @At("/getDicts")
    @POST
    @GET
    public Object getDicts(@Param("dictKey") String dictKey) {
        try {
            IObjectCache objectCache = ObjectCache.getInstance();
            String key = RedisKeys.getSysDictKey(dictKey);
            List<Map<String,String>> result = new ArrayList<Map<String,String>>();
            Map<String,String> dictMap =( Map<String,String> )objectCache.get(key);
            if(dictMap!=null){
                Iterator<String> keys=  dictMap.keySet().iterator();
                String keyTmp ="";
                while (keys.hasNext()){
                    keyTmp=keys.next();
                    Map<String,String> map =new HashMap<String,String>();
                    map.put("name",keyTmp);
                    map.put("value",dictMap.get(keyTmp));
                    result.add(map);
                }
            }

            if(result.size()==0){
                List<SysDict>  distsDB=sysDictService.queryDictsByParentId(dictKey);
                for(int i=0;i<distsDB.size();i++){
                    Map<String,String> map =new HashMap<String,String>();
                    map.put("name",distsDB.get(i).getCode());
                    map.put("value",distsDB.get(i).getName());
                    result.add(map);
                }
            }
           return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(),result);
        }catch(Exception e){
            log.error("dictKey="+dictKey,e);
        }
        return  ResultsImpl.parse(ReturnCode.CODE_103020.getCode(), ReturnCode.CODE_103020.getValue());
    }
    
    
}
