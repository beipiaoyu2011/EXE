package entity;

import java.util.List;

/**
 * 
 * 数据中心--自主报表
 * 
 * @version 1.0
 * @since JDK1.7
 * @author fanjj
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月15日 下午3:05:20
 */
public class Report extends Params {

	/**
	 * 待下载的excel表头
	 */
	private List<String> headers;
	
	/**
	 * 表格数据
	 */
	private List<List<Object>> reportData;

	public List<List<Object>> getReportData() {
		return reportData;
	}
	
	public void setReportData(List<List<Object>> reportData) {
		this.reportData = reportData;
	}

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}
	
}
