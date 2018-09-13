package lgjt.domain.backend.user.vo;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 管理员帐号
 */

@Data
@Table("sys_user_admin")
public class SysUserAdminVo2 extends CaseEntity {

      /** 超级管理员ID */
      public static final String SUPER_ADMIN = "1";

      /** 数据权限过滤 */
      public static final String SQL_FILTER = "sql_filter";

      @Column("user_name")
      private String userName ;
      //真实姓名
      @Column("real_name")
      private String realName ;
      //密码。Md5加密
      @Column("password")
      private String password ;
      //状态。0：启用；1：禁用
      @Column("status")
      private int status ;

      private String confpwd;

      private String newpwd;

      private String oldpwd;

      @Column("extend2")
      private String extend2;
      @Column("extend3")
      private String extend3;


}
