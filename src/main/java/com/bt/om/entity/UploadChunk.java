package com.bt.om.entity;

import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

/**
 * 上传（大）文件的 分片
 */
public class UploadChunk {

    @Id
    private String md5; // 此分片的md5
    /**
     * 原始文件（母文件）的 md5
     * @see UploadTask#md5
     */
    private String originMd5;
    private int index;
    private long size;
    private Timestamp createTime;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getOriginMd5() {
        return originMd5;
    }

    public void setOriginMd5(String originMd5) {
        this.originMd5 = originMd5;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
