package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.CreateHistoryTableRecord;

public interface ICreateHistoryTableRecordService {
	/**
	 * 获取最近两年的历史表名
	 */
	public List<CreateHistoryTableRecord> selectRecord();
}
