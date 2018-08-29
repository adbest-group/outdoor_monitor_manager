package com.bt.om.entity;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

/**
 * 上传任务
 * 每个任务只包含一个完整文件
 * 文件过大而进行分片传输时将建立此任务
 */
public class UploadTask {

    @Id
    private String md5; // 文件在浏览器或客户端计算得出的 md5
    private String mimeType;
    private long length;
    private long maxChunkSize; // 最大分片大小
    private Timestamp createTime;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getMaxChunkSize() {
        return maxChunkSize;
    }

    public void setMaxChunkSize(long maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
