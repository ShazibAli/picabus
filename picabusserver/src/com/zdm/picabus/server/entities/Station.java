package com.zdm.picabus.server.entities;

public class Station {
	
	private double latitude;
	private double longitude;
	private String stopName;
	
	public Station(double latitude, double longitude, String stopName) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.stopName = stopName;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	
	
	
	
}
