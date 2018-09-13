package lgjt.services.resource.util;

import lgjt.common.base.utils.SimpleDateFormatUtils;
import lgjt.domain.resource.TtsxFile;

import java.io.InputStream;
import java.util.Date;

/**
 * TtsxFile工具类，主要处理一些对象相关的内容
 *
 * @Author： dai.jiaqi
 * @Description:
 * @Date:Created in 15:01 2018/1/16
 * @Modified by:
 */
public class TtsxFileUtil {

    /**
     * 上传文件时处理 对象属性
     *
     * @param ttsxFile 对象
     */
    public static void checkTtsxFileForUpload(TtsxFile ttsxFile) throws Exception {
        if (ttsxFile == null) {
            return;
        }
        //设置文件类型，如果是空默认是主文件
        if (ttsxFile.getFileFlag() == null) {
            ttsxFile.setFileFlag(TtsxFile.FILEFLAG_DEFAULT);
        }

        //如果文件名为空，问件显示的名称设置为文件名
        if (ttsxFile.getFileName() == null) {
            ttsxFile.setFileName(StringTools.trim(ttsxFile.getDisplayName()));
        }

        //如果父ID为空默认-1，这件显示的名称设置为文件名
        if (ttsxFile.getParentId() == null) {
            ttsxFile.setParentId(TtsxFile.PARENTID_DEFAULT);
        }

        //如果文件标识为空，则默认设置常规文件
        if (ttsxFile.getStateFlag() == null) {
            ttsxFile.setStateFlag(TtsxFile.STATEFLAG_NORMAL);
        }
        //如果显示名称为空，这默认将filename赋值给显示名称
        if (ttsxFile.getDisplayName() == null) {
            ttsxFile.setDisplayName(StringTools.trim(ttsxFile.getFileName()));
        }
        //如果描述为空，默认为""
        if (ttsxFile.getDisplayName() == null) {
            ttsxFile.setDisplayName("");
        }


        //如果父ID为空默认-1，这件显示的名称设置为文件名
        if (ttsxFile.getReferId() == null) {
            ttsxFile.setReferId(TtsxFile.REFERID_DEFAULT);
        }

        //如果业务类型为空，设置默认值
        if (ttsxFile.getReferType() == null) {
            ttsxFile.setReferType(TtsxFile.REFERTYPE_DEFAULT);
        }

        //如果文件类型为空，设置""
        if (ttsxFile.getFileType() == null) {
            ttsxFile.setFileType("");
        }

        //如果排序字段为空
        if (ttsxFile.getSort() == null) {
            ttsxFile.setSort(TtsxFile.SORT_DEFAULT);
        }
        Date currentDate = new Date();
        String currentDateString = SimpleDateFormatUtils.getStringDate(currentDate, SimpleDateFormatUtils.PATTERN_TYPE_3);
        ttsxFile.setCrtTime(currentDate);
        ttsxFile.setC_ytd(currentDateString.substring(0, 8));
        ttsxFile.setC_year(currentDateString.substring(0, 4));
        ttsxFile.setC_month(currentDateString.substring(4, 6));
        ttsxFile.setC_day(currentDateString.substring(6, 8));
        ttsxFile.setC_hour(currentDateString.substring(8, 10));
        ttsxFile.setC_min(currentDateString.substring(10, 12));
        ttsxFile.setC_second(currentDateString.substring(12, 14));




    }


}
