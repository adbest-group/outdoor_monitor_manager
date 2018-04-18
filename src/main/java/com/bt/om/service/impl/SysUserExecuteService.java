package com.bt.om.service.impl;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.vo.web.SearchDataVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<SysUserExecute> getByConditionMap(Map map) {
        return sysUserExecuteMapper.selectByConditionMap(map);
    }

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = sysUserExecuteMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(sysUserExecuteMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTask>());
        }
    }

    @Override
    public SysUserExecute getById(Integer id) {
        return sysUserExecuteMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SysUserExecute> isExistsName(String username) {
        return sysUserExecuteMapper.isExistsName(username,null);
    }

    @Override
    public List<SysUserExecute> isExistsName(String username,Integer id) {
        return sysUserExecuteMapper.isExistsName(username,id);
    }

    @Override
    public void add(SysUserExecute userExecute) {
        sysUserExecuteMapper.insertSelective(userExecute);
    }

    @Override
    public void modify(SysUserExecute userExecute) {
        sysUserExecuteMapper.updateByPrimaryKeySelective(userExecute);
    }
}
