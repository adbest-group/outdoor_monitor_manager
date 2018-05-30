package com.bt.om.mapper;

import com.bt.om.entity.AdJiucuoTask;
import com.bt.om.entity.vo.AdJiucuoTaskMobileVo;
import com.bt.om.entity.vo.AdJiucuoTaskVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AdJiucuoTaskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    int insert(AdJiucuoTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    int insertSelective(AdJiucuoTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    AdJiucuoTask selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    int updateByPrimaryKeySelective(AdJiucuoTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ad_jiucuo_task
     *
     * @mbggenerated Sun Jan 21 14:46:36 CST 2018
     */
    int updateByPrimaryKey(AdJiucuoTask record);

    int getPageCount(Map<String, Object> searchMap);

    List<AdJiucuoTaskVo> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);

    AdJiucuoTaskVo selectVoByPrimaryKey(Integer id);

    List<AdJiucuoTaskMobileVo> selectByUserId(@Param("userId")Integer userId);

    List<AdJiucuoTaskVo> selectAllByAssessorId(Map<String, Object> searchMap);
    List<AdJiucuoTaskVo> getTenAdJiucuoTaskVo(Map<String, Object> searchMap);
    int updateAssessorId(Map<String, Object> searchMap);
    int selectCountByActivityAndSeat(Map<String, Object> searchMap);
    
    List<AdJiucuoTask> selectInfoByQrCode(Map<String, Object> searchMap);
    List<AdJiucuoTask> selectInfoByLonLatTitle(Map<String, Object> searchMap);

	List<AdJiucuoTask> selectInfoByMemo(Map<String, Object> searchMap);
}