package com.bt.om.service;

import com.bt.om.vo.web.SearchDataVo;

public interface IUserMoneyService {

	void getPageData(SearchDataVo vo);

	Double getMoneyCountById(Integer userId);


}
