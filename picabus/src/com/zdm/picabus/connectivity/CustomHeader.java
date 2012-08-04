package com.zdm.picabus.connectivity;

/**
 * Enum representing the custom headers
 * 
 * @author user
 * 
 */
public enum CustomHeader {
	TASK_NAME("Task-name");

	private String headerName;

	private CustomHeader(String headerName) {
		this.headerName = headerName;
	}

	public String getHeaderName() {
		return this.headerName;
	}
};