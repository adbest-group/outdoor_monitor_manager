package com.bt.om.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.HistoryAdActivity;
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
public interface IHistoryAdActivityService {
    public void getPageData(SearchDataVo vo);

    public AdActivityVo getVoById(SearchDataVo vo);
    
    public AdActivity getById(Map<String, Object> searchMap);

    /**
     *  根据id获取 广告活动广告位信息
     **/
    public AdActivityAdseat getActivitySeatById(SearchDataVo vo);
    
	List<AdActivityAdseatTaskVo> newSelectAdActivityAdseatTaskReport(Map<String, Object> searchMap);

	public void batchDataTransfer(HistoryAdActivity adActivity);

}