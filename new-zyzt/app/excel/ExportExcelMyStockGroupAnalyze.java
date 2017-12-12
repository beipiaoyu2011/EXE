package excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.Region;

import entity.DataFormat;

/**
 * 导出自选股组合分析收益统计
 * 
 * @version 1.0
 * @since JDK1.7
 * @author zhougang
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2017 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2017年2月24日 下午2:48:09
 */
public class ExportExcelMyStockGroupAnalyze {
	/**
	 * 自选股收益统计时导出特殊表格
	 *
	 * @param workbook
	 * @param fileName
	 * @param dataset
	 * @param headerList
	 * 
	 * @author zhougang
	 * @date 2017年2月24日 下午1:42:43
	 */
	public static void exportExcel(HSSFWorkbook workbook, String fileName, List<Map<String, Object>> dataset, 
			List<String> headerList) {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(fileName);
		sheet.setDefaultColumnWidth(20);
		HSSFCellStyle styleTitle = ExportExcel.getTitleStyle(workbook);
		
		List<String> headerNewList = new ArrayList<String>();
		//表头占2行
		HSSFRow row = sheet.createRow(0);
		HSSFRow row2 = sheet.createRow(1);
		for (int i = 0; i < headerList.size(); i++) {// i是headers的索引，n是Excel的索引
			String header = headerList.get(i);
			if ("general_analyze".equals(header) || "type".equals(header)) {
				continue;
			}
			HSSFCell cell1 = row.createCell(i);
			HSSFCell cell2 = row2.createCell(i);
			cell1.setCellStyle(styleTitle);//设置表头样式
			cell2.setCellStyle(styleTitle);
			
			sheet.addMergedRegion(new Region(0, (short) i, 1, (short) i));
			
			cell1.setCellValue(header);
			headerNewList.add(header);
		}
		
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> stockList = null;
		List<Map<String, Object>> etfsList = null;
		List<Map<String, Object>> bondList = null;
		for (Map<String, Object> map : dataset) {
			if (map.get("general_analyze") != null && map.get("general_analyze") instanceof Map) {
				Map<String, Object> general = (Map<String, Object>) map.get("general_analyze");
				//合计
				int sumCount = Integer.parseInt(general.get("sum_count").toString());
				if (sumCount > 0) {
					String sumYieldRate = DataFormat.getFormatDoubleValue(general.get("sum_yield_rate"), "0.00%").toString();
					String sumExcessYieldRate = DataFormat.getFormatDoubleValue(general.get("sum_excess_yield_rate"), "0.00%").toString();
					
					List<Map<String, Object>> sumList = new ArrayList<Map<String, Object>>();
					Map<String, Object> sumMap = new HashMap<String, Object>();
					sumMap.put("count", sumCount);
					sumMap.put("收益率", sumYieldRate == null ? "--" : sumYieldRate);
					sumMap.put("超额收益率", sumExcessYieldRate == null ? "--" : sumExcessYieldRate);
					sumList.add(sumMap);
					result.put("sum", sumList);
				}
				
				//股票
				int stockCount = Integer.parseInt(general.get("stock_count").toString());
				if (stockCount > 0) {
					if (stockList == null) {
						stockList = new ArrayList<Map<String, Object>>();
					}
					String stockYieldRate = DataFormat.getFormatDoubleValue(general.get("stock_yield_rate"), "0.00%").toString();
					String stockExcessYieldRate = DataFormat.getFormatDoubleValue(general.get("stock_excess_yield_rate"), "0.00%").toString();
					
					Map<String, Object> stockMap = new HashMap<String, Object>();
					stockMap.put("count", stockCount);
					stockMap.put("收益率", stockYieldRate == null ? "--" : stockYieldRate);
					stockMap.put("超额收益率", stockExcessYieldRate == null ? "--" : stockExcessYieldRate);
					stockList.add(0, stockMap);
				}
				
				//基金
				int etfsCount = Integer.parseInt(general.get("etfs_count").toString());
				if (etfsCount > 0) {
					if (etfsList == null) {
						etfsList = new ArrayList<Map<String, Object>>();
					}
					
					String etfsYieldRate = DataFormat.getFormatDoubleValue(general.get("etfs_yield_rate"), "0.00%").toString();
					String etfsExcessYieldRate = DataFormat.getFormatDoubleValue(general.get("etfs_excess_yield_rate"), "0.00%").toString();
					
					Map<String, Object> etfsMap = new HashMap<String, Object>();
					etfsMap.put("count", etfsCount);
					etfsMap.put("收益率", etfsYieldRate == null ? "--" : etfsYieldRate);
					etfsMap.put("超额收益率", etfsExcessYieldRate == null ? "--" : etfsExcessYieldRate);
					etfsList.add(0, etfsMap);
				}
				
				//债券
				int bondCount = Integer.parseInt(general.get("bond_count").toString());
				if (bondCount > 0) {
					if (bondList == null) {
						bondList = new ArrayList<Map<String, Object>>();
					}
					String bondYieldRate = (String) DataFormat.getFormatDoubleValue(general.get("bond_yield_rate"), "0.00%").toString();
					String bondExcessYieldRate = (String) DataFormat.getFormatDoubleValue(general.get("bond_excess_yield_rate"), "0.00%").toString();
					
					Map<String, Object> bondMap = new HashMap<String, Object>();
					bondMap.put("count", bondCount);
					bondMap.put("收益率", bondYieldRate == null ? "--" : bondYieldRate);
					bondMap.put("超额收益率", bondExcessYieldRate == null ? "--" : bondExcessYieldRate);
					bondList.add(0, bondMap);
				}
				map.remove("general_analyze");
			}
			
			if (map.get("type") == null || map.get("type").toString().equals("--")) {
				continue;
			}
			
			int type = Integer.parseInt(map.get("type").toString());
			
			if (type == 1 || type == 5) {
				if (stockList == null) {
					stockList = new ArrayList<Map<String, Object>>();
				}
				stockList.add(map);
			} else if (type == 3) {
				if (etfsList == null) {
					etfsList = new ArrayList<Map<String, Object>>();
				}
				etfsList.add(map);
			} else if (type == 4) {
				if (bondList == null) {
					bondList = new ArrayList<Map<String, Object>>();
				}
				bondList.add(map);
			}
		}
		result.put("stock", stockList);
		result.put("etfs", etfsList);
		result.put("bond", bondList);
		
		//开始产生数据
		List<Map<String, Object>> sumLists = result.get("sum");
		//表头下面一行 --->合计
		if (sumLists != null) {
			setHSSFRowAnalyze(workbook, sheet, "合计", sumLists, headerNewList);
		}
		
		//接下来股票
		List<Map<String, Object>> stockLists = result.get("stock");
		if (stockLists != null) {
			setHSSFRowAnalyze(workbook, sheet, "股票", stockLists, headerNewList);
		}
		
		//基金
		List<Map<String, Object>> etfsLists = result.get("etfs");
		if (etfsLists != null) {
			setHSSFRowAnalyze(workbook, sheet, "基金", etfsLists, headerNewList);
		}
		
		//债券
		List<Map<String, Object>> bondLists = result.get("bond");
		if (bondLists != null) {
			setHSSFRowAnalyze(workbook, sheet, "债券", bondLists, headerNewList);
		}
	}
	
