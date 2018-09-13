package lgjt.services.help;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import lgjt.domain.help.ERPWorkToDo;
import lgjt.domain.help.LgHelpInfo;
import lgjt.domain.help.WenNuan;
import lgjt.domain.help.vo.HelpInfoToDoVo;
import lgjt.domain.help.vo.HelpInfoView;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.*;


@Log4j
@IocBean(fields = {"dao:daoHelp"})
public class LgHelpInfoService extends BaseService {


    /**
     * 分页查询申报信息
     *
     * @param
     * @param userName
     * @param
     * @param page
     * @return
     */
    public PageResult<HelpInfoView> queryPage(String userName, Integer pageSize, Integer page, String startTime, String endTime, Integer type) {
        log.info("queryPage-----------当前页数：" + page + "------数据大小：" + pageSize);

        //所有申报信息
        PageResult<LgHelpInfo> helpList = getListInfo(userName, pageSize, page, startTime, endTime);
        log.info("--------------开始查询申请表（lg_help_info）的信息 -----------");
        //温暖表信息
        List<WenNuan> wenNuanList = queryWenNuanList();

        //代办表
        List<ERPWorkToDo> erpList = queryERPWorkToDoList();
        System.out.println("ERP-------------------------"+erpList.size());

        if (helpList == null) {
            return  null;
        }

        Set<HelpInfoView> hset = new HashSet<>();


        for (LgHelpInfo l : helpList.getRows()) {
            System.out.println("help------------------------------------"+l.getIdNo());
            //判断送温暖表的数据 不为空 并且size大于0
            if (wenNuanList != null && wenNuanList.size() > 0) {
                for (int i = 0; i < wenNuanList.size(); i++) {
                    System.out.println("wennuan------------------------------------"+wenNuanList.get(i).getWn_sfz());
                    if (l.getIdNo().trim().equals(wenNuanList.get(i).getWn_sfz())) {
                        // 如果当前申请信息已经在温暖表中存在 并且身份证号码相同，然后在循环代办表数据，查找该条记录的状态
                        if (erpList != null && erpList.size() > 0) {
                            for (int i2 = 0; i2 < erpList.size(); i2++) {
                                System.out.println("erp---------------"+wenNuanList.get(i).getApp_id() +"=============="+ erpList.get(i2).getApp_ID()); // 1
                                if (wenNuanList.get(i).getApp_id().trim().equals(erpList.get(i2).getApp_ID())) {
                                    System.out.println("erp2---------------"+wenNuanList.get(i).getApp_id() +"=============="+ erpList.get(i2).getApp_ID());
                                    HelpInfoView h = new HelpInfoView();
                                    h.setId(l.getId());
                                    h.setCrt_time(l.getCrt_time());
                                    h.setRel_name(l.getRel_name());
                                    h.setStateNow(erpList.get(i2).getStateNow());
                                    h.setType(l.getType());
                                    hset.add(h);
                                } else {
                                    HelpInfoView h = new HelpInfoView();
                                    h.setId(l.getId());
                                    h.setCrt_time(l.getCrt_time());
                                    h.setRel_name(l.getRel_name());
                                    h.setStateNow("等待审核");
                                    h.setType(l.getType());
                                    hset.add(h);
                                }
                            }
                        } else {
                            //代办数据是空
                            HelpInfoView h = new HelpInfoView();
                            h.setId(l.getId());
                            h.setCrt_time(l.getCrt_time());
                            h.setRel_name(l.getRel_name());
                            h.setStateNow("等待审核");
                            h.setType(l.getType());
                            hset.add(h);
                        }
                    } else {
                        //如果身份证不等，那就new一个对象，说明是刚刚申请或者对方还没有来的及处理的申报信息
                        HelpInfoView h = new HelpInfoView();
                        h.setId(l.getId());
                        h.setCrt_time(l.getCrt_time());
                        h.setRel_name(l.getRel_name());
                        h.setStateNow("等待审核");
                        h.setType(l.getType());
                        hset.add(h);
                    }

                }
            } else {
                HelpInfoView h = new HelpInfoView();
                h.setId(l.getId());
                h.setCrt_time(l.getCrt_time());
                h.setRel_name(l.getRel_name());
                h.setStateNow("等待审核");
                h.setType(l.getType());
                hset.add(h);
            }
        }


        List<HelpInfoView> lv2 = new ArrayList<>();
        for (HelpInfoView v : hset) {
            lv2.add(v);
        }



        if (type == 0) {
            PageResult<HelpInfoView> result = new PageResult<>(lv2, hset.size());
            return result;
        }

        if (type == 1) {
            List<HelpInfoView> lvl1 = new ArrayList<>();
            PageResult<HelpInfoView> result = new PageResult<>(lv2, hset.size());
            for (HelpInfoView v : result.getRows()) {
                if ("正在办理".trim().equals(v.getStateNow().trim())) {
                    lvl1.add(v);
                }
                result.setTotal(lvl1.size());
                result.setRows(lvl1);
            }
            return result;
        }
        if (type == 2) {
            List<HelpInfoView> lvl1 = new ArrayList<>();
            PageResult<HelpInfoView> result = new PageResult<>(lv2, hset.size());
            for (HelpInfoView v : result.getRows()) {
                if ("正在办理".equals(v.getStateNow())) {
                    lvl1.add(v);
                }
                result.setTotal(lvl1.size());
                result.setRows(lvl1);
            }
            return result;
        }

        return null;
    }


