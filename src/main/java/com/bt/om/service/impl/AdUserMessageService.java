package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.AdUserMessage;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.vo.web.SearchDataVo;
@Service
public class AdUserMessageService implements IAdUserMessageService{

	@Autowired
	private AdUserMessageMapper adUserMessageMapper;
	
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adUserMessageMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adUserMessageMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdUserMessage>());
        }
	}

	@Override
	public void insertMessage(List<AdUserMessage> message) {
		adUserMessageMapper.insertMessage(message);
	}

}
