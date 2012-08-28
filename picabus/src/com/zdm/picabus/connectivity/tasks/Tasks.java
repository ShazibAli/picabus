package com.zdm.picabus.connectivity.tasks;

/**
 * Enum representing the available requests (each contains it's task name)
 * 
 * @author user
 * 
 */
public enum Tasks {
	GET_DEPARTURE_TIMES("getDepartureTimes"), 
	GET_ROUTE_DETAILS("getRouteDetails"),
	REPORT_LOCATION("reportLocation"),
	REPORT_CHECKOUT("reportCheckout"),
	REPORT_TEXTUAL_MSG("reportTextualMsg"), 
	GET_LAST_REPORTED_LOCATION("getLastReportedLocation"),
	GET_USER_SCORE("getUserScore");

	private String taskName;

	private Tasks(String value) {
		this.taskName = value;
	}

	public String getTaskName() {
		return this.taskName;
	}
};



