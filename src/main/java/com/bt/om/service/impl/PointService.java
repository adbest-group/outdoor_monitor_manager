package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdPoint;
import com.bt.om.entity.SysResources;
import com.bt.om.mapper.AdPointMapper;
import com.bt.om.service.IPointService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 积分 相关事务层
 */
@Service
public class PointService implements IPointService {

	@Autowired
	private AdPointMapper adPointMapper;
	
	/**
	 * 将积分添加进积分设置表
	 * */
	@Override
	public void addUserPoint() {
		
	}

	/**
	 * 查找当前积分类型下的积分设置数据
	 * */
	@Override
	public AdPoint findPointValue(int type) {
		return adPointMapper.selectByPointType(type);
	}

	/**
	 * 查找所有符合条件的数据
	 * */
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adPointMapper.getPageCount(vo.getSearchMap());
		vo.setCount(count);
		if (count > 0) {
			vo.setList(adPointMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
		} else {
			vo.setList(new ArrayList<SysResources>());
		}
	}

	/**
	 * 通过id查询积分设置信息
	 */
	@Override
	public AdPoint getVoById(Integer id) {
		return adPointMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改积分设置
	 */
	@Override
	public void modify(AdPoint adpoint) {
		adPointMapper.updateByPrimaryKeySelective(adpoint);
	}

	/**
	 * 插入一条积分设置
	 */
	@Override
	public void save(AdPoint adpoint) {
		adPointMapper.insert(adpoint);
	}

}
