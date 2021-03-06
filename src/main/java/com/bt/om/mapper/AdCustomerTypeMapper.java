package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdCustomerType;

public interface AdCustomerTypeMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	int insert(AdCustomerType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	int insertSelective(AdCustomerType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	AdCustomerType selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	int updateByPrimaryKeySelective(AdCustomerType record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table ad_customer_type
	 * @mbg.generated  Wed Apr 25 09:23:29 CST 2018
	 */
	int updateByPrimaryKey(AdCustomerType record);
	
	int getPageCount(Map<String, Object> searchMap);
    
    List<AdCustomerType> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
    
    List<AdCustomerType> getAll();
}