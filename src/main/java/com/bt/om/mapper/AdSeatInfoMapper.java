package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.AdSeatInfo;
import com.bt.om.entity.vo.AdSeatInfoVo;
import com.bt.om.entity.vo.CountGroupByCityVo;
import com.bt.om.entity.vo.HeatMapVo;

public interface AdSeatInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    int insert(AdSeatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    int insertSelective(AdSeatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    AdSeatInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    int updateByPrimaryKeySelective(AdSeatInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_seat_info
     *
     * @mbggenerated Thu Feb 01 13:48:16 CST 2018
     */
    int updateByPrimaryKey(AdSeatInfo record);
    
    
    int getPageCount(Map<String, Object> searchMap);
    
    List<AdSeatInfo> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
    
    AdSeatInfoVo getAdSeatInfoById(Integer id);
    List<AdSeatInfo> getAdSeatInfoByStreetAndMediaUserId(@Param("userId")  Integer userId, @Param("street")  Long street);
    AdSeatInfo getAdSeatInfoByAdActivitySeatId(@Param("adActivitySeatId")  Integer adActivitySeatId);
    int getCountByAdCode(@Param("adSeatCode")String adSeatCode);
    int insertBatchByExcel(@Param("adSeatInfos")List<AdSeatInfo> adSeatInfos);
    
    List<AdSeatInfo> getAdSeatByPointAround(@Param("lon")Double lon,@Param("lat")Double lat,@Param("metre")Double metre,@Param("metreDegree")Double metreDegree);
    List<AdSeatInfo> getAdSeatByMediaId(@Param("mediaId")  Integer mediaId);
    
    List<CountGroupByCityVo> getCountGroupByCity(HeatMapVo heatMapVo);
    List<AdSeatInfo> getAllLonLat(HeatMapVo heatMapVo);

	int updateFlag(@Param("codeFlag")Integer codeFlag,@Param("id") Integer id);
}