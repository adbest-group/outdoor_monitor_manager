package com.bt.om.service.impl;

import com.bt.om.entity.MonitorDailyReport;
import com.bt.om.mapper.MonitorDailyReportMapper;
import com.bt.om.service.IMonitorDailyReportService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by caiting on 2018/3/13.
 */
@Service
public class MonitorDailyReportService implements IMonitorDailyReportService {

    @Autowired
    private MonitorDailyReportMapper monitorDailyReportMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public MonitorDailyReport getSumReport(Map condition) {
        return monitorDailyReportMapper.getSumReport(condition);
    }

    @Override
    public MonitorDailyReport getTodaySumReport(MonitorDailyReport report) {
        return monitorDailyReportMapper.getTodaySumReport(report);
    }

    @Override
    public MonitorDailyReport getWeekLastSumReport(Map condition) {
        return monitorDailyReportMapper.getWeekLastSumReport(condition);
    }
}
