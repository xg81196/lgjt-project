package tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
/**
 * show 对比filters文件夹下properties文件，获取每个文件中key的缺少值.
 * @author wangyu
 * @date 2018-07-05
 */
public class FiltersPropertiesCompareUtils {

	public static void main(String[] args) throws IOException {

		//声明文件地址
		String path = "D:\\qzngworkspace\\project\\filters\\";
		
		List<String> fileNames = getFile(path);
		
		if(fileNames == null || fileNames.size() == 0) {
			System.out.println("当前文件夹下没有文件！");
		}
		
		int size = fileNames.size();
		
		List<Set<String>> keyList = new ArrayList<Set<String>>();
		
		for(int i=0; i<size; i++) {
			//将每个key值集合放入一个集合中
			keyList.add(getKey(path+fileNames.get(i)));
		}
	
		//获取全部文件key值并集
		Set<String> unionSet = new HashSet<String>();
		for (Set<String> set : keyList) {
			unionSet.addAll(set);
		}
		System.out.println("全部文件的key值全集为："+unionSet);
			
		//获取每个文件key集合与全集的差集
		for(int i=0; i<size; i++) {
			Set<String> set = new HashSet<String>();
			set.addAll(unionSet);
			Set<String> keySet = getDifferentSet(set, keyList.get(i));
			System.out.println(fileNames.get(i) + "文件下不存在的key值有："  + keySet);
		}		
		
		System.out.println("----------------------------------------------");
		//获取每个文件中重复的key值集合
		for(int i=0; i<size; i++) {
			List<String> list = new ArrayList<String>();
			list.addAll(keyList.get(i));
			System.out.println(fileNames.get(i) + "文件中重复的key值有：" + getRepeatMap(list));
		}
	}

	/**
	 * show 读取properties文件中的key，存放在集合中返回.
	 * @author wangyu
	 * @param file 文件地址
	 * @return key key值集合
	 * @throws IOException
	 */
	public static Set<String> getKey(String file) throws IOException{
		Set<String> keyList = new HashSet<String>();
		Properties prop = new Properties();
		try {
			//读取文件
			InputStream inStream= new BufferedInputStream (new FileInputStream(file));
			prop.load(inStream);
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while(it.hasNext()) {
				String key = it.next();
				keyList.add(key);
			}
			inStream.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		return keyList;
	}
	
	/**
	 * show 获取两个集合的差集.
	 * @author wangyu
	 * @param unionSet 全集
	 * @param keyList 子集
	 * @return 差集
	 */
	public static Set<String> getDifferentSet(Set<String> unionSet, Set<String> keyList) {
		unionSet.removeAll(keyList);
		return unionSet;
	}
	
	/**
	 * show 从固定目录下读取全部文件名.
	 * @author wangyu
	 * @param fileDir 文件目录
	 * @return 全部文件list
	 */
	public static List<String> getFile(String fileDir) {
		//用于存放当前目录下所有文件名
		List<String> fileName = new ArrayList<String>();
		
		//获取当前目录下的文件及文件夹
		File file = new File(fileDir);
		File[] files = file.listFiles();
		
		//如果目录下不存在文件或文件夹，返回空集合
		if(files == null) {
			return fileName;
		}
		
		//存放当前目录下的全部文件（子子孙孙）
		List<File> fileList = new ArrayList<File>();
		
		//遍历文件数组，如果是文件，将文件加入集合，如果是目录，将目录遍历取其下边所有文件
		for (File f : files) {
			if(f.isFile()) {
				fileList.add(f);
			}else if(f.isDirectory()){
				getFile(f.getAbsolutePath());
			}
		}
		
		//遍历当前目录下的所有文件，获取其文件名，并将其存放在集合中
		for (File f2 : fileList) {
			fileName.add(f2.getName());
		}
		return fileName;
	}
	
	public static HashMap<String,String> getRepeatMap(List<String> list){
		HashMap<String,String> map = new HashMap<String,String>();
		HashMap<String,String> repeatMap = new HashMap<String,String>();
		for (String str : list) {
			if(map.get(str) == null || map.get(str).equals("")) {
				map.put(str, "1");
			}else {
				int num = Integer.parseInt(map.get(str));
				map.put(str, num+1+"");
				repeatMap.put(str, num+1+"");
			}
		}
		return repeatMap;
	}
}
