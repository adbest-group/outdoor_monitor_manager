package com.bt.om.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.SysRole;
import com.bt.om.mapper.SysRoleMapper;
import com.bt.om.service.ISysRoleService;

/**
 * 后台角色 相关事务层
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {

	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	/**
	 * 通过用户id查询出角色信息
	 */
	@Override
	public List<SysRole> findRoleByUserId(Integer userId) {
		return sysRoleMapper.selectByUserId(userId);
	}
}