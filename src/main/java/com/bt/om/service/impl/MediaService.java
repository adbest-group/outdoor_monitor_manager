package com.bt.om.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.SysUserDetailMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.mapper.SysUserRoleMapper;
import com.bt.om.service.IMediaService;

/**
 * 媒体主 相关事务层
 */
@Service
public class MediaService implements IMediaService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;
    @Autowired
    private AdMediaMapper adMediaMapper;
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

    /**
     * 新增媒体主
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
        detail.setPrefix(user.getPrefix());
        detail.setCreateTime(now);
        detail.setUpdateTime(now);
        sysUserDetailMapper.insertSelective(detail);
        SysUserRole userRole = new SysUserRole();
        userRole.setPlatform(1);
        userRole.setUserId(user.getId());
        userRole.setRoleId(102); //102: media
        userRole.setCreateTime(now);
        userRole.setUpdateTime(now);
        sysUserRoleMapper.insertSelective(userRole);
        AdMedia media = new AdMedia();
        media.setMediaName(user.getRealname());
        media.setUserId(user.getId());
        media.setCreateTime(now);
        media.setUpdateTime(now);
        adMediaMapper.insertSelective(media);
    }

    /**
     * 修改媒体主
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(SysUserVo user) {
        Date now = new Date();
        user.setUpdateTime(now);
        sysUserMapper.updateByPrimaryKeySelective(user);
        SysUserDetail detail = sysUserDetailMapper.selectByUserId(user.getId());
        if(!detail.getTelephone().equals(user.getTelephone())||detail.getPrefix()==null||!detail.getPrefix().equals(user.getPrefix())) {
            detail.setTelephone(user.getTelephone());
            detail.setPrefix(user.getPrefix());
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

    /**
     * 通过后台用户id查询媒体信息
     */
    @Override
    public AdMedia getMediaByUserId(Integer id) {
        return adMediaMapper.selectByUserId(id);
    }

    /**
     * 通过媒体id查询媒体信息
     */
    @Override
    public AdMedia getById(Integer id) {
        return adMediaMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询全部的媒体主
     */
	@Override
	public List<AdMedia> selectAllMedia() {
		return adMediaMapper.selectAllMedia();
	}

}
