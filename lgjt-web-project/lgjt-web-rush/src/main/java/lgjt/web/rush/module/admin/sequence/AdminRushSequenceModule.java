package lgjt.web.rush.module.admin.sequence;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.DELETE;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.nutz.result.Results;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lgjt.domain.rush.sequence.RushSequence;
import lgjt.services.rush.sequence.RushSequenceService;

import javax.annotation.PostConstruct;
import javax.xml.transform.Result;
import java.util.Date;


@At("/admin/rushSequence")
@IocBean
@Log4j
public class AdminRushSequenceModule {
    
	@Inject("rushSequenceService")
	RushSequenceService service;

	@At("/?")
	@POST
	public Object get(String id) {
		RushSequence obj = service.get(id);
		if(null != obj) {
			return Results.parse(Constants.STATE_SUCCESS, null,obj);
		}else {
			return Results.parse(Constants.STATE_FAIL,"数据不存在");
		}
	}
	
	@At("/delete")
	public Object delete(String ids) {
		int ok = service.delete(ids);
		if(ok > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
	}
	
	@At("/insert")
	@POST
	public Object insert(@Param("..") RushSequence obj) {
		obj.setScoreFlag(1);
		obj.setExtend1("0");//默认未发布
		obj.setCrtTime(new Date());
		RushSequence o = service.insert(obj);
		if(o!=null) {
			return Results.parse(Constants.STATE_SUCCESS,null,o);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}
	@At("/update")
	@POST
	public Object update(@Param("..") RushSequence obj) {


		if (StringUtils.isNotBlank(obj.getImageId()))  {

			RushSequence rushSequence = service.checkId(obj.getId());
			if ( rushSequence != null ) {
				if ( StringUtils.isNotBlank(rushSequence.getImageId())) {
					JSONObject jsonObject = JSON.parseObject(rushSequence.getImageId());
					JSONObject object= (JSONObject)jsonObject.getJSONArray("data").get(0);

					JSONObject jsonObject2 = JSON.parseObject(obj.getImageId());
					JSONObject object2= (JSONObject)jsonObject2.getJSONArray("data").get(0);
					try {
					String id = ResourceEncryptionUtil.base64Decode(object2.get("id").toString());
					if (id.contains(object.get("id").toString())) {

						obj.setImageId(null);
					}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			}
		int upd = service.updateIgnoreNull(obj);
		if(upd>0) {
			return Results.parse(Constants.STATE_SUCCESS);
		}else {
			return Results.parse(Constants.STATE_FAIL);
		}
	}

	@At("/multipleUpdate")
    @POST
    public Object multipleUpdate(@Param("..") RushSequence obj, @Param("ids") String ids) {
/*        if (StringUtils.isNotBlank(obj.getImageId()))  {

            JSONObject jsonObject = JSON.parseObject(obj.getImageId());
            JSONObject object= (JSONObject)jsonObject.getJSONArray("data").get(0);

            try {
                object.replace("id",ResourceEncryptionUtil.base64Encoder((String)object.get("id")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            obj.setImageId(jsonObject.toJSONString());

        }*/
        int success = service.multipleUpdate(ids, obj);
        if(success > 0) {
            return Results.parse(Constants.STATE_SUCCESS);
        } else {
            return Results.parse(Constants.STATE_FAIL);
        }
    }

	@At("/")
	@POST
	public Object queryPage(@Param("..") RushSequence obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPage(obj));
	}

	/**
	 * 2018-07-24 赵天意
	 * 通过模糊搜索查询工种
	 * @param obj
	 * @return
	 */
	@At("/queryPageByName")
	@POST
	public Object queryPageByName(@Param("..") RushSequence obj) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryPageByName(obj));
	}

	/**
	 * 通过ID查询工种
	 * @param id
	 * @return
	 */
	@At("/queryById")
	@POST
	public Object queryById(@Param("id") String id) {
		return Results.parse(Constants.STATE_SUCCESS,null,service.queryById(id));
	}

	@At("/checkId")
	@POST
	public Object checkId(String value) {
		return service.checkId(value);
	}

	@At("/release")
	public Object release(@Param("ids") String ids) {
		return Results.parse(Constants.STATE_SUCCESS,"SUCCESS",service.release(ids));
	}

	@At("/deRelease")
	public Object deRelease(@Param("ids") String ids) {
		return Results.parse(Constants.STATE_SUCCESS,"SUCCESS",service.deRelease(ids));
	}
}