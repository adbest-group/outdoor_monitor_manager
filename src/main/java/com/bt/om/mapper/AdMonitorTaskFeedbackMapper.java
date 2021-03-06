package com.bt.om.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.HistoryAdMonitorTask;
import com.bt.om.entity.HistoryAdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdMonitorTaskVo;

public interface AdMonitorTaskFeedbackMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    int insert(AdMonitorTaskFeedback record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    int insertSelective(AdMonitorTaskFeedback record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    HistoryAdMonitorTaskFeedback selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    int updateByPrimaryKeySelective(AdMonitorTaskFeedback record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_monitor_task_feedback
     *
     * @mbggenerated Wed Jan 24 17:01:09 CST 2018
     */
    int updateByPrimaryKey(AdMonitorTaskFeedback record);

    List<AdMonitorTaskFeedback> selectByTaskId(@Param("taskId") Integer taskId,@Param("status") Integer status);
    
    List<AdMonitorTaskFeedback> selectByActivity(@Param("monitorTaskIds") List<Integer> monitorTaskIds);
    
    int updatePicUrl1(Map<String, Object> searchMap);
    
    int updatePicUrl2(Map<String, Object> searchMap);
    
    int updatePicUrl3(Map<String, Object> searchMap);
    
    int updatePicUrl4(Map<String, Object> searchMap);
    
    int selectCountByMonitorTaskId(@Param("monitorTaskId") Integer monitorTaskId);

	List<HistoryAdMonitorTaskFeedback> selectAllByMonitorTaskIds(@Param("adMonitorTasks") List<HistoryAdMonitorTask> adMonitorTasks);

	int deleteByIds(@Param("adMonitorTaskFeedbacks") List<HistoryAdMonitorTaskFeedback> adMonitorTaskFeedbacks);

	int insertBatch(@Param("tasks")List<AdMonitorTaskVo> tasks);

}