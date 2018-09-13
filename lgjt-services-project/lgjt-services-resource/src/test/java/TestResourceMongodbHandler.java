import lgjt.domain.resource.TtsxFile;
import lgjt.services.resource.ResourceMongodbHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author daijiaqi
 * @date 2018/7/519:09
 */
public class TestResourceMongodbHandler {

    public  static void main(String [] arr)throws  Exception{
        //, TtsxFile ttsxFile
         String id=     ResourceMongodbHandler.getInstance("file").upload(new FileInputStream("d://12.htm"),new TtsxFile());
        System.out.println("============");
        ResourceMongodbHandler.getInstance("file").download(new FileOutputStream("d://dd.htm"),id,-1,-1);
    }
}
