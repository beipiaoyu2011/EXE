package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 业务工具类
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月25日 下午2:01:14
 */
public class IndexUtil {

	/**
	 * 获得基准年
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月25日 下午2:01:31
	 */
	public static int getBaseYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH); // 一月份是0，十二月份是11
		if (month < 4) {
			return year - 2;
		}
		return year - 1;
	}
	
	/**
	 * 替换基准年
	 *
	 * @param title
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月25日 下午1:54:44
	 */
	public static List<String> getRealHeader(String[] headers) {
		if(headers == null || headers.length == 0){
			return null;
		}
		
		List<String> hds = new ArrayList<String>();
		hds.addAll(Arrays.asList(headers));
		List<String> batch = new ArrayList<String>();
		
		for (String head : hds) {
			if(head.contains("y0")){
				head = head.replaceAll("y0", IndexUtil.getBaseYear() + "");
			}
			if(head.contains("y1")){
				head = head.replaceAll("y1", (IndexUtil.getBaseYear() + 1) + "");
			}
			if(head.contains("y2")){
				head = head.replaceAll("y2", (IndexUtil.getBaseYear() + 2) + "");
			}
			batch.add(head);
		}
		return batch;
	}
}
