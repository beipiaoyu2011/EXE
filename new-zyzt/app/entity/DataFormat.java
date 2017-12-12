package entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ThisExpression;

import util.DateUtil;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import constant.Constant;
import constant.InvokeConstant;

/**
 * 数据格式化
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月19日 上午11:08:02
 */
public class DataFormat {
	
	private static Map<Integer, String> symbol_map = new HashMap<Integer, String>();
		
	static{
		symbol_map.put(0, "--");
		symbol_map.put(1, "→");
		symbol_map.put(2, "↑");
		symbol_map.put(3, "→");
		symbol_map.put(4, "√");
		symbol_map.put(5, "☆");
	}
	

	/**
	 * 
	 * 格式化数据
	 *
	 * @param text
	 * @param project：数据key与表头的映射
	 * @param pattern：导出的时间格式
	 * @return
	 * 
	 * @author lisl
	 * @date 2016年5月24日 下午5:23:16
	 */
	public static List<Map<String, Object>> formatData(List<String> texts, Map<String, Object> project, String pattern , List<String> apiNameList) {
		
		if(project==null || project.isEmpty() || texts==null || texts.isEmpty()){
			return null;
		}
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for(String text : texts){
			
			List<Map<String, Object>> infoData = stringToResult(text , apiNameList);//获得数据域
			if(infoData == null || infoData.size() == 0){
				return null;
			}
			
			Set<String> keys = project.keySet();
			for (Map<String, Object> map : infoData) {
				Map<String, Object> m = new HashMap<String, Object>();
				for(String key : keys){
					if(key.indexOf(".") != -1){
						String [] keyArr = key.split("\\.");
						Map<String, Object> tmpMap = null;
						List<Object> tmpList = null;
						for (int i = 0; i < keyArr.length; i++) {
							String k = keyArr[i];
							if(i == 0){
								if(map.get(k) instanceof List){
									tmpList = (List<Object>) map.get(k);
								}else{
									tmpMap = (Map<String, Object>) map.get(k);
								}
							}else if(i == keyArr.length - 1){
								if(tmpList != null){
									for(Object o : tmpList){
										if(o instanceof Map){
											Map<String, Object> tmpMap2 = (Map<String, Object>) o;
											setVal(m, project.get(key).toString(), tmpMap2, pattern,k,apiNameList);
										}
									}
								}else{
									setVal(m, project.get(key).toString(), tmpMap, pattern,k,apiNameList);
								}
							}else{
								tmpMap = (Map<String, Object>) tmpMap.get(k);
							}
						}
					}else{
						setVal(m, project.get(key).toString(), map, pattern,key,apiNameList);
					}
				}
				
				data.add(m);
			}
		}
		return data;
		
	}
	
	/**
	 * 
	 * 格式化val
	 *
	 * @param value
	 * @param pattern
	 * @param dKey  //API数据字段 
	 * @param hKey  //表头
	 * @return
	 * 
	 * @author lisl
	 * @date 2016年5月24日 下午6:39:51
	 */
	public static void setVal(Map<String, Object> map, String hKey, Map<String, Object> result, String pattern,String dKey,List<String> apiNameList) {
		Object value = null;
		if(result == null || result.isEmpty() || result.get(dKey) == null || result.get(dKey).toString().isEmpty()){//格式化空值
			value = "--";
		} else {
			value = result.get(dKey);
			Date time = DateUtil.stringToDate(value.toString(), "yyyy-MM-dd HH:mm:ss");
			if(time != null){//格式化日期格式
				if(pattern == null){
					pattern = "yyyy-MM-dd";
				}
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				value = sdf.format(time);
			} else {
				Object valueTmp = value;
				value = dataSpecialHand(apiNameList, dKey, result);
				if (valueTmp.equals(value) && !(value instanceof String) && !(value instanceof Integer)) {
					value = getFormatDoubleValue(value, "0.00");
				}
			}
		}
		
		if(map.get(hKey) == null){
			map.put(hKey, value);
		}else{
			map.put(hKey, map.get(hKey) + "," + value);
		}
		 
	}
	
