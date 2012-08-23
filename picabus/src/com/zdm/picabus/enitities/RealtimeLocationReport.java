package com.zdm.picabus.enitities;


/**
 * 
 * Represents a single real-time location report
 *
 */
public class RealtimeLocationReport {

	private long tripId;
	private double longitude;
	private double latitude;
	private String reportTimestampString;
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
			double latitude, String reportTimestamp, boolean isEmpty) {
		super();
		this.tripId = tripId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.reportTimestampString = reportTimestamp;
		this.isEmpty = isEmpty;
	}






	/**
	 * @param longitude
	 * @param latitude
	 * @param reportTimestamp
	 * @param isEmpty
	 */
	public RealtimeLocationReport(double longitude, double latitude,
			String reportTimestamp, boolean isEmpty) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.reportTimestampString = reportTimestamp;
		this.isEmpty = isEmpty;
	}



	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}



	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
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
	public String getReportTimestamp() {
		return reportTimestampString;
	}
	
	
	
}
