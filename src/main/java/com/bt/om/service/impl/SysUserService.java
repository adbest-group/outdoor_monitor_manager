package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.entity.vo.UserRoleVo;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserResMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * sys_user表数据服务层接口实现类
 */
@Service
public class SysUserService implements ISysUserService {

	@Autowired
	SysUserMapper sysUserMapper;
	@Autowired
	SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	SysUserDetailMapper sysUserDetailMapper;
	@Autowired
	SysUserResMapper sysUserResMapper;
	
	@Override
	public SysUserVo findUserinfoById(Integer id) {
		return sysUserMapper.findUserinfoById(id);
	}
	
	@Override
	public SysUserVo findByUsername(String username) {
		return sysUserMapper.findByUsername(username);
	}

	@Override
	public int getPageCount(Map<String, Object> searchMap) {
		return sysUserMapper.getPageCount(searchMap);
	}
	
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
	
	@Override
	public List<SysUserVo> isExistsName(String username) {
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

	@Override
	public boolean isExistsPrefix(String prefix,Integer id) {
		List<SysUserDetail> details = sysUserDetailMapper.isExistsPrefix(prefix,id);
		return details!=null&&details.size()>0;
	}
	
	@Override
	public SysUserDetail getUserDetail(Integer userId) {
		return sysUserDetailMapper.selectByUserId(userId);
	}

	/**
	 * 新增后台用户
	 */
	@Override
	public void addUser(SysUser sysUser, Integer roleId) {
		Date now = new Date();
		sysUser.setPassword(new Md5Hash(sysUser.getPassword(), sysUser.getUsername()).toString());
		sysUser.setCreateTime(now);
		sysUser.setUpdateTime(now);
		sysUserMapper.insertSelective(sysUser);
		SysUserDetail detail  = new SysUserDetail();
		detail.setUserId(sysUser.getId());
		detail.setTelephone(sysUser.getMobile());
		detail.setCreateTime(now);
		detail.setUpdateTime(now);
		sysUserDetailMapper.insertSelective(detail);
		SysUserRole userRole = new SysUserRole();
        userRole.setPlatform(1);
        userRole.setUserId(sysUser.getId());
        userRole.setRoleId(roleId);
        userRole.setCreateTime(now);
        userRole.setUpdateTime(now);
        sysUserRoleMapper.insertSelective(userRole);
	}

	@Override
	public List<SysUserVo> getAllByUserType(Integer userType) {
		return sysUserMapper.getAllByUserType(userType);
	}
	
	@Override
	public List<SysUser> getAvailableByUserType(Integer userType) {
		return sysUserMapper.getAvailableByUserType(userType);
	}
	
	@Override
	public List<SysUser> getIdNameByUserType(Integer userType) {
		return sysUserMapper.getIdNameByUserType(userType);
	}

	/**
	 * 新增部门领导
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int createDepartmentLeader(SysUser record, SysUserDetail sysUserDetail, SysUserRole sysUserRole) {
		//[1] 插入sys_user表
		int result = sysUserMapper.createDepartmentLeader(record);
		//[2] 插入sys_user_detail表
		sysUserDetail.setUserId(record.getId());
		sysUserDetailMapper.insert(sysUserDetail);
		//[3] 插入sys_user_role表
		sysUserRole.setUserId(record.getId());
		sysUserRoleMapper.insert(sysUserRole);
		return result;
	}

	@Override
	public List<SysUser> findLeaderList() {
		return sysUserMapper.findLeaderList();
	}

	@Override
	public int updatePasswordAndName(SysUser record) {
		return sysUserMapper.updatePasswordAndName(record);
	}

	@Override
	public int updateStatus(SysUser status) {
		return sysUserMapper.updateStatus(status);
	}

	@Override
	public List<SysUser> findAllTask() {
		return sysUserMapper.findAllTask();
	}

	@Override
	public int deleteUserById(Integer id) {
		return sysUserMapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 直接删除员工-组之间的关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteUserRess(SysUserRes sysUserRes, UserRoleVo userRoleVo) {
		//[1] 更新不在组内的员工角色信息
		if(userRoleVo.getUserIds().size() > 0) {
			sysUserRoleMapper.updateUserRole(userRoleVo);
    	}
		//[2] 批量删除操作
		return sysUserResMapper.deleteByResIdAndType(sysUserRes);
	}
	
	/**
	 * 更改某组后台用户的角色role
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateListUserRes(UserRoleVo userRoleVo) {
		if(userRoleVo.getUserIds().size() > 0) {
			return sysUserRoleMapper.updateUserRole(userRoleVo);
    	}
		return 0;
	}
	
	/**
	 * 更改员工-组之间的关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateUserRess(List<SysUserRes> sysUserRess, SysUserRes sysUserRes, UserRoleVo userRoleVo, UserRoleVo userRoleVo2) {
		//[1] 更新还在组内的员工角色信息
		if(userRoleVo.getUserIds().size() > 0) {
    		sysUserRoleMapper.updateUserRole(userRoleVo);
    	}
		//[2] 更新不在组内的员工角色信息
		if(userRoleVo2.getUserIds().size() > 0) {
			sysUserRoleMapper.updateUserRole(userRoleVo2);
    	}
		//[3] 批量删除操作
		sysUserResMapper.deleteByResIdAndType(sysUserRes);
		//[4] 批量插入操作
		return sysUserResMapper.insertUserRess(sysUserRess);
	}
	
	/**
	 * 更改广告商-组之间的关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertUserRess(List<SysUserRes> sysUserRess, SysUserRes sysUserRes) {
		//[1] 批量删除操作
		sysUserResMapper.deleteByResIdAndType(sysUserRes);
		//[2] 批量插入操作
		return sysUserResMapper.insertUserRess(sysUserRess);
	}
	
	/**
	 * 直接删除广告商-组之间的关系
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int deleteCustomerRess(SysUserRes sysUserRes) {
		//[1] 批量删除操作
		return sysUserResMapper.deleteByResIdAndType(sysUserRes);
	}

	@Override
	public List<Integer> getCustomerIdsByAdminId(Integer userId) {
		//[1] 查询员工admin所属组id
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("type", 1);
		searchMap.put("userId", userId);
		int groupId = sysUserResMapper.selectGroupIdByUserId(searchMap);
		//[2] 查询组包含的广告商id集合
		searchMap.clear();
		searchMap.put("type", 2);
		searchMap.put("resId", groupId);
		List<Integer> customerIds = sysUserResMapper.selectCustomerIdsByResId(searchMap);
		return customerIds;
	}

	@Override
	public List<Integer> selectUserIdsByResIds(Map<String, Object> searchMap) {
		return sysUserResMapper.selectUserIdsByResIds(searchMap);
	}

	@Override
	public int updateByPrimaryKeySelective(SysUser u) {
		return sysUserMapper.updateUserPwd(u);
	}

	@Override
	public List<Integer> getUserId(int i) {
		return sysUserMapper.getUserId(i);
	}

	@Override
	public SysUser getUserNameById(Integer customerId) {
		return sysUserMapper.selectByPrimaryKey(customerId);
	}

	@Override
	public SysUser getUserAppType(Integer userId) {
		return sysUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public void changeAppType(Integer id) {
		sysUserMapper.changeAppTypeById(id);
	}

	/**
	 * 修改后台用户信息
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
	public void modify(SysUser sysUser) {
		Date now = new Date();
		SysUser user = sysUserMapper.findUserinfoById(sysUser.getId());
		if(!sysUser.getPassword().equals("******")) {
			sysUser.setPassword(new Md5Hash(sysUser.getPassword(), sysUser.getUsername()).toString());
		}else {
			sysUser.setPassword(user.getPassword());
		}
		sysUser.setUpdateTime(now);
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
        SysUserDetail detail = sysUserDetailMapper.selectByUserId(sysUser.getId());
	
		if(!sysUser.getMobile().equals(detail.getTelephone())) {
			detail.setTelephone(sysUser.getMobile());
			detail.setUpdateTime(now);
			sysUserDetailMapper.updateByPrimaryKeySelective(detail);
		}
	}

}