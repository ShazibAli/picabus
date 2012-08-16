package com.zdm.picabus.connectivity;

/**
 * Enum representing the available requests (each contains it's task name)
 * 
 * @author user
 * 
 */
public enum Tasks {
	GET_DEPARTURE_TIMES("getDepartureTimes"), GET_ROUTE_DETAILS("getRouteDetails");

	private String taskName;

	private Tasks(String value) {
		this.taskName = value;
	}

	public String getTaskName() {
		return this.taskName;
	}
};



