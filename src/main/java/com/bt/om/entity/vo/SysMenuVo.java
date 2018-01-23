package com.bt.om.entity.vo;

import java.util.ArrayList;
import java.util.List;

import com.bt.om.entity.SysMenu;

public class SysMenuVo extends SysMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6170023652221230143L;

	private List<SysMenuVo> childList;

	public List<SysMenuVo> getChildList() {
		return childList;
	}

	public void setChildList(List<SysMenuVo> childList) {
		this.childList = childList;
	}

	public void add(SysMenuVo menu) {
		if (null == childList) {
			childList = new ArrayList<SysMenuVo>();
		}
		childList.add(menu);
	}
}