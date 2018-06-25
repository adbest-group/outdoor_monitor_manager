package com.bt.om.mapper;

import java.util.List;

import com.bt.om.entity.AdMedia;
import org.apache.ibatis.annotations.Param;

public interface AdMediaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    int insert(AdMedia record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    int insertSelective(AdMedia record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    AdMedia selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    int updateByPrimaryKeySelective(AdMedia record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_media
     *
     * @mbggenerated Thu Feb 01 14:08:19 CST 2018
     */
    int updateByPrimaryKey(AdMedia record);
    
    List<AdMedia> getAll();
    
    List<AdMedia> getAvailableAll(); 

    AdMedia selectByUserId(@Param("id") Integer id);
    
    List<AdMedia> selectNameByMediaId(@Param("id")Integer id); 
}