package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdSystemPush;
import com.bt.om.vo.web.SearchDataVo;

public interface IAdSystemPushService {

	public void getPageData(SearchDataVo vo);
    public void save(AdSystemPush adSystemPush);
	public void add(AdSystemPush push);
}
