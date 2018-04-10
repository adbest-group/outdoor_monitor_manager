package com.bt.om.service;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.vo.web.SearchDataVo;

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

    public void getPageData(SearchDataVo vo);

    public SysUserExecute getById(Integer id);
    List<SysUserExecute> isExistsName(String username);

    public void add(SysUserExecute userExecute);
    public void modify(SysUserExecute userExecute);
}