	/**
	 * 
	 * 对数据格式的特殊处理
	 *
	 * @return
	 * 
	 * @author fanjj
	 * @date 2016年6月13日 下午8:04:23
	 */
	public static Object dataSpecialHand(List<String> apiNameList , String key , Map<String, Object> result){
		Object value = result.get(key);
		if(apiNameList == null || key == null || value == null|| "--".equals(value.toString())){
			return "--";
		}
		String str = "";
		Integer id;
		for(String api : apiNameList){
			switch (api) {
				case "v1/report/search_user_report":
				case "v1/report/search_report_by_customization":
				case "v1/report/search":
				case "v1/mystock/get_report":
				case "v1/focus_topic/search_company_report":
					// 研报--按报告类型研究
					if ("change_rate".equals(key) || "week_change_rate".equals(key)) {// change_rate:涨幅；week_change_rate：周涨幅
						value = getFormatDoubleValue(value, "0.00%");
					}
					if ("sw_first_name".equals(key)) {// sw_first_name:所属行业(行业名称)
						Object sw_first_name = value;
						if (sw_first_name != null) {
							List<String> ts = (List<String>) sw_first_name;
							if (ts.isEmpty()) {
								value = getSymbolValue(0);
							} else {
								String tagStr = "";
								for (int i = 0; i < ts.size() - 1; i++) {
									tagStr = tagStr + ts.get(i) + ",";
								}
								tagStr = tagStr + ts.get(ts.size() - 1);
								value = tagStr;
							}
						}
					}
					if ("recommend".equals(key)) {
						if (value.toString().equals("1")) {
							value = getSymbolValue(4);
						} else {
							value = getSymbolValue(0);
						}
					}
					if ("core".equals(key)) {
						if (value.toString().equals("1")) {
							value = getSymbolValue(5);
						} else {
							value = getSymbolValue(0);
						}
					}
					if ("profit_rate".equals(key) || "income_rate".equals(key) || "target_price_rate".equals(key)
							|| "score_rate".equals(key)) {
						value = getPointValue(value);
					}
					break;
				case "v1/myfavorreport/search_myfavorreports":
					if ("recommend".equals(key)) {// 是否推荐
						if (value.toString().equals("1")) {
							value = getSymbolValue(4);
						} else {
							value = getSymbolValue(0);
						}
					}
					if ("core".equals(key)) {// 是否核心研究员
						if (value.toString().equals("1")) {
							value = getSymbolValue(5);
						} else {
							value = getSymbolValue(0);
						}
					}
					break;
				case "v1/mystock/mystock_list":// 我的自选股
				case "v1/mystock/get_system_group_list":// 自选股系统分组MSCI 222
					Object symbolTypeObj = result.get("symbol_type");
					Object sourceObj = result.get("source");
					str = "price;high_price;low_price;open_price;change_value;close_price";
					if (str.contains(key) && sourceObj != null && symbolTypeObj != null) {
						String symbolType = symbolTypeObj.toString();
						String source = sourceObj.toString();
						if ("3".equals(symbolType) 
								|| "4".equals(symbolType)
								|| ("5".equals(symbolType) && "sh".equals(source))
								|| ("1".equals(symbolType) && "hk".equals(source))) {// 基金，债券，上证B股
								value = getFormatDoubleValue(value, "0.000");
								try {
									if (!"change_value".equals(key) && Double.parseDouble(value.toString().trim()) == 0d) {
										value = getSymbolValue(0);
									}
								} catch (Exception e) {
									
								}
						}
					}
					if ("volume".equals(key)) {// 成交量(先除100得 “手”单位，再除以10000)
						if ((Double.parseDouble(value.toString().trim()) / 100) >= 0d && ((Double.parseDouble(value.toString().trim()) / 100)) <= 10000d){
							value = getFormatDoubleValue(value, "0.01/0.00");
						} else {
							value = getFormatDoubleValue(value, "0.000001/0.00万");
						}
						
					}
					if ("turnover".equals(key) || "capitalization".equals(key)) {// 成交额, 总市值
						if (Double.parseDouble(value.toString().trim()) >= 0d && Double.parseDouble(value.toString().trim()) <= 10000d) {
							value = getFormatDoubleValue(value, "0.00万");
						} else if (Double.parseDouble(value.toString().trim()) >= 10000d){
							value = getFormatDoubleValue(value, "0.0001/0.00亿");
						} else {
							value = getFormatDoubleValue(value, "0.00");
						}						
					}
					if ("change_rate".equals(key)) {// change_rate：涨跌幅
						value = getFormatDoubleValue(value, "0.01/0.00%");
					}
					if ("turnover_rate".equals(key)) {// turnover_rate：换手率
						value = getFormatDoubleValue(value, "0.00%");
					}
					break;
					
				case "v1/mystock/get_mystock_alteration_record" :
					if ("type".equals(key)) {
						int type = (Integer)value;
						if (type == 0) {
							value = "调出组合";
						} else if (type == 1) {
							value = "调入组合";
						}
					}
					if ("insert_price".equals(key) || "delete_price".equals(key)) {
						int type = (Integer)result.get("code_type");
						if (type == 5) {
							//上海B股保留3位小数点
							String stockCode = result.get("stock_code").toString();
							if (stockCode.matches("^900.*")) {
								value = getFormatDoubleValue(value, "0.000");
							}
						}
						if (type == 3 || type == 4) {
							value = getFormatDoubleValue(value, "0.000");
						}
					}
					break;
				case "v1/mystock/get_mystock_group_analyze" :
					if ("yield_rate".equals(key) || "excess_yield_rate".equals(key) 
							|| "interval_max_yield".equals(key) || "interval_max_retracement".equals(key)) {
						value = getFormatDoubleValue(value, "0.00%");
					}
					
					if ("select_price".equals(key) || "price".equals(key)) {
						int type = (Integer)result.get("type");
						if (type == 5) {
							//上海B股保留3位小数点
							String stockCode = result.get("stock_code").toString();
							if (stockCode.matches("^900.*")) {
								value = getFormatDoubleValue(value, "0.000");
							}
						}
						if (type == 3 || type == 4) {
							value = getFormatDoubleValue(value, "0.000");
						}
					}
					break;
				case "v1/eg/newmystocklist":// 寻宝--寻宝池
					if ("rate".equals(key) || "max_rate".equals(key) || "max_retrat_rate".equals(key)) {
							value = getFormatDoubleValue(value, "0.00%");
					}
					break;
				case "v1/treasure/summary_list":// 寻宝--寻宝总结
					if (!"stock_code".equals(key) && !"total_num".equals(key) && value.toString().matches("^-?\\d+$")) {
						value = Double.parseDouble(value.toString());
					}
					if ("before_overincome3m".equals(key) || "before_overincome1m".equals(key)
							|| "before_overincome1w".equals(key) || "later_overincome1w".equals(key)
							|| "new_rate".equals(key) || "max_rate".equals(key)) {
							value = getFormatDoubleValue(value, "0.00%");
					}
					break;
				case "v1/treasure/get_summary_quarter_report":// 寻宝--寻宝总结(新版，各季度寻宝数据)
					if(value.toString().indexOf("E") != -1 || value.toString().indexOf("e") != -1){
						value = String.format("%.6f", Double.parseDouble(String.valueOf(value)));
					}
					if (!"stock_code".equals(key) && !"radar_count".equals(key) && value.toString().matches("^-?\\d+$")) {
						value = Double.parseDouble(value.toString());
					}
					if ("cal_rate_1m".equals(key) || "new_rate".equals(key)
							|| "cal_rate_a1w".equals(key) || "cal_rate_3m".equals(key)
							|| "max_rate".equals(key) || "cal_rate_a3m".equals(key) || "cal_rate_a1m".equals(key) 
							|| "cal_rate_1w".equals(key) || "cal_rate_a6m".equals(key)) {
							value = getFormatDoubleValue(value, "0.00%");
					}
					break;	
				case "v1/theme/get_stock_pool":// 主题
				case "v1/theme/search_theme":// 主题
					if (!"stock_code".equals(key) && !"report_count".equals(key) && !"news_count".equals(key)
							&& value.toString().matches("^-?\\d+$")) {
						value = Double.parseDouble(value.toString());
					}
					if ("tcp".equals(key)) {
						value = getFormatDoubleValue(value, "0.0001/0.00");
					}
					if ("y1pe".equals(key) || "y2pe".equals(key)) {
						Double tmpVal = Double.parseDouble(value.toString());
						if (tmpVal > 100 || tmpVal <= 0) {
							value = 100;
						}
						value = getFormatDoubleValue(value, "0.00");
					}
					break;
				case "v1/good_company/get_summarize":// 我的好公司收益率、期间最大收益率、期间最大回撤、超额收益率转换100%
				case "v1/good_company/get_my_good_company":// 好公司总结
					str = "yield;max_yield;withdraw;excess_yield;before_w1_yield_rate;before_m1_yield_rate;before_m3_yield_rate;"
								+ "after_w1_yield_rate;after_m1_yield_rate;after_m3_yield_rate;after_m6_yield_rate;after_m12_yield_rate;today_yield";
					if (str.contains(key)) {
						value = getFormatDoubleValue(value, "0.00%");
					} else if ("name".equals(key)) {
						id = Integer.parseInt(result.get("_id").toString());
						if (id == -2) {
							value += "(当前)";
						} else if (id == -5) {
							value += "(历史)";
						}
					}
					
					break;
				case "v1/mystock/mydelstock_list" :
					Object symbolTypeDel = result.get("symbol_type");
					Object sourceDel = result.get("source");
					str = "price;change_value;select_price;delete_price";
					if (str.contains(key) && sourceDel != null && symbolTypeDel != null) {
						String symbolType = symbolTypeDel.toString();
						String source = sourceDel.toString();
						if ("3".equals(symbolType) 
								|| "4".equals(symbolType)
								|| ("5".equals(symbolType) && "sh".equals(source))
								|| ("1".equals(symbolType) && "hk".equals(source))) {// 基金，债券，上证B股
								value = getFormatDoubleValue(value, "0.000");
								try {
									if (!"change_value".equals(key) && Double.parseDouble(value.toString().trim()) == 0d) {
										value = getSymbolValue(0);
									}
								} catch (Exception e) {
									
								}
						}
					}
					if ("change_rate".equals(key)) {// change_rate：涨跌幅
						value = getFormatDoubleValue(value, "0.01/0.00%");
					}
					if ("del_profit".equals(key)) {// del_profit：删除前收益
						value = getFormatDoubleValue(value, "1.0/0.00%");
					}
					if ("current_profit".equals(key)) {// current_profit：删除后至今收益
						value = getFormatDoubleValue(value, "1.0/0.00%");
					}
					
					break;
				case "v1/select_stock/get_stockbytags_info": 
					str = "roe;npgrt;pe;pb;noworgcount;lastorgcount;laddratio;wk1profittb;"
							+ "y1incomezs;y1proiftzs;y3incometb;y3profittb;naddratio;fincometb;fprofittb;c3;"
							+ "wk1profittb;wk4profittb;wk13profittb;wk26profit;wk52profit;fcbtb;fcbtb_1;fcbtb_2;fcbtb_3;"
							+ "hml;profittb;capital_rate;return_equity;freturn_equity";
					String s = "y0free_cash;opercfps;general_assent;tcap_1;mcap;fcb;ccb;current;roe_2;sales_income"
							+ "Profit;fincome;feps;con_eps;cincome;con_np;con_eps_rolling;y1pe";
					String t = "pb_order;peg_order;ps_order;pe_order;cps_order;cpe_order;cpb_order;cpeg_order;pessimism_confidence10"
							+ "pessimism_confidence5;pessimism_confidence15;pessimism_confidence25;pessimism_confidence45;consensus_confidence5"
							+ "consensus_confidence15;consensus_confidence25;consensus_confidence75;consensus_confidence10;optimism_confidence5"
							+ "optimism_confidence15;optimism_confidence25;optimism_confidence75;optimism_confidence10";
					if (str.contains(key)) {
						value = getFormatDoubleValue(value, "1.00/0.00%");
					}
					if(s.contains(key)){
						if(value.toString().matches("^(-?\\d+)(\\.\\d+)?([Ee]?[-+]?\\d+)?$")){
							Double v = Double.parseDouble(value.toString());
							if(v < 10000){
								value = getFormatDoubleValue(value, "0.00");
							}else if(v < 100000000){
								value = getFormatDoubleValue(value, "0.0001/0.00万");
							}else{
								value = getFormatDoubleValue(value, "0.00000001/0.00亿");
							}
						}
					}
					if(t.contains(key)){
						value = getFormatDoubleValue(value, "0.000");
					}
					if("change_rate".contains(key)) {// change_rate：涨跌幅
						value = getFormatDoubleValue(value, "0.01/0.00%");
					}
					break;
				case "v1/select_stock/get_stock_value": 
					str ="y0roe;y1roe;y2roe;y3roe;y0profittb;y1profittb;y2profittb;y3profittb";
					if(str.contains(key)){
						value = getFormatDoubleValue(value, "0.01/0.00%");
					}
					break;
				case "v1/select_stock/get_stock_tags": 
					if("history_achievement_appraise".equals(key)){
						if(value == null){
							value = "无法鉴定";
						}
					}
					if ("over_rate".equals(key)) {// over_rate：当季超额收益率
						value = getFormatDoubleValue(value, "1.0/0.00%");
					}
					if("hold_change".equals(key)){
						if(value != null){
							if(value.toString().contains("-999")){
								value = "--";
							}else{
								value = getFormatDoubleValue(value, "0.01/0.00%");
							}
						}
					}
					if("success_rate".equals(key)){
						if(value == null){
							value = "无法鉴定";
						}else{
							Double rate =Double.parseDouble(value.toString()) ;
							if(rate >= 0.8){
								value = "很高";
							}else if(rate >= 0.7){
								value = "较高";
							}else if(rate >= 0.3){
								value = "中性";
							}else if(rate >= 0.2){
								value = "较低";
							}else{
								value = "很低";
							}
						}
					}
					if("tags".equals(key)){
						if(value == null){
							value = "未鉴定";
						}else{
							Integer type = Integer.parseInt(value.toString());
							switch (type) {
							case 1:
								value = "希望之星";
								break;
							case 2:
								value = "好公司";
								break;
							case 0:
								value = "历史好公司";
								break;
							case -1:
								value = "平凡";
								break;
							default:
								break;
							}
						}
					}
					break;
				default:
					break;
			}
		}
		
		return value;
	}
	
