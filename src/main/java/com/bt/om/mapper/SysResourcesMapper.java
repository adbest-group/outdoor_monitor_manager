package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.bt.om.entity.SysResources;

public interface SysResourcesMapper {

    int deleteByPrimaryKey(Integer id);
    

    int insert(SysResources record);
    
    

    int insertSelective(SysResources record);

    SysResources selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(SysResources record);


    int updateByPrimaryKey(SysResources record);
    
    List<SysResources> getAll();

    List<SysResources> selectByUserId(@Param("userId") Integer userId);
    
    int getPageCount(Map<String, Object> searchMap);

    List<SysResources> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
    
   int  addGroup(SysResources record);
   int updateGroupName(SysResources record);
   int deleteGroup(Integer id);
   List<SysResources> findGroupById(@Param("userId") Integer userId);
   SysResources getByUserId(@Param("userId") Integer userId);
}