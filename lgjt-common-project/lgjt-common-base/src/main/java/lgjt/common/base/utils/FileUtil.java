package lgjt.common.base.utils;

import com.google.common.collect.Lists;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 文件上传工具类
 * 
 * @author wy
 */
public class FileUtil {

	private static  ArrayList<File> fileList = Lists.newArrayList();

	public static String getFileName(String suffix){
		return UUID.randomUUID().toString().replace("-", "")+suffix;
	}
	
	/**
	 * 普通公用文件存储路径
	 * @author yangleihong
	 * @date 2017-7-3上午12:03:41
	 * @return
	 */
	public static String getFilePath(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		sdf.format(new Date());
		String filePath = CommonUtil.ROOT_PATH+File.separator+sdf.format(new Date());
		File pathFile = new File(filePath);
		if(!pathFile.exists()){
			pathFile.mkdirs();
		}
		return filePath;
	}


	/**
	 * 根据文件扩展名获取文件类型
	 * @param fileExtension
	 * @return
	 */
	public static Integer fileType(String fileExtension){
		if(TXT.contains(fileExtension)){
			return 6;
		}
		if(VIDEO.contains(fileExtension)){
			return 2;
		}
		if(AUDIO.contains(fileExtension)){
			return 3;
		}
		if(IMG.contains(fileExtension)){
			return 7;
		}
		return -1;
	}

	/**
	 * 递归获取文件
	 * @param path
	 * @return
	 * @throws Exception
	 */

	public static ArrayList<File> getFiles(String path) throws Exception{
		File file = new File(path);
		if(file.isDirectory()){
			File []files = file.listFiles();
			for(File fileIndex:files){
				if(fileIndex.isDirectory()){
					getFiles(fileIndex.getPath());
				}else {
					fileList.add(fileIndex);
				}
			}
		}
		return fileList;
	}


	
	private static final List<String> TXT = new ArrayList<String>();
	private static final List<String> VIDEO = new ArrayList<String>();
	private static final List<String> AUDIO = new ArrayList<String>();
	private static final List<String> IMG = new ArrayList<String>();
	private static final List<String> SWF = new ArrayList<String>();
	private static final List<String> PDF = new ArrayList<String>();
	private static final List<String> SCROM = new ArrayList<String>();
	
	private static final String TXT_FLAG = "txt";
	private static final String VIDEO_FLAG = "video";
	private static final String AUDIO_FLAG = "audio";
	private static final String IMG_FLAG = "img";
	private static final String SWF_FLAG = "swf";
	private static final String PDF_FLAG = "pdf";
	private static final String SCROM_FLAG = "scrom";
	
	public static final String SUFFIX2TYPE = "suffix.properties";
	
	static{
		Properties properties = new Properties();
		FileReader fr=null;
		try {
			
//			properties.load(Mvcs.getServletContext().getClassLoader().getResourceAsStream(SUFFIX2TYPE));
			String path =Thread.currentThread().getContextClassLoader().getResource("/").getPath();
			 fr =new FileReader(new File(path+"/"+SUFFIX2TYPE));
			properties.load(fr);
		
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			if(String.valueOf(entry.getValue()).toLowerCase().equals(TXT_FLAG))
				TXT.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(VIDEO_FLAG))
				VIDEO.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(AUDIO_FLAG))
				AUDIO.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(IMG_FLAG))
				IMG.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(SWF_FLAG))
				SWF.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(PDF_FLAG))
				PDF.add(String.valueOf(entry.getKey()));
			if(String.valueOf(entry.getValue()).toLowerCase().equals(SCROM_FLAG))
				SCROM.add(String.valueOf(entry.getKey()));
		}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
				}
				fr=null;
			}
		}
	}
	
	
	public static void copyFile(File src, File dest)throws Exception {
		if(src == null || dest == null){
			throw new Exception("src or  dest is null!");
		}
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			if(!dest.getParentFile().exists()){
				dest.getParentFile().mkdirs();
			}
			fin = new FileInputStream(src);
			fout = new FileOutputStream(dest);
			byte b[] = new byte[10240];
			int size = 0;
			while((size=fin.read(b))!=-1)
			{
				fout.write(b,0,size);
			}
			fout.flush();
		} catch (Exception e) {
			throw e;
		}finally {
			try {
				if (fout != null) {
					fout.flush();
					fout.close();
					fout = null;
				}
				
				if (fin != null) {
					fin.close();
					fin = null;
				}
			} catch (IOException e) {
			}
		}		
	}
}
