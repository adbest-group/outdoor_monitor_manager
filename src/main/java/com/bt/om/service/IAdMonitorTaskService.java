package com.bt.om.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.vo.AbandonTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.entity.vo.PictureVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/20.
 */
public interface IAdMonitorTaskService {

	public AdMonitorTask selectByPrimaryKey(Integer id);
	public void getPageData(SearchDataVo vo);
	/**
	 * 只适用于首次分配，内部采用和抢任务一样的机制
	 **/
	public void assign(String[] taskIds, Integer userId, Integer assignorId);

	/**
	 * 获取监测任务详情信息
	 * 
	 * @param taskId
	 * @return 监测任务详情信息
	 */
	public AdMonitorTaskVo getTaskDetails(String taskId);

    public void update(AdMonitorTask task);

    /**
     * 审核通过
     **/
    public void pass(String[] taskIds, Integer assessorId, Integer status);
    /**
     * 审核不通过
     **/
    public void reject(String[] taskIds, String reason, Integer assessorId, Integer status);

    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId);

    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback,String adSeatCode, SysUserExecute user);

    /**
	 * 根据当前任务编号生成子任务
	 **/
    public void createSubTask(Integer taskId);
	/**
	 * 获取提交详情
	 * 
	 * @param taskId
	 * @return 返回任务提交详情情况
	 */
	public List<AdMonitorTaskVo> getSubmitDetails(String taskId);

	/**
	 * 根据id获取任务详情（包含有效的监测提交）
	 **/
	public AdMonitorTaskVo getTaskVoById(Integer id);

	public void getByPointAroundPageData(SearchDataVo vo);
	public void getByCurCityPageData(SearchDataVo vo);

	public boolean grabTask(Integer userId,Integer id);

	/**
	 * 查看所有任务
	 **/
	public List<AdMonitorTask>selectAllTask();
	public void getPageDataAllTask(SearchDataVo vo);
	List<AdMonitorTaskVo> selectAllByAssessorId(Map<String, Object> searchMap);
	List<AdMonitorTaskVo> getTenAdMonitorTaskVo(Map<String, Object> searchMap);
	List<AdMonitorTaskVo> getTenAdMonitorTaskAssignVo(Map<String, Object> searchMap);
	
	public List<AdMonitorTask> selectLatestMonitorTaskIds(Integer activityId);
	public List<AdMonitorTaskFeedback> selectByActivity(List<Integer> monitorTaskIds);
	
	public void activateMonitorTask(Date nowDate);
	public void recycleMonitorTask();
	public void forceAssignTask();
	public void abandonUserTask(AbandonTaskVo vo);
	public void offAdMonitorTaskByAssessorId(Integer id);
	public void offAdMonitorTaskByAssignorId(Integer id);
	 public List<AdMonitorTask> getAllByStatusUnCheck(Map<String, Object> searchMap);
	 public List<AdMonitorTask> getAllByStatusUnZhipai(Map<String, Object> searchMap);
	List<PictureVo> selectFeedBackByActivityIdAndSeatId(Map<String, Object> searchMap);
    /**
     * 获取即将结束的任务(2小时之前),进行消息推送
     */
	String getTaskWillEnd(Integer duration);
	public void getTaskPageData(SearchDataVo datavo);
	void newActivateMonitorTask(Date nowDate);

	void updatePicUrl(Integer id, String picUrl, Integer index);

	public AdMonitorTask getActivityId(int id);

	void insertMonitorTask(Integer activityId, List<String> seatIds, String reportTime);

	Integer selectCountByMonitorTaskId(int monitorTaskId);
	AdSeatInfo selectLonLatByMonitorTaskId(int monitorTaskId);
	int insertMonitorTaskFeedback(AdMonitorTaskFeedback feedback, Integer userId, Integer assessorId);

	//查询所有任务列表
	public List<AdMonitorTask> getAllTasksByActivityId(Integer activityId);
	public List<AdMonitorTask> newSelectLatestMonitorTaskIds(Map<String, Object> searchMap);
	List<String> selectUserNameByTaskIdBatch(Map<String, Object> searchMap);
	void recycleMediaMonitorTask();
}
