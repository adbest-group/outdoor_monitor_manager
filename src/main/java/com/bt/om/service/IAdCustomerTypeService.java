package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdCustomerType;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by jiayong.mao on 2018/4/25.
 */
public interface IAdCustomerTypeService {
	public void getPageData(SearchDataVo vo);
    public AdCustomerType getById(Integer id);
    public void save(AdCustomerType adCustomerType);
    public void modify(AdCustomerType adCustomerType);
    public List<AdCustomerType> getAll();
}
