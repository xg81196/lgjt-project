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
public class SysUserAdminVo extends CaseEntity {

      /** 超级管理员ID */
      public static final String SUPER_ADMIN = "1";

      /** 数据权限过滤 */
      public static final String SQL_FILTER = "sql_filter";

      @Column("user_name")
      private String userName ;
      //真实姓名
      @Column("real_name")
      private String realName ;
      //头像
      @Column("head_portrait")
      private String headPortrait;
      //密码。Md5加密
      @JsonField(ignore=true)
      @Column("password")
      private String password ;
      //状态。0：启用；1：禁用
      @Column("status")
      private int status ;
      //性别。性别： 1男 ，0女
      @Column("sex")
      private Integer sex ;
      @Column("phone_number")
      private String phoneNumber ;
      @Column("email")
      private String  email ;
      //组织ID
      @Column("org_id")
      private String orgId ;

      @Column("org_name")
      private String orgName ;

      //身份证号
      @JsonField(ignore=true)
      @Column("id_no")
      private String idNo ;
      //防篡改
      @JsonField(ignore=true)
      @Column("anti_tamper")
      private String antiTamper ;
      //盐值
      @JsonField(ignore=true)
      @Column("salt")
      private String salt ;
      @Column("remark")
      private String remark ;

      private String confpwd;

      private String newpwd;

      private String oldpwd;

      @Column("extend1")
      private String extend1;

      @Column("extend2")
      private String extend2;

      @Column("role_id")
      private String roleId;

      @Column("role_name")
      private String roleName;

      @Column("union_name")
      private String unionName;


}
