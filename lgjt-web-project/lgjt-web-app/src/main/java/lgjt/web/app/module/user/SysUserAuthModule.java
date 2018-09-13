package lgjt.web.app.module.user;

import com.alibaba.fastjson.JSONObject;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.ILoginInfoCache;
import com.ttsx.util.cache.domain.UserLoginInfo;
import com.ttsx.util.cache.util.StringUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.ClientInfo;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.domain.app.systask.secretkey.SysSecretKey;
import lgjt.domain.app.user.SysUser;
import lgjt.domain.app.user.SysUserAuth;
import lgjt.services.app.org.SysOrganizationService;
import lgjt.services.app.secretkey.SysSecretKeyService;
import lgjt.services.app.user.SysUserAuthService;
import lgjt.services.app.user.SysUserService;
import lgjt.vo.app.user.SysUserVo;
import lgjt.web.app.config.AppConfig;
import lgjt.web.app.module.base.AppBaseModule;
import lgjt.web.app.utils.LoginUtil;
import lgjt.web.app.utils.SysUserAuthUtil;
import lgjt.web.app.utils.SysUserUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * show 用户认证接口类.
 * @author daijiaqi
 * @date 2018/5/723:50
 */
@At("/sysUserAuth")
@IocBean
@Log4j
public class SysUserAuthModule extends AppBaseModule {
    @Inject("sysUserService")
    SysUserService sysUserService;
    @Inject("sysUserAuthService")
    SysUserAuthService sysUserAuthService;

    @Inject("sysSecretKeyService")
    SysSecretKeyService sysSecretKeyService;


    @Inject("sysOrganizationService")
    SysOrganizationService sysOrganizationService;
    /**
     * show 用户认证 姓名、身份证号、企业名称、企业工号、所属工会、手机号.
     * @author daijiaqi
     * @date 2018年4月27日
     * @param sysUserAuth 用户认证信息表，对应lgjt.domain.app.user中的实体类SysUserAuth
     * @return 成功/失败信息
     */
    @At("/userApprove")
    @POST
    @GET
    public Object userApprove(@Param("..") SysUserAuth sysUserAuth) {
        try {

            //获取登录用户
        	UserLoginInfo userLoginInfo = LoginUtil.getUserLoginInfo();

            if(userLoginInfo==null){
                return ResultsImpl.parse(ReturnCode.CODE_103021.getCode(), ReturnCode.CODE_103021.getValue());
            }

            // 判断用户是否存在 ，判断用户是否已经认证
            lgjt.domain.app.user.SysUserVo sysUser = sysUserService.getByUserName(userLoginInfo.getUserName());
            if (sysUser == null) {
                return ResultsImpl.parse(ReturnCode.CODE_103000.getCode(), ReturnCode.CODE_103000.getValue());
            }
            if (sysUser.getUserType() == SysUser.USERTYPE_AUTHENTICATEDUSER) {
                return ResultsImpl.parse(ReturnCode.CODE_103006.getCode(), ReturnCode.CODE_103006.getValue());
            }
            // 判断用户是否已经提交认证
            int isExist = sysUserAuthService.isExist(sysUser.getId(), SysUserAuth.STATUS_AUDIT_WAIT);
            if (isExist > 0) {
                return ResultsImpl.parse(ReturnCode.CODE_103007.getCode(), ReturnCode.CODE_103007.getValue());
            }
            // 添加认证
            sysUserAuth.setCrtUser(sysUser.getUserName());
            sysUserAuth.setCrtTime(new Date());
            sysUserAuth.setCrtIp(ClientInfo.getIp());
            sysUserAuth.setStatus(SysUserAuth.STATUS_AUDIT_WAIT);
            sysUserAuth.setUserId(sysUser.getId());
            if(StringUtil.trim(sysUserAuth.getOrgId()).length()==0){
                sysUserAuth.setOrgId(sysUserAuth.getComId());
            }
            //删除 审核不通过的。
            SimpleCriteria cri = Cnd.cri();
            cri.where().andEquals("user_id",sysUser.getId());
            cri.where().andEquals("status",SysUserAuth.STATUS_AUDIT_NOTPASS);
            sysUserAuthService.delete(SysUserAuth.class,cri);

            if(sysUserAuthService.insert(sysUserAuth)==null){
                return ResultsImpl.parse(ReturnCode.CODE_103019.getCode(), ReturnCode.CODE_103019.getValue());
            }
        } catch (Exception e) {
            log.error("userApprove", e);
            return ResultsImpl.parse(ReturnCode.CODE_103005.getCode(), ReturnCode.CODE_103005.getValue());
        }
        return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
    }




    /**
     * show 获取认证信息.
     * @author daijiaqi
     * @date 2018年4月27日
     * @param sysUserAuth 用户认证信息表，对应lgjt.domain.app.user中的实体类SysUserAuth
     * @return 成功/失败信息+用户认证信息
     */
    @At("/getUserApproveInfo")
    @POST
    @GET
    public Object getUserApproveInfo(@Param("..") SysUserAuth sysUserAuth) {
        try {

            String authUserId = StringUtils.trim(sysUserAuth.getUserId());
            if(authUserId.length()==0){//如果没有关联ID 就返回失败
                return ResultsImpl.parse(ReturnCode.CODE_103031.getCode(), ReturnCode.CODE_103031.getValue());
            }

                //获取用户对象
            SimpleCriteria cri= Cnd.cri();
            cri.where().andEquals("user_id",authUserId);
            SysUserAuth  sysUser= sysUserAuthService.fetch(SysUserAuth.class,cri);

                if(sysUser==null){
                    return ResultsImpl.parse(ReturnCode.CODE_103031.getCode(), ReturnCode.CODE_103031.getValue());
                }
                //赋值用户 相关属性
                sysUser.setPassword(null);
                sysUser.setSalt(null);
                SysUserVo sysUserVo = new SysUserVo();
                BeanUtils.copyProperties(sysUserVo,sysUser);
                SysUserUtil.setUserPropertys(sysUserVo,"1",sysOrganizationService);

            return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(),sysUserVo);
        } catch (Exception e) {
            String errMsg = "";
            if(sysUserAuth!=null){
                errMsg = JSONObject.toJSONString(sysUserAuth);
            }
            log.error("getUserApproveInfo(" + errMsg + ")", e);

        }
        return ResultsImpl.parse(ReturnCode.CODE_101001.getCode(), ReturnCode.CODE_101001.getValue());

    }
}
