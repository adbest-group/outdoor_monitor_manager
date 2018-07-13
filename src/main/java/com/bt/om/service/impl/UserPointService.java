package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdPoint;
import com.bt.om.entity.AdUserPoint;
import com.bt.om.entity.SysResources;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.mapper.AdUserPointMapper;
import com.bt.om.service.IUserPointService;
import com.bt.om.vo.web.SearchDataVo;
@Service
public class UserPointService implements IUserPointService {

	@Autowired
	private AdUserPointMapper adUserPointMapper;

	@Override
	public void addUserPoint(AdUserPoint adUserPoint) {
		adUserPointMapper.insert(adUserPoint);
	}

	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adUserPointMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(adUserPointMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<SysResources>());
		}
	}

	//通过用户id查找该用户的所有积分数据
	@Override
	public List<AdUserPoint> getListById(Integer userId) {
		return adUserPointMapper.getListById(userId);
	}

	@Override
	public int getPageCount(SearchDataVo vo) {
		return adUserPointMapper.getPageCount(vo.getSearchMap());
		
	}

	@Override
	public Integer getPointCountById(Integer userId) {
		return adUserPointMapper.getPointCountById(userId);
	}

	@Override
	public void addByInvite(SysUserExecute sysUser, AdPoint adpoint, AdPoint adpointreg, String username, SysUserExecute sysUserExecute) {
		AdUserPoint adUserPoint = new AdUserPoint();
		Date now = new Date();
		//被邀请人积分增加
        adUserPoint.setUserId(sysUser.getId());
        adUserPoint.setPoint(adpoint.getPoint());
        adUserPoint.setResult("恭喜邀请注册成功！");
        adUserPoint.setCreateTime(now);
        adUserPoint.setUpdateTime(now);
        adUserPointMapper.insert(adUserPoint);
        
        adUserPoint.setUserId(sysUser.getId());
        adUserPoint.setPoint(adpointreg.getPoint());
        adUserPoint.setResult("恭喜注册成功！");
        adUserPoint.setCreateTime(now);
        adUserPoint.setUpdateTime(now);
        adUserPointMapper.insert(adUserPoint);
        //邀请人积分增加
		adUserPoint.setUserId(sysUserExecute.getId());
		adUserPoint.setPoint(adpoint.getPoint());
		adUserPoint.setResult("邀请用户"+username+"成功！");
		adUserPoint.setCreateTime(now);
		adUserPoint.setUpdateTime(now);
		adUserPointMapper.insert(adUserPoint);
		
	}

	@Override
	public void addByReg(SysUserExecute sysUser, AdPoint adpointreg) {
		AdUserPoint adUserPoint = new AdUserPoint();
		Date now = new Date();
		adUserPoint.setUserId(sysUser.getId());
        adUserPoint.setPoint(adpointreg.getPoint());
        adUserPoint.setResult("恭喜注册成功！");
        adUserPoint.setCreateTime(now);
        adUserPoint.setUpdateTime(now);
        adUserPointMapper.insert(adUserPoint);
	}
	
	

}
