package com.bt.om.web.quartz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.bt.om.util.ConfigUtil;

/**
 * 定时清理服务器文件
 * @author gaohao
 *
 */
public class ClearFilesTask extends AbstractTask{
	private String fileUploadPath = ConfigUtil.getString("file.upload.path");

	@Override
	protected boolean canProcess() {
		return true;
	}

	@Override
	protected void process() {
		List<String> directoryNames = new ArrayList<>();
		directoryNames.add("pdf");
		directoryNames.add("excel");
		getFile(fileUploadPath, directoryNames);
	}  
	/**
	 * 查找某个文件夹并删除里面所有的文件
	 * @param path 文件夹路径
	 * @param directoryName 文件名称
	 */
	private static void getFile(String path, List<String> directoryNames) {
		File file = new File(path);
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isDirectory()) {
				if (directoryNames.contains(array[i].getName())) {
					try {
						FileUtils.cleanDirectory(array[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					getFile(array[i].getPath(),directoryNames);
				}
			}
		}
	}
}
