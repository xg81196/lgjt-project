package lgjt.services.resource.config;

public class MongoDbConstants {
    public static final String BUSINESS_TABLE_NAME="ttsx.files";
    /**
     * 删除标识：默认值
     */
    public static final Integer DELETEFLAG_DEFAULT=0;

    /**
     * 删除标识：已删除
     */
    public static final Integer DELETEFLAG_ISDEL=1;

    /**
     * 配置文件中zip文件入库是否解析的标志位
     * zip文件是否解压存放
     * 0代表不解压，只存放zip文件
     * 1代表加压，存放zip文件和加压后的文件
     */
    public static final String CONFIG_ZIP_DECOMPRESSION="zip-decompression";
    /**
     * zip文件不解压
     */
    public static final String CONFIG_ZIP_DECOMPRESSION_NO="0";
    /**
     * zip文件解压
     */
    public static final String CONFIG_ZIP_DECOMPRESSION_YES="1";


}
