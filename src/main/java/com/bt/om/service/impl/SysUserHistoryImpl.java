package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.SysUserHistory;
import com.bt.om.mapper.SysUserHistoryMapper;
import com.bt.om.service.ISysUserHistoryService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * APP用户公司历史 相关业务层
 */
@Service
public class SysUserHistoryImpl implements ISysUserHistoryService {

	@Autowired
	private SysUserHistoryMapper sysUserHistoryMapper;

	/**
	 * 分页查询APP用户公司历史
	 */
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
