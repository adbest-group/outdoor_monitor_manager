package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdSystemPush;
import com.bt.om.mapper.AdSystemPushMapper;
import com.bt.om.service.IAdSystemPushService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 消息推送相关事务层
 */
@Service
public class AdSystemPushService implements IAdSystemPushService {
	
	@Autowired
	private AdSystemPushMapper adSystemPushMapper;

	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adSystemPushMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adSystemPushMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdSystemPush>());
        }
	}

	@Override
	@Transactional
	public void save(AdSystemPush adSystemPush) {
		adSystemPushMapper.insert(adSystemPush);
	}

}
