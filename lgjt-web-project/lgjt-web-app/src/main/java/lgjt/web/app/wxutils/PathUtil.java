package lgjt.web.app.wxutils;

import java.io.File;
import java.util.Calendar;

import com.ttsx.platform.tool.util.StringTool;
import com.ttsx.platform.tool.util.UUIDUtil;

public class PathUtil {
	public static final String ROOT_KEY = "fileroot";
	public static final String DEFAULT = "default";

	public static final String SEPARATOR = ".";

	public static String getFilePath(String key) {

		String root = PropertyUtil.getProperty(ROOT_KEY);
		if (!root.endsWith(File.separator)) {
			root += File.separator;
		}
		String path = root + key;
		path = path.replace(SEPARATOR, File.separator);
		return path;
	}

	public static String getFileKey(String module) {

		if (null == module) {
			module = DEFAULT;
		}
		String path = module + SEPARATOR;
		Calendar calendar = Calendar.getInstance();
		String year = calendar.get(Calendar.YEAR) + "";
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
		return path + year + SEPARATOR + month + SEPARATOR + day + SEPARATOR
				+ UUIDUtil.getUUID();
	}

	public static void main(String[] args) {
		String key = getFileKey(null);
		String path = getFilePath(key);

		File f = new File("e:\\1.txt");
		File dest = new File(path);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		System.out.println(f.renameTo(dest));
	}
}
