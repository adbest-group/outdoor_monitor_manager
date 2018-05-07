package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.service.ISysGroupService;
import com.bt.om.vo.web.SearchDataVo;

@Service
public class SysGroupService implements ISysGroupService{
	@Autowired
	private SysResourcesMapper sysResourcesMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources)	;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysResources sysResources) {
		sysResourcesMapper.addGroup(sysResources);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modify(SysResources sysResources) {
		sysResourcesMapper.updateGroupName(sysResources);		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Integer id) {
		sysResourcesMapper.deleteGroup(id);		
	}

	@Override
	public List<SysResources> selectById(Integer id) {
		// TODO Auto-generated method stub
		return sysResourcesMapper.findGroupById(id);
	}

	@Override
	public SysResources getById(Integer id) {
		// TODO Auto-generated method stub
		 return sysResourcesMapper.selectByPrimaryKey(id);
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
	public SysResources getByUserId(Integer userId) {
		return sysResourcesMapper.getByUserId(userId);
	}

	@Override
	public List<SysUser> selectUserName(Integer groupId) {
		return sysUserMapper.findUserName(groupId);
	}

	@Override
	public List<SysUser> selectCustomerName(Integer groupId) {
		return sysUserMapper.findCustomerName(groupId);
	}

	@Override
	public List<SysUser> selectNoUserName(Integer groupId) {
		// TODO Auto-generated method stub
		return sysUserMapper.findNoUserName(groupId);
	}

	@Override
	public List<SysUser> selectNoCustomerName(Integer groupId) {
		// TODO Auto-generated method stub
		return sysUserMapper.findNoCustomerName(groupId);
	}
}
