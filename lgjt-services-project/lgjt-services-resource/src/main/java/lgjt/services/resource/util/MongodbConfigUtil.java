package lgjt.services.resource.util;

import lgjt.services.resource.exception.MongodbException;
import lgjt.services.resource.exception.MongodbExceptionEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： dai.jiaqi
 * @Description:
 * @Date:Created in 16:25 2018/1/3/003
 * @Modified by:
 */
public class MongodbConfigUtil {


    /**
     * 检查并格式化主配置文件中的mongodb-server-includes 配置
     * @param mongodbServerIncludesValue
     * @return
     * @throws MongodbException
     */
    public static String [] formatMongodbServerIncludes(String mongodbServerIncludesValue)throws MongodbException {
        String[] configFilesArr = StringTools.trim(mongodbServerIncludesValue).split(";");
        if(configFilesArr==null || configFilesArr.length==0){
            return configFilesArr;
        }
        List<String> tmp =new ArrayList<String>();
        String configFile="";
        for (int i = 0; i < configFilesArr.length; i++) {
            configFile = StringTools.trim(configFilesArr[i]);
            if(configFile.length()>0){
                if(!configFile.toLowerCase().endsWith(".properties")){
                    throw new MongodbException(MongodbExceptionEnum.config_00003);
                }
                tmp.add(configFilesArr[i]);
            }
        }
        return tmp.toArray(new String[tmp.size()]);
    }
}
