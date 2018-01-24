package com.bt.om.vo.api;

import com.bt.om.entity.SysUserExecute;

import java.io.Serializable;

/**
 * Created by caiting on 2018/1/23.
 */
public class SysUserExecuteVo implements Serializable{
    private Integer userId;
    private String realname;
    private String company;
    private Integer usertype;

    public SysUserExecuteVo(SysUserExecute sue){
        this.userId = sue.getId();
        this.realname = sue.getRealname();
        this.company = sue.getCompany();
        this.usertype = sue.getUsertype();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }
}
