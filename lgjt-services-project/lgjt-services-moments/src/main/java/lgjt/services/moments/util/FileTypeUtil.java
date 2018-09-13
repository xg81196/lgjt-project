package lgjt.services.moments.util;

/**
 * show 文件列类别.
 * @author daijiaqi
 * @date 2018/5/23 13:15
 */
public class FileTypeUtil {
    /**
     * 视频文件 3
     */
    private static String VIDEO_SUFFIX = ";RMVB;RM;WMV;AVI;3GP;MP4;FLV;SWF;IPOD;PSP;";
    /**
     * 音频文件2
     */
    private static String AUDIO_SUFFIX = ";CD;OGG;MP3;ASF;WMA;WAV;MP3PRO;RM;REAL;APE;MODULE;MIDI;VQF;";
    /**
     * 图片 1
     */
    private static String IMAGE_SUFFIX = ";JPG;JPEG;PNG;GIF;BMP;";

    /**
     * show 根据文件后缀判断图片类型.
     * @author daijiaqi
     * @date 2018/5/23 13:15
     * @param suffix 文件后缀
     * @return 1 图片 2 音频 3 视频
     */
    public static String getFileType(String suffix) {
        suffix = ";" + suffix + ";".toUpperCase();
        if (VIDEO_SUFFIX.indexOf(suffix) > -1) {
            return "3";
        } else if (AUDIO_SUFFIX.indexOf(suffix) > -1) {
            return "2";
        } else if (IMAGE_SUFFIX.indexOf(suffix) > -1) {
            return "1";
        } else {
            return "0";
        }
    }
}
