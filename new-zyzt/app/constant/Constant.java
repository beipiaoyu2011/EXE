package constant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 常量存储区域
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年2月19日 下午1:30:49
 */
public class Constant {
	
	public static List<TreeMap<String, Object>> fields;//API对应域
	
	static {
		fields = new ArrayList<TreeMap<String,Object>>();
		fields.add(getRollPe());//滚动pe
		fields.add(getRollProfits());//滚动净利润
		fields.add(getAbsweight());//获得绝对估值
		fields.add(getRelWeight());//获得相对估值
		fields.add(getProfitBasisRegular());//净利润同比--增减变动
		fields.add(getRejectBankChange());//净利润同比--剔除银行前后变动
		fields.add(getWeightBasic());//获得权重同比增长
		fields.add(getValuerProfits());//权重行业复合同比增长
		fields.add(getWeightBasisRecombination());//沪深300权重行业估值及盈利表
		fields.add(getValuerProfitWeek());//权重行业估值及盈利--周涨幅情况
		fields.add(getWeightProfitTop100());//权重top100成分股盈利预测
		fields.add(getWeightElemShare());//沪深300权重100成分股分红
		
		fields.add(getMystockList());//获得自选股列表 
		fields.add(getReportList());//获得研报中心列表 
		
		fields.add(getTreasureList());//获得寻宝游戏线索列表
		fields.add(getTreasureTag());//获得寻宝游戏我的股票书签
		fields.add(getEgMystockList());//获得我的股票池
		fields.add(getSummaryList());//获得寻宝总结
		
		fields.add(getMyFavourReportList());//获得我的收藏报告列表
		fields.add(getMyCustomizationReportList());//获得我的定制报告
		fields.add(getCompanyReportList());//获得我的公司报告列表
		
		//热点追踪
		fields.add(getHotSpotTraceStock());//相关股票
		fields.add(getHotSpotTraceStockReport());//个股报告
		fields.add(getHotSpotTraceIndustryReport());//行业报告
		fields.add(getHotSpotTraceNews());//新闻
		
		
		fields.add(getIndustryReportList());//获得行业报告列表
	}
	
	/**
	 * 沪深300估值及盈利变化--滚动PE
	 * 
	 * 位置：0
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 下午1:51:16
	 */
	public static TreeMap<String, Object> getRollPe(){
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "统计日期", "PE均值", "滚动PE", "调进", "调出"};
		map.put("titles", titles);
		
