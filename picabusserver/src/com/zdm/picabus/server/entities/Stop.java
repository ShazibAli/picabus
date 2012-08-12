package com.zdm.picabus.server.entities;

public class Stop {
	
	private long stopID;
	private int stopCode;
	private String stopName;
	private String stopDescription;
	private double latitude;
	private double longitude;
	
	
	
	public Stop() {
	}
	
	/**
	 * @param stopID
	 * @param stopCode
	 * @param stopName
	 * @param stopDescription
	 * @param latitude
	 * @param longitude
	 */
	public Stop(long stopID, int stopCode, String stopName,
			String stopDescription, double latitude, double longitude) {
		super();
		this.stopID = stopID;
		this.stopCode = stopCode;
		this.stopName = stopName;
		this.stopDescription = stopDescription;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return the stopID
	 */
	public long getStopID() {
		return stopID;
	}

	/**
	 * @param stopID the stopID to set
	 */
	public void setStopID(long stopID) {
		this.stopID = stopID;
	}

	/**
	 * @return the stopCode
	 */
	public int getStopCode() {
		return stopCode;
	}

	/**
	 * @param stopCode the stopCode to set
	 */
	public void setStopCode(int stopCode) {
		this.stopCode = stopCode;
	}

	/**
	 * @return the stopDescription
	 */
	public String getStopDescription() {
		return stopDescription;
	}

	/**
	 * @param stopDescription the stopDescription to set
	 */
	public void setStopDescription(String stopDescription) {
		this.stopDescription = stopDescription;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the stopName
	 */
	public String getStopName() {
		return stopName;
	}

	/**
	 * @param stopName the stopName to set
	 */
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	
	
	
	
}
