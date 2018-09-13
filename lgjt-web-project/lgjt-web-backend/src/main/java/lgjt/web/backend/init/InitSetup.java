package lgjt.web.backend.init;

import com.google.common.collect.Maps;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.util.RedisUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.nutz.lang.Encoding;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;
import lgjt.common.base.utils.IocUtils;
import lgjt.common.base.utils.RedisKeys;
import lgjt.common.base.vo.SysCityVo;
import lgjt.domain.backend.city.SysCity;
import lgjt.domain.backend.dict.SysDict;
import lgjt.domain.backend.industry.SysIndustry;
import lgjt.services.backend.city.SysCityService;
import lgjt.services.backend.dict.SysDictService;
import lgjt.services.backend.industry.SysIndustryService;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 项目启动类
 * @author daijiaqi
 */
@Log4j
public class InitSetup implements Setup {

	private AtomicBoolean started = new AtomicBoolean(false);

	public void init(NutConfig nc) {
		
		   if (!Charset.defaultCharset().name().equalsIgnoreCase(Encoding.UTF8)) {
               log.warn("This project must run in UTF-8, pls add -Dfile.encoding=UTF-8 to JAVA_OPTS");
           }
		    String url =  StringUtil.trim(PropertyUtil.getProperty("redis-url"));
			String passwd = StringUtil.trim(PropertyUtil.getProperty("redis-pwd"));
			try {
				RedisUtil.getInstance().init(url, passwd,RedisUtil.TYPE_SINGLE);
			} catch (Exception e) {
				e.printStackTrace();
			}

		/***
		 * 加载相应的数据到缓存
		 */
		try {
			loadDictToRedis();

			loadCityToRedis();

			loadIndustryToRedis();

		} catch (Exception e ) {

		}
//		String uniquePrefix="tree";
//		String key="id";
//		String superId="superId";
//		new TreeCacheImpl("tree","id","superId");
		TreeCacheImpl.getInstance();

	}



	@Override
	public void destroy(NutConfig nc) {
		
	}


