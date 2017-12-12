package controllers.upload;

import java.io.File;

import play.mvc.Controller;
import util.UploadUtil;

/**
 * 
 * 
 * 
 * @version 1.0
 * @since JDK1.7
 * @author yaomy
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2016 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2016年7月29日 上午10:51:51
 */
public class FileUploaderAction extends Controller {

	/**
	 * 
	 * 方法描述 上传图片
	 *
	 * @param file
	 * @param width
	 * @param height
	 * @throws Exception
	 * 
	 * @author yaomy
	 * @date 2017年7月18日 下午2:28:19
	 */
	public static void upload(File file, Integer width, Integer height) throws Exception {
		String result = UploadUtil.uploadImage(file, "feedback", width, height);
		renderText(result);
	}
}
