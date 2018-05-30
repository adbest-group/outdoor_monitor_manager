package com.bt.om.service;

import com.bt.om.entity.AdApp;
import com.bt.om.vo.web.SearchDataVo;

public interface IAppService {

	void getPageData(SearchDataVo vo);

	AdApp getVoById(Integer id);

	void modify(AdApp adapp);

	void save(AdApp adapp);

}
