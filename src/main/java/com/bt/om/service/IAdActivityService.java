package com.bt.om.service;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.vo.web.SearchDataVo;

import java.util.List;

/**
 * Created by caiting on 2018/1/18.
 */
public interface IAdActivityService {
    public void save(AdActivity adActivity);

    public void add(AdActivityVo adActivityVo);

    public void modify(AdActivityVo adActivityVo);

    public void getPageData(SearchDataVo vo);

    public AdActivityVo getVoById(Integer id);

    public void confirm(Integer id);

    public void delete(Integer id);

    public List<AdActivity> getAll();

    public List<AdActivity> getByUerId(Integer userId);

    /**
     *  根据id获取 广告活动广告位信息
     **/
    public AdActivityAdseat getActivitySeatById(Integer id);

    /**
     * 手机端客户获取活动列表报表
     * @param user  手机端客户登录的用户对象，主要使用其中username来获取后台SysUser对象;
     * */
    public List<ActivityMobileReportVo> getMobileReport(SysUserExecute user);
}
