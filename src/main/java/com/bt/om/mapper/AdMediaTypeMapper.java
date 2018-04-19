package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdMediaType;
import com.bt.om.entity.vo.AdMediaTypeVo;

public interface AdMediaTypeMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	int insert(AdMediaType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	int insertSelective(AdMediaType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	AdMediaType selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	int updateByPrimaryKeySelective(AdMediaType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_media_type
	 * @mbg.generated  Fri Apr 13 10:37:52 CST 2018
	 */
	int updateByPrimaryKey(AdMediaType record);

	int getPageCount(Map<String, Object> searchMap);
    
    List<AdMediaType> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
    
    List<AdMediaType> getParentMedia(Integer mediaType);
    
    List<AdMediaType> getParentMediaAvailable(Integer mediaType);
    
    int updateStatusById(AdMediaType record);
    
    int updateStatusByParentId(AdMediaType record);
    
    int updateNeedById(AdMediaType record);
    
    List<AdMediaTypeVo> selectParentAndSecond();
    
    List<AdMediaType> getAll();
}