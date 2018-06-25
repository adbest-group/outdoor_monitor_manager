package com.bt.om.mapper;

import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.UserRoleVo;

public interface SysUserRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insert(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insertSelective(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    SysUserRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKeySelective(SysUserRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_role
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKey(SysUserRole record);
    
    int updateUserRole(UserRoleVo userRoleVo);

	SysUserRole findRoleByUserId(Integer userId);
}