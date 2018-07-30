package com.bt.om.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdApp;
import com.bt.om.entity.AdCustomerType;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.AdAppMapper;
import com.bt.om.mapper.AdCustomerTypeMapper;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ICustomerService;

/**
 * 广告主相关事务层
 */
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;
    @Autowired
    private AdCustomerTypeMapper adCustomerTypeMapper;
    @Autowired
    private AdAppMapper adAppMapper;
    
    private ThreadLocal<SimpleDateFormat> localFormat = new ThreadLocal<>();

    private SimpleDateFormat getFormat(){
        SimpleDateFormat format = localFormat.get();
        if(format == null){
            format = new SimpleDateFormat("yyyy-MM-dd");
            localFormat.set(format);
        }
        return format;
    }

    /**
     * 插入广告主
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserVo user) {
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        sysUserMapper.insertSelective(user);
        SysUserDetail detail = new SysUserDetail();
        detail.setUserId(user.getId());
        detail.setTelephone(user.getTelephone());
        detail.setCreateTime(now);
        detail.setUpdateTime(now);
        sysUserDetailMapper.insertSelective(detail);
        SysUserRole userRole = new SysUserRole();
        userRole.setPlatform(1);
        userRole.setUserId(user.getId());
        userRole.setRoleId(101); //customer: 101
        userRole.setCreateTime(now);
        userRole.setUpdateTime(now);
        sysUserRoleMapper.insertSelective(userRole);
        //创建app端账号
        SysUserExecute userExe = new SysUserExecute();
        AdApp adapp = adAppMapper.selectByPrimaryKey(user.getAppTypeId()); //通过appTypeId找到app_sid
        userExe.setUsername(user.getUsername());
        userExe.setPassword(user.getPassword());
        userExe.setUsertype(2);
        userExe.setOperateId(user.getId());
        userExe.setStatus(1);
        userExe.setRealname(user.getRealname());
        userExe.setMobile(user.getTelephone());
        userExe.setAppSid(adapp.getAppSid());
        userExe.setCreateTime(now);
        userExe.setUpdateTime(now);
        sysUserExecuteMapper.insertSelective(userExe);
    }

    /**
     * 修改广告主
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysUserVo user) {
        Date now = new Date();
        user.setUpdateTime(now);
        sysUserMapper.updateByPrimaryKeySelective(user);
        SysUserDetail detail = sysUserDetailMapper.selectByUserId(user.getId());
        if(!detail.getTelephone().equals(user.getTelephone())) {
            detail.setTelephone(user.getTelephone());
            detail.setUpdateTime(now);
            sysUserDetailMapper.updateByPrimaryKeySelective(detail);
        }
        SysUserExecute userExe = sysUserExecuteMapper.selectByUsername(user.getUsername());
        if(user.getPassword()!=null){
            userExe.setPassword(user.getPassword());
        }
        userExe.setRealname(user.getRealname());
        userExe.setMobile(user.getTelephone());
        AdApp adApp = adAppMapper.selectByPrimaryKey(user.getAppTypeId());
        userExe.setAppSid(adApp.getAppSid());
        sysUserExecuteMapper.updateByPrimaryKeySelective(userExe);
    }
    
    /**
     * 通过行业类型id查询行业类型信息
     */
    @Override
    public AdCustomerType selectById(Integer id) {
    	return adCustomerTypeMapper.selectByPrimaryKey(id);
    }
}
