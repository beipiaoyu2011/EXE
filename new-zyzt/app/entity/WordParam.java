package entity;

import java.util.List;
import java.util.Map;

/**
 * word参数数据模板
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月2日 下午2:14:46
 */
public class WordParam {

	private String path;//请求路径
	
	private Map<String, String> param;//请求参数
	
	private String fileName;//导出文件名
	
	private String pattern;//时间格式
	
	private Map<String, Object> data;//数据域

	
	public String getPath() {
		return path;
	}

	
	public void setPath(String path) {
		this.path = path;
	}

	
	public Map<String, String> getParam() {
		return param;
	}

	
	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	
	public String getFileName() {
		return fileName;
	}

	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public String getPattern() {
		return pattern;
	}

	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


	public Map<String, Object> getData() {
		return data;
	}

	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
