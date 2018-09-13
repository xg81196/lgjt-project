package lgjt.web.backend.utils;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author majinyong
 * @date 2018/6/8 下午5:29
 * @Description:
 */
public class ExportUtils {
    /**
     * 通过文件流的方式从服务器上下载文件模板
     *
     * @param filepath 文件模板的路径
     * @param filename 下载后的文件名
     */
    public static void downLoadTemplateFromServer(String filepath, HttpServletResponse response, String filename) throws Exception{
        byte[] content = FileUtils.readFileToByteArray(new File(filepath));
        response.setCharacterEncoding("gbk");
        String header = "attachment;filename=" + new String((filename).getBytes("UTF-8"), "ISO-8859-1");
        if(filepath.endsWith(".xlsx")){
            response.setHeader("Content-Disposition", header + ".xlsx");
            WebUtil.writeToResponse(response, content, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=gbk");
        }else{
            response.setHeader("Content-Disposition", header + ".xls");
            WebUtil.writeToResponse(response, content, "application/vnd.ms-excel;charset=gbk");
        }
    }
}
