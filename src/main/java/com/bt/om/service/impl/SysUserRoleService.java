package com.bt.om.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysUserRoleService;

/**
 * 后台用户角色相关事务层
 */
@Service
public class SysUserRoleService implements ISysUserRoleService {

	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
	private SysUserResMapper sysUserResMapper;
	
	/**
	 * 更新用户角色
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUserRole(UserRoleVo userRoleVo) {
		return sysUserRoleMapper.updateUserRole(userRoleVo);
	}
	
	/**
	 * 通过后台用户id和关联类型查询 关联的组id
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer selectGroupIdByUserId(Map<String, Object> searchMap) {
		return sysUserResMapper.selectGroupIdByUserId(searchMap);
	}

}
