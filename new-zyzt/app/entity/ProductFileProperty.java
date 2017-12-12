package entity;

import java.util.List;

/**
 * 产品参数配置
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月19日 上午10:36:49
 */
public  class ProductFileProperty {
	
	public static final String page =  "page";
	public static final String rows =  "rows";
	public static final String file_name =  "file_name";
	public static final String reportType =  "reportType";
	public static final String type = "type";
	public static final String period_prefix =  "period";
	public static final String key =  "key";
	public static final String businesskey =  "businesskey";
	public static final String field =  "field";
	public static final String theader =  "theader";
	public static final String excelfield = "excel_field";
	public static final String reportdownload = "reportdownload";
	/**
	 * 请求本身固有参
	 */
	public static final String body =  "body";
	public static final String version =  "version";
	public static final String namespace =  "namespace";
	public static final String method =  "method";
	public static final String data  = "data";
	public static final String result  = "result";
	public static final String code  = "code";
	public static final String message  = "message";
	
	
	private String FileName;
	
	public void setFileName(String filename) {
		FileName = filename;
	}
	
	public String getFileName(){
		return FileName;
	}
}
