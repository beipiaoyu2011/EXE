package controllers.gogoalweb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

import entity.DataFormat;
import entity.ImageInfo;
import entity.Params;
import entity.WordParam;
import excel.ExportExcel;
import excel.ExportExcelMyStockGroupAnalyze;
import ggframework.bottom.log.GGLogger;
import ggframework.bottom.store.cache.GGCache;
import gogoal.api.GoGoalAPI;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import util.FtpUtilSeven;
import util.WordExport;

/**
 * 数据服务接口
 * 
 * @version 1.0
 * @since JDK1.7
 * @author Albert
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2015 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2015年6月9日 下午3:03:03
 */
public class DataHandler extends Controller {

	public static String checkToken(String token) throws Exception {
		String code = GGCache.get("11111111", token);
		if (code == null) {
			Map<String, String> paramsToken = new HashMap<String, String>();
			paramsToken.put("token", token);
			String jsonStr =  GoGoalAPI.get("v1/system/get_token", paramsToken, getClientIp(request));
			JSONObject tokenObj = JSONObject.parseObject(jsonStr);
			code = tokenObj.get("code").toString();
			if ("0".equals(code)) {
				GGCache.setex("11111111", token, 24 * 60 * 60, code);
			}
		}
		return code;
	}
	
	/**
	 * API调用通道
	 * 
	 * @author Albert
	 * @date 2015年6月9日 下午3:02:47
	 */
	public static void execute() {
		Map<String, String> params = request.params.allSimple();
		params.remove("body");
		String method = params.get("method");
		String apiName = params.get("version") + "/" + params.get("namespace") + "/" + params.get("method");
		boolean check = "1".equals(params.get("ignore_check"));
		params.remove("ignore_check");
		params.remove("version");
		params.remove("namespace");
		params.remove("method");
		//排除登陆、邮箱注册、邮箱激活
		if (!check && !method.contains("login") && !"email_activate".equals(method) && !"bind_email".equals(method)){
			Boolean checkToken = true;
			if (request.cookies == null || request.cookies.get("token") == null || request.cookies.get("token").value == null) {
				checkToken = false;
			}
			if (checkToken) {
				String token = request.cookies.get("token").value;
				try {
					String tokenCode = null;
					tokenCode = checkToken(token);
					checkToken = "0".equals(tokenCode);
				} catch (SocketTimeoutException e) {//新增API超时状态返回
					play.Logger.error(e, "API请求异常");
					
					JsonObject json = new JsonObject();
					json.addProperty("code", 504);
					json.addProperty("message", "API超时");
					renderJSON(json.toString());
				} catch (Exception e) {
					errorLog(e, params, apiName);
					JsonObject json = new JsonObject();
					json.addProperty("code", 500);
					json.addProperty("message", "API异常");
					renderJSON(json.toString());
				}
			}
			if (!checkToken) {
				JsonObject json = new JsonObject();
				json.addProperty("code", 405);
				json.addProperty("message", "非法请求");
				renderJSON(json.toString());
			}
		}
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
			errorLog(e, params, apiName);
			
			JsonObject json = new JsonObject();
			json.addProperty("code", 500);
			json.addProperty("message", "API异常");
			renderJSON(json.toString());
		}
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
		JsonObject json = new JsonObject();
		json.addProperty("code", 500);
		json.addProperty("message", "API异常");
		renderJSON(json.toString());
	}

	/**
	 * 
	 * html跳转
	 *
	 * 
	 * @author lisl
	 * @date 2016年7月4日 下午2:26:29
	 */
	public static void htmlRender() {
		Map<String, String> params = request.params.allSimple();
		params.remove("body");
		String token = params.get("token");

		if (token != null) {
			// 检查是否有访问权限
		}
		String namespace = params.get("namespace");
		namespace = namespace.contains(".html") ? namespace : namespace + ".html";
		render("html/" + namespace);
	}

	public static String getClientIp2(Request request) {
		String clientRealIp = null;
		if (request.headers.get("x-forwarded-for") != null) {
			clientRealIp = request.headers.get("x-forwarded-for").toString();
			clientRealIp = clientRealIp.substring(1, clientRealIp.length() - 1);
		} else {
			clientRealIp = request.remoteAddress;
		}
		return clientRealIp;
	}

	public static String getClientIp(Request header) {
		Map<String, Header> headers = header.headers;
		String ip = null;
		if(headers != null)	{
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("gg-ip") == null ? null : headers.get("gg-ip").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("cdn-src-ip") == null ? null : headers.get("cdn-src-ip").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("x-forwarded-for") == null ? null : headers.get("x-forwarded-for").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("x-real-ip") == null ? null : headers.get("x-real-ip").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("Proxy-Client-IP") == null ? null : headers.get("Proxy-Client-IP").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = headers.get("WL-Proxy-Client-IP") == null ? null : headers.get("WL-Proxy-Client-IP").value();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = header.remoteAddress;
			}
		} else {
			ip = header.remoteAddress;
		}
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.split(",")[0].trim();
			if (isIp(ip)) {
				return ip;
			}
		}

		if (isIp(ip)) {
			return ip;
		}
		return null;
	}

	private static boolean isIp(String ip) {
		Pattern pattern = Pattern.compile(
				"\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	/**
	 * 
	 * Excel数据下载
	 *
	 * 
	 * @author lisl
	 * @date 2016年5月31日 上午11:05:41
	 */
	public static void downLoadDataExcel() {
		String param = request.params.get("data", String.class);// 数据调取参数(含有API接口名称和调用接口所需参数)
		String fileName = request.params.get("file_name", String.class);// 导出文件名(Excel文件的名称)
		String pattern = request.params.get("pattern", String.class);// 导出时间格式
		String projectString = request.params.get("project", String.class);// Excel表头信息

		if (param == null || projectString == null || param.isEmpty() || projectString.isEmpty()) {
			JsonObject json = new JsonObject();
			json.addProperty("code", 110);
			json.addProperty("message", "不存在有效数据");
			renderJSON(json.toString());
		}

		List<Map<String, Object>> maps = (List<Map<String, Object>>) JSONArray.parse(projectString);// 解析表头

		List<Map<String, Object>> headers = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : maps) {
			Map<String, String> project = null;
			List<String> header = null;
			Map<String, String> temp = new HashMap<String, String>();// 临时变量--存储表头对应子表头信息
			List<String> tempHeader = new ArrayList<String>();// 临时变量--存储表头的先后顺序信息
			List<String> headerList = new ArrayList<String>();
			String projectStr = map.get("sheet_header").toString();// 表头
			if (StringUtils.isNotBlank(projectStr)) {
				project = new HashMap<String, String>();
				header = new ArrayList<String>();
				String[] projectArray = projectStr.split(";");
				for (int i = 0; i < projectArray.length; i++) {
					String p = projectArray[i];
					int index = p.indexOf(":");
					project.put(p.substring(0, index), p.substring(index + 1));

					String headStr = p.substring(index + 1);
					headerList.add(headStr);
					if (headStr.contains("@@")) {
						String[] hierarchyHear = headStr.split("@@");
						if (temp.get(hierarchyHear[0]) != null) {
							String pj = temp.get(hierarchyHear[0]) + "," + hierarchyHear[1];
							temp.put(hierarchyHear[0], pj);
						} else {
							temp.put(hierarchyHear[0], hierarchyHear[0] + ":" + hierarchyHear[1]);
						}
						if (!tempHeader.contains(hierarchyHear[0])) {
							tempHeader.add(hierarchyHear[0]);
						}
					} else {
						if (!tempHeader.contains(headStr)) {
							tempHeader.add(headStr);
							temp.put(headStr, headStr);
						}
					}
				}
			}
			if (header != null && !tempHeader.isEmpty()) {
				for (String h : tempHeader) {
					header.add(temp.get(h));
				}
			}

			String[] arrHeaders = (String[]) header.toArray(new String[header.size()]);

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("new_header", arrHeaders);
			m.put("old_header", headerList);
			m.put("sheet_name", map.get("sheet_name"));// Excel的sheet名称
			m.putAll(project);
			headers.add(m);
		}

		List<Params> params = explainPath(param);
		List<String> apiNameList = new ArrayList<String>();// 所请求的API
		if (params != null && !params.isEmpty() && maps != null && !headers.isEmpty()) {
			List<String> contentList = new ArrayList<String>();// 所有接口返回的数据
			try {
				for (Params para : params) {
					String content = GoGoalAPI.post(para.getPath(), para.getParam(), getClientIp(request));
					contentList.add(content);
					apiNameList.add(para.getPath());
				}

				String fileNa = java.net.URLEncoder.encode(fileName != null ? fileName + ".xls" : "数据导出.xls", "UTF-8");
				response.setContentTypeIfNotSet("application/vnd.ms-excel;charset=UTF-8");
				response.setHeader("Content-disposition", "attachment;filename=" + fileNa);
				HSSFWorkbook book = new HSSFWorkbook();
				if (!headers.isEmpty()) {
					int i=0;
					for (Map<String, Object> map : headers) {
						String[] arrHeaders = (String[]) map.get("new_header");
						map.remove("new_header");
						List<String> headerList = (List<String>) map.get("old_header");
						map.remove("old_header");
						String excelSheetName = map.get("sheet_name") == null ? (fileName == null ? "数据导出" : fileName)
								: map.get("sheet_name").toString();
						map.remove("sheet_name");
						
						List<Map<String, Object>> data = null;
						if(apiNameList.contains("v1/select_stock/get_stockbytags_info")|| 
								apiNameList.contains("v1/select_stock/get_stock_value")|| 
								apiNameList.contains("v1/select_stock/get_stock_tags")){
							data = DataFormat.formatData(contentList.subList(i, i+1), map, pattern, apiNameList.subList(i, i+1));
							i++;
						}else{
							data = DataFormat.formatData(contentList, map, pattern, apiNameList);	
						}
						
						if (data == null || data.isEmpty()) {
							continue;
						}

						// 增加序号信息
						int flag = 0;
						for (Map<String, Object> dataMap : data) {
							flag++;
							dataMap.put("序号", flag);
						}

						// 生成单个Sheet
						if (apiNameList.contains("v1/mystock/get_mystock_group_analyze")) {//自选股分析->收益统计 : 表格格式特殊
							ExportExcelMyStockGroupAnalyze.exportExcel(book, excelSheetName, data, headerList);
						} else {
							ExportExcel.exportExcel(book, excelSheetName, arrHeaders, data, headerList);
						}
					}

					// 输出文件
					book.write(response.out);
				} else {
					JsonObject json = new JsonObject();
					json.addProperty("code", 110);
					json.addProperty("message", "不存在有效数据");
					renderJSON(json.toString());
				}
			} catch (Exception e) {
				play.Logger.error(e, "API请求异常");
				JsonObject json = new JsonObject();
				json.addProperty("code", 500);
				json.addProperty("message", "API异常");
				renderJSON(json.toString());
			}
		} else {
			JsonObject json = new JsonObject();// 没有数据导出
			json.addProperty("code", 110);
			json.addProperty("message", "不存在有效数据");
			renderJSON(json.toString());
		}
	}

	/**
	 * excel解析导出路径
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 下午2:35:14
	 */
	public static List<Params> explainPath(String param) {
		if (param == null || param.isEmpty()) {
			return null;
		}

		List<Params> ps = new ArrayList<Params>();
		List<Map<String, Object>> maps = (List<Map<String, Object>>) JSONArray.parse(param);
		for (Map<String, Object> map : maps) {
			if (map.get("path") == null) {
				continue;
			}
			Params p = new Params();
			String path = map.get("path").toString();// API名称
			map.remove("path");
			p.setPath(path);

			if (map.get("project") != null) {
				String project = map.get("project").toString();
				p.setProject(project);
				map.remove("project");
			}
			if (map.get("file_name") != null) {
				p.setFileName(map.get("file_name").toString());
				map.remove("file_name");
			}

			Set<String> paramNames = map.keySet();
			Map<String, String> pn = new TreeMap<String, String>();
			for (String paramName : paramNames) {// 参数转型
				pn.put(paramName, map.get(paramName).toString());
			}
			p.setParam(pn);// 对应API参数
			ps.add(p);
		}

		return ps;
	}

	/**
	 * word文件格式导出
	 *
	 * 用处：文字一分钟
	 * 
	 * @author xuyong
	 * @date 2016年3月2日 下午2:13:37
	 */
	public static void wordExport() {
		String param = request.params.get("data", String.class);// API接口名称以及调用该接口所需参数等参数信息(必传参数)
		String stock_name = request.params.allSimple().get("file_name");// word文件名称(可不传参数)
		List<WordParam> params = explainWordPath(param);// 解析API名称及其参数
		if (params != null && !params.isEmpty()) {
			InputStream in = null;
			File file = null;
			try {
				List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
				for (WordParam para : params) {
					String content = GoGoalAPI.post(para.getPath(), para.getParam(), getClientIp(request));
					Map<String, Object> data = DataFormat.formatDocData(content, para.getPattern(),
							para.getParam().get("keyword").toString());
					if (data == null || data.isEmpty()) {
						continue;
					}
					datas.add(data);
				}
				Map<String, Object> dataMaps = new HashMap<String, Object>();
				dataMaps.put("datas", datas);
				if (params.size() > 0) {
					if (stock_name == null || stock_name.isEmpty()) {
						stock_name = "文字一分钟";
					}
					file = WordExport.createDoc(dataMaps, "texttemplate", stock_name);
					String fileNa = java.net.URLEncoder
							.encode(file.getName() != null ? file.getName() /*+ ".doc"*/ : "文字一分钟.doc", "UTF-8");
					response.setContentTypeIfNotSet("application/msword;charset=UTF-8");
					response.setHeader("Content-disposition", "attachment;filename=" + fileNa);
					in = new FileInputStream(file);
					byte[] b = new byte[1024 * 1024 * 8];
					int length = in.read(b);

					response.out.write(b, 0, length);
				} else {
					JsonObject json = new JsonObject();
					json.addProperty("code", 110);
					json.addProperty("message", "不存在有效数据");
					renderJSON(json.toString());
				}
			} catch (Exception e) {
				play.Logger.error(e, "API请求异常");
				JsonObject json = new JsonObject();
				json.addProperty("code", 500);
				json.addProperty("message", "API异常");
				renderJSON(json.toString());
			} finally {
				if (in != null) {
					try {
						in.close();
						// 删除临时文件
						if (file.exists()) {
							file.delete();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			JsonObject json = new JsonObject();// 没有数据导出
			json.addProperty("code", 110);
			json.addProperty("message", "不存在有效数据");
			renderJSON(json.toString());
		}
	}

	/**
	 * word导出路径解析
	 *
	 * @param param
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月2日 下午2:18:15
	 */
	public static List<WordParam> explainWordPath(String param) {
		if (param == null || param.isEmpty()) {
			return null;
		}

		List<WordParam> ps = new ArrayList<WordParam>();
		List<Map<String, Object>> maps = (List<Map<String, Object>>) JSONObject.parse(param);
		for (Map<String, Object> map : maps) {
			WordParam p = new WordParam();
			String path = map.get("path").toString();// API名称
			map.remove("path");
			p.setPath(path);

			if (map.get("pattern") != null && !map.get("pattern").toString().isEmpty()) {
				p.setPattern(map.get("pattern").toString());// 设置导出时间格式
				map.remove("pattern");
			}

			Set<String> paramNames = map.keySet();
			Map<String, String> pn = new TreeMap<String, String>();
			for (String paramName : paramNames) {// 参数转型
				pn.put(paramName, map.get(paramName).toString());
			}
			p.setParam(pn);// 对应API参数
			ps.add(p);
		}

		return ps;
	}

	/**
	 * 上传用户头像
	 * 
	 * @param tempFile
	 * @return
	 */
	public static void uploadHead(String token, File file, String clientIp) throws IOException {
		JsonObject json = new JsonObject();
		if (token == null || file == null) {
			json.addProperty("code", 400);
			json.addProperty("message", "错误的请求参数【必填参数不完整，缺少以下参数：token或file】");
			renderJSON(json);
		}
		FtpUtilSeven ftp = new FtpUtilSeven(ImageInfo.URL_HEAD, ImageInfo.PORT_HEAD, ImageInfo.OPERATOR_NAME_HEAD,
				ImageInfo.OPERATOR_PWD_HEAD);
		String ftpFileName = ftp.upload(file);

		if (ftpFileName == null) {
			json.addProperty("code", 1);
			json.addProperty("message", "头像上传失败");
			renderJSON(json);
		} else {
			String apiName = "v1/user/edit";
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", token);
			params.put("avatar", ftpFileName);
			try {
				renderJSON(GoGoalAPI.post(apiName, params, getClientIp(request)));
			} catch (Exception e) {
				json.addProperty("code", 500);
				json.addProperty("message", "API异常");
				renderJSON(json.toString());
			}
		}
	}
}
