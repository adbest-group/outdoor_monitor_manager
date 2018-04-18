package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.mapper.AdMediaTypeMapper;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by jiayong.mao on 2018/4/13.
 */
@Service
public class AdMediaTypeService implements IAdMediaTypeService {

	@Autowired
	private AdMediaTypeMapper adMediaTypeMapper;
	
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adMediaTypeMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMediaTypeMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMediaType>());
        }
	}

	@Override
	public AdMediaType getById(Integer id) {
		return adMediaTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void save(AdMediaType adMediaType) {
		adMediaTypeMapper.insert(adMediaType);
	}

	@Override
	@Transactional
	public void modify(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateByPrimaryKeySelective(adMediaType);
	}

	@Override
	public List<AdMediaType> getParentMedia(Integer mediaType) {
		return adMediaTypeMapper.getParentMedia(mediaType);
	}
	
	@Override
	public List<AdMediaType> getParentMediaAvailable(Integer mediaType) {
		return adMediaTypeMapper.getParentMediaAvailable(mediaType);
	}

	@Override
	@Transactional
	public void updateStatusById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		if(adMediaType.getMediaType() == 1) {
			//更新父类
			adMediaTypeMapper.updateStatusById(adMediaType);
			//更新子类
			adMediaTypeMapper.updateStatusByParentId(adMediaType);
		} else {
			//更新子类
			adMediaTypeMapper.updateStatusById(adMediaType);
		}
	}

	@Override
	@Transactional
	public void updateNeedById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateNeedById(adMediaType);
	}

	@Override
	public List<AdMediaTypeVo> selectParentAndSecond() {
		return adMediaTypeMapper.selectParentAndSecond();
	}
}
