package com.zdm.picabus.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.connectivity.tasks.GetDepartureTimeTask;
import com.zdm.picabus.connectivity.tasks.GetLastReportedLocationTask;
import com.zdm.picabus.connectivity.tasks.GetRouteDetailsTask;
import com.zdm.picabus.connectivity.tasks.GetUserScoreTask;
import com.zdm.picabus.connectivity.tasks.HttpAbstractTask;
import com.zdm.picabus.connectivity.tasks.ReportTask;
import com.zdm.picabus.connectivity.tasks.Tasks;

import android.app.ProgressDialog;
import android.content.Context;

public class HttpCaller implements IHttpCaller {

	private static HttpCaller instance = null;
	
	private static final String publicServerURLOld = "http://picabusapp.appspot.com/picabusserver";
	private static final String publicServerURL = "http://picabusapp.appspot.com/";
	private static final String localServerURL = "http://10.0.0.2:8888/";
	private static final String SERVICES = "services";
	private static final String REPORTS = "reports";
		
	private HttpCaller() {
		
	}
	
	public static synchronized HttpCaller getInstance() {
		if (instance == null) {
			instance = new HttpCaller();
		}
		return instance;
	}

	
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
	
	public void getDepartureTime(Context mContext, ProgressDialog waitSpinner, final int lineNumber, final double latitude, final double longitude, final String clientTime, final int timeInterval) {
 
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
		hat.execute(publicServerURLOld, null, null);


	}
	
	public void getRouteDetails(Context mContext, ProgressDialog waitSpinner, int currentStopSequenceNumber, long tripID) {
		 
		JSONObject requestPayload = new JSONObject();
		try {
			requestPayload.put("currentStopSequenceNumber", currentStopSequenceNumber);
			requestPayload.put("tripID", tripID);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new GetRouteDetailsTask(mContext, waitSpinner, Tasks.GET_ROUTE_DETAILS.getTaskName(), requestPayload);
		hat.execute(publicServerURLOld, null, null);


	}

	public void reportCheckin(Context mContext, ProgressDialog waitSpinner, String userId, double longitude, double latitude,
			long tripId) {
		
		JSONObject requestPayload = new JSONObject();
		try {		
			requestPayload.put("userId", userId);
			requestPayload.put("latitude", latitude);
			requestPayload.put("longitude", longitude);
			requestPayload.put("tripId", tripId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new ReportTask(mContext, waitSpinner, Tasks.REPORT_LOCATION.getTaskName(), requestPayload);
		hat.execute(localServerURL + REPORTS, null, null);
	}

	public void reportCheckout(Context mContext, ProgressDialog waitSpinner, String userId, long tripId) {
		JSONObject requestPayload = new JSONObject();
		try {		
			requestPayload.put("userId", userId);
			requestPayload.put("tripId", tripId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new ReportTask(mContext, waitSpinner, Tasks.REPORT_CHECKOUT.getTaskName(), requestPayload);
		hat.execute(localServerURL + REPORTS, null, null);	
	}

	public void reportTripDescription(Context mContext, ProgressDialog waitSpinner, String userId, long tripId, String message) {
		JSONObject requestPayload = new JSONObject();
		try {		
			requestPayload.put("userId", userId);
			requestPayload.put("tripId", tripId);
			requestPayload.put("reportMessage", message);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new ReportTask(mContext, waitSpinner, Tasks.REPORT_TEXTUAL_MSG.getTaskName(), requestPayload);
		hat.execute(localServerURL + REPORTS, null, null);	
	}

	public void getUserScore(Context mContext, ProgressDialog waitSpinner, String userId) {
		
		JSONObject requestPayload = new JSONObject();
		try {		
			requestPayload.put("userId", userId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new GetUserScoreTask(mContext, waitSpinner, Tasks.GET_USER_SCORE.getTaskName(), requestPayload);
		hat.execute(localServerURL + REPORTS, null, null);
	}

	public void getLastReportedLocation(Context mContext, ProgressDialog waitSpinner, long tripId) {
		JSONObject requestPayload = new JSONObject();
		try {
			requestPayload.put("tripId", tripId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		};
		
		HttpAbstractTask hat = new GetLastReportedLocationTask(mContext, waitSpinner, Tasks.GET_LAST_REPORTED_LOCATION.getTaskName(), requestPayload);
		hat.execute(localServerURL + REPORTS, null, null);

		
	}



}
