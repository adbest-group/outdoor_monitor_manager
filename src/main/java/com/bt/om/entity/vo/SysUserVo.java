package com.bt.om.entity.vo;

import com.bt.om.entity.SysUser;

import java.util.List;

/**
 * Created by caiting on 2018/1/16.
 */
public class SysUserVo extends SysUser {
    private List<SysMenuVo> menuList;

    public List<SysMenuVo> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenuVo> menuList) {
        this.menuList = menuList;
    }
}
