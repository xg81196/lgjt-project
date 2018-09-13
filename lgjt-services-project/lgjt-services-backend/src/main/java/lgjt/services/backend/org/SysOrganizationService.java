package lgjt.services.backend.org;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.ttsx.platform.nutz.result.PageResult;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;
import lgjt.domain.backend.org.KsOrg;
import lombok.extern.log4j.Log4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SimpleCriteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.common.base.constants.UserDataType;
import lgjt.common.base.utils.ClientInfo;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.org.SysOrganization;
import lgjt.domain.backend.org.SysUnion;
import lgjt.domain.backend.org.SysUnionCom;
import lgjt.domain.backend.org.vo.SysOrganizationVo;
import lgjt.domain.backend.role.SysMenu;
import lgjt.domain.backend.user.SysUser;
import lgjt.domain.backend.user.SysUserAdmin;
import lgjt.domain.backend.user.SysUserData;
import lgjt.domain.backend.utils.UserUtil;
import lgjt.services.backend.city.SysCityService;
import lgjt.services.backend.industry.SysIndustryService;
import lgjt.services.backend.user.SysUserDataService;

import java.lang.reflect.InvocationTargetException;
import java.text.Collator;
import java.util.*;

import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.joining;

/**
 * @author wuguangwei
 * @date 2018/4/16
 * @Description:
 */

@Log4j
@IocBean
public class SysOrganizationService extends BaseService {

    public static final String QUERY_ORG = "sys.org.queryPage";


    public static final String GET_ALL_USER_SCOPE = "sys.department.getUserData";

    @Inject
    SysCityService sysCityService;

    @Inject
    SysIndustryService sysIndustryService;

    @Inject
    SysUnionComService sysUnionComService;

    @Inject
    SysUserDataService sysUserDataService;

    @Inject
    SysUnionService sysUnionService;

    @Inject
    KsOrgService ksOrgService;