	/**
	 *	生成自选股组合分析中特殊行
	 *
	 * @param workbook
	 * @param sheet
	 * @param cell1		合计(10), 股票(3), 基金(3), 债券(4)
	 * @param dataMap	key : 对应的中文列明	value : 这一列的值
	 * @param headerList	表头
	 * @return
	 * 
	 * @author zhougang
	 * @date 2017年2月22日 下午4:47:46
	 */
	public static void setHSSFRowAnalyze(HSSFWorkbook workbook, HSSFSheet sheet, String cell1Value,
											List<Map<String, Object>> list, List<String> headerList) {
		int currRowCount = sheet.getLastRowNum() + 1;
		HSSFRow currRow = sheet.createRow(currRowCount);
		HSSFCell cell1 = currRow.createCell(0);
		
		HSSFCellStyle style = ExportExcel.getCellStyle(workbook);
		cell1.setCellStyle(style);
		//第一列显示
		Map<String, Object> dataMap = list.get(0);
		
		cell1.setCellValue(cell1Value + "(" + dataMap.get("count") + ")");
		//获取单元格内容样式
		for (int i = 0; i < headerList.size(); i++) {
			String headerValue = headerList.get(i);
			if (!"收益率".equals(headerValue) && !"超额收益率".equals(headerValue)) {
				continue;
			}
			CellRangeAddress region = null;
			if ("收益率".equals(headerValue)) {
				region = new CellRangeAddress(currRowCount, currRowCount, 1, i - 1);
				sheet.addMergedRegion(region);//合并到收益率前一列单元格
			}
			if ("超额收益率".equals(headerValue)) {
				region = new CellRangeAddress(currRowCount, currRowCount, i + 1, headerList.size() - 1);  
				sheet.addMergedRegion(region);//超额收益率后一列合并到最后
			}
			//为合并后的单元格设置边框样式
			for (int j = region.getFirstRow(); j <= region.getLastRow(); j++) {
				HSSFRow row = HSSFCellUtil.getRow(j, sheet);
		        for (int k = region.getFirstColumn(); k <= region.getLastColumn(); k++) {
		        	HSSFCell singleCell = HSSFCellUtil.getCell(row, (short)k);
		            singleCell.setCellStyle(style);
		        }
		    }
			
			Object value = dataMap.get(headerValue);
			HSSFCell cell = currRow.createCell(i);//创建列明相对应的这一列
			cell.setCellStyle(style);//设置内容样式
			cell.setCellValue(value.toString());
		}
		
		for (int i = 1; i < list.size(); i++) {
			Map<String, Object> t = list.get(i);
			currRowCount = sheet.getLastRowNum() + 1;
			currRow = sheet.createRow(currRowCount);
			for (int j = 0; j < headerList.size(); j++) {
				String header = headerList.get(j);
				if ("分类".equals(header)) {
					HSSFCell cell = currRow.createCell(j);
					cell.setCellStyle(style);
					continue;
				}
				HSSFCell cell = currRow.createCell(j);
				cell.setCellStyle(style);
				try {
					Object value = t.get(headerList.get(j));
					if (value == null) {
						value = "--";
					}
					cell.setCellValue(value.toString());//给单元格赋值
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
}
