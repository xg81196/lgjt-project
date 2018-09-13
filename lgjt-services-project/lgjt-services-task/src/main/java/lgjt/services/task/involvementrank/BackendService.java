package lgjt.services.task.involvementrank;

import com.ttsx.platform.nutz.service.BaseService;
import lgjt.domain.task.involvementrank.HumanCount;
import lgjt.domain.task.involvementrank.SysOrganization;
import lombok.extern.log4j.Log4j;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.ArrayList;
import java.util.List;

@Log4j
@IocBean
public class BackendService extends BaseService {

    private static final String HUMAN_COUNT = "backend.org.human.count";

    public List<HumanCount> getHumanCountByOrg() {
        //查询莱钢集团二级
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("super_id", "16");
        List<SysOrganization> sysOrganizations = super.query(SysOrganization.class, cri);

        List<HumanCount> humanCounts = new ArrayList<>();


        for(SysOrganization sys: sysOrganizations) {
            Integer count = 0;
            List<SysOrganization> companyIdList = new ArrayList<>();
            List<SysOrganization> subIdList = queryCompanyIdList(sys.getId());

            getCompanyTreeList(subIdList, companyIdList);

            for(SysOrganization value: companyIdList) {
                cri=Cnd.cri();
                cri.where().andEquals("a.id", value.getId());
                List<HumanCount> again = super.query(HUMAN_COUNT, HumanCount.class, cri);
                if(again.size() > 0) {
                    for(HumanCount s: again) {
                        count += Integer.valueOf(s.getHumanCount());
                    }
                }
            }

            cri=Cnd.cri();
            cri.where().andEquals("a.id", sys.getId());
            List<HumanCount> father = super.query(HUMAN_COUNT, HumanCount.class, cri);
            if(father.size() > 0) {
                for(HumanCount s: father) {
                    count += Integer.valueOf(s.getHumanCount());
                }

            }

            HumanCount human = new HumanCount();
            human.setHumanCount(count);
            human.setOrgId(sys.getId());
            human.setOrgName(sys.getName());
            humanCounts.add(human);
        }


        return humanCounts;
    }

    public List<SysOrganization> queryCompanyIdList(String parentId) {
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("super_id", parentId);

        return  super.query(SysOrganization.class, cri);

    }

    private void getCompanyTreeList(List<SysOrganization> subIdList, List<SysOrganization> companyIdList){

        for(SysOrganization company : subIdList){
            List<SysOrganization> list = queryCompanyIdList(company.getId());
            if(list != null && list.size() > 0){
                getCompanyTreeList(list, companyIdList);
            }

            companyIdList.add(company);
        }
    }

    public Boolean queryParentIdList(String parentId, String orgId) {
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("id", parentId);
        List<SysOrganization> sysOrganizations = super.query(SysOrganization.class, cri);
        if(!sysOrganizations.get(0).getSuperId().equals("16")) {
            return queryParentIdList(sysOrganizations.get(0).getSuperId(), orgId);
        } else {
            if(sysOrganizations.get(0).getId().equals(orgId)) {
                return true;
            } else {
                return false;
            }
        }
    }


}
