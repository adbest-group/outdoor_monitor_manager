package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysGroupService;
import com.bt.om.vo.web.SearchDataVo;

@Service
public class SysGroupService implements ISysGroupService{
	@Autowired
	private SysResourcesMapper sysResourcesMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	SysUserResMapper sysUserResMapper;
	@Autowired
	SysUserRoleMapper sysUserRoleMapper;
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
	public SysResources getById(Integer id) {
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
		return sysUserMapper.findNoUserName(groupId);
	}

	@Override
	public List<SysUser> selectNoCustomerName(Map<String, Object> searchMap) {
		return sysUserMapper.findNoCustomerName(searchMap);
	}

	@Override
	public List<Integer> selectGroupIdsByDepartmentId(Integer parentId) {
		return sysResourcesMapper.selectGroupIdsByDepartmentId(parentId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(Integer id) {
		 List<SysUser> sysUsers=sysUserMapper.findUserName(id);
		 List<Integer> userIds = new ArrayList<>(); //存所有与该组有关系的员工的id集合
         for (SysUser sysUser : sysUsers) {
         	userIds.add(sysUser.getId());
         }
         UserRoleVo userRoleVo = new UserRoleVo();
         userRoleVo.setRoleId(100);
         userRoleVo.setUserIds(userIds);
         Date now = new Date();
         userRoleVo.setUpdateTime(now);
         sysUserRoleMapper.updateUserRole(userRoleVo);
         sysResourcesMapper.deleteGroup(id);
         sysUserResMapper.deleteByResId(id);
  }

	@Override
	public int deleteGroupById(Integer id) {
		return sysResourcesMapper.deleteByPrimaryKey(id);
	}
}
