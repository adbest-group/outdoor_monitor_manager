package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.SysResources;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.service.ISysResourcesService;
import com.bt.om.vo.web.SearchDataVo;

@Service
public class SysResourcesService implements ISysResourcesService {
	@Autowired
	private SysResourcesMapper sysResourcesMapper;

	@Override
	public SysResources getById(Integer id) {
		return sysResourcesMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateByPrimaryKey(SysResources sysResources) {
		sysResourcesMapper.updateByPrimaryKey(sysResources);
	}

	@Override
	public void getPageData(SearchDataVo vo) {
		int count = sysResourcesMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(sysResourcesMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<SysResources>());
		}
	}

	@Override
	public List<SysResources> getAll() {
		return sysResourcesMapper.getAll();
	}

	@Override
	public List<SysResources> selectByUserId(Integer userId) {
		return sysResourcesMapper.selectByUserId(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modify(SysResources sysResources) {
		Date now = new Date();
		sysResources.setUpdateTime(now);
		sysResourcesMapper.updateByPrimaryKeySelective(sysResources);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateByPrimaryKeyUserIdNull(SysResources sysResources) {
		Date now = new Date();
		sysResources.setUpdateTime(now);
		sysResourcesMapper.updateByPrimaryKeyUserIdNull(sysResources);
	}

	@Override
	public int selectCountByUserId(Integer userId) {
		return sysResourcesMapper.selectCountByUserId(userId);
	}
	
	@Override
	public SysResources getByUserId(Integer userId) {
		return sysResourcesMapper.getByUserId(userId);
	}

/*	@Override
	public List<SysResources> selectByUserId(List<Integer> groupIds) {
		return null;
	}*/
}
