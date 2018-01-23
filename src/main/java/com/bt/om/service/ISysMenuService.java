package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.vo.SysMenuVo;

/**
 *
 * SysMenu表数据服务层接口
 *
 */
public interface ISysMenuService {

	List<SysMenuVo> findMenuListByUsername(String username);
}