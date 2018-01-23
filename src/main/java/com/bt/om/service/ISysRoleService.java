package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.vo.SysRoleVo;

/**
 * 
 * 
 * @author tanyong
 * @version $Id: SysRoleService.java, v 0.1 2016年12月2日 下午2:36:01 tanyong Exp $
 */
public interface ISysRoleService {

	// void insertRole(Integer roleId, String name, String desc, String menu,
	// Integer parentId);

	// int getRoleCount(HashMap<String, Object> searchMap);

	// List<SysRole> getRoleData(SearchDataVo vo);

	List<SysRoleVo> findRoleByUserId(Integer userId);

	// List<SysRole> isExistsName(Map<String, Object> map);

	// SysRole getUserRoleName(Map<String, Object> map);
}