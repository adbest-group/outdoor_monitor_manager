package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adtime.common.lang.StringUtil;
import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.vo.AdMediaTypeVo;
import com.bt.om.enums.AllowMultiEnum;
import com.bt.om.enums.MediaType;
import com.bt.om.mapper.AdMediaTypeMapper;
import com.bt.om.service.IAdMediaTypeService;
import com.bt.om.vo.web.SearchDataVo;
import com.google.common.collect.Table;

/**
 * 媒体类型相关事务层
 */
@Service
public class AdMediaTypeService implements IAdMediaTypeService {

	@Autowired
	private AdMediaTypeMapper adMediaTypeMapper;
	
	/**
	 * 分页查询媒体类型
	 */
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

	/**
	 * 通过id查询媒体类型
	 */
	@Override
	public AdMediaType getById(Integer id) {
		return adMediaTypeMapper.selectByPrimaryKey(id);
	}

	/**
	 * 插入一条媒体类型
	 */
	@Override
	@Transactional
	public void save(AdMediaType adMediaType) {
		adMediaTypeMapper.insert(adMediaType);
	}

	/**
	 * 更新媒体类型
	 */
	@Override
	@Transactional
	public void modify(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateByPrimaryKeySelective(adMediaType);
	}

	/**
	 * 通过媒体类型查询所有的媒体大类/媒体小类
	 */
	@Override
	public List<AdMediaType> getParentMedia(Integer mediaType) {
		return adMediaTypeMapper.getParentMedia(mediaType);
	}
	
	/**
	 * 通过媒体类型查询所有可用的媒体大类/媒体小类
	 */
	@Override
	public List<AdMediaType> getParentMediaAvailable(Integer mediaType) {
		return adMediaTypeMapper.getParentMediaAvailable(mediaType);
	}

	/**
	 * 更新媒体类型是否可用
	 */
	@Override
	@Transactional
	public void updateStatusById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		if(adMediaType.getMediaType() == MediaType.PARENT_TYPE.getId()) {
			//更新父类
			adMediaTypeMapper.updateStatusById(adMediaType);
			//更新子类
			adMediaTypeMapper.updateStatusByParentId(adMediaType);
		} else {
			//更新子类
			adMediaTypeMapper.updateStatusById(adMediaType);
		}
	}

	/**
	 * 更新是否需要唯一标识, 功能已废弃
	 */
	@Override
	@Transactional
	public void updateNeedById(AdMediaType adMediaType) {
		Date now = new Date();
		adMediaType.setUpdateTime(now);
		adMediaTypeMapper.updateNeedById(adMediaType);
	}

	/**
	 * 获取媒体大类与对应的媒体小类
	 */
	@Override
	public List<AdMediaTypeVo> selectParentAndSecond() {
		return adMediaTypeMapper.selectParentAndSecond();
	}

	/**
	 * 获取全部的媒体类型
	 */
	@Override
	public List<AdMediaType> getAll() {
		return adMediaTypeMapper.getAll();
	}
	
	/**
	 * 通过媒体大类id查询对应的媒体小类信息
	 */
	@Override
	public List<AdMediaType> selectByParentId(Integer parentId) {
		return adMediaTypeMapper.selectByParentId(parentId);
	}
	
	/**
	 * 批量导入媒体类型
	 */
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
            if(lo.size() == 3) {
            	if(lo.get(0) == null || lo.get(1) == null || lo.get(2) == null) {
            		break;
            	} else {
            		String parentName = String.valueOf(lo.get(0)).trim(); //媒体大类名称
            		String secondName = String.valueOf(lo.get(1)).trim(); //媒体小类名称
            		String activityNum = String.valueOf(lo.get(2)).trim(); //支持活动数量
            		
            		// 本身填写的不是数字, 捕获转换异常, 设置为默认的1
            		int multiNum = 1;
            		try {
            			Double doubleNum = Double.parseDouble(activityNum);
            			multiNum = (int) Math.ceil(doubleNum);
            		} catch (Exception e) {
            			multiNum = 1;
            		}
            		
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
                			parentType.setMediaType(MediaType.PARENT_TYPE.getId()); //1：媒体大类
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
            			// 支持活动数量得是正整数, 否则设置为1
                		if(Pattern.matches("^[1-9]\\d*$", multiNum + "") == true) {
                			childType.setMultiNum(multiNum);
                			if(multiNum > 1) {
                				childType.setAllowMulti(AllowMultiEnum.ALLOW.getId());
                			} else {
                				childType.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
                			}
                		} else {
                			childType.setMultiNum(1);
                			childType.setAllowMulti(AllowMultiEnum.NOT_ALLOW.getId());
                		}
            			childType.setName(String.valueOf(lo.get(1)).trim()); //媒体小类名称
            			childType.setMediaType(MediaType.SECOND_TYPE.getId()); //2：媒体小类
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
        if(childTypes != null && childTypes.size() > 0) {
        	adMediaTypeMapper.insertBatchByExcel(childTypes);
        }
	}
}
