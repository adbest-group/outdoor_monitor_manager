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
import com.bt.om.enums.UserRoleEnum;
import com.bt.om.mapper.SysResourcesMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysGroupService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 部门与组 相关事务层1
 */
@Service
public class SysGroupService implements ISysGroupService{
	
	@Autowired
	private SysResourcesMapper sysResourcesMapper;
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysUserResMapper sysUserResMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	/**
	 * 插入部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(SysResources sysResources) {
		sysResourcesMapper.insert(sysResources)	;
	}

	/**
	 * 保存部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysResources sysResources) {
		sysResourcesMapper.addGroup(sysResources);
	}

	/**
	 * 修改部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modify(SysResources sysResources) {
		sysResourcesMapper.updateGroupName(sysResources);		
	}

	/**
	 * 删除部门或组信息
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Integer id) {
		sysResourcesMapper.deleteGroup(id);		
	}

	/**
	 * 通过id查询部门或组信息
	 */
	@Override
	public SysResources getById(Integer id) {
		 return sysResourcesMapper.selectByPrimaryKey(id);
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
	 * 通过部门领导id查询部门信息
	 */
	@Override
	public SysResources getByUserId(Integer userId) {
		return sysResourcesMapper.getByUserId(userId);
	}

	/**
	 * 通过组id查询所有与该组关联的员工信息
	 */
	@Override
	public List<SysUser> selectUserName(Integer groupId) {
		return sysUserMapper.findUserName(groupId);
	}

	/**
	 * 获取属于某组下的广告主信息
	 */
	@Override
	public List<SysUser> selectCustomerName(Integer groupId) {
		return sysUserMapper.findCustomerName(groupId);
	}

	/**
	 * 获取不属于任何组的员工信息
	 */
	@Override
	public List<SysUser> selectNoUserName(Integer groupId) {
		return sysUserMapper.findNoUserName(groupId);
	}

	/**
	 * 获取不属于某部门下任何组的广告主信息
	 */
	@Override
	public List<SysUser> selectNoCustomerName(Map<String, Object> searchMap) {
		return sysUserMapper.findNoCustomerName(searchMap);
	}

	/**
	 * 查询某部门下组的id集合
	 */
	@Override
	public List<Integer> selectGroupIdsByDepartmentId(Integer parentId) {
		return sysResourcesMapper.selectGroupIdsByDepartmentId(parentId);
	}

	/**
	 * 删除组, 同时删除与该组相关的用户关联关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(Integer id) {
		 List<SysUser> sysUsers=sysUserMapper.findUserName(id);
		 List<Integer> userIds = new ArrayList<>(); //存所有与该组有关系的员工的id集合
         for (SysUser sysUser : sysUsers) {
         	userIds.add(sysUser.getId());
         }
         UserRoleVo userRoleVo = new UserRoleVo();
         userRoleVo.setRoleId(UserRoleEnum.ADMIN.getId());
         userRoleVo.setUserIds(userIds);
         Date now = new Date();
         userRoleVo.setUpdateTime(now);
         if(userIds != null && userIds.size() > 0) {
        	 sysUserRoleMapper.updateUserRole(userRoleVo);
         }
         sysResourcesMapper.deleteGroup(id);
         sysUserResMapper.deleteByResId(id);
  }

	/**
	 * 删除部门或组信息
	 */
	@Override
	public int deleteGroupById(Integer id) {
		return sysResourcesMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 获取全部的部门或组信息
	 */
	@Override
	public List<SysResources> getAllDepartment(int i) {
		return sysResourcesMapper.getAllDepartment(i);
	}
}
