package com.zdm.picabus.enitities;

import java.io.Serializable;

public class HistoryObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int lineNumber;
	private String stationName;
	private String companyName;
	private double lat;
	private double lng;
	
	public HistoryObject(int lineNumber,String stationName, String companyName, double lat, double lng) {
		// TODO Auto-generated constructor stub
		this.lineNumber=lineNumber;
		this.stationName=stationName;
		this.companyName=companyName;
		this.lat=lat;
		this.lng=lng;
	}
	
	
	/**
	 * Getter for line number
	 * @return lineNumber
	 */
	public int getLineNumber(){
		return lineNumber;
	}
	
	/**
	 * getter for station name
	 * @return stationName 
	 */
	public String getStationName(){
		return stationName;
	}
	
	/**
	 * Getter for getCompanyName
	 * @return companyName
	 */
	public String getCompanyName(){
		return companyName;
	}
	
	/**
	 * Getter for getLatitude
	 * @return lat
	 */
	public double getLatitude(){
		return lat;
	}
	
	/**
	 * Getter for getLatitude
	 * @return lat
	 */
	public double getLongtitude(){
		return lng;
	}
	
}
