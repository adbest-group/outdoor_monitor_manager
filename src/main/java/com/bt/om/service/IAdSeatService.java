package com.bt.om.service;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.vo.web.SearchDataVo;

import java.util.List;

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
	public int insertBatchByExcel(List<AdSeatInfo> adSeatInfos, Integer userId);
}
