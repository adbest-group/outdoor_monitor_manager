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
import com.bt.om.entity.AdSeatInfoTmp;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;
import com.bt.om.mapper.AdActivityAdseatMapper;
import com.bt.om.mapper.AdCrowdMapper;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdSeatInfoMapper;
import com.bt.om.mapper.AdSeatInfoTmpMapper;
import com.bt.om.service.IAdSeatService;
import com.bt.om.util.GeoUtil;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 广告位相关事务层
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
    @Autowired
    private AdSeatInfoTmpMapper adSeatInfoTmpMapper;

    /**
     * 分页查询广告位信息
     */
    @Override
    public int getPageCount(SearchDataVo vo) {
        return adSeatInfoMapper.getPageCount(vo.getSearchMap());
    }

    /**
     * 分页查询广告位信息
     */
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

    /**
     * 通过id查询广告位信息
     */
    @Override
    public AdSeatInfo getById(Integer id) {
        return adSeatInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过媒体主后台id查询出对应街道的广告位信息
     */
    @Override
    public List<AdSeatInfo> getByStreetAndMediaUserId(Long street, Integer userId) {
        return adSeatInfoMapper.getAdSeatInfoByStreetAndMediaUserId(userId,street);
    }

    /**
     * 通过二维码查询广告位数量
     */
    @Override
    public int getCountByAdCode(String adSeatCode) {
        return adSeatInfoMapper.getCountByAdCode(adSeatCode);
    }

    /**
     * 保存一条广告位信息
     */
    @Override
    public void save(AdSeatInfo adSeatInfo) {
        adSeatInfoMapper.insertSelective(adSeatInfo);
    }
    
    /**
     * 保存一条广告位信息
     */
    @Override
    public void save(AdSeatInfo adSeatInfo, Integer userId) {
        AdMedia media = adMediaMapper.selectByUserId(userId);
        if(media!=null){
            adSeatInfo.setMediaId(media.getId());
        }
        adSeatInfoMapper.insertSelective(adSeatInfo);
    }

    /**
     * 修改广告位
     */
    @Override
    public void modify(AdSeatInfo adSeatInfo) {
        Date now = new Date();
        adSeatInfo.setUpdateTime(now);
        adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
    }

    /**
     * 修改广告位
     */
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

    /**
     * 修改广告位
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyInfo(AdSeatInfo adSeatInfo) {
        Date now = new Date();
        adSeatInfo.setUpdateTime(now);
        
        adSeatInfoMapper.updateByPrimaryKeySelective(adSeatInfo);
    }
    
    /**
     * 删除广告位
     */
    @Override
    public void delete(Integer id) {
        adSeatInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<AdCrowd> getCrowdsBySeatId(Integer adSeatId) {
        return adCrowdMapper.getAgePartListByAdSeatId(adSeatId);
    }
    
    /**
     * 批量插入广告位
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchByExcel(List<AdSeatInfo> adSeatInfos, List<Integer> tmpSeatIds, Date nowDate) {
    	int count = 0;
    	//批量插入广告位, 可能不需要插入新的广告位
    	if(adSeatInfos != null && adSeatInfos.size() > 0) {
    		count = adSeatInfoMapper.insertBatchByExcel(adSeatInfos);
    		adSeatInfos.clear();
    	}
    	//【群邑方】导入还要插入临时表(先清空后插入)
    	if(tmpSeatIds != null && nowDate != null) {
    		// [1] 先获取刚刚新插入的广告位id集合
    		List<Integer> seatIds = adSeatInfoMapper.selectNewSeatIds(nowDate);
    		tmpSeatIds.addAll(seatIds);
    		// [2] 清空临时表
    		adSeatInfoTmpMapper.deleteAll();
    		// [3] 批量插入临时表
    		List<AdSeatInfoTmp> adSeatInfoTmps = new ArrayList<>();
    		for (Integer seatInfoId : tmpSeatIds) {
    			AdSeatInfoTmp adSeatInfoTmp = new AdSeatInfoTmp();
    			adSeatInfoTmp.setSeatInfoId(seatInfoId);
    			adSeatInfoTmp.setCreateTime(nowDate);
    			adSeatInfoTmp.setUpdateTime(nowDate);
    			adSeatInfoTmps.add(adSeatInfoTmp);
			}
    		if(adSeatInfoTmps.size() > 0) {
    			adSeatInfoTmpMapper.insertBatch(adSeatInfoTmps);
    			adSeatInfoTmps.clear();
    			tmpSeatIds.clear();
    		}
    	}
    	return count;
    }

    /**
     * 通过经纬度查询附近的广告位
     */
    @Override
    public List<AdSeatInfo> getAdseatAround(Double lat, Double lon, Double metre) {
        return adSeatInfoMapper.getAdSeatByPointAround(lon,lat,metre, GeoUtil.getDegreeFromDistance(metre));
    }

    /**
     * 查询出这家媒体下的所有广告位
     */
	@Override
	public List<AdSeatInfo> getAdSeatByMediaId(Integer mediaId) {
		return adSeatInfoMapper.getAdSeatByMediaId(mediaId);
	}

	/**
	 * 查询所有的广告位
	 */
	@Override
	public List<AdSeatInfo> selectAllSeats() {
		return adSeatInfoMapper.selectAllSeats();
	}
	
	/**
	 * 广告主查看某市广告位热力图
	 */
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
	
	/**
	 * 广告主查看广告位热力图
	 */
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

	/**
	 * 更新是否贴上二维码
	 */
	@Override
	public int updateFlag(Integer codeFlag,Integer id) {
		return adSeatInfoMapper.updateFlag(codeFlag,id);
	}
	
	/**
	 * 筛选条件查询广告位数量
	 */
	@Override
	public int selectByLocation(Map<String, Object> searchMap) {
		return adSeatInfoMapper.selectByLocation(searchMap);
	}

	/**
	 * 查询广告位详细位置/广告位编号是否重复  排除自身
	 */
	@Override
	public List<AdSeatInfo> searchLocation(Map<String, Object> searchMap) {
		return adSeatInfoMapper.searchLocation(searchMap);
	}
	
	/**
	 * 插入最近一次导入的广告位id集合
	 */
	@Override
	public List<Integer> selectSeatIds(){
		return adSeatInfoTmpMapper.selectSeatIds();
	}
	
	/**
	 * 查询指定id集合的广告位信息
	 */
	@Override
	public List<AdSeatInfoVo> selectSeatByIds(Map<String, Object> searchMap){
		return adSeatInfoMapper.selectSeatByIds(searchMap);
	}

}
