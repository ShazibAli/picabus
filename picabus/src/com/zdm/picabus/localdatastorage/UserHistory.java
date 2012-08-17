package com.zdm.picabus.localdatastorage;


public class UserHistory {

	private int lineNumber;
	private int direction;
	private String stopName;
	private long stopID;
	private String companyName;
	private double longitude;
	private double latitude;
	

	/**
	 * @param lineNumber
	 * @param direction
	 * @param stopName
	 * @param stopID
	 * @param companyName
	 * @param longitude
	 * @param latitude
	 */
	public UserHistory(int lineNumber, int direction, String stopName,
			long stopID, String companyName, double longitude, double latitude) {
		super();
		this.lineNumber = lineNumber;
		this.direction = direction;
		this.stopName = stopName;
		this.stopID = stopID;
		this.companyName = companyName;
		this.longitude = longitude;
		this.latitude = latitude;
	}


	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}


	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}


	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}


	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}


	/**
	 * @return the stopName
	 */
	public String getStopName() {
		return stopName;
	}


	/**
	 * @return the stopID
	 */
	public long getStopID() {
		return stopID;
	}


	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	
	
	
	
	
}
