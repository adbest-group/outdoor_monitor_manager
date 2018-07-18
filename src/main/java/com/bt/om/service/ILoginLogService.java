package com.bt.om.service;

import com.bt.om.entity.LoginLog;
import com.bt.om.vo.web.SearchDataVo;

public interface ILoginLogService {
	
	public void save(LoginLog loginLog);
	public LoginLog getById(Integer id);
	public void getPageData(SearchDataVo vo);

}
