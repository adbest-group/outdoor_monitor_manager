package com.bt.om.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/18.
 */
public interface IAdActivityService {
    public void save(AdActivity adActivity);

    public void add(AdActivityVo adActivityVo);

    public void modify(AdActivityVo adActivityVo);

    public void modify(AdActivityVo adActivityVo,Integer[] activitySeatDels);

    public void getPageData(SearchDataVo vo);

    public AdActivityVo getVoById(Integer id);
    
    public AdActivity getById(Integer id);

    public void confirm(Integer id, Integer assessorId);

    public void delete(Integer id);

    public List<AdActivity> getAll();

    public List<AdActivity> getByUerId(Integer userId);

    /**
     *  根据id获取 广告活动广告位信息
     **/
    public AdActivityAdseat getActivitySeatById(Integer id);

    /**
     *  根据广告位id获取 广告活动广告位信息，主要用于二维码中提供广告位id时使用
     **/
    public List<AdActivityAdseatVo> getActivitySeatBySeatId(Integer id);

    /**
     *  根据广告位ad_code获取 广告活动广告位信息，主要用于二维码中提供广告位code时使用
     **/
    public List<AdActivityAdseatVo> getActivitySeatBySeatCode(String adSeatCode);

    /**
     * 手机端客户获取活动列表报表
     * @param user  手机端客户登录的用户对象，主要使用其中username来获取后台SysUser对象;
     * */
    public List<ActivityMobileReportVo> getMobileReport(SysUserExecute user);
    
    public List<AdActivityAdseatTaskVo> selectAdSeatTaskReport(Integer activityId);
    
    public void selectReportPageData(SearchDataVo vo);
    
    public List<AdActivity> selectAllByAssessorId(Map<String, Object> searchMap);
    public List<AdActivity> getAtimeActivity(Map<String, Object> searchMap);
	public void updateStatusByEndTime(Date nowDate);

	public void offActivityByAssessorId(Integer id);

	List<AdActivity> getAllByStatusUncertain(Map<String, Object> searchMap);

	public List<AdActivityAdseatVo> selectVoByLonLatTitle(Double lon, Double lat, String title);

	public List<AdSeatCount> selectActiveActivityCount(Map<String, Object> searchMap);

	public List<Integer> selectReportPageDataTime(Map<String, Object> searchMap);

	public void selectAdActivityAdseatTask(SearchDataVo vo);

	List<AdActivityAdseatTaskVo> selectAdActivityAdseatTaskReport(Integer activityId);

	List<AdSeatInfo> selectSeatInfoByActivityId(Integer activityId);

	public List<AdActivityAdseatVo> getActivitySeatByMemo(String memo);
	List<Integer> getEndActivityList(Date nowDate);
}