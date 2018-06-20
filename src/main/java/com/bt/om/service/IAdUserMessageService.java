package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdUserMessage;
import com.bt.om.vo.web.SearchDataVo;

public interface IAdUserMessageService {

	void getPageData(SearchDataVo vo);

	void insertMessage(List<AdUserMessage> message);

}
