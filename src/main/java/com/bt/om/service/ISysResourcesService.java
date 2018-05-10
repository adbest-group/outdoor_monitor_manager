package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.SysResources;
import com.bt.om.vo.web.SearchDataVo;

public interface ISysResourcesService {
	public SysResources getById(Integer id);
	
	public void insert(SysResources sysResources);
	
	public void save(SysResources sysResources);
	
    public void modify(SysResources sysResources);

    public void updateByPrimaryKey(SysResources sysResources);

    public void getPageData(SearchDataVo vo);

    public List<SysResources> getAll();

    public List<SysResources> selectByUserId(Integer userId);

    public int selectCountByUserId(Integer userId);
}