	/**
	 * 
	 * 方法描述 TODO
	 *
	 * @param value
	 * @param format ("[倍数]/[精度][单位];如0.0001/0.00万")
	 * @return
	 * 
	 * @author fengbo
	 * @date 2016年11月30日 下午7:29:31
	 */
	public static Object getFormatDoubleValue(Object value, String format){
		try {
			if(value instanceof Double || value.toString().matches("^(-?\\d+)(\\.\\d+)?([Ee]?[-+]?\\d+)?$")){
				Double valueTmp;
				if (value instanceof Double) {
					valueTmp = (Double)value;
				} else {
					valueTmp = Double.valueOf(value.toString());
				}
				int index = format.indexOf("/");
				if (format.indexOf("/") > 0) {
					valueTmp *= Double.parseDouble(format.substring(0, index));
				}
				DecimalFormat df = new DecimalFormat("##################" + format.substring(index + 1));
				value = df.format(Double.parseDouble(valueTmp.toString()));
			}
		} catch (Exception e) {
			return value;
		}
		return value;
	}
	
	/**
	 *(0, "--");
	 *(1, "→");
	 *(2, "↑");
	 *(3, "→");
	 *(4, "√");
	 *(5, "☆");
	 */
	public static Object getSymbolValue(int key){
		Object symbol = symbol_map.get(key);
		if (symbol == null) {
			symbol = "--";
		}
		return symbol;
	}
	
