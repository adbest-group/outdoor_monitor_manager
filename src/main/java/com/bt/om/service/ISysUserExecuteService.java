package com.bt.om.service;

import com.bt.om.entity.SysUserExecute;

import java.util.List;
import java.util.Map;

/**
 * Created by caiting on 2018/1/23.
 */
public interface ISysUserExecuteService {
    public SysUserExecute getByUsername(String username);
    /**
     * map 为查询条件组成
     **/
    public List<SysUserExecute> getByConditionMap(Map map);
}
