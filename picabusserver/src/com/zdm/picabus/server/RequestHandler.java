package com.zdm.picabus.server;

import com.google.gson.JsonObject;
import com.zdm.picabus.db.DBServices;
import com.zdm.picabus.db.IDBServices;
import com.zdm.picabus.server.datastore.DataStoreHandler;
import com.zdm.picabus.server.datastore.IDataStoreHandler;
import com.zdm.picabus.server.entities.Line;

public class RequestHandler {

	public JsonObject getDepartueTime(int lineNumber, double lat, double lng,
			String clientTimeString) {

		IDBServices idbs = new DBServices();
		Line requestedLData = idbs.getNextDepartureTimePerLine(lineNumber, lat, lng, clientTimeString); 
		// TODO: handle return value
		
		
		return null;
	}

}
