package com.zdm.picabus.enitities;

import java.io.Serializable;
import java.util.Collection;

public class Line implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean biDirectional;
	private Collection<Trip> trips;
	private String stopHeadsign;
	
	public Line() {
	}
	

	/**
	 * @param biDirectional
	 * @param trips
	 * @param stopHeadsign
	 */
	public Line(boolean biDirectional, Collection<Trip> trips,
			String stopHeadsign) {
		super();
		this.biDirectional = biDirectional;
		this.trips = trips;
		this.stopHeadsign = stopHeadsign;
	}



	/**
	 * @return the stopHeadsign
	 */
	public String getStopHeadsign() {
		return stopHeadsign;
	}



	/**
	 * @param stopHeadsign the stopHeadsign to set
	 */
	public void setStopHeadsign(String stopHeadsign) {
		this.stopHeadsign = stopHeadsign;
	}



	/**
	 * @return the biDirectional
	 */
	public boolean isBiDirectional() {
		return biDirectional;
	}

	/**
	 * @param biDirectional the biDirectional to set
	 */
	public void setBiDirectional(boolean biDirectional) {
		this.biDirectional = biDirectional;
	}

	/**
	 * @return the trips
	 */
	public Collection<Trip> getTrips() {
		return trips;
	}

	/**
	 * @param trips the trips to set
	 */
	public void setTrips(Collection<Trip> trips) {
		this.trips = trips;
	} 

	/**
	 * 
	 * @return true iff the current line contains no data
	 */
	public boolean isEmpty() {
		return !biDirectional && trips == null && stopHeadsign == null ;
	}
	
}


