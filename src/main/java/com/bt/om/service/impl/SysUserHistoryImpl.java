package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.SysUserHistory;
import com.bt.om.mapper.SysUserHistoryMapper;
import com.bt.om.service.ISysUserHistoryService;
import com.bt.om.vo.web.SearchDataVo;
@Service
public class SysUserHistoryImpl implements ISysUserHistoryService {

	@Autowired
	private SysUserHistoryMapper sysUserHistoryMapper;
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = sysUserHistoryMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(sysUserHistoryMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<SysUserHistory>());
        }
	}

}
