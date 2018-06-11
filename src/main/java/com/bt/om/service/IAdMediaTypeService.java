package com.bt.om.service;

import java.util.List;

import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Table;

/**
 * Created by jiayong.mao on 2018/4/13.
 */
public interface IAdMediaTypeService {

	public void getPageData(SearchDataVo vo);
    public AdMediaType getById(Integer id);
    public void save(AdMediaType adSeatInfo);
    public void modify(AdMediaType adSeatInfo);
    public List<AdMediaType> getParentMedia(Integer mediaType);
    public List<AdMediaType> getParentMediaAvailable(Integer mediaType);
    public void updateStatusById(AdMediaType adMediaType);
    public void updateNeedById(AdMediaType adMediaType);
    public List<AdMediaTypeVo> selectParentAndSecond();
    public List<AdMediaType> getAll();
	List<AdMediaType> selectByParentId(Integer parentId);
	void insertBatchByExcel(List<List<Object>> listob, Table<String, String, AdMediaTypeVo> table);
}
