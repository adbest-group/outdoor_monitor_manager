package com.bt.om.entity.vo;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;

import java.util.List;

/**
 * Created by caiting on 2018/1/16.
 */
public class SysUserVo extends SysUser {
    private List<SysMenuVo> menuList;
    private String telephone;
    private String prefix;
    private SysUserDetail userinfo;
    private String isOwn;
    private String customerTypeName;

    private String groupName;
    private String departmentName;
    
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public SysUserDetail getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(SysUserDetail userinfo) {
        this.userinfo = userinfo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<SysMenuVo> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenuVo> menuList) {
        this.menuList = menuList;
    }
    
    public String getIsOwn() {
		return isOwn;
	}
    
    public void setIsOwn(String isOwn) {
		this.isOwn = isOwn;
	}
    
    public String getCustomerTypeName() {
		return customerTypeName;
	}
    
    public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
  }
  
    public String getGroupName() {
		return groupName;
	}
    
    public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
    
    public String getDepartmentName() {
		return departmentName;
	}
    
    public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
