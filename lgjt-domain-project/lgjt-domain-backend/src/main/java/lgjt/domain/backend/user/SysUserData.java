package lgjt.domain.backend.user;

import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import lgjt.common.base.constants.UserDataType;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:用户数据权限
 */
@Data
@Table("sys_user_data")
public class SysUserData extends CaseEntity {

      @Column("admin_user_id")
      private String adminUserId ;
      @Column("object_id")
      private String objectId ;
      //对象类型。0:组织；1:题库；
       @Column("object_type")
      private int objectType = UserDataType.UNION_TYPE ;

    /**
     * 是否向下兼容 0向下兼容 1不向下兼容
     */
    @Column("compatibility")
    private int compatibility;

       //数据权限
      @Readonly
      private String orgIdList ;

      @Readonly
      private  String orgId;

}
