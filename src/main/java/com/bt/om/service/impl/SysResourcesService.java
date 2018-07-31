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

/**
 * 部门与组 相关事务层2
 */
@Service
public class SysResourcesService implements ISysResourcesService {
	
	@Autowired
	private SysResourcesMapper sysResourcesMapper;

	/**
	 * 通过id查询部门或组信息
	 */
	@Override
	public SysResources getById(Integer id) {
		return sysResourcesMapper.selectByPrimaryKey(id);
	}

	/**
	 * 插入部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources);
	}

	/**
	 * 更新部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateByPrimaryKey(SysResources sysResources) {
		sysResourcesMapper.updateByPrimaryKey(sysResources);
	}

	/**
	 * 分页查询部门或组信息
	 */
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

	/**
	 * 获取全部的部门/组信息
	 */
	@Override
	public List<SysResources> getAll() {
		return sysResourcesMapper.getAll();
	}

	/**
	 * 通过部门领导id查询部门信息
	 */
	@Override
	public List<SysResources> selectByUserId(Integer userId) {
		return sysResourcesMapper.selectByUserId(userId);
	}

	/**
	 * 插入部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources);
	}

	/**
	 * 更新部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modify(SysResources sysResources) {
		Date now = new Date();
		sysResources.setUpdateTime(now);
		sysResourcesMapper.updateByPrimaryKeySelective(sysResources);
	}
	
	/**
	 * 更新部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateByPrimaryKeyUserIdNull(SysResources sysResources) {
		Date now = new Date();
		sysResources.setUpdateTime(now);
		sysResourcesMapper.updateByPrimaryKeyUserIdNull(sysResources);
	}

	/**
	 * 查询部门领导管理的部门数量
	 */
	@Override
	public int selectCountByUserId(Integer userId) {
		return sysResourcesMapper.selectCountByUserId(userId);
	}
	
	/**
	 * 通过部门领导id查询部门信息
	 */
	@Override
	public SysResources getByUserId(Integer userId) {
		return sysResourcesMapper.getByUserId(userId);
	}

	/**
	 * 查询某一部门类型的部门领导id
	 */
	@Override
	public int getUserId(Integer departmentType) {
		return sysResourcesMapper.getUserId(departmentType);
	}

	/**
	 * 通过父部门id和组id查询 部门下其他组id集合
	 */
	@Override
	public Integer getResId(Integer id, Integer parentId) {
		return sysResourcesMapper.getResId(id,parentId);
	}

/*	@Override
	public List<SysResources> selectByUserId(List<Integer> groupIds) {
		return null;
	}*/
}
