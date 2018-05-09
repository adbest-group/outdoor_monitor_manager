package com.bt.om.service;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.AdMonitorTaskFeedback;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdMonitorTaskMobileVo;
import java.util.List;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.vo.web.SearchDataVo;

import java.util.List;

/**
 * Created by caiting on 2018/1/20.
 */
public interface IAdMonitorTaskService {

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
	
	
}
