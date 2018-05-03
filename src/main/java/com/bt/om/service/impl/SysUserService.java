package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ISysUserService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * OttvUser表数据服务层接口实现类
 */
@Service
public class SysUserService implements ISysUserService {

	@Autowired
	SysUserMapper sysUserMapper;
	@Autowired
	SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	SysUserDetailMapper sysUserDetailMapper;
	
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
	public void addUser(SysUser sysUser) {
		
	}

	@Override
	public List<SysUserVo> getAllByUserType(Integer userType) {
		return sysUserMapper.getAllByUserType(userType);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int createDepartmentLeader(SysUser record, SysUserDetail sysUserDetail, SysUserRole sysUserRole) {
		//[1] 插入sys_user表
		int result = sysUserMapper.createDepartmentLeader(record);
		//[2] 插入sys_user_detail表
		sysUserDetail.setUserId(record.getId());
		sysUserDetailMapper.insert(sysUserDetail);
		//[3] 插入sys_user_role表
		sysUserDetail.setUserId(record.getId());
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

}