	private void loadDictToRedis() {

		SysDictService sysDictService = IocUtils.getBean(SysDictService.class);

        SysDictRedis sysDictRedis = SysDictRedis.getInstance();

		sysDictRedis.delete(RedisKeys.DICT_EDUCATION_KEY);
		sysDictRedis.delete(RedisKeys.DICT_NATION_KEY);
		sysDictRedis.delete(RedisKeys.DICT_REGISTERED_KEY);
		sysDictRedis.delete(RedisKeys.DICT_WORKSTATUS_KEY);
		sysDictRedis.delete(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY);
		sysDictRedis.delete(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY);
		sysDictRedis.delete(RedisKeys.DICT_ECONOMICS_KEY);
		sysDictRedis.delete(RedisKeys.SEX_KEY);
		sysDictRedis.delete(RedisKeys.CERTIFICATE_KEY);
		sysDictRedis.delete(RedisKeys.DICT_TECHNICAL_LEVEL_KEY);
		sysDictRedis.delete(RedisKeys.DICT_PROPERTY_KEY);

        if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_EDUCATION_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_EDUCATION_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_EDUCATION_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_EDUCATION_KEY,map);
			//为用户导入服务
            //---------------开始--------------------
			Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map1.put(sysDict.getCodeName(),sysDict.getDictCode());
			}
			sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_EDUCATION_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_NATION_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_NATION_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_NATION_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
        	sysDictRedis.saveOrUpdate(RedisKeys.DICT_NATION_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_NATION_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_REGISTERED_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_REGISTERED_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_REGISTERED_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_REGISTERED_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_REGISTERED_KEY,map1);
            //---------------结束--------------------
		}
		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_WORKSTATUS_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_WORKSTATUS_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_WORKSTATUS_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_WORKSTATUS_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_WORKSTATUS_KEY,map1);
            //---------------结束--------------------
		}
		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_TYPE_KEY,map1);
            //---------------结束--------------------
		}
		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_MEMBERSHIP_CHANGE_REASON_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_ECONOMICS_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_ECONOMICS_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_ECONOMICS_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_ECONOMICS_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_ECONOMICS_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.SEX_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.SEX_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.SEX_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
				System.out.println(sysDict.getDictCode()+"------------------"+sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.SEX_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.SEX_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.CERTIFICATE_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.CERTIFICATE_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.CERTIFICATE_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.CERTIFICATE_KEY,map);
			//为用户导入服务
			//---------------开始--------------------
			Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map1.put(sysDict.getCodeName(),sysDict.getDictCode());
			}
			sysDictRedis.saveOrUpdate4User(RedisKeys.CERTIFICATE_KEY,map1);
			//---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_TECHNICAL_LEVEL_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_TECHNICAL_LEVEL_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_TECHNICAL_LEVEL_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_TECHNICAL_LEVEL_KEY,map);
            //为用户导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_TECHNICAL_LEVEL_KEY,map1);
            //---------------结束--------------------
		}

		if (MapUtils.isEmpty(sysDictRedis.get(RedisKeys.DICT_PROPERTY_KEY)) && MapUtils.isEmpty(sysDictRedis.get4User(RedisKeys.DICT_PROPERTY_KEY))) {
			List<SysDict> sysDictList = sysDictService.getDictBycode(RedisKeys.DICT_PROPERTY_KEY);
			Map<String,String> map = Maps.newHashMapWithExpectedSize(sysDictList.size());
			for (SysDict sysDict : sysDictList) {
				map.put(sysDict.getDictCode(),sysDict.getCodeName());
			}
			sysDictRedis.saveOrUpdate(RedisKeys.DICT_PROPERTY_KEY,map);
            //为企业导入服务
            //---------------开始--------------------
            Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysDictList.size());
            for (SysDict sysDict : sysDictList) {
                map1.put(sysDict.getCodeName(),sysDict.getDictCode());
            }
            sysDictRedis.saveOrUpdate4User(RedisKeys.DICT_PROPERTY_KEY,map1);
            //---------------结束--------------------
		}

	}


	private void loadCityToRedis() {

		SysCityService sysCityService = IocUtils.getBean(SysCityService.class);

		SysCityRedis sysCityRedis = SysCityRedis.getInstance();

		if (CollectionUtils.isEmpty(sysCityRedis.get(RedisKeys.PROVINCE_KEY))) {

			List<SysCity> provinceList = sysCityService.listSons(null,1);
			List<SysCityVo> destList = new ArrayList<>();
			try {

			for ( SysCity sysCity : provinceList)  {
				SysCityVo sysCityVo = new SysCityVo();

					BeanUtils.copyProperties(sysCityVo,sysCity);
					destList.add(sysCityVo);
			}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			sysCityRedis.saveOrUpdate(RedisKeys.PROVINCE_KEY,destList);
			//获取每个省下的市
			for (SysCity province : provinceList) {
				List<SysCity> cityList = sysCityService.listSons(province.getId(),1);

				List<SysCityVo> cList = new ArrayList<>();
				try {

					for ( SysCity sysCity : cityList)  {
						SysCityVo sysCityVo = new SysCityVo();

						BeanUtils.copyProperties(sysCityVo,sysCity);
						cList.add(sysCityVo);
					}

				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				//每个省的id为key（34个），对应下属的各个市
				sysCityRedis.saveOrUpdate(RedisKeys.PROVINCE2CITY_KEY+province.getId(),cList);

				for (SysCity country : cityList) {

					List<SysCity> countryList = sysCityService.listSons(country.getId(),1);

					List<SysCityVo> ccList = new ArrayList<>();
					try {

						for ( SysCity sysCity : countryList)  {
							SysCityVo sysCityVo = new SysCityVo();

							BeanUtils.copyProperties(sysCityVo,sysCity);
							ccList.add(sysCityVo);
						}

					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					//各个市的id为key，对应最终的区
					sysCityRedis.saveOrUpdate(RedisKeys.CITY2COUNTRY_KEY+country.getId(),ccList);

				}
			}

		}

	}

	private void loadIndustryToRedis() {

		SysIndustryService sysIndustryService = IocUtils.getBean(SysIndustryService.class);

		SysIndustryRedis sysIndustryRedis = SysIndustryRedis.getInstance();

		SysIndustry sysIndustry = new SysIndustry();
		sysIndustry.setLevel(1);

		List<SysIndustry> oneSysIndustry =  sysIndustryService.query(sysIndustry);
        //为企业导入服务
        //---------------开始--------------------
        SysIndustry sysIndustry1 = new SysIndustry();
		List<SysIndustry> sysIndustrys =  sysIndustryService.query(sysIndustry1);
        Map<String,String> map1 = Maps.newHashMapWithExpectedSize(sysIndustrys.size());
        for (SysIndustry industry : sysIndustrys) {
            map1.put(industry.getName(),industry.getId());
        }
        sysIndustryRedis.saveOrUpdate4Org(RedisKeys.INDUSTRY_KEY,map1);
        //---------------结束--------------------

		sysIndustryRedis.saveOrUpdate(RedisKeys.ONE_INDUSTRY_KEY,oneSysIndustry);

		for (SysIndustry one : oneSysIndustry)  {
			SysIndustry oneIndustry = new SysIndustry();
			oneIndustry.setSuperId(one.getId());

			List<SysIndustry> twoSysIndustry =  sysIndustryService.query(oneIndustry);
			sysIndustryRedis.saveOrUpdate(RedisKeys.TWO_INDUSTRY_KEY+one.getId(),twoSysIndustry);


			for (SysIndustry two : twoSysIndustry)  {
				SysIndustry twoIndustry = new SysIndustry();
				twoIndustry.setSuperId(two.getId());

				List<SysIndustry> threeSysIndustry =  sysIndustryService.query(twoIndustry);
				sysIndustryRedis.saveOrUpdate(RedisKeys.THREE_INDUSTRY_KEY+one.getId(),threeSysIndustry);

				for (SysIndustry three : threeSysIndustry)  {
					SysIndustry threeIndustry = new SysIndustry();
					threeIndustry.setSuperId(three.getId());

					List<SysIndustry> fourSysIndustry =  sysIndustryService.query(threeIndustry);
					sysIndustryRedis.saveOrUpdate(RedisKeys.FOUR_INDUSTRY_KEY+one.getId(),fourSysIndustry);

				}

			}

		}


	}
}
