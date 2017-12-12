package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import javax.imageio.ImageIO;

import entity.ImageInfo;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

public class FtpUtilSeven {

	public FtpClient ftpclient;

	public FtpUtilSeven(String url, String port, String username, String password) {
		// 创建ftp
		try {
			// 创建地址
			SocketAddress addr = new InetSocketAddress(url, Integer.valueOf(port));
			// 连接
			ftpclient = FtpClient.create();
			ftpclient.connect(addr);
			// 登陆
			ftpclient.login(username, password.toCharArray());
			ftpclient.setBinaryType();
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切换目录
	 * 
	 * @param ftp
	 * @param path
	 */
	public void changeDirectory(String path) {
		try {
			ftpclient.changeDirectory(path);
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭ftp
	 * 
	 * @param ftp
	 */
	public void disconnectFTP() {
		try {
			ftpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param localFile
	 * @param ftpFile
	 * @param ftp
	 */
	public String upload(File file) {
		OutputStream os = null;
		FileInputStream fis = null;

		File headPath = new File("tmp/head/");
		if(!headPath.isDirectory()){
			headPath.mkdirs();
		}
		
		String fileName = file.getName();
		Long time = System.currentTimeMillis();
		String ftpFileName = "gg3" + time + fileName.substring(fileName.lastIndexOf("."));
		File saveFile = new File("tmp/head/" + ftpFileName);
		
		try {
			BufferedImage srcImage;
			String imgType = "JPEG";
			if (fileName.toLowerCase().endsWith(".png")) {
				imgType = "PNG";
			}
			srcImage = ImageIO.read(file);
			int width = srcImage.getWidth();
			int hight = srcImage.getHeight();
			if (width > ImageInfo.WIDTH_LIST || hight > ImageInfo.HEIGHT_LIST) {
				srcImage = ImageInfo.resize(srcImage, ImageInfo.WIDTH_LIST, ImageInfo.HEIGHT_LIST);
			}
			if (ImageIO.write(srcImage, imgType, saveFile)) {
				String ftpFilePath = ImageInfo.DIR_ROOT_HEAD + ftpFileName;
				// 将ftp文件加入输出流中。输出到ftp上
				os = ftpclient.putFileStream(ftpFilePath);
				// 创建一个缓冲区
				fis = new FileInputStream(saveFile);
				byte[] bytes = new byte[1024];
				int c;
				while ((c = fis.read(bytes)) != -1) {
					os.write(bytes, 0, c);
				}
			}
			return ftpFileName;
		} catch (FtpProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
				if (fis != null)
					fis.close();
				disconnectFTP();
				//删除临时文件
				if(saveFile.exists()){
					saveFile.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * TODO
	 * 文件下载
	 * 
	 * @param localFile
	 * @param ftpFile
	 * @param ftp
	 */
	public Boolean download(String localFile, String ftpFile) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			// 获取ftp上的文件
			is = ftpclient.getFileStream(ftpFile);
			File file = new File(localFile);
			byte[] bytes = new byte[1024];
			int i;
			fos = new FileOutputStream(file);
			while ((i = is.read(bytes)) != -1) {
				fos.write(bytes, 0, i);
			}
			System.out.println(ftpFile + "  download success!!");
			return true;
		} catch (FtpProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (is != null)
					is.close();
				disconnectFTP();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}