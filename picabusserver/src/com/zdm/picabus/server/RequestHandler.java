package com.zdm.picabus.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.zdm.picabus.db.DBServices;
import com.zdm.picabus.db.IDBServices;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.RealtimeLocationReport;
import com.zdm.picabus.server.entities.Report;
import com.zdm.picabus.server.entities.Stop;
import com.zdm.picabus.server.entities.Trip;
import com.zdm.picabus.server.exceptions.EmptyResultException;

public class RequestHandler {

	private static final String TRIP_PREFIX = "trip";
	private static final String STOP_PREFIX = "stop";
	
	private static final int NUMBER_OF_POINTS_PER_LOCATION_REPORT = 2;
	private static final int NUMBER_OF_POINTS_PER_TEXTUAL_REPORT = 5;
	
	private IDBServices idbs = null;
	
	public RequestHandler() {
		idbs = new DBServices();
	}
	public JsonObject getDepartueTimePerLine(int lineNumber, double lat, double lng,
			String clientTimeString, int timeIntervalInMinutes) throws EmptyResultException {

		Line retrievedLine = idbs.getNextDepartureTimePerLine(lineNumber, lat, lng, clientTimeString, timeIntervalInMinutes); 
		
		// validity check
		if (retrievedLine == null) {
			return null;
		}
		
		int tripCounter = 0;
		JsonObject currentTripInner;
		JsonObject line = new JsonObject();
		JsonObject data = new JsonObject();
		line.addProperty("stopHeadsign", retrievedLine.getStopHeadsign());
		line.addProperty("bidirectional", retrievedLine.isBiDirectional());
		
		for (Trip trip : retrievedLine.getTrips()) {
			currentTripInner = new JsonObject();
			currentTripInner.addProperty("direction", trip.getDirectionID());
			currentTripInner.addProperty("id", trip.getTripID());
			currentTripInner.addProperty("destination", trip.getDestination());
			currentTripInner.addProperty("lineNumber", trip.getLineNumber());
			currentTripInner.addProperty("eta", trip.getEta().toString());
			currentTripInner.addProperty("companyName", trip.getCompany().getCompanyName());
			currentTripInner.addProperty("stopID", trip.getStopID());
			currentTripInner.addProperty("stopSequence", trip.getStopSequence());
			currentTripInner.addProperty("serviceID", trip.getServiceID());
			currentTripInner.addProperty("routeID", trip.getRouteID());	
			String tripFN = TRIP_PREFIX + tripCounter;
			line.add(tripFN, currentTripInner);
			tripCounter++;
		}
		line.addProperty("tripCount", tripCounter);
		data.add("data", line);
		
		return data;
	}