	public static Object getPointValue(Object value){
		if("1".equals(value.toString())){
			value = "→";
		}else if("2".equals(value.toString())){
			value = "↑";
		}else if("3".equals(value.toString())){
			value = "↓";
		}else{
			value = "--";
		}
		
		return value;
	}
	
	
	/**
	 * 格式化数据
	 *
	 * @param datas	
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 上午11:09:42
	 */
	public static List<Map<String, Object>> formatData(String text, Integer status, String pattern) {
//		if(text == null || status == null){
//			return null;
//		}
		
		List<Map<String, Object>> datas = stringToResult(text);//获得数据域
		if(datas == null || datas.size() == 0){
			return null;
		}
		
		TreeMap<String, Object> field = Constant.fields.get(status);
		String[] titles = (String[]) field.get("titles");
		Map<String, String> invokeTitle = InvokeConstant.getInvokeTitle(status);
		for (Map<String, Object> map : datas) {
//			if(field.get("tags") != null) {//寻宝线索标签拼接
			if(status == 14) {//寻宝线索标签拼接
				Object tags = map.get("tags");
				if(tags != null) {
					List<Map<String, Object>> ts = (List<Map<String, Object>>) tags;
					if(ts.isEmpty()){
						map.put("tags", "--");
					} else {
						String tagStr = "";
						for (int i = 0; i < ts.size() - 1; i++) {
							tagStr = tagStr + ts.get(i).get("name") + ",";
						}
						tagStr = tagStr + ts.get(ts.size() - 1).get("name");
						map.put("tags", tagStr);
					}
				}
			}
			
//			if(field.get("sw_first_name") != null) {//公司报告行业拼接
			if(status == 20 || status == 22 || status == 25) {//公司报告行业拼接
				Object sw_first_name = map.get("sw_first_name");
				if(sw_first_name != null) {
					List<String> ts = (List<String>) sw_first_name;
					if(ts.isEmpty()){
						map.put("sw_first_name", "--");
					} else {
						String tagStr = "";
						for (int i = 0; i < ts.size() - 1; i++) {
							tagStr = tagStr + ts.get(i) + ",";
						}
						tagStr = tagStr + ts.get(ts.size() - 1);
						map.put("sw_first_name", tagStr);
					}
				}
			}
			
			if(field.get("volume") != null) {//成交量处理
				Object volume = map.get("volume");
				map.put("volume", Integer.parseInt(volume.toString()) / 100);
			}
			
			if(status == 7) {//数据中心权重行业复合增长率数据处理
				Object minvalue = map.get("minvalue");
				Object maxvalue = map.get("maxvalue");
				Object startvalue = map.get("startvalue");
				Object endvalue = map.get("endvalue");
				
				if(minvalue != null) {
					map.put("minvalue", Double.parseDouble(minvalue.toString()) / 100d);
				}
				if(maxvalue != null) {
					map.put("maxvalue", Double.parseDouble(maxvalue.toString()) / 100d);
				}
				if(startvalue != null) {
					map.put("startvalue", Double.parseDouble(startvalue.toString()) / 100d);
				}
				if(endvalue != null) {
					map.put("endvalue", Double.parseDouble(endvalue.toString()) / 100d);
				}
			}
			
			List<String> fieldNames = Arrays.asList(titles);
			for (String fieldName : fieldNames) {
				Object value = map.get(invokeTitle.get(fieldName));
				if(value == null){//格式化空值
					map.put(invokeTitle.get(fieldName), "--");
					continue;
				}
				
				Date time = DateUtil.stringToDate(value.toString(), "yyyy-MM-dd HH:mm:ss");
				if(time != null){//格式化日期格式
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					map.put(invokeTitle.get(fieldName), sdf.format(time));
					continue;
				}
				
				if(value.toString().matches("^(\\-|\\+)?[0-9]+\\.[0-9]+$")){
                    //是数字当作double处理
              	   DecimalFormat df = new DecimalFormat("##################0.0000"); 
              	   map.put(invokeTitle.get(fieldName), df.format(Double.parseDouble(value.toString())));
              	   continue;
                } 
				 
				map.put(invokeTitle.get(fieldName), value.toString());
				
			}
		}
		return datas;
	}
	
	
	/**
	 * 结果集字符串转json对象
	 *
	 * @param data	转换字符串
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 上午11:11:59
	 */
	public static List<Map<String, Object>> stringToResult(String data , List<String> apiNameList) {
		if(data == null){
			return null;
		}
		
		Object obj = JSONObject.parse(data);//json格式转化
		if(obj == null){
			return null;
		}
		
		Map<String, Object> newData = null;//转换结果集
		if(obj instanceof Map){
			newData = (Map<String, Object>) obj;
		}
		
		List<Map<String, Object>> result = null;//获得数据域
		if(newData != null) {
			if(newData.get("data") instanceof Map){
				result = new ArrayList<Map<String, Object>>();
				Map<String, Object> dataMap = (Map<String, Object>)newData.get("data");
				for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
					if(entry.getValue() instanceof List){
						List<Map<String,Object>> lList = (List<Map<String,Object>>)entry.getValue();
						result.addAll(lList);
					}
					
					if (entry.getValue() instanceof Map) {
						if (apiNameList.contains("v1/mystock/get_mystock_group_analyze")) {//api为自选股自合分析收益统计时导出数据进行特殊排版.
							Map<String,Object> tempMap = new HashMap<String,Object>();
							tempMap.put("general_analyze", (Map<String,Object>) entry.getValue());
							result.add(tempMap);
						}
					}
				}
				
				//result.add((Map<String, Object>)newData.get("data"));
			}else{
				result = (List<Map<String, Object>>) newData.get("data");
				
				//对【自选股】导出的特殊处理
				if(apiNameList.contains("v1/mystock/mystock_list")){
					for(Map<String, Object> dataMap : result){
						String stockType = dataMap.get("stock_type") == null ? null : dataMap.get("stock_type").toString();
						if("0".equals(stockType)){
							dataMap.put("price", "停牌");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}else if("-1".equals(stockType)){
							dataMap.put("price", "退市");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
						}else if("-2".equals(stockType)){
							dataMap.put("change_value", "未上市");
							dataMap.put("change_rate", "未上市");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}else if("2".equals(stockType)){
							dataMap.put("price", "暂停上市");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}
					}
				} else if(apiNameList.contains("v1/mystock/get_system_group_list")){//自选股系统分组MSCI222
					for(Map<String, Object> dataMap : result){
						String stockType = dataMap.get("stock_type") == null ? null : dataMap.get("stock_type").toString();
						if("0".equals(stockType)){
							dataMap.put("price", "停牌");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}else if("-1".equals(stockType)){
							dataMap.put("price", "退市");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
						}else if("-2".equals(stockType)){
							dataMap.put("change_value", "未上市");
							dataMap.put("change_rate", "未上市");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}else if("2".equals(stockType)){
							dataMap.put("price", "暂停上市");
							dataMap.put("change_value", "0.00");
							dataMap.put("change_rate", "0.00");
							dataMap.put("high_price", "--");
							dataMap.put("low_price", "--");
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 结果集字符串转json对象
	 *
	 * @param data	转换字符串
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 上午11:11:59
	 */
	public static List<Map<String, Object>> stringToResult(String data) {
		if(data == null){
			return null;
		}
		
		Object obj = JSONObject.parse(data);//json格式转化
		if(obj == null){
			return null;
		}
		
		Map<String, Object> newData = null;//转换结果集
		if(obj instanceof Map){
			newData = (Map<String, Object>) obj;
		}
		
		List<Map<String, Object>> result = null;//获得数据域
		if(newData != null) {
			if(newData.get("data") instanceof Map){
				result = new ArrayList<Map<String, Object>>();
				result.add((Map<String, Object>)newData.get("data"));
			}else{
				result = (List<Map<String, Object>>) newData.get("data");
			}
		}
		return result;
	}
	
	/**
	 * 格式化word数据格式
	 *
	 * @param text
	 * @param pattern
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月2日 下午2:33:42
	 */
	public static Map<String, Object> formatDocData(String text, String pattern, String title) {
		if(text == null || title == null || text.isEmpty() || title.isEmpty()){
			return null;
		}
		if(pattern == null || pattern.isEmpty()){
			pattern = "yyyy-MM-dd";
		}
		List<Map<String, Object>> datas = stringToResult(text);//获得数据域
		if(datas == null || datas.size() == 0){
			return null;
		}
		
		Map<String, Object> ds = new HashMap<String, Object>();
		ds.put("title", title);
		List<Map<String, Object>> body = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : datas) {
			Map<String, Object> m = new HashMap<String, Object>();
			Object auths = map.get("authors");
			String author = "";
			if(auths != null) {
				for (Map<String, Object> auth : (List<Map<String, Object>>) auths) {
					author  = author + auth.get("author_name") + "  ";
				}
			}
			m.put("authors", author);
			m.put("organ", map.get("organ_name"));
			m.put("date", map.get("create_date"));
			String summary = null;
			if(map.get("report_summary") != null){
				summary = map.get("report_summary").toString();
				
				//xml中特殊字符转义
				summary = summary.replaceAll("&", "&amp;");
				summary = summary.replaceAll("<", "&lt;");
				summary = summary.replaceAll(">", "&gt;");
				summary = summary.replaceAll("\"", "&quot;");
				summary = summary.replaceAll("\'", "&apos;");
				
				summary = summary.replace("\n\n", "<br><br>");
				int index = summary.indexOf("<br><br>");
				if(index > 0) {
					summary = "<w:r><w:rPr>"
							+ "<w:rFonts w:fareast='宋体' w:hint='fareast'/>"
							+ "<w:b/><w:b-cs/>"
							+ "<w:u w:val='single'/>"
							+ "<w:lang w:val='EN-US' w:fareast='ZH-CN'/>"
							+ "</w:rPr>"
							+ "<w:t>" + summary + "</w:t></w:r>";
					summary = summary.replace("<br><br>", "</w:t></w:r><w:r><w:t>");
				} else {
					summary = "<w:r><w:t>" + summary + "</w:t></w:r>";
				}
				summary = summary.replaceAll(title, 
						"</w:t></w:r><w:r><w:rPr><w:rFonts w:fareast='宋体' w:hint='fareast'/>"
						+ "<w:b/><w:b-cs/>"
						+ "<w:u w:val='single'/>"
						+ "<w:lang w:val='EN-US' w:fareast='ZH-CN'/>"
						+ "</w:rPr>"
						+ "<w:t>" +  title + "</w:t></w:r><w:r><w:t>");
			}
			m.put("summary", summary);
			body.add(m);
		}
		ds.put("body", body);
		
		return ds;
	}
}
