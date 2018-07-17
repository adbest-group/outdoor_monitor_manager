package com.bt.om.service.impl;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.LoginLog;
import com.bt.om.mapper.LoginLogMapper;
import com.bt.om.service.ILoginLogService;
import com.bt.om.vo.web.SearchDataVo;

@Service
public class LoginLogService implements ILoginLogService{
	@Autowired
	private  LoginLogMapper loginLogMapper;	
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = loginLogMapper.getPageCountSysUser(vo.getSearchMap());	
    	String type = (String) vo.getSearchMap().get("type"); 
		if(type.equals("0") ) {	
	    	vo.setCount(count);
	    	if(count > 0) {
	    		vo.setList(loginLogMapper.getPageDataSysUser(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
	    	}else {
	    		vo.setList(new ArrayList<LoginLog>());
	    	} 
		}
    	if(type.equals("1")) {
	    	int countExecute = loginLogMapper.getPageCountSysUserExecute(vo.getSearchMap());
	        vo.setCount(countExecute);
	        if (countExecute > 0 ){
	        	vo.setList(loginLogMapper.getPageDataSysUserExecute(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
	        } else {
	        	vo.setList(new ArrayList<LoginLog>());
	        } 
    	}
       
	}
	@Override
	public void save(LoginLog loginLog) {
		loginLogMapper.insertSelective(loginLog);		
	}
	@Override
	public LoginLog getById(Integer id) {
		return loginLogMapper.selectByPrimaryKey(id);
	}

}
