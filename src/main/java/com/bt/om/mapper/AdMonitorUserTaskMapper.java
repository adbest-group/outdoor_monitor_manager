package com.bt.om.mapper;

import java.util.Date;
import java.util.List;

import com.bt.om.entity.AdMonitorUserTask;
import com.bt.om.entity.vo.AbandonTaskVo;

public interface AdMonitorUserTaskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    int insert(AdMonitorUserTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    int insertSelective(AdMonitorUserTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    AdMonitorUserTask selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    int updateByPrimaryKeySelective(AdMonitorUserTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_user_task
     *
     * @mbggenerated Wed Apr 25 14:15:20 CST 2018
     */
    int updateByPrimaryKey(AdMonitorUserTask record);
    
    List<Integer> selectRecycleTaskIds(Date nowDate);
    int recycleUserTask(AdMonitorUserTask task);
    int abandonUserTask(AbandonTaskVo vo);
}