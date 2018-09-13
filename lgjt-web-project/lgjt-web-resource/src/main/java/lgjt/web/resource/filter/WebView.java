package lgjt.web.resource.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import lgjt.common.base.ResultsImpl;
import lgjt.common.base.constants.ReturnCode;
import lgjt.common.base.encryption.ResourceEncryptionUtil;
import lgjt.services.resource.ResourceMongodbHandler;
import lgjt.services.resource.util.StringTools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * @author daijiaqi
 * @date 2018/6/2610:20
 */
@WebServlet("/webView/*")
@Log4j
public class WebView extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String fileId = StringTools.trim(request.getParameter("fid"));
        long skip = -1;
        long length = -1;
        try {
            if (fileId.length() > 0) {
                String fileIdDecode = ResourceEncryptionUtil.base64Decode(fileId);
                String id = ResourceEncryptionUtil.getDecodeUUIDFromDecoderString(fileIdDecode);
                long timestamp = ResourceEncryptionUtil.getDecodeTimestampLongFromDecoderString(fileIdDecode);
                if (checkTimestamp(timestamp)) {
                    ResourceMongodbHandler.getInstance("resource").download(response.getOutputStream(), id, skip, length);
                } else {
                    response.getWriter().write(JSONObject.toJSONString(ResultsImpl.parse(ReturnCode.CODE_101005.getCode(), ReturnCode.CODE_106001.getValue())));
                }
            } else {
                response.setHeader("Accept-Ranges", "bytes");
                response.setHeader("Connection", "keep-alive");
                String[] url = requestURI.split("/");
                String fileName = url[url.length - 1];
                String parentId = url[url.length - 2];

                String parentIdDecode = ResourceEncryptionUtil.base64Decode(parentId);
                String id = ResourceEncryptionUtil.getDecodeUUIDFromDecoderString(parentIdDecode);
                long timestamp = ResourceEncryptionUtil.getDecodeTimestampLongFromDecoderString(parentIdDecode);
                if (checkTimestamp(timestamp)) {
                    ResourceMongodbHandler.getInstance("resource").download(response, id, fileName, skip, length);
                } else {
                    response.getWriter().write(JSONObject.toJSONString(ResultsImpl.parse(ReturnCode.CODE_101005.getCode(), ReturnCode.CODE_106001.getValue())));
                }
            }
        } catch (Exception e) {
            log.error("WebView.deGet():requestURI=" + requestURI + ":fileId=" + fileId, e);
        }
    }

    /**
     * always return true
     *
     * @param timestamp
     * @return
     */
    public boolean checkTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > (timestamp + 120 * 60 * 1000)) {
            return true;
        }
        return true;
    }
}
