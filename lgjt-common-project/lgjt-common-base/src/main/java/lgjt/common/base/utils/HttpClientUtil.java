package lgjt.common.base.utils;

import com.alibaba.fastjson.JSON;
import com.ttsx.platform.nutz.result.Results;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author wuguangwei
 * @date 2018/1/31
 * @Description: Http client操作工具类 ，支持Get和Post(支持JSON序列化传输)
 */
public class HttpClientUtil {

    private final static String CHARSET = "utf-8";


    /**
     * 发送 get请求
     * @author wuguangwei
     */
    public static String get(String url,Map<String, Object> params) {

        if(isBlank(url))return null;

        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();

        CloseableHttpClient httpclient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config).build();
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(),value.toString()));
                    }
                }
                url += "?"+ EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader(StaticUtils.TOKEN_NAME,(String)params.get(StaticUtils.TOKEN_NAME));
            CloseableHttpResponse response = httpclient.execute(httpget);
            // 执行get请求.
            try{
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpget.abort();
                    return null;
                }
                HttpEntity entity = response.getEntity();
                String rs = EntityUtils.toString(entity,CHARSET);
                EntityUtils.consume(entity);
                return rs;
            }finally{
                response.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


    /**
     * 发送 post请求
     * @author wuguangwei
     */
    public static String post(String url, String json) {

        if (isBlank(url)) {
            throw new RuntimeException("request url is required.");
        }

        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            if (isNotBlank(json)) {
                StringEntity content = new StringEntity(json, Charset.forName(CHARSET));
                content.setContentType("application/json; charset=" + CHARSET);
                content.setContentEncoding(CHARSET);
                httpPost.setEntity(content);
            }
            CloseableHttpResponse response = httpclient.execute(httpPost);
            // 执行get请求.
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpPost.abort();
                    return null;
                }
                HttpEntity entity = response.getEntity();
                String rs = EntityUtils.toString(entity,CHARSET);
                EntityUtils.consume(entity);

                return rs;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }


    /**
     * 发送 post请求
     * @author wuguangwei
     */
    public static String post(String url, Map<String, Object> params) {
        if (isBlank(url)) {
            throw new RuntimeException("request url is required.");
        }

        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            String json = "{}";
            if (params != null) {
                json = JSON.toJSONString(params);
            }
            StringEntity content = new StringEntity(json,Charset.forName(CHARSET));
            content.setContentType("application/json; charset=" + CHARSET);
            content.setContentEncoding(CHARSET);
            httpPost.setEntity(content);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            // 执行get请求.
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    httpPost.abort();
                    return null;
                }
                HttpEntity entity = response.getEntity();
                String rs = EntityUtils.toString(entity,CHARSET);
                EntityUtils.consume(entity);

                /*if(isNotBlank(rs)){
                    return JSON.parseObject(rs, Results.class);
                }*/

                return rs;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
