package com.bt.om.web;

import java.io.*;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bt.om.common.ImageUpload;
import com.bt.om.util.ConfigUtil;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: UploadController.java, v 0.1 2016年12月10日 下午5:08:56 tanyong Exp
 *          $
 */
@Controller
public class UploadController extends BasicController {

	private String ip = ConfigUtil.getString("sys.uploadAdd.domain");

	private String path = ConfigUtil.getString("sys.uploadAdd.display.domain");

	@RequestMapping(value = "/upload")
	public @ResponseBody String uploadPic(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		String path = request.getRealPath("/");
		path = path + (path.endsWith(File.separator)?"":File.separatorChar)+"static"+File.separatorChar+"upload"+File.separatorChar;
		String imageName = file.getOriginalFilename();
		InputStream is;
		String filepath;
		try {
			is = file.getInputStream();
			Long size = file.getSize();
			if (!isImg(imageName.toLowerCase())) {
				return "notPic";
			}
			if (size > 1024 * 1024) {
				return "overPic";
			}

//			filepath = ImageUpload.uploadStreamImg(ip, is, size, imageName);
//			filepath = path + filepath.substring(filepath.indexOf("/", 10));

			filepath = saveFile(path,imageName,is);
		} catch (IOException e) {
			return "error";
		}
		return filepath;
	}

	//保存在本服务器
	private String saveFile(String path,String filename,InputStream is){
		String ext = filename.substring(filename.lastIndexOf("."));
		filename = UUID.randomUUID().toString().toLowerCase()+"."+ext.toLowerCase();
		FileOutputStream fos = null;
		try {
			 fos = new FileOutputStream(path+filename);
			int len = 0;
			byte[] buff = new byte[1024];
			while((len=is.read(buff))>0){
				fos.write(buff);
			}
			return "/static/upload/"+filename;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos!=null){
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "error";
	}

	private boolean isImg(String imgName) {
		imgName = imgName.toLowerCase();
		if (imgName.endsWith(".jpg") || imgName.endsWith(".jpeg") || imgName.endsWith(".png") || imgName.endsWith(".gif")) {
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/uploadFile")
	public @ResponseBody String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, ModelMap model) {
		String imageName = file.getOriginalFilename();
		InputStream is;
		String filepath;
		try {
			is = file.getInputStream();
			Long size = file.getSize();
			if (!isReport(imageName.toLowerCase())) {
				return "notPic";
			}
			if (size > 1024 * 1024 * 30) {
				return "overPic";
			}

			filepath = ImageUpload.uploadStreamImg(ip, is, size, imageName);
			filepath = path + filepath.substring(filepath.indexOf("/", 10));
		} catch (IOException e) {
			return "error";
		}
		return filepath;
	}

	private boolean isReport(String fileName) {
		fileName = fileName.toLowerCase();
		if (fileName.toLowerCase().endsWith(".doc") || fileName.toLowerCase().endsWith(".docx")
				|| fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx")
				|| fileName.toLowerCase().endsWith(".ppt") || fileName.toLowerCase().endsWith(".pptx")
				|| fileName.toLowerCase().endsWith(".pdf")) {
			return true;
		}
		return false;
	}

}
