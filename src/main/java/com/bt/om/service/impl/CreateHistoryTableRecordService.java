package com.bt.om.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.CreateHistoryTableRecord;
import com.bt.om.mapper.CreateHistoryTableRecordMapper;
import com.bt.om.service.ICreateHistoryTableRecordService;

/**
 * 历史表记录相关事务层
 */
@Service
public class CreateHistoryTableRecordService implements ICreateHistoryTableRecordService{
	@Autowired
	private CreateHistoryTableRecordMapper createHistoryTableRecordMapper;
	
	/**
	 * 获取最近两年的历史表名,不包含当前时间的半年
	 */
	@Override
	public List<CreateHistoryTableRecord> selectRecord() {
		return createHistoryTableRecordMapper.selectRecord();
	}

}
