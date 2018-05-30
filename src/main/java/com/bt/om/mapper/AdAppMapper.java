package com.bt.om.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdApp;

public interface AdAppMapper {

	int getPageCount(HashMap<String, Object> searchMap);

	List<?> getPageData(HashMap<String, Object> searchMap, RowBounds rowBounds);

	AdApp selectByPrimaryKey(Integer id);

	void updateByPrimaryKeySelective(AdApp adapp);

	void insertSelective(AdApp adapp);

}
