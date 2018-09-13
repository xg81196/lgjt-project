package lgjt.services.resource;


import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;
import org.nutz.mvc.Mvcs;
import lgjt.domain.resource.TtsxFile;
import lgjt.services.resource.config.MongoDbConstants;
import lgjt.services.resource.config.MongodbConfig;
import lgjt.services.resource.pools.MongodbConnectionPools;
import lgjt.services.resource.util.FileTypeTools;
import lgjt.services.resource.util.StringTools;
import lgjt.services.resource.util.TtsxFileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author daijiaqi
 * @date 2018/7/517:15
 */
public class ResourceMongodbHandler {

    /**
     * 配置文件中的标记
     */
    private String index;

    /**
     * 配置文件中数据库名所对应的KEY；
     * mongodb-dbname-****
     */
    private String mongodbConfigKey;
    /**
     * 数据库连接
     */
    private DB db;
    /**
     * fs 对象
     */
    private GridFS gridFS;


    private static Map<String, ResourceMongodbHandler> resourceMongodbHandlers = new HashMap<>();

    /**
     * 构造方法
     *
     * @param index 配置文件中的标记
     */
    private ResourceMongodbHandler(String index) throws Exception {
        if (index != null) {
            this.index = index.trim();

//            this.mongodbConfigKey = MongodbConfig.CONFIG_KEY_MONGODB_DBNAME+"-"+ this.index;
            this.db = MongodbConnectionPools.getInstance().getDB(index, MongodbConfig.getValue(this.index, MongodbConfig.CONFIG_KEY_MONGODB_DBNAME));
            this.gridFS = new GridFS(this.db, MongodbConfig.getValue(this.index, MongodbConfig.CONFIG_KEY_MONGODB_DBNAME));
//            String chunkSizeConfig= StringTools.trim(MongodbConfig.getValue(MongodbConnectionPools.MONGODB_CHUNKSIZE+"-"+this.configFlag));
//            if(chunkSizeConfig.length()>0){
//                chunkSize=Integer.parseInt(chunkSizeConfig);
//            }
        }
    }

    /**
     * 获取操作业务类
     *
     * @param configFlag 配置文件中的标记
     * @return ResourceMongodbHandler 业务类
     */
    public static ResourceMongodbHandler getInstance(String configFlag) {
        try {
            if (resourceMongodbHandlers.get(configFlag) == null) {
                resourceMongodbHandlers.put(configFlag, new ResourceMongodbHandler(configFlag));
            }
            return resourceMongodbHandlers.get(configFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件上传 支持压缩包
     *
     * @param inputStream 文件输入流
     * @param ttsxFile    实体类
     * @return 文件ID
     * @throws IOException
     */
    public String uploadAutoParse(InputStream inputStream, TtsxFile ttsxFile) throws Exception {
        String fileTypeTtsxFile = ttsxFile == null ? null : ttsxFile.getFileType();
        String fileNameTtsxFile = ttsxFile == null ? null : ttsxFile.getFileName();
        String fileType = FileTypeTools.getTileType(inputStream, fileTypeTtsxFile, fileNameTtsxFile);
        ttsxFile.setFileType(fileType);
        String _id = upload(inputStream, ttsxFile);
        //判断配置文件中的
        if ((fileType.length() > 0 && fileType.equalsIgnoreCase("zip")) &&
                MongodbConfig.getValue(this.index, MongoDbConstants.CONFIG_ZIP_DECOMPRESSION).equals(MongoDbConstants.CONFIG_ZIP_DECOMPRESSION_YES)) {
            GridFSDBFile gridFSDBFile = this.gridFS.findOne(new BasicDBObject("_id", new ObjectId(_id.toString())));
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(gridFSDBFile.getInputStream()));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    String fileName = StringTools.trim(entryName).replaceAll("\\\\", "/");
                    ttsxFile.setFileType(fileName.substring(fileName.lastIndexOf(".")));
                    try {
                        fileName = fileName.substring(fileName.indexOf("/") + 1);
                    } catch (Exception e) {
                    }
                    if (ttsxFile.getFileType().equalsIgnoreCase(".m3u8")) {
                        fileName = "index.m3u8";
                    }
                    ttsxFile.setDisplayName(fileName);
                    ttsxFile.setFileName(fileName);
                    ttsxFile.setParentId(_id.toString());
                    ttsxFile.setFileFlag(TtsxFile.FILEFLAG_CHILD);
                    ttsxFile.setLength(entry.getSize());
                    ttsxFile.setFullFileName(entryName);
                    upload(zis, ttsxFile);
                }
            }
        }
        return _id;
    }

