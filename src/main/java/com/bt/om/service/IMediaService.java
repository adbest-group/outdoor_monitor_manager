package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.vo.SysUserVo;

/**
 * Created by caiting on 2018/2/27.
 */
public interface IMediaService {
    public void add(SysUserVo user);
    public void modify(SysUserVo user);
    public AdMedia getMediaByUserId(Integer id);
    public AdMedia getById(Integer id);

    public List<AdMedia> selectAllMedia();
}
