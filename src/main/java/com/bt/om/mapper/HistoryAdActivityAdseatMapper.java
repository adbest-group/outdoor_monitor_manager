package com.bt.om.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.HistoryAdActivityAdseat;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import com.bt.om.entity.vo.AdSeatCount;
import com.bt.om.vo.web.SearchDataVo;

public interface HistoryAdActivityAdseatMapper {

    List<AdActivityAdseatVo> selectByActivityId(HashMap<String, Object> searchMap);

    AdActivityAdseatVo selectVoById(HashMap<String, Object> adSeatCode);

    List<AdActivityAdseatTaskVo> selectAdActivityAdseatTask(@Param("activityId") Integer activityId);
    
	int insertBatch(@Param("tableName") String tableName,@Param("adActivityAdseats") List<HistoryAdActivityAdseat> adActivityAdseats);

	List<Integer> selectByActivityIdAndSeatIds(Map<String, Object> searchMap);
	
	List<AdActivityAdseatTaskVo> newSelectAdActivityAdseatTaskReport(Map<String, Object> searchMap);
}