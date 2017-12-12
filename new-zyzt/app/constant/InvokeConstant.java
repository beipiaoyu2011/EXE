package constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import util.IndexUtil;

/**
 * 标题反转
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月22日 下午5:12:51
 */
public class InvokeConstant {

	/**
	 * 获得反转标题
	 *
	 * @param status
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:54:35
	 */
	public static TreeMap<String, String> getInvokeTitle(int status) {
		if(status < 0 || status > Constant.fields.size() - 1){
			return null;
		}
		
		TreeMap<String, String> invokeTitle = new TreeMap<String, String>();
		Map<String, String> map = (Map<String, String>) Constant.fields.get(status).get("map");
		Set<String> t = map.keySet();
		String[] ts = (String[]) Constant.fields.get(status).get("titles");
		List<String> titles = Arrays.asList(ts);
		Iterator<String> its = titles.iterator();
		while(its.hasNext()){
			String title = its.next();
			if(title == null){
				continue;
			}
			Iterator<String> is = t.iterator();
			while(is.hasNext()){
				String tl = is.next();
				if(title.toString().equals(map.get(tl))){
					invokeTitle.put(title.toString(), tl);
					break;
				}
			}
		}
		
		return invokeTitle;
	}
}
