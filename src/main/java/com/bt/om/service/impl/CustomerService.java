package com.bt.om.service.impl;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.ICustomerService;
import com.bt.om.service.IMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caiting on 2018/2/27.
 */
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    private ThreadLocal<SimpleDateFormat> localFormat = new ThreadLocal<>();

    private SimpleDateFormat getFormat(){
        SimpleDateFormat format = localFormat.get();
        if(format == null){
            format = new SimpleDateFormat("yyyy-MM-dd");
            localFormat.set(format);
        }
        return format;
    }

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
        userRole.setRoleId(101);
        userRole.setCreateTime(now);
        userRole.setUpdateTime(now);
        sysUserRoleMapper.insertSelective(userRole);
    }

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
    }
}
