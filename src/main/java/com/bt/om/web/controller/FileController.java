package com.bt.om.web.controller;

import com.bt.om.entity.FileEntity;
import com.bt.om.web.util.FileUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 文件请求
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Value("${file.md5.salt}")
    private String salt;
    @Value("${file.directory.chunk}")
    private String chunkDir;
    @Value("${file.directory.upload}")
    private String uploadDir;

    /**
     * 上传文件
     * 使用 org.apache.commons.fileupload 组件
     */
    @RequestMapping("/upload")
    @ResponseBody
    public FileEntity upload(HttpServletRequest request, HttpServletResponse response) {
        DefaultMultipartHttpServletRequest defaultRequest = (DefaultMultipartHttpServletRequest) request;
        MultipartFile file = defaultRequest.getFile("file");
        Map<String, String[]> parameterMap = defaultRequest.getParameterMap();
        FileEntity fileEntity = new FileEntity();
        String md5 = parameterMap.get("md5")[0];
        fileEntity.setName(md5);
        // 区分是单文件还是分片
        try {
            if (parameterMap.get("chunks") != null) { // 分片
                String chunk = parameterMap.get("chunk")[0];
                FileUtils.save(file.getInputStream(), chunkDir + File.separator + md5, chunk, false);
            } else { // 单文件
                FileUtils.save(file.getInputStream(), uploadDir + File.separator + md5.substring(0, 2), md5, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileEntity;
    }

    /**
     * 验证文件分片
     *
     * @param md5
     * @param chunk
     * @param chunkSize
     * @return
     */
    @RequestMapping("/checkChunk")
    @ResponseBody
    public FileEntity checkChunk(String md5, String chunk, long chunkSize) {
        Assert.isTrue(StringUtils.hasText(md5) && StringUtils.hasText(chunk) && chunkSize > 0, "参数无效");
        File file = new File(chunkDir + File.separator + md5 + File.separator + chunk);
        if (file.exists() && file.length() == chunkSize) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(file.getName());
            fileEntity.setLength(file.length());
            return fileEntity;
        }
        return null;
    }

    /**
     * 合并文件分片
     *
     * @param md5
     * @return
     */
    @RequestMapping("/mergeChunks")
    @ResponseBody
    public FileEntity mergeChunks(String md5) {
        Assert.isTrue(StringUtils.hasText(md5), "参数无效");
        // 文件验证
        File directory = new File(chunkDir + File.separator + md5);
        Assert.isTrue(directory.isDirectory(), "找不到此目录: " + directory.getAbsolutePath());
        List<File> fileList = Arrays.asList(directory.listFiles());
        Assert.isTrue(fileList.size() > 0, "此目录是空的");
        // 分片升序排序
        fileList.sort((a, b) -> {
            if (Integer.parseInt(a.getName()) - Integer.parseInt(b.getName()) < 0) {
                return -1;
            }
            return 1;
        });
        // 准备合并的目录和文件
        File dir = new File(uploadDir + File.separator + md5.substring(0, 2));
        File mergedFile = new File(dir.getAbsolutePath() + File.separator + md5);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileChannel outChannel = null;
            outChannel = new FileOutputStream(mergedFile).getChannel();
            // 合并分片
//            FileChannel inChannel;
            for (File file : fileList) {
                FileUtils.save(new FileInputStream(file), dir.getAbsolutePath(), md5, true);
//                inChannel = new FileInputStream(file).getChannel();
//                inChannel.transferTo(0, inChannel.size(), outChannel);
//                inChannel.close();
                // 删除分片
                file.delete();
            }
            // 关闭流
            outChannel.close();
            // 删除文件夹
            if (directory.isDirectory()) {
                directory.delete();
            }
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(md5);
            fileEntity.setMimeType(Files.probeContentType(mergedFile.toPath()));
            fileEntity.setLength(mergedFile.length());
            fileEntity.setUpdateTime(new Timestamp(mergedFile.lastModified()));
            return fileEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载文件
     *
     * @return
     */
    public HttpServletResponse downloadFile(String fileName) {

        return null;
    }

    /**
     * 预览文件
     *
     * @param fileName
     */
    public void viewFile(String fileName) {

    }


    /**
     * 查询文件信息
     *
     * @return
     */
    public FileEntity getFileEntity(String fileName) {

        return null;
    }

}
