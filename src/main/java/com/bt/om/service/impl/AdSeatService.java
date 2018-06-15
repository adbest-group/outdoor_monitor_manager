package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdCrowdMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.service.IAdSeatService;
import com.bt.om.util.GeoUtil;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/3/5.
 */
@Service
public class AdSeatService implements IAdSeatService {
    @Autowired
    private AdSeatInfoMapper adSeatInfoMapper;
    @Autowired
    private AdMediaMapper adMediaMapper;
    @Autowired
    private AdCrowdMapper adCrowdMapper;
    @Autowired
    private AdActivityAdseatMapper adActivityAdseatMapper;

    @Override
    public int getPageCount(SearchDataVo vo) {
        return adSeatInfoMapper.getPageCount(vo.getSearchMap());
    }

    @Override
    public void getPageData(SearchDataVo vo) {
        int count = adSeatInfoMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adSeatInfoMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdSeatInfoVo>());
        }
    }

    @Override
    public AdSeatInfo getById(Integer id) {
        return adSeatInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AdSeatInfo> getByStreetAndMediaUserId(Long street, Integer userId) {
        return adSeatInfoMapper.getAdSeatInfoByStreetAndMediaUserId(userId,street);
    }

    @Override
    public int getCountByAdCode(String adSeatCode) {
        return adSeatInfoMapper.getCountByAdCode(adSeatCode);
    }

    @Override
    public void save(AdSeatInfo adSeatInfo) {
        adSeatInfoMapper.insertSelective(adSeatInfo);
    }
    
    @Override
    public void save(AdSeatInfo adSeatInfo, Integer userId) {
        AdMedia media = adMediaMapper.selectByUserId(userId);
        if(media!=null){
            adSeatInfo.setMediaId(media.getId());
        }
        adSeatInfoMapper.insertSelective(adSeatInfo);
    }

    @Override
    public void modify(AdSeatInfo adSeatInfo) {
        Date now = new Date();
        adSeatInfo.setUpdateTime(now);
        adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(AdSeatInfo adSeatInfo, List<AdCrowd> crowds) {
        Date now = new Date();
        adSeatInfo.setUpdateTime(now);
        adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
        for(AdCrowd crowd:crowds){
            crowd.setUpdateTime(now);
            if(crowd.getId()!=null){
                adCrowdMapper.updateByPrimaryKeySelective(crowd);
            }else{
                crowd.setCreateTime(now);
                adCrowdMapper.insertSelective(crowd);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyInfo(AdSeatInfo adSeatInfo) {
        Date now = new Date();
        adSeatInfo.setUpdateTime(now);
        adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
    }
    
    @Override
    public void delete(Integer id) {
        adSeatInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AdCrowd> getCrowdsBySeatId(Integer adSeatId) {
        return adCrowdMapper.getAgePartListByAdSeatId(adSeatId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchByExcel(List<AdSeatInfo> adSeatInfos) {
    	if(adSeatInfos != null && adSeatInfos.size() > 0) {
    		int count = adSeatInfoMapper.insertBatchByExcel(adSeatInfos);
    		return count;
    	}
    	return 0;
    }

    @Override
    public List<AdSeatInfo> getAdseatAround(Double lat, Double lon, Double metre) {
        return adSeatInfoMapper.getAdSeatByPointAround(lon,lat,metre, GeoUtil.getDegreeFromDistance(metre));
    }

	@Override
	public List<AdSeatInfo> getAdSeatByMediaId(Integer mediaId) {
		return adSeatInfoMapper.getAdSeatByMediaId(mediaId);
	}

	@Override
	public List<CountGroupByCityVo> getCountGroupByCity(HeatMapVo heatMapVo, Integer userId) {
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("userId", userId);
		List<Integer> seatIds = new ArrayList<>();
		
		//[1] 查询某活动的所有广告位id集合
		if(heatMapVo.getActivityId() != null) {
			searchMap.put("activityId", heatMapVo.getActivityId());
		}
		seatIds = adActivityAdseatMapper.selectSeatIdByActivityId(searchMap);
		if(seatIds.size() > 0) {
			heatMapVo.setInfoIds(seatIds);
		} else {
			heatMapVo.setInfoIds(null);
			return new ArrayList<CountGroupByCityVo>();
		}
		
		//[2] 查询热力图报表
		return adSeatInfoMapper.getCountGroupByCity(heatMapVo);
	}
	
	@Override
	public List<AdSeatInfo> getAllLonLat(HeatMapVo heatMapVo, Integer userId) {
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("userId", userId);
		List<Integer> seatIds = new ArrayList<>();
		
		//[1] 查询某活动的所有广告位id集合
		if(heatMapVo.getActivityId() != null) {
			searchMap.put("activityId", heatMapVo.getActivityId());
		}
		seatIds = adActivityAdseatMapper.selectSeatIdByActivityId(searchMap);
		if(seatIds.size() > 0) {
			heatMapVo.setInfoIds(seatIds);
		} else {
			heatMapVo.setInfoIds(null);
			return new ArrayList<AdSeatInfo>();
		}
		
		//[2] 查询热力图报表
		return adSeatInfoMapper.getAllLonLat(heatMapVo);
	}

	@Override
	public int updateFlag(Integer codeFlag,Integer id) {
		return adSeatInfoMapper.updateFlag(codeFlag,id);
	}
	
	@Override
	public int selectByLocation(Map<String, Object> searchMap) {
		return adSeatInfoMapper.selectByLocation(searchMap);
	}

}
