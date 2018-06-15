package com.bt.om.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdActivity;
import com.bt.om.entity.vo.ActivityMobileReportVo;
import com.bt.om.entity.vo.AdActivityVo;
import com.bt.om.entity.vo.AdActivityVo2;

public interface AdActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insert(AdActivity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insertSelective(AdActivity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    AdActivity selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKeySelective(AdActivity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKey(AdActivity record);

    int getPageCount(Map<String, Object> searchMap);

    List<AdActivityVo2> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);

    AdActivityVo selectVoByPrimaryKey(Integer id);

    List<AdActivity> selectByMap(Map map);

    List<ActivityMobileReportVo> selectActivityReportForMobile(@Param("userId")Integer UserId);
    
    List<AdActivity> selectActivityReportByUserId(Map<String, Object> searchMap, RowBounds rowBounds);
    
    int selectActivityReportByUserIdCount(Map<String, Object> searchMap);
    
    List<AdActivity> selectAllByAssessorId(Map<String, Object> searchMap);
    List<AdActivity> getAtimeActivity(Map<String, Object> searchMap);
    List<AdActivity> getAllByStatusUncertain(Map<String, Object> searchMap);
    int updateAssessorId(Map<String, Object> searchMap);
	
	int updateStatusByEndTime(Date nowDate);
	int deadLineAuditActivity(Date endDate);
	int cancelActivityByAssessorId(Integer id);
	
	List<Integer> selectActivityReportByTime1(Map<String, Object> searchMap);
	List<Integer> selectActivityReportByTime2(Map<String, Object> searchMap);
	
	List<Integer> getEndActivityList(Date nowDate);
	
	List<Integer> getDeadLineAuditActivity(Date endDate);
}