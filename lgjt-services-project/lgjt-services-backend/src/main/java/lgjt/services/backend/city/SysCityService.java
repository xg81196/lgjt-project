package lgjt.services.backend.city;

import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.IocBean;
import lgjt.domain.backend.city.SysCity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IocBean
public class SysCityService extends BaseService {

	public List<SysCity> listSons(String pid, Integer status) {
		if (StringTool.isNull(pid)) {
			pid = SysCity.CHINA_ID;
		}

		Criteria cri = Cnd.cri();
		cri.where().andEquals("parentid", pid);
		if (null != status) {
			cri.where().andEquals("status",status);
		}
		cri.getOrderBy().asc("id");
		List<SysCity> sysCityList =    super.query(SysCity.class, cri);
		SysCity sysCity = new SysCity();
		sysCity.setId("");
		sysCity.setName(" ");
		sysCity.setParentid("-1");
		sysCityList.add(0,sysCity);
		return sysCityList;
	}


	private SysCity getFixedTopRes() {
		SysCity resource = new SysCity();
		resource.setId(SysCity.CHINA_ID);
		resource.setName("中国");
		return resource;
	}

	public boolean save(SysCity city) {
		SysCity sc = super.fetch(SysCity.class, city.getId());
		if (null == sc) {
			return false;
		}
		sc.setStatus(city.getStatus());
		sc.setBase(city.getBase());
		sc.setCold(city.getCold());
		super.update(sc);
		return true;

	}

	/**
	 * 查询行政单位
	 * @return
	 */
	public SysCity fetchCity(String id) {

		Criteria cri = Cnd.cri();
		cri.where().andEquals("id",id);
		cri.where().andEquals("status", 1);
        SysCity fetch = super.fetch(SysCity.class, cri);
        return fetch;
    }


	/**
	 * 一次性全部查询出机构树
	 * @return
	 */
	public List<SysCity> querySysCityTree() {

		Criteria cri = Cnd.cri();
		cri.where().andEquals("status",1);
		List<SysCity> organizations = super.query(SysCity.class, cri);

		Map<String,List<SysCity>> p2sons = new HashMap<>();
		for(SysCity organization:organizations) {
			if(p2sons.containsKey(organization.getParentid())) {
				p2sons.get(organization.getParentid()).add(organization);
			}else {
				List<SysCity> list = new ArrayList<>();
				list.add(organization);
				p2sons.put(organization.getParentid(), list);
			}
		}
		SysCity fixedTopRes = this.getFixedTopRess();

		List<SysCity> topRess = p2sons.get("-1");
		if(topRess!=null&&topRess.size()>0){
			for (SysCity topRes : topRess) {
				topRes.setList(p2sons.get(topRes.getId()));
			}
		}
		fixedTopRes.setList(topRess);
		List<SysCity> result = new ArrayList<>();
		result.add(fixedTopRes);
		return result;
	}

	private SysCity getFixedTopRess() {
		SysCity resource = new SysCity();
		resource.setId("-1");
		resource.setName("行政区域树");
		return resource;
	}

	/**
	 * 获取某个区域的全名，自动拼接上上级区域名称
	 * @return
	 */
	public String getRegionString(String regionId) {

		if(regionId == null) {
			   return "";
		}
		SysCity region = this.fetch(SysCity.class,regionId);
		 if(region != null) {
		   return getRegionString(region.getParentid()) + region.getName();  //  递归调用方法getRegionString(Long regionId)，停止条件设为regionId==null为真
		 }
		 return "";
	}

}
