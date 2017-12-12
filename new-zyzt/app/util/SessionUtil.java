package util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;

import constant.APIMap;
import ggframework.bottom.log.GGLogger;
import ggframework.bottom.store.cache.GGCache;
import play.Play;
import play.libs.Time;
import play.mvc.Http.Cookie;
import play.mvc.Scope;
import play.mvc.Scope.Session;

/**
 * 
 * 根据session会话数据进行相关操作
 * 
 * @version 1.0
 * @since JDK1.7
 * @author majc
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月2日 下午2:09:35
 */
public class SessionUtil {
	
	public static int maxAge = 30 * 60; //session有效时间(秒)
	public static String  CachebusinessCode = "99999999";
	public static String domain=Play.configuration.getProperty("domain");
	/**
	 * 
	 * 方法描述 初始化session
	 *
	 * @param session
	 * @param c
	 * @return
	 * 
	 * @author majc
	 * @date 2016年3月1日 下午2:57:18
	 */
//	public static boolean  init(Session session,Map<String, Cookie>  c){
//		String sessionName = Scope.COOKIE_PREFIX + "_SESSION";
//		String sessionKey = "___ID";
//		if(valid(c)){
//			for (String item : c.get(sessionName).value.split("&")) {
//				if (item.split("=")[0].endsWith(sessionKey)) {
//					session.put(sessionKey, item.split("=")[1]);
//				} else {
//					session.put(item.split("=")[0], item.split("=")[1]);
//				}
//			}
//			return true;
//		}
//		
//		return false;
//	}
	/**
	 * 
	 * 校验
	 *
	 * @param session
	 * @param c
	 * 
	 * @author majc
	 * @date 2016年3月1日 下午3:02:30
	 */
	public static boolean  valid(Map<String, Cookie>  c){
		if (c.containsKey(APIMap.Cookie_SessionId)) {
			return true;
		} else {
			GGLogger.info("-------浏览器关闭或session失效  会话失效");
			return false;
		}
	}
	/**
	 * 
	 * 校验
	 *
	 * @param session
	 * @param c
	 * 
	 * @author majc
	 * @date 2016年3月1日 下午3:02:30
	 */
	public static String  validToken(Map<String, Cookie>  c){
		if(c== null || c.isEmpty()){
			return null;
		}
		if(!valid(c)){
			return null;
		}
		String sessionId =  c.get(APIMap.Cookie_SessionId).value;
		String token = GGCache.get(CachebusinessCode, "token_"+sessionId);
		if (Play.configuration.containsKey("application.session.maxAge")) {
			maxAge = Time.parseDuration(Play.configuration.getProperty("application.session.maxAge"));
		}
		if (StringUtils.isNotBlank(token)) {
			GGCache.setex(CachebusinessCode, "token_"+sessionId, maxAge, token);
		} else {
			GGLogger.info("-------未登录，请登录===");
//			GGCache.setex("99999999", "token_"+sessionId, maxAge, 8888);
			return null;
		}
		return token;
	}
	/**
	 * 
	 * 设置tokenSession
	 *
	 * @param sessionId
	 * @param token
	 * 
	 * @author majc
	 * @date 2016年3月1日 下午3:57:41
	 */
	public static void setTokenSession(String key,String value){
		
		GGCache.setex(CachebusinessCode, key, maxAge, value);
	}
	/**
	 * 
	 * 清除缓存
	 *
	 * @param key
	 * @param session
	 * @param c
	 * 
	 * @author majc
	 * @date 2016年3月2日 下午4:48:21
	 */
	public static void clearToken(String key,Map<String, Cookie>  c){
		if(valid(c)){
			GGCache.del(CachebusinessCode, key+c.get(APIMap.Cookie_SessionId));
		}
	}
	
	public static String buildResult(int code, String message) {
		JsonObject json = new JsonObject();
		json.addProperty("code", code);
		json.addProperty("message", message);
		return json.toString();
	}
}
