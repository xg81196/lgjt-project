package lgjt.domain.backend.user.vo;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;
import lgjt.common.base.constants.UserDataType;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:用户数据权限
 */
@Data
public class SysUserDataVo extends CaseEntity {


      private String name ;


    /**
     * 是否向下兼容 0向下兼容 1不向下兼容
     */
    private int compatibility;

       //数据权限
      @Readonly
      private String orgIdList ;

      @Readonly
      private  String orgId;

}
