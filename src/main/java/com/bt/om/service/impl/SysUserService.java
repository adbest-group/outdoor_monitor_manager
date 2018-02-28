package com.bt.om.service.impl;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import com.bt.om.mapper.OttvUserinfoManagerMapper;

/**
 *
 * OttvUser表数据服务层接口实现类
 *
 */
@Service
public class SysUserService implements ISysUserService {


	@Autowired
	SysUserMapper sysUserMapper;
	@Autowired
	SysUserRoleMapper sysUserRoleMapper;
//	@Autowired
//	OttvUserinfoManagerMapper ottvUserinfoManagerMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.bt.om.service.IOttvUserService#findUserinfoById(java.lang.Integer)
	 */
	@Override
	public SysUserVo findUserinfoById(Integer id) {
		return sysUserMapper.findUserinfoById(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bt.om.service.IOttvUserService#findByUsername(java.lang.String)
	 */
	@Override
	public SysUserVo findByUsername(String username) {
		return sysUserMapper.findByUsername(username);
	}

	/*
	 * (non-Javadoc)
	 * @see com.bt.om.service.IOttvUserService#getPageCount(java.util.Map)
	 */
	@Override
	public int getPageCount(Map<String, Object> searchMap) {
		return sysUserMapper.getPageCount(searchMap);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bt.om.service.IOttvUserService#getPageData(com.bt.om.vo.web.SearchDataVo)
	 */
	@Override
	public List<SysUserVo> getPageData(SearchDataVo vo) {
		int count = sysUserMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if(count>0){
			vo.setList(sysUserMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		}else{
			vo.setList(new ArrayList<>());
		}
		return (List<SysUserVo>)vo.getList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.bt.om.service.IOttvUserService#isExistsName(java.lang.String)
	 */
	@Override
	public List<SysUser> isExistsName(String username) {
		return sysUserMapper.isExistsName(username);
	}

	@Override
	public int update(SysUserVo user) {
		if(user.getId()!=null){
			user.setUpdateTime(new Date());
			return sysUserMapper.updateByPrimaryKeySelective(user);
		}
		return -1;
	}

//	/*
//	 * (non-Javadoc)
//	 * @see com.bt.om.service.IOttvUserService#saveUser(com.bt.om.entity.vo.OttvUserVo, java.lang.String)
//	 */
//	/**
//	 * @param userVo
//	 * @param roleIds
//	 */
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public void saveUser(OttvUserVo userVo, String roleIds) {
//
//		Date sysDate = new Date();
//		OttvUser userInfo = null;
//		OttvUserinfoManager userinfoManager = null;
//		int updCount = 0;
//
//		// 更新登录账号信息
//		if (userVo.getId() != null) {
//			// 获取原先账号信息
//			userInfo = ottvUserMapper.selectByPrimaryKey(userVo.getId());
//
//			// 删除原有账号权限
//			ottvUserRoleMapper.deleteRoleByUserId(userInfo.getId(), 1);
//
//			if ("******".equals(userVo.getPassword())) {
//				userInfo.setPassword(null);
//			} else {
//				userInfo.setPassword(new Md5Hash(userVo.getPassword(), userInfo.getUsername()).toString());
//			}
//			userInfo.setUpdateTime(sysDate);
//			// 更新登录账号信息
//			updCount = ottvUserMapper.updateByPrimaryKeySelective(userInfo);
//			if (updCount != 1) {
//				throw new RuntimeException("更新登录账号信息失败");
//			}
//
//			// 更新账号信息
////			userinfoManager = ottvUserinfoManagerMapper.selectByPrimaryKey(userInfo.getUserinfoId());
////			userinfoManager.setName(userVo.getUserinfo().getName());
////			userinfoManager.setTelephone(userVo.getUserinfo().getTelephone());
////			userinfoManager.setUpdateTime(sysDate);
////			updCount = ottvUserinfoManagerMapper.updateByPrimaryKey(userinfoManager);
//			if (updCount != 1) {
//				throw new RuntimeException("更新账号信息失败");
//			}
//		} else {
//			// 新建账号信息并返回信息ID
//			userinfoManager = new OttvUserinfoManager();
//			userinfoManager.setName(userVo.getUserinfo().getName());
//			userinfoManager.setTelephone(userVo.getUserinfo().getTelephone());
//			userinfoManager.setCreateTime(sysDate);
//			userinfoManager.setUpdateTime(sysDate);
//			userinfoManager.setOperateId(userVo.getOperateId());
////			updCount = ottvUserinfoManagerMapper.insertSelective(userinfoManager);
//			if (updCount != 1) {
//				throw new RuntimeException("新建账号信息失败");
//			}
//
//			// 新建登录账号信息并返回用户ID
//			userInfo = new OttvUser();
//			userInfo.setPlatform(1);
//			userInfo.setUsername(userVo.getUsername());
//			userInfo.setPassword(new Md5Hash(userVo.getPassword(), userInfo.getUsername()).toString());
//			userInfo.setParentId(0);
//			userInfo.setUserinfoId(userinfoManager.getId());
//			userInfo.setCreateTime(sysDate);
//			userInfo.setUpdateTime(sysDate);
//			userInfo.setOperateId(userVo.getOperateId());
//			updCount = ottvUserMapper.insertSelective(userInfo);
//			if (updCount != 1) {
//				throw new RuntimeException("新建登录账号信息失败");
//			}
//		}
//
//		// 根据设定的账号权限和获取到的用户ID新建用户权限信息
//		String[] roleIdArr = roleIds.split(",");
//		for (String roleId : roleIdArr) {
//			OttvUserRole userRole = new OttvUserRole();
//			userRole.setPlatform(1);
//			userRole.setUserId(userInfo.getId());
//			userRole.setRoleId(Integer.parseInt(roleId));
//			userRole.setCreateTime(sysDate);
//			updCount = ottvUserRoleMapper.insertSelective(userRole);
//			if (updCount != 1) {
//				throw new RuntimeException("新建用户权限信息失败");
//			}
//		}
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.bt.om.service.IOttvUserService#delUserById(java.lang.Integer)
//	 */
//	@Override
//	public int delUserById(Integer id) {
//		return ottvUserMapper.deleteUser(id);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see com.bt.om.service.IOttvUserService#updateUserStatus(java.lang.Integer, java.lang.Integer)
//	 */
//	@Override
//	public int updateUserStatus(Integer id, Integer status) {
//		return ottvUserMapper.updateUserStatus(id, status);
//	}
//	@Override
//	public void addUser(OttvUser ottvUser) {
//		ottvUserMapper.insert(ottvUser);
//	}
//
//	@Override
//	public List<OttvUserVo> findAllSale() {
//		return ottvUserMapper.selectAll();
//	}
}