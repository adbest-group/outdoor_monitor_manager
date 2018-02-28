package com.bt.om.service.impl;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserMapper;
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
public class MediaService implements IMediaService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;
    @Autowired
    private AdMediaMapper adMediaMapper;

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
        AdMedia media = new AdMedia();
        media.setMediaName(user.getRealname());
        media.setUserId(user.getId());
        media.setCreateTime(now);
        media.setUpdateTime(now);
        adMediaMapper.insertSelective(media);
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
        AdMedia media = adMediaMapper.selectByUserId(user.getId());
        if(!media.getMediaName().equals(user.getRealname())){
            media.setMediaName(user.getRealname());
            media.setUpdateTime(now);
            adMediaMapper.updateByPrimaryKeySelective(media);
        }
    }
}
