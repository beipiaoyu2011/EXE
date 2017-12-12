package gogoal.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import ggframework.bottom.config.GGConfigurer;
import play.Play;

/**
 * GoGoal新一代数据平台
 * 
 * @version 1.0
 * @since JDK1.7
 * @author Albert
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2015 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2015年4月27日 下午2:50:48
 */
public class GoGoalAPI {
	
	private static final String DOMAIN = GGConfigurer.get("api.domain");
	private static final String APP_KEY = GGConfigurer.get("api.appKey");
	private static final String APP_SECRET = GGConfigurer.get("api.appSecret");
	
	private static org.apache.http.client.HttpClient getClient() {
		org.apache.http.client.HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Integer.parseInt(Play.configuration.getProperty("http.timeout", "10000")));
		return client;
	}
	
	private static String requestByGet(String url, String clientIp) throws Exception {
		HttpGet get = new HttpGet(url);
		get.setHeader("x-forwarded-for", clientIp);
		HttpResponse response = getClient().execute(get);
//		System.out.println(url);
		return EntityUtils.toString(response.getEntity());
	}

	@SuppressWarnings("deprecation")
	private static String requestByPost(String url, Map<String, String> parameters, String clientIp) throws Exception {
		HttpPost post = new HttpPost(url);
		post.setHeader("x-forwarded-for", clientIp);
		if (parameters != null && !parameters.isEmpty()) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Entry<String, String> en : parameters.entrySet()) {
				nvps.add(new BasicNameValuePair(en.getKey(), en.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		}
		HttpResponse response = getClient().execute(post);
		return EntityUtils.toString(response.getEntity());
	}
	
	public static String get(String api, Map<String, String> params, String clientIp) throws Exception {
		long timeStamp = System.currentTimeMillis() / 1000;
		
		String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "GET");
		String incomingParams = "";
		if (params != null && !params.isEmpty()) {
			try {
				for (String s : params.keySet()) {
					incomingParams = incomingParams + s + "=" + URLEncoder.encode((String) params.get(s), "UTF-8") + "&";
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			incomingParams = incomingParams.substring(0, incomingParams.length() - 1);
		}
		
		String url = "";
		try {
			url = "/" + api + "?" + "app_key=" + APP_KEY + "&time_stamp=" + timeStamp + "&sign=" + URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (incomingParams != null && !"".equals(incomingParams.trim())) {
			url += "&" + incomingParams;
		}
		url = DOMAIN + url;
		return requestByGet(url, clientIp);
	}
	
	public static String post(String api, Map<String, String> params, String clientIp) throws Exception {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		long timeStamp = System.currentTimeMillis() / 1000;
		String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "POST");
		params.put("app_key", APP_KEY);
		params.put("time_stamp", String.valueOf(timeStamp));
		params.put("sign", sign);
		return requestByPost(DOMAIN + "/" + api, params, clientIp);
	}
	
	private static String getSign(String appKey, String appSecret, Long timeStamp, Map<String, String> params, String url, String requestMethod) {
        Map paramsMap = new HashMap();
        if (params != null) {
        	paramsMap.putAll(params);
        }
        paramsMap.put("app_key", appKey);
        paramsMap.put("time_stamp", timeStamp.toString());
        paramsMap.remove("sign");
        SignHelper.codePayValue(paramsMap);
        try {
            return SignHelper.makeSign(requestMethod, url, paramsMap, appSecret);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }
	
	public static String uploadFile(File file, Map<String,String> params, String clientIp){
		String api = Constant.API_Upload;
		long timeStamp = System.currentTimeMillis() / 1000;
		String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "POST");
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {

		
			HttpPost httppost = new HttpPost(DOMAIN + "/" + api);
			httppost.setHeader("x-forwarded-for", clientIp);

			FileBody bin = new FileBody(file, "image/x-png", "UTF-8");

			MultipartEntity reqEntity = new MultipartEntity();

			reqEntity.addPart("gg_file", bin);// file1为请求后台的File upload;属性
			reqEntity.addPart("app_key", new StringBody(APP_KEY));
			reqEntity.addPart("time_stamp", new StringBody(String.valueOf(timeStamp)));
			reqEntity.addPart("sign", new StringBody(sign));
			reqEntity.addPart("token",new StringBody(params.get("token")));
			//reqEntity.addPart("path", new StringBody(params.get("path")));
			//reqEntity.addPart("file_name", new StringBody(params.get("file_name")));
			httppost.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(httppost);
			result = EntityUtils.toString(response.getEntity());
			 
//			int statusCode = response.getStatusLine().getStatusCode();
//
//			if (statusCode == HttpStatus.SC_OK) {
//
//				System.out.println("服务器正常响应.....");
//
//				HttpEntity resEntity = response.getEntity();
//
//				System.out.println(EntityUtils.toString(resEntity));// httpclient自带的工具类读取返回数据
//
//				System.out.println(resEntity.getContent());
//
//				EntityUtils.consume(resEntity);
//				
//				
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static String postExport(String api, Map<String, String> params, String clientIp) throws Exception {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		long timeStamp = System.currentTimeMillis() / 1000;
		String sign = getSign(APP_KEY, APP_SECRET, timeStamp, params, api, "POST");
		params.put("app_key", APP_KEY);
		params.put("time_stamp", String.valueOf(timeStamp));
		params.put("sign", sign);

		HttpPost post = new HttpPost(DOMAIN + "/" + api);
		post.setHeader("x-forwarded-for", clientIp);
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Entry<String, String> en : params.entrySet()) {
				nvps.add(new BasicNameValuePair(en.getKey(), en.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		}
		HttpResponse response = getClient().execute(post);

		File dir = new File("tmp/report");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String filePath =("tmp/report") + File.separator;
		String fileName = UUID.randomUUID().toString().replaceAll("-", "")+".pdf";
		String fileFullName = filePath+fileName;

		File f = new File(fileFullName);
		byte[] result = EntityUtils.toByteArray(response.getEntity());
		BufferedOutputStream bw = null;
		try {
			bw = new BufferedOutputStream(new FileOutputStream(f));
			bw.write(result);
			bw.close();
		} catch (Exception e) {

		}
		return fileFullName;
	}
	
}
