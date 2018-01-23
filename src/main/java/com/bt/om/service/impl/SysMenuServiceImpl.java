package com.bt.om.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bt.om.entity.vo.SysMenuVo;
import com.bt.om.mapper.SysMenuMapper;
import com.bt.om.service.ISysMenuService;

/**
 * SysMenu表数据服务层接口实现类
 *
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {

	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	public List<SysMenuVo> findMenuListByUsername(String username) {
		return sysMenuMapper.findMenuListByUsername(username);
	}
}