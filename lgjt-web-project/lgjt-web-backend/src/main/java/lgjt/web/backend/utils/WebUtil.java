package lgjt.web.backend.utils;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author majinyong
 * @date 2018/6/8 下午5:26
 * @Description:
 */
public class WebUtil {
    /**
     * 将data写入http响应中
     *
     * @param response
     * @param data
     * @param mimeType
     */
    public static void writeToResponse(HttpServletResponse response, byte[] data, String mimeType) {
        response.setContentType(mimeType);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
