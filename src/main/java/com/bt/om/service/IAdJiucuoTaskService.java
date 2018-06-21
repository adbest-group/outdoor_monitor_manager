package com.bt.om.service;

import java.util.List;
import java.util.Map;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.AdJiucuoTaskFeedback;
import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import com.bt.om.entity.vo.AdMonitorTaskVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/21.
 */
public interface IAdJiucuoTaskService {
    public void getPageData(SearchDataVo vo);
    public AdJiucuoTask getById(Integer id);
    public AdJiucuoTaskVo getVoById(Integer id);
    public AdJiucuoTaskFeedback getFeadBackById(Integer id);
    public AdJiucuoTaskFeedback getFeadBackByTaskId(Integer id);
    public void update(AdJiucuoTask task);
    /**
     *  审核通过
     **/
    public void pass(String[] jiucuoIds, Integer assessorId, Integer status);
    /**
     *  审核不通过
     **/
    public void reject(String[] taskIds, String reason, Integer assessorId, Integer status);
    public void feedback(AdJiucuoTask task,AdJiucuoTaskFeedback feedback);
    public List<AdJiucuoTaskMobileVo> getByUserIdForMobile(Integer userId);
    /**
     * 根据当前纠错编号生成子任务
     **/
    public void createSubTask(Integer taskId);
    /**
     * 更改纠错图片
     **/
	void updatePicUrl(Integer id, String picUrl, Integer index);

    /**
     *  查询纠错子任务执行情况，用于详情
     **/
    public List<AdMonitorTaskVo> getSubTask(Integer id);
    
    public List<AdJiucuoTaskVo> selectAllByAssessorId(Map<String, Object> searchMap);
    public List<AdJiucuoTaskVo> getTenAdMonitorTaskVo(Map<String, Object> searchMap);
	int selectCountByActivityAndSeat(Map<String, Object> searchMap);
	public void offJiucuoTaskByAssessorId(Integer id);
	 public List<AdJiucuoTaskVo> getAllByStatusUnCheck(Map<String, Object> searchMap);
	List<AdJiucuoTask> selectInfoByQrCode(Map<String, Object> searchMap);
	List<AdJiucuoTask> selectInfoByLonLatTitle(Map<String, Object> searchMap);
	List<AdJiucuoTask> selectInfoByMemo(Map<String, Object> searchMap);
	public void getJiucuoPageData(SearchDataVo datavo);
	public AdJiucuoTask getActivityId(int id);
}
