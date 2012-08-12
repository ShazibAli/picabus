package com.zdm.picabus.server;

import com.google.gson.JsonObject;
import com.zdm.picabus.db.DBServices;
import com.zdm.picabus.db.IDBServices;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Trip;

public class RequestHandler {

	
	
	public JsonObject getDepartueTimePerLine(int lineNumber, double lat, double lng,
			String clientTimeString) {

		IDBServices idbs = new DBServices();
		Line retrievedLine = idbs.getNextDepartureTimePerLine(lineNumber, lat, lng, clientTimeString); 
		
		int tripCounter = -1;
		JsonObject currentTripInner;
		JsonObject line = new JsonObject();
		JsonObject data = new JsonObject();
		line.addProperty("stopHeadsign", retrievedLine.getStopHeadsign());
		line.addProperty("bidirectional", retrievedLine.isBiDirectional());
		
		for (Trip trip : retrievedLine.getTrips()) {
			tripCounter++;
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
			String tripFN = "trip" + tripCounter;
			line.add(tripFN, currentTripInner);
		}
		line.addProperty("tripCount", tripCounter+1);
		data.add("data", line);
		
		return data;
	}

	public JsonObject getRouteDetails(long tripID,
			int currentStopSequenceNumber, 
			String departureTimeString) {
		// TODO Auto-generated method stub
		return null;
	}

}