    /**
     * 管理员查询记录
     *
     * @param
     * @return
     */
    public PageResult<LgHelpInfo> queryPageInfo(LgHelpInfo obj) {
        SimpleCriteria cri = Cnd.cri();

        if (StringTool.isNotNull(obj.getRel_name())) {

            cri.where().andLike("rel_name", obj.getRel_name());
        }
        if (StringTool.isNotNull(obj.getIdNo())) {

            cri.where().andLike("id_no", obj.getIdNo());
        }
        if (StringTool.isNotNull(obj.getType())) {
            cri.where().andLike("type", obj.getType());
        }
        if (StringTool.isNotNull(obj.getType2())) {
            cri.where().andLike("type2", obj.getType2());
        }
        if (StringTool.isNotNull(obj.getAttachId())) {
            cri.where().andLike("attach_id", obj.getAttachId());
        }
        if (StringTool.isNotNull(obj.getCause())) {
            cri.where().andLike("cause", obj.getCause());
        }
        if (StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andLike("crt_user", obj.getCrtUser());
        }
        if (StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andLike("crt_ip", obj.getCrtIp());
        }
        if (StringTool.isNotNull(obj.getUpdUser())) {
            cri.where().andLike("upd_user", obj.getUpdUser());
        }
        if (StringTool.isNotNull(obj.getUpdIp())) {
            cri.where().andLike("upd_ip", obj.getUpdIp());
        }
        HashMap<String, Object> param = new HashMap<>();
        return super.queryPage(LgHelpInfo.class, obj, cri);
    }


    /**
     * 查询代办事项
     *
     * @param obj
     * @return
     */
    public PageResult<HelpInfoView> queryCount(ERPWorkToDo obj, String name, String startTime, String endTime) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andLike("ShenPiRenList",name);
        if(StringTool.isNotEmpty(startTime) && StringTool.isNotEmpty(endTime)){
            cri.where().andBetween("TimeStr",startTime,endTime);
        }
        //查询我的全部代办信息
        PageResult<ERPWorkToDo> ERPWorkToDoPageResult = super.queryPage(ERPWorkToDo.class, obj, cri);
        //所有申报信息
        PageResult<LgHelpInfo> helpList = getListInfo(name, obj.getPageSize(), obj.getPage(), startTime, endTime);
        //温暖表信息
        List<WenNuan> wenNuanList = queryWenNuanList();

        //是否为空
        if(ERPWorkToDoPageResult==null){
            return new PageResult<>(new ArrayList<HelpInfoView>(),0);
        }
        if(ERPWorkToDoPageResult.getRows().size()<=0){
            return new PageResult<>(new ArrayList<HelpInfoView>(),0);
        }

        Set<HelpInfoView> hset = new HashSet<>();
        for (ERPWorkToDo v:ERPWorkToDoPageResult.getRows()){
            //判断温暖申请信息是否为空
            if(wenNuanList != null && wenNuanList.size()>0){
                for (int i = 0; i < wenNuanList.size(); i++) {
                    if(v.getApp_ID().trim().equals(wenNuanList.get(i).getApp_id())){
                        if(helpList!=null && helpList.getRows().size()>0){
                            for (int i2 = 0; i2 < helpList.getRows().size(); i2++){
                                if(helpList.getRows().get(i2).getIdNo().trim().equals(wenNuanList.get(i).getWn_sfz())){
                                    HelpInfoView h = new HelpInfoView();
                                    h.setId(helpList.getRows().get(i2).getId());
                                    h.setCrt_time(helpList.getRows().get(i2).getCrt_time());
                                    h.setRel_name(helpList.getRows().get(i2).getRel_name());
                                    h.setStateNow(v.getStateNow());
                                    h.setType(helpList.getRows().get(i2).getType());
                                    hset.add(h);
                                }
                            }
                        }
                    }
                }
            }
        }
        List<HelpInfoView> ll = new ArrayList<>();
        for (HelpInfoView v : hset){
            ll.add(v);
        }
        return new PageResult<>(ll,ll.size());
    }

    public LgHelpInfo get(String id) {
        return super.fetch(LgHelpInfo.class, id);
    }

    public int delete(String ids) {
        if (StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andIn("id", ids.split(","));
            return super.delete(LgHelpInfo.class, cri);
        }
        return 0;
    }

    public LgHelpInfo checkId(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("id", value);
        return super.fetch(LgHelpInfo.class, cri);
    }

    public PageResult<LgHelpInfo> getListInfo(String name, Integer pageSize, Integer page, String startTime, String endTime) {
        System.out.println("getListInfo" + pageSize + "-----------" + page);
        SimpleCriteria cri = Cnd.cri();
        LgHelpInfo l = new LgHelpInfo();
        l.setPageSize(pageSize);
        l.setPage(page);
        cri.where().andEquals("crt_user", name);
        cri.desc("crt_time");
        if (StringTool.isNotEmpty(startTime) || StringTool.isNotEmpty(endTime)) {
            //加时间判断
            cri.where().andBetween("crt_time", startTime, endTime);
        }
        return super.queryPage(LgHelpInfo.class, l, cri);
    }


    public List<WenNuan> queryWenNuanList() {
        return super.query(WenNuan.class, null);
    }


    public List<ERPWorkToDo> queryERPWorkToDoList() {
        return super.query(ERPWorkToDo.class, null);
    }


}