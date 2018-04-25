package com.bt.om.mapper;

import com.bt.om.entity.AdActivityAdseat;
import com.bt.om.entity.vo.AdActivityAdseatTaskVo;
import com.bt.om.entity.vo.AdActivityAdseatVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdActivityAdseatMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insert(AdActivityAdseat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int insertSelective(AdActivityAdseat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    AdActivityAdseat selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKeySelective(AdActivityAdseat record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_activity_adseat
     *
     * @mbggenerated Thu Jan 18 14:25:00 CST 2018
     */
    int updateByPrimaryKey(AdActivityAdseat record);

    List<AdActivityAdseatVo> selectByActivityId(@Param("activityId") Integer activityId);

    int deleteByActivityId(@Param("activityId") Integer activityId);

    AdActivityAdseatVo selectVoById(@Param("id") Integer id);

    List<AdActivityAdseatVo> selectVoBySeatId(@Param("seatId") Integer seatId);
    List<AdActivityAdseatVo> selectVoBySeatCode(@Param("adSeatCode") String adSeatCode);

    AdActivityAdseat selectByActivityAndSeatId(@Param("activityId") Integer activityId,@Param("seatId") Integer seatId);
    
    List<AdActivityAdseatTaskVo> selectAdActivityAdseatTask(@Param("activityId") Integer activityId);
    List<AdActivityAdseatTaskVo> selectAdSeatTaskReport(@Param("activityId") Integer activityId);
}