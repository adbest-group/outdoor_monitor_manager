package com.bt.om.mapper;

import com.bt.om.entity.UploadTask;

/**
 * 上传任务的基础数据支持
 * @see com.bt.om.entity.UploadTask
 */
public interface UploadTaskMapper {

    // 创建
    int createUploadTask(UploadTask uploadTask);

    // 删除
    int deleteUploadTask(String md5);
}
