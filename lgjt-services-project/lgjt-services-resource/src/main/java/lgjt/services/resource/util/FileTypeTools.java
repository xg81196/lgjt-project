package lgjt.services.resource.util;


import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 */
public class FileTypeTools {
    /**
     * 根据文件流获取文件类型
     * 优先级：
     * ttsxFile.fileType
     * filename截取
     * 根据文件流判断
     * @param inputStream 文件流
     * @param fileType 用户指定的文件类型
     * @param fileName 文件名称
     * @return 文件类型
     */
    public static String getTileType(InputStream inputStream, String  fileType,String fileName){
        fileType = StringTools.trim(fileType).toLowerCase();
        fileName =  StringTools.trim(fileName).toLowerCase();;
        if(fileType.length()>0){
            if(fileType.indexOf(".")>=0){
                return fileType.substring(fileType.lastIndexOf("."));
            }else{
                return fileType;
            }
        }
        if(fileName.length()>0 && fileName.indexOf(".")>=0){
            return fileName.substring(fileName.lastIndexOf("."));
        }
        try {
            FileType ftEnum = FileTypeTools.getType(inputStream);
            if(ftEnum!=null){
                fileType = ftEnum.name();
            }
        }catch(Exception e){}
        return fileType;
    }

    /**
     * 将文件头转换成16进制字符串
     *
     * @param fileHeaderBytes
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] fileHeaderBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (fileHeaderBytes == null || fileHeaderBytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < fileHeaderBytes.length; i++) {
            int v = fileHeaderBytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     *
     * @param inputStream 文件流
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(InputStream inputStream) throws IOException {
        byte[] b = new byte[28];
        try {
            if (inputStream.markSupported()) {
                inputStream.mark(28);
                inputStream.read(b, 0, 28);
                inputStream.reset();
            } else {
                return "";
            }
        } catch (IOException e) {
            throw e;
        }
        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     *
     * @param inputStream 文件路径
     * @return 文件类型
     */
    public static FileType getType(InputStream inputStream) throws IOException {
        String fileHead = getFileContent(inputStream);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

}
