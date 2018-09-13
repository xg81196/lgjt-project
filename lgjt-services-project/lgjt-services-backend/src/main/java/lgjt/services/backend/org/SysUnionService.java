package lgjt.services.backend.org;

import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.vo.SysOrganizationVo;
import lgjt.domain.backend.org.vo.SysUnionVo;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.city.SysCityService;
import lgjt.services.backend.industry.SysIndustryService;
import lgjt.services.backend.user.SysUserDataService;

import java.util.*;
import static java.util.stream.Collectors.joining;

import static java.util.stream.Collectors.joining;

/**
 * @author wuguangwei
 * @date 2018/6/13
 * @Description:
 */
@IocBean
public class SysUnionService extends BaseService {



    public static final String QUERY_UNION= "sys.union.queryPage";

    @Inject
    SysCityService sysCityService;

    @Inject
    SysIndustryService sysIndustryService;

    @Inject
    SysUserDataService sysUserDataService;

    public SysUnion get(String id) {
        return super.fetch(SysUnion.class, id);
    }



    /**
     * 删除机构
     * @param ids
     * @return
     */
    public int delete(String ids) {
        String[] str=ids.split(",");
        //判断公司或者机构旗下是否有人旗下是否有人员
        SimpleCriteria cri= Cnd.cri();
        cri.where().andIn("union_id",str);
        List<SysUser> query = super.query(SysUser.class, cri);
        if (query.size() > 0){
            return 0;
        }
        int delete = deleteOrg(ids);
        return delete;
    }


    /**
     * 删除机构
     * @param ids
     * @return
     */
    public int deleteOrg(String ids) {
        if(StringTool.isNotNull(ids)) {
            SimpleCriteria cri = Cnd.cri();
            cri.where().andEquals("super_id", ids);
            if(null != super.fetch(SysUnion.class, cri)) {
                return 0;
            }
            /*
             2018-07-20 赵天意
             删除工会的同时删除管理员
             */
            //第一步，通过工会ID去 USER_DATA 中查
            int isDelete = 0;
            cri = Cnd.cri();
            cri.where().andEquals("object_id", ids);
            List<SysUserData> suds = super.query(SysUserData.class, cri);
            for(SysUserData sud: suds) {
                //第二步，查询管理员账户，不过要首先排除admin id为1的特殊管理员
                if (!sud.getAdminUserId().equals("1")) {
                    //删除管理员账户
                    isDelete = super.delete(SysUserAdmin.class, sud.getAdminUserId());
                }
                //删除关系表
                isDelete = super.delete(SysUserData.class, sud.getId());
            }
            //删除工会
            return isDelete = super.delete(SysUnion.class, ids);
        }
        return 0;
    }


    public SysUnion insertUnion(SysUnion obj) {

        StringJoiner sj = new StringJoiner(",");
        sj.add(obj.getProvince());
        sj.add(obj.getCity());
        sj.add(obj.getCounty());
        obj.setExtend1(obj.getSuperOrgName());
        obj.setUnitScope(sj.toString().trim());
        obj.setSort(0);
        obj.setCrtTime(new Date());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setCrtUser("admin");
        return super.insert(obj);
    }



    public PageResult<SysUnionVo> queryPage(SysUnion obj,String userName,String unionId) {

        SimpleCriteria cri = Cnd.cri();
        /*if (!SysUserAdmin.ADMIN.equals(userName)) {*/
           if (StringUtils.isNotBlank(unionId)) {
               /**
                * fixme 过滤非本节点，
                */
               if (!SysUserAdmin.ADMIN.equals(userName)) {
                   SysUserData sysUserData = new SysUserData();
                   sysUserData.setAdminUserId(UserUtil.getAdminUser().getId());
                   sysUserData.setObjectType(UserDataType.UNION_TYPE);
                   List<SysUserData> sysUserDataList = sysUserDataService.query(sysUserData);
                   if (CollectionUtils.isNotEmpty(sysUserDataList)) {
                       sysUserData = sysUserDataList.get(0);
                       if (!unionId.equals(sysUserData.getObjectId())) {
                           return new PageResult<SysUnionVo>(new ArrayList<SysUnionVo>());
                       }
                   }
               }

               cri.where().andEquals("super_id", unionId);
               cri.where().orEquals("id", unionId);
              // List<SysUnion>  unionList = this.querySubUnionList(unionId);
               List<SysUnion>  unionList = this.query(SysUnion.class,cri);
               if (CollectionUtils.isNotEmpty(unionList)) {
                   String unionIds = unionList.stream().map(SysUnion :: getId).collect(joining(","));
                   cri.where().andIn("id", unionIds.split(","));
               }
           }
       /* }*/

        if (StringUtils.isNotBlank(obj.getName()))  {
            cri.where().andLike("name", obj.getName());
        }

        if (StringUtils.isNotBlank(obj.getProvince()) && !"-1".equals(obj.getProvince()))  {
            cri.where().andLike("unit_scope", obj.getProvince());
        }
        if (StringUtils.isNotBlank(obj.getCity()) && !"-1".equals(obj.getCity()))  {
            cri.where().andLike("unit_scope", obj.getCity());
        }
        if (StringUtils.isNotBlank(obj.getCounty()) && !"-1".equals(obj.getCounty()))  {
            cri.where().andLike("unit_scope", obj.getCounty());
        }

        cri.desc("crt_time");


       // cri.where().andEquals("type",obj.getType());

        PageResult<SysUnionVo>  pageResult = super.queryPage(QUERY_UNION,SysUnionVo.class, obj, cri);
        for (SysUnionVo sysUnion : pageResult.getRows()) {
            sysUnion.setSuperName(sysUnion.getExtend1());
            String[] district = sysUnion.getUnitScope().split(",");
            if (district != null&& !"null".equals(district[0])) {
                if ( district.length==1) {
                    sysUnion.setProvince(district[0]);
                    sysUnion.setUnitScope(sysCityService.fetch(SysCity.class,district[0]).getMergername());
                }else if (district.length==2) {
                    if (!"0".equals(district[1])) {
                        sysUnion.setProvince(district[0]);
                        sysUnion.setCity(district[1]);
                        sysUnion.setUnitScope(sysCityService.fetch(SysCity.class,district[1]).getMergername());
                    }

                } else {
                    if (!"0".equals(district[1])&&!"0".equals(district[2])) {
                        sysUnion.setProvince(district[0]);
                        sysUnion.setCity(district[1]);
                        sysUnion.setCounty(district[2]);
                        sysUnion.setUnitScope(sysCityService.fetch(SysCity.class,district[2]).getMergername());
                    }

                }

            }


            String orgName = this.getUnionString(sysUnion.getId());
            if (StringUtils.isNotBlank(orgName)) {
                if (orgName.lastIndexOf("/") > 0) {
                    sysUnion.setSuperName(orgName.substring(1,orgName.lastIndexOf("/")));
                }else {
                    sysUnion.setSuperName(orgName.substring(1));
                }

            }
            sysUnion.setIndName(sysIndustryService.getIndustryString(sysUnion.getIndId()));

        }
        return pageResult;
    }


