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
 * Created by jiayong.mao on 2018/5/24.
 */
@Service
public class OperateLogService implements IOperateLogService {

	@Autowired
	private OperateLogMapper operateLogMapper;
	
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

	@Override
	public OperateLog getById(Integer id) {
		return operateLogMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void save(OperateLog operateLog) {
		operateLogMapper.insert(operateLog);
	}

	@Override
	@Transactional
	public void modify(OperateLog operateLog) {
		Date now = new Date();
		operateLog.setUpdateTime(now);
		operateLogMapper.updateByPrimaryKeySelective(operateLog);
	}

}
