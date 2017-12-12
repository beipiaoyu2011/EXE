package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties文件解析
 * 
 * @version 1.0
 * @since JDK1.7
 * @author xuyong
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年3月2日 下午1:34:13
 */
public class PropertiesParse {

	/**
	 * 获得操作对象
	 *
	 * @return
	 * 
	 * @author xuyong
	 * @date 2016年3月2日 下午1:36:03
	 */
	public static Properties getProperties() {
		 // 生成输入流  
        InputStream ins = null;
		try {
			ins = new FileInputStream(new File("conf/doc_export.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
        //创建properties对象
        Properties p = new Properties();
        
        try {
        	if(ins == null) {
        		return null;
        	}
			p.load(ins);//导入文件流
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {	//关闭资源
			if(ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return null;
	}
}
