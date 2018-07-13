package com.bt.om.entity.vo;

import com.bt.om.entity.SysResources;

public class SysResourcesVo extends SysResources {

	private String leaderName; //查询部门时, 部门领导的名字
	
	private String departmentName; //查询组时, 所属部门的名称
	
	public String getLeaderName() {
		return leaderName;
	}
	
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
