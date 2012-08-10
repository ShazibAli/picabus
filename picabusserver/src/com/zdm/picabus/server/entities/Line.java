package com.zdm.picabus.server.entities;

import java.util.Collection;

public class Line {
	
	private boolean biDirectional;
	private Collection<Trip> trips;
	
	/**
	 * @param biDirectional
	 * @param trips
	 */
	public Line(boolean biDirectional, Collection<Trip> trips) {
		super();
		this.biDirectional = biDirectional;
		this.trips = trips;
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

	
	
}


