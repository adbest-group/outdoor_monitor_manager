package com.bt.om.service;

import com.bt.om.entity.MonitorDailyReport;

import java.util.Map;

/**
 * Created by caiting on 2018/3/13.
 */
public interface IMonitorDailyReportService {

    MonitorDailyReport getSumReport(Map condition);

    MonitorDailyReport getTodaySumReport(MonitorDailyReport report);

    MonitorDailyReport getWeekLastSumReport(Map condition);
}
