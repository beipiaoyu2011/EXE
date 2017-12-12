package controllers.userlogin;

import ggframework.bottom.log.GGLogger;
import ggframework.bottom.store.cache.GGCache;
import gogoal.api.GoGoalAPI;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import play.Play;
import play.libs.Time;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Scope.Session;
import util.DateUtil;
import util.SessionUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import constant.APIMap;

/**
 * web终端登陆
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月18日 下午4:35:32
 */
public class LoginAction extends Controller{

	/**
	 * web终端登陆
	 *
	 * 
	 * @author xuyong
	 * @date 2016年3月18日 下午4:35:59
	 */
	public static void login()
	{
		String domain=Play.configuration.getProperty("domain");
		long time = System.currentTimeMillis();
		Map<String, String> params = request.params.allSimple();
		String content="";
		String flag = params.get("flag");
		String pwd = params.get("passwordmd5");
		if(StringUtils.isNotBlank(flag)){
			String cachePwd = GGCache.get(SessionUtil.CachebusinessCode, "pwd_"+flag);
			if(StringUtils.isNotBlank(cachePwd)){
				pwd = cachePwd;
			}
			params.remove("flag");
			if(StringUtils.isNotBlank(pwd)){
				params.put("passwordmd5", pwd);
			}else{
				JsonObject data = new JsonObject();
				data.addProperty("code", 3);
				data.addProperty("message", "保存密码已失效");
				JsonObject json = new JsonObject();
				json.addProperty("code", 0);
				json.add("data", data);
				json.addProperty("message", "成功");
				renderJSON(json.toString());
			}
	
		}
		try {
			params.remove("body");
			content=GoGoalAPI.post(APIMap.API_Login, params, getClientIp(request));
			JSONObject jsonObj=(JSONObject) JSON.parse(content);
			String code=jsonObj.get("code")==null?"":jsonObj.get("code").toString();
			if(jsonObj!=null &&code.equals("0")){
				Map<String,Object> data=(Map<String, Object>) jsonObj.get("data");
				
				if(data.get("code").toString().equals("0")){
					String token = data.get("token").toString();
					response.setCookie(APIMap.Cookie_Web, token+time, domain, "/", Time.parseDuration("30d"), false);
					SessionUtil.setTokenSession("token_"+token+time,data.get("token").toString());
					SessionUtil.setTokenSession("date_"+token+time,DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
					String autologin=params.get("autologin");				
					if(autologin!=null && autologin.equals("1")){
						response.setCookie(APIMap.Cookie_Name, params.get("account_name"), domain, "/", Time.parseDuration("30d"), false);
						response.setCookie(APIMap.Cookie_Flag, token, domain, "/", Time.parseDuration("30d"), false);
						GGCache.setex(SessionUtil.CachebusinessCode, "pwd_"+token, 30 * 24 * 60 * 60, pwd); //设置md5密码缓存
					}
						response.setCookie(APIMap.Cookie_Token,  token, domain, "/",-1, false);	
						response.setCookie(APIMap.Cookie_SessionId, token+time , domain, "/", -1, false);

				}
			}  
			renderJSON(content);
			
		}catch (Exception e) {
			play.Logger.error(e, "API请求异常");
			renderJSON(buildResult(500, "login异常"));
		}
	
	}
	
	/**
	 * 
	 * 用户退出
	 *
	 * 
	 * @author majc
	 * @date 2015年8月4日 下午7:58:50
	 */
	public static void logout(){
		String domain=Play.configuration.getProperty("domain");
		response.setCookie(APIMap.Cookie_Token, "", domain, "/", 0, false);
		response.setCookie(APIMap.Cookie_SessionId, "", domain, "/", 0, false);
		Map<String, String> params = request.params.allSimple();
		SessionUtil.clearToken("token_", request.cookies);
		String content="";
		try {
			params.remove("body");
			content=GoGoalAPI.get(APIMap.API_Logout, params, getClientIp(request));
			JSONObject jsonObj=(JSONObject) JSON.parse(content);
			String code=jsonObj.get("code")==null?"":jsonObj.get("code").toString();
			if(jsonObj!=null &&code.equals("0")){
				Map<String,Object> data=(Map<String, Object>) jsonObj.get("data");
				if(data.get("code").toString().equals("0")){
			
				}
			}  
			renderJSON(content);
			
		}catch (Exception e) {
			play.Logger.error(e, "API请求异常");
			renderJSON(buildResult(500, "logout异常"));
		}
	}
	/**
	 * 
	 * 登录状态
	 *
	 * 
	 * @author majc
	 * @date 2016年3月2日 上午11:24:01
	 */
	public static void loginStatus(){
		Map<String, Cookie> c = request.cookies;
		Map<String, String> params = request.params.allSimple();
		String content="";
		String token = params.get("token");
		if(StringUtils.isBlank(token)){
			renderJSON(buildResult(400, "【必填参数不完整，缺少以下参数：token】"));
		}
		if(!(c.get(APIMap.Cookie_Web) == null || c.get(APIMap.Cookie_Web) .value == null)){
			String valid = SessionUtil.validToken(c);
			if(StringUtils.isBlank(valid)){
				SessionUtil.clearToken("token_", c);
				response.setCookie(APIMap.Cookie_Token, "", SessionUtil.domain, "/", 0, false);
				response.setCookie(APIMap.Cookie_Web, "", SessionUtil.domain, "/", 0, false);
				renderJSON(buildResult(1100, "会话无效"));
			}
		}
		content  = GGCache.get(SessionUtil.CachebusinessCode,"login_status_"+token);
//		params.put("token",valid);
		
		if(StringUtils.isNotBlank(content)){
			renderJSON(content);
		}
		content="";
		try {
			params.remove("body");
			content=GoGoalAPI.get("v1/account/login_status", params, getClientIp(request));
			JSONObject jsonObj=(JSONObject) JSON.parse(content);
			String code=jsonObj.get("code")==null?"":jsonObj.get("code").toString();
			if(jsonObj!=null &&code.equals("0")){
				Map<String,Object> data=(Map<String, Object>) jsonObj.get("data");
				if(data != null && data.get("code").toString().equals("0")){
					GGCache.setex("99999999", "login_status_"+token, 30 * 60 , content);
					renderJSON(content);
				}else{
					renderJSON(buildResult(1,"失败"));
				}
			}else{
				renderJSON(buildResult(1,"失败"));
			}  
		
		}catch (Exception e) {
			play.Logger.error(e, "API请求异常");
			renderJSON(buildResult(500, "login异常"));
		}
		
	}

	private static String getClientIp(Request request) {
		String clientRealIp = null;
		if (request.headers.get("x-forwarded-for") != null) {
			clientRealIp = request.headers.get("x-forwarded-for").toString();
			clientRealIp = clientRealIp.substring(1, clientRealIp.length() - 1);
		} else {
			clientRealIp = request.remoteAddress;
		}
		return clientRealIp;
	}

	private static String buildResult(int code, String message) {
		JsonObject json = new JsonObject();
		json.addProperty("code", code);
		json.addProperty("message", message);
		return json.toString();
	}
}
