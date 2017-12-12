package util;

import ggframework.bottom.config.GGConfigurer;

import java.io.File;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import common.org.ucloud.upload.UFileMultiUpload;
/**
 * 
 * 上传图片工具类
 * 
 * @version 1.0
 * @since JDK1.7
 * @author yaomy
 * @company 上海朝阳永续信息技术有限公司
 * @copyright (c) 2017 SunTime Co'Ltd Inc. All rights reserved.
 * @date 2017年7月18日 下午2:18:32
 */
public class UploadUtil {

	/**
	 * 
	 * 方法描述 上传图片
	 *
	 * @param file
	 * @param dir
	 * @param width
	 * @param height
	 * @return
	 * 
	 * @author yaomy
	 * @date 2017年7月18日 下午2:18:50
	 */
	public static String uploadImage(File file, String dir, Integer width, Integer height) {
		String uuid = UUID.randomUUID().toString().split("-")[0];
		String imageName = "ggimages/" + dir + "/" + uuid + "." + file.getName().split("\\.")[1];
		String url = GGConfigurer.get("api.ucloud.url") + "/" + imageName;
		String bucket = GGConfigurer.get("api.ucloud.bucket");
		UFileMultiUpload.uFIleUpload(file, bucket, imageName);

		if ((height == null) && (width == null)) {// 不限制
			return url;
		} else {
			if (StringUtils.isNotBlank(url)) {
				if ((width != null) && (height == null)) {// 指定宽度缩放 高度自适应
					url = url + "?iopcmd=thumbnail&type=4&width=" + width;
				}
				if ((height != null) && (width == null)) {// 指定高度缩放 宽度自适应
					url = url + "?iopcmd=thumbnail&type=5&height=" + height;
				}
				if ((height != null) && (width != null)) {// 指定高度缩放 指定宽度
					url = url+"?iopcmd=thumbnail&type=8&width="+width+"&height="+height;
				}
			}
		}
		return url;
	}
}
