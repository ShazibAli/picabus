package com.zdm.picabus.connectivity;

/**
 * Enum representing the available requests (each contains it's task name)
 * 
 * @author user
 * 
 */
public enum Request {
	GET_DEPARTURE_TIMES("getDepartureTimes"), GET_ROUTE_DETAILS("getRoute");

	private String taskName;

	private Request(String value) {
		this.taskName = value;
	}

	public String getTaskName() {
		return this.taskName;
	}
};



