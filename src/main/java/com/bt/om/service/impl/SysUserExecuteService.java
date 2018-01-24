package com.bt.om.service.impl;

import com.bt.om.entity.SysUserExecute;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.service.ISysUserExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiting on 2018/1/23.
 */
@Service
public class SysUserExecuteService implements ISysUserExecuteService {
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;

    @Override
    public SysUserExecute getByUsername(String username) {
        return sysUserExecuteMapper.selectByUsername(username);
    }
}
