package lgjt.web.app.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {

	/**
	 * show 获取http请求返回内容.
	 * @author daijiaqi
     * @date 2018/5/918:48
	 * @param httpclient httpclient请求对象
	 * @param url 请求地址
	 * @param params 请求参数
	 * @param headers 需要增加的header
	 * @return 请求结果String
	 * @throws Exception
	 */
	public static String getPostResult(CloseableHttpClient httpclient,String url,HashMap<String,String> params,HashMap<String,String> headers) throws Exception{
		CloseableHttpResponse response = null;
		long bt = System.currentTimeMillis();
		try{
			HttpPost httppost = new HttpPost(url);
			
			if(params!=null && params.size()>0){
				//装填参数  
		        List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
				Iterator<String> keys = params.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					String value = params.get(key);     
			        nvps.add(new BasicNameValuePair(key,value));
				}    
		        //设置参数到请求对象中  
		        httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8")); 
			} 
			
			if(headers!=null && headers.size()>0){
				Iterator<String> keys = headers.keySet().iterator();
				while(keys.hasNext()){
					String key = keys.next();
					String value = headers.get(key);     
					httppost.addHeader(key, value);
				}    
			} 
			
			response = httpclient.execute(httppost);
			return readResponse(response);
		}
		catch(Exception e){
			throw e;
		}
		finally{
			if(response!=null){
				try{
					response.close();
				}catch(Exception e){}
			}
			long et = System.currentTimeMillis();

		}
	}
	/**
	 * show HTTP POST请求.
	 * @author daijiaqi
     * @date 2018/5/918:48
	 * @param httpclient  http请求对象
	 * @param url   请求的url
	 * @param params  请求参数
	 * @param headers 需要增加的header
	 * @return  返回的json对象
	 * @throws Exception
	 */
	public static JSONObject post(CloseableHttpClient httpclient,String url,HashMap<String,String> params,HashMap<String,String> headers) throws Exception{
		return JSON.parseObject(getPostResult(httpclient,url,params,headers));
	}
	
	/**
	 * show 读取http的返回内容.
	 * @author daijiaqi
     * @date 2018/5/918:48
	 * @param response  http返回
	 * @return  返回的字符串
	 * @throws Exception
	 */
	public static String readResponse(HttpResponse response) throws Exception{
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
			String line;
			StringBuffer sb = new StringBuffer();
			while((line=br.readLine())!=null){
				sb.append(line);
			}
//			System.out.println(sb.toString());
			return sb.toString();
		}
		catch(Exception e){
			throw e;
		}
		finally{
			if(br!=null){
				try{
					br.close();
				}catch(Exception e){}
			}
		}
	}
	
	/**
	 * show URL拼接.
	 * @author daijiaqi
     * @date 2018/5/918:48
	 * @param prefix  统一前缀
	 * @param path  url路径
	 * @return 拼接后的url
	 */
	public static String getUrl(String prefix,String path){
		if(prefix.endsWith("/") && path.startsWith("/")){
			return prefix+path.substring(1);
		}
		return prefix+path;
	}

}
