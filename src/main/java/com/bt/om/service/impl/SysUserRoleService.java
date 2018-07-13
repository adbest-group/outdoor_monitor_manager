package com.bt.om.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysUserRoleService;

@Service
public class SysUserRoleService implements ISysUserRoleService {

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
	private SysUserResMapper sysUserResMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUserRole(UserRoleVo userRoleVo) {
		return sysUserRoleMapper.updateUserRole(userRoleVo);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer selectGroupIdByUserId(Map<String, Object> searchMap) {
		return sysUserResMapper.selectGroupIdByUserId(searchMap);
	}

}
