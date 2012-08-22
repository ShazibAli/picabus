package com.zdm.picabus.db;

import java.util.List;

import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Stop;
import com.zdm.picabus.server.exceptions.EmptyResultException;

public interface IDBServices {

	/**
	 * Return the trips with all the required information.
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
	 *            specifying the interval in minutes from the client time in
	 *            which the user wants to see the results
	 * 
	 * @return bus line containing the relevant data
	 * @throws EmptyResultException
	 *             thrown when no result was found
	 */
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude,
			double longitude, String clientTimeString, int timeIntervalInMinutes)
			throws EmptyResultException;

	/**
	 * Find the closest stop to the given latitude/longitude coordination (using
	 * "Haversine" formula).
	 * 
	 * 
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param isRecursive
	 *            when set to true, search will double the radius in each
	 *            iteration until a station will be found (default: false)
	 * @param maxNumOfIterations
	 *            maximum number of iterations when the search is recursive
	 *            (each iteration translates to a query)
	 * 
	 * @return bus stop containing the relevant data
	 * @throws EmptyResultException
	 *             thrown when no result was found
	 */
	public Stop getNearestStop(double latitude, double longitude,
			boolean isRecursive, int maxNumOfIterations)
			throws EmptyResultException;

	/**
	 * Getting the details of a specific trip (basically, what are the stations
	 * on the rest of the trip)
	 * 
	 * @param tripId
	 *            the id of the chosen trip
	 * @param currentStopSequenceNumber
	 *            the stop sequence number of the user current location
	 * @return return all the stations which comprises this trip continuation
	 * @throws EmptyResultException
	 *             thrown when no result was found
	 */
	public List<Stop> getRouteDetails(long tripID, int currentStopSequenceNumber)
			throws EmptyResultException;

	/**
	 * Increase user's points 
	 * 
	 * @param userId
	 *            the user's id
	 * @param numOfPoints
	 *            number of points we want to add for this user
	 * @return current number of points of this user (after the raise)
	 * 
	 */
	public long increaseUserPoints(long userId, int numOfPoints);

	/**
	 * Reporting current location of a user on a specific trip.
	 * 
	 * @param userId
	 *            the user's id
	 * @param logitude
	 *            current longitude coordinate
	 * @param latitude
	 *            current latitude coordinate
	 * @param tripId
	 *            the trip's id
	 * @return true if the operation was successful, false otherwise 
	 */
	public boolean reportCurrentLocation(long userId, double logitude,
			double latitude, long tripId);

	/**
	 * Report a checkout of a user. This call results in clearing all the user's
	 * reports on this trip
	 * 
	 * @param userId
	 *            the user's id
	 * @param tripId
	 *            the trip's id
	 * @return true if the operation was successful, false otherwise
	 */
	public boolean reportCheckout(long userId, long tripId);

	/**
	 * Enter a report message for this specific trip
	 * 
	 * @param userId
	 *            the user's id
	 * @param tripId
	 *            the trip's id
	 * @param reportMessage
	 *            free text description for the given trip 
	 *            (max length: 200 characters)
     * @return true if the operation was successful, false otherwise
	 */
	public boolean reportTripDescription(long userId, long tripId,
			String reportMessage);

}
