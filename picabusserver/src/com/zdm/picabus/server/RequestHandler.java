package com.zdm.picabus.server;

import java.util.List;

import com.google.gson.JsonObject;
import com.zdm.picabus.db.DBServices;
import com.zdm.picabus.db.IDBServices;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Stop;
import com.zdm.picabus.server.entities.Trip;

public class RequestHandler {

	private static final String TRIP_PREFIX = "trip";
	private static final String STOP_PREFIX = "stop";
	
	public JsonObject getDepartueTimePerLine(int lineNumber, double lat, double lng,
			String clientTimeString) {

		IDBServices idbs = new DBServices();
		Line retrievedLine = idbs.getNextDepartureTimePerLine(lineNumber, lat, lng, clientTimeString); 
		
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

	public JsonObject getRouteDetails(long tripID, int currentStopSequenceNumber) {
		IDBServices idbs = new DBServices();
		List<Stop> retrievedStops = idbs.getRouteDetails(tripID, currentStopSequenceNumber); 
		JsonObject stopsWrapper = new JsonObject();
		JsonObject currentStopInner;
		JsonObject data = new JsonObject();
		

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
		return data;
	}

}