    /**
     * 文件上传
     * 一般上传
     *
     * @param inputStream 文件输入流
     * @param ttsxFile    文件实体类
     * @return 文件ID
     */
    public String upload(InputStream inputStream, TtsxFile ttsxFile) throws Exception {
        TtsxFileUtil.checkTtsxFileForUpload(ttsxFile);
        GridFSInputFile inputFile = this.gridFS.createFile(inputStream);
        if (ttsxFile != null) {
            ttsxFile.setMd5(inputFile.getMD5());
        }
        String filename = StringTools.trim(ttsxFile.getFileName());
        if (filename.length() >= 0) {
            inputFile.setFilename(filename);
        }
        DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(ttsxFile));
        inputFile.setMetaData(dbObject);
        inputFile.save();
        return inputFile.getId().toString();
    }


    /**
     * 文件下载基础类
     * 断点续读
     *
     * @param outputStream 文件输出流
     * @param id           文件ID
     * @param skip         跳过多少字节 <=0忽略
     * @param length       输出字节长度 <=忽略
     * @throws Exception
     */
    public void download(OutputStream outputStream, String id, long skip, long length) throws Exception {
        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        GridFSDBFile gridFSDBFile = this.gridFS.findOne(query);
        download(outputStream, gridFSDBFile, skip, length);
    }


    /**
     * 文件下载基础类
     * 断点续读
     *
     * @param outputStream 文件输出流
     * @param gridFSDBFile mongo文件
     * @param skip         跳过多少字节 <=0忽略
     * @param length       输出字节长度 <=忽略
     * @throws Exception
     */
    public void download(OutputStream outputStream, GridFSDBFile gridFSDBFile, long skip, long length) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = gridFSDBFile.getInputStream();
            if (skip > 0) {
                inputStream.skip(skip);
            }
            byte[] bs = new byte[1024];
            int len;
            while ((len = inputStream.read(bs)) != -1) {
                if (length > 0) {
                    if (length > len) {
                        outputStream.write(bs, 0, len);
                        outputStream.flush();
                        length -= len;
                    } else {
                        outputStream.write(bs, 0, (int) length);
                        outputStream.flush();
                        break;
                    }
                } else
                    outputStream.write(bs, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            throw e;
        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (outputStream != null) {
//                outputStream.close();
//            }
        }
    }

    /**
     * @param outputStream
     * @param parentId
     * @param fileName
     * @param skip
     * @param length
     * @throws Exception
     */
    public void download(OutputStream outputStream, String parentId, String fileName, long skip, long length) throws Exception {
        fileName = StringTools.trim(fileName);
        DBObject query = new BasicDBObject();
        query.put(TtsxFile.METADATA_FIELD_PARENTID, parentId);
        query.put(TtsxFile.METADATA_FIELD_FILENAME, fileName);
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);
        download(outputStream, gridFSDBFile, skip, length);
    }


    /**
     * @param response
     * @param parentId
     * @param fileName
     * @param skip
     * @param length
     * @throws Exception
     */
    public void download(HttpServletResponse response, String parentId, String fileName, long skip, long length) throws Exception {
        fileName = StringTools.trim(fileName);
        DBObject query = new BasicDBObject();
        query.put(TtsxFile.METADATA_FIELD_PARENTID, parentId);
        query.put(TtsxFile.METADATA_FIELD_FILENAME, fileName);
        GridFSDBFile gridFSDBFile = gridFS.findOne(query);
        if (gridFSDBFile != null && gridFSDBFile.getMetaData() != null && gridFSDBFile.getMetaData().get("contentType") != null) {
            String ct = gridFSDBFile.getMetaData().get("contentType").toString();
            response.setContentType(ct);
        }
        long fileLength = Long.parseLong(gridFSDBFile.getMetaData().get("length").toString().trim());
//        long skip = -1;
        long end = fileLength - 1;
//        long length = -1;
        String range = Mvcs.getReq().getHeader("Range");
        if (range != null && range.length() > 0) {
            int idx = range.indexOf("-");
            skip = Long.parseLong(range.substring(6, idx));
            if ((idx + 1) < range.length()) {
                end = Long.parseLong(range.substring(idx + 1));
            }
            length = end - skip + 1;
        }

        if (range == null || range.length() <= 0) {//bytes=32523-32523
            response.setHeader("Content-Length", "" + fileLength);
            response.setStatus(200);
        } else {
            response.setHeader("Content-Length", "" + length);
            response.setHeader("Content-Range", "bytes " + skip + "-" + end + "/" + fileLength);
            response.setStatus(206);
        }

        download(response.getOutputStream(), gridFSDBFile, skip, length);
    }
}
