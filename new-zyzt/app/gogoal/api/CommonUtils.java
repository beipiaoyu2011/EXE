package gogoal.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * 
 * 通用工具类
 * 
 * @version 1.0
 * @since JDK1.7
 * @author weijs
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年9月22日 下午3:34:35
 */
public class CommonUtils {

    /**
     * 
     * 从url里获取参数值
     *
     * @param url
     * @return
     * 
     * @author weijs
     * @date 2016年9月22日 下午7:13:34
     */
    public static Map<String, String> getUrlParams(String url) {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	if(StringUtils.isNotBlank(url)){
    		String[] params = url.split("&");
    		for (String param : params) {
    			String[] entry = param.split("=");
    			if (entry.length != 2) {
    				continue;
    			}
    			paramMap.put(entry[0], entry[1]);
    		}
    	}
        return paramMap;
    }
    
}
