package com.zdm.picabus.server.entities;

public class Station {
	
	private double latitude;
	private double longitude;
	private String stopName;
	

	/**
	 * @param latitude
	 * @param longitude
	 * @param stopName
	 */
	public Station(double latitude, double longitude, String stopName) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.stopName = stopName;
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
