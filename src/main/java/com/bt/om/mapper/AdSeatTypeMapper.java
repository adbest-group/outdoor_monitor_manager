package com.bt.om.mapper;

import java.util.List;

import com.bt.om.entity.AdSeatType;

public interface AdSeatTypeMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	int insert(AdSeatType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	int insertSelective(AdSeatType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	AdSeatType selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	int updateByPrimaryKeySelective(AdSeatType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_seat_type
	 *
	 * @mbggenerated Thu Feb 01 14:08:19 CST 2018
	 */
	int updateByPrimaryKey(AdSeatType record);

	List<AdSeatType> getSeatTypeAll();
}