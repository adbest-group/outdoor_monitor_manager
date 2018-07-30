package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdApp;
import com.bt.om.mapper.AdAppMapper;
import com.bt.om.service.IAppService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * APP相关事务层
 */
@Service
public class AdAppService implements IAppService {

	@Autowired
	private AdAppMapper adAppMapper;
	
	/**
	 * 分页查询APP信息
	 */
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adAppMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adAppMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdApp>());
        }

	}

	/**
	 * 通过id查询APP信息
	 */
	@Override
	public AdApp getVoById(Integer id) {
		return adAppMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改APP信息
	 */
	@Override
	public void modify(AdApp adapp) {
		// TODO Auto-generated method stub
		Date date = new Date();	
		adapp.setUpdateTime(date);
		adAppMapper.updateByPrimaryKeySelective(adapp);
	}

	/**
	 * 插入APP信息
	 */
	@Override
	public void save(AdApp adapp) {
		adAppMapper.insert(adapp);
	}

	/**
	 * 获取全部APP信息
	 */
	@Override
	public List<AdApp> getAllAppType() {
		return adAppMapper.getAllAppType();
	}

	/**
	 * 删除一条APP信息
	 */
	@Override
	public int deleteAppById(Integer id) {
		return adAppMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 通过id查询APP信息
	 */
	@Override
	public AdApp selectById(Integer id) {
		return adAppMapper.selectByPrimaryKey(id);
	}

	/**
	 * 通过sid查询APP信息
	 */
	@Override
	public AdApp selectAppPicUrlAndTitleBySid(String sid) {
		return adAppMapper.getAppPicUrlAndTitleBySid(sid);
	}


}
