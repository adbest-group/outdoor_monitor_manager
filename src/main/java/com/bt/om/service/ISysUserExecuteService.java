package com.bt.om.service;

import com.bt.om.entity.SysUserExecute;

/**
 * Created by caiting on 2018/1/23.
 */
public interface ISysUserExecuteService {
    public SysUserExecute getByUsername(String username);
}
