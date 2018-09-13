package lgjt.web.resource.module.UploadModule;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.FieldMeta;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.utils.LoginUtil;
import lgjt.domain.resource.TtsxFile;
import lgjt.services.resource.ResourceMongodbHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author daijiaqi
 * @date 2018/7/610:54
 */
@At("/upload")
@IocBean
@Log4j
public class UploadModule {



    /**
     * 文件上传
     *
     * @param ttsxFile  文件信息，对应lgjt.domain.mongodb中实体类TtsxFiles
     * @param tempFiles 文件流，对应org.nutz.mvc.upload中实体类TempFile[]
     * @return 成功/失败信息+文件信息
     * @author daijiaqi
     * @date 2018/5/6 23:57
     * @author daijiaqi
     * @date 2018年4月26日
     */
    @POST
    @At("/uploadFile")
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:myUpload"})
    public Object uploadFile(@Param("..") TtsxFile ttsxFile, @Param("file") TempFile[] tempFiles) {
        try {
            StringBuffer sb = new StringBuffer();
            UserLoginInfo userLoginInfo = null;

            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            for (TempFile tempFile : tempFiles) {
                Map<String, String> resultMap = new HashMap<>();
                FieldMeta meta = tempFile.getMeta();
                String fileName = meta.getFileLocalName();
                String suffix = StringUtils.trim(meta.getFileExtension()).replaceFirst("\\.", "").toUpperCase();
                if (ttsxFile == null) {
                    ttsxFile = new TtsxFile();
                    ttsxFile.setSort(0l);
                }
                ttsxFile.setContentType(meta.getContentType());
                ttsxFile.setFullFileName(fileName);
                ttsxFile.setDeleteFlag(ttsxFile.DELETEFLAG_UNDELETED);
                ttsxFile.setDisplayName(fileName);
                ttsxFile.setFileName(fileName);
                ttsxFile.setFileType(suffix);
                ttsxFile.setSuffix(suffix);
                ttsxFile.setLength(tempFile.getSize());
                if (userLoginInfo == null) {
                    ttsxFile.setCrtUser("anonymous");
                    ttsxFile.setCrtIp("127.0.0.1");
                } else {
                    ttsxFile.setCrtUser(userLoginInfo.getUserName());
                    ttsxFile.setCrtIp(userLoginInfo.getLoginIP());
                }
                String id = ResourceMongodbHandler.getInstance("resource").uploadAutoParse(tempFile.getInputStream(), ttsxFile);
                if (sb.length() > 0) {
                    sb.append(",");
                }
                //sb.append(id);
                // 加密
                id = ResourceEncryptionUtil.base64Encoder(id);
                resultMap.put("id", id);
                resultMap.put("fileName", fileName);
                result.add(resultMap);
            }
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", result);
            return ResultsImpl.parse(Constants.STATE_SUCCESS, ReturnCode.CODE_100000.getValue(), data);
        } catch (Exception e) {
            log.error("UploadModule.uploadFile", e);
        }
        return ResultsImpl.parse(Constants.STATE_FAIL, ReturnCode.CODE_103002.getValue());
    }


    /**
     * 文件上传
     *
     * @param ttsxFile  文件信息，对应lgjt.domain.mongodb中实体类TtsxFiles
     * @param tempFiles 文件流，对应org.nutz.mvc.upload中实体类TempFile[]
     * @return 成功/失败信息+文件信息
     * @author daijiaqi
     * @date 2018/5/6 23:57
     * @author daijiaqi
     * @date 2018年4月26日
     */
    @POST
    @At("/uploadFile")
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:myUpload"})
    public Object uploadFileBACK(@Param("..") TtsxFile ttsxFile, @Param("file") TempFile[] tempFiles) {
        try {
//            StringBuffer sb = new StringBuffer();
            UserLoginInfo userLoginInfo = null;
            List<Map<String, String>> result = new ArrayList<Map<String, String>>();
            for (TempFile tempFile : tempFiles) {
                Map<String, String> resultMap = new HashMap<>();
                FieldMeta meta = tempFile.getMeta();
                String fileName = meta.getFileLocalName();
                String suffix = StringUtils.trim(meta.getFileExtension()).replaceFirst("\\.", "").toUpperCase();
                if (ttsxFile == null) {
                    ttsxFile = new TtsxFile();
                    ttsxFile.setSort(0l);
                }
                ttsxFile.setContentType(meta.getContentType());
                ttsxFile.setFullFileName(fileName);
                ttsxFile.setDeleteFlag(ttsxFile.DELETEFLAG_UNDELETED);
                ttsxFile.setDisplayName(fileName);
                ttsxFile.setFileName(fileName);
                ttsxFile.setFileType(suffix);
                ttsxFile.setSuffix(suffix);
                ttsxFile.setLength(tempFile.getSize());
                if (userLoginInfo == null) {
                    ttsxFile.setCrtUser("anonymous");
                    ttsxFile.setCrtIp("127.0.0.1");
                } else {
                    ttsxFile.setCrtUser(userLoginInfo.getUserName());
                    ttsxFile.setCrtIp(userLoginInfo.getLoginIP());
                }
                String id = ResourceMongodbHandler.getInstance("resource").uploadAutoParse(tempFile.getInputStream(), ttsxFile);
//                if (sb.length() > 0) {
//                    sb.append(",");
//                }
//                sb.append(id);
                id = ResourceEncryptionUtil.base64Encoder(id);
                resultMap.put("id", id);
                resultMap.put("fileName", fileName);
                resultMap.put("type",suffix);
                result.add(resultMap);
            }
            Map<String, Object> data = new HashMap<String, Object>();

            data.put("data", result);
            return ResultsImpl.parse(Constants.STATE_SUCCESS, ReturnCode.CODE_100000.getValue(), data);
        } catch (Exception e) {
            log.error("UploadModule.uploadFile", e);
        }
        return ResultsImpl.parse(Constants.STATE_FAIL, ReturnCode.CODE_103002.getValue());
    }
}
