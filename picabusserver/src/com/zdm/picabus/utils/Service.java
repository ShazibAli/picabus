package com.zdm.picabus.utils;

/**
 * Enum representing the available services (each contains it's own task name)
 * 
 * 
 */
public enum Service {
	GET_DEPARTURE_TIMES("getDepartureTimes"), 
	GET_ROUTE_DETAILS("getRouteDetails"),
	REPORT_LOCATION("reportLocation"),
	REPORT_CHECKOUT("reportCheckout"),
	REPORT_TEXTUAL_MSG("reportTextualMsg"),
	GET_LAST_REPORTED_LOCATION("getLastReportedLocation"),
	GET_USER_SCORE("getUserScore"),
	GET_TRIP_REPORTS("getTripReports");

	private String taskName;

	private Service(String value) {
		this.taskName = value;
	}

	public String getTaskName() {
		return this.taskName;
	}
}
