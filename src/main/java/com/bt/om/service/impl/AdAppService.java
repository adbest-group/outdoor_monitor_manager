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

@Service
public class AdAppService implements IAppService {

	@Autowired
	private AdAppMapper adAppMapper;
	
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

	@Override
	public AdApp getVoById(Integer id) {
		return adAppMapper.selectByPrimaryKey(id);
	}

	@Override
	public void modify(AdApp adapp) {
		// TODO Auto-generated method stub
		Date date = new Date();	
		adapp.setUpdateTime(date);
		adAppMapper.updateByPrimaryKeySelective(adapp);
	}

	@Override
	public void save(AdApp adapp) {
		adAppMapper.insert(adapp);
	}

	@Override
	public List<AdApp> getAllAppType() {
		return adAppMapper.getAllAppType();
	}

	@Override
	public int deleteAppById(Integer id) {
		return adAppMapper.deleteByPrimaryKey(id);
	}

	@Override
	public AdApp selectById(Integer id) {
		return adAppMapper.selectByPrimaryKey(id);
	}

}
