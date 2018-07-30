package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.OperateLog;
import com.bt.om.mapper.OperateLogMapper;
import com.bt.om.service.IOperateLogService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 操作日志 相关事务层
 */
@Service
public class OperateLogService implements IOperateLogService {

	@Autowired
	private OperateLogMapper operateLogMapper;
	
	/**
	 * 分页查询操作日志
	 */
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = operateLogMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(operateLogMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<OperateLog>());
        }
	}

	/**
	 * 通过id查询操作日志
	 */
	@Override
	public OperateLog getById(Integer id) {
		return operateLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 插入一条操作日志
	 */
	@Override
	@Transactional
	public void save(OperateLog operateLog) {
		operateLogMapper.insert(operateLog);
	}

	/**
	 * 修改操作日志
	 */
	@Override
	@Transactional
	public void modify(OperateLog operateLog) {
		Date now = new Date();
		operateLog.setUpdateTime(now);
		operateLogMapper.updateByPrimaryKeySelective(operateLog);
	}

}
