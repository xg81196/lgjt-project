package lgjt.services.resource.exception;


import lgjt.services.resource.util.StringTools;

/**
 * 自定义异常类
 * @Author： dai.jiaqi
 * @Description:
 * @Date:Created in 13:45 2017/12/29/029
 * @Modified by:
 */
public class MongodbException extends  Exception {
    /**
     * 异常编号
     */
    private String code;
    /**
     * 异常信息
     */
    private String msg;

    /**
     * 构造方法
     */
    private MongodbException(){}

    /**
     * 构造方法
     * @param mongodbExceptionEnum 自定义异常枚举
     */
    public MongodbException( MongodbExceptionEnum mongodbExceptionEnum){
        if(mongodbExceptionEnum==null){
            this.code=mongodbExceptionEnum.name();
            this.msg=mongodbExceptionEnum.valueOf(this.code).toString();
        }
    }

    /**
     * 构造方法
     * @param mongodbExceptionEnum 自定义异常枚举
     */
    public MongodbException( MongodbExceptionEnum mongodbExceptionEnum,String appendMsg){
        if(mongodbExceptionEnum==null) {
            this.code = mongodbExceptionEnum.name();
            this.msg = mongodbExceptionEnum.valueOf(this.code).toString() ;
        }
        if(appendMsg!=null){
            this.msg=this.msg+"::"+ StringTools.trim(appendMsg);
        }
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
    public String getCode() {
        return this.code;
    }
}
