package com.bt.om.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.HistoryAdMonitorTask;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.AllAdMonitorTaskVo;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.entity.vo.PushInfoVo;
import com.bt.om.entity.vo.TaskAdSeat;

public interface AdMonitorTaskMapper {
	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	int deleteByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	int insert(AdMonitorTask record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	int insertSelective(AdMonitorTask record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	AdMonitorTask selectByPrimaryKey(Integer id);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	int updateByPrimaryKeySelective(AdMonitorTask record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to
	 * the database table ad_monitor_task
	 *
	 * @mbggenerated Sat Jan 20 10:48:02 CST 2018
	 */
	int updateByPrimaryKey(AdMonitorTask record);

	void insertList(@Param("tasks") List<AdMonitorTask> tasks);

	int deleteByActivityId(@Param("activityId") Integer activityId);

	int getPageCount(Map<String, Object> searchMap);

    List<AdMonitorTaskVo> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);

    List<AdMonitorTaskMobileVo> selectByUserId(@Param("userId") Integer userId);

	/**
	 * 获取监测任务详情
	 * 
	 * @param taskId
	 * @return 返回监测任务详情
	 */
	AdMonitorTaskVo getTaskDetails(Integer taskId);

	/**
	 * 获取提交详情
	 * 
	 * @param taskId
	 * @return 返回任务提交详情情况
	 */
	List<AdMonitorTaskVo> getSubmitDetails(Integer taskId);

	/**
	 * 根据父id和父类型查询子任务和有效提交列表，放在详情里
	 **/
	List<AdMonitorTaskVo> selectVoByParent(@Param("parentId") Integer parentId,@Param("parentType") Integer parentType);

	/**
	 * 根据父id和父类型查询子任务和有效的监测反馈
	 **/
	AdMonitorTaskVo selectVoByPrimaryKey(@Param("id") Integer id);

	/**
	 * 上刊任务的活动广告位关联id，激活该id所有监测任务
	 **/
	int activeTask(@Param("activitySeatId") Integer activitySeatId);

	int getByPointAroundPageCount(Map<String, Object> searchMap);

	List<AdMonitorTaskMobileVo> getByPointAroundPageData(Map<String, Object> searchMap, RowBounds rowBounds);

	int getByCurCityPageCount(Map<String, Object> searchMap);

	List<AdMonitorTaskMobileVo> getByCurCityPageData(Map<String, Object> searchMap, RowBounds rowBounds);

	int grabTask(@Param("userId")Integer userId,@Param("id")Integer id,@Param("updateTime")Date update_time);
	
	List<AdMonitorTask> findAllTask();
	int getPageCountAllTask(Map<String, Object> searchMap);

  List<AllAdMonitorTaskVo> getPageDataAllTask(Map<String, Object> searchMap, RowBounds rowBounds);
    
  List<AdMonitorTaskVo> selectAllByAssessorId(Map<String, Object> searchMap);
  List<AdMonitorTaskVo> getTenAdMonitorTaskVo(Map<String, Object> searchMap);
  int updateAssessorId(Map<String, Object> searchMap);
  int updateAssignorId(Map<String, Object> searchMap);

	List<AdMonitorTask> selectLatestMonitorTaskIds(@Param("activityId")Integer activityId);
	
	int activateMonitorTask(Date nowDate);
	int newActivateMonitorTask(Date nowDate);
	
	int recycleTask(@Param("monitorTaskIds") List<Integer> monitorTaskIds, @Param("duration") Integer duration);
	int forceAssignTask(@Param("duration") Integer duration);
	int cancelAdMonitorTaskByAssessorId(Integer id);
	int cancelAdMonitorTaskByAssignorId(Integer id);
    List<AdMonitorTask> getAllByStatusUnCheck(Map<String, Object> searchMap);
    List<AdMonitorTask> getAllByStatusUnZhipai(Map<String, Object> searchMap);
	
	List<PictureVo> selectFeedBackByActivityIdAndSeatId(Map<String, Object> searchMap);

	List<?> getTaskPageData(HashMap<String, Object> searchMap, RowBounds rowBounds);

	int getTaskPageCount(HashMap<String, Object> searchMap);

	List<TaskAdSeat> getWaitToActivateIds(Date nowDate);

	int cleanTask(AdMonitorTask task);
	
	int changeStatusAndproblemStatus(Integer id);


	AdSeatInfo selectLonLatByMonitorTaskId(Integer id);

	List<AdMonitorTask> getAllTasksByActivityId(Integer activityId);

	List<AdMonitorTask> newSelectLatestMonitorTaskIds(Map<String, Object> searchMap);

	List<TaskAdSeat> selectUpTaskIds(@Param("activityId") Integer activityId);
	
	List<String> selectUserNameByTaskIdBatch(Map<String, Object> searchMap);
	
	int recycleMediaTask(Map<String, Object> searchMap);

	boolean updateTaskStatus(Date nowDate);

	List<AdMonitorTask> selectOverTimeTasks(Date now);

	List<HistoryAdMonitorTask> selectByActivityIds(@Param("activityId") Integer activityId);

	String selectUserNameByTaskId(@Param("id") Integer id);
	

	List<AdMonitorTaskVo> selectMonitorTaskIdsByActicityId(@Param("acticityId")Integer acticityId);

	List<AdMonitorTaskVo> findAllMemo(@Param("activityId") Integer activityId,@Param("tasks") List<AdMonitorTaskVo> memos);

	int insertBatch(@Param("tasks")List<AdMonitorTaskVo> tasks);

	int deleteBatch(@Param("tasks")List<AdMonitorTaskVo> tasks);

	List<AdMonitorTask> getAllTaskTypesByActivityIdReportTime(Map<String, Object> searchMap);

}