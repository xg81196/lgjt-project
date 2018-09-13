package lgjt.domain.backend.user;

import com.google.common.collect.Maps;
import com.ttsx.platform.nutz.pojo.CaseEntity;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.JsonField;
import org.nutz.json.JsonIgnore;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.utils.SignUtils;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Map;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description: 管理员帐号
 */

@Data
@Table("sys_user_admin")
public class SysUserAdmin extends CaseEntity {

      @JsonField(ignore=true)
      public static final String ANTI_KEY = "anti:sign";

      /** 超级管理员ID */
      @JsonField(ignore=true)
      public static final String SUPER_ADMIN = "1";

      /** 数据权限过滤 */
      @JsonField(ignore=true)
      public static final String SQL_FILTER = "sql_filter";

      @JsonField(ignore=true)
      public static final String ADMIN="admin";

      @JsonField(ignore=true)
      public static final String PLATFORM_ADMIN="平台管理员";

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
      private Integer status ;
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

      @JsonField(ignore=true)
      @Column("extend3")
      private String extend3;

      @Column("extend4")
      private String extend4;

      @Column("extend5")
      private String extend5;

      @Readonly
      private String roleName;

      @Readonly
      private String roleId;



      public Map<String, String> getAddAntiTamperString(SysUserAdmin obj) {
            //防篡改 密码+用户名
            Map<String, String> needSignParams = Maps.newHashMap();
            needSignParams.put("userName",obj.getUserName());
            needSignParams.put("realName",obj.getRealName());
            needSignParams.put("headPortrait",obj.getHeadPortrait());
            needSignParams.put("password",obj.getPassword());
            needSignParams.put("status",obj.getStatus()+"");
            needSignParams.put("sex",obj.getSex()+"");
            needSignParams.put("phoneNumber",obj.getPhoneNumber());
            needSignParams.put("email",obj.getEmail());
            needSignParams.put("salt",obj.getSalt());
            needSignParams.put("idNo",obj.getIdNo());
            needSignParams.put("crtUser",obj.getCrtUser());
            needSignParams.put("crtIp",obj.getCrtIp());
            needSignParams.put("crtTime",obj.getCrtTime()+"");
            needSignParams.put("remark",obj.getRemark());
            return needSignParams;
      }


      public Map<String, String> getUpdAntiString(SysUserAdmin obj) {
            //防篡改 密码+用户名
            Map<String, String> needSignParams = Maps.newHashMap();
            needSignParams.put("userName",obj.getUserName());
            needSignParams.put("realName",obj.getRealName());
            needSignParams.put("headPortrait",obj.getHeadPortrait());
            needSignParams.put("password",obj.getPassword());
            needSignParams.put("status",obj.getStatus()+"");
            needSignParams.put("sex",obj.getSex()+"");
            needSignParams.put("phoneNumber",obj.getPhoneNumber());
            needSignParams.put("email",obj.getEmail());
            needSignParams.put("salt",obj.getSalt());
            needSignParams.put("idNo",obj.getIdNo());
            needSignParams.put("crtUser",obj.getCrtUser());
            needSignParams.put("crtIp",obj.getCrtIp());
            needSignParams.put("crtTime",obj.getCrtTime()+"");
            needSignParams.put("updUser",obj.getCrtUser());
            needSignParams.put("updIp",obj.getCrtIp());
            needSignParams.put("updTime",obj.getCrtTime()+"");
            needSignParams.put("remark",obj.getRemark());
            return needSignParams;
      }



      public  boolean isAdmin(String userName){
            return userName != null && ADMIN.equals(userName);
      }

      public void setValueForKsGroupAdmin(KsGroupAdmin value) {
            this.setId(value.getId());
            this.userName = value.getUserName();
            this.realName = value.getRealName();
            this.password = value.getPassword();
            this.salt = value.getSalt();
            this.orgId = value.getOrgId();
            this.extend5 = "3";
            this.setCrtIp("127.0.0.1");
            this.setCrtTime(new Date());
            this.setCrtUser("admin");
      }

}
