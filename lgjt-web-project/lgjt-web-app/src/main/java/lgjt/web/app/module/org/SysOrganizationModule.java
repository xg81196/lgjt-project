package lgjt.web.app.module.org;


import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.app.user.SysUser;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.domain.app.org.SysOrganization;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.web.app.module.base.AppBaseModule;
import lgjt.web.app.utils.PinyinUtil;


import java.util.*;
/**
 * show 组织结构.
 * @author daijiaqi
 * @date 2018年4月25日
 */
@At("/organization")
@IocBean
@Log4j
public class SysOrganizationModule extends AppBaseModule {


    @Inject("sysOrganizationService")
    SysOrganizationService sysOrganizationService;

    /**
     * show 获取组织结构.
     * @author daijiaqi
     * @date 2018年5月10日
     * @param superId 组织ID
     * @return 组织结构信息Object
     */

    @At("/queryOrg")
    @POST
    @GET
    public Object queryOrg(@Param("orgId") String superId) {

        try {

            if (superId == null || superId.trim().length() == 0) {
                superId = "-1";
            }
            //获取下面1级节点
            List<SysOrganization> sysOrganizations = sysOrganizationService.queryBySuperId(superId);
            //排序dao
            Collections.sort(sysOrganizations, new Comparator<SysOrganization>() {
                @Override
                public int compare(SysOrganization o1, SysOrganization o2) {
                    String pingyin1 = StringUtil.trim(PinyinUtil.toHanyuPinyin(o1.getName())).toUpperCase();
                    String pingyin2 = StringUtil.trim(PinyinUtil.toHanyuPinyin(o2.getName())).toUpperCase();
                    o1.setWord(pingyin1.length() > 0 ? pingyin1.substring(0, 1) : "");
                    o2.setWord(pingyin2.length() > 0 ? pingyin2.substring(0, 1) : "");
                    return pingyin1.compareTo(pingyin2);
                }
            });
            if(sysOrganizations.size()==1){
                String pinyin = StringUtil.trim(PinyinUtil.toHanyuPinyin(sysOrganizations.get(0).getName())).toUpperCase();
                sysOrganizations.get(0).setWord(pinyin.length() > 0 ? pinyin.substring(0, 1) : "");
            }
            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), sysOrganizations);
        } catch (Exception e) {
            log.error("SysUserModule:insert(orgId=" + superId + ")", e);

        }
        return ResultsImpl.parse(ReturnCode.CODE_103023.getCode(), ReturnCode.CODE_103023.getValue());
    }

    /**
     * 根据用户获取所属机构
     * @param obj
     * @return
     */
    @At("/getOrgByUserId")
    public Object getOrgByUserId(@Param("..") SysOrganization obj) {
        if(StringUtils.isEmpty(obj.getId())) {
            //获取当前登录用户的
            obj.setId(LoginUtil.getUserLoginInfo().getInfos().get("orgId"));
        }
        return Results.parse(Constants.STATE_SUCCESS,null,sysOrganizationService.getById(obj.getId()));
    }
}