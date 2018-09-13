package lgjt.web.help.module.UploadModule;

import com.ttsx.platform.nutz.common.Constants;
import com.ttsx.platform.tool.util.PropertyUtil;
import com.ttsx.util.cache.domain.UserLoginInfo;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lgjt.domain.resource.TtsxFile;
import lgjt.services.resource.ResourceMongodbHandler;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.upload.FieldMeta;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author daijiaqi
 * @date 2018/7/610:54
 */
@At("/helpupload")
@IocBean
@Log4j
public class UploadFile {

    private static final String FTP_HOST = PropertyUtil.getProperty("ftphost");
    private static final Integer FTP_PROT = Integer.valueOf(PropertyUtil.getProperty("ftpprot"));
    private static final String FTP_USERNAME =PropertyUtil.getProperty("ftpusername");
    private static final String FTP_PASSWORD = PropertyUtil.getProperty("ftppassword");
    private static final String FTP_BASEPATH=PropertyUtil.getProperty("ftpbasepath");
    private static final String FTP_FILEPATH=PropertyUtil.getProperty("tpfilepath");

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
    @At("/helpuploadFile")
    @AdaptBy(type = UploadAdaptor.class, args = {"ioc:myUpload"})
    public Object uploadFile(@Param("..") TtsxFile ttsxFile, @Param("file") TempFile[] tempFiles) {
        try {
            StringBuffer sb = new StringBuffer();
            UserLoginInfo userLoginInfo = null;

           List<Map<String,String>> result =new ArrayList<Map<String,String>>();
            for (TempFile tempFile : tempFiles) {

                //返回数据
                Map<String,String> resultMap=new HashMap<>();


                FieldMeta meta = tempFile.getMeta();
                String fileName = meta.getFileLocalName();
                Date ss = new Date();
                SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd");
                // 上传到对方服务器路径
                // 文件存储的路径
                String filePath = FTP_FILEPATH+format0.format(ss.getTime());
                // 文件名称
                String fileName2 = System.currentTimeMillis()+fileName;
                boolean flag = FtpUtil.uploadFile(FTP_HOST, FTP_PROT, FTP_USERNAME, FTP_PASSWORD, FTP_BASEPATH, filePath, fileName2,tempFile.getInputStream());
                if(flag)
                    System.out.println("==============================附件上传ftp服务器------成功=================================");
               else
                    System.out.println("==============================附件上传ftp服务器------失败=================================");
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
                resultMap.put("id",id);
                resultMap.put("fileName",fileName);
                resultMap.put("fileName2",fileName2);
                resultMap.put("filePath",filePath);

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
            List<Map<String,String>> result =new ArrayList<Map<String,String>>();
            for (TempFile tempFile : tempFiles) {
                Map<String,String> resultMap=new HashMap<>();
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
                resultMap.put("id",id);
                resultMap.put("fileName",fileName);
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
