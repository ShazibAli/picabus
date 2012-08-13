package com.zdm.picabus.db;

import java.util.List;
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
	 * @param timeIntervalInMinutes
	 *            specifying the interval in minutes from the client time in which the user wants to see the results
	 *
	 * @return bus line containing the relevant data
	 */
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude, double longitude, String clientTimeString, int timeIntervalInMinutes);
	

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
	 * @return return all the stations which comprises this trip continuation
	 */
	public List<Stop> getRouteDetails(long tripID,
			int currentStopSequenceNumber);

}