    public PageResult<SysOrganizationVo> queryPage(SysOrganization obj,String userName) {

        SimpleCriteria cri = Cnd.cri();
      /*  if (!SysUserAdmin.ADMIN.equals(userName)) {*/
            /**
             *TODO:考虑缓存 查工会下所有企业
             */

             if (StringUtils.isNotBlank(obj.getOrgId())) {
                 //List<SysUnionCom> sysUnionComList = sysUnionComService.getUnionComByUnionId(obj.getOrgId());
                 List<SysUnion> sysUnionList = sysUnionService.querySubUnionList(obj.getOrgId());

                 /**
                  * 查工会
                  */
                 List<SysUnionCom> sysUnionComList = Lists.newArrayList();
                 for (SysUnion sysUnion : sysUnionList)  {
                     sysUnionComList.addAll(sysUnionComService.getUnionComByUnionId(sysUnion.getId())) ;
                 }

                 /**
                  * 查询所有企业
                  */
                 List<SysOrganization> sysOrganizationList = Lists.newArrayList();
                 for (SysUnionCom sysUnionCom : sysUnionComList)  {
                     sysOrganizationList.addAll( sysUserDataService.querySubOrgList(sysUnionCom.getComId()));
                 }


                 if (CollectionUtils.isNotEmpty(sysOrganizationList)) {
                     String comIds = sysOrganizationList.stream().map(SysOrganization:: getId).collect(joining(","));
                     cri.where().andIn("id", comIds.split(","));
                 }else {
                     return new PageResult<SysOrganizationVo>(new ArrayList<SysOrganizationVo>());
                 }
             }else {
                 List<SysUserData> sysUserDataList = sysUserDataService.getOrgsByUserId(UserUtil.getAdminUser().getId());
                 List<SysOrganization> organizationList = Lists.newArrayList();
                 List<SysUnionCom> sysUnionComList = Lists.newArrayList();
                 for (SysUserData sysUserData : sysUserDataList) {
                     if (sysUserData.getObjectType() == UserDataType.UNION_TYPE) {
                         List<SysUnion> sysUnionList = sysUnionService.querySubUnionList(sysUserData.getObjectId());
                         for (SysUnion sysUnion : sysUnionList) {
                             sysUnionComList.addAll(sysUnionComService.getUnionComByUnionId(sysUnion.getId())) ;
                         }


                     } else {
                         organizationList.addAll(sysUserDataService.querySubOrgList(sysUserData.getObjectId())) ;

                     }
                 }

                 /**
                  * 查询所有企业
                  */

                 for (SysUnionCom sysUnionCom : sysUnionComList)  {
                     organizationList.addAll( sysUserDataService.querySubOrgList(sysUnionCom.getComId()));
                 }



                /* String comIds = sysUserDataList.stream().map(SysUserData:: getObjectId).collect(joining(","));
                 String[] comIdss = comIds.split(",");
                 List<SysOrganization> sysOrganizations = Lists.newArrayList();
                 for ( String comId : comIdss ) {
                     List<SysOrganization> sysOrganizationList = sysUserDataService.querySubOrgList(comId);
                     sysOrganizations.addAll(sysOrganizationList);
                 }*/
                  String comIds = organizationList.stream().map(SysOrganization:: getId).collect(joining(","));
                 cri.where().andIn("id", comIds.split(","));
             }


      /*  }*/

        if (StringUtils.isNotBlank(obj.getName()))  {
            cri.where().andLike("name", obj.getName());
        }

        if (StringUtils.isNotBlank(obj.getOrgCode()))  {
            cri.where().andEquals("org_code", obj.getOrgCode());
        }

        if (StringUtils.isNotBlank(obj.getIdentifierCode()))  {
            cri.where().andEquals("identifier_code", obj.getIdentifierCode());
        }

        cri.desc("crt_time");
       //cri.where().andEquals("type",obj.getType());
        SysUnion sysUnion = null;
        if (StringUtils.isNotBlank(obj.getOrgId())) {
             sysUnion = sysUnionService.get(obj.getOrgId());
        }
        /**
         * 2018-07-18 赵天意
         * 解决无法显示子企业的BUG
         *
         */
//        PageResult<SysOrganizationVo>  pageResult = super.queryPage(QUERY_ORG,SysOrganizationVo.class, obj, cri);
        List<SysOrganizationVo> resultList = super.query(SysOrganizationVo.class, cri);
//        List<String> superIdList = new ArrayList<>();
//        for (SysOrganizationVo sysOrganization : resultList) {
//            superIdList.add(sysOrganization.getId());
//        }
//
//        for(String superId: superIdList) {
//            //递归
//            resultList.addAll(checkOrgChildNode(superId));
//        }


        for (SysOrganizationVo sysOrganization : resultList) {
            sysOrganization.setOrgId(sysOrganization.getExtend2());
            sysOrganization.setSuperId(sysOrganization.getExtend3());
            if (StringUtils.isNotBlank(sysOrganization.getSuperId())) {
                StringBuilder sb = new StringBuilder();
                sb= this.getOrgNameString(sysOrganization.getId(),sb);
                String orgName = sb.toString()/*getOrgString(sysOrganization.getId())*/;
                if (StringUtils.isNotBlank(orgName)) {
                    sysOrganization.setSuperId(orgName);
                }
            }
            if (!Objects.isNull(sysUnion)) {
                sysOrganization.setSuperName(sysUnion.getName());
            }
            sysOrganization.setIndName(sysIndustryService.getIndustryString(sysOrganization.getIndId()));
        }

        PageResult<SysOrganizationVo>  pageResult = new PageResult<>(resultList);

        Collections.sort(pageResult.getRows(), new Comparator<SysOrganizationVo>() {
            Collator collator = Collator.getInstance(java.util.Locale.CHINA);
            public int compare(SysOrganizationVo o1, SysOrganizationVo o2) {
                String name1 = o1.getName();
                String name2  = o2.getName();
                if(collator.compare(name1,name2)>0)
                    return 1;
                else
                    return -1;
            }
        });
        return pageResult;
    }


