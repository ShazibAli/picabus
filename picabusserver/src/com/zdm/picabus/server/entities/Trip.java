package com.zdm.picabus.server.entities;

import java.util.Calendar;

public class Trip {

	private int tripID;
	private String destination;
	private int directionID;
	private int lineNumber;
	private Calendar eta;
	private Company company;
	private long stopID;
	


	/**
	 * @param tripID
	 * @param destination
	 * @param directionID
	 * @param lineNumber
	 * @param eta
	 * @param company
	 * @param stopID
	 */
	public Trip(int tripID, String destination, int directionID,
			int lineNumber, Calendar eta, Company company, long stopID) {
		super();
		this.tripID = tripID;
		this.destination = destination;
		this.directionID = directionID;
		this.lineNumber = lineNumber;
		this.eta = eta;
		this.company = company;
		this.stopID = stopID;
	}

	/**
	 * @return the tripID
	 */
	public int getTripID() {
		return tripID;
	}

	/**
	 * @param tripID the tripID to set
	 */
	public void setTripID(int tripID) {
		this.tripID = tripID;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the directionID
	 */
	public int getDirectionID() {
		return directionID;
	}

	/**
	 * @param directionID the directionID to set
	 */
	public void setDirectionID(int directionID) {
		this.directionID = directionID;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the eta
	 */
	public Calendar getEta() {
		return eta;
	}

	/**
	 * @param eta the eta to set
	 */
	public void setEta(Calendar eta) {
		this.eta = eta;
	}

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
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

	

}
