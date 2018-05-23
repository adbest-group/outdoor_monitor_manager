package com.bt.om.service;

import com.bt.om.entity.AdCustomerType;
import com.bt.om.entity.vo.SysUserVo;

/**
 * Created by caiting on 2018/2/27.
 */
public interface ICustomerService {
    public void add(SysUserVo user);
    public void modify(SysUserVo user);
	AdCustomerType selectById(Integer id);
}
