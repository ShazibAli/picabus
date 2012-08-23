package com.zdm.picabus.server.entities;

import java.sql.Timestamp;

/**
 * 
 * Represents a single real-time location report
 *
 */
public class RealtimeLocationReport {

	private long tripId;
	private double longitude;
	private double latitude;
	private Timestamp reportTimestamp;
	private boolean isEmpty;
	
	
	public RealtimeLocationReport() {
		isEmpty = true;
	}
	


	/**
	 * @param tripId
	 * @param longitude
	 * @param latitude
	 * @param reportTimestamp
	 * @param isEmpty
	 */
	public RealtimeLocationReport(long tripId, double longitude,
			double latitude, Timestamp reportTimestamp, boolean isEmpty) {
		super();
		this.tripId = tripId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.reportTimestamp = reportTimestamp;
		this.isEmpty = isEmpty;
	}



	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}



	/**
	 * @return the tripId
	 */
	public long getTripId() {
		return tripId;
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
	 * @return the reportTimestamp
	 */
	public Timestamp getReportTimestamp() {
		return reportTimestamp;
	}
	
	
	
}
