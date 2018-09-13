package lgjt.web.task.Job.impl;

import lgjt.domain.task.involvementrank.HumanCount;
import lgjt.domain.task.involvementrank.InvolvementRank;
import lgjt.domain.task.involvementrank.RushLevelRank;
import lgjt.domain.task.involvementrank.SysOrganization;
import lgjt.services.task.involvementrank.BackendService;
import lgjt.services.task.involvementrank.InvolvementRankService;
import lgjt.services.task.involvementrank.RushService;
import lgjt.web.task.Job.TaskJob;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.quartz.JobDataMap;

import java.util.List;
import java.util.UUID;

public class TaskJobCalculationInvolvementImpl extends TaskJob {

    @Inject
    BackendService backendService;

    @Inject
    RushService rushService;

    @Inject
    InvolvementRankService involvementRankService;

    @Override
    public String work(JobDataMap jobDataMap) throws Exception {

        List<HumanCount> humanCounts = backendService.getHumanCountByOrg();
        List<RushLevelRank> rushLevelRanks = rushService.queryRushByCompanyName();

        involvementRankService.deleteAll();
        for(HumanCount hc: humanCounts) {
            int rushCount = 0;
            for(RushLevelRank rushLevelRank: rushLevelRanks) {
                if(backendService.queryParentIdList(rushLevelRank.getCompanyId(), hc.getOrgId())) {
                    rushCount ++;
                }
            }
            hc.setRushCount(rushCount);

            //入库
            InvolvementRank data = new InvolvementRank();
            data.setId(hc.getOrgId());
            data.setOrgId(hc.getOrgId());
            data.setOrgName(hc.getOrgName());


            Double rush = Double.valueOf(hc.getRushCount());
            Double human = Double.valueOf(hc.getHumanCount());

            Double invo =  rush / human;
            if(Double.isNaN(invo)) {
                invo = 0.0;
            }

            data.setInvolvement(invo);
            involvementRankService.insert(data);
        }
        return "更新公司参与度完毕";
    }

}
