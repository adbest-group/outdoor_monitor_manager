package com.bt.om.service;

import com.bt.om.entity.AdPoint;
import com.bt.om.vo.web.SearchDataVo;

public interface IPointService {

	void addUserPoint();

	AdPoint findPointValue(int i);

	void getPageData(SearchDataVo vo);

	AdPoint getVoById(Integer id);

	void modify(AdPoint adpoint);

	void save(AdPoint adpoint);

}
