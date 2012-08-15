package com.zdm.picabus.enitities;

public class Stop {
	
	private long stopID;
	private int stopCode;
	private String stopName;
	private String stopDescription;
	private double latitude;
	private double longitude;
	private int stopSequenceNumber;
	private String departureTimeString;
	
	
	public Stop() {
	}
	
	
	
	/**
	 * @param stopID
	 * @param stopCode
	 * @param stopName
	 * @param stopDescription
	 * @param latitude
	 * @param longitude
	 * @param stopSequenceNumber
	 */
	public Stop(long stopID, int stopCode, String stopName,
			String stopDescription, double latitude, double longitude,
			int stopSequenceNumber) {
		super();
		this.stopID = stopID;
		this.stopCode = stopCode;
		this.stopName = stopName;
		this.stopDescription = stopDescription;
		this.latitude = latitude;
		this.longitude = longitude;
		this.stopSequenceNumber = stopSequenceNumber;
		this.departureTimeString = null;
	}



	/**
	 * @return the departureTimeString
	 */
	public String getDepartureTimeString() {
		return departureTimeString;
	}



	/**
	 * @param departureTimeString the departureTimeString to set
	 */
	public void setDepartureTimeString(String departureTimeString) {
		this.departureTimeString = departureTimeString;
	}



	/**
	 * @return the stopSequenceNumber
	 */
	public int getStopSequenceNumber() {
		return stopSequenceNumber;
	}



	/**
	 * @param stopSequenceNumber the stopSequenceNumber to set
	 */
	public void setStopSequenceNumber(int stopSequenceNumber) {
		this.stopSequenceNumber = stopSequenceNumber;
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
