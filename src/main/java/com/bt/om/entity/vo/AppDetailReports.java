package com.bt.om.entity.vo;

import java.util.List;

public class AppDetailReports {

	private List<AppDetailReport> notStartReport;
	private List<AppDetailReport> monitorReport;
	private List<AppDetailReport> problemReport;
	
	public List<AppDetailReport> getNotStartReport() {
		return notStartReport;
	}
	public void setNotStartReport(List<AppDetailReport> notStartReport) {
		this.notStartReport = notStartReport;
	}
	public List<AppDetailReport> getMonitorReport() {
		return monitorReport;
	}
	public void setMonitorReport(List<AppDetailReport> monitorReport) {
		this.monitorReport = monitorReport;
	}
	public List<AppDetailReport> getProblemReport() {
		return problemReport;
	}
	public void setProblemReport(List<AppDetailReport> problemReport) {
		this.problemReport = problemReport;
	}
}
