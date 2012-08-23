package com.zdm.picabus.connectivity;

import android.app.ProgressDialog;
import android.content.Context;

public interface IHttpCaller {

	public void getDepartureTime(Context mContext, ProgressDialog waitSpinner, final int lineNumber, final double latitude, final double longitude, final String clientTime, final int timeInterval);
	
	public void getRouteDetails(Context mContext, ProgressDialog waitSpinner, int currentStopSequenceNumber, long tripID);

	public void reportCheckin(long userId, double logitude, double latitude, long tripId);
	
	public void reportCheckout(long userId, long tripId);
	
	public void reportTripDescription(long userId, long tripId, String message);
}
