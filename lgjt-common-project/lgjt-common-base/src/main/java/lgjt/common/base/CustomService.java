package lgjt.common.base;

import java.util.Date;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.SimpleCriteria;

import com.ttsx.platform.nutz.pojo.CaseEntity;
import com.ttsx.platform.nutz.service.BaseService;
import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;

/**
 * 
 * 业务逻辑层基类
 * 
 * @Description: 其它业务需要继承这个类
 * @author daijiaqi
 * @CreateDate: 2016-12-7 下午2:01:47
 * 
 * @UpdateUser: daijiaqi
 * @UpdateDate: 2016-12-7 下午2:01:47
 * @UpdateRemark: 说明本次修改内容
 */
public class CustomService extends BaseService {

	/**
	 * 当调用插入方法时，以下字段自动赋值 crt_user 创建人 crt_time 创建时间 crt_ip 创建人IP
	 * 
	 * @param obj
	 *            添加的对象
	 * @return 返回当前添加成功的对象
	 * @author daijiaqi
	 * @date 2016-12-7 下午2:02:55
	 */
	public <T extends CaseEntity> T insert(T obj) {
		if (StringTool.isNull(obj.getCrtUser())) {
			obj.setCrtUser("system");
		}
		if (StringTool.isNull(obj.getCrtTime())) {
			obj.setCrtTime(new Date());
		}
		if (StringTool.isNull(obj.getCrtIp())) {
			obj.setCrtIp("127.0.0.1");
		}
		return super.insert(obj);
	}

	/**
	 * 当调用修改方法时，该方法修改实体类中所有字段 以下字段自动赋值： upd_user 修改人 upd_time 修改时间 upd_ip 修改人IP
	 * 
	 * @param obj
	 *            需修改的对象
	 * @return 影响的行数
	 * @author daijiaqi
	 * @date 2016-12-7 下午2:07:13
	 */
	public <T extends CaseEntity> int update(T obj) {
		if (StringTool.isNull(obj.getUpdUser())) {
			obj.setUpdUser("system");
		}
		if (StringTool.isNull(obj.getUpdTime())) {
			obj.setUpdTime(new Date());
		}
		if (StringTool.isNull(obj.getUpdIp())) {
			obj.setUpdIp("127.0.0.1");
		}
		return super.update(obj);
	}

	/**
	 * 当调用修改方法时，该方法只会修改参数中不为空的字段 以下字段自动赋值： upd_user 修改人 upd_time 修改时间 upd_ip
	 * 修改人IP
	 * 
	 * @param obj
	 *            需修改的对象
	 * @return 影响的行数
	 * @author daijiaqi
	 * @date 2016-12-7 下午2:07:13
	 */
	public <T extends CaseEntity> int updateIgnoreNull(T obj) {
		if (StringTool.isNull(obj.getUpdUser())) {
			obj.setUpdUser("system");
		}
		if (StringTool.isNull(obj.getUpdTime())) {
			obj.setUpdTime(new Date());
		}
		if (StringTool.isNull(obj.getUpdIp())) {
			obj.setUpdIp("127.0.0.1");
		}
		return super.updateIgnoreNull(obj);
	}

	/**
	 * 批量快速插入
	 * 
	 * @param objs
	 *            需要插入的集合
	 * @return 返回该批集合
	 * @author daijiaqi
	 * @date 2016-12-20 上午9:51:51
	 */
	public <T extends CaseEntity> List<T> fastInsert(List<T> objs) throws Exception{
		List<T> result = null;
		if (objs != null && objs.size() > 0) {
			for (int i = 0; i < objs.size(); i++) {
				objs.get(i).setId(UUIDUtil.getUUID());
				if (StringTool.isNull(objs.get(i).getCrtUser())) {
					objs.get(i).setCrtUser("system");
				}
				if (StringTool.isNull(objs.get(i).getCrtTime())) {
					objs.get(i).setCrtTime(new Date());
				}
				if (StringTool.isNull(objs.get(i).getCrtIp())) {
					objs.get(i).setCrtIp("127.0.0.1");
				}
			}
			result = super.dao.fastInsert(objs);
		}
		return result;
	}

	/**
	 * 共有SQL条件 以下字段自动赋值： crt_*(3) 创建相关 upd_*(3) 修改相关
	 * 
	 * @param obj
	 *            实体对象
	 * @author daijiaqi
	 * @date 2016-12-7 下午2:07:13
	 */
	public <T extends CaseEntity> SimpleCriteria getCommonCondition(T obj) {
		SimpleCriteria cri = Cnd.cri();
		if (obj != null) {
			if (StringTool.isNotNull(obj.getCrtUser())) {
				cri.where().andEquals("crt_user", obj.getCrtUser());
			}
			if (StringTool.isNotNull(obj.getCrtTime())) {
				cri.where().andEquals("crt_time", obj.getCrtTime());
			}
			if (StringTool.isNotNull(obj.getCrtIp())) {
				cri.where().andEquals("crt_ip", obj.getCrtIp());
			}
			if (StringTool.isNotNull(obj.getUpdUser())) {
				cri.where().andEquals("upd_user", obj.getUpdUser());
			}
			if (StringTool.isNotNull(obj.getUpdTime())) {
				cri.where().andEquals("upd_time", obj.getUpdTime());
			}
			if (StringTool.isNotNull(obj.getUpdIp())) {
				cri.where().andEquals("upd_ip", obj.getUpdIp());
			}
		}
		return cri;
	}
}
