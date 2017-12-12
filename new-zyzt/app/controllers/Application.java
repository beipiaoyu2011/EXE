package controllers;

import ggframework.bottom.log.GGLogger;
import ggframework.bottom.store.cache.GGCache;
import gogoal.api.GoGoalAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http.Request;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

public class Application extends Controller {

	public static void index() {
		render();
	}

//	public static String checkToken(String token) throws Exception {
//		String code = GGCache.get("11111111", token);
//		if (code == null) {
//			Map<String, String> paramsToken = new HashMap<String, String>();
//			paramsToken.put("token", token);
//			String jsonStr = GoGoalAPI.get("v1/system/get_token", paramsToken, getClientIp(request));
//			JSONObject tokenObj = JSONObject.parseObject(jsonStr);
//			code = tokenObj.get("code").toString();
//			if ("0".equals(code)) {
//				GGCache.setex("11111111", token, 24 * 60 * 60, code);
//			}
//		}
//		return code;
//	}

	public static void execute() {
		Map<String, String> params = request.params.allSimple();
		params.remove("body");
//		String method = params.get("method");
		String apiName = params.get("version") + "/" + params.get("namespace") + "/" + params.get("method");
//		boolean check = "1".equals(params.get("ignore_check"));
//		params.remove("ignore_check");
		params.remove("version");
		params.remove("namespace");
		params.remove("method");

//		// 排除登陆、邮箱注册、邮箱激活
//		if (!check && !method.contains("login") && !"email_activate".equals(method) && !"bind_email".equals(method)) {
//			Boolean checkToken = true;
//			if (request.cookies == null || request.cookies.get("token") == null
//					|| request.cookies.get("token").value == null) {
//				checkToken = false;
//			}
//			if (checkToken) {
//				String token = request.cookies.get("token").value;
//				try {
//					String tokenCode = null;
//					tokenCode = checkToken(token);
//					checkToken = "0".equals(tokenCode);
//				} catch (Exception e) {
//					errorLog(e, params, apiName);
//					JsonObject json = new JsonObject();
//					json.addProperty("code", 500);
//					json.addProperty("message", "API异常");
//					renderJSON(json.toString());
//				}
//			}
//			if (!checkToken) {
//				JsonObject json = new JsonObject();
//				json.addProperty("code", 405);
//				json.addProperty("message", "非法请求");
//				renderJSON(json.toString());
//			}
//		}
		try {
			if ("GET".equalsIgnoreCase(request.method)) {
				renderJSON(GoGoalAPI.get(apiName, params, getClientIp(request)));
			} else if ("POST".equalsIgnoreCase(request.method)) {
				renderJSON(GoGoalAPI.post(apiName, params, getClientIp(request)));
			} else {
				JsonObject json = new JsonObject();
				json.addProperty("code", 405);
				json.addProperty("message", "HTTP请求方式错误");
				renderJSON(json.toString());
			}
		} catch (Exception e) {
			GGLogger.error(e, "app remote " + apiName + " API请求异常");
			errorLog(e, params, apiName);
			JsonObject json = new JsonObject();
			json.addProperty("code", 500);
			json.addProperty("message", "execute异常");
			renderJSON(json.toString());
		}

		render();
	}
	
	public static void errorLog(Exception e, Map<String, String> params, String apiName) {
		StringBuffer param = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			int size = keys.size();
			int i = 0;
			for (String key : keys) {
				i++;
				param.append(key);
				param.append("=");
				param.append(params.get(key));
				if (i != size) {
					param.append("&");
				}
			}
		}
		play.Logger.error(e, "API请求异常" + "; api: " + apiName + "; params: " + param.toString());
	}

	@Catch(priority = 10, value = Throwable.class)
	public static void internalError(Throwable throwable) {
		GGLogger.error(throwable, "app remote internalError异常");
		JsonObject json = new JsonObject();
		json.addProperty("code", 500);
		json.addProperty("message", "internalError异常");
		renderJSON(json.toString());
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

}