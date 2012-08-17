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
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + direction;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + lineNumber;
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (stopID ^ (stopID >>> 32));
		result = prime * result
				+ ((stopName == null) ? 0 : stopName.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserHistory other = (UserHistory) obj;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (direction != other.direction)
			return false;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (lineNumber != other.lineNumber)
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (stopID != other.stopID)
			return false;
		if (stopName == null) {
			if (other.stopName != null)
				return false;
		} else if (!stopName.equals(other.stopName))
			return false;
		return true;
	}

	
	
}
