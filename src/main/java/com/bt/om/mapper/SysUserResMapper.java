package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bt.om.entity.SysUserRes;

public interface SysUserResMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    int insert(SysUserRes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    int insertSelective(SysUserRes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    SysUserRes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    int updateByPrimaryKeySelective(SysUserRes record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_res
     *
     * @mbg.generated Tue Apr 24 17:12:35 CST 2018
     */
    int updateByPrimaryKey(SysUserRes record);
    int insertUserRess(@Param("sysUserRess") List<SysUserRes> sysUserRess);
    int deleteByResIdAndType(SysUserRes sysUserRes);
    
    int selectGroupIdByUserId(Map<String, Object> searchMap);
    List<Integer> selectCustomerIdsByResId(Map<String, Object> searchMap);
    List<Integer> selectUserIdsByResIds(Map<String, Object> searchMap);
}