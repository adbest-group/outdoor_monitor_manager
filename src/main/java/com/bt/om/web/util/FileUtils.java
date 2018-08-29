package com.bt.om.web.util;

import com.bt.om.entity.FileEntity;
import eu.medsea.mimeutil.MimeUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 常用文件方法工具类
 */
@Component
public class FileUtils {

    @Value("${file.md5.salt}")
    private String salt;
    @Value("${file.directory.chunk}")
    private String chunkBase;
    @Value("${file.directory.upload}")
    private String uploadBase;

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    /**
     * 保存文件
     *
     * @param inputStream
     * @param fullPath
     * @param append      是否追加内容
     */
    public void save(InputStream inputStream, String fullPath, boolean append) {
        // 检查路径是否存在，不存在则新建完整路径
        File targetFile = new File(fullPath);
        File dir = new File(targetFile.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile, append);
            // 每次写入 1024 字节
            byte[] buffer = new byte[1024 * 10];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("成功写入文件：" + targetFile.getAbsolutePath());
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
    public FileInputStream readFile(String fullPath) {
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("未找到指定的文件：" + fullPath, e);
        }
    }

    /**
     * 验证文件
     * 文件存在并且长度吻合则返回文件信息
     *
     * @param fullPath
     * @param size
     * @return
     */
    public FileEntity validateFile(String fullPath, Long size) {
        FileEntity fileEntity = new FileEntity();
        File file = new File(fullPath);
        if (file.isFile()) {
            Assert.notNull(size, "文件大小为空");
            if (file.length() != size) {
                throw new RuntimeException("文件名重复：" + fullPath);
            }
            fileEntity.setName(file.getName());
            fileEntity.setLength(file.length());
            fileEntity.setUpdateTime(new Timestamp(file.lastModified()));
            fileEntity.setMimeType(MimeUtil.getMimeTypes(file).toString());
        }
        return fileEntity;
    }

    /**
     * 生成文件名
     *
     * @param md5
     * @return
     */
    public String getFileName(String md5) {
        Assert.hasText(md5, "md5 为空");
        return DigestUtils.md5Hex(md5 + salt);
    }

    /**
     * 获取文件全路径
     *
     * @param fileName
     * @return
     */
    public String getFileFullPath(String fileName) {
        return uploadBase + File.separator + fileName.substring(0, 2) + File.separator + fileName;
    }

    /**
     * 获取文件分片的父级路径
     *
     * @param md5
     * @return
     */
    public String getChunkDirectory(String md5) {
        return chunkBase + File.separator + md5;
    }

    /**
     * 获取文件分片全路径
     *
     * @param md5
     * @param chunk
     * @return
     */
    public String getChunkFullPath(String md5, String chunk) {
        Assert.hasText(md5, "md5 为空");
        Assert.hasText(chunk, "chunk 为空");
        return chunkBase + File.separator + md5 + File.separator + chunk;
    }

    /**
     * 下载
     *
     * @param fullName
     * @param request
     * @param response
     * @param isOnLine
     */
    public void downLoad(String fullName, HttpServletRequest request, HttpServletResponse response, boolean isOnLine) {
        String suffix = fullName.substring(fullName.lastIndexOf(".") + 1);
        String fileName = fullName.substring(0, fullName.lastIndexOf("."));
        String fileFullPath = getFileFullPath(fileName);
        response.reset(); // 非常重要
        try {
            File file = new File(fileFullPath);
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file), 1024 * 256);
            byte[] buf = new byte[1024 * 16];
            int len;
            response.setHeader("Content-Length", "" + file.length());
            if (isOnLine) { // 在线打开方式
                response.setContentType(MimeUtil.getMimeTypes(new File(fileFullPath)).toString());
                response.setHeader("Content-Disposition", "inline; filename=" + fullName);
            } else { // 纯下载方式
                String range = request.getHeader("Range");
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + fullName);
                HttpRange httpRange = getHttpRange(range);
                if(httpRange.getRangeStart(file.length()) > 0){
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    response.setHeader("Content-Range", getHttpRangeResponse(httpRange, file.length()));
                    inputStream.skip(httpRange.getRangeStart(file.length()));
                }
            }
//            javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream(), 1024 * 256);
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装 HttpRange
     * @param range
     * @return
     */
    public HttpRange getHttpRange(String range) {
        List<HttpRange> rangeList = HttpRange.parseRanges(range);
        if (rangeList.size() > 0) {
            return rangeList.get(0);
        }
        return HttpRange.createByteRange(0);
    }

    /**
     * 组装 HttpRange 返回头
     * @param range
     * @param totalLength
     * @return
     */
    public String getHttpRangeResponse(HttpRange range, long totalLength){
        // bytes 500-1233/1234
        String rangeStr = "bytes " + range.getRangeStart(totalLength) + "-" + range.getRangeEnd(totalLength) + "/" + totalLength;
        return rangeStr;
    }

}
