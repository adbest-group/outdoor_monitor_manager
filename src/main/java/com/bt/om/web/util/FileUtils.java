package com.bt.om.web.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 常用文件方法工具类
 */
public class FileUtils {


    /**
     * 保存文件
     *
     * @param inputStream
     * @param directory
     * @param fileName
     * @param append    是否追加内容
     */
    public static void save(InputStream inputStream, String directory, String fileName, boolean append) {
        // 检查路径是否存在，不存在则新建完整路径
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(directory + File.separator + fileName, append);
            // 每次写入 1024 字节
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) > 0) {
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("成功写入文件：" + directory + File.separator + fileName);
            // 关闭流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     *
     * @param fullPath
     * @return
     */
    public static FileInputStream readFile(String fullPath) {
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("未找到指定的文件：" + fullPath, e);
        }
    }

}
