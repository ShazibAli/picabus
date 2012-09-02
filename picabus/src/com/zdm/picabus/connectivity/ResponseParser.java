package com.zdm.picabus.connectivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.RealtimeLocationReport;
import com.zdm.picabus.enitities.Report;
import com.zdm.picabus.enitities.ReportResult;
import com.zdm.picabus.enitities.Stop;
import com.zdm.picabus.enitities.Trip;

public class ResponseParser implements IResponseParser {

	public Line parseGetDepJsonResponse(JSONObject responseJson) {
		
		int tripCount;
		String stopHeadsign;
		boolean bidirectional;
		Line line = new Line();
		List<Trip> trips = new ArrayList<Trip>(); 
		
		try {
			// check if the response is empty
			if (responseJson.getString("data").equalsIgnoreCase("empty")) {
				return null;
			}
			
			JSONObject data = responseJson.getJSONObject("data");
			stopHeadsign = data.getString("stopHeadsign");
			bidirectional = data.getBoolean("bidirectional");
			tripCount = data.getInt("tripCount");
			
			JSONObject currentTrip;
			String currentTripIdentifier;
			
			// single trip values holders
			int directionID;
			long tripID;
			String destination;
			int lineNumber;
			String eta;
			String companyName;
			long stopID;
			int stopSequence;
			long serviceID;
			long routeID;
			
			for (int i = 0; i < tripCount; i++) {
				currentTripIdentifier = "trip" + i;
				currentTrip = data.getJSONObject(currentTripIdentifier);
				// extracting current trip data
				directionID = currentTrip.getInt("direction");
				tripID = currentTrip.getLong("id");
				destination = currentTrip.getString("destination");
				lineNumber = currentTrip.getInt("lineNumber");
				eta = currentTrip.getString("eta");
				companyName = currentTrip.getString("companyName");
				stopID = currentTrip.getLong("stopID");
				stopSequence = currentTrip.getInt("stopSequence");
				serviceID = currentTrip.getLong("serviceID");
				routeID = currentTrip.getLong("routeID");
				Trip currentTripObject = new Trip(tripID, destination, directionID, lineNumber, eta, Company.getCompanyByString(companyName), stopID, stopSequence, routeID, serviceID);
				trips.add(currentTripObject);
			}
		
			line.setTrips(trips);
			line.setBiDirectional(bidirectional);
			line.setStopHeadsign(stopHeadsign);	
		
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return line;
	}

	public Collection<Stop> parseGetRouteJsonResponse(JSONObject responseJson) {
		List<Stop> stops = new ArrayList<Stop>();
		JSONObject data;
		try {
			// check if the response is empty
			if (responseJson.getString("data").equalsIgnoreCase("empty")) {
				return null;
			}
			data = responseJson.getJSONObject("data");
			int stopCount = data.getInt("stopCount");
			String currentStopIdentifier;
			JSONObject currentStop;
			
			// single stop value holders
			String stopName;
			String stopDescription;
			int stopSequenceNumber;
			double latitude;
			double longitude;
			String departureTimeString;
			int stopCode;
			
			// handling the stops information
			for (int i = 0; i < stopCount; i++) {
				currentStopIdentifier = "stop" + i;
				currentStop = data.getJSONObject(currentStopIdentifier);
				
				// extracting current trip data
				stopName = currentStop.getString("stopName");
				stopDescription = currentStop.getString("stopAddress");
				stopSequenceNumber = currentStop.getInt("stopSequence");
				latitude = currentStop.getDouble("latitude");
				longitude = currentStop.getDouble("longitude");
				departureTimeString = currentStop.getString("departureTime");
				stopCode = currentStop.getInt("stop_code");
				Stop currentStopObject = new Stop(stopCode, stopName, stopDescription, latitude, longitude, stopSequenceNumber, departureTimeString);
				currentStopObject.setRealtimeReport(false);
				stops.add(currentStopObject);			
			}
			// handling real-time report information
			JSONObject realtimeInfo = responseJson.getJSONObject("realtimeLocation");
			
			// if real time information is available, we add it to the stop list (as marked stop)
			if (realtimeInfo.getBoolean("available") == true) {
				Stop realtimeReportAsStop = new Stop(realtimeInfo.getDouble("latitude"), realtimeInfo.getDouble("longitude"), true, realtimeInfo.getString("reportTimestampString"));
				stops.add(realtimeReportAsStop);
			}
			 

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return stops;
	}

	public long parseGetScoreResponse(JSONObject json) {
		try {
			JSONObject data = json.getJSONObject("data");
			return data.getLong("points");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public RealtimeLocationReport parseRealtimeLocationResponse(JSONObject json) {
		RealtimeLocationReport rlr = null;
		try {
			JSONObject data = json.getJSONObject("data");
			double lng = data.getDouble("longitude");
			double lat = data.getDouble("latitude");
			String timestampString = data.getString("time_stamp_string");
			rlr = new RealtimeLocationReport(lng, lat, timestampString, false);
			
		} catch (JSONException e) {
			e.printStackTrace();
			rlr = new RealtimeLocationReport();
		}
		
		return rlr;
	}

	public ReportResult parseReportResult(JSONObject json) {
		ReportResult reportResult = null;
		try {
			String status = json.getString("status");
			boolean result = (status.equalsIgnoreCase("success") ? true : false);
			String reportType = json.getString("Task-name");
			long currentNumOfPoints = json.getLong("currentPoints");
			
			reportResult = new ReportResult(reportType, result, currentNumOfPoints, false);
			
		} catch (JSONException e) {
			e.printStackTrace();
			reportResult = new ReportResult();
			
		}
		return reportResult;
	}

	public ArrayList<Report> parseTripReports(JSONObject json) {
		ArrayList<Report> reports = new ArrayList<Report>();
		try {
			int numOfReports = json.getInt("numOfReports");
			
			for (int i = 0; i < numOfReports; i++) {
				JSONObject currentReport = json.getJSONObject("report_" + i);
				reports.add(new Report((long) -1, currentReport
						.getLong("reporterId"), currentReport
						.getString("report"), currentReport
						.getString("reportTime")));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			
		}
		return reports;
	}



}
