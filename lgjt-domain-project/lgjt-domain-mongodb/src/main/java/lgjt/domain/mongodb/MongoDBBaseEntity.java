package lgjt.domain.mongodb;

import com.mongodb.ReflectionDBObject;

import java.util.Date;


public class MongoDBBaseEntity  {
    /**
     * 基础字段：主键
     */
    private String _id;

    /**
     * 业务字段：创建用户
     */
    private String crtUser;
    /**
     * 业务字段：创建时间
     */
    private Date crtTime;
    /**
     * 业务字段：创建IP
     */
    private String crtIp;
    /**
     * 业务字段：更新用户
     */
    private String updUser;
    /**
     * 业务字段：更新时间
     */
    private Date updTime;
    /**
     * 业务字段：更新IP
     */
    private String updIp;

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_Id() {
        return _id;
    }
    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtIp() {
        return crtIp;
    }

    public void setCrtIp(String crtIp) {
        this.crtIp = crtIp;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getUpdIp() {
        return updIp;
    }

    public void setUpdIp(String updIp) {
        this.updIp = updIp;
    }
}
