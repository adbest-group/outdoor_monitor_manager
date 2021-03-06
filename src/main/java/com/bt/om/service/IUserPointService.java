package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdPoint;
import com.bt.om.entity.AdUserPoint;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.vo.web.SearchDataVo;

public interface IUserPointService {

	void addUserPoint(AdUserPoint adUserPoint);

	void getPageData(SearchDataVo vo);

	//通过用户id查找该用户的所有积分数据
	List<AdUserPoint> getListById(Integer userId);

	int getPageCount(SearchDataVo vo);

	Integer getPointCountById(Integer userId);

	void addByInvite(SysUserExecute sysUser, AdPoint adpoint, AdPoint adpointreg, String username, SysUserExecute sysUserExecute);

	void addByReg(SysUserExecute sysUser, AdPoint adpointreg);


}
