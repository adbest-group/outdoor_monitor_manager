package com.bt.om.mapper;

import java.util.List;

import com.bt.om.entity.AdCrowd;
import com.bt.om.entity.vo.AdCrowdVo;

public interface AdCrowdMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	int insert(AdCrowd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	int insertSelective(AdCrowd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	AdCrowd selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	int updateByPrimaryKeySelective(AdCrowd record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_crowd
	 *
	 * @mbggenerated Fri Feb 02 10:04:38 CST 2018
	 */
	int updateByPrimaryKey(AdCrowd record);

	int insertAdCrowdVoMale(AdCrowdVo vo);
	
	int insertAdCrowdVoFemale(AdCrowdVo vo);
}