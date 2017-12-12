package entity;

import java.util.List;
import java.util.Map;

/**
 * 参数模型
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月22日 下午1:19:47
 */
public class Params {

	private String path;//请求路径
	
	private Map<String, String> param;//请求参数
	
	private Integer status;//表头状态
	
	private String fileName;//导出文件名
	
	private String pattern;//时间格式
	
	private List<Map<String, Object>> data;//数据域

	private String project;//请求表头信息
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}
	
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


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

	
	public Integer getStatus() {
		return status;
	}

	
	public void setStatus(Integer status) {
		this.status = status;
	}
}
