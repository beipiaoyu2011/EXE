package excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import com.alibaba.fastjson.JSONObject;

import entity.Report;

/**
 * 
 * 自主报表--导出
 * 
 * @version 1.0
 * @since JDK1.7
 * @author fanjj
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月15日 下午8:06:57
 */
@SuppressWarnings("deprecation")
public class ExportReportExcel {

	/**
	 * 
	 * title：sheet页名称
	 * headers：标题栏数组，支持双行标题
	 * dataset：具体要在Excel中显示的数据链表
	 * out：输出路径
	 * 
	 * @author fanjj
	 * @date 2016年3月15日 下午8:08:04
	 */
	public static void exportExcel(HSSFWorkbook workbook,String title, String[] headers, List<List<Object>> dataset, OutputStream out) {
		// 声明一个工作薄
		//HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("--"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("这里是设置作者");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		HSSFRow row2 = sheet.createRow(1);
		for(int i = 0, n = 0; i < headers.length; i++){//i是headers的索引，n是Excel的索引
			HSSFCell cell1 = row.createCell(n);
			cell1.setCellStyle(style);
			HSSFRichTextString text = null;
			if(headers[i].contains(":")){//双标题
				String[] temp = headers[i].split(":");
				text = new HSSFRichTextString(temp[0]);
				String[] tempSec = temp[1].split(",");
				sheet.addMergedRegion(new Region(0, (short)n, 0, (short) (n + tempSec.length -1)));//new CellRangeAddress( 0,  n,  0, (n + tempSec.length -1) )
				int tempI = n;
				for(int j = 0; j < tempSec.length -1; j++){
					HSSFCell cellT = row.createCell(++tempI);
					cellT.setCellStyle(style);
				}
				for(int j = 0; j < tempSec.length; j++){
					HSSFCell cell2 = row2.createCell(n++);
					cell2.setCellStyle(style);
					cell2.setCellValue(new HSSFRichTextString(tempSec[j]));	
				}
			}else{//单标题
				HSSFCell cell2 = row2.createCell(n);
				cell2.setCellStyle(style);
				text = new HSSFRichTextString(headers[i]);
				sheet.addMergedRegion(new Region(0, (short)n, 1, (short)n));
				n++;
			}
			cell1.setCellValue(text);
		}
		// 遍历集合数据，产生数据行
		for (int i = 0, index = 2; i < dataset.size(); i++, index++) {
			row = sheet.createRow(index);
			for (int j = 0; j < (/*(ArrayList)*/dataset.get(i)).size(); j++) {
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style2);
				String temp = dataset.get(i).get(j) == null?"--":dataset.get(i).get(j)+"";
				HSSFRichTextString richString = new HSSFRichTextString(temp);
				cell.setCellValue(richString);
			}
		}
		/*try{
			workbook.write(out);
		}catch(IOException e){
			e.printStackTrace();
		}*/
	}
	
	/**
	 * excel解析导出路径 
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 下午2:35:14
	 */
	public static List<Report> explainPath(String param){
		if(param == null || param.isEmpty()){
			return null;
		}
		
		List<Report> ps = new ArrayList<Report>();
		List<Map<String, Object>> maps = (List<Map<String, Object>>) JSONObject.parse(param);
		for (Map<String, Object> map : maps) {
			Report p = new Report();
			String path = map.get("version") + "/" + map.get("namespace") + "/" + map.get("method");//API接口名称
			map.remove("version");
			map.remove("namespace");
			map.remove("method");
			p.setPath(path);
			if(map.get("file_name") != null){
				p.setFileName(map.get("file_name").toString());//设置导出文件名
				map.remove("file_name");				
			}
			if(map.get("pattern") != null){
				p.setPattern(map.get("pattern").toString());//设置导出时间格式
				map.remove("pattern");				
			}
			
			Set<String> paramNames = map.keySet();
			Map<String, String> pn = new TreeMap<String, String>();
			for (String paramName : paramNames) {//参数转型
				pn.put(paramName, map.get(paramName).toString());
			}
			p.setParam(pn);//对应API参数
			ps.add(p);
		}
		
		return ps;
	}
	
	/**
	 * 
	 * 处理API接口返回值
	 *
	 * @param text
	 * @return
	 * 
	 * @author fanjj
	 * @date 2016年3月16日 上午9:59:32
	 */
	public static List<List<Object>> formatData(String text) {
		if(text == null){
			return null;
		}
		
		return stringToResult(text);//获得数据域
	}
	
	/**
	 * 
	 * 获取API接口返回值中需要的数据
	 *
	 * @param data
	 * @return
	 * 
	 * @author fanjj
	 * @date 2016年3月16日 上午10:02:20
	 */
	public static List<List<Object>> stringToResult(String data) {
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
		
		List<List<Object>> result = null;//获得数据域
		if(newData != null) {
			if(newData.get("data") == null){
				return null;
			}
			Object obj2 = JSONObject.parse(newData.get("data").toString());
			if(obj2 != null){
				result = (List<List<Object>>) ((JSONObject) obj2).get("index");				
			}
		}
		return result;
	}
	
}
