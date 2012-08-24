package com.zdm.picabus.connectivity;

import android.app.ProgressDialog;
import android.content.Context;

public interface IHttpCaller {

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param lineNumber
	 *            The line number
	 * @param latitude
	 *            Latitude coordinate from which the user is searching
	 * @param longitude
	 *            Longitude coordinate from which the user is searching
	 * @param clientTime
	 *            The client time
	 * @param timeInterval
	 *            The delta used for searching next departures
	 */
	public void getDepartureTime(Context mContext, ProgressDialog waitSpinner,
			final int lineNumber, final double latitude,
			final double longitude, final String clientTime,
			final int timeInterval);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param currentStopSequenceNumber
	 *            Sequence number of the stop in this trip
	 * @param tripID
	 *            Trip id
	 */
	public void getRouteDetails(Context mContext, ProgressDialog waitSpinner,
			int currentStopSequenceNumber, long tripID);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param userId
	 *            User id
	 * @param longitude
	 *            Longitude coordinate from which the user is reporting
	 * @param latitude
	 *            Latitude coordinate from which the user is reporting
	 * @param tripId
	 *            Trip id
	 */
	public void reportCheckin(Context mContext, ProgressDialog waitSpinner,
			String userId, double longitude, double latitude, long tripId);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param userId
	 *            User id
	 * @param tripId
	 *            Trip id
	 */
	public void reportCheckout(Context mContext, ProgressDialog waitSpinner,
			String userId, long tripId);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param userId
	 *            User id
	 * @param tripId
	 *            Trip id
	 * @param message
	 *            Report message depicting the trip
	 */
	public void reportTripDescription(Context mContext,
			ProgressDialog waitSpinner, String userId, long tripId, String message);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param userId
	 *            User id
	 */
	public void getUserScore(Context mContext, ProgressDialog waitSpinner,
			String userId);

	/**
	 * 
	 * @param mContext
	 *            Current activity context
	 * @param waitSpinner
	 *            Wait spinner reference
	 * @param tripId
	 *            Trip id
	 */
	public void getLastReportedLocation(Context mContext,
			ProgressDialog waitSpinner, long tripId);
}
