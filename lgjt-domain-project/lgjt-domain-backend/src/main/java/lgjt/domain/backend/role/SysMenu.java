package lgjt.domain.backend.role;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import lombok.Data;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Readonly;
import org.nutz.dao.entity.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:系统菜单
 */

@Data
@Table("sys_menu")
public class SysMenu extends CaseEntity {


    @Column("parent_id")
    //@Default("-1")
    private String parentId;

    /**
     * 资源名称
     */
    @Column("menu_name")
    private String name;

    //资源代码
    @Column("menu_code")
    private String resCode;

    @Column("menu_headline")
    private Integer isHeadline;


    @Column("menu_img")
    private String img;

    @Column("menu_view")
    private String view;

    @Column("menu_options")
    private String option;

    @Column("menu_action")
    private String action;

    //URL
    @Column("menu_url")
    private String url;

    @Column("menu_sort")
    private String sort;

    //菜单类型。menu,button
    @Column("menu_type")
    private String menuType;

    //系统类型。0：后端
    @Column("sys_type")
    private String sysType;

    @Readonly
    private List<SysMenu> list = new ArrayList<SysMenu>();

}
