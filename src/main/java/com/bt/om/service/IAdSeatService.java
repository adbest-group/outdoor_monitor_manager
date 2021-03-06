package com.bt.om.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/3/5.
 */
public interface IAdSeatService {
    public int getPageCount(SearchDataVo vo);
    public void getPageData(SearchDataVo vo);
    public AdSeatInfo getById(Integer id);
    public List<AdSeatInfo> getByStreetAndMediaUserId( Long street, Integer userId);
    public int getCountByAdCode(String adSeatCode);
    public void save(AdSeatInfo adSeatInfo);
    public void save(AdSeatInfo adSeatInfo,Integer userId);
    public void modify(AdSeatInfo adSeatInfo);
    public void modify(AdSeatInfo adSeatInfo, List<AdCrowd> crowds);
    public void delete(Integer id);
    public List<AdCrowd> getCrowdsBySeatId(Integer adSeatId);
	
	/**
     * 查询给定坐标点，半径metre范围内（单位：米）所有的目前有活动的广告位
     **/
	public List<AdSeatInfo> getAdseatAround(Double lat,Double lon,Double metre);
	public List<AdSeatInfo> getAdSeatByMediaId(Integer mediaId);
	
	void modifyInfo(AdSeatInfo adSeatInfo);
	int updateFlag(Integer codeFlag,Integer id);
	int selectByLocation(Map<String, Object> searchMap);
	public List<AdSeatInfo> searchLocation(Map<String, Object> searchMap);
	List<AdSeatInfo> selectAllSeats();
	int insertBatchByExcel(List<AdSeatInfo> adSeatInfos, List<Integer> tmpSeatIds, Date nowDate);
	List<Integer> selectSeatIds();
	List<AdSeatInfoVo> selectSeatByIds(Map<String, Object> searchMap);
	List<CountGroupByCityVo> getCountGroupByCity(HeatMapVo heatMapVo, SysUser user);
	List<AdSeatInfo> getAllLonLat(HeatMapVo heatMapVo, SysUser user);
	List<String> selectAllSeatMemoByActivityId(Integer activityId);
}
