package com.bt.om.entity.vo;

import com.bt.om.entity.SysRole;

public class SysRoleVo extends SysRole {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2765425369236795946L;
	private Boolean isSelect;

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		this.isSelect = isSelect;
	}
}