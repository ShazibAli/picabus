package com.zdm.picabus.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

public class HttpCaller {

	public static final String publicServerURL = "http://picabusapp.appspot.com/picabusserver";
	public static final String localServerURL = "http://10.0.0.1:8888/picabusserver";

	public static String readContentFromIS(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(in, "UTF-8"), 8);
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		return sb.toString();		
	}
	
	public static void getDepartureTime(Context mContext, ProgressDialog waitSpinner, final int lineNumber, final double latitude, final double longitude, final String clientTime, final int timeInterval) {
 
		JSONObject requestPayload = new JSONObject();
		try {
			requestPayload.put("lineNumber", lineNumber);
			requestPayload.put("latitude", latitude);
			requestPayload.put("longitude", longitude);
			requestPayload.put("clientTime", clientTime);
			requestPayload.put("timeInterval", timeInterval);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new GetDepartureTimeTask(mContext, waitSpinner, Tasks.GET_DEPARTURE_TIMES.getTaskName(), requestPayload);
		hat.execute(publicServerURL ,null,null);


	}
	
	public static void getRouteDetails(Context mContext, ProgressDialog waitSpinner, int currentStopSequenceNumber, long tripID) {
		 
		JSONObject requestPayload = new JSONObject();
		try {
			requestPayload.put("currentStopSequenceNumber", currentStopSequenceNumber);
			requestPayload.put("tripID", tripID);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new GetRouteDetailsTask(mContext, waitSpinner, Tasks.GET_ROUTE_DETAILS.getTaskName(), requestPayload);
		hat.execute(publicServerURL ,null,null);


	}



}
