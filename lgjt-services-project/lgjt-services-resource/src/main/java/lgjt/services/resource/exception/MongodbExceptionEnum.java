package lgjt.services.resource.exception;


/**
 * @Author： dai.jiaqi
 * @Description:
 * @Date:Created in 16:04 2018/1/3/003
 * @Modified by:
 */
public enum MongodbExceptionEnum {

    /**
     * 配置文件相关异常
     */
    config_00001("00001","mongodb-root.properties 配置文件不存在"),
    config_00002("00002","主配置文件mongodb-server-includes"),
    config_00003("00003","主配置文件中mongodb-server-includes配置值存在不是以.properties结尾的文件"),
    config_00004("00004","子配置文件中必要配置项缺失!"),
    config_00005("00005","子配置文件中mongodb-index违反唯一性原则!"),

    config_01000("01000","查询条件错误查询字段不能为空");


    MongodbExceptionEnum(String code, String msg) {  }
}
