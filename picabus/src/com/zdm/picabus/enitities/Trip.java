package com.zdm.picabus.enitities;

import java.io.Serializable;
import java.sql.Time;

public class Trip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long tripID;
	private String destination;
	private int directionID;
	private int lineNumber;
	private String eta;
	private Company company;
	private long stopID;
	private int stopSequence;
	private long routeID;
	private long serviceID;
	
	
	public Trip() {
	}


	/**
	 * @param tripID
	 * @param destination
	 * @param directionID
	 * @param lineNumber
	 * @param eta
	 * @param company
	 * @param stopID
	 * @param stopSequence
	 * @param routeID
	 * @param serviceID
	 */
	public Trip(long tripID, String destination, int directionID,
			int lineNumber, String eta, Company company, long stopID,
			int stopSequence, long routeID, long serviceID) {
		super();
		this.tripID = tripID;
		this.destination = destination;
		this.directionID = directionID;
		this.lineNumber = lineNumber;
		this.eta = eta;
		this.company = company;
		this.stopID = stopID;
		this.stopSequence = stopSequence;
		this.routeID = routeID;
		this.serviceID = serviceID;
	}


	/**
	 * @return the stop_sequence
	 */
	public int getStopSequence() {
		return stopSequence;
	}


	/**
	 * @param stopSequence the stop_sequence to set
	 */
	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}


	/**
	 * @return the routeID
	 */
	public long getRouteID() {
		return routeID;
	}


	/**
	 * @param routeID the routeID to set
	 */
	public void setRouteID(long routeID) {
		this.routeID = routeID;
	}


	/**
	 * @return the serviceID
	 */
	public long getServiceID() {
		return serviceID;
	}


	/**
	 * @param serviceID the serviceID to set
	 */
	public void setServiceID(long serviceID) {
		this.serviceID = serviceID;
	}


	/**
	 * @return the tripID
	 */
	public long getTripID() {
		return tripID;
	}

	/**
	 * @param tripID the tripID to set
	 */
	public void setTripID(long tripID) {
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
	public String getEta() {
		return eta;
	}

	/**
	 * @param eta the eta to set
	 */
	public void setEta(String eta) {
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
