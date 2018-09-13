package lgjt.web.api.module.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.GET;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.platform.tool.util.StringUtil;
import com.ttsx.util.cache.CacheFactory;
import com.ttsx.util.cache.IObjectCache;

import lombok.extern.log4j.Log4j;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.ParameterVerificationUtils;
import lgjt.domain.api.systask.secretkey.SysSecretKey;
import lgjt.services.api.secretkey.SysSecretKeyService;
import lgjt.services.mongodb.company.MongodbService;
import lgjt.web.api.config.ApiConfig;
import lgjt.web.api.module.base.ApiBaseModule;

/**
   * 文件上传接口类
   *@author daijiaqi
   *@date 2018/5/6 23:57
   */
@At("/upload")
@IocBean
@Log4j
public class UploadModule extends ApiBaseModule {

	@Inject
	private SysSecretKeyService sysSecretKeyService;

	/**
	 * 
	 * 文件上传
	 * @author daijiaqi
	 * @date 2018年4月26日
	 * @param appId
	 *            对接系统ID
	 * @param timestamp
	 *            时间戳，格式为yyyyMMddHHmmss。例如：20180101142513
	 * @param apiVersion
	 *            API接口版本，当前固定为1.0
	 * @param authToken
	 *            用户在对接系统中的唯一ID
	 * @param paramSign
	 *            参数签名
	 * @param fileAbs
	 *            上传文件的MD5摘要
	 * @param fileContent
	 *            文件内容（不参加签名计算）
	 * @return
	 */
	@POST
	@GET
	@At("/uploadFile")
	@AdaptBy(type = UploadAdaptor.class, args = { "ioc:myUpload" })
	public Object uploadFile(@Param("app_id") String appId, @Param("timestamp") String timestamp,
			@Param("api_version") String apiVersion, @Param("auth_token") String authToken,
			@Param("param_sign") String paramSign, @Param("file_abs") String fileAbs,
			@Param("file_content") TempFile fileContent) {
		try {
//			// 判断共有参数
//			ReturnCode returnCode = ParameterVerificationUtils.checkStandardParameters(appId, timestamp, apiVersion,
//					authToken, paramSign);
//			if (!returnCode.getCode().equals(ReturnCode.CODE_100000.getCode())) {
//				return ResultsImpl.parse(returnCode.getCode(), returnCode.getValue());
//			}
//			// 判断 appId是否存在
//			SysSecretKey sysSecretKey = sysSecretKeyService.get(appId);
//			if (sysSecretKey == null) {
//				return ResultsImpl.parse(ReturnCode.CODE_101003.getCode(), ReturnCode.CODE_101003.getValue());
//			}
//			String communicationKey = ParameterVerificationUtils.getCommunicationKey(ApiConfig.REDIS_PREFIX_COMMUNICATIONKEY,appId, timestamp);
//			if (communicationKey.length() == 0) {
//				return ResultsImpl.parse(ReturnCode.CODE_101010.getCode(), ReturnCode.CODE_101010.getValue());
//			}
//			// 判断签名是否正确
//			String paramSignLocal = ParameterVerificationUtils
//					.md5(ParameterVerificationUtils.parametersSort("app_id=" + appId, "timestamp=" + timestamp,
//							"auth_token=" + authToken, "api_version=" + apiVersion, "file_abs=" + fileAbs) + "|key="
//							+ communicationKey);
//
//
//			// 正常的处理逻辑 key = REDIS_PREFIX+appId+day;
//			if (!paramSignLocal.equalsIgnoreCase(paramSign)) {
//				return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
//			}
			List<String> params =new ArrayList<String>();
			params.add("file_abs="+fileAbs);
			ReturnCode code =  super.parametersCheck(appId,timestamp,apiVersion,authToken,paramSign,params);
			if(!code.codeEquals(ReturnCode.CODE_100000)){
				return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
			}

				// 文件上传MONGODB
			String fId = MongodbService.getInstance("1").upload(fileContent.getInputStream());
			Map<String, String> result = new HashMap<String, String>();
			result.put("id", fId);
			return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue(), result);
		} catch (IOException e) {
			log.error("UploadModule.uploadFile(appId=" + appId + ",timestamp=" + timestamp + ",apiVersion=" + apiVersion
					+ ",authToken=" + authToken + ",paramSign=" + paramSign + ",fileAbs=" + fileAbs + ").", e);
			return ResultsImpl.parse(ReturnCode.CODE_103002.getCode(), ReturnCode.CODE_103002.getValue());
		}
	}

	/**
	 * 用户信息上传
	 * 
	 * @author daijiaqi
	 * @date 2018年4月27日
	 * @param appId
	 *            对接系统ID
	 * @param timestamp
	 *            时间戳，格式为yyyyMMddHHmmss。例如：20180101142513
	 * @param apiVersion
	 *            API接口版本，当前固定为1.0
	 * @param authToken
	 *            对接系统ID
	 * @param paramSign
	 *            参数签名
	 * @param fileId
	 *            用户信息上送摘要文件的文件ID
	 * @return
	 */
	@POST
	@GET
	@At("/sendUserInfo")
	public Object sendUserInfo(@Param("app_id") String appId, @Param("timestamp") String timestamp,
			@Param("api_version") String apiVersion, @Param("auth_token") String authToken,
			@Param("param_sign") String paramSign, @Param("file_id") String fileId) {
		BufferedReader br = null;
		try {
			// 判断共有参数
			ReturnCode returnCode = ParameterVerificationUtils.checkStandardParameters(appId, timestamp, apiVersion,
					authToken, paramSign);
			if (!returnCode.getCode().equals(ReturnCode.CODE_100000.getCode())) {
				return ResultsImpl.parse(returnCode.getCode(), returnCode.getValue());
			}
			// 判断 appId是否存在
			SysSecretKey sysSecretKey = sysSecretKeyService.get(appId);
			if (sysSecretKey == null) {
				return ResultsImpl.parse(ReturnCode.CODE_101003.getCode(), ReturnCode.CODE_101003.getValue());
			}

			// 正常的处理逻辑 key = REDIS_PREFIX+appId+day;
			String communicationKey = ParameterVerificationUtils.getCommunicationKey(ApiConfig.REDIS_PREFIX_COMMUNICATIONKEY,appId, timestamp);
			if (communicationKey.length() == 0) {
				return ResultsImpl.parse(ReturnCode.CODE_101010.getCode(), ReturnCode.CODE_101010.getValue());
			}

			// 判断签名是否正确
			String paramSignLocal = ParameterVerificationUtils
					.md5(ParameterVerificationUtils.parametersSort("app_id=" + appId, "timestamp=" + timestamp,
							"auth_token=" + authToken, "api_version=" + apiVersion, "file_id=" + fileId) + "|key="
							+ communicationKey);
			if (!paramSignLocal.equalsIgnoreCase(paramSign)) {
				return ResultsImpl.parse(ReturnCode.CODE_101009.getCode(), ReturnCode.CODE_101009.getValue());
			}

			// 文件上传MONGODB
			// 获取摘要信息
			br = new BufferedReader(new InputStreamReader(MongodbService.getInstance("1").readById(fileId),"utf-8"));
			StringBuffer zysb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				zysb.append(line);
			}
			// 解析json
			JSONObject zyObj = JSONObject.parseObject(zysb.toString());
			JSONArray filesArray = zyObj.getJSONArray("files");
			if (filesArray == null || filesArray.size() == 0) {
				return ResultsImpl.parse(ReturnCode.CODE_103003.getCode(), ReturnCode.CODE_103003.getValue());
			}

			// 处理其所对应的文件
			for (int i = 0; i < filesArray.size(); i++) {
				String userFileId = filesArray.getJSONObject(i).getString("id");// 处理单个用户信息文件
				try {
					br = new BufferedReader(new InputStreamReader(
							MongodbService.getInstance("1").readById(filesArray.getJSONObject(i).getString("id")),"utf-8"));
					String userInfoLine = "";
					DBCollection dbCollection = MongodbService.getInstance("1").getDBCollectionByName(appId);
					while ((userInfoLine = br.readLine()) != null) {
						DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(userInfoLine);
						DBObject query = new BasicDBObject("id", dbObject.get("id"));
						dbCollection.remove(query);
						dbCollection.insert(dbObject);
					}
				} catch (Exception e) {
					log.error("userFileId=" + userFileId, e);
				} finally {
					if (br != null) {
						br.close();
						br = null;
					}
				}
			}
			return ResultsImpl.parse(ReturnCode.CODE_100000.getCode(), ReturnCode.CODE_100000.getValue());
		} catch (IOException e) {
			log.error("UploadModule.uploadFile(appId=" + appId + ",timestamp=" + timestamp + ",apiVersion=" + apiVersion
					+ ",authToken=" + authToken + ",paramSign=" + paramSign + ",fileId=" + fileId + ").", e);
			return ResultsImpl.parse(ReturnCode.CODE_103002.getCode(), ReturnCode.CODE_103002.getValue());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {

				}
				br = null;
			}
		}
	}

}