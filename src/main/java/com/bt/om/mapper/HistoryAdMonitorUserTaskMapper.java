package com.bt.om.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bt.om.entity.AdMonitorUserTask;
import com.bt.om.entity.HistoryAdMonitorUserTask;
import com.bt.om.entity.vo.AbandonTaskVo;

public interface HistoryAdMonitorUserTaskMapper {
    int insertSelective(HistoryAdMonitorUserTask record);

	int insertBatch(@Param("tableName") String tableName,@Param("adMonitorUserTasks") List<HistoryAdMonitorUserTask> adMonitorUserTasks);

}