package com.bt.om.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
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
	public void assign(String[] taskIds, Integer userId);

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
    public void pass(AdMonitorTask task);
    /**
     * 审核不通过
     **/
    public void reject(AdMonitorTask task,String reason);

    public List<AdMonitorTaskMobileVo> getByUserIdForMobile(Integer userId);

    public void feedback(Integer taskId, AdMonitorTaskFeedback feedback,String adSeatCode);

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
	List<PictureVo> selectFeedBackByActivityIdAndSeatId(Map<String, Object> searchMap);

}
