package com.bt.om.service;

import java.util.List;
import java.util.Map;

import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUser;
import com.bt.om.vo.web.SearchDataVo;

public interface ISysGroupService {
	
	public void insert(SysResources sysResources);
	
	public void save(SysResources sysResources);
	
    public void modify(SysResources sysResources);

    public SysResources getById(Integer id);
	void delete(Integer id);
	public void getPageData(SearchDataVo vo);
	public SysResources getByUserId(Integer userId);
	public List<SysUser> selectUserName(Integer groupId);
	public List<SysUser> selectCustomerName(Integer groupId);
	public List<SysUser> selectNoUserName(Integer groupId);
	public List<SysUser> selectNoCustomerName(Map<String, Object> searchMap);
	public List<Integer> selectGroupIdsByDepartmentId(Integer parentId);

	public int deleteGroupById(Integer id);
}
