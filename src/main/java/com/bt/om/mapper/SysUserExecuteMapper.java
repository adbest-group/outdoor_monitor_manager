package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.SysUserExecuteVo;

public interface SysUserExecuteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    int insert(SysUserExecute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    int insertSelective(SysUserExecute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    SysUserExecute selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    int updateByPrimaryKeySelective(SysUserExecute record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_user_execute
     *
     * @mbggenerated Tue Jan 23 17:12:28 CST 2018
     */
    int updateByPrimaryKey(SysUserExecute record);

    SysUserExecute selectByUsername(String username);
    List<SysUserExecute> selectByUsernames(@Param("usernames") List<String> usernames);
    
    List<SysUserExecute> selectByConditionMap(Map map);

    int getPageCount(Map<String, Object> searchMap);

    List<SysUserExecuteVo> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
    List<SysUserExecute> isExistsName(@Param("username") String username,@Param("id") Integer id);
    
    SysUserExecuteVo selectByIdAndMedia(@Param("id") Integer id);

	void insertMacAddress(@Param("mac")String mac);
}