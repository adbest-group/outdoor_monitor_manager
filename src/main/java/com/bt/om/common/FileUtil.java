package com.bt.om.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 * 
 * @author Chenjie
 *
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 视频转换成byte[]
	 * 
	 * @param actualPath
	 * @return
	 */
	public static byte[] uploadCreative(String actualPath) {
		byte[] content = {};
		logger.info("开始上传视频文件:" + actualPath);
		// 从URL中截取最后部分作为文件名
		String fileName = FileUtil.getFileNameFromUrl(actualPath);
		// 视频文件本地存放路径
		String filePath = System.getProperty("user.dir") + File.separator + fileName;
		try {
			// 先下载创意对应的视频文件到本地路径下以fileName作为文件名
			// FileUtil.download(actualPath.replaceAll("cdn.adtime.com",
			// "11.1.1.64"),filePath);
			FileUtil.download(actualPath, filePath);
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			content = bos.toByteArray();
			return content;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			FileUtil.remove(filePath);
		}
		return null;
	}

	/**
	 * 获取文件内的key码
	 * 
	 * @param fileUrl
	 * @param keys
	 */
	public static Set<String> getKeys(InputStream file,String suffix) {
		if ("txt".equalsIgnoreCase(suffix)) {
			// 读取txt文件中券码
			return FileUtil.readTxtFile(file);
		} else if ("csv".equals(suffix)) {
			// 读取csv文件中券码
			return FileUtil.readCsvFile(file);
		} else if ("xlsx".equalsIgnoreCase(suffix) || "xls".equalsIgnoreCase(suffix)) {
			// 读取xlsx、xls文件中券码
			return FileUtil.readExcelFile(file);
		}
		return null;

	}

	/**
	 * txt券码封装
	 * 
	 * @param actualPath
	 * @param keys
	 */
	public static Set<String> readTxtFile(InputStream file) {

		Set<String> tmp = new HashSet<String>();
		String encoding = "UTF-8";
		try {
			 // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(file, encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String strCode = null;
			while ((strCode = bufferedReader.readLine()) != null) {
				strCode = strCode.trim();
				if (!tmp.contains(strCode)) {
					tmp.add(strCode);
				} else {
					continue;
				}
			}
			read.close();
		
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * csv券码封装
	 * 
	 * @param actualPath
	 * @param keys
	 * @throws Exception
	 */
	public static Set<String> readCsvFile(InputStream csv) {

		Set<String> tmp = new HashSet<String>();

		try {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(csv));
			String strLine = "";
			try {
				while ((strLine = br.readLine()) != null) {
					String strCode = strLine.split(",")[0].trim();
					if (!tmp.contains(strCode)) {
						tmp.add(strCode);
					} else {
						continue;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 
	 * 获取excel文件
	 * 
	 * @param actualPath
	 * @throws Exception
	 */
	// 去读Excel的方法readExcel，该方法的入口参数为一个File对象
	@SuppressWarnings("resource")
	public static Set<String> readExcelFile(InputStream is) {

		Set<String> tmp = new HashSet<String>();
		try {
			// 创建输入流，读取Excel

			// jxl提供的Workbook类
			Workbook wb = null;
			try {
				wb = new XSSFWorkbook(is);
			} catch (Exception ex) {
				wb = new HSSFWorkbook(is);
			}

			// Excel的页签数量
			Sheet sheet = wb.getSheetAt(0);
			int firstRowIndex = sheet.getFirstRowNum();
			int lastRowIndex = sheet.getLastRowNum();
			for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
				Row row = sheet.getRow(rIndex);
				if (row != null) {
					int firstCellIndex = row.getFirstCellNum();
					for (int cIndex = firstCellIndex; cIndex < 1; cIndex++) {
						Cell cell = row.getCell(cIndex);
						String strCode = cell.toString().trim();
						if (!tmp.contains(strCode)) {
							tmp.add(strCode);
						} else {
							continue;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 下载文件到本地
	 *
	 * @param urlString
	 *            被下载的文件地址
	 * @param filename
	 *            本地文件名
	 * @throws Exception
	 *             各种异常
	 */
	public static void download(String urlString, String filename) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	/**
	 * 获取url中的文件名
	 * 
	 * @param url
	 */
	public static String getFileNameFromUrl(String url) {
		int index = url.lastIndexOf("/");
		return url.substring(index + 1);
	}

	/**
	 * 删除本地文件
	 * 
	 * @param string
	 */
	public static boolean remove(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			System.out
					.println(getFileNameFromUrl("http://cdn.adtime.com/M00/00/14/CwEBQFbT74WAAWZsAAeVoIm0qms305.FLV"));
			// download("http://cdn.adtime.com/M00/00/14/CwEBQFbT74WAAWZsAAeVoIm0qms305.FLV",
			// "D:\\videos\\CwEBQFbT74WAAWZsAAeVoIm0qms305.FLV");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
