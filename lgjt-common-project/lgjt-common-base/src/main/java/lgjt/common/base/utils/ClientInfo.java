package lgjt.common.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.nutz.mvc.Mvcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ClientInfo {

	private static final String ANONYMOUS = "anonymous";

	public static String getIp() {
		String ip = "0.0.0.0";
		if(Mvcs.getReq() != null) {
			ip = Mvcs.getReq().getRemoteAddr();
		}
		return ip;
		
	}

	public static void main(String[] args) {
		JSONArray jsonDate = null;
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File("d:/quizzes.json")), "gbk");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			jsonDate = JSON.parseArray(stringBuilder.toString());//得到JSONobject对象
			System.out.println(jsonDate.toString());

		} catch (Exception e) {
           e.printStackTrace();
		}

	}
}
