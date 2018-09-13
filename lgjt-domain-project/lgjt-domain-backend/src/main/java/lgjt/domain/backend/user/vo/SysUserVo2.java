package lgjt.domain.backend.user.vo;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonIgnore;

/**
 * @author wuguangwei
 * @date 2018/4/19
 * @Description:
 */

@Data
@Table("sys_user")
public class SysUserVo2 extends CaseEntity {


    /**
     启用
     */
    public static final Integer STATUS_ENABLED   = 0 ;
    /**
     禁用
     */
    public static final Integer STATUS_DISABLE   = 1 ;
    /**
     一般用户
     */
    public static final Integer USERTYPE_ORDINARYUSER   = 0 ;
    /**
     认证用户
     */
    public static final Integer USERTYPE_AUTHENTICATEDUSER   = 1 ;

    /**
     * 用户名。
     */
    @Column("user_name")
    private String userName;
    /**
     * 姓名。
     */
    @Column("real_name")
    private String realName;

    /**
     * 密码。Md5加密
     */
    @Column("password")
    private String password;


    @Column("extend1")
    private String extend1;

    @Column("extend2")
    private String extend2;

    @Column("extend3")
    private String extend3;
    /**
     * 状态。0：启用；1：禁用
     取值范围：#[enabled:0:启用$disable:1:禁用]#
     */
}
