package com.zdm.picabus.enitities;

public class ReportResult {
	
	private String taskName;
	private boolean isSuccess;
	private long currentNumOfPoints;
	private boolean isEmpty;
	
	public ReportResult() {
		this.isEmpty = true;
	}
	

	/**
	 * @param taskName
	 * @param isSuccess
	 * @param currentNumOfPoints
	 * @param isEmpty
	 */
	public ReportResult(String taskName, boolean isSuccess,
			long currentNumOfPoints, boolean isEmpty) {
		super();
		this.taskName = taskName;
		this.isSuccess = isSuccess;
		this.currentNumOfPoints = currentNumOfPoints;
		this.isEmpty = isEmpty;
	}

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}
	/**
	 * @return the isSuccess
	 */
	public boolean isSuccess() {
		return isSuccess;
	}
	/**
	 * @return the currentNumOfPoints
	 */
	public long getCurrentNumOfPoints() {
		return currentNumOfPoints;
	}
	
	
	
}
