package excel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * 数据导出(下载)
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月22日 下午4:51:46
 */
public class ExportExcel {
	
   /**
    * 数据导出
    * 
    * @param title   表格标题名
    * @param headers 表格属性列名数组
    * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
    *                javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
    * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
    *            
    */
	public static void exportExcel(HSSFWorkbook workbook, String fileName, String[] headers,
									List<Map<String, Object>> dataset, List<String> headerList) {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(fileName);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		// 生成一个样式---这个样式用于表头
		HSSFCellStyle styleTitle = getTitleStyle(workbook);
		// ----------------------------------------------------------------------------------------
		// 设置另一个样式--这个样式用于内容
		HSSFCellStyle style = getCellStyle(workbook);
		// ----------------------------------------------------------------------------------------
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		HSSFRow row2 = sheet.createRow(1);
		for (int i = 0, n = 0; i < headers.length; i++) {// i是headers的索引，n是Excel的索引
			HSSFCell cell1 = row.createCell(n);
			cell1.setCellStyle(styleTitle);//设置表头样式
			HSSFRichTextString text = null;
			if (headers[i].contains(":")) {// 双标题
				String[] temp = headers[i].split(":");
				text = new HSSFRichTextString(temp[0]);
				String[] tempSec = temp[1].split(",");
				sheet.addMergedRegion(new Region(0, (short) n, 0, (short) (n + tempSec.length - 1)));
				int tempI = n;
				for (int j = 0; j < tempSec.length - 1; j++) {
					HSSFCell cellT = row.createCell(++tempI);
					cellT.setCellStyle(styleTitle);//设置表头样式
				}
				for (int j = 0; j < tempSec.length; j++) {
					HSSFCell cell2 = row2.createCell(n++);
					cell2.setCellStyle(styleTitle);//设置表头样式
					cell2.setCellValue(new HSSFRichTextString(tempSec[j]));
				}
			} else {// 单标题
				HSSFCell cell2 = row2.createCell(n);
				cell2.setCellStyle(styleTitle);//设置表头样式
				text = new HSSFRichTextString(headers[i]);
				sheet.addMergedRegion(new Region(0, (short) n, 1, (short) n));
				n++;
			}
			cell1.setCellValue(text);
		}
		
		// 遍历集合数据，产生数据行
		Iterator<Map<String, Object>> it = dataset.iterator();
		int index = 1;
		HSSFFont font3 = workbook.createFont();
		// TreeMap<String, String> tt = InvokeConstant.getInvokeTitle(status);
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Map<String, Object> t = it.next();
			for (int i = 0; i < headerList.size(); i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);//设置内容样式
				try {
					Object value = t.get(headerList.get(i));
					if (value == null) {
						value = "--";
					}
					
					HSSFRichTextString richString = new HSSFRichTextString();
					richString.applyFont(font3);
					cell.setCellValue(value.toString());//给单元格赋值
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					cell.setCellValue("数据过多，excel(最大处理量)无法处理！");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取excel单元格样式
	 */
	public static HSSFCellStyle getCellStyle(HSSFWorkbook workbook) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 11);// 设置字体大小
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font2);
		return style;
	}
	
	/**
	 * 获取excel表头的样式
	 */
	public static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
		HSSFCellStyle styleTitle = workbook.createCellStyle();
		// 设置这些样式
		styleTitle.setFillForegroundColor((short) 13);// 设置背景色
		styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 设置边框
		styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框
		styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 上边框
		styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 右边框
		// 设置居中
		styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 居中
		// 设置字体
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		// font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 13);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		// 把字体应用到当前的样式
		styleTitle.setFont(font);// 选择需要用到的字体格式
		return styleTitle;
	}
}