package lgjt.services.backend.org;

import com.ttsx.platform.nutz.service.BaseService;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;

import java.util.List;

/**
 * @author wuguangwei
 * @date 2018/6/13
 * @Description:
 */
@IocBean
public class SysUnionComService extends BaseService {

    public List<SysUnionCom> getUnionComByUnionId(String unionId) {

        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("union_id",unionId);
        return this.query(SysUnionCom.class,cri);
    }

    /**
     * 递归查询工会
     * @param loginOrgId
     * @param superId
     * @return
     */
    public boolean recursionUnion(String loginOrgId, String superId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id", superId);
        List<SysUnion> sus = super.query(SysUnion.class, cri);
        SysUnion sysUnion = sus.get(0);

        if(sysUnion.getSuperId().equals(loginOrgId)) {
            return true;
        } else {
            if(sysUnion.getSuperId().equals("-1")) {
                return false;
            } else {
                return this.recursionUnion(loginOrgId, sysUnion.getId());
            }
        }
    }


    /**
     * 递归查询企业
     * @param loginOrgId
     * @param superId
     * @return
     */
    public boolean recursionOrg(String loginOrgId, String superId) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id", superId);
        List<SysOrganization> syos = super.query(SysOrganization.class, cri);

        if(syos.size() > 0) {
            SysOrganization syo = syos.get(0);
            if(syo.getSuperId().equals(loginOrgId)) {
                return true;
            } else {
                if(syo.getSuperId().equals("-1")) {
                    cri = Cnd.cri();
                    cri.where().andEquals("com_id", syo.getId());
                    List<SysUnionCom> sucs = super.query(SysUnionCom.class, cri);
                    SysUnionCom suc = sucs.get(0);
                    return this.recursionUnion(loginOrgId, syo.getSuperId());
                } else {
                    return this.recursionOrg(loginOrgId, syo.getId());
                }
            }
        } else {
            return false;
        }

    }

}