    /**
     * 获取某个区域的全名，自动拼接上上级区域名称
     * @return
     */
    public String getUnionString(String regionId) {

        if(regionId == null) {
            return "";
        }
        SysUnion region = this.fetch(SysUnion.class,regionId);
        if(region != null) {
            return getUnionString(region.getSuperId()) +"/"+ region.getName();  //  递归调用方法getRegionString(Long regionId)，停止条件设为regionId==null为真
        }
        return "";
    }


    /**
     * 根据父节点查询
     * @author wuguangwei
     * @param parentId
     * @return
     */
    public List<SysUnion> queryCompanyIdList(String parentId) {
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("super_id", parentId);

        return  super.query(SysUnion.class, cri);

    }


    /**
     * 查询包括本机构及下属机构
     * @author wuguangwei
     * @param companyId
     * @return
     */
    public List<SysUnion> querySubUnionList(String companyId) {

        //公司及下面公司ID列表
        List<SysUnion> companyIdList = new ArrayList<>();
        SysUnion company = super.fetch(SysUnion.class, companyId);

        //获取下面公司ID
        if (!Objects.isNull( company )) {
            List<SysUnion> subIdList = queryCompanyIdList(companyId);
            getCompanyTreeList(subIdList, companyIdList);
            companyIdList.add(company);
        }

        return companyIdList;

    }



    /**
     * 递归
     * @author wuguangwei
     */
    private void getCompanyTreeList(List<SysUnion> subIdList, List<SysUnion> companyIdList){

        for(SysUnion company : subIdList){
            List<SysUnion> list = queryCompanyIdList(company.getId());
            if(list != null && list.size() > 0){
                getCompanyTreeList(list, companyIdList);
            }

            companyIdList.add(company);
        }
    }


    public List<SysUnion> query(SysUnion obj) {
        SimpleCriteria cri = Cnd.cri();
        if(StringTool.isNotNull(obj.getName())) {
            cri.where().andEquals("name", obj.getName());
        }

        if(StringTool.isNotNull(obj.getSuperId())) {
            cri.where().andEquals("super_id", obj.getSuperId());
        }

        if(StringTool.isNotNull(obj.getUnionLeader())) {
            cri.where().andEquals("union_leader", obj.getUnionLeader());
        }
        if(StringTool.isNotNull(obj.getUnionLeaderPhone())) {
            cri.where().andEquals("union_leader_phone", obj.getUnionLeaderPhone());
        }
        if(StringTool.isNotNull(obj.getIndId())) {
            cri.where().andEquals("ind_id", obj.getIndId());
        }
        if(StringTool.isNotNull(obj.getLogoUrl())) {
            cri.where().andEquals("logo_url", obj.getLogoUrl());
        }
        if(StringTool.isNotNull(obj.getBannerUrl())) {
            cri.where().andEquals("banner_url", obj.getBannerUrl());
        }
        if(StringTool.isNotNull(obj.getIntroduce())) {
            cri.where().andEquals("introduce", obj.getIntroduce());
        }

        if(StringTool.isNotNull(obj.getStatus())) {
            cri.where().andEquals("status", obj.getStatus());
        }
        if(StringTool.isNotNull(obj.getSort())) {
            cri.where().andEquals("sort", obj.getSort());
        }

        if(StringTool.isNotNull(obj.getCrtUser())) {
            cri.where().andEquals("crt_user", obj.getCrtUser());
        }
        if(StringTool.isNotNull(obj.getCrtTime())) {
            cri.where().andEquals("crt_time", obj.getCrtTime());
        }
        if(StringTool.isNotNull(obj.getCrtIp())) {
            cri.where().andEquals("crt_ip", obj.getCrtIp());
        }
        if(StringTool.isNotNull(obj.getUpdUser())) {
            cri.where().andEquals("upd_user", obj.getUpdUser());
        }
        if(StringTool.isNotNull(obj.getUpdTime())) {
            cri.where().andEquals("upd_time", obj.getUpdTime());
        }
        if(StringTool.isNotNull(obj.getUpdIp())) {
            cri.where().andEquals("upd_ip", obj.getUpdIp());
        }

        return super.query(SysUnion.class, cri);



    }

    public SysUnion checkName(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("name",value);
        return super.fetch(SysUnion.class,cri);
    }



}
