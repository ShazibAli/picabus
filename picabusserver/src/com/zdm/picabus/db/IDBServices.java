package com.zdm.picabus.db;

import java.util.List;

import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Stop;
import com.zdm.picabus.server.exceptions.EmptyResultException;

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
	 * @throws EmptyResultException thrown when no result was found
	 */
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude, double longitude, String clientTimeString, int timeIntervalInMinutes) throws EmptyResultException;
	

	/**
	 * 
	 * Find the closest stop to the given latitude/longitude coordination (using "Haversine" formula).
	 * 
	 * 
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param isRecursive
	 *            when set to true, search will double the radius in each iteration until a station will be found (default: false) 
	 * @param maxNumOfIterations
	 *            maximum number of iterations when the search is recursive (each iteration translates to a query)            
	 * 
	 * @return bus stop containing the relevant data
	 * @throws EmptyResultException thrown when no result was found
	 */
	public Stop getNearestStop(double latitude, double longitude, boolean isRecursive, int maxNumOfIterations) throws EmptyResultException;
	
	
	/**
	 * 
	 * @param tripId
	 *            the id of the chosen trip
	 * @param currentStopSequenceNumber
	 *            the stop sequence number of the user current location
	 * @return return all the stations which comprises this trip continuation
	 * @throws EmptyResultException thrown when no result was found
	 */
	public List<Stop> getRouteDetails(long tripID,
			int currentStopSequenceNumber) throws EmptyResultException;
	
	public void increaseUserPoints(long userId,int numOfPints);
	
	public void updateCurrentLocationReport (long tripId, double longitude, double latitude, long reporterId);
	
	public void clearReport(long tripId);
	
	
	
	
	
	
	
	

}
