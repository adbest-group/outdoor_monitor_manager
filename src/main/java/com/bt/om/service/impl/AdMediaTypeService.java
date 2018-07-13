package com.bt.om.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adtime.common.lang.StringUtil;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.enums.ResultCode;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.mapper.AdMediaTypeMapper;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Table;

/**
 * Created by jiayong.mao on 2018/4/13.
 */
@Service
public class AdMediaTypeService implements IAdMediaTypeService {

	@Autowired
	private AdMediaTypeMapper adMediaTypeMapper;
	
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adMediaTypeMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adMediaTypeMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMediaType>());
        }
	}

	@Override
	public AdMediaType getById(Integer id) {
		return adMediaTypeMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void save(AdMediaType adMediaType) {
		adMediaTypeMapper.insert(adMediaType);
	}

	@Override
	@Transactional
	public void modify(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateByPrimaryKeySelective(adMediaType);
	}

	@Override
	public List<AdMediaType> getParentMedia(Integer mediaType) {
		return adMediaTypeMapper.getParentMedia(mediaType);
	}
	
	@Override
	public List<AdMediaType> getParentMediaAvailable(Integer mediaType) {
		return adMediaTypeMapper.getParentMediaAvailable(mediaType);
	}

	@Override
	@Transactional
	public void updateStatusById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		if(adMediaType.getMediaType() == 1) {
			//更新父类
			adMediaTypeMapper.updateStatusById(adMediaType);
			//更新子类
			adMediaTypeMapper.updateStatusByParentId(adMediaType);
		} else {
			//更新子类
			adMediaTypeMapper.updateStatusById(adMediaType);
		}
	}

	@Override
	@Transactional
	public void updateNeedById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateNeedById(adMediaType);
	}

	@Override
	public List<AdMediaTypeVo> selectParentAndSecond() {
		return adMediaTypeMapper.selectParentAndSecond();
	}

	@Override
	public List<AdMediaType> getAll() {
		return adMediaTypeMapper.getAll();
	}
	
	@Override
	public List<AdMediaType> selectByParentId(Integer parentId) {
		return adMediaTypeMapper.selectByParentId(parentId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBatchByExcel(List<List<Object>> listob, Table<String, String, AdMediaTypeVo> table) {
		Date now = new Date();
        List<AdMediaType> childTypes = new ArrayList<>(); //需要导入到数据库中的媒体小类信息
        
        //查询数据库中所有的媒体大类信息
        List<AdMediaType> parents = adMediaTypeMapper.getByType(1); //1：媒体大类
        
        //excel列表
        for (int i = 1; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            if(lo.size() == 2) {
            	if(lo.get(0) == null || lo.get(1) == null) {
            		break;
            	} else {
            		String parentName = String.valueOf(lo.get(0)).trim();
            		String secondName = String.valueOf(lo.get(1)).trim();
            		AdMediaTypeVo adMediaTypeVo = table.get(parentName, secondName);
            		if(adMediaTypeVo != null) {
            			//数据库里已有对应的媒体大类和媒体小类
            		} else {
            			//数据库里没有对应的媒体大类和媒体小类
            			//首先查询是否存在了媒体大类
            			boolean exist = false; //是否已存在媒体大类
            			Integer parentId = null; //存在的媒体大类的id
            			String parentNameExist = null; //存在的媒体大类的名称
            			for (AdMediaType adMediaType : parents) {
            				if(StringUtil.equals(adMediaType.getName(), parentName)) {
            					exist = true;
            					parentId = adMediaType.getId();
            					parentNameExist = parentName;
            					break;
            				}
            			}
            			
            			if(exist == false) {
            				AdMediaType parentType = new AdMediaType();
                			parentType.setName(String.valueOf(lo.get(0)).trim()); //媒体大类名称
                			parentType.setMediaType(1); //1：媒体大类
                			parentType.setUniqueKeyNeed(2); //默认 2：不是
                			parentType.setStatus(1); //1：可用
                			parentType.setCreateTime(now);
                			parentType.setUpdateTime(now);
                			
                			adMediaTypeMapper.insert(parentType); //插入媒体大类并返回主键
                			parentNameExist = parentType.getName();
                			parentId = parentType.getId();
                			parents.add(parentType);
            			}
            			
            			AdMediaType childType = new AdMediaType();
            			childType.setName(String.valueOf(lo.get(1)).trim()); //媒体小类名称
            			childType.setMediaType(2); //2：媒体小类
            			childType.setUniqueKeyNeed(2); //默认 2：不是
            			childType.setStatus(1); //1：可用
            			childType.setCreateTime(now);
            			childType.setUpdateTime(now);
            			childType.setParentId(parentId);
            			
            			childTypes.add(childType);
            			
            			table.put(parentNameExist, childType.getName(), new AdMediaTypeVo());
            		}
            	}
            }
        }
        
        //批量插入媒体小类信息
        adMediaTypeMapper.insertBatchByExcel(childTypes);
	}
}
