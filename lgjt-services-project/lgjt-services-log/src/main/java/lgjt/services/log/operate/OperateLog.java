package lgjt.services.log.operate;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import lombok.Data;

import java.util.Date;

/**
 * 日志实体类
 * @author daijiaqi
 * @date 2018年5月1日
 */
@Data
@Entity(value = "OperateLog", noClassnameStored = true)
public class OperateLog {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 操作人。
     */
    @Indexed
    @Property("user_name")
    private String userName;
    /**
     * 操作模块。操作的类名
     */
    @Indexed
    @Property("log_module")
    private String logModule;
    /**
     * 操作类型。操作的方法名
     */
    @Indexed
    @Property("opt_type")
    private String optType;
    /**
     * 参数
     */
    @Property("parameters")
    private String parameters;

    /**
     * 操作结果。succ：成功；fail：失败
     */
    @Property("result_info")
    private String resultInfo;


    /**
     * 返回結果代码
     */
    @Property("return_code")
    private String returnCode;

    /**
     * 返回結果
     */
    @Property("return_info")
    private String returnInfo;

    /**
     * 备注。
     */
    @Property("remarks")
    private String remarks;
    /**
     * 开始时间。
     */
    @Property("start_time")
    private java.util.Date startTime;
    /**
     * 结束时间。
     */
    @Property("end_time")
    private java.util.Date endTime;
    /**
     * 耗时。单位毫秒
     */
    @Property("cost_time")
    private Integer costTime;
    /**
     * 操作IP。示例：192.168.100.1
     */
    @Property("log_ip")
    private String logIp;
    /**
     * 所在节点。
     */
    @Property("log_node")
    private String logNode;

    /**
     * 开始日期
     */
    @Property("start_date")
    private String startDate;

    /**
     * 结束日期
     */
    @Property("end_date")
    private String endDate;

    /**
     * 入库时间
     */
    @Property("load_time")
    private Date loadTime;

    /**
     * 文件内记录唯一标识
     */
    @Property("uuid")
    private String uuid;



}