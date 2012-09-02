package com.zdm.picabus.enitities;

public class Report {

	private Long tripId;
	private Long reporterId;
	private String report;
	private String reportTimeString;
	


	/**
	 * @param tripId
	 * @param reporterId
	 * @param report
	 * @param reportTimeString
	 */
	public Report(Long tripId, Long reporterId, String report,
			String reportTimeString) {
		super();
		this.tripId = tripId;
		this.reporterId = reporterId;
		this.report = report;
		this.reportTimeString = reportTimeString;
	}
	
	
	/**
	 * @return the reportTimeString
	 */
	public String getReportTimeString() {
		return reportTimeString;
	}


	/**
	 * @param reportTimeString the reportTimeString to set
	 */
	public void setReportTimeString(String reportTimeString) {
		this.reportTimeString = reportTimeString;
	}


	/**
	 * @return the tripId
	 */
	public Long getTripId() {
		return tripId;
	}
	/**
	 * @param tripId the tripId to set
	 */
	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}
	/**
	 * @return the reporterId
	 */
	public Long getReporterId() {
		return reporterId;
	}
	/**
	 * @param reporterId the reporterId to set
	 */
	public void setReporterId(Long reporterId) {
		this.reporterId = reporterId;
	}
	/**
	 * @return the report
	 */
	public String getReport() {
		return report;
	}
	/**
	 * @param report the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}
	
	
}
