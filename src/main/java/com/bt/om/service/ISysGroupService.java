package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.SysResources;
import com.bt.om.vo.web.SearchDataVo;

public interface ISysGroupService {
	
	public void insert(SysResources sysResources);
	
	public void save(SysResources sysResources);
	
    public void modify(SysResources sysResources);

    public List<SysResources> selectById(Integer id);
    public SysResources getById(Integer id);
	void delete(Integer id);
	public void getPageData(SearchDataVo vo);
	public SysResources getByUserId(Integer userId);
}