		Map<String, String> m = new TreeMap<String, String>();
		m.put("index_code", "指数代码");
		m.put("cdate", "统计日期");
		m.put("avg_pe", "PE均值");
		m.put("pe", "滚动PE");
		m.put("stock_in", "调进");
		m.put("stock_out", "调出");
		map.put("map", m);
		return map;
	}
	
	/**
	 * 滚动净利润
	 * 
	 * 位置：1
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月19日 下午1:53:05
	 */
	public static TreeMap<String, Object> getRollProfits(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "统计日期", "指数价格", "滚动净利润（万元）", "y1年净利润（万元）", "y2年净利润（万元）"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("cdate", "统计日期");
		map.put("indexprice", "指数价格");
		map.put("rprofit", "滚动净利润（万元）");
		map.put("y1profit", "y1年净利润（万元）");
		map.put("y2profit", "y2年净利润（万元）");
		m.put("map", map);
		return m;
	}
	
	/**
	 * 获得绝对估值
	 * 
	 * 位置：2
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午10:33:18
	 */
	public static TreeMap<String, Object> getAbsweight(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "周期", 
				"绝对估值-最低", "绝对估值-最高", "绝对估值-开盘", "绝对估值-收盘"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("period", "周期");
		map.put("abs_minvalue", "绝对估值-最低");
		map.put("abs_maxvalue", "绝对估值-最高");
		map.put("abs_startvalue", "绝对估值-开盘");
		map.put("abs_endvalue", "绝对估值-收盘");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得相对估值
	 * 
	 * 位置：3
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午10:39:34
	 */
	public static TreeMap<String, Object> getRelWeight(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "周期",  
				"相对估值-最低", "相对估值-最高", "相对估值-开盘", "相对估值-收盘"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("period", "周期");
		map.put("rel_minvalue", "相对估值-最低");
		map.put("rel_maxvalue", "相对估值-最高");
		map.put("rel_startvalue", "相对估值-开盘");
		map.put("rel_endvalue", "相对估值-收盘");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 净利润同比--增减变动
	 * 
	 * 位置：4
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午10:44:19
	 */
	public static TreeMap<String, Object> getProfitBasisRegular(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "统计日期", "y1净利润同比", "y2净利润同比"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("cdate", "统计日期");
		map.put("y1profittb", "y1净利润同比");
		map.put("y2profittb", "y2净利润同比");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 净利润同比--剔除银行前后变动
	 * 
	 * 位置：5
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午10:59:32
	 */
	public static TreeMap<String, Object> getRejectBankChange(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "统计日期", "y1年剔除银行前净利润同比", "y1年剔除银行后净利润同比"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("cdate", "统计日期");
		map.put("y1profittb", "y1年剔除银行前净利润同比");
		map.put("revokeprofittb", "y1年剔除银行后净利润同比");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得权重同比增长
	 * 
	 * 位置：6
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:05:29
	 */
	public static TreeMap<String, Object> getWeightBasic(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "周期", 
				"预测年", "最低", "最高", "开盘", "收盘"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("period", "周期");
		map.put("forecastyear", "预测年");
		map.put("minvalue", "最低");
		map.put("maxvalue", "最高");
		map.put("startvalue", "开盘");
		map.put("endvalue", "收盘");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 权重行业复合同比增长
	 * 
	 * 位置：7
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:10:37
	 */
	public static TreeMap<String, Object> getValuerProfits(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "周期", 
				"最低", "最高", "开盘", "收盘"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("period", "周期");
		map.put("minvalue", "最低");
		map.put("maxvalue", "最高");
		map.put("startvalue", "开盘");
		map.put("endvalue", "收盘");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 沪深300权重行业估值及盈利表
	 * 
	 * 位置：8
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:09:17
	 */
	public static TreeMap<String, Object> getWeightBasisRecombination(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "统计日期", 
				"成分股数", "y1年pe", "y2年pe", "y1年pb", "y2年pb", 
				"y1年roe", "y2年roe", "y1年净利润近一周调整幅度", "y1年净利润近一月调整幅度", 
				"y1年净利润近一季调整幅度", "y1年净利润同比", "y2年净利润同比"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("cdate", "统计日期");
		map.put("stockcount", "成分股数");
		map.put("y1pe", "y1年pe");
		map.put("y2pe", "y2年pe");
		map.put("y1pb", "y1年pb");
		map.put("y2pb", "y2年pb");
		map.put("y1roe", "y1年roe");
		map.put("y2roe", "y2年roe");
		map.put("w1peirate", "y1年净利润近一周调整幅度");
		map.put("m1peirate", "y1年净利润近一月调整幅度");
		map.put("q1peirate", "y1年净利润近一季调整幅度");
		map.put("y1profittb", "y1年净利润同比");
		map.put("y2profittb", "y2年净利润同比");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 权重行业估值及盈利--周涨幅情况
	 * 
	 * 位置：9
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:20:01
	 */
	public static TreeMap<String, Object> getValuerProfitWeek(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "行业代码", "行业名称", "统计日期", "涨幅"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("industry_code", "行业代码");
		map.put("industry_name", "行业名称");
		map.put("cdate", "统计日期");
		map.put("raisepct", "涨幅");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 权重top100成分股盈利预测
	 * 
	 *位置：10
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:21:49
	 */
	public static TreeMap<String, Object> getWeightProfitTop100(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "股票代码", "股票名称", "统计日期", 
				"机构覆盖数", "权重","y1年eps", "y2年eps", "y1年pe", "y2年pe", "y1年pb", "y2年pb", 
				"y1年参考roe", "y2年参考roe", "y1年净利润同比", "y2年净利润同比"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("stock_code", "股票代码");
		map.put("stock_name", "股票名称");
		map.put("cdate", "统计日期");
		map.put("organcount", "机构覆盖数");
		map.put("weight", "权重");
		map.put("y1eps", "y1年eps");
		map.put("y2eps", "y2年eps");
		map.put("y1pe", "y1年pe");
		map.put("y2pe", "y2年pe");
		map.put("y1pb", "y1年pb");
		map.put("y2pb", "y2年pb");
		map.put("y1roe", "y1年参考roe");
		map.put("y2roe", "y2年参考roe");
		map.put("y1profittb", "y1年净利润同比");
		map.put("y2profittb", "y2年净利润同比");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 沪深300权重100成分股分红
	 * 
	 * 位置：11
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年2月23日 上午11:27:26
	 */
	public static TreeMap<String, Object> getWeightElemShare(){
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"指数代码", "股票代码", "股票名称", "权重", "y0年年报披露时间", 
				"y0年EPS", "y0年派息率", "y0年每股红利", 
				"y0年股权登记日价格", "y0年股权登记日", "y0年股息率", 
				"现价", "y1年EPS", "y1年派息率", "y1年预期投资分红率", 
				"y1年预计年报披露时间"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("index_code", "指数代码");
		map.put("stock_code", "股票代码");
		map.put("stock_name", "股票名称");
		map.put("weight", "权重");
		map.put("bpublishdate", "y0年年报披露时间");
		map.put("beps", "y0年EPS");
		map.put("binterestrate", "y0年派息率");
		map.put("bbonus", "y0年每股红利");
		map.put("bregisterprice", "y0年股权登记日价格");
		map.put("bregisterdate", "y0年股权登记日");
		map.put("bstockinterestrate", "y0年股息率");
		map.put("currentprice", "现价");
		map.put("teps", "y1年EPS");
		map.put("tinterestrate", "y1年派息率");
		map.put("tbonusrate", "y1年预期投资分红率");
		map.put("tpublishdate", "y1年预计年报披露时间");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得自选股列表
	 *
	 * 位置：12
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月1日 上午10:39:46
	 */
	public static TreeMap<String, Object> getMystockList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"代码", "名称", "最新价", "涨跌额", 
				"涨跌幅(%)", "昨收", "今开", "最高", "最低", "成交量", 
				"成交额(万)", "换手率", "总市值(万)", "y1EPS", "y2EPS",
				"y1PE", "y2PE", "申万二级"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("stock_code", "代码");
		map.put("stock_name", "名称");
		map.put("price", "最新价");
		map.put("change_value", "涨跌额");
		map.put("change_rate", "涨跌幅(%)");
		map.put("close_price", "昨收");
		map.put("open_price", "今开");
		map.put("high_price", "最高");
		map.put("low_price", "最低");
		map.put("volume", "成交量");
		map.put("turnover", "成交额(万)");
		map.put("turnover_rate", "换手率");
		map.put("capitalization", "总市值(万)");
		map.put("eps_y1", "y1EPS");
		map.put("eps_y2", "y2EPS");
		map.put("pe_y1", "y1PE");
		map.put("pe_y2", "y2PE");
		map.put("industry_name", "申万二级");
		m.put("map", map);
		
		m.put("volume", "volume");
		return m;
	}
	
	/**
	 * 获得报告列表
	 * 
	 * 位置：13
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月1日 下午4:23:58
	 */
	public static TreeMap<String, Object> getReportList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"推荐", "发布日期", "报告标题", "机构", "核心研究员", 
				"研究员", "报告类别", "报告类型", "接收日期", "报告页数"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("recommend", "推荐");
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构");
		map.put("core", "核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类别");
		map.put("second_class", "报告类型");
		map.put("into_date", "接收日期");
		map.put("file_pages", "报告页数");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得线索列表
	 *
	 * 位置：14
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月7日 下午3:37:24
	 */
	public static TreeMap<String, Object> getTreasureList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"提示时间", "证券代码", "证券名称", "线索来源及文章标题", "标签", 
				"信息次数"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("create_date", "提示时间");
		map.put("stock_code", "证券代码");
		map.put("sname", "证券名称");
		map.put("title", "线索来源及文章标题");
		map.put("tags", "标签");
		map.put("total_num", "信息次数");
		m.put("map", map);
		
		m.put("tags", "name");
		return m;
	}
	
	/**
	 * 获得我的股票书签
	 *
	 * 位置：15
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月7日 下午4:26:22
	 */
	public static TreeMap<String, Object> getTreasureTag() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"发布日期", "证券代码", "证券名称", "线索来源及文章标题"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("create_date", "发布日期");
		map.put("stock_code", "证券代码");
		map.put("stock_name", "证券名称");
		map.put("title", "线索来源及文章标题");
		m.put("map", map);
		return m;
	}
	
	/**
	 * 获得我的股票池
	 *
	 * 位置：16
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月7日 下午4:32:06
	 */
	public static TreeMap<String, Object> getEgMystockList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"证券代码", "证券名称", "入选日期", "入选基准股价", "最新价格", "持有交易日", "收益率", 
				"期间最大收益", "期间最大回撤"};
		m.put("titles", titles);

		Map<String, String> map = new TreeMap<String, String>();
		map.put("stock_code", "证券代码");
		map.put("stock_name", "证券名称");
		map.put("insert_date", "入选日期");
		map.put("base_val", "入选基准股价");
		map.put("close_price", "最新价格");
		map.put("times", "持有交易日");
		map.put("rate", "收益率");
		map.put("max_rate", "期间最大收益");
		map.put("max_retrat_rate", "期间最大回撤");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得寻宝总结列表
	 * 
	 * 位置：17
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月7日 下午4:41:29
	 */
	public static TreeMap<String, Object> getSummaryList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"证券代码", "证券名称", "信息次数", "首次提示日期", "最新提示日期", "报表拟披露日期", 
				"诊断类型编号", "入选价格", "首次提示日期前三个月", "首次提示日期前一个月", "首次提示日期前一周", 
				"首次提示日期后一周", "至今", "期间最大收益率", "超预期鉴定报表周期", "超预期鉴定报表发布日"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("stock_code", "证券代码");
		map.put("stock_name", "证券名称");
		map.put("total_num", "信息次数");
		map.put("first_date", "首次提示日期");
		map.put("new_date", "最新提示日期");
		map.put("publish_date", "报表拟披露日期");
		map.put("type_id", "诊断类型编号");
		map.put("base_price", "入选价格");
		map.put("before_overincome3m", "首次提示日期前三个月");
		map.put("before_overincome1m", "首次提示日期前一个月");
		map.put("before_overincome1w", "首次提示日期前一周");
		map.put("later_overincome1w", "首次提示日期后一周");
		map.put("new_rate", "至今");
		map.put("max_rate", "期间最大收益率");
		map.put("excessperiod", "超预期鉴定报表周期");
		map.put("excess_end_date", "超预期鉴定报表发布日");
		m.put("map", map);
		return m;
	}
	
	/**
	 * 我的收藏报告
	 * 
	 * loction:18
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月8日 下午4:16:49
	 */
	public static TreeMap<String, Object> getMyFavourReportList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"推荐", "发布日期", "报告标题", "机构", "核心研究员", 
				"研究员", "报告类别", "报告类型", "收藏日期"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("recommend", "推荐");
		map.put("createdate", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构");
		map.put("core", "核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类别");
		map.put("second_class", "报告类型");
		map.put("favor_time", "收藏日期");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 我的定制报告
	 * 
	 * loction:19
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月8日 下午4:31:49
	 */
	public static TreeMap<String, Object> getMyCustomizationReportList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"推荐", "发布日期", "报告标题", "机构", "核心研究员", 
				"研究员", "报告类别", "报告类型", "接收日期", "报告页数", "行业名称"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("recommend", "推荐");
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构");
		map.put("core", "核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类别");
		map.put("second_class", "报告类型");
		map.put("into_date", "接收日期");
		map.put("file_pages", "报告页数");
		map.put("industry", "行业名称");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 公司报告
	 * 
	 * loction:20
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月8日 下午4:59:00
	 */
	public static TreeMap<String, Object> getCompanyReportList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"推荐", "发布日期", "报告标题", "机构", "核心研究员", 
				"研究员", "报告类别", "报告类型", "接收日期", "报告页数", "涨幅", "周涨幅", 
				"证券代码", "证券名称", "净利润", "收入", "目标价", "评级", "行业名称"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("recommend", "推荐");
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构");
		map.put("core", "核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类别");
		map.put("second_class", "报告类型");
		map.put("into_date", "接收日期");
		map.put("file_pages", "报告页数");
		map.put("change_rate", "涨幅");
		map.put("week_change_rate", "周涨幅");
		map.put("code", "证券代码");
		map.put("code_name", "证券名称");
		map.put("profit_rate", "净利润");
		map.put("income_rate", "收入");
		map.put("target_price_rate", "目标价");
		map.put("score_rate", "评级");
		map.put("sw_first_name", "行业名称");
		m.put("map", map);
		
		m.put("sw_first_name", "sw_first_name");
		return m;
	}
	
	/**
	 * 热点追踪--相关股票
	 *
	 * loction:21
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月16日 下午4:11:55
	 */
	public static TreeMap<String, Object> getHotSpotTraceStock() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"证券代码", "证券名称", "报告数", "新闻数", "总市值（亿元）", 
				"y1(EPS)", "y2(EPS)", "y1(PE)", "y2(PE)"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("stock_code", "证券代码");
		map.put("stock_name", "证券名称");
		map.put("report_count", "报告数");
		map.put("news_count", "新闻数");
		map.put("tcp", "总市值（亿元）");
		map.put("y1eps", "y1(EPS)");
		map.put("y2eps", "y2(EPS)");
		map.put("y1pe", "y1(PE)");
		map.put("y2pe", "y2(PE)");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 热点追踪--个股报告
	 *
	 * loction:22
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月16日 下午4:11:55
	 */
	public static TreeMap<String, Object> getHotSpotTraceStockReport() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"发布日期", "报告标题", "机构名称", "是否核心研究员", "研究员", "报告类型", 
				"报告类别", "行业"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构名称");
		map.put("core", "是否核心研究员");
		map.put("author", "研究员");
		map.put("second_class", "报告类型");
		map.put("first_class", "报告类别");
		map.put("sw_first_name", "行业");
		m.put("map", map);
		
		m.put("sw_first_name", "sw_first_name");
		return m;
	}
	
	/**
	 * 热点追踪--行业报告报告
	 *
	 * loction:23
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月16日 下午4:11:55
	 */
	public static TreeMap<String, Object> getHotSpotTraceIndustryReport() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"发布日期", "报告标题", "机构名称", "是否核心研究员", "研究员", "报告类型"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构名称");
		map.put("core", "是否核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类型");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 热点追踪--行业报告报告
	 *
	 * loction:24
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月16日 下午4:11:55
	 */
	public static TreeMap<String, Object> getHotSpotTraceNews() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"发布日期", "标题", "来源"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("date", "发布日期");
		map.put("title", "标题");
		map.put("origin", "来源");
		m.put("map", map);
		
		return m;
	}
	
	/**
	 * 获得行业报告列表
	 * 
	 * 位置：25
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月1日 下午4:23:58
	 */
	public static TreeMap<String, Object> getIndustryReportList() {
		TreeMap<String, Object> m = new TreeMap<String, Object>();
		
		String[] titles = {"推荐", "发布日期", "报告标题", "机构", "核心研究员", 
				"研究员", "报告类别", "报告类型", "接收日期", "报告页数", "行业名称"};
		m.put("titles", titles);
		
		Map<String, String> map = new TreeMap<String, String>();
		map.put("recommend", "推荐");
		map.put("create_date", "发布日期");
		map.put("report_title", "报告标题");
		map.put("organ_name", "机构");
		map.put("core", "核心研究员");
		map.put("author", "研究员");
		map.put("first_class", "报告类别");
		map.put("second_class", "报告类型");
		map.put("into_date", "接收日期");
		map.put("file_pages", "报告页数");
		map.put("sw_first_name", "行业名称");
		m.put("map", map);
		
		m.put("sw_first_name", "sw_first_name");
		return m;
	}
}
