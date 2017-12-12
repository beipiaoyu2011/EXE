package gogoal.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoGoalUtil {
	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取股票相关新闻
	 * 
	 * @param stockCode
	 * @return
	 */
	public List<Map<String, String>> getStockAboutNews(String stockCode) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		// TODO 查库获取新闻
		return result;
	}

}
