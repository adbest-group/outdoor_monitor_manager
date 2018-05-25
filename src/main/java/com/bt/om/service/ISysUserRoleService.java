package com.bt.om.service;

import java.util.Map;

import com.bt.om.entity.vo.UserRoleVo;

public interface ISysUserRoleService {

	public int updateUserRole(UserRoleVo userRoleVo);

	public int selectGroupIdByUserId(Map<String, Object> searchMap);
}