    private SimpleCriteria getCri(SysOrganization obj) {
        SimpleCriteria cri = Cnd.cri();

        if(StringTool.isNotNull(obj.getName())) {
            cri.where().andLike("name", obj.getName());
        }
        if(StringTool.isNotNull(obj.getSuperId())) {
            cri.where().andEquals("super_id", obj.getSuperId());
        }
        if(StringTool.isNotNull(obj.getSort())) {
            cri.where().andEquals("sort", obj.getSort());
        }
        if(StringTool.isNotNull(obj.getType())) {
            cri.where().andEquals("type", obj.getType());
        }
        if(StringTool.isNotNull(obj.getOrgCode())) {
            cri.where().andEquals("org_code", obj.getOrgCode());
        }
        if(StringTool.isNotNull(obj.getStatus())) {
            cri.where().andEquals("status", obj.getStatus());
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
        return cri;
    }

    public SysOrganization get(String id) {
        return super.fetch(SysOrganization.class, id);
    }



    public List<SysOrganization> queryOrgList(SysOrganization obj) {
        return super.query(SysOrganization.class,getCri(obj));
    }



    public List<SysOrganization> queryOrg(SysOrganization obj,SysUserAdmin sysUserAdmin,String userData) {

        //若为平台管理员查询所有工会及企业

        if(SysUserAdmin.ADMIN.equalsIgnoreCase(sysUserAdmin.getUserName())){
            return super.query(SysOrganization.class, getCri(obj));
        } else {
            if (StringUtils.isNotBlank(userData)) {
                SimpleCriteria cri = Cnd.cri();
                if (obj.getType() == 1) {
                    cri.where().andIn("id", userData.split(","));
                    List<SysOrganization> organizations = super.query(SysOrganization.class, cri);
                    if (CollectionUtils.isNotEmpty(organizations)) {
                        String ids = organizations.stream().map(SysOrganization :: getId).collect(joining(","));
                        cri = Cnd.cri();
                        cri.where().andIn("id", ids.split(","));
                        return super.query(SysOrganization.class, cri);
                    }

                }
                //查询本企业及下属企业
                //cri = Cnd.cri();
                //cri.where().andIn("userId", userData.split(","));
                Map<String,Object> params = new HashMap<>(1);
                params.put("userId",sysUserAdmin.getId());
                return  super.query(GET_ALL_USER_SCOPE,SysOrganization.class, null,params);
              //  return  sysUserDataService.querySubOrgList(orgId);

            }

        }
        return null;
    }


    public List<SysOrganization> query(SysOrganization obj) {
        SimpleCriteria cri = Cnd.cri();
        if(StringTool.isNotNull(obj.getName())) {
            cri.where().andEquals("name", obj.getName());
        }
        if(StringTool.isNotNull(obj.getOrgCode())) {
            cri.where().andEquals("org_code", obj.getOrgCode());
        }
        if(StringTool.isNotNull(obj.getIdentifierCode())) {
            cri.where().andEquals("identifier_code", obj.getIdentifierCode());
        }

        if(StringTool.isNotNull(obj.getUnitPropertyCategory())) {
            cri.where().andEquals("unit_property_category", obj.getUnitPropertyCategory());
        }
        if(StringTool.isNotNull(obj.getEconomicType())) {
            cri.where().andEquals("economic_type", obj.getEconomicType());
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
        if(StringTool.isNotNull(obj.getType())) {
            cri.where().andEquals("type", obj.getType());
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

        return super.query(SysOrganization.class, cri);



    }


    /**
     * 删除机构
     * @param ids
     * @return
     */
    public int delete(String ids) {
        String[] str=ids.split(",");
        //判断公司或者机构旗下是否有人旗下是否有人员
        SimpleCriteria cri=Cnd.cri();
        cri.where().andIn("org_id",str).orIn("com_id",str).orIn("union_id",str);
        List<SysUser> query = super.query(SysUser.class, cri);
        if (query.size() > 0) {
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
            if(null != super.fetch(SysOrganization.class, cri)) {
                return 0;
            }
            /*
            2018-07-20 赵天意
            删除企业的同时删除管理员账号
         */
            int delete = 0;
            cri = Cnd.cri();
            cri.where().andEquals("object_id", ids);
            List<SysUserData> suds = super.query(SysUserData.class, cri);
            for(SysUserData sud: suds) {
                //第二步，查询管理员账户，不过要首先排除admin id为1的特殊管理员
                if (!sud.getAdminUserId().equals("1")) {
                    //删除管理员账户
                    delete = super.delete(SysUserAdmin.class, sud.getAdminUserId());
                }
                //删除关系表
                delete = super.delete(SysUserData.class, sud.getId());
            }
            return delete = super.delete(SysOrganization.class, ids);
        }
        return 0;
    }


    public SysOrganization insert(SysOrganization obj) {

        obj.setExtend1(obj.getSuperOrgName());
        obj.setCrtTime(new Date());
        obj.setCrtIp(ClientInfo.getIp());
        obj.setCrtUser("admin");
        SysOrganization organization = super.insert(obj);

        /**
         * 添加企业所属公会
         */
       /* SysUnionCom sysUnionCom = new SysUnionCom();
        sysUnionCom.setComId(id);
        sysUnionCom.setUnionId(obj.getSuperId());
        sysUnionCom.setComName(obj.getName());
        sysUnionComService.insert(sysUnionCom);*/
        return organization;
    }


    /**
     * 一次性全部查询出机构树
     * @return
     */
    public List<SysOrganization> queryOrganizationTree(int type,String userData,String username) {

        Criteria cri = Cnd.cri();


        /**
         * business:0:企业
         union:1:工会
         ORGANIZATION 2:组织机构
         */
         /*if ( type == 2 || type == 0) {
             cri.where().andEquals("type",2).orEquals("type",0);
         }*/
        //cri.where().andEquals("type",2).orEquals("type",0).orEquals("type",1);
        if (StringUtils.isNotBlank(userData) && !SysUserAdmin.ADMIN.equals(username) )  {
            cri.where().andIn("id",userData.split(","));
        }

        cri.where().andEquals("status",0);
        List<SysOrganization> organizations = super.query(SysOrganization.class, cri);

        Map<String,List<SysOrganization>> p2sons = new HashMap<>();
        for(SysOrganization organization:organizations) {
            if(p2sons.containsKey(organization.getSuperId())) {
                p2sons.get(organization.getSuperId()).add(organization);
            }else {
                List<SysOrganization> list = new ArrayList<>();
                list.add(organization);
                p2sons.put(organization.getSuperId(), list);
            }
        }

        List<SysOrganization> result = new ArrayList<>();
        if (p2sons.containsKey("-1")) {
            for (SysOrganization org : p2sons.get("-1")) {
                getSub(org, p2sons);
                result.add(org);
            }
        }else {
            for (Map.Entry<String,List<SysOrganization>> map : p2sons.entrySet())  {
                for (SysOrganization org : map.getValue()) {
                    getSub(org, p2sons);
                    result.add(org);
                }
            }
        }

        SysOrganization fixedTopRes = this.getFixedTopRes();

    /*    List<SysOrganization> topRess = p2sons.get("-1");
        if(topRess!=null&&topRess.size()>0){
            for (SysOrganization topRes : topRess) {
                topRes.setList(p2sons.get(topRes.getId()));
            }
        }*/
        fixedTopRes.setList(result);
        List<SysOrganization> list = new ArrayList<>();
        list.add(fixedTopRes);
        return list;
    }

    private void getSub(SysOrganization org, Map<String, List<SysOrganization>> p2son) {
        if (p2son.containsKey(org.getId())) {
            for (SysOrganization obj : p2son.get(org.getId())) {
                getSub(obj, p2son);
            }
            org.setList(p2son.get(org.getId()));
        }
    }

    private SysOrganization getFixedTopRes() {
        SysOrganization resource = new SysOrganization();
        resource.setId("-1");
        resource.setName("全国女工工会");
        return resource;
    }



    /**
     * 查询机构树(前台异步版)
     * @Date 2017-12-25
     * @author majinyong
     * @return
     */
    public List<SysOrganization> listTree(SysOrganization obj) {
        if(StringUtils.isBlank(obj.getSuperId())){
            List<SysOrganization> list = new ArrayList<>();
            SysOrganization organization = new SysOrganization();
            organization.setName("机构树");
            organization.setId("-1");
            organization.setType(0);
            list.add(organization);
            return list;
        }
        if("-1".equals(obj.getSuperId())){
            SimpleCriteria cri=Cnd.cri();
            cri.where().andEquals("type",0);
            cri.where().andEquals("super_id","-1");
            return super.query(SysOrganization.class, cri);

        }else {
            if ("1".equals(obj.getType())){
                List<SysOrganization> list = new ArrayList<>();
                SimpleCriteria cri=Cnd.cri();
                cri.where().andEquals("type",2);
                cri.where().andEquals("super_id",obj.getSuperId());
                return  super.query(SysOrganization.class,cri);

            }else if("2".equals(obj.getType())) {
                SysOrganization org = new SysOrganization();
                org.setSuperId(obj.getSuperId());
                List<SysOrganization> query = query(org);
                if (query.size() > 0) {
                    for (SysOrganization organization : query) {
                        organization.setType(0);
                    }
                }
                return query;
            }
        }
        return null;
    }




    /**
     * 获取某个区域的全名，自动拼接上上级区域名称
     * @return
     */
    public String getOrgString(String regionId) {

        if(regionId == null) {
            return "";
        }
        SysOrganization region = this.fetch(SysOrganization.class,regionId);
        if(region != null) {
             return getOrgString(region.getSuperId()) +"/"+ region.getName();  //  递归调用方法getRegionString(Long regionId)，停止条件设为regionId==null为真
        }
        return "";
    }



    /**
     * 获取某个区域的全名，自动拼接上上级区域名称
     * @return
     */
    public StringBuilder getOrgNameString(String orgName,StringBuilder sb) {

        if(StringUtils.isBlank(orgName)) {
            return sb;
        }
        SimpleCriteria cri=Cnd.cri();
        cri.where().andEquals("id",orgName);
        SysOrganization region = this.fetch(SysOrganization.class,cri);
        if(region != null) {
            sb.append(region.getName());
            cri=Cnd.cri();
            cri.where().andEquals("id",region.getSuperId());
             region = this.fetch(SysOrganization.class,cri);
            if(region != null ) {
                sb.insert(0,region.getName()+"/");
                cri=Cnd.cri();
                cri.where().andEquals("id",region.getSuperId());
                region = this.fetch(SysOrganization.class,cri);
                if (region != null ) {
                    sb.insert(0,region.getName()+"/");
                }

            }

            //  递归调用方法getRegionString(Long regionId)，停止条件设为regionId==null为真
            //return sb ;
        }
        return sb;
    }



    /**
     * 查询数据权限树
     * @param userId
     * @return
     */
    public List<SysOrganization> queryUserDataScopeTree(String userId) {

        // 获取登陆用户的权限和权限所对应的资源
        List<SysOrganization> resources = getUserSysOrganization(userId);

        // 查询所有父节点
        List<SysOrganization> topResources = getTopOrganization();

        topResources.add(0, this.bindFixedRes());

        List<SysOrganization> renderResource = new ArrayList<>();
        renderResource.add(this.bindFixedRes());

        for (SysOrganization topResource : topResources) {
            String topResouceId = topResource.getId();
            for (SysOrganization resource : resources) {
                if (resource.getSuperId().equals(topResouceId)) {
                    topResource.getList().add(resource);
                }
            }
            // 过滤没有子节点的父节点
            if (topResource.getList().size() > 0) {
                renderResource.add(topResource);
            }
        }

        return renderResource;
    }


    private SysOrganization bindFixedRes() {
        SysOrganization resource = new SysOrganization();
        resource.setId("-1");
        resource.setName("机构树");
        resource.setList(new ArrayList<>());
        return resource;
    }


    /**
     * 获取用户所属机构
     * @param userId
     * @return
     */
    public List<SysOrganization> getUserSysOrganization(String userId) {

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        Criteria cri = Cnd.cri();
        cri.getOrderBy().asc("sort");
        return super.query(GET_ALL_USER_SCOPE, SysOrganization.class, cri, param);
    }


    public List<SysOrganization> getTopOrganization() {
        Criteria cri = Cnd.cri();
        cri.where().andEquals("super_id", -1);
        cri.getOrderBy().asc("sort").asc("id");
        return super.query(SysOrganization.class, cri);
    }


    public SysOrganization checkOrg_name(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("name",value);
        return super.fetch(SysOrganization.class,cri);
    }


    public SysOrganization checkSuperOrg_name(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("extend1",value);
        return super.fetch(SysOrganization.class,cri);
    }


    public SysOrganization checkOrg_code(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("org_code",value);
        return super.fetch(SysOrganization.class,cri);
    }


    public SysOrganization checkOrg_identifier_code(String value) {
        SimpleCriteria cri = Cnd.cri();
        cri.where().andEquals("identifier_code",value);
        return super.fetch(SysOrganization.class,cri);
    }

    /**
     * 查询当前数据和他所有的子集
     * zhaotianyi
     * 2018-07-19
     * @param parentId
     * @return
     */
    public List<SysOrganization> querOrgAndChild(String parentId) {
        List<SysOrganizationVo> list = this.checkOrgChildNode(parentId);
        List<SysOrganization> soList = new ArrayList<>();

        SimpleCriteria cri = Cnd.cri();
        cri = Cnd.cri();
        cri.where().andEquals("id", parentId);
        soList = super.query(SysOrganization.class, cri);

        for(SysOrganizationVo vo: list) {
            SysOrganization so = new SysOrganization();
            try {
                BeanUtils.copyProperties(so, vo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            soList.add(so);
        }
        return soList;
    }

    /**
     * 递归查询所有企业的子集
     * 2018-07-18
     * 赵天意
     * @param superId
     * @return
     */
    private List<SysOrganizationVo> checkOrgChildNode(String superId) {

        SimpleCriteria cri = Cnd.cri();
        cri = Cnd.cri();
        cri.where().andEquals("super_id", superId);
        List<SysOrganizationVo> resultList = super.query(SysOrganizationVo.class, cri);
        resultList = orgChildNode(resultList);
        return resultList;
    }

    /**
     * 递归查询所有企业的子集
     * 2018-07-18
     * 赵天意
     *
     */
    private List<SysOrganizationVo> orgChildNode(List<SysOrganizationVo> list) {

        for(int i=0; i < list.size(); i++) {
            SimpleCriteria cri = Cnd.cri();
            cri = Cnd.cri();
            cri.where().andEquals("super_id", list.get(i).getId());
            if(super.query(SysOrganizationVo.class, cri).size() > 0) {
                List<SysOrganizationVo> listAgain = super.query(SysOrganizationVo.class, cri);
                list.get(i).setList(listAgain);
                orgChildNode( list.get(i).getList());
            }
        }
        return list;
    }

    /**
     * 导入企业信息
     * @return
     */
    public Integer getOrgBySQLServer() {
        //删除所有org
        SimpleCriteria cri = Cnd.cri();
        super.delete(SysOrganization.class, cri);
        List<KsOrg> ksOrgs = ksOrgService.getKsOrgList();
        int i = 0;
        for(KsOrg ksOrg: ksOrgs) {
            SysOrganization org = new SysOrganization();
            org.setValueForKsOrg(ksOrg);
            super.insert(org);
            i++;
        }
        return i;
    }
}
