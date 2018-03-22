package com.bt.om.vo.report;

import com.bt.om.entity.MonitorDailyReport;

import java.io.Serializable;

/**
 * Created by caiting on 2018/3/13.
 */
public class ManageIndexReportVo implements Serializable{
    private MonitorDailyReport yesterday;
    private MonitorDailyReport today;
    private MonitorDailyReport tomorrow;
    private MonitorDailyReport leftWeek;

    public MonitorDailyReport getYesterday() {
        return yesterday;
    }

    public void setYesterday(MonitorDailyReport yesterday) {
        this.yesterday = yesterday;
    }

    public MonitorDailyReport getToday() {
        return today;
    }

    public void setToday(MonitorDailyReport today) {
        this.today = today;
    }

    public MonitorDailyReport getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(MonitorDailyReport tomorrow) {
        this.tomorrow = tomorrow;
    }

    public MonitorDailyReport getLeftWeek() {
        return leftWeek;
    }

    public void setLeftWeek(MonitorDailyReport leftWeek) {
        this.leftWeek = leftWeek;
    }
    //    public static class ManageIndexReportItemVo implements Serializable{
//        private
//    }
}
