package com.zdm.picabus.enitities;

import java.io.Serializable;

public class TripResultObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int lineNumber;
	private String stationName;
	private long tripId;
	private String arrivalTime;
	private int stopSequence;
	
	public TripResultObject(int lineNumber,String stationName, long tripId, String arrivalTime,int stopSequence) {
		// TODO Auto-generated constructor stub
		this.lineNumber=lineNumber;
		this.stationName=stationName;
		this.tripId=tripId;
		this.arrivalTime=arrivalTime;
		this.stopSequence=stopSequence;
	}
	
	
	/**
	 * Getter for stopSequence
	 * @return the stop_sequence
	 */
	public int getStopSequence() {
		return stopSequence;
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
	 * Getter for trip id
	 * @return tripId
	 */
	public long getTripId(){
		return tripId;
	}
	
	/**
	 * Getter for arrivalTime
	 * @return
	 */
	public String getArrivalTime(){
		return arrivalTime;
	}
}
