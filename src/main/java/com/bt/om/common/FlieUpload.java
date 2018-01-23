package com.bt.om.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.bt.om.util.ConfigUtil;

public class FlieUpload {

	private static Logger logger = LoggerFactory.getLogger(FlieUpload.class);

	private static final String conf = ConfigUtil.getString("file.config.name");

	/**
	 * 存储文件到字节数组中
	 * 
	 * @param inStream
	 * @param fileLength
	 * @return
	 * @throws IOException
	 */
	private static byte[] getFileBuffer(InputStream inStream, long fileLength) throws IOException {

		byte[] buffer = new byte[256 * 1024];
		byte[] fileBuffer = new byte[(int) fileLength];

		int count = 0;
		int length = 0;

		while ((length = inStream.read(buffer)) != -1) {
			for (int i = 0; i < length; ++i) {
				fileBuffer[count + i] = buffer[i];
			}
			count += length;
		}
		return fileBuffer;
	}

	public static String uploadImg(String uploadFilePath, String uploadFileName) throws IOException {
		File f = new File(uploadFilePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStream is = fis;
		byte[] fileBuff = getFileBuffer(is, f.length());
		String fileId = "";
		String fileExtName = "";
		if (uploadFileName.contains(".")) {
			fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
		} else {
			logger.warn("Fail to upload file, because the format of filename is illegal.");
			return fileId;
		}

		try {
			ClientGlobal.init(conf);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ClientGlobal.setG_network_timeout(1000);
		// 建立连接
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient1 client = new StorageClient1(trackerServer, storageServer);

		// 设置元信息
		NameValuePair[] metaList = new NameValuePair[3];
		metaList[0] = new NameValuePair("fileName", uploadFileName);
		metaList[1] = new NameValuePair("fileExtName", fileExtName);
		metaList[2] = new NameValuePair("fileLength", String.valueOf(f.length()));

		// 文件上传
		try {
			fileId = client.upload_file1(fileBuff, fileExtName, metaList);
		} catch (Exception e) {
			logger.warn("Upload file \"" + uploadFileName + "\"fails");
		}
		// 关闭连接
		trackerServer.close();
		return fileId.substring(6);
	}

	public static ImageInfoVO getImageInfo(String ip, File fileImg, String uploadFileName) throws IOException {
		ImageInfoVO imageInfo = new ImageInfoVO();
		InputStream fileStream = new FileInputStream(fileImg);
		try {
			String imageUrl = uploadStreamImg(ip, fileStream, fileStream.available(), uploadFileName);
			imageInfo.setImageUrl(imageUrl);
		} catch (Exception e1) {
			try {
				throw e1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		BufferedImage bufferImage = ImageIO.read(new FileInputStream(fileImg));
		imageInfo.setHeight(bufferImage.getHeight());
		imageInfo.setWidth(bufferImage.getWidth());
		return imageInfo;
	}

	/**
	 * 上传图片 返回图片地址
	 * 
	 * @param ip
	 *            服务器IP
	 * @param streamImg
	 * @param streamLen
	 * @param uploadFileName
	 * @return
	 * @throws IOException
	 */
	public static String uploadStreamImg(String ip, InputStream streamImg, long streamLen, String uploadFileName)
			throws IOException {
		InputStream is = streamImg;
		byte[] fileBuff = getFileBuffer(is, streamLen);
		String fileId = "";
		String fileExtName = "";
		if (uploadFileName.contains(".")) {
			fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
		} else {
			logger.warn("Fail to upload file, because the format of filename is illegal.");
			return fileId;
		}

		// 加载解析配置文件
		try {
			ClientGlobal.init(conf);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 根据配置文件信息，建立连接
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient1 client = new StorageClient1(trackerServer, storageServer);

		// 设置元信息
		NameValuePair[] metaList = new NameValuePair[3];
		String uploadImgName = uploadFileName + "_" + System.currentTimeMillis();
		metaList[0] = new NameValuePair("fileName", uploadImgName);
		metaList[1] = new NameValuePair("fileExtName", fileExtName);
		metaList[2] = new NameValuePair("fileLength", String.valueOf(streamLen));

		// 文件上传
		try {
			fileId = client.upload_file1(fileBuff, fileExtName, metaList);
		} catch (Exception e) {
			logger.warn("Upload file \"" + uploadFileName + "\"fails");
		}
		trackerServer.close();
		// fileId： group1/M00/00/01/CwEBPVX6daCAHq6tAAEhl4KM9Y0500.png
		return "http://" + ip + fileId.substring(6);
	}

	public static String uploadCreative(String ip, MultipartFile mf) throws Exception {
		String uploadFileName = mf.getOriginalFilename();
		InputStream is = mf.getInputStream();
		byte[] fileBuff = getFileBuffer(is, is.available());
		String fileId = "";
		String fileExtName = "";
		if (mf.getOriginalFilename().contains(".")) {
			fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
		} else {
			logger.warn("Fail to upload file, because the format of filename is illegal.");
			return fileId;
		}
		// 加载解析配置文件
		try {
			ClientGlobal.init("fdfs.conf");
		} catch (Exception e) {
			logger.info("加载解析配置文件失败");
			throw e;
		}

		// 根据配置文件信息，建立连接
		TrackerClient tracker = new TrackerClient();
		TrackerServer trackerServer = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient1 client = new StorageClient1(trackerServer, storageServer);

		// 设置元信息
		NameValuePair[] metaList = new NameValuePair[3];
		String uploadImgName = uploadFileName + "_" + System.currentTimeMillis();
		metaList[0] = new NameValuePair("fileName", uploadImgName);
		metaList[1] = new NameValuePair("fileExtName", fileExtName);
		metaList[2] = new NameValuePair("fileLength", String.valueOf(is.available()));

		// 文件上传
		try {
			fileId = client.upload_file1(fileBuff, fileExtName, metaList);
		} catch (Exception e) {
			logger.warn("Upload file \"" + uploadFileName + "\" fails");
			throw e;
		}
		trackerServer.close();
		// fileId： group1/M00/00/01/CwEBPVX6daCAHq6tAAEhl4KM9Y0500.png
		System.out.println("http://" + ip + fileId.substring(6));
		return "http://" + ip + fileId.substring(6);
	}
}
