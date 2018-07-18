package com.bt.om.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.LoginLog;

public interface LoginLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    int insert(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    int insertSelective(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    LoginLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    int updateByPrimaryKeySelective(LoginLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table login_log
     *
     * @mbg.generated Mon Jul 16 11:07:08 CST 2018
     */
    int updateByPrimaryKey(LoginLog record);

	int getPageCountSysUser(HashMap<String, Object> searchMap);
	int getPageCountSysUserExecute(HashMap<String, Object> searchMap);
	List<LoginLog> getPageDataSysUser(HashMap<String, Object> searchMap, RowBounds rowBounds);
	List<LoginLog> getPageDataSysUserExecute(HashMap<String, Object> searchMap, RowBounds rowBounds);
	
	
	 
}