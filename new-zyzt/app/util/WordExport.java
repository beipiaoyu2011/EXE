package util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * word导出工具类
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月1日 下午7:54:36
 */
public class WordExport {
	private static Configuration configuration = null;  
    private static Map<String, Template> allTemplates = null;  
    
    /**
     * 静态加载所有模板
     */
    static {  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");  
        try {
			configuration.setDirectoryForTemplateLoading(new File("template/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }  
  
    /**
     * 抛出模板加载异常
     */
    private WordExport() {  
        throw new AssertionError();  
    }  
  
    /**
     * 创建doc文件
     *
     * @param dataMap
     * @param type
     * @return
     * 
     * @author xuyong
     * @date 2016年3月1日 下午7:56:15
     */
    public static File createDoc(Map<String, Object> dataMaps, String module, String stock_name) {
    	//首先判断目录temp是否存在，若不存在则创建该目录
    	File tFile = new File("temp/");
    	if(!tFile.exists() && !tFile.isDirectory()){
    		tFile.mkdirs();
    	}
    	
    	String uuid = UUID.randomUUID().toString();
    	
       // String name = "temp/" + stock_name + "投研编辑报告(" + DateUtil.dateToString(DateUtil.getNowDate(), "yyyy-MM-dd") + ").doc";  
        String name = "temp/" + stock_name + "投研编辑报告(" + uuid + ").doc";  
        File file = new File(name);  
        allTemplates = new HashMap<String, Template>();   // Java 7 钻石语法  
        try { 
        	Properties pro = PropertiesParse.getProperties();
        	if(pro == null){
        		return null;
        	}
        	Set<Object> tmeps = pro.keySet();
        	for (Object key : tmeps) {
        		String ss = pro.getProperty(key.toString());
        		allTemplates.put(key.toString(), configuration.getTemplate(ss));  
			}
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        Template t = allTemplates.get(module);
        Writer w = null;
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            w = new OutputStreamWriter(new FileOutputStream(file), "utf-8");  
            t.process(dataMaps, w);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally {
			if (w != null){
				try {
					w.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
        return file;  
    }
}
