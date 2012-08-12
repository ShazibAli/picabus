package com.zdm.picabus.db;

import java.util.Set;

import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Stop;

public interface IDBServices {

	/**
	 * 
	 * @param lineNumber
	 *            the line number 
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param clientTime
	 *            the time of the client (can differ from the server time)
	 * 
	 * @return bus line containing the relevant data
	 */
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude, double longitude, String clientTimeString);
	

	/**
	 * 
	 * find the closest stop to the given latitude/longitude coordination (using "Haversine" formula).
	 * 
	 * 
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param isRecursive
	 *            when set to true, search will double the radius in each iteration until a station will be found (default: false) 
	 * @param maxNumOfIterations
	 *            maximum number of iterations when the search is recursive (each iteration is a query)            
	 * 
	 * @return bus stop containing the relevant data
	 */
	public Stop getNearestStop(double latitude, double longitude, boolean isRecursive, int maxNumOfIterations);
	
	
	/**
	 * 
	 * @param tripId
	 *            the id of the chosen trip
	 * @param currentStopSequenceNumber
	 *            the stop sequence number of the user current location
	 * @param departureTimeString
	 *            the exact time which this trip is leaving the station
	 * @return return all the stations which comprises this trip continuation
	 */
	public Set<Stop> getRouteDetails(long tripID,
			int currentStopSequenceNumber,
			String departureTimeString);

}