	public JsonObject getRouteDetails(long tripID, int currentStopSequenceNumber) throws EmptyResultException {
		List<Stop> retrievedStops = idbs.getRouteDetails(tripID, currentStopSequenceNumber); 
		RealtimeLocationReport currentBusLocation = idbs.getRealtimeLocation(tripID);
		// validity check
		if (retrievedStops == null || currentBusLocation == null) {
			return null;
		} 

		JsonObject stopsWrapper = new JsonObject();
		JsonObject realtimeLocationWrapper = new JsonObject();
		JsonObject currentStopInner;
		JsonObject data = new JsonObject();
		
		// creating the stops list 
		for (int i=0; i < retrievedStops.size(); i++) {
			Stop stop = retrievedStops.get(i);
			currentStopInner = new JsonObject();
			currentStopInner.addProperty("stopName", stop.getStopName());
			currentStopInner.addProperty("stopAddress", stop.getStopDescription());
			currentStopInner.addProperty("stopSequence", stop.getStopSequenceNumber());
			currentStopInner.addProperty("latitude", stop.getLatitude());
			currentStopInner.addProperty("longitude", stop.getLongitude());
			currentStopInner.addProperty("departureTime", stop.getDepartureTimeString());
			currentStopInner.addProperty("stop_code", stop.getStopCode());
			String stopFN = STOP_PREFIX + i;	
			stopsWrapper.add(stopFN, currentStopInner);
		} 
		
		stopsWrapper.addProperty("stopCount", retrievedStops.size());
		data.add("data", stopsWrapper);
		
		// creating the real time location report
		if (currentBusLocation.isEmpty()) { // relevant report doesn't exists 
			realtimeLocationWrapper.addProperty("available", false);
		}
		else { // relevant report exists
			realtimeLocationWrapper.addProperty("available", true);
			realtimeLocationWrapper.addProperty("longitude", currentBusLocation.getLongitude());
			realtimeLocationWrapper.addProperty("latitude", currentBusLocation.getLatitude());
			realtimeLocationWrapper.addProperty("reportTimestampString", currentBusLocation.getReportTimestamp().toString());
		}
		data.add("realtimeLocation", realtimeLocationWrapper);
		return data;
	}

	
	public Long reportLocation(final Long userIdValue, Double latValue,
			Double lngValue, Long tripIdValue) {
		
/*		// update user's score (in a separate thread)
		Thread increasePoints = new Thread(new Runnable() {
			@Override
			public void run() {
				idbs.increaseUserPoints(userIdValue, NUMBER_OF_POINTS_PER_REPORT);
			}
		});
		increasePoints.run();
*/		
		Long currentNumberOfPoints = idbs.increaseUserPoints(userIdValue, NUMBER_OF_POINTS_PER_LOCATION_REPORT);
		boolean opResult = idbs.reportCurrentLocation(userIdValue, lngValue, latValue, tripIdValue);
		return (opResult == true ? currentNumberOfPoints : -1);
	}

	public Long reportCheckout(Long userIdValue, Long tripIdValue) { 
		// get user's current number of points
		Long currentNumberOfPoints = idbs.getPointStatus(userIdValue);
		boolean opResult = idbs.reportCheckout(userIdValue,tripIdValue);
		return (opResult == true ? currentNumberOfPoints : -1);
	}

	public Long reportTextualMessage(Long userIdValue, Long tripIdValue,
			String reportMessageValue) {
		Long currentNumberOfPoints = idbs.increaseUserPoints(userIdValue, NUMBER_OF_POINTS_PER_TEXTUAL_REPORT);
		boolean opResult = idbs.reportTripDescription(userIdValue,tripIdValue, reportMessageValue);
		return (opResult == true ? currentNumberOfPoints : -1);
	}

	public JsonObject getRealtimeLocation(Long tripIdValue) throws EmptyResultException {

		RealtimeLocationReport rlr = idbs.getRealtimeLocation(tripIdValue);
		JsonObject data = null;
		JsonObject response = null;
		if (rlr == null) {
			return null;
		}
		else if (rlr.isEmpty()) {
			throw new EmptyResultException();
		}
		else {
			response = new JsonObject();
			data = new JsonObject();
			data.addProperty("longitude", rlr.getLongitude());
			data.addProperty("latitude", rlr.getLatitude());
			data.addProperty("time_stamp_string", rlr.getReportTimestamp().toString());
			response.add("data", data);
		}
		
		return response;
	}
	public JsonObject getUserScore(Long userIdValue) {
		Long points = idbs.getPointStatus(userIdValue);
		if (points != null) {
			JsonObject data = new JsonObject();
			JsonObject response = new JsonObject();
			data.addProperty("points", points);
			response.add("data", data);
			return response;
		}
		return null;
	}
	public JsonObject getTripReports(Long tripIdValue) {
		ArrayList<Report> reports = idbs.getTextualReports(tripIdValue);
		if (reports != null) {
			JsonObject response = new JsonObject();
			JsonObject currentReport;
			int reportCount = 0;
			
			for (int i = 0; i < reports.size(); i++) {
				Report currentReportObj = reports.get(i);
				currentReport = new JsonObject();
				currentReport.addProperty("reporterId", currentReportObj.getReporterId());
				currentReport.addProperty("reportTime", currentReportObj.getReportTimeString());
				currentReport.addProperty("report", currentReportObj.getReport());
				response.add("report_" + reportCount, currentReport);
				reportCount++;
			}
			response.addProperty("numOfReports", reportCount);
			return response;
			
		}
		return null;
	}

	

}
