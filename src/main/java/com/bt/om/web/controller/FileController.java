package com.bt.om.web.controller;

import com.bt.om.entity.FileEntity;
import com.bt.om.web.util.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 文件请求
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileUtils fileUtils;

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
        String fileName = fileUtils.getFileName(md5);
        String fullPath = fileUtils.getFileFullPath(fileName);
        long fileSize = Long.parseLong(parameterMap.get("size")[0]);
        // 完整文件检测
        FileEntity validatedFile = fileUtils.validateFile(fullPath, fileSize);
        if (StringUtils.hasText(validatedFile.getName())) {
            return validatedFile;
        }
        // 区分是单文件还是分片
        try {
            if (parameterMap.get("chunks") != null) { // 分片
                String chunk = parameterMap.get("chunk")[0];
                Assert.hasText(chunk, "分片下标为空");
                fileSize = Long.parseLong(parameterMap.get("chunkSize")[0]);
                fullPath = fileUtils.getChunkFullPath(md5, chunk);
                // 若验证分片已存在且完整则直接返回分片文件信息
                FileEntity validatedChunk = fileUtils.validateFile(fullPath, fileSize);
                if (StringUtils.hasText(validatedFile.getName())) {
                    return validatedChunk;
                }
            }
            fileUtils.save(file.getInputStream(), fullPath, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileEntity.setName(fileName);
        fileEntity.setLength(fileSize);
        return fileEntity;
    }

    /**
     * 验证完整文件
     * 文件存在并且长度吻合则返回文件信息
     *
     * @param md5
     * @param fileSize
     * @return
     */
    @RequestMapping("/checkFile")
    @ResponseBody
    public FileEntity checkFile(String md5, Long fileSize) {
        String fileName = fileUtils.getFileName(md5);
        return fileUtils.validateFile(fileUtils.getFileFullPath(fileName), fileSize);
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
        String chunkFullPath = fileUtils.getChunkFullPath(md5, chunk);
        return fileUtils.validateFile(chunkFullPath, chunkSize);
    }

    /**
     * 合并文件分片
     *
     * @param md5
     * @return
     */
    @RequestMapping("/mergeChunks")
    @ResponseBody
    public FileEntity mergeChunks(String md5, long fileSize) {
        Assert.isTrue(StringUtils.hasText(md5), "参数无效");
        // 文件验证
        String fileFullPath = fileUtils.getFileFullPath(fileUtils.getFileName(md5));
        FileEntity validateFile = fileUtils.validateFile(fileFullPath, fileSize);
        if (StringUtils.hasText(validateFile.getName())) {
            return validateFile;
        }
        File mergedFile = new File(fileFullPath);
        // 文件分片所在文件夹路径
        File chunkDirectory = new File(fileUtils.getChunkDirectory(md5));
        Assert.isTrue(chunkDirectory.isDirectory(), "找不到此目录: " + chunkDirectory.getAbsolutePath());
        List<File> fileList = Arrays.asList(chunkDirectory.listFiles());
        Assert.isTrue(fileList.size() > 0, "此目录是空的");
        // 分片升序排序
        fileList.sort((a, b) -> {
            if (Integer.parseInt(a.getName()) - Integer.parseInt(b.getName()) < 0) {
                return -1;
            }
            return 1;
        });
        try {
            // 合并分片
            for (File file : fileList) {
                fileUtils.save(new FileInputStream(file), mergedFile.getAbsolutePath(), true);
                // 删除分片
                file.delete();
            }
            // 删除分片所在文件夹
            if (chunkDirectory.isDirectory()) {
                chunkDirectory.delete();
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
     * @param fullName
     * @param response
     * @return
     */
    @RequestMapping("/downloadFile")
    @ResponseBody
    public void downloadFile(String fullName, HttpServletResponse response) {
        fileUtils.downLoad(fullName, response, false);
    }

    /**
     * 预览文件
     *
     * @param fullName
     * @param response
     */
    @RequestMapping("/viewFile")
    @ResponseBody
    public void viewFile(String fullName, HttpServletResponse response) {
        fileUtils.downLoad(fullName, response, true);
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